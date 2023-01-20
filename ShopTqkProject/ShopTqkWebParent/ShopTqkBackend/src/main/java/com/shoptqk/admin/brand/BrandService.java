package com.shoptqk.admin.brand;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shoptqk.admin.paging.PagingAndSortingHelper;
import com.shoptqk.common.entity.Brand;
import com.shoptqk.common.entity.Category;

@Service
public class BrandService {
	
	public static final int BRANDS_PER_PAGE = 4;
	
	@Autowired
	private BrandRepository brandRepository;
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, BRANDS_PER_PAGE, brandRepository);
	}
	
	public List<Brand> listAll() {
		return (List<Brand>) brandRepository.findAll();
	}
	
	public Brand save(Brand brand) {
		return brandRepository.save(brand);
	}

	public Brand get(Integer id) throws BrandNotFoundException {
		try {
			return brandRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new BrandNotFoundException("Could not find any brand with ID: " + id);
		}
	}
	
	public void delete(Integer id) throws BrandNotFoundException{
		Long countById = brandRepository.countById(id);
		if (countById == null || countById == 0) {
			throw new BrandNotFoundException("Could not find any brand with ID " + id);
		}
		brandRepository.deleteById(id);
	}

	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);
		Brand brandByName = brandRepository.findByName(name);
		if (isCreatingNew) {
			if (brandByName != null) {
				return "DuplicateName";
			}
		} else {
			if (brandByName != null && brandByName.getId() != id) {
				return "DuplicateName";
			}
		}
		return "OK";
	}
	
}
