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

package org.eclipse.birt.report.model.parser;

import java.util.List;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColorHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.TableColumn;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.interfaces.ICellModel;
import org.eclipse.birt.report.model.metadata.ColorPropertyType;

import com.ibm.icu.util.ULocale;

/**
 * The test case of <code>GridItem</code> parser and writer.
 * <p>
 * <code>TableColumn</code>,<code>TableRow</code> and <code>Cell</code> are also
 * tested in this test case.
 *
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse: *
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 *
 * <tr>
 * <td>{@link #testParser()}</td>
 * <td>Test properties of GridItem after parsing design file</td>
 * <td>All properties are right</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Test properties of TableColumn after parsing design file</td>
 * <td>All properties are right</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Test properties of TableRow after parsing design file</td>
 * <td>All properties are right</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Test properties of Cell after parsing design file</td>
 * <td>All properties are right</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testWriter()}</td>
 * <td>Compare the written file with the golden file</td>
 * <td>Two files are same</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testSemanticCheck()}</td>
 * <td>Test inconsistant column count error, properties of column, row and cell
 * are negative.</td>
 * <td>Error found</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Test horizontal cell overlapping error</td>
 * <td>Error found</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Test vertical cell overlapping error</td>
 * <td>Error found</td>
 * </tr>
 * </table>
 *
 * @see GridItem
 * @see TableColumn
 * @see TableRow
 * @see Cell
 */

public class GridItemParseTest extends ParserTestCase {

	String fileName = "GridItemParseTest.xml"; //$NON-NLS-1$
	String goldenFileName = "GridItemParseTest_golden.xml"; //$NON-NLS-1$
	String semanticCheckFileName = "GridItemParseTest_1.xml"; //$NON-NLS-1$

	/**
	 * Test parser and its properties.
	 *
	 * @throws Exception
	 *
	 */

	public void testParser() throws Exception {
		openDesign(fileName);

		SlotHandle body = designHandle.getBody();

		GridHandle grid = (GridHandle) designHandle.findElement("My grid"); //$NON-NLS-1$

		assertNotNull(grid);
		assertEquals(0, body.findPosn(grid));

		assertEquals("summary", grid.getSummary()); //$NON-NLS-1$

		assertEquals("bluehero", grid.getCaption()); //$NON-NLS-1$

		assertEquals("blue he", grid.getCaptionKey()); //$NON-NLS-1$

		assertEquals("Grid Role", grid.getTagType()); //$NON-NLS-1$
		assertEquals("English", grid.getLanguage()); //$NON-NLS-1$
		assertEquals(1, grid.getOrder()); // $NON-NLS-1$
		// Test column properties

		SlotHandle columns = grid.getColumns();
		assertEquals(2, columns.getCount());

		ColumnHandle column = (ColumnHandle) columns.get(0);
		assertEquals(2.5, column.getWidth().getMeasure(), 0.1);
		assertEquals(3, column.getRepeatCount());
		assertEquals("My-Style", column.getStyle().getName()); //$NON-NLS-1$

		assertNull(column.getPropertyDefn(Style.PAGE_BREAK_INSIDE_PROP));

		// Test row peoperties

		SlotHandle rows = grid.getRows();
		assertEquals(2, rows.getCount());

		RowHandle row = (RowHandle) rows.get(0);
		assertEquals(5, row.getHeight().getMeasure(), 1);

		assertEquals("This is bookmark for section.", row.getBookmark()); //$NON-NLS-1$

		assertEquals("My-Style", row.getStyle().getName()); //$NON-NLS-1$

		// Test cell properties

		SlotHandle cells = row.getCells();
		assertEquals(2, cells.getCount());

		CellHandle cell = (CellHandle) cells.get(0);
		assertEquals(DesignChoiceConstants.LINE_STYLE_NONE, cell.getDiagonalStyle());
		assertEquals(DesignChoiceConstants.LINE_STYLE_NONE, cell.getAntidiagonalStyle());
		assertEquals(0, cell.getDiagonalNumber());
		assertEquals(0, cell.getAntidiagonalNumber());
		assertEquals(ColorPropertyType.BLACK, cell.getDiagonalColor().getStringValue()); // $NON-NLS-1$
		assertEquals(ColorPropertyType.BLACK, cell.getAntidiagonalColor().getStringValue()); // $NON-NLS-1$
		DimensionHandle dimensionHandle = cell.getDiagonalThickness();
		assertEquals(DesignChoiceConstants.LINE_WIDTH_MEDIUM, dimensionHandle.getStringValue());
		dimensionHandle = cell.getAntidiagonalThickness();
		assertEquals(DesignChoiceConstants.LINE_WIDTH_MEDIUM, dimensionHandle.getStringValue());

		// test default value of Role in Cell
		assertEquals("td", cell.getTagType()); //$NON-NLS-1$

		cell = (CellHandle) cells.get(1);
		assertEquals(2, cell.getColumn());
		assertEquals(3, cell.getColumnSpan());
		assertEquals(1, cell.getRowSpan());
		assertEquals("all", cell.getDrop()); //$NON-NLS-1$
		assertEquals("1.5mm", cell.getHeight().getStringValue()); //$NON-NLS-1$
		assertEquals("2mm", cell.getWidth().getStringValue()); //$NON-NLS-1$
		assertEquals("red", cell.getPrivateStyle().getBackgroundColor().getStringValue()); //$NON-NLS-1$

		assertEquals("Cell Role", cell.getTagType()); //$NON-NLS-1$
		assertEquals("English", cell.getLanguage()); //$NON-NLS-1$
		assertEquals("Alt Text", cell.getAltTextExpression().getStringExpression()); //$NON-NLS-1$

		SlotHandle content = cell.getContent();
		LabelHandle label = (LabelHandle) content.get(0);
		assertEquals("address", label.getName()); //$NON-NLS-1$

		// tests the given value of the diagonal line.
		assertEquals(DesignChoiceConstants.LINE_STYLE_SOLID, cell.getDiagonalStyle());
		assertEquals(DesignChoiceConstants.LINE_STYLE_DASHED, cell.getAntidiagonalStyle());
		assertEquals(10, cell.getDiagonalNumber());
		assertEquals(20, cell.getAntidiagonalNumber());
		assertEquals(ColorPropertyType.RED, cell.getDiagonalColor().getStringValue()); // $NON-NLS-1$
		assertEquals(ColorPropertyType.BLUE, cell.getAntidiagonalColor().getStringValue()); // $NON-NLS-1$
		dimensionHandle = cell.getDiagonalThickness();
		assertEquals("10mm", dimensionHandle.getStringValue()); //$NON-NLS-1$
		dimensionHandle = cell.getAntidiagonalThickness();
		assertEquals("20mm", dimensionHandle.getStringValue()); //$NON-NLS-1$

		assertEquals(DesignChoiceConstants.SCOPE_TYPE_ROWGROUP, cell.getScope());
		assertEquals("bookmark for cell", cell.getBookmark()); //$NON-NLS-1$
		assertEquals("headers for cell", cell.getHeaders()); //$NON-NLS-1$

		// reads in a grid that exists in the components.

		grid = (GridHandle) designHandle.findElement("componentsGrid"); //$NON-NLS-1$
		// test default value of Role in Grid
		assertEquals("sect", grid.getTagType()); //$NON-NLS-1$

		assertNotNull(grid);

		// reads in a grid that exists in the scratch pad.

		grid = (GridHandle) designHandle.findElement("scratchpadGrid"); //$NON-NLS-1$

		assertNotNull(grid);

		// reads in a grid that exists in the simple master page.

		grid = (GridHandle) designHandle.findElement("simplemasterpageHeaderGrid"); //$NON-NLS-1$

		assertNotNull(grid);

		// reads in a grid that exists in the free form.

		grid = (GridHandle) designHandle.findElement("freeformGrid"); //$NON-NLS-1$

		assertNotNull(grid);

		// reads in a grid that exists in the list header.

		grid = (GridHandle) designHandle.findElement("listHeaderGrid"); //$NON-NLS-1$

		assertNotNull(grid);

		// reads in a grid that exists in the list detail.

		grid = (GridHandle) designHandle.findElement("listDetailGrid"); //$NON-NLS-1$

		assertNotNull(grid);

		// reads in a grid that exists in the list footer.

		grid = (GridHandle) designHandle.findElement("listFooterGrid"); //$NON-NLS-1$

		assertNotNull(grid);

		// reads in a grid that exists in the list group header.

		grid = (GridHandle) designHandle.findElement("listgroupHeaderGrid"); //$NON-NLS-1$

		assertNotNull(grid);

		// reads in a grid that exists in the list group footer.

		grid = (GridHandle) designHandle.findElement("listgroupFooterGrid"); //$NON-NLS-1$

		assertNotNull(grid);

		// reads in a grid that exists in the cell.

		grid = (GridHandle) designHandle.findElement("cellGrid"); //$NON-NLS-1$

		assertNotNull(grid);

	}

	/**
	 * This test writes the design file and compare it with golden file.
	 *
	 * @throws Exception
	 *
	 */
	public void testWriter() throws Exception {
		openDesign(fileName, ULocale.ENGLISH);
		GridHandle grid = (GridHandle) designHandle.findElement("My grid"); //$NON-NLS-1$

		grid.setCaption("new caption"); //$NON-NLS-1$
		grid.setCaptionKey("new caption key"); //$NON-NLS-1$
		grid.setSummary("new summary"); //$NON-NLS-1$

		grid.setTagType("Grid Role"); //$NON-NLS-1$
		grid.setLanguage("English"); //$NON-NLS-1$
		grid.setOrder(1); // $NON-NLS-1$

		SlotHandle rows = grid.getRows();
		RowHandle row = (RowHandle) rows.get(0);
		SlotHandle cells = row.getCells();
		CellHandle cell = (CellHandle) cells.get(1);
		cell.setDiagonalStyle(DesignChoiceConstants.LINE_STYLE_INSET);
		cell.setAntidiagonalStyle(DesignChoiceConstants.LINE_STYLE_OUTSET);
		cell.setDiagonalNumber(20);
		cell.setAntidiagonalNumber(30);
		cell.setProperty(ICellModel.DIAGONAL_THICKNESS_PROP, "1.5mm"); //$NON-NLS-1$
		cell.setProperty(ICellModel.ANTIDIAGONAL_THICKNESS_PROP, "2.5mm"); //$NON-NLS-1$

		cell.setTagType("Cell Role"); //$NON-NLS-1$
		cell.setLanguage("English"); //$NON-NLS-1$
		cell.setAltTextExpression(new Expression("Alt Text", ExpressionType.CONSTANT)); //$NON-NLS-1$

		ColorHandle colorHandle = cell.getDiagonalColor();
		colorHandle.setStringValue(ColorPropertyType.YELLOW); // $NON-NLS-1$

		colorHandle = cell.getAntidiagonalColor();
		colorHandle.setStringValue(ColorPropertyType.OLIVE); // $NON-NLS-1$

		cell.setScope(DesignChoiceConstants.SCOPE_TYPE_COL);
		cell.setBookmark("new bookmark for cell");//$NON-NLS-1$
		cell.setHeaders("new headers for cell");//$NON-NLS-1$

		save();
		assertTrue(compareFile(goldenFileName));
	}

	/**
	 * Test semantic check error.
	 *
	 * @throws Exception
	 */
	public void testSemanticCheck() throws Exception {
		openDesign(semanticCheckFileName);
		List<ErrorDetail> errors = design.getErrorList();
		assertEquals(3, errors.size());

		int i = 0;

		ErrorDetail error = errors.get(i++);
		assertEquals("First grid", error.getElement().getName()); //$NON-NLS-1$
		assertEquals(SemanticError.DESIGN_EXCEPTION_INCONSITENT_GRID_COL_COUNT, error.getErrorCode());

		error = errors.get(i++);
		assertEquals(SemanticError.DESIGN_EXCEPTION_OVERLAPPING_CELLS, error.getErrorCode());

		error = errors.get(i++);
		assertEquals(SemanticError.DESIGN_EXCEPTION_OVERLAPPING_CELLS, error.getErrorCode());
	}

}
