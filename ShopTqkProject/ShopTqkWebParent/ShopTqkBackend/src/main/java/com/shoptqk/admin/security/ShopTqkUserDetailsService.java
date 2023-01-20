package com.shoptqk.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.shoptqk.admin.user.UserRepository;
import com.shoptqk.common.entity.User;

public class ShopTqkUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepo.getUserByEmail(email);
		
		if (user != null) {
			return new ShopTqkUserDetails(user);
		}
		throw new UsernameNotFoundException("Could not find user with email " + email);
		
	}

}
