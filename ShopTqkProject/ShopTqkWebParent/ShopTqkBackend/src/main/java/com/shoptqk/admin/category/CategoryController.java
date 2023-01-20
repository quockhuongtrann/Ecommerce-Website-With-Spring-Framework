package com.shoptqk.admin.category;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shoptqk.admin.AmazonS3Util;
import com.shoptqk.admin.FileUploadUtil;
import com.shoptqk.admin.export.CategoryCsvExporter;
import com.shoptqk.common.entity.Category;
import com.shoptqk.common.exception.CategoryNotFoundException;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/categories")
	public String listFirstPage(@Param("sortDir") String sortDir, Model model) {
		return listByPage(1, sortDir, null, model);
	}
	
	@GetMapping("/categories/page/{pageNum}") 
	public String listByPage(@PathVariable(name = "pageNum") int pageNum,
			@Param("sortDir") String sortDir, 
			@Param("keyword") String keyword,
			Model model) {
		if (sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc";
		}
		
		CategoryPageInfo pageInfo = new CategoryPageInfo();
		List<Category> listCategories = categoryService.listByPage(pageInfo, pageNum, sortDir, keyword);
		
		long startCount = (pageNum - 1) * CategoryService.ROOT_CATEGORIES_PER_PAGE + 1;
		long endCount = startCount + CategoryService.ROOT_CATEGORIES_PER_PAGE - 1;
		if (endCount > pageInfo.getTotalElements()) {
			endCount = pageInfo.getTotalElements();
		}
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("totalPage", pageInfo.getTotalPages());
		model.addAttribute("totalItems", pageInfo.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", "name");
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("reverseSortDir", reverseSortDir);
		return "categories/categories";
	}
	
	@GetMapping("/categories/new")
	public String newCategory(Model model) {
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		model.addAttribute("category", new Category());
		model.addAttribute("pageTitle", "Create New Category");
		model.addAttribute("listCategories", listCategories);
		return "categories/category_form";
	}
	
	@PostMapping("/categories/save")
	public String saveCategory(Category category,
			@RequestParam("fileImage") MultipartFile multipartFile,
			RedirectAttributes redirectAttributes) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			category.setImage(fileName);
			
			Category savedCategory = categoryService.save(category);
			String uploadDir = "category-images/" + savedCategory.getId();
			
			AmazonS3Util.removeFolder(uploadDir);
			AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
		} else {
			categoryService.save(category);
		}
		redirectAttributes.addFlashAttribute("message", "The category has been saved successfully");
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/edit/{id}")
	public String editCategory(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Category category = categoryService.get(id);
			List<Category> listCategories = categoryService.listCategoriesUsedInForm();
			model.addAttribute("category", category);
			model.addAttribute("listCategories", listCategories);
			model.addAttribute("pageTitle", "Edit Category (ID: " + id + ")");
			return "categories/category_form";
		} catch (CategoryNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/categories";
		}
	}
	
	@GetMapping("/categories/{id}/enabled/{status}")
	public String updateCategoryEnabledStatus(@PathVariable("id") Integer id, 
			@PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes) {
		categoryService.updateCategoryEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The category ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/delete/{id}")
	public String deleteCategory(@PathVariable(name = "id") Integer id,
			Model model, RedirectAttributes redirectAttributes) {
		try {
			categoryService.delete(id);
			String categoryDir = "category-images/" + id;
			AmazonS3Util.removeFolder(categoryDir);
			redirectAttributes.addFlashAttribute("message", 
					"The category ID " + id + " has been deleted successfully");
		} catch (CategoryNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		CategoryCsvExporter exporter = new CategoryCsvExporter();
		exporter.export(listCategories, response);
	}
}