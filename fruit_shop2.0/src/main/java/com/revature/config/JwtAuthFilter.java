package com.revature.config;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.revature.exceptions.BadTokenException;
import com.revature.repositories.UserRepository;
import com.revature.service.JwtService;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwt;
    private final UserRepository users;

    public JwtAuthFilter(JwtService jwt, UserRepository users) {
        this.jwt = jwt;
        this.users = users;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            try {
                int userId = jwt.getUserId(header.substring(7));
                users.findById(userId)
                        .filter(user -> user.getRole() != null)
                        .ifPresent(user -> SecurityContextHolder.getContext().setAuthentication(
                                new UsernamePasswordAuthenticationToken(
                                        user.getId(),
                                        null,
                                        Collections.singletonList(
                                                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())))));
            } catch (BadTokenException e) {
                // bad or expired token: stay anonymous, the rules below return 401
            }
        }
        chain.doFilter(request, response);
    }
}