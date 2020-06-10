package com.practice.springredditclone.controller;


import com.practice.springredditclone.dto.SubredditDto;
import com.practice.springredditclone.exception.SubredditNotFoundException;
import com.practice.springredditclone.service.SubredditService;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor
public class SubredditController {

    private final SubredditService subredditService;

    @GetMapping
    public ResponseEntity<List<SubredditDto>> getAllSubreddits() {
        return ResponseEntity.status(HttpStatus.OK)
        		.body(subredditService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubredditDto> getSubreddit(@PathVariable Long id) {
        try {
		
        	return ResponseEntity.status(HttpStatus.OK)
        			.body(subredditService.getSubreddit(id));
			
		} catch (SubredditNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
    }

    @PostMapping
    public ResponseEntity<SubredditDto> create(@RequestBody @Valid SubredditDto subredditDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
        		.body(subredditService.save(subredditDto));
    }
}