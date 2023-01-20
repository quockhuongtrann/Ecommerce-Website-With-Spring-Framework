package com.shoptqk.address;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shoptqk.common.entity.Address;
import com.shoptqk.common.entity.Country;
import com.shoptqk.common.entity.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class AddressRepositoryTests {
	@Autowired private AddressRepository addressRepository;
	
	@Test
	public void testCreateNew() {
		Integer customerId = 5;
		Integer countryId = 234;
		
		Address newAddress = new Address();
		newAddress.setCustomer(new Customer(customerId));
		newAddress.setCountry(new Country(countryId));
		newAddress.setFirstName("Tobie");
		newAddress.setLastName("Abel");
		newAddress.setPhoneNumber("19094644165");
		newAddress.setAddressLine1("4213 Gordon Street");
		newAddress.setAddressLine2("Novak Building");
		newAddress.setCity("Chicago");
		newAddress.setState("California");
		newAddress.setPostalCode("91710");
		
		Address savedAddress = addressRepository.save(newAddress);
		assertThat(savedAddress).isNotNull();
		assertThat(savedAddress.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testUpdate() {
		Integer addressId = 2;
		Address address = addressRepository.findById(addressId).get();
		address.setDefaultForShipping(true);
	}
	
	@Test
	public void testSetDefault() {
		Integer addressId = 1;
		addressRepository.setDefaultAddress(addressId);
		
		Address address = addressRepository.findById(addressId).get();
		
		assertThat(address.isDefaultForShipping()).isTrue();
	}
	
	@Test
	public void testSetNonDefaultAddresses() {
		Integer addressId = 2;
		Integer customerId = 5;
		addressRepository.setNonDefaultForOthers(addressId, customerId);
	}
	
	@Test
	public void testGetDefault() {
		Integer customerId = 5;
		Address address = addressRepository.findDefaultByCustomer(customerId);
		
		assertThat(address).isNotNull();
		System.out.println(address);
	}
}
