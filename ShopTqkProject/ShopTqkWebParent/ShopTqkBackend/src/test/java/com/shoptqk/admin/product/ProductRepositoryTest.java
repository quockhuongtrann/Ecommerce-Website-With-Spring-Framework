package com.shoptqk.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shoptqk.common.entity.Brand;
import com.shoptqk.common.entity.Category;
import com.shoptqk.common.entity.product.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateProduct() {
		Brand brand = entityManager.find(Brand.class, 37);
		Category category = entityManager.find(Category.class, 5);
		
		Product product = new Product();
		product.setName("Acer Aspire Desktop");
		product.setAlias("acer_aspire_desktop");
		product.setShortDescription("Short description for Acer Aspire Desktop");
		product.setFullDescription("Full description for Acer Aspire Desktop");
		
		product.setBrand(brand);
		product.setCategory(category);
		
		product.setPrice(678);
		product.setCost(600);
		product.setEnabled(true);
		product.setInStock(true);
		product.setCreatedTime(new Date());
		product.setUpdatedTime(new Date());
		
		Product savedProduct = productRepository.save(product);
		
		assertThat(savedProduct).isNotNull();
		assertThat(savedProduct.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllProducts() {
		Iterable<Product> iterableProducts = productRepository.findAll();		
		iterableProducts.forEach(product -> System.out.println(product));
	}
	
	@Test
	public void testGetProduct() {
		Integer id = 2;
		Product product = productRepository.findById(id).get();
		System.out.println(product);
		assertThat(product).isNotNull();
	}
	
	@Test
	public void testUpdateProduct() {
		Integer id = 1;
		Product product = productRepository.findById(id).get();
		
		product.setPrice(499);
		
		productRepository.save(product);
		
		Product updatedProduct = entityManager.find(Product.class, id);
		assertThat(updatedProduct.getPrice()).isEqualTo(499);
	}
	
	@Test
	public void testDeleteProduct() {
		Integer id = 3;
		productRepository.deleteById(id);
		
		Optional<Product> result = productRepository.findById(id);
		
		assertThat(!result.isPresent());
	}
	
	@Test
	public void testEnabledProduct() {
		Integer id = 1;
		productRepository.updateEnabledStatus(id, true);
	}
	
	@Test
	public void testDisabledProduct() {
		Integer id = 1;
		productRepository.updateEnabledStatus(id, false);
	}
	
	@Test
	public void testSaveProductWithImages() {
		Integer productId = 1;
		Product product = productRepository.findById(productId).get();
		
		product.setMainImage("main_image.png");
		product.addExtraImages("extra_image_1.png");
		product.addExtraImages("extra_image_2.png");
		product.addExtraImages("extra_image_3.png");
		
		Product savedProduct = productRepository.save(product);
		
		assertThat(savedProduct.getImages().size()).isEqualTo(3);
	}
	
	@Test
	public void testSaveProductWithDetails() {
		Integer productId = 1;
		Product product = productRepository.findById(productId).get();
		
		product.addDetail("Device Memory", "128GB");
		product.addDetail("CPU Model", "MediaTek");
		product.addDetail("OS", "Android 10");
		
		Product savedProduct = productRepository.save(product);
		
		assertThat(savedProduct.getDetails()).isNotEmpty();
	}
	
	@Test
	public void testUpdateReviewCountAndAverageRating() {
		Integer productId = 10;
		productRepository.updateReviewCountAndAverageRating(productId);
	}
	
	@Test
	public void testUpdateReviewCountAndAverageRatingToZero() {
		Integer productId = 10;
		productRepository.updateReviewCountAndAverageRatingToZero(productId);
	}
}
