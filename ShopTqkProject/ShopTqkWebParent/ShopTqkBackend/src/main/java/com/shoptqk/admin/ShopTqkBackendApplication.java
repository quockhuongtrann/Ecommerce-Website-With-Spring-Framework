package com.shoptqk.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.shoptqk.common.entity", "com.shoptqk.admin.user"})
public class ShopTqkBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopTqkBackendApplication.class, args);
	}

}
