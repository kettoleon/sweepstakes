package com.github.kettoleon.sweepstakes.configuration;

import com.github.kettoleon.sweepstakes.users.SweepstakesUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {


    @Bean
    public SecurityFilterChain customSecurityFilterChain(HttpSecurity http, SweepstakesUserDetailsService userDetailsService) throws Exception {
        http.authorizeRequests(req ->
                        req
                                .antMatchers("/bet", "/admin").authenticated()
                                .anyRequest().permitAll()
                )
                .formLogin(login -> login
                        .loginPage("/login").permitAll()
                        .loginProcessingUrl("/login").permitAll()
                        .defaultSuccessUrl("/bet")

                )
                .userDetailsService(userDetailsService)
                .logout(logout -> logout.permitAll())
                .csrf().disable() //TODO not disable this
        ;

        return http.build();

    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
