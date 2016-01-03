package org.cateproject.web.auth;

import org.cateproject.repository.jpa.auth.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserAccountRepository userAccountRepository;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	http.userDetailsService(userManager())
	.authorizeRequests()
	//.expressionHandler(new TenantWebExpressionHandler())
        .antMatchers("/", "/index", "/init","/health","/explore","/static/**","/webjars/**","/multitenant/event").permitAll()
        .antMatchers("/system").hasAnyAuthority("ROLE_CONFIGURE_SYSTEM")
        .antMatchers("/admin").hasAnyAuthority("ROLE_ADMINISTRATE")
        .antMatchers("/edit").hasAnyAuthority("ROLE_EDIT")
        .anyRequest().authenticated()
    //.hasAuthority("isAuthenticated() and tenantAllowed")
        .and()
        .formLogin()
        .loginPage("/login")
        .permitAll()
        .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")).logoutSuccessUrl("/").permitAll();
	}
	
	@Bean
	public UserDetailsService userManager() {
		UserManager userManager = new UserManager();
		userManager.setUserAccountRepository(userAccountRepository);
		return userManager;
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.userDetailsService(userManager()).passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public PasswordEncoder passwordEncoder(){
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
}
