package com.shoptqk.shipping;

import org.springframework.data.repository.CrudRepository;

import com.shoptqk.common.entity.Country;
import com.shoptqk.common.entity.ShippingRate;

public interface ShippingRateRepository extends CrudRepository<ShippingRate, Integer> {
	public ShippingRate findByCountryAndState(Country country, String state);
}
