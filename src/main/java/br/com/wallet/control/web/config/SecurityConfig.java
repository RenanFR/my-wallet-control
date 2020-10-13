package br.com.wallet.control.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import br.com.wallet.control.web.service.LoginService;

@EnableWebSecurity
@EnableAuthorizationServer
@EnableResourceServer
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private LoginService loginService;
	
    @Autowired
    private PasswordEncoder passwordEncoder;	
	
	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
       webSecurity
	   	.ignoring()
	   	.antMatchers("/walletws/**");
    }	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.userDetailsService(loginService)
			.passwordEncoder(passwordEncoder);
	}
	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }    	
	
}
