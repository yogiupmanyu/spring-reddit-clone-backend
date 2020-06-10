package com.practice.springredditclone.controller;



import com.practice.springredditclone.dto.AuthenticationResponse;
import com.practice.springredditclone.dto.LoginRequest;
import com.practice.springredditclone.dto.RegisterRequest;
import com.practice.springredditclone.exception.SpringRedditException;
import com.practice.springredditclone.service.AuthService;

import io.jsonwebtoken.security.InvalidKeyException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
        try {
			authService.signup(registerRequest);
			return new ResponseEntity<String>("Success",HttpStatus.OK);
		} catch (SpringRedditException e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
        
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
			return new ResponseEntity<AuthenticationResponse>(authService.login(loginRequest),HttpStatus.OK);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			return new ResponseEntity<AuthenticationResponse>(new AuthenticationResponse("", loginRequest.getUsername()),HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (SpringRedditException e) {
			e.printStackTrace();
			return new ResponseEntity<AuthenticationResponse>(new AuthenticationResponse("", loginRequest.getUsername()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
    
    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
    	try {
			authService.verifyAccount(token);
			return new ResponseEntity<>("Account Activated Successully", HttpStatus.OK);
		} catch (SpringRedditException e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
    	
    }
}