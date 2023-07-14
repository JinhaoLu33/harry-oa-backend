package com.harry.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity //@EnableWebSecurity is the default behavior for enabling SpringSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

}
