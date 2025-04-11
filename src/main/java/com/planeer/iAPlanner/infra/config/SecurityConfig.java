package com.planeer.iAPlanner.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher h2ConsoleMatcher = new MvcRequestMatcher(introspector, "/h2-console/**");
        h2ConsoleMatcher.setServletPath("/h2-console");

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(h2ConsoleMatcher).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/schedules/addSchedule")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/schedules/getSchedule/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/schedules/getAllSchedules")).permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(h2ConsoleMatcher)
                        .disable() // Desabilita CSRF para simplificação (não recomendado em produção)
                )
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                );

        return http.build();
    }
}