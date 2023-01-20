package com.shoptqk.admin.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {
	@Autowired private CustomerService customerService;
	
	@PostMapping("/customers/check_email")
	public String checkDuplicateEmail(Integer id, String email) {
		if (customerService.isEmailUnique(id, email)) {
			return "OK";
		} else {
			return "Duplicated";
		}
	}
}
