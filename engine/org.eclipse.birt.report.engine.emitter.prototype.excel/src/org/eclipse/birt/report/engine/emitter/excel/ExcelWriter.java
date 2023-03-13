/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.excel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.excel.ExcelXmlWriter.XMLWriterXLS;
import org.eclipse.birt.report.engine.emitter.excel.layout.ExcelContext;

/**
 * @author Administrator
 *
 */
public class ExcelWriter implements IExcelWriter {

	private ExcelXmlWriter writer, tempWriter;
	private final ExcelContext context;
	private String tempFilePath;
	private int sheetIndex = 1;

	/**
	 * @param out
	 * @param context
	 * @param isRtlSheet
	 * @param pageHeader
	 * @param pageFooter
	 * @param orientation
	 */
	public ExcelWriter(ExcelContext context) {
		this.context = context;
	}

	@Override
	public void end() throws IOException {
		writer.end();
	}

	@Override
	public void endRow() {
		writer.endRow();
	}

	@Override
	public void endSheet(double[] coordinates, String oritentation, int pageWidth, int pageHeight, float leftMargin,
			float rightMargin, float topMargin, float bottomMargin) {
		writer.endSheet(coordinates, oritentation, pageWidth, pageHeight, leftMargin, rightMargin, topMargin,
				bottomMargin);
	}

	@Override
	public void outputData(String sheet, SheetData data, StyleEntry style, int column, int colSpan) throws IOException {
		// TODO: ignored sheet temporarily
		outputData(data, style, column, colSpan);
	}

	@Override
	public void outputData(SheetData data, StyleEntry style, int column, int colSpan) throws IOException {
		writer.outputData(data, style, column, colSpan);
	}

	@Override
	public void start(IReportContent report, Map<StyleEntry, Integer> styles,
			// TODO: style ranges.
			// List<ExcelRange> styleRanges,
			HashMap<String, BookmarkDef> bookmarkList) throws IOException {
		writer = new ExcelXmlWriter(context);
		writer.setSheetIndex(sheetIndex);
		// TODO: style ranges.
		// writer.start( report, styles, styleRanges, bookmarkList );
		writer.start(report, styles, bookmarkList);
		copyOutputData();
	}

	private void copyOutputData() throws IOException {
		if (tempWriter != null) {
			tempWriter.close();
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(tempFilePath), "UTF-8"));
				String line = reader.readLine();
				XMLWriterXLS xlsWriter = writer.getWriter();
				while (line != null) {
					xlsWriter.literal("\n");
					xlsWriter.literal(line);
					line = reader.readLine();
				}
			} finally {
				if (reader != null) {
					reader.close();
					reader = null;
					File file = new File(tempFilePath);
					if (file.exists() && file.isFile()) {
						file.delete();
					}
					tempFilePath = null;
					tempWriter = null;
				}
			}
		}
	}

	@Override
	public void startRow(double rowHeight) {
		writer.startRow(rowHeight);
	}

	@Override
	public void startSheet(String name) throws IOException {
		if (writer == null) {
			initializeWriterAsTempWriter();
		}
		writer.startSheet(name);
		sheetIndex++;
	}

	@Override
	public void startSheet(double[] coordinates, String pageHeader, String pageFooter, String name) throws IOException {
		if (writer == null) {
			initializeWriterAsTempWriter();
		}
		writer.startSheet(coordinates, pageHeader, pageFooter, name);
		sheetIndex++;
	}

	/**
	 * @throws FileNotFoundException
	 *
	 */
	private void initializeWriterAsTempWriter() throws FileNotFoundException {
		String tempFolder = context.getTempFileDir();
		if (!(tempFolder.endsWith("/") || tempFolder.endsWith("\\"))) {
			tempFolder = tempFolder.concat("/");
		}
		tempFilePath = tempFolder + "birt_xls_" + UUID.randomUUID().toString();
		FileOutputStream out = new FileOutputStream(tempFilePath);
		tempWriter = new ExcelXmlWriter(out, context);
		writer = tempWriter;
	}

	@Override
	public void endSheet() {
		writer.endSheet();
	}

	@Override
	public void startRow() {
		writer.startRow();
	}

	@Override
	public void outputData(int col, int row, int type, Object value) {
		writer.outputData(col, row, type, value);
	}

	@Override
	public String defineName(String cells) {
		return writer.defineName(cells);
	}
}
