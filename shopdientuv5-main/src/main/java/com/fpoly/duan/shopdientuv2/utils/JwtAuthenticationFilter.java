package com.fpoly.duan.shopdientuv2.utils;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpoly.duan.shopdientuv2.services.CustomUserDetailsService;
import com.fpoly.duan.shopdientuv2.reps.ResponseData;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

        private final JwtUtil jwtUtil;
        private final CustomUserDetailsService userDetailsService;
        private final ObjectMapper objectMapper;

        public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService,
                        ObjectMapper objectMapper) {
                this.jwtUtil = jwtUtil;
                this.userDetailsService = userDetailsService;
                this.objectMapper = objectMapper;
        }

        @Override
        protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                        @NonNull FilterChain chain) throws ServletException, IOException {
                String token = request.getHeader("Authorization");
                if (token != null && token.startsWith("Bearer ")) {
                        token = token.substring(7);
                        try {
                                String username = jwtUtil.extractUsername(token);
                                if (username != null
                                                && SecurityContextHolder.getContext().getAuthentication() == null) {
                                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                                        if (jwtUtil.validateToken(token, userDetails.getUsername())) {
                                                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                                                userDetails, null, userDetails.getAuthorities());
                                                authenticationToken.setDetails(new WebAuthenticationDetailsSource()
                                                                .buildDetails(request));
                                                SecurityContextHolder.getContext()
                                                                .setAuthentication(authenticationToken);
                                        }
                                }
                        } catch (Exception e) {
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                response.setContentType("application/json;charset=UTF-8");
                                response.getWriter().write(objectMapper.writeValueAsString(
                                                new ResponseData(false, "Token không hợp lệ hoặc hết hạn", null)));
                                return;
                        }
                }
                chain.doFilter(request, response);
        }
}
