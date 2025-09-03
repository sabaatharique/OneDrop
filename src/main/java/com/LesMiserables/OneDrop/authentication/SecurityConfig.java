package com.LesMiserables.OneDrop.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JWTAuthFilter jwtAuthFilter;

    public SecurityConfig(JWTAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    SecurityFilterChain security(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/donors").hasAnyAuthority("ADMIN", "RECIPIENT")
                        .requestMatchers(HttpMethod.POST, "/api/donors").hasAnyAuthority("ADMIN", "DONOR")
                        .requestMatchers(HttpMethod.PUT, "/api/donors").hasAnyAuthority("ADMIN", "DONOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/donors").hasAnyAuthority("ADMIN", "DONOR")
                        .requestMatchers(HttpMethod.GET, "/api/recipients").hasAnyAuthority("ADMIN", "DONOR")
                        .requestMatchers(HttpMethod.POST, "/api/recipients").hasAnyAuthority("ADMIN", "RECIPIENT")
                        .requestMatchers(HttpMethod.PUT, "/api/recipients").hasAnyAuthority("ADMIN", "RECIPIENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/recipients").hasAnyAuthority("ADMIN", "RECIPIENT")
                        .requestMatchers(HttpMethod.GET, "/api/requests").hasAnyAuthority("ADMIN", "DONOR")
                        .requestMatchers(HttpMethod.POST, "/api/requests").hasAnyAuthority("ADMIN", "RECIPIENT")
                        .requestMatchers(HttpMethod.PUT, "/api/requests").hasAnyAuthority("ADMIN", "RECIPIENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/requests").hasAnyAuthority("ADMIN", "RECIPIENT")
                        .requestMatchers("/api/donations/**").hasAnyAuthority("ADMIN", "DONOR")
                        .requestMatchers("/api/matches/**").hasAnyAuthority("ADMIN", "DONOR", "RECIPIENT")
                        .requestMatchers("/", "/index.html", "/login.html", "/register.html", "/assets/**", "/*.html").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}