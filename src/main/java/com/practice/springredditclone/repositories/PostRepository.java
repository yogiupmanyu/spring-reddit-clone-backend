package com.practice.springredditclone.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.practice.springredditclone.model.Post;
import com.practice.springredditclone.model.Subreddit;
import com.practice.springredditclone.model.User;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);
    List<Post> findByUser(User user);
}