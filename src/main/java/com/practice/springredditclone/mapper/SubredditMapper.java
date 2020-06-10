package com.practice.springredditclone.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.practice.springredditclone.dto.SubredditDto;
import com.practice.springredditclone.model.Post;
import com.practice.springredditclone.model.Subreddit;

import java.util.List;

//@Mapper(componentModel = "spring")
//public interface SubredditMapper {
//
//    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
//    SubredditDto mapSubredditToDto(Subreddit subreddit);
//
//    default Integer mapPosts(List<Post> numberOfPosts) {
//        return numberOfPosts.size();
//    }
//
//    @InheritInverseConfiguration
//    @Mapping(target = "posts", ignore = true)
//    Subreddit mapDtoToSubreddit(SubredditDto subreddit);
//}
