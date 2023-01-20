package com.shoptqk.admin.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shoptqk.admin.paging.SearchRepository;
import com.shoptqk.common.entity.product.Product;

public interface ProductRepository extends SearchRepository<Product, Integer> {
	public Product findByName(String name);

	public long countById(Integer id);
	
	@Query("UPDATE Product p SET p.enabled = ?2 WHERE p.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);
	
	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1%" 
			+ "OR p.shortDescription LIKE %?1%"
			+ "OR p.fullDescription LIKE %?1%"
			+ "OR p.brand.name LIKE %?1%"
			+ "OR p.category.name LIKE %?1%")
	public Page<Product> findAll(String keyword, Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE p.category.id = ?1 "
			+ "OR p.category.allParentIDs LIKE %?2%")
	public Page<Product> findAllInCategory(Integer categoryId, String categoryIdMatch, Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE (p.category.id = ?1 "
			+ "OR p.category.allParentIDs LIKE %?2%) AND "
			+ "(p.name LIKE %?3% "
			+ "OR p.shortDescription LIKE %?3% "
			+ "OR p.fullDescription LIKE %?3% "
			+ "OR p.brand.name LIKE %?3% "
			+ "OR p.category.name LIKE %?3%)")
	public Page<Product> searchInCategory(Integer categoryId, String categoryIdMatch,
			String keyword, Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
	public Page<Product> searchProductsByName(String keyword, Pageable pageable);
	
	@Query("UPDATE Product p SET p.averageRating = (SELECT AVG(r.rating) FROM Review r WHERE r.product.id = ?1)," 
			+ " p.reviewCount = (SELECT COUNT(r.id) FROM Review r WHERE r.product.id = ?1) " 
			+ "WHERE p.id = ?1")
	@Modifying
	public void updateReviewCountAndAverageRating(Integer productId);
	
	@Query("UPDATE Product p SET p.averageRating = 0, p.reviewCount = 0 WHERE p.id = ?1")
	public void updateReviewCountAndAverageRatingToZero(Integer productId);
}
