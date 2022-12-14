//package com.example.hospitalreview2.configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity
//                .httpBasic().disable()
//                .csrf().disable() //크로스사이트 기능
//                .cors().and()
//                .authorizeRequests()
//                .antMatchers("/api/**").permitAll()
//                .antMatchers("/api/v1/users/join", "/api/v1/users/login").permitAll()
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .build();
//    }
//}
