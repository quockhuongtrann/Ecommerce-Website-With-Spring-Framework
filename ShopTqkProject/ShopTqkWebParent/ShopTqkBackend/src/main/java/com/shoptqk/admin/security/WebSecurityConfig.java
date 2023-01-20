package com.shoptqk.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new ShopTqkUserDetailsService();
	}
	
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(PasswordEncoder());
		return authProvider;
	}
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		String[] staticResources = {"/images/**", "/js/**", "/webjars/**"};
		
        http.authorizeRequests()
        		.requestMatchers("/states/list_by_country/**")
        			.hasAnyAuthority("Admin", "Salesperson")
        		.requestMatchers("/users/**", "/settings/**", "/countries/**", "/states/**")
        			.hasAuthority("Admin")
        		.requestMatchers("/categories/**", "/brands/**")
        			.hasAnyAuthority("Admin", "Editor")
        		.requestMatchers("/products/new", "/products/delete/**")
        			.hasAnyAuthority("Admin", "Editor")
        		.requestMatchers("/products/edit/**", "/products/save", "/products/check_unique")
        			.hasAnyAuthority("Admin", "Editor", "Salesperson")
        		.requestMatchers("/products", "/products/", "/products/detail/**", "/products/page/**")
        			.hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")
        		.requestMatchers("/products/**")
        			.hasAnyAuthority("Admin", "Editor")
        		.requestMatchers("/orders", "/orders/", "/orders/page/**", "/orders/detail/**")
        			.hasAnyAuthority("Admin", "Salesperson", "Shipper")
        		.requestMatchers("/products/detail/**", "/customers/detail/**")
        			.hasAnyAuthority("Admin", "Editor", "Salesperson", "Assistant")
        		.requestMatchers("/customers/**", "/orders/**", "/get_shipping_cost", "/reports/**")
        			.hasAnyAuthority("Admin", "Salesperson")
        		.requestMatchers("/orders_shipper/update/**")
        			.hasAuthority("Shipper")
        		.requestMatchers("/reviews/**")
        			.hasAnyAuthority("Admin", "Assistant")
        		.requestMatchers(staticResources).permitAll()
        		.anyRequest().authenticated()
        	.and()
        	.formLogin()
        		.loginPage("/login")
        		.usernameParameter("email")
        		.permitAll()
        	.and()
        		.logout().permitAll()
        	.and()
        		.rememberMe()
        		.key("AbcDefgHijKlmnOpqres_1234567890")
        		.tokenValiditySeconds(7 * 24 * 60 * 60)
        	.and()
        	.authenticationProvider(authenticationProvider());
        http.headers().frameOptions().sameOrigin();
        return http.build();
    }
		
	@Bean
	public PasswordEncoder PasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
}
