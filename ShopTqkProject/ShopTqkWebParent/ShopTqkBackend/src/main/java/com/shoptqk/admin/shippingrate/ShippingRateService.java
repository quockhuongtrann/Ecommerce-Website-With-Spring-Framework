package com.shoptqk.admin.shippingrate;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoptqk.admin.paging.PagingAndSortingHelper;
import com.shoptqk.admin.product.ProductRepository;
import com.shoptqk.admin.setting.country.CountryRepository;
import com.shoptqk.common.entity.Country;
import com.shoptqk.common.entity.ShippingRate;
import com.shoptqk.common.entity.product.Product;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ShippingRateService {
	public static final int RATES_PER_PAGE = 10;
	private static final int DIM_DIVISOR = 139;
	@Autowired private ShippingRateRepository shippingRateRepository;
	@Autowired private CountryRepository countryRepository;
	@Autowired private ProductRepository productRepository;
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, RATES_PER_PAGE, shippingRateRepository);
	}
	
	public List<Country> listAllCountries() {
		return countryRepository.findAllByOrderByNameAsc();
	}
	
	public void save(ShippingRate rateInForm) throws ShippingRateAlreadyExistsException {
		ShippingRate rateInDB = shippingRateRepository.findByCountryAndState(
				rateInForm.getCountry().getId(), rateInForm.getState());
		boolean foundExistingRateInNewMode = rateInForm.getId() == null && rateInDB != null;
		boolean foundDifferentExistingRateInEditMode = rateInForm.getId() != null && rateInDB != null && rateInForm.getId() != rateInDB.getId();
		if (foundExistingRateInNewMode || foundDifferentExistingRateInEditMode) {
			throw new ShippingRateAlreadyExistsException("There's already a rate for the destination: "
					+ rateInForm.getCountry().getName() + ", " + rateInForm.getState());
		}
		shippingRateRepository.save(rateInForm);
	}
	
	public ShippingRate get(Integer id) throws ShippingRateAlreadyExistsException {
		try {
			return shippingRateRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new ShippingRateAlreadyExistsException("Cound not find shipping rate with ID " + id);
		}
	}
	
	public void updateCODSupport(Integer id, boolean codSupported) throws ShippingRateAlreadyExistsException {
		Long count = shippingRateRepository.countById(id);
		if (count == null || count == 0) {
			throw new ShippingRateAlreadyExistsException("Cound not find shipping rate with ID " + id);
		}
		shippingRateRepository.updateCODSupport(id, codSupported);
	}
	
	public void delete(Integer id) throws ShippingRateAlreadyExistsException {
		Long count = shippingRateRepository.countById(id);
		if (count == null || count == 0) {
			throw new ShippingRateAlreadyExistsException("Cound not find shipping rate with ID " + id);
		}
		shippingRateRepository.deleteById(id);
	}
	
	public float calculateShippingCost(Integer productId, Integer countryId, String state) throws ShippingRateNotFoundException {
		ShippingRate shippingRate = shippingRateRepository.findByCountryAndState(countryId, state);
		
		if (shippingRate == null) {
			throw new ShippingRateNotFoundException("No shipping rate found for the given" 
					+ " destination. You have to enter shipping cost manually.");
		}
		
		Product product = productRepository.findById(productId).get();
		float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISOR;
		float finalWeight = product.getWeight() > dimWeight ? product.getWeight() : dimWeight;
		
		return finalWeight * shippingRate.getRate();
	}
}
