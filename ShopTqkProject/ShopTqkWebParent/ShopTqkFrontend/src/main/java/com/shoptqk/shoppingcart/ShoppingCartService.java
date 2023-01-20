package com.shoptqk.shoppingcart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoptqk.common.entity.CartItem;
import com.shoptqk.common.entity.Customer;
import com.shoptqk.common.entity.product.Product;
import com.shoptqk.product.ProductRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ShoppingCartService {
	
	@Autowired private CartItemRepository cartItemRepository;
	@Autowired private ProductRepository productRepository;
	public Integer addProduct(Integer productId, Integer quantity, Customer customer) throws ShoppingCartException {
		Integer updatedQuantity = quantity;
		Product product = new Product(productId);
		
		CartItem cartItem = cartItemRepository.findByCustomerAndProduct(customer, product);
		
		if (cartItem != null) {
			updatedQuantity = cartItem.getQuantity() + quantity;
			
			if (updatedQuantity > 5) {
				throw new ShoppingCartException("Could not add more " + quantity + " item(s) "
						+ "because there's is already " + cartItem.getQuantity() + " item(s) "
						+ "in your shopping cart. Maximum allowed quantity is 5.");
			}
		} else {
			cartItem = new CartItem();
			cartItem.setCustomer(customer);
			cartItem.setProduct(product);
		}
		cartItem.setQuantity(updatedQuantity);
		cartItemRepository.save(cartItem);
		
		return updatedQuantity;
	}
	
	public List<CartItem> listCartItems(Customer customer) {
		return cartItemRepository.findByCustomer(customer);
	}
	
	public float updateQuantity(Integer productId, Integer quantity, Customer customer) {
		cartItemRepository.updateQuantity(quantity, customer.getId(), productId);
		Product product = productRepository.findById(productId).get();
		float subTotal = product.getDiscountPrice() * quantity;
		return subTotal;
	}
	
	public void removeProduct(Integer productId, Customer customer) {
		cartItemRepository.deleteByCustomerAndProduct(customer.getId(), productId);
	}
	
	public void deleteByCustomer(Customer customer) {
		cartItemRepository.deleteByCustomer(customer.getId());
	}
}
