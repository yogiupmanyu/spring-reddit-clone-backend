package com.practice.springredditclone.exception;

public class SubredditNotFoundException extends Exception {


	private static final long serialVersionUID = 1L;
	
	public SubredditNotFoundException(String msg) {
		super(msg);
	}

}
