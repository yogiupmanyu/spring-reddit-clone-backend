package com.practice.springredditclone.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.practice.springredditclone.model.Subreddit;

import java.util.Optional;

public interface SubredditRepository extends JpaRepository<Subreddit, Long> {
    Optional<Subreddit> findByName(String subredditName);
}