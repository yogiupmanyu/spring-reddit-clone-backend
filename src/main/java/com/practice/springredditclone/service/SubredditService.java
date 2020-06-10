package com.practice.springredditclone.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.practice.springredditclone.dto.SubredditDto;
import com.practice.springredditclone.exception.SubredditNotFoundException;
import com.practice.springredditclone.model.Subreddit;
import com.practice.springredditclone.repositories.SubredditRepository;

import java.util.List;

import static java.time.Instant.now;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class SubredditService {

    private final SubredditRepository subredditRepository;
    
    /**
     * Use when maptoInstruct will used 
     * */
    //private final SubredditMapper subredditMapper;
    
    private final AuthService authService;

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(toList());
    }

    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
    	// before implementing mapstruct
        Subreddit subreddit = subredditRepository.save(mapToSubreddit(subredditDto));
        
    	//Subreddit subreddit = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
        subredditDto.setId(subreddit.getId());
        return subredditDto;
    }

    @Transactional(readOnly = true)
    public SubredditDto getSubreddit(Long id) throws SubredditNotFoundException {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SubredditNotFoundException("Subreddit not found with id -" + id));
        return mapToDto(subreddit);
    }
// we can use mapToInstruct API here
    private SubredditDto mapToDto(Subreddit subreddit) {
    	
    /** Use Builder Pattern to initialize the Entitiy ( builder method is provide with help of @Builder
     *  annotation of Lombok*/
        return SubredditDto.builder().name(subreddit.getName())
                .id(subreddit.getId())
                .postCount(subreddit.getPosts().size())
                .build();
    }

    private Subreddit mapToSubreddit(SubredditDto subredditDto) {
        return Subreddit.builder().name("/r/" + subredditDto.getName())
                .description(subredditDto.getDescription())
                .user(authService.getCurrentUser())
                .createdDate(now()).build();
    }
}
