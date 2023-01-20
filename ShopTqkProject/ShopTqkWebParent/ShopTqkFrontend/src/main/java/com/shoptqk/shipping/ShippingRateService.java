package com.shoptqk.shipping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoptqk.common.entity.Address;
import com.shoptqk.common.entity.Customer;
import com.shoptqk.common.entity.ShippingRate;

@Service
public class ShippingRateService {
	@Autowired private ShippingRateRepository shippingRateRepository;
	
	public ShippingRate getShippingRateForCustomer(Customer customer) {
		String state = customer.getState();
		if (state == null || state.isEmpty()) {
			state = customer.getCity();
		}
		
		return shippingRateRepository.findByCountryAndState(customer.getCountry(), state);
	}
	
	public ShippingRate getShippingRateForAddress(Address address) {
		String state = address.getState();
		if (state == null || state.isEmpty()) {
			state = address.getCity();
		}
		
		return shippingRateRepository.findByCountryAndState(address.getCountry(), state);
	}
}
