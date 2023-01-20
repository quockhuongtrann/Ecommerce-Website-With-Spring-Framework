package com.shoptqk.review;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ReviewRepositoryTests {
	@Autowired private ReviewRepository reviewRepository;
	
	@Test
	public void testCountByCustomerAndProduct() {
		Integer customerId = 5;
		Integer productId = 1;
		
		Long count = reviewRepository.countByCustomerAndProduct(customerId, productId);
		
		assertThat(count).isEqualTo(1);
	}
}
