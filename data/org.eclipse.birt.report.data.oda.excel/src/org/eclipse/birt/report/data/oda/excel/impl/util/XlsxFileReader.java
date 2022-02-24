/*******************************************************************************
  * Copyright (c) 2012 Megha Nidhi Dahal and others.
  * All rights reserved. This program and the accompanying materials
  * are made available under the terms of the Eclipse Public License v2.0
  * which accompanies this distribution, and is available at
  * http://www.eclipse.org/legal/epl-2.0.html
  *
  * Contributors:
  *    Megha Nidhi Dahal - initial API and implementation and/or initial documentation
  *    Actuate Corporation - more efficient xlsx processing;
  *         support of timestamp, datetime, time, and date data types
  *    Actuate Corporation - support defining an Excel input file path or URI as part of the data source definition
  *******************************************************************************/

package org.eclipse.birt.report.data.oda.excel.impl.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.eclipse.birt.report.data.oda.excel.ExcelODAConstants;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.ibm.icu.text.SimpleDateFormat;

public class XlsxFileReader {
	final static String PARSER_CLASS_NAME = "org.apache.xerces.parsers.SAXParser"; //$NON-NLS-1$
	final static String ROW_LIMIT_REACHED_EX_MSG = "Row Limit Reached"; //$NON-NLS-1$

	final private XSSFReader reader;

	enum cDataType {
		BOOL, DATE, DATETIME, FORMULA, SSTINDEX, TIME, NUMBER, STATIC
	}

	public XlsxFileReader(InputStream fis) throws IOException, OpenXML4JException {
		OPCPackage pkg = OPCPackage.open(fis);
		reader = new XSSFReader(pkg);
	}

	public LinkedHashMap<String, String> getSheetNames() throws InvalidFormatException, IOException, SAXException {
		BufferedInputStream wbData = new BufferedInputStream(reader.getWorkbookData());
		LinkedHashMap<String, String> sheetMap = new LinkedHashMap<String, String>();
		try {
			InputSource wbSource = new InputSource(wbData);
			XMLReader parser = fetchWorkbookParser(sheetMap);
			parser.parse(wbSource);
		} finally {
			if (wbData != null)
				wbData.close();
		}
		return sheetMap;
	}

	public void processSheet(String rid, XlsxRowCallBack callback, int xlsxRowsToRead)
			throws InvalidFormatException, IOException, SAXException {
		SharedStringsTable sst = reader.getSharedStringsTable();
		StylesTable st = reader.getStylesTable();

		XMLReader parser = fetchSheetParser(st, sst, callback, xlsxRowsToRead);
		BufferedInputStream sheet = new BufferedInputStream(reader.getSheet(rid));
		try {
			InputSource sheetSource = new InputSource(sheet);
			parser.parse(sheetSource);
		} finally {
			if (sheet != null)
				sheet.close();
		}
	}

	private XMLReader getXMLReader() throws SAXException {
		try {
			return XMLReaderFactory.createXMLReader();
		} catch (SAXException e) {
			try {
				return (XMLReader) Class.forName(PARSER_CLASS_NAME).newInstance();
			} catch (Exception e1) {
				throw e;
			}
		}
	}

	private XMLReader fetchSheetParser(StylesTable st, SharedStringsTable sst, XlsxRowCallBack callback,
			int xlsxRowsToRead) throws SAXException {
		XMLReader parser = getXMLReader();
		ContentHandler handler = new SheetHandler(st, sst, callback, xlsxRowsToRead);
		parser.setContentHandler(handler);
		return parser;
	}

	private XMLReader fetchWorkbookParser(LinkedHashMap<String, String> sheetMap) throws SAXException {

		XMLReader parser = getXMLReader();
		ContentHandler handler = new WorkbookHandler(sheetMap);
		parser.setContentHandler(handler);
		return parser;
	}

	/**
	 * See org.xml.sax.helpers.DefaultHandler javadocs
	 */
	private static class SheetHandler extends DefaultHandler {

		private cDataType cellDataType;
		private int columnCount = 1;
		final private SharedStringsTable sst;
		final private StylesTable st;
		final private XlsxRowCallBack callback;
		private String lastContents;
		private ArrayList<Object> values;
		private int currentColumn = 0;
		private int xlsxRowsToRead = 0;
		private int currentXlsxRowNumber = 0;
		private SimpleDateFormat sdf;

		private SheetHandler(StylesTable st, SharedStringsTable sst, XlsxRowCallBack callback, int xlsxRowsToRead) {
			this.sst = sst;
			this.st = st;
			this.callback = callback;
			values = new ArrayList<Object>();
			this.cellDataType = cDataType.NUMBER;
			this.xlsxRowsToRead = xlsxRowsToRead;
			sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");// ISO date format
		}

		public void startElement(String uri, String localName, String qName, Attributes attributes)
				throws SAXException {
			if (qName.equals("c")) {
				String vCellType = attributes.getValue("t");
				String cellS = attributes.getValue("s");
				if ("b".equals(vCellType))
					cellDataType = cDataType.BOOL;
				else if ("e".equals(vCellType))
					cellDataType = cDataType.FORMULA;
				else if ("s".equals(vCellType))
					cellDataType = cDataType.SSTINDEX;
				else if ("str".equals(vCellType))
					cellDataType = cDataType.STATIC;
				else if (cellS != null) {
					// number with formatting or date
					int styleIndex = Integer.parseInt(cellS);
					XSSFCellStyle style = st.getStyleAt(styleIndex);
					short formatIndex = style.getDataFormat();
					String formatString = style.getDataFormatString();

					if (formatString == null)
						formatString = BuiltinFormats.getBuiltinFormat(formatIndex);

					if (org.apache.poi.ss.usermodel.DateUtil.isADateFormat(formatIndex, formatString)) {
						cellDataType = cDataType.DATETIME;
					} else {
						cellDataType = cDataType.NUMBER;
					}
				} else
					cellDataType = cDataType.NUMBER;

				String r = attributes.getValue("r");

				currentColumn = getColumnNumber(r);
				// expand the number of columns if needed in existing rows
				if (currentColumn + 1 > columnCount) {
					callback.columnExpansion(currentColumn + 1);

					// clean up current row
					int newvals = (currentColumn + 1) - columnCount;
					for (int ii = 0; ii < newvals; ii++) {
						values.add(ExcelODAConstants.EMPTY_STRING);
					}

					columnCount = currentColumn + 1;
				}

			}

			// empty cells are not in the xml so we have
			// create them in the row
			if (qName.equals("row")) {
				for (int i = 0; i < columnCount; i++) {
					values.add(i, ExcelODAConstants.EMPTY_STRING);
				}
			}
			lastContents = ExcelODAConstants.EMPTY_STRING;
		}

		public void endElement(String uri, String localName, String name) throws SAXException {
			if (name.equals("row")) {
				callback.handleRow(values);
				values.clear();
				currentColumn = -1;
				currentXlsxRowNumber++;
				if (xlsxRowsToRead > 0) {
					if (currentXlsxRowNumber > xlsxRowsToRead) {
						throw new SAXException(ROW_LIMIT_REACHED_EX_MSG);
					}
				}
				return;
			} else if (name.equals("c")) {
				cellDataType = cDataType.NUMBER;
				return;
			} else if (name.equals("v")) {

				String val = ExcelODAConstants.EMPTY_STRING;

				// Process the last contents as required.
				// Do now, as characters() may be called more than once
				if (cellDataType == cDataType.SSTINDEX) {
					int idx;
					idx = Integer.parseInt(lastContents);
					val = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
				} else if (cellDataType == cDataType.STATIC || cellDataType == cDataType.NUMBER) {
					val = lastContents;
				} else if (cellDataType == cDataType.DATETIME || cellDataType == cDataType.DATE
						|| cellDataType == cDataType.TIME) {

					Date myjavadate = org.apache.poi.ss.usermodel.DateUtil
							.getJavaDate(Double.parseDouble(lastContents));
					val = sdf.format(myjavadate);
				} else if (cellDataType == cDataType.BOOL) {
					if (lastContents.compareTo("1") == 0) {
						Boolean mybool = new Boolean(true);
						val = mybool.toString();
					} else if (lastContents.compareTo("0") == 0) {
						Boolean mybool = new Boolean(false);
						val = mybool.toString();
					}
				}

				// v => contents of a cell
				if (val != null) {
					if (currentColumn != -1) {
						values.remove(currentColumn);
						values.add(currentColumn, val);
					}
				}
			}
		}

		public void characters(char[] ch, int start, int length) throws SAXException {
			lastContents += new String(ch, start, length);
		}

		private int getColumnNumber(String colname) {
			int tmpcol = 0;
			String drpNumber = colname;
			for (int ch = 0; ch < colname.length(); ++ch) {
				if (!Character.isLetter(colname.charAt(ch))) {
					drpNumber = colname.substring(0, ch);
					break;
				}
			}

			int sum = 0;
			for (int ii = 0; ii < drpNumber.length(); ii++) {
				tmpcol = (drpNumber.charAt(ii) - 'A') + 1;
				sum = sum * 26 + tmpcol;
			}
			return sum - 1;

		}
	}

	/**
	 * See org.xml.sax.helpers.DefaultHandler javadocs
	 */
	private static class WorkbookHandler extends DefaultHandler {
		final private LinkedHashMap<String, String> sheetMap;

		private WorkbookHandler(LinkedHashMap<String, String> sheetMap) {
			this.sheetMap = sheetMap;
		}

		public void startElement(String uri, String localName, String qName, Attributes attributes)
				throws SAXException {
			// <sheet r:id="rId1" name="Sheet1" />
			if (qName.equals("sheet")) {
				String rid = attributes.getValue("r:id");
				String sheetName = attributes.getValue("name");
				sheetMap.put(sheetName, rid);
			}
		}
	}

}
