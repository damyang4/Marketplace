package com.fmi.springcourse.marketplace.filter;

import com.fmi.springcourse.marketplace.util.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@NullMarked
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
	
	private final UserDetailsService userDetailsService;
	private final JwtService jwtService;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String path = request.getRequestURI();
		// Exclude Stripe Webhooks, If the request is for the stripe webhook, skip this filter entirely
		return path.startsWith("/api/stripe/");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
	                                HttpServletResponse response,
	                                FilterChain filterChain)
		throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		
		String username = null;
		String jwt = null;
		String bearer = "Bearer ";
		
		if (authHeader != null && authHeader.startsWith(bearer)) {
			jwt = authHeader.substring(bearer.length());
			try {
				username = jwtService.extractUsername(jwt);
			} catch (JwtException | IllegalArgumentException e) {
				filterChain.doFilter(request, response);
				return;
			}
		}
		
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			if (jwtService.isTokenValid(jwt, userDetails.getUsername())) {
				UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities());
				upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(upat);
			}
		}
		
		filterChain.doFilter(request, response);
	}
}
