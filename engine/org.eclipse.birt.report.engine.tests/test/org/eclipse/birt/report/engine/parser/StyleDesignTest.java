/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.engine.parser;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.LabelItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;

/**
 * Test Parser.
 * 
 * test case to test the parser,especially the capability to parse the Style. To
 * get the content about Style from an external file and then compare the
 * expected result with the real result of some basic properties of DataSet. If
 * they are the same,that means the IR is correct, otherwise, there exists
 * errors in the parser
 * 
 */
public class StyleDesignTest extends AbstractDesignTestCase {
	public void setUp() throws Exception {
		loadDesign("style.xml");
	}

	/**
	 * test if the shared style is same.
	 * 
	 * Two element use the same shared style, so those two elements should have same
	 * style properties.
	 */
	public void testSharedStyle() {
		GridItemDesign grid = (GridItemDesign) report.getContent(0);
		IStyle style = report.findStyle(grid.getStyleName());
		assertEquals(style.getColor(), "red");
		assertEquals(style.getBorderBottomStyle(), "solid");

		// Since column style has been transfer to cell style. Engine need process
		// column style now
//		ColumnDesign column = grid.getColumn(0);
//		style = report.findStyle( column.getStyleName( ) );
//		assertEquals( style.getColor( ), "yellow" );

		RowDesign row = grid.getRow(0);
		style = report.findStyle(row.getStyleName());
		assertEquals(style.getColor(), "blue");
		assertEquals(style.getBorderBottomStyle(), "double");

		CellDesign cell = row.getCell(0);
		style = report.findStyle(cell.getStyleName());
		assertEquals(style.getColor(), "gray");
		assertEquals(style.getFontSize(), "7pt");
		assertEquals(style.getBorderBottomStyle(), "none");

		LabelItemDesign label = (LabelItemDesign) cell.getContent(0);
		assertTrue(label.getStyleName() == null);
	}
}
