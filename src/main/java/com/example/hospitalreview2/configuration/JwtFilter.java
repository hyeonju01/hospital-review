package com.example.hospitalreview2.configuration;

import com.example.hospitalreview2.service.UserService;
import com.example.hospitalreview2.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        final String authorizaiotn = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authentication: {}", authorizaiotn);

        // token 없을 경우 블락!
        if (authorizaiotn == null || !authorizaiotn.startsWith("Bearer ")) {
            log.error("authentication을 잘못 보냈습니다. ");
            filterChain.doFilter(request, response);
            return;
        }

        // token 꺼내기
        String token = authorizaiotn.split(" ")[1];

        // token 발급일 만료 여부 확인
        if(JwtTokenUtil.isExpired(token, secretKey)) {
            log.error("Token이 만료되었습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        // token에서 Username 꺼내기
        String userName = JwtTokenUtil.getUserName(token, secretKey);
        log.info("userName: {}", userName);

        // 권한 부여
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userName, null, List.of(new SimpleGrantedAuthority("USER")));
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }



}
