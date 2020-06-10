package com.practice.springredditclone.service;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.practice.springredditclone.exception.SpringRedditException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Date;

@Service
@Slf4j
public class JwtProvider {
	
	private KeyStore keyStore;
	
	// it is the alias name that we provide during ( generate of the KeyStore )
	@Value("${cdac.mis.app.keyStoreFileName}")
    private String keyStoreFileName;

	@Value("${cdac.mis.app.keyStorePassword}")
    private String keyStorePassword;

    @Value("${cdac.mis.app.jwtExpiration}")
    private int jwtExpiration;

	@PostConstruct
	public void init() throws SpringRedditException {
		try {
			// to get JKS KeyStore 
			keyStore = KeyStore.getInstance("JKS");
			// name of the KeyStore with path of resources folder
			InputStream resourceAsStream = getClass().getResourceAsStream("/"+ keyStoreFileName +".jks");
			// load KeyStore arguments are resource stream and password of the KeyStore 
			keyStore.load(resourceAsStream, keyStorePassword.toCharArray());
		} catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
			log.error(e.getMessage());
			throw new SpringRedditException("Exception occurred while loading keystore");
		}
	}

	
	public String generateToken(Authentication authentication) throws InvalidKeyException, SpringRedditException {
		
		org.springframework.security.core.userdetails.User principal = (User) authentication.getPrincipal();
		
		return Jwts.builder()
				.setSubject(principal.getUsername())
				.setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration*1000))
                .signWith(getPrivateKey())  
				.compact();
	}
	// used in authentication filter to get UserName from JWT token 
	 public String getUserNameFromJwtToken(String token) throws SpringRedditException {
	        try {
				return Jwts.parser()
					                .setSigningKey(getPrivateKey())
					                .parseClaimsJws(token)
					                .getBody().getSubject();
			} catch (SignatureException | ExpiredJwtException | UnsupportedJwtException | MalformedJwtException
					| IllegalArgumentException | SpringRedditException e) {
				log.error(e.getMessage());
				throw new SpringRedditException("Exception occured while retrieving username from keystore");
			}
	    }
	 // used in authentication filter to validate the received token
	  public boolean validateJwtToken(String authToken) throws SpringRedditException {
	        
	        try {
				Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(authToken);
			} catch (SignatureException | ExpiredJwtException | UnsupportedJwtException | MalformedJwtException
					| IllegalArgumentException | SpringRedditException e) {
				
				log.error(e.getMessage());
				throw new SpringRedditException("Exception occured while retrieving public key from keystore");
			}
	        
	        return false;
	    }
	
	
	private PublicKey getPublicKey() throws SpringRedditException {
		try {
			return (PublicKey)keyStore.getCertificate(keyStoreFileName).getPublicKey();
		} catch (KeyStoreException e) {
			log.error(e.getMessage());
			throw new SpringRedditException("Exception occured while retrieving public key from keystore");
		}
	}


	private PrivateKey getPrivateKey() throws SpringRedditException {
		try {
			// provide password of the KeyStore and name of the KeyStore file or the alias name 
			return (PrivateKey) keyStore.getKey(keyStoreFileName, keyStorePassword.toCharArray());
		} catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
			log.error(e.getMessage());
			throw new SpringRedditException("Exception occured while retrieving private key from keystore");
		}
	}
}
