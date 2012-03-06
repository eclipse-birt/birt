/*******************************************************************************
  * Copyright (c) 2012 Megha Nidhi Dahal.
  * All rights reserved. This program and the accompanying materials
  * are made available under the terms of the Eclipse Public License v1.0
  * which accompanies this distribution, and is available at
  * http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *    Megha Nidhi Dahal - initial API and implementation and/or initial documentation
  *******************************************************************************/
package org.eclipse.birt.report.data.oda.excel.impl.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.eclipse.birt.report.data.oda.excel.ExcelODAConstants;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class XlsxFileReader {
	final private XSSFReader reader;

	public XlsxFileReader(FileInputStream fis) throws IOException,
			OpenXML4JException {
		OPCPackage pkg = OPCPackage.open(fis);
		reader = new XSSFReader(pkg);
	}

	public LinkedHashMap<String, String> getSheetNames()
			throws InvalidFormatException, IOException, SAXException {
		InputStream wbData = reader.getWorkbookData();
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

	public void processSheet(String rid, RowCallBack callback)
			throws InvalidFormatException, IOException, SAXException {
		SharedStringsTable sst = reader.getSharedStringsTable();
		XMLReader parser = fetchSheetParser(sst, callback);
		InputStream sheet = reader.getSheet(rid);
		try {
			InputSource sheetSource = new InputSource(sheet);
			parser.parse(sheetSource);
		} finally {
			if (sheet != null)
				sheet.close();
		}
	}

	private XMLReader fetchSheetParser(SharedStringsTable sst,
			RowCallBack callback) throws SAXException {
		XMLReader parser = XMLReaderFactory
				.createXMLReader("org.apache.xerces.parsers.SAXParser");
		ContentHandler handler = new SheetHandler(sst, callback);
		parser.setContentHandler(handler);
		return parser;
	}

	private XMLReader fetchWorkbookParser(LinkedHashMap<String, String> sheetMap)
			throws SAXException {
		XMLReader parser = XMLReaderFactory
				.createXMLReader("org.apache.xerces.parsers.SAXParser");
		ContentHandler handler = new WorkbookHandler(sheetMap);
		parser.setContentHandler(handler);
		return parser;
	}

	/**
	 * See org.xml.sax.helpers.DefaultHandler javadocs
	 */
	private static class SheetHandler extends DefaultHandler {
		static private enum CellType {
			non, num, staticText, sharedText
		};

		final private SharedStringsTable sst;
		final private RowCallBack callback;
		private String lastContents;
		private CellType cellType;
		private ArrayList<Object> values;
		private int currentIdx = -1;

		private SheetHandler(SharedStringsTable sst, RowCallBack callback) {
			this.sst = sst;
			this.callback = callback;
			values = new ArrayList<Object>();
		}

		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			// c => cell
			if (qName.equals("c")) {
				// Figure out if the value is an index in the SST
				String type = attributes.getValue("t");
				if (type == null) {
					cellType = CellType.num;
				} else if (type.equals("s")) {
					cellType = CellType.sharedText;
				} else if (type.equals("str")) {
					cellType = CellType.staticText;
				} else {
					cellType = CellType.non;
				}
				currentIdx++;
				values.add(currentIdx, ExcelODAConstants.EMPTY_STRING);
			}
			if (qName.equals("row")) {
				currentIdx = -1;
			}
			lastContents = "";
		}

		public void endElement(String uri, String localName, String name)
				throws SAXException {
			if (name.equals("row")) {
				callback.handleRow(values);
				values.clear();
				return;
			} else if (name.equals("c")) {
				cellType = CellType.non;
				return;
			} else if (name.equals("v")) {

				String val = null;

				// Process the last contents as required.
				// Do now, as characters() may be called more than once
				if (cellType == CellType.sharedText) {
					int idx;
					idx = Integer.parseInt(lastContents);
					val = new XSSFRichTextString(sst.getEntryAt(idx))
							.toString();
				} else if (cellType == CellType.staticText
						|| cellType == CellType.num) {
					val = lastContents;
				}

				// v => contents of a cell
				if (val != null) {
					values.remove(currentIdx);
					values.add(currentIdx, val);
				}
			}
		}

		public void characters(char[] ch, int start, int length)
				throws SAXException {
			lastContents += new String(ch, start, length);
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

		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			// <sheet r:id="rId1" name="Sheet1" />
			if (qName.equals("sheet")) {
				String rid = attributes.getValue("r:id");
				String sheetName = attributes.getValue("name");
				sheetMap.put(sheetName, rid);
			}
		}
	}

	/*
	 * public static void main(String[] args) throws Exception { // String
	 * xlsxFileName = "test.xlsx"; String xlsxFileName =
	 * "c:/work/styles/LargeXls.xlsx"; FileInputStream fis = new
	 * FileInputStream(xlsxFileName); XlsxFileReader poiTest = new
	 * XlsxFileReader(fis);
	 *
	 * LinkedHashMap<String, String> sheetMap = poiTest.getSheetNames();
	 *
	 * XlsxRowCallback callback = new XlsxRowCallback(); String rid =
	 * sheetMap.get("Data"); poiTest.processSheet(rid, callback);
	 *
	 *
	 * for (String sheetName : sheetMap.keySet()) { String rid =
	 * sheetMap.get(sheetName); poiTest.processOneSheet(rid, callback); }
	 *
	 * }
	 */

}
