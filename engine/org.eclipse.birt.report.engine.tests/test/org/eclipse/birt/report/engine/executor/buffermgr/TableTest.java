/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.executor.buffermgr;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import junit.framework.TestCase;

/**
 * test the table layout.
 * 
 * In the testcases, read in a table model, and render it. Compare the render
 * output with the golden output to see if the table layout algorithm is
 * correct.
 * 
 */
public class TableTest extends TestCase {

	public void testFixedLayout() throws Exception {
		executeTest("fixed");
	}

	public void testFloatLayout() throws Exception {
		executeTest("float");
	}

	public void testSpanLayout() throws Exception {
		executeTest("span");
	}

	public void testConflictLayout() throws Exception {
		executeTest("conflict");
	}

	public void testDropLayout() throws Exception {
		executeTest("drop");
	}

	public void testEmptyCellLayout() throws Exception {
		executeTest("empty");
	}

	// append the cell into table row by row, cell by cell, and get the resolved
	// table layout.

	void executeTest(String name) throws Exception {
		String sourceFile = "org/eclipse/birt/report/engine/executor/buffermgr/" + name + ".txt";
		String goldenFile = "org/eclipse/birt/report/engine/executor/buffermgr/" + name + ".golden";

		InputStream in = getClass().getClassLoader().getResourceAsStream(sourceFile);
		Table table = createTable(in);
		assertEquals(false, table.hasDropCell());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		outputTable(table, out);
		byte[] output = out.toByteArray();
		byte[] golden = loadResource(goldenFile);
		assertEquals(new String(golden).replaceAll("\\s", ""), new String(output).replaceAll("\\s", ""));

	}

	byte[] loadResource(String name) throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(name);
		if (in != null) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int readSize;
			do {
				readSize = in.read(buffer);
				if (readSize > 0) {
					out.write(buffer, 0, readSize);
				}
			} while (readSize != -1);
			return out.toByteArray();
		}
		return null;
	}

	Table createTable(InputStream in) throws IOException {
		Table table = new Table();
		// each line in the stream defines a ROW or CELL, looks like:
		// ROW ID
		// CELL COLID ROWSPAN COLSPAN CELLID
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));

		String line = reader.readLine();
		while (line != null) {
			if (!line.startsWith("#")) {
				String[] tokens = line.split(" ");
				if (tokens != null && tokens.length > 0) {
					if ("ROW".equals(tokens[0])) {
						String rowName = tokens[1];
						table.createRow(rowName);
					} else if ("CELL".equals(tokens[0])) {
						int colId = Integer.parseInt(tokens[1]);
						int rowSpan = Integer.parseInt(tokens[2]);
						int colSpan = Integer.parseInt(tokens[3]);
						String cellName = tokens[4];
						table.createCell(colId, rowSpan, colSpan, new CellContent(cellName));
					} else if ("BAND".equals(tokens[0])) {
						int bandId = Integer.parseInt(tokens[1]);
						table.resolveDropCells(bandId);
					}
				}
			}
			line = reader.readLine();
		}
		return table;

	}

	void outputTable(Table table, OutputStream out) throws IOException {
		PrintWriter writer = new PrintWriter(out);
		int rowCount = table.getRowCount();
		int colCount = table.getColCount();
		for (int i = 0; i < rowCount; i++) {
			Row row = table.getRow(i);
			writer.println("ROW " + row.getContent());
			for (int j = 0; j < colCount; j++) {
				Cell cell = table.getCell(i, j);
				if (cell.getStatus() == Cell.CELL_USED) {
					writer.println("CELL " + CellUtil.getColId(cell) + " " + CellUtil.getRowSpan(cell) + " "
							+ CellUtil.getColSpan(cell) + " " + CellUtil.getContent(cell));
				} else if (cell.getStatus() == Cell.CELL_EMPTY) {
					writer.println("CELL " + j + " 1 1 EMPTY");
				}
			}
		}
		writer.flush();
	}

	class CellContent implements Cell.Content {
		String content;

		CellContent(String obj) {
			content = obj;
		}

		public boolean isEmpty() {
			return "NULL".equals(content);
		}

		public String toString() {
			return content;
		}

		public void reset() {
			content = null;
		}
	}
}