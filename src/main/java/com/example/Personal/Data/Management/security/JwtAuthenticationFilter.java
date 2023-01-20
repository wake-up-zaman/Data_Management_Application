package com.example.Personal.Data.Management.security;

import com.example.Personal.Data.Management.exceptions.ApiException;
import com.example.Personal.Data.Management.repositories.WhiteTokenRepo;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private WhiteTokenRepo whiteTokenRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestToken = request.getHeader("Authorization");
        System.out.println(requestToken);

        String username = null;
        String token = null;

        if(requestToken != null  && requestToken.startsWith("Bearer")){
            token = requestToken.substring(7);
            try{
                username=this.jwtTokenHelper.getUsernameFromToken(token);

//                new UserNameStore(username);
                System.out.println("This is Your UserName: "+username);
            }
            catch(IllegalArgumentException ex){
                System.out.println("Unable to get JWT Token"+ ex);
            }
            catch (ExpiredJwtException e){
                System.out.println("JWT token has expired");
            }
            catch (MalformedJwtException e){
                System.out.println("Invalid JWT");
            }

        }
        else{
            System.out.println("JWT token does not begin with Bearer");
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){

            //Checking for White Listed Token
            this.whiteTokenRepo.findByWhiteToken(token).orElseThrow(()->new ApiException("Token is Black Listed"));

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if(this.jwtTokenHelper.validateToken(token, userDetails)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken= new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            else{
                System.out.println("Invalid JWT token");
            }
        }
        else{
            System.out.println("Username is null or context is not null");
        }
        filterChain.doFilter(request, response);
    }
}