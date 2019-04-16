package com.example.demo.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf()
//                .csrfTokenRepository(csrfTokenRepository())
//                .and()
//                .authorizeRequests()
//                .antMatchers("/", "/js/**", "/css/**", "/img/**").permitAll()
//                .antMatchers("/user/login", "/user/register").anonymous()
//                .antMatchers( "/viruses/edit/*", "/viruses/delete/*").hasAuthority("ROLE_MODERATOR")
//                .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
//                .anyRequest().authenticated()//
//                .and()
//                .formLogin()
//                .loginPage("/user/login")
//                .usernameParameter("username")
//                .passwordParameter("password")
//                .defaultSuccessUrl("/")
//                .and()
//                .logout().logoutSuccessUrl("/")
//                .and()
//                .exceptionHandling()
//                .accessDeniedPage("/");
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository =
                new HttpSessionCsrfTokenRepository();
        repository.setSessionAttributeName("_csrf");
        return repository;
    }
}
