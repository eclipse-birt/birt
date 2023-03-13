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

package org.eclipse.birt.report.model.api;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.command.StyleException;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.metadata.IColorConstants;
import org.eclipse.birt.report.model.api.metadata.ISlotDefn;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests the paste and canContain methods in the SlotHandle.
 *
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 *
 * <tr>
 * <td>{@link #testPaste()}</td>
 * <td>Tests paste a data-set to another design.</td>
 * <td>DataSource referred by the data-set was invalid.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Tests paste a data-set to the same design.</td>
 * <td>DataSource referred by the data-set was valid and the back references
 * were changed.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Tests paste a text item with a shared style to another design.</td>
 * <td>The share style referred by the text item was invalid.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Tests paste a text item with a shared style to the same design.</td>
 * <td>The shared style referred by the text item was valid and the back
 * references were changed.</td>
 * </tr>
 *
 * </table>
 *
 */

public class SlotHandleTest extends BaseTestCase {

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#setUp()
	 */

	@Override
	protected void setUp() throws Exception {
		createDesign();
	}

	/**
	 * Tests paste methods with element references.
	 *
	 * @throws SemanticException
	 *
	 */

	public void testPaste() throws SemanticException {
		ElementFactory factory = new ElementFactory(design);
		SharedStyleHandle style = factory.newStyle("style"); //$NON-NLS-1$
		style.getColor().setValue(IColorConstants.AQUA);
		designHandle.getStyles().add(style);

		designHandle.getDataSources().add(factory.newOdaDataSource("DataSource1")); //$NON-NLS-1$

		DataSetHandle dataset = factory.newOdaDataSet("DataSet1"); //$NON-NLS-1$
		designHandle.getDataSets().add(dataset);
		dataset.setDataSource("DataSource1"); //$NON-NLS-1$

		TextItemHandle text = factory.newTextItem("text"); //$NON-NLS-1$
		text.setStyle(style);

		// test on copy/paste for a datasource and a dataset

		ReportDesignHandle newDesignHandle = sessionHandle.createDesign();
		ReportDesign newDesign = (ReportDesign) newDesignHandle.getModule();

		OdaDataSetHandle newDataSet = (OdaDataSetHandle) dataset.copy().getHandle(newDesign);
		List errors = newDesignHandle.getDataSets().paste(newDataSet);
		assertEquals(1, errors.size());
		assertEquals(SemanticError.DESIGN_EXCEPTION_INVALID_ELEMENT_REF, ((ErrorDetail) errors.get(0)).getErrorCode());

		assertNull(newDataSet.getDataSource());

		// if copy/paste to the same report, no errors.

		newDataSet = (OdaDataSetHandle) dataset.copy().getHandle(design);
		designHandle.rename(newDataSet);
		errors = designHandle.getDataSets().paste(newDataSet);

		assertEquals(0, errors.size());
		assertNotNull(newDataSet.getDataSource());

		Iterator iter = designHandle.findDataSource("DataSource1") //$NON-NLS-1$
				.clientsIterator();
		int count = 0;
		while (iter.hasNext()) {
			count++;
			iter.next();
		}
		assertEquals(2, count);

		// test on copy/paste for a styledElement and Style

		TextItemHandle newText = (TextItemHandle) text.copy().getHandle(newDesign);
		errors = newDesignHandle.getBody().paste(newText);
		assertEquals(1, errors.size());
		assertEquals(StyleException.DESIGN_EXCEPTION_NOT_FOUND, ((ErrorDetail) errors.get(0)).getErrorCode());
		assertEquals(IColorConstants.BLACK, newText.getProperty(Style.COLOR_PROP));

		// if copy/paste to the same report, no errors.

		newText = (TextItemHandle) text.copy().getHandle(design);
		designHandle.rename(newText);
		errors = designHandle.getBody().paste(newText);
		assertEquals(0, errors.size());

		assertEquals(IColorConstants.AQUA, newText.getProperty(Style.COLOR_PROP));

		iter = style.clientsIterator();
		count = 0;
		while (iter.hasNext()) {
			count++;
			iter.next();
		}
		assertEquals(2, count);

		// copy row/column that is without extends definition

		TableHandle table = factory.newTableItem("table1", 3); //$NON-NLS-1$

		ColumnHandle column = (ColumnHandle) table.getColumns().get(0);
		IDesignElement tocopy = column.copy();
		table.getColumns().paste(tocopy, 1);
	}

	/**
	 * Test cases:
	 *
	 * <ul>
	 * <li>copy and paste a parameter group.
	 * </ul>
	 *
	 * @throws Exception
	 *
	 */

	public void testPasteCompoundElements() throws Exception {
		ParameterGroupHandle paramGroup1 = designHandle.getElementFactory().newParameterGroup("group1"); //$NON-NLS-1$
		ParameterHandle param1 = designHandle.getElementFactory().newScalarParameter("param1"); //$NON-NLS-1$

		paramGroup1.getParameters().add(param1);
		designHandle.getParameters().add(paramGroup1);

		ParameterGroupHandle paramGroup2 = (ParameterGroupHandle) paramGroup1.copy().getHandle(design);
		designHandle.rename(paramGroup2);

		List errors = designHandle.getParameters().paste(paramGroup2);
		assertEquals(0, errors.size());
	}

	/**
	 * Test the forbidden operations in compound extends cases.
	 *
	 * @throws NameException
	 * @throws DesignFileException
	 */

	public void testCompoundExtendsOperations() throws NameException, DesignFileException {
		openDesign("SlotHandleTest.xml"); //$NON-NLS-1$
		ElementFactory factory = new ElementFactory(design);

		GridHandle grid1Handle = (GridHandle) designHandle.findElement("Grid1"); //$NON-NLS-1$
		assertNotNull(grid1Handle.getExtends());

		// Drop the top level child element should be successful.
		GridHandle grid2Handle = (GridHandle) designHandle.findElement("Grid2"); //$NON-NLS-1$
		try {
			grid2Handle.drop();
		} catch (SemanticException e1) {
			fail();
		}

		// Child element is not allowed to change structure.
		RowHandle newRow = factory.newTableRow();
		RowHandle firstRowHandle = (RowHandle) grid1Handle.getRows().get(0);

		assertFalse(firstRowHandle.canDrop());
		assertFalse(grid1Handle.getRows().canContain(newRow));
		assertFalse(grid1Handle.getRows().canContain(ReportDesignConstants.CELL_ELEMENT));

		// 2. Content operation is not allowed in top level child(Grid1).

		// 2.1 Add operation forbidden.
		try {
			grid1Handle.addElement(newRow, GridHandle.ROW_SLOT);
			fail();
		} catch (ContentException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_STRUCTURE_CHANGE_FORBIDDEN, e.getErrorCode());
		}

		// 2.2 Drop(int) operation forbidden.
		try {
			grid1Handle.getSlot(GridHandle.ROW_SLOT).drop(0);
			fail();
		} catch (SemanticException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_STRUCTURE_CHANGE_FORBIDDEN, e.getErrorCode());
		}

		// 2.3 Drop(DesignElementHandle) operation forbidden.
		try {
			grid1Handle.getSlot(GridHandle.ROW_SLOT).drop(firstRowHandle);
			fail();
		} catch (SemanticException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_STRUCTURE_CHANGE_FORBIDDEN, e.getErrorCode());
		}

		// 2.4 DropAndClear(DesignElementHandle) operation forbidden.
		try {
			grid1Handle.getSlot(GridHandle.ROW_SLOT).dropAndClear(firstRowHandle);
			fail();
		} catch (SemanticException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_STRUCTURE_CHANGE_FORBIDDEN, e.getErrorCode());
		}

		// 2.5 DropAndClear(int) operation forbidden.
		try {
			grid1Handle.getSlot(GridHandle.ROW_SLOT).dropAndClear(0);
			fail();
		} catch (SemanticException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_STRUCTURE_CHANGE_FORBIDDEN, e.getErrorCode());
		}

		// 2.6 shift(DesignElementHandle,int) operation forbidden.
		try {
			grid1Handle.getSlot(GridHandle.ROW_SLOT).shift(firstRowHandle, 1);
			fail();
		} catch (SemanticException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_STRUCTURE_CHANGE_FORBIDDEN, e.getErrorCode());
		}

		// 2.7 paste(DesignElementHandle) operation forbidden.
		try {
			grid1Handle.getSlot(GridHandle.ROW_SLOT).paste(newRow);
			fail();
		} catch (SemanticException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_STRUCTURE_CHANGE_FORBIDDEN, e.getErrorCode());
		}

		// 2.8 paste(DesignElementHandle, int) operation forbidden.
		try {
			grid1Handle.getSlot(GridHandle.ROW_SLOT).paste(newRow, 1);
			fail();
		} catch (SemanticException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_STRUCTURE_CHANGE_FORBIDDEN, e.getErrorCode());
		}

		// 3. Virtual element also is not allowed to change structure.

		assertTrue(firstRowHandle.getElement().isVirtualElement());

		CellHandle newCell = factory.newCell();
		assertFalse(firstRowHandle.canContain(RowHandle.CONTENT_SLOT, newCell));
		assertFalse(firstRowHandle.canContain(RowHandle.CONTENT_SLOT, ReportDesignConstants.CELL_ELEMENT));
		assertFalse(firstRowHandle.canDrop());

		assertFalse(firstRowHandle.getCells().canContain(ReportDesignConstants.CELL_ELEMENT));

		// 3.1 Drop() operation forbidden.
		try {
			firstRowHandle.drop();
			fail();
		} catch (SemanticException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_STRUCTURE_CHANGE_FORBIDDEN, e.getErrorCode());
		}

		// 3.2 Add(DesignElementHandle) operation forbidden.
		try {
			firstRowHandle.getCells().add(newCell);
			fail();
		} catch (SemanticException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_STRUCTURE_CHANGE_FORBIDDEN, e.getErrorCode());
		}

		// 3.3 Add(DesignElementHandle, int) operation forbidden.
		try {
			firstRowHandle.getCells().add(newCell, 1);
			fail();
		} catch (SemanticException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_STRUCTURE_CHANGE_FORBIDDEN, e.getErrorCode());
		}
	}

	/**
	 * Test methods are:
	 *
	 * <ul>
	 * <li>get the definition of the slot and test its methods.
	 *
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testOtherMethods() throws Exception {
		createDesign();

		ISlotDefn bodyDefn = designHandle.getBody().getDefn();
		assertEquals("body", bodyDefn.getName()); //$NON-NLS-1$
		assertTrue(bodyDefn.isMultipleCardinality());
	}

}
