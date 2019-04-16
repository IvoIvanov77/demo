package com.example.demo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final SavedRequestAwareAuthenticationSuccessHandler successHandler;
    private final SimpleUrlAuthenticationFailureHandler failureHandler;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public WebSecurityConfiguration(RestAuthenticationEntryPoint restAuthenticationEntryPoint,
                                    SavedRequestAwareAuthenticationSuccessHandler successHandler,
                                    SimpleUrlAuthenticationFailureHandler failureHandler, BCryptPasswordEncoder passwordEncoder) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/users/login").anonymous()
//                .antMatchers("/employees").authenticated()
                .antMatchers("/employees/edit").hasRole("ADMIN")
                .antMatchers("/employees/create").hasRole("ADMIN")
                .antMatchers("/employees/delete").hasRole("ADMIN")
                .and()
                .formLogin()
                .loginPage("/users/login")
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .and()
                .logout();
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository =
                new HttpSessionCsrfTokenRepository();
        repository.setSessionAttributeName("_csrf");
        return repository;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password(passwordEncoder.encode("123")).roles("ADMIN")
                .and()
                .withUser("user").password(passwordEncoder.encode("123")).roles("USER");
    }
}
