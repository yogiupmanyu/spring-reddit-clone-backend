package com.practice.springredditclone.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.practice.springredditclone.dto.AuthenticationResponse;
import com.practice.springredditclone.dto.LoginRequest;
import com.practice.springredditclone.dto.RegisterRequest;
import com.practice.springredditclone.exception.SpringRedditException;
import com.practice.springredditclone.model.NotificationEmail;
import com.practice.springredditclone.model.User;
import com.practice.springredditclone.model.VerificationToken;
import com.practice.springredditclone.repositories.UserRepository;
import com.practice.springredditclone.repositories.VerificationTokenRepository;
import com.practice.springredditclone.util.Constants;

import io.jsonwebtoken.security.InvalidKeyException;

import java.util.Optional;
import java.util.UUID;

import static java.time.Instant.now;

@Service
@AllArgsConstructor
public class AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final VerificationTokenRepository verificationTokenRepository;
	private final MailContentBuilder mailContentBuilder;
	private final MailService mailService;
	private final AuthenticationManager authenticationManager;
	private final JwtProvider jwtProvider;

	@Transactional
	public void signup(RegisterRequest registerRequest) throws SpringRedditException {
		User user = new User();
		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(encodePassword(registerRequest.getPassword()));
		user.setCreated(now());
		user.setEnabled(false);
		userRepository.save(user);
		
		String token = generateVerificationToken(user);
		
		String message = mailContentBuilder.build(
				"Thank you for signing up to Spring Reddit, please click on the below url to activate your account : "
						+ Constants.ACTIVATION_EMAIL + "/" + token);
		System.out.println("Token :: "+token);
		// asynchronous method ++ we can user RabbitMQ/Apache Kafka also here or any message queue
		mailService.sendMail(new NotificationEmail("Please Activate your account", user.getEmail(), message));
	}

	public AuthenticationResponse login(LoginRequest loginRequest) throws InvalidKeyException, SpringRedditException {
		
		Authentication authenticate = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authenticate);
		
		String authenticationToken = jwtProvider.generateToken(authenticate);
		
		return new AuthenticationResponse(authenticationToken, loginRequest.getUsername());
	}
	
	private String generateVerificationToken(User user) {
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(user);
		verificationTokenRepository.save(verificationToken);
		return token;
	}
	
	@Transactional(readOnly = true)
	User getCurrentUser() {
		org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		return userRepository.findByUsername(principal.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
	}

	private String encodePassword(String password) {
		return passwordEncoder.encode(password);
	}

	public void verifyAccount(String token) throws SpringRedditException {
		Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByToken(token);
		verificationTokenOptional.orElseThrow(() -> new SpringRedditException("Invalid Token"));
		fetchUserAndEnable(verificationTokenOptional.get());
	}

	@Transactional
	private void fetchUserAndEnable(VerificationToken verificationToken) throws SpringRedditException {
		String username = verificationToken.getUser().getUsername();
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new SpringRedditException("User Not Found with id - " + username));
		user.setEnabled(true);
		userRepository.save(user);
	}
}