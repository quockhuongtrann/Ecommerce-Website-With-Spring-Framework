package com.shoptqk.oauth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.shoptqk.common.entity.AuthenticationType;
import com.shoptqk.common.entity.Customer;
import com.shoptqk.customer.CustomerService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	@Autowired private CustomerService customerService;
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		CustomerOAuth2User oAuth2User =  (CustomerOAuth2User) authentication.getPrincipal();
		
		String name = oAuth2User.getName();
		String email = oAuth2User.getEmail();
		String countryCode = request.getLocale().getCountry();
				
		Customer customer = customerService.getCustomerByEmail(email);
		if (customer == null) {
			customerService.addNewCustomerUponOAuthLogin(name, email, countryCode);
		} else {
			oAuth2User.setFullName(customer.getFullName());
			customerService.updateAuthentication(customer, AuthenticationType.GOOGLE);
		}
		
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
}
