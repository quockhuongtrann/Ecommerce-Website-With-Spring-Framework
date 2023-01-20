package com.shoptqk.shipping;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.shoptqk.common.entity.Country;
import com.shoptqk.common.entity.ShippingRate;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ShippingRateRepositoryTests {
	
	@Autowired private ShippingRateRepository shippingRateRepository;
	
	@Test
	public void testFindByCountryAndState() {
		Country usa = new Country(234);
		String state = "New York";
		ShippingRate shippingRate = shippingRateRepository.findByCountryAndState(usa, state);
		
		assertThat(shippingRate).isNotNull();
		System.out.println(shippingRate);
	}
}
