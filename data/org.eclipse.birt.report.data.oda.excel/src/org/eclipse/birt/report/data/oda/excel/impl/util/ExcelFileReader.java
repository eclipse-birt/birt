/*******************************************************************************
  * Copyright (c) 2012 Megha Nidhi Dahal and others.
  * All rights reserved. This program and the accompanying materials
  * are made available under the terms of the Eclipse Public License v1.0
  * which accompanies this distribution, and is available at
  * http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *    Megha Nidhi Dahal - initial API and implementation and/or initial documentation
  *    Actuate Corporation - more efficient xlsx processing;
  *         support of timestamp, datetime, time, and date data types
  *******************************************************************************/

package org.eclipse.birt.report.data.oda.excel.impl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.birt.report.data.oda.excel.ExcelODAConstants;
import org.eclipse.birt.report.data.oda.excel.impl.i18n.Messages;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.xml.sax.SAXException;

public class ExcelFileReader {

	private FileInputStream fis;
	private String fileExtension;
	private List<String> workSheetList;
	static LinkedHashMap<String, String> xlsxWorkSheetList;
	private int currentSheetIndex = 0;

	private Workbook workBook;
	private Sheet sheet;
	private FormulaEvaluator formulaEvaluator;

	private boolean isInitialised;

	private int maxRowsInAllSheet;
	private int maxRowsInThisSheet;
	private int currentRowIndex = 0;
	private int maxColumnIndex = 0;
	private int xlsxRowsToRead;
	private XlsxRowCallBack callback;
	private XlsxFileReader xlsxread;
	Map<String, String> xlsxSheetRidNameMap;

	public void setMaxColumnIndex(int maxColumnIndex) {
		this.maxColumnIndex = maxColumnIndex;
	}

	public ExcelFileReader(FileInputStream fis, String fileExtension,
			List<String> sheetNameList, int rowsToRead) {
		this.fis = fis;
		this.fileExtension = fileExtension;
		this.workSheetList = sheetNameList;
		this.xlsxRowsToRead = rowsToRead;
	}

	public List<String> readLine() throws IOException, OdaException {
		if (!isInitialised)
			initialise();

		if (currentRowIndex >= maxRowsInThisSheet) {
			if (!initialiseNextSheet())
				return null;
		}
		List<String> rowData = new ArrayList<String>();
		if (!isXlsxFile(fileExtension)) {
			Row row = sheet.getRow(currentRowIndex);
			if (row != null) {
				if (maxColumnIndex == 0)
					maxColumnIndex = row.getLastCellNum();

				for (short colIx = 0; colIx < maxColumnIndex; colIx++) {
					Cell cell = row.getCell(colIx);
					rowData.add(getCellValue(cell));
				}

			} else {
				return null;
			}
		} else {
			rowData = callback.getRow(currentRowIndex);
		}

		currentRowIndex++;
		return rowData;

	}

	public void close() throws IOException {
		this.fis.close();
	}

	private void initialise() throws IOException, OdaException {
		try {
			if (isXlsxFile(fileExtension)) {
				xlsxread = new XlsxFileReader(fis);
				callback = new XlsxRowCallBack();
				xlsxSheetRidNameMap = xlsxread.getSheetNames();
				String rid = xlsxSheetRidNameMap.get(workSheetList.get(currentSheetIndex));
				xlsxread.processSheet(rid, callback, this.xlsxRowsToRead);
				maxRowsInThisSheet = callback.getMaxRowsInSheet();

				for (String sheetName : workSheetList) {
					rid = xlsxSheetRidNameMap.get(sheetName);
					if (rid == null)
						throw new OdaException(
								Messages.getString("invalid_sheet_name")); //$NON-NLS-1$

					XlsxRowCallBack newCallback = new XlsxRowCallBack();
					xlsxread.processSheet(rid, newCallback, this.xlsxRowsToRead);
					maxRowsInAllSheet += newCallback.getMaxRowsInSheet();
				}

			} else {

				workBook = new HSSFWorkbook(fis);
				formulaEvaluator = workBook.getCreationHelper()
						.createFormulaEvaluator();
				workBook.setMissingCellPolicy(Row.RETURN_NULL_AND_BLANK);
				sheet = workBook.getSheet(workSheetList.get(currentSheetIndex));
				maxRowsInThisSheet = sheet.getPhysicalNumberOfRows();

				for (String sheetName : workSheetList) {
					Sheet localSheet = workBook.getSheet(sheetName);
					maxRowsInAllSheet += localSheet.getPhysicalNumberOfRows();
				}
			}
			isInitialised = true;
		} catch (NullPointerException e) {
			throw new OdaException(e);
		} catch (OpenXML4JException e) {
			throw new OdaException(e);
		} catch (SAXException e) {
			if( e.getMessage().equalsIgnoreCase( XlsxFileReader.ROW_LIMIT_REACHED_EX_MSG ) ){
				maxRowsInThisSheet = callback.getMaxRowsInSheet();
				isInitialised = true;
			}else{
				throw new OdaException(e);
			}
		}
	}

	private boolean initialiseNextSheet() throws IOException, OdaException {
		if (workSheetList.size() <= ++currentSheetIndex) {
			return false;
		}
		if (isXlsxFile(fileExtension)) {
			try {
				String rid = xlsxSheetRidNameMap.get(workSheetList.get(currentSheetIndex));
				callback = new XlsxRowCallBack();
				xlsxread.processSheet(rid, callback,this.xlsxRowsToRead);
				maxRowsInThisSheet = callback.getMaxRowsInSheet();
			} catch (OpenXML4JException e) {
				throw new OdaException(e);
			} catch (SAXException e) {
				throw new OdaException(e);
			}
		} else {
			do {
				sheet = workBook.getSheet(workSheetList.get(currentSheetIndex));
				maxRowsInThisSheet = sheet.getPhysicalNumberOfRows();
			} while (maxRowsInThisSheet == 0
					&& (workSheetList.size() < ++currentSheetIndex));
		}
		if (maxRowsInThisSheet == 0)
			return false;

		currentRowIndex = 0;
		return true;
	}

	private static boolean isXlsxFile(String extension) {
		return !extension.equals(ExcelODAConstants.XLS_FORMAT);
	}

	public String getCellValue(Cell cell) {
		if (cell == null)
			return ExcelODAConstants.EMPTY_STRING;

		if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
			return resolveFormula(cell);
		}

		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
				Date myjavadate = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(cell.getNumericCellValue());
				long millis = myjavadate.getTime();
				return Long.toString(millis);
			}
			return ((Double) cell.getNumericCellValue()).toString();
		}

		return cell.toString();
	}

	private String resolveFormula(Cell cell) {
		if (formulaEvaluator == null)
			return cell.toString();

		if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell) ){
			//need to check for nulls
			//double myexdate = org.apache.poi.ss.usermodel.DateUtil.getExcelDate(cell.getDateCellValue());
			Date myjavadate = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(cell.getNumericCellValue());
			long millis = myjavadate.getTime();
			return Long.toString(millis);

		}

		switch (formulaEvaluator.evaluateFormulaCell(cell)) {
		case Cell.CELL_TYPE_BOOLEAN:
			return ((Boolean) cell.getBooleanCellValue()).toString();

		case Cell.CELL_TYPE_NUMERIC:
			return ((Double) cell.getNumericCellValue()).toString();

		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();

		default:
			return null;
		}
	}

	public int getMaxRows() throws IOException, OdaException {
		if (!isInitialised)
			initialise();
		return maxRowsInAllSheet;
	}

	public static List<String> getSheetNamesInExcelFile(File file) {
		String extension = file.getName();
		extension = extension.substring(extension.lastIndexOf(".") + 1, //$NON-NLS-1$
				extension.length());
		FileInputStream fis;
		List<String> sheetNames = new ArrayList<String>();
		try {
			fis = new FileInputStream(file);

			if (isXlsxFile(extension)) {
				XlsxFileReader poiRdr = new XlsxFileReader(fis);
				xlsxWorkSheetList = poiRdr.getSheetNames();
				for (Map.Entry<String, String> entry : xlsxWorkSheetList
						.entrySet()) {
					sheetNames.add(entry.getKey());
				}
			} else {
				Workbook workBook = new HSSFWorkbook(fis);
				for (int i = 0; i < workBook.getNumberOfSheets(); i++) {
					sheetNames.add(workBook.getSheetName(i));
				}
			}
		} catch (FileNotFoundException e) {
			// do nothing
		} catch (IOException e) {
			// do nothing
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sheetNames;
	}
}
