package com.shoptqk.admin.export;

import java.io.IOException;
import java.util.List;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shoptqk.common.entity.Category;

import jakarta.servlet.http.HttpServletResponse;

public class CategoryCsvExporter extends AbstractExporter {
	public void export(List<Category> listCategories, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv", "categories", ".csv");
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
										CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader = {"Category ID", "Category Name", "Alias", "Enabled"};
		String[] fieldMapping = {"id", "name", "alias", "enabled"};
		
		csvWriter.writeHeader(csvHeader);
		for (Category category : listCategories) {
			category.setName(category.getName().replace("--", "  "));
			csvWriter.write(category, fieldMapping);
		}
		
		csvWriter.close();
	}
}
