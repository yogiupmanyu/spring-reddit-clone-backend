package com.practice.springredditclone.exception;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ResponseError {
	
	 private HttpStatus status;
	    private String message;
	    private List<String> errors;
	 
	    public ResponseError(HttpStatus status, String message, List<String> errors) {
	        super();
	        this.status = status;
	        this.message = message;
	        this.errors = errors;
	    }
	 
	    public ResponseError(HttpStatus status, String message, String error) {
	        super();
	        this.status = status;
	        this.message = message;
	        errors = Arrays.asList(error);
	    }

}
