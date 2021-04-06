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

package org.eclipse.birt.report.engine.parser;

import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.ColumnDesign;
import org.eclipse.birt.report.engine.ir.GroupDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.TableBandDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;

/**
 * Test Parser.
 * 
 */
public class TableItemDesignTest extends AbstractDesignTestCase {

	public void setUp() throws Exception {
		loadDesign("table.xml");
	}

	/**
	 * test case to test the parser,especially the capability to parse the Table. To
	 * get the content about Table from an external file and then compare the
	 * expected result with the real result of some basic properties of DataSet. If
	 * they are the same,that means the IR is correct, otherwise, there exists
	 * errors in the parser
	 */

	public void testTable() {
		TableItemDesign table = (TableItemDesign) report.getContent(0);
		assertEquals("Table Caption", table.getCaption());
		// test columns
		assertEquals(6, table.getColumnCount());
		ColumnDesign column = table.getColumn(0);
		assertEquals("7cm", column.getWidth().toString());

		// test header
		TableBandDesign header = (TableBandDesign) table.getHeader();
		assertEquals(1, header.getRowCount());
		RowDesign row = header.getRow(0);
		assertEquals(row.getBookmark().getScriptText(), "row[\"COLUMN_32\"]");
		assertEquals(1, row.getCellCount());
		CellDesign cell = row.getCell(0);
		assertEquals(6, cell.getColSpan());
		assertEquals(1, cell.getContentCount());

		// test groups
		assertEquals(2, table.getGroupCount());
		GroupDesign group = (GroupDesign) table.getGroup(0);
		// group header
		header = (TableBandDesign) group.getHeader();
		assertEquals(1, header.getRowCount());
		row = header.getRow(0);
		assertEquals(6, row.getCellCount());
		cell = row.getCell(0);
		assertEquals("all", cell.getDrop());
		cell = row.getCell(1);
		assertEquals("detail", cell.getDrop());

		TableBandDesign footer = (TableBandDesign) group.getFooter();
		assertEquals(1, footer.getRowCount());

		// group footer

		// test details
		TableBandDesign detail = (TableBandDesign) table.getDetail();
		assertEquals(1, detail.getRowCount());

		// test footer
		assertEquals(1, detail.getRowCount());
	}
}