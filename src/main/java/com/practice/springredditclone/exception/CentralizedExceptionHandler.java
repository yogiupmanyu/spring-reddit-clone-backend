package com.practice.springredditclone.exception;


import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class CentralizedExceptionHandler extends ResponseEntityExceptionHandler {

	
	@ExceptionHandler(com.practice.springredditclone.exception.SubredditNotFoundException.class)
	public ResponseEntity<Object> handleSubredditNotFoundException(SubredditNotFoundException ex,WebRequest web){
		ex.printStackTrace();
		log.error("SubredditNotFoundException : " + ex.getMessage());
		log.info(web.getDescription(true));
		ex.printStackTrace();
		List<String> errors = new ArrayList<>();
		errors.add(ex.getMessage());
		ResponseError error = new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), errors);
		return new ResponseEntity<Object>( error , HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(com.practice.springredditclone.exception.SpringRedditException.class)
	public ResponseEntity<Object> handleSpringRedditException(SpringRedditException ex,WebRequest web){
		ex.printStackTrace();
		log.error("SpringRedditException : " + ex.getMessage());
		log.info(web.getDescription(true));
		ex.printStackTrace();
		List<String> errors = new ArrayList<>();
		errors.add(ex.getMessage());
		ResponseError error = new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), errors);
		return new ResponseEntity<Object>( error , HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
