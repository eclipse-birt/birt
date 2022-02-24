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
import com.ibm.icu.util.ULocale;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.metadata.ColorPropertyType;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test cases for compound extends.
 */
public class CompoundExtendParserTest extends BaseTestCase {

	private final static String INPUT1 = "CompoundExtendParserTest.xml"; //$NON-NLS-1$
	private final static String INPUT2 = "CompoundExtendParserTest2.xml"; //$NON-NLS-1$
	private final static String INPUT3 = "CompoundExtendParserTest3.xml"; //$NON-NLS-1$

	private final static String GOLDEN_FILE = "CompoundExtendParserTest_golden.xml"; //$NON-NLS-1$
	private final static String GOLDEN_FILE2 = "CompoundExtendParserTest_golden2.xml"; //$NON-NLS-1$

	private final static String semanticErrorCheckFileName = "CompoundExtendParserTest4.xml"; //$NON-NLS-1$

	/**
	 * Tests all properties and slots.
	 * 
	 * @throws Exception if any exception
	 */

	public void testParser() throws Exception {
		openDesign(INPUT1, ULocale.ENGLISH);

		// verify the color in the table in the component

		TableHandle baseTable = (TableHandle) designHandle.findElement("baseTable"); //$NON-NLS-1$
		assertEquals(ColorPropertyType.GREEN,
				baseTable.getHeader().get(0).getElement().getLocalProperty(design, Style.COLOR_PROP));

		TableHandle interTable = (TableHandle) designHandle.findElement("innerTable"); //$NON-NLS-1$
		assertNotNull(interTable);

		assertEquals(ColorPropertyType.YELLOW, interTable.getHeader().get(0).getElement().getSlot(TableRow.CONTENT_SLOT)
				.getContent(0).getLocalProperty(design, Style.COLOR_PROP));

		// verify the overridden color in the design.

		TableHandle bodyTable = (TableHandle) designHandle.findElement("table1"); //$NON-NLS-1$
		assertEquals("New Design Table", bodyTable.getCaption()); //$NON-NLS-1$

		TableRow row = (TableRow) bodyTable.getElement().getSlot(TableItem.HEADER_SLOT).getContent(0);

		assertEquals(ColorPropertyType.BLUE, row.getLocalProperty(design, Style.COLOR_PROP));

		CellHandle bodyCell = (CellHandle) bodyTable.getDetail().get(1).getSlot(TableRow.CONTENT_SLOT).get(0);

		TableHandle bodyInnerTable = (TableHandle) bodyCell.getContent().get(0);
		RowHandle bodyInnerRow = (RowHandle) bodyInnerTable.getHeader().get(0);
		CellHandle bodyInnerCell = (CellHandle) bodyInnerRow.getCells().get(0);
		assertEquals(ColorPropertyType.LIME, bodyInnerCell.getElement().getLocalProperty(design, Style.COLOR_PROP));

	}

	/**
	 * Test property search.
	 * <p>
	 * 1) Child element property search.
	 * <p>
	 * 2) Virtual element property search.
	 * <p>
	 * 3) Intrinsic property search, name and style property can not extends.
	 * 
	 * @throws DesignFileException
	 */

	public void testPropertySearch() throws DesignFileException {
		openDesign(INPUT3, ULocale.ENGLISH);

		GridHandle gridHandle = (GridHandle) designHandle.findElement("Grid1"); //$NON-NLS-1$

		// 1. Grid extends its property value from parent.

		assertEquals("24pt", gridHandle.getHeight().getStringValue()); //$NON-NLS-1$
		assertEquals("40mm", gridHandle.getWidth().getStringValue()); //$NON-NLS-1$

		RowHandle rowHandle1 = (RowHandle) gridHandle.getRows().get(0);
		CellHandle cellHandle1 = (CellHandle) rowHandle1.getCells().get(0);
		LabelHandle labelHandle = (LabelHandle) cellHandle1.getContent().get(0);
		LabelHandle labelHandle1 = (LabelHandle) cellHandle1.getContent().get(1);

		// 2. test virtual element overridden properties.

		// overriden "height"
		assertEquals("28mm", cellHandle1.getHeight().getStringValue()); //$NON-NLS-1$

		// "width" value extends from virtual parent
		assertEquals("64pt", cellHandle1.getWidth().getStringValue()); //$NON-NLS-1$

		// Test override text-property "text"
		assertEquals("New Address", labelHandle.getText()); //$NON-NLS-1$

		// Test override externalized-property "textID"
		assertEquals("new_text_key", labelHandle.getTextKey()); //$NON-NLS-1$

		// 3. test intrinsic properties.

		// Test overridden intrinsic property "name"
		assertEquals("Child Label(Address)", labelHandle.getName()); //$NON-NLS-1$

		// Name and style property can not extends.

		assertEquals("baseLabel21", labelHandle1.getName()); //$NON-NLS-1$
		assertEquals(null, labelHandle1.getStyle());

		// Test override intrinsic property "style".
		assertEquals("style1", labelHandle.getStyle().getName()); //$NON-NLS-1$

		// Test getProperty from local style
		assertEquals("red", labelHandle.getStringProperty(StyleHandle.COLOR_PROP)); //$NON-NLS-1$
		assertEquals("12mm", labelHandle.getStringProperty(StyleHandle.FONT_SIZE_PROP)); //$NON-NLS-1$

		DataItemHandle dataHandle = (DataItemHandle) cellHandle1.getContent().get(2);

		// Test override expression-property "name"

		assertEquals("dataSetRow[\"STUDENT_ID\"]", dataHandle //$NON-NLS-1$
				.getResultSetExpression());

		assertEquals(9, labelHandle.getElement().getBaseId());
	}

	/**
	 * Test cases:
	 * <p>
	 * 1. Ensure that if an element has parent, its contents(slot) will be ignored
	 * during parsing, a semantic warning is logged.
	 * <p>
	 * 2. If baseId doesn't reference a correct virtual parent, the ref-entry is
	 * ignored. A semantic warning is logged.
	 * <p>
	 * 
	 * @throws Exception
	 */

	public void testSemanticWarning() throws Exception {
		openDesign(INPUT2, ULocale.ENGLISH);
		List<ErrorDetail> errors = designHandle.getErrorList();
		assertEquals(4, errors.size());

		ErrorDetail error1 = errors.get(0);
		assertEquals(ContentException.DESIGN_EXCEPTION_STRUCTURE_CHANGE_FORBIDDEN, error1.getErrorCode());

		ErrorDetail error2 = errors.get(1);
		assertEquals(ContentException.DESIGN_EXCEPTION_STRUCTURE_CHANGE_FORBIDDEN, error2.getErrorCode());

		ErrorDetail error3 = errors.get(2);
		assertEquals(DesignParserException.DESIGN_EXCEPTION_VIRTUAL_PARENT_NOT_FOUND, error3.getErrorCode());

		ErrorDetail error4 = errors.get(3);
		assertEquals(DesignParserException.DESIGN_EXCEPTION_VIRTUAL_PARENT_NOT_FOUND, error4.getErrorCode());

		save();
		compareFile(GOLDEN_FILE2);
	}

	/**
	 * Test semantic errors during parsing. Properties that will changed the
	 * structure is not allowed to be overridden. Properties include: cell.colSpan;
	 * cell.rowSpan; cell.drop; cell.column
	 */

	public void testSemanticWarning2() {
		try {
			openDesign(semanticErrorCheckFileName, ULocale.ENGLISH);
		} catch (DesignFileException e) {
			List<ErrorDetail> errors = e.getErrorList();
			assertEquals(4, errors.size());

			assertEquals(PropertyValueException.DESIGN_EXCEPTION_PROPERTY_CHANGE_FORBIDDEN,
					errors.get(0).getErrorCode());
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_PROPERTY_CHANGE_FORBIDDEN,
					errors.get(1).getErrorCode());
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_PROPERTY_CHANGE_FORBIDDEN,
					errors.get(2).getErrorCode());
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_PROPERTY_CHANGE_FORBIDDEN,
					errors.get(3).getErrorCode());
		}
	}

	/**
	 * Tests writing the properties.
	 * 
	 * @throws Exception if any error found.
	 */

	public void testWriter() throws Exception {
		openDesign(INPUT1, ULocale.ENGLISH);

		// verify the overridden color in the design.

		TableHandle bodyTable = (TableHandle) designHandle.findElement("table1"); //$NON-NLS-1$
		assertEquals("New Design Table", bodyTable.getCaption()); //$NON-NLS-1$

		RowHandle bodyRow = (RowHandle) bodyTable.getDetail().get(1);
		bodyRow.getPrivateStyle().getColor().setStringValue(ColorPropertyType.FUCHSIA);
		bodyRow.getHeight().setAbsolute(1.1);
		bodyRow.setBookmark("http://www.eclipse.org/birt"); //$NON-NLS-1$

		CellHandle bodyCell = (CellHandle) bodyRow.getCells().get(0);
		bodyCell.getPrivateStyle().getColor().setStringValue(ColorPropertyType.RED);

		TableHandle bodyInnerTable = (TableHandle) bodyCell.getContent().get(0);
		RowHandle bodyInnerRow = (RowHandle) bodyInnerTable.getHeader().get(0);
		CellHandle bodyInnerCell = (CellHandle) bodyInnerRow.getCells().get(0);

		bodyInnerCell.getPrivateStyle().getColor().setStringValue(ColorPropertyType.NAVY);

		GridHandle grid1 = (GridHandle) designHandle.findElement("grid1"); //$NON-NLS-1$
		LabelHandle label1 = (LabelHandle) ((CellHandle) ((RowHandle) grid1.getRows().get(0)).getCells().get(0))
				.getContent().get(0);
		label1.setName("new label"); //$NON-NLS-1$
		label1.setStyleName("style1"); //$NON-NLS-1$

		save();
		assertTrue(compareFile(GOLDEN_FILE));
	}
}
