/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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
package org.eclipse.birt.report.engine.emitter.ods;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.XMLWriter;
import org.eclipse.birt.report.engine.emitter.ods.layout.OdsContext;
import org.eclipse.birt.report.engine.odf.style.StyleEntry;

/**
 * @author Administrator
 * 
 */
public class OdsWriter implements IOdsWriter {

	private OdsXmlWriter writer, tempWriter;
	private final OdsContext context;
	private final OutputStream out;
	private final boolean isRTLSheet;
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
	public OdsWriter(OutputStream out, OdsContext context, boolean isRtlSheet) {
		this.out = out;
		this.context = context;
		this.isRTLSheet = isRtlSheet;
	}

	public void end() throws IOException {
		writer.end();
		if (tempFilePath != null) {
			File file = new File(tempFilePath);
			if (file.exists() && file.isFile()) {
				file.delete();
			}
		}
	}

	public void endRow() {
		writer.endRow();
	}

	public void outputData(SheetData data, StyleEntry style, int column, int colSpan) throws IOException {
		writer.outputData(data, style, column, colSpan);
	}

	public void start(IReportContent report, HashMap<String, BookmarkDef> bookmarkList) throws IOException {
		writer = new OdsXmlWriter(out, context, isRTLSheet);
		writer.setSheetIndex(sheetIndex);
		writer.start(report, bookmarkList);
		copyOutputData();
	}

	private void copyOutputData() throws IOException {
		if (tempWriter != null) {
			tempWriter.close();
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(new File(tempFilePath)));
				String line = reader.readLine();
				XMLWriter xmlWriter = writer.getWriter();
				while (line != null) {
					xmlWriter.literal("\n");
					xmlWriter.literal(line);
					line = reader.readLine();
				}
			} finally {
				if (reader != null) {
					reader.close();
					reader = null;
				}
			}
		}
	}

	public void startRow(StyleEntry rowStyle) {
		writer.startRow(rowStyle);
	}

	public void startSheet(String name) throws IOException {
		if (writer == null) {
			initializeWriterAsTempWriter();
		}
		writer.startSheet(name);
		sheetIndex++;
	}

	public void startSheet(StyleEntry tableStyle, StyleEntry[] colStyles, String name) throws IOException {
		if (writer == null) {
			initializeWriterAsTempWriter();
		}
		writer.startSheet(tableStyle, colStyles, name);
		sheetIndex++;
	}

	/**
	 * @throws FileNotFoundException
	 * 
	 */
	private void initializeWriterAsTempWriter() throws FileNotFoundException {
		tempFilePath = context.getTempFileDir() + "_BIRTEMITTER_ODS_TEMP_FILE" + Thread.currentThread().getId();
		FileOutputStream out = new FileOutputStream(tempFilePath);
		tempWriter = new OdsXmlWriter(out, context, isRTLSheet);
		writer = tempWriter;
	}

	public void endSheet() {
		writer.endSheet();
	}

	public void startRow() {
		writer.startRow();
	}

	public void outputData(int col, int row, int type, Object value) {
		writer.outputData(col, row, type, value);
	}

	public String defineName(String cells) {
		return writer.defineName(cells);
	}
}
