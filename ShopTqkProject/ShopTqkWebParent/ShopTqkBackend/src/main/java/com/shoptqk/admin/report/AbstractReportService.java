package com.shoptqk.admin.report;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class AbstractReportService{
	protected DateFormat dateFormatter;
	
	public List<ReportItem> getReportDataLast7Days(ReportType reportType) throws ParseException {
		return getReportDataLastXDays(7, reportType);
	}
	
	public List<ReportItem> getReportDataLast28Days(ReportType reportType) throws ParseException {
		return getReportDataLastXDays(28, reportType);
	}
	
	protected List<ReportItem> getReportDataLastXDays(int days, ReportType reportType) throws ParseException {
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd"); 
		Date endTime = dateFormatter.parse("2021-10-15");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endTime);
		calendar.add(Calendar.DAY_OF_MONTH, -(days - 1));
		Date startTime = calendar.getTime();
		
		System.out.println("Start time: " + startTime);
		System.out.println("End time: " + endTime);
		return getReportDataByDateRangeInternal(startTime, endTime, reportType);
	}
	
	public List<ReportItem> getReportDataLast6Months(ReportType reportType) throws ParseException {
		return getReportDataLastXMonths(6, reportType);
	}
	
	public List<ReportItem> getReportDataLastYear(ReportType reportType) throws ParseException {
		return getReportDataLastXMonths(12, reportType);
	}
	
	protected List<ReportItem> getReportDataLastXMonths(int months, ReportType reportType) throws ParseException {
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd"); 
		Date endTime = dateFormatter.parse("2021-10-15");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endTime);
		calendar.add(Calendar.MONTH, -(months - 1));
		Date startTime = calendar.getTime();
		
		System.out.println("Start time: " + startTime);
		System.out.println("End time: " + endTime);
		
		dateFormatter = new SimpleDateFormat("yyyy-MM"); 
		return getReportDataByDateRangeInternal(startTime, endTime, reportType);
	}
	
	public List<ReportItem> getReportDataByDateRange(Date startTime, Date endTime, ReportType reportType) {
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd"); 
		return getReportDataByDateRangeInternal(startTime, endTime, reportType);
	}
	
	protected abstract List<ReportItem> getReportDataByDateRangeInternal(
			Date startDate, Date endDate, ReportType reportType);
}
