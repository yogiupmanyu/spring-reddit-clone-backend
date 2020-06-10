package com.practice.springredditclone.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.practice.springredditclone.model.Comment;
import com.practice.springredditclone.model.Post;
import com.practice.springredditclone.model.User;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
