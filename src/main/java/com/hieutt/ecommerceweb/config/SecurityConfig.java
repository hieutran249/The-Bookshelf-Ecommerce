package com.hieutt.ecommerceweb.config;

import com.hieutt.ecommerceweb.utils.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    @Order(3)
    public SecurityFilterChain securityFilterChainAdmin(HttpSecurity http) throws Exception {
        http
                // this filter chain only handle urls that have the pattern "/admin/**
                .securityMatcher("/admin/**", "/login-admin", "/logout-admin")
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(Constants.WHITELIST_ENDPOINTS).permitAll()
                // all the urls that have the pattern /admin/** need to be ADMIN
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()

                .and()
                .formLogin()
                .loginPage("/login-admin")
                .loginProcessingUrl("/login-admin")
                .failureUrl("/login-admin?error")
                .defaultSuccessUrl("/admin")
                .permitAll()

                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout-admin"))
                .logoutSuccessUrl("/login-admin?logout")
                .permitAll()

                .and().authenticationProvider(authenticationProvider);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChainCustomer(HttpSecurity http) throws Exception {
        http
                // this filter chain only handle urls that have the pattern "/customer/**
                .securityMatcher("/customer/**", "/login", "/logout")
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(Constants.WHITELIST_ENDPOINTS).permitAll()
                // all the urls that have the pattern /customer/** need to be CUSTOMER
                .requestMatchers("/customer/**").hasAuthority("CUSTOMER")
                .anyRequest().authenticated()

                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .failureUrl("/login?error")
                .permitAll()

                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .permitAll()

                .and().authenticationProvider(authenticationProvider);

        return http.build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChainAnonymous(HttpSecurity http) throws Exception {
        http
                // this filter chain only handle anonymous urls
                .securityMatcher("/")
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(Constants.WHITELIST_ENDPOINTS).permitAll()
                .requestMatchers("/").permitAll();

        return http.build();
    }
}
