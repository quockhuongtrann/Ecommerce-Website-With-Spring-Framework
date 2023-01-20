package com.shoptqk.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shoptqk.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CategoryRepositoryTests {
	@Autowired private CategoryRepository categoryRepository;
	
	
	@Test
	public void testListEnabledCategories() {
		List<Category> categories = categoryRepository.findAllEnabled();
		categories.forEach(category -> {
			System.out.println(category.getName() + " (" + category.isEnabled() + ")");
		});
	}
	
	@Test
	public void testFindCategoryByAlias() {
		String alias = "electronics";
		Category category = categoryRepository.findByAliasEnabled(alias);
		assertThat(category).isNotNull();
	}
}
