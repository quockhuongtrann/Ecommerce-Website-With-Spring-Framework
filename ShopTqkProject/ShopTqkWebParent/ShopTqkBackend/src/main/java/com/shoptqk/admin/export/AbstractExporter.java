package com.shoptqk.admin.export;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.servlet.http.HttpServletResponse;

public class AbstractExporter {
	public void setResponseHeader(HttpServletResponse response, String contentType,
			String moduleName, String extension) throws IOException {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-ss_HH-mm-ss");
		String timestamp = dateFormatter.format(new Date());
		String fileName = moduleName + "_" + timestamp + extension;
		response.setContentType(contentType);
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName;
		response.setHeader(headerKey, headerValue);
	}
}
