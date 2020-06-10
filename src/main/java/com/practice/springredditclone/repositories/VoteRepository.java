package com.practice.springredditclone.repositories;

import com.practice.springredditclone.model.Post;
import com.practice.springredditclone.model.User;
import com.practice.springredditclone.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
