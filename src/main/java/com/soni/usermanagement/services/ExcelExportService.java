package com.soni.usermanagement.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import com.soni.usermanagement.dto.AccountsAppResponse;
import com.soni.usermanagement.model.ContactManagement;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

/**
 * ExcelExportService
 */
@Service
public class ExcelExportService {
	
	public static String contactListToExcelFile(List<ContactManagement> accountApps) {
		try(Workbook workbook = new XSSFWorkbook()){
			Sheet sheet = workbook.createSheet("Contacts");
			
			Row row = sheet.createRow(0);
	        CellStyle headerCellStyle = workbook.createCellStyle();
	        headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            
	        // Creating header
	        Cell cell = row.createCell(0);
	        cell.setCellValue("ID");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(1);
	        cell.setCellValue("App Code");
	        cell.setCellStyle(headerCellStyle);
	
	        cell = row.createCell(2);
	        cell.setCellValue("File Code");
	        cell.setCellStyle(headerCellStyle);
	
	        cell = row.createCell(3);
	        cell.setCellValue("File Type Code");
            cell.setCellStyle(headerCellStyle);
            
            cell = row.createCell(4);
	        cell.setCellValue("Contacts");
	        cell.setCellStyle(headerCellStyle);
	        
	        // Creating data rows for each customer
	        for(int i = 0; i < accountApps.size(); i++) {
	        	Row dataRow = sheet.createRow(i + 1);
	        	dataRow.createCell(0).setCellValue(accountApps.get(i).getId());
	        	dataRow.createCell(1).setCellValue(accountApps.get(i).getAppCode());
	        	dataRow.createCell(2).setCellValue(accountApps.get(i).getFileCode());
                dataRow.createCell(3).setCellValue(accountApps.get(i).getFileTypeCode());
                dataRow.createCell(4).setCellValue(accountApps.get(i).getContacts());
	        }
	
	        // Making size of column auto resize to fit with data
	        sheet.autoSizeColumn(0);
	        sheet.autoSizeColumn(1);
	        sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
			
			String currDir = Paths.get("").toAbsolutePath().toString();
			String filePath = currDir + "\\accountApps.xlsx";
			
			try(FileOutputStream fout = new FileOutputStream(
				new File(filePath))) {
				workbook.write(fout);
			}

			return filePath;
            // return new ByteArrayInputStream(outputStream.toByteArray());
            
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static String accountAppListToExcel(List<AccountsAppResponse> accountApps) {
		try(Workbook workbook = new XSSFWorkbook()){
			Sheet sheet = workbook.createSheet("AccountApps");
			
			Row row = sheet.createRow(0);
	        CellStyle headerCellStyle = workbook.createCellStyle();
	        headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            
	        // Creating header
	        Cell cell = row.createCell(0);
	        cell.setCellValue("ID");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(1);
	        cell.setCellValue("Account Code");
	        cell.setCellStyle(headerCellStyle);
	
	        cell = row.createCell(2);
	        cell.setCellValue("IBAN");
	        cell.setCellStyle(headerCellStyle);
	
	        cell = row.createCell(3);
	        cell.setCellValue("Bank Code");
            cell.setCellStyle(headerCellStyle);
            
            cell = row.createCell(4);
	        cell.setCellValue("Entity");
			cell.setCellStyle(headerCellStyle);
			
			cell = row.createCell(5);
	        cell.setCellValue("KMT54");
			cell.setCellStyle(headerCellStyle);
			
			cell = row.createCell(6);
	        cell.setCellValue("Format");
			cell.setCellStyle(headerCellStyle);
			
			cell = row.createCell(7);
	        cell.setCellValue("Last Updated User");
			cell.setCellStyle(headerCellStyle);
			
			cell = row.createCell(8);
	        cell.setCellValue("Last Updated Date");
			cell.setCellStyle(headerCellStyle);
			
			cell = row.createCell(9);
	        cell.setCellValue("File Code");
			cell.setCellStyle(headerCellStyle);
			
			cell = row.createCell(10);
	        cell.setCellValue("AppCode");
	        cell.setCellStyle(headerCellStyle);
	        
	        // Creating data rows for each customer
	        for(int i = 0; i < accountApps.size(); i++) {
	        	Row dataRow = sheet.createRow(i + 1);
	        	dataRow.createCell(0).setCellValue(accountApps.get(i).getID());
	        	dataRow.createCell(1).setCellValue(accountApps.get(i).getAccountCode());
	        	dataRow.createCell(2).setCellValue(accountApps.get(i).getIban());
                dataRow.createCell(3).setCellValue(accountApps.get(i).getBankCode());
				dataRow.createCell(4).setCellValue(accountApps.get(i).getEntity());
				dataRow.createCell(5).setCellValue(accountApps.get(i).getIsKMT54());
				dataRow.createCell(6).setCellValue(accountApps.get(i).getFormat());
				dataRow.createCell(7).setCellValue(accountApps.get(i).getLastUpdatedUserEmail());
				dataRow.createCell(8).setCellValue(accountApps.get(i).getLastUpdatedDate());
				dataRow.createCell(9).setCellValue(accountApps.get(i).getFileCode());
				dataRow.createCell(10).setCellValue(accountApps.get(i).getApplicationCode());
				
	        }
	
	        // Making size of column auto resize to fit with data
	        sheet.autoSizeColumn(0);
	        sheet.autoSizeColumn(1);
	        sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
            sheet.autoSizeColumn(6);
            sheet.autoSizeColumn(7);
            sheet.autoSizeColumn(8);
            sheet.autoSizeColumn(9);
            sheet.autoSizeColumn(10);
			
			String currDir = Paths.get("").toAbsolutePath().toString();
			String filePath = currDir + "\\accountApps.xlsx";
			
			try(FileOutputStream fout = new FileOutputStream(
				new File(filePath))) {
				workbook.write(fout);
			}

			return filePath;
            // return new ByteArrayInputStream(outputStream.toByteArray());
            
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}
}