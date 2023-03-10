package com.shoptqk.admin.shippingrate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ShippingRateRestController {
	@Autowired private ShippingRateService shippingRateService;
	
	@PostMapping("/get_shipping_cost")
	public String getShippingCost(Integer productId, Integer countryId, String state) throws ShippingRateNotFoundException {
		float shippingCost = shippingRateService.calculateShippingCost(productId, countryId, state);
		return String.valueOf(shippingCost);
	}
}
