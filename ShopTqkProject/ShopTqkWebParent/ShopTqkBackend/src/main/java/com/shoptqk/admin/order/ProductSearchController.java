package com.shoptqk.admin.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.shoptqk.admin.paging.PagingAndSortingHelper;
import com.shoptqk.admin.paging.PagingAndSortingParam;
import com.shoptqk.admin.product.ProductService;

@Controller
public class ProductSearchController {
	@Autowired private ProductService productService;
	
	@GetMapping("/orders/search_product")
	public String showSearchProductPage() {
		return "orders/search_product";
	}
	
	@PostMapping("/orders/search_product")
	public String searchProducts(String keyword) {
		return "redirect:/orders/search_product/page/1?sortField=name&sortDir=asc&keyword=" + keyword;
	}
	
	@GetMapping("/orders/search_product/page/{pageNum}")
	public String searchProductByPage(@PagingAndSortingParam(listName = "listProducts", 
			moduleURL = "/orders/search_product") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum) {
		productService.searchProducts(pageNum, helper);
		return "orders/search_product";
	}
}
