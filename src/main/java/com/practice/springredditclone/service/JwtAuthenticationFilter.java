package com.practice.springredditclone.service;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.practice.springredditclone.exception.SpringRedditException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	private JwtProvider tokenProvider;

	@Autowired
	private UserDetailServiceImpl userDetailsService;
	
	 
	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
			
				String jwt = getJwt(request);
				try {
					if (jwt != null && tokenProvider.validateJwtToken(jwt)) {
						String username = tokenProvider.getUserNameFromJwtToken(jwt);

				// retrieves the UserName from the database
						UserDetails userDetails = userDetailsService.loadUserByUsername(username);
						UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// sets the authentication ( User Info ) in the Security Context ( It is extended ThreadLocal class which contains 
						// the user for this specific request ) this is a State less mechanism we do not store the token explicitly like
						// in session Id mechanism
						SecurityContextHolder.getContext().setAuthentication(authentication);
					
					}
				} catch (SpringRedditException e) {
					e.printStackTrace();
				}
		

		filterChain.doFilter(request, response);
	}

	private String getJwt(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.replace("Bearer ", "");
		}

		return null;
	}
}
