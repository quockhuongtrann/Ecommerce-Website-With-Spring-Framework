package com.shoptqk.address;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoptqk.common.entity.Address;
import com.shoptqk.common.entity.Customer;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AddressService {
	@Autowired private AddressRepository addressRepository;
	
	public List<Address> listAddressBook(Customer customer) {
		return addressRepository.findByCustomer(customer);
	}
	
	public void save(Address address) {
		addressRepository.save(address);
	}
	
	public Address get(Integer addressId, Integer customerId) {
		return addressRepository.findByIdAndCustomer(addressId, customerId);
	}
	
	public void delete(Integer addressId, Integer customerId) {
		addressRepository.deleteByIdAndCustomer(addressId, customerId);
	}
	
	public void setDefaultAddress(Integer defaultAddressId, Integer customerId) {
		if (defaultAddressId > 0) {
			addressRepository.setDefaultAddress(defaultAddressId);
		}
		addressRepository.setDefaultAddress(defaultAddressId);
		addressRepository.setNonDefaultForOthers(defaultAddressId, customerId);
	}
	
	public Address getDefaultAddress(Customer customer) {
		return addressRepository.findDefaultByCustomer(customer.getId());
	}
}
