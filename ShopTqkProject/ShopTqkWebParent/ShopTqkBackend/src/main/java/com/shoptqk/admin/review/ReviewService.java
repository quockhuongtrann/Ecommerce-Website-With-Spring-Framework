package com.shoptqk.admin.review;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoptqk.admin.paging.PagingAndSortingHelper;
import com.shoptqk.admin.product.ProductRepository;
import com.shoptqk.common.entity.Review;
import com.shoptqk.common.entity.product.Product;
import com.shoptqk.common.exception.ReviewNotFoundException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReviewService {
	public static final int REVIEWS_PER_PAGE = 5;
	@Autowired private ReviewRepository reviewRepository;
	@Autowired private ProductRepository productRepository;
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, REVIEWS_PER_PAGE, reviewRepository);
	}
	
	public Review get(Integer id) throws ReviewNotFoundException {
		try {
			return reviewRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new ReviewNotFoundException("Could not find any reviews with ID " + id);
		}
	}
	
	public void save(Review reviewInForm) {
		Review reviewInDB = reviewRepository.findById(reviewInForm.getId()).get();
		reviewInDB.setHeadline(reviewInForm.getHeadline());
		reviewInDB.setComment(reviewInForm.getComment());
		
		reviewRepository.save(reviewInDB);
		List<Review> listReviews = reviewRepository.findByProductId(reviewInDB.getProduct().getId());
		if (listReviews.size() > 0) {
			productRepository.updateReviewCountAndAverageRating(reviewInDB.getProduct().getId());
		} else {
			Product product = productRepository.findById(reviewInDB.getProduct().getId()).get();
			product.setAverageRating(0);
			product.setReviewCount(0);
			productRepository.save(product);
		}
	}
	
	public void delete(Integer id) throws ReviewNotFoundException {
		if (!reviewRepository.existsById(id)) {
			throw new ReviewNotFoundException("Could not find any reviews with ID " + id);
		}
		
		reviewRepository.deleteById(id);
	}
}
