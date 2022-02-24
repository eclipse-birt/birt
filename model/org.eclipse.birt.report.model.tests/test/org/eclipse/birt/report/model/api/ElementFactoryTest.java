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

import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.interfaces.IThemeModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * The test case for element factory method.
 * 
 * Test DimensionHandle.
 * 
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testNewElement()}</td>
 * <td>Creates the new elements.</td>
 * <td>elements are created successfully.</td>
 * </tr>
 * 
 * </table>
 */

public class ElementFactoryTest extends BaseTestCase {

	protected void setUp() throws Exception {
		super.setUp();
		designHandle = new SessionHandle(ULocale.getDefault()).createDesign();
		design = (ReportDesign) designHandle.getModule();
	}

	/**
	 * Test the generic create element factory method.Create every type of element
	 * which was defined in reportDesignConstents.
	 * 
	 */

	public void testNewElement() {

		ElementFactory factory = new ElementFactory(design);

		DesignElementHandle element = factory.newElement(ReportDesignConstants.DATA_ITEM, null);
		assertTrue(element instanceof DataItemHandle);

		element = factory.newElement(ReportDesignConstants.CELL_ELEMENT, null);
		assertTrue(element instanceof CellHandle);

		element = factory.newElement(ReportDesignConstants.COLUMN_ELEMENT, null);
		assertTrue(element instanceof ColumnHandle);

		// abstract class data
		element = factory.newElement(ReportDesignConstants.DATA_SET_ELEMENT, null);
		assertNull(element);

		element = factory.newElement(ReportDesignConstants.DATA_SOURCE_ELEMENT, null);
		assertNull(element);

		element = factory.newElement(ReportDesignConstants.EXTENDED_ITEM, null);
		assertTrue(element instanceof ExtendedItemHandle);

		element = factory.newElement(ReportDesignConstants.FREE_FORM_ITEM, null);
		assertTrue(element instanceof FreeFormHandle);

		element = factory.newElement(ReportDesignConstants.GRAPHIC_MASTER_PAGE_ELEMENT, null);
		assertTrue(element instanceof GraphicMasterPageHandle);

		element = factory.newElement(ReportDesignConstants.GRID_ITEM, null);
		assertTrue(element instanceof GridHandle);

		element = factory.newElement(ReportDesignConstants.IMAGE_ITEM, null);
		assertTrue(element instanceof ImageHandle);

		element = factory.newElement(ReportDesignConstants.LABEL_ITEM, null);
		assertTrue(element instanceof LabelHandle);

		element = factory.newElement(ReportDesignConstants.LINE_ITEM, null);
		assertTrue(element instanceof LineHandle);

		element = factory.newElement(ReportDesignConstants.LIST_GROUP_ELEMENT, null);
		assertTrue(element instanceof ListGroupHandle);

		element = factory.newElement(ReportDesignConstants.LIST_ITEM, null);
		assertTrue(element instanceof ListHandle);

		element = factory.newElement(ReportDesignConstants.MASTER_PAGE_ELEMENT, null);
		assertNull(element);

		element = factory.newElement(ReportDesignConstants.TEXT_DATA_ITEM, null);
		assertTrue(element instanceof TextDataHandle);

		element = factory.newElement(ReportDesignConstants.PARAMETER_GROUP_ELEMENT, null);
		assertTrue(element instanceof ParameterGroupHandle);

		element = factory.newElement(ReportDesignConstants.RECTANGLE_ITEM, null);
		assertTrue(element instanceof RectangleHandle);

		element = factory.newElement(ReportDesignConstants.REPORT_DESIGN_ELEMENT, null);
		assertTrue(element instanceof ReportDesignHandle);

		element = factory.newElement(ReportDesignConstants.REPORT_ITEM, null);
		assertNull(element);

		element = factory.newElement(ReportDesignConstants.ROW_ELEMENT, null);
		assertTrue(element instanceof RowHandle);

		element = factory.newElement(ReportDesignConstants.SCALAR_PARAMETER_ELEMENT, null);
		assertTrue(element instanceof ScalarParameterHandle);

		element = factory.newElement(ReportDesignConstants.SCRIPT_DATA_SOURCE, null);
		assertTrue(element instanceof ScriptDataSourceHandle);

		element = factory.newElement(ReportDesignConstants.SIMPLE_MASTER_PAGE_ELEMENT, null);
		assertTrue(element instanceof SimpleMasterPageHandle);

		element = factory.newElement(ReportDesignConstants.STYLE_ELEMENT, null);
		assertTrue(element instanceof StyleHandle);

		element = factory.newElement(ReportDesignConstants.TABLE_GROUP_ELEMENT, null);
		assertTrue(element instanceof TableGroupHandle);

		element = factory.newElement(ReportDesignConstants.TABLE_ITEM, null);
		assertTrue(element instanceof TableHandle);

		element = factory.newElement(ReportDesignConstants.TEXT_ITEM, null);
		assertTrue(element instanceof TextItemHandle);

		// test given name
		element = factory.newElement(ReportDesignConstants.TEXT_ITEM, "text item"); //$NON-NLS-1$
		assertEquals("text item", element.getName()); //$NON-NLS-1$

	}

	/**
	 * Test new element from
	 * 
	 * @throws Exception
	 * 
	 * @throws Exception
	 */

	public void testNewElementFrom() throws Exception {
		openDesign("ElementFactoryTest_1.xml"); //$NON-NLS-1$

		LibraryHandle libHandle = designHandle.getLibrary("Lib3"); //$NON-NLS-1$
		LabelHandle baseLabelHandle = (LabelHandle) libHandle.getComponents().get(0);
		TableHandle baseTableHandle = (TableHandle) libHandle.getComponents().get(1);

		ElementFactory factory = designHandle.getElementFactory();

		LabelHandle label1 = (LabelHandle) factory.newElementFrom(baseLabelHandle, "label1"); //$NON-NLS-1$
		TableHandle table1 = (TableHandle) factory.newElementFrom(baseTableHandle, "table1"); //$NON-NLS-1$

		designHandle.getBody().add(label1);
		designHandle.getBody().add(table1);
		design.getActivityStack().undo();
		design.getActivityStack().undo();

		assertNull(designHandle.findElement("label1")); //$NON-NLS-1$
		assertNull(designHandle.findElement("table1")); //$NON-NLS-1$

		ParameterGroupHandle baseParameterGroup = (ParameterGroupHandle) libHandle.getParameters().get(0);
		assertNotNull(baseParameterGroup);
		ParameterGroupHandle childParameterGroup = (ParameterGroupHandle) factory.newElementFrom(baseParameterGroup,
				"newPara"); //$NON-NLS-1$

		designHandle.getParameters().add(childParameterGroup);
		childParameterGroup = (ParameterGroupHandle) factory.newElementFrom(baseParameterGroup, "newParaTwo"); //$NON-NLS-1$
		designHandle.getParameters().add(childParameterGroup);

		save();
		compareFile("ElementFactoryTest_golden.xml"); //$NON-NLS-1$

		// clear the parameter slot and test again

		int count = designHandle.getParameters().getCount();
		for (int i = count - 1; i >= 0; i--) {
			DesignElementHandle param = designHandle.getParameters().get(i);
			param.drop();
		}
		childParameterGroup = (ParameterGroupHandle) factory.newElementFrom(baseParameterGroup, "newPara"); //$NON-NLS-1$

		designHandle.getParameters().add(childParameterGroup);
		save();
	}

	/**
	 * Test the specified create element factory method.Create every type of element
	 * which was defined in reportDesignConstents.
	 * 
	 */

	public void testNewSpecifiedElement() {
		ElementFactory factory = new ElementFactory(design);

		DesignElementHandle handle = factory.newCell();
		assertNotNull(handle);

		handle = factory.newDataItem("new data"); //$NON-NLS-1$
		assertNotNull(handle);

		// TODO: test the new function of ExtendedItem

		// handle = factory.newExtendedItem( "new extended item" );
		// //$NON-NLS-1$
		// assertNotNull( handle );

		handle = factory.newFreeForm("new freeform"); //$NON-NLS-1$
		assertNotNull(handle);

		handle = factory.newGraphicMasterPage("new graphic master page"); //$NON-NLS-1$
		assertNotNull(handle);

		handle = factory.newGridItem("new grid item"); //$NON-NLS-1$
		assertNotNull(handle);

		handle = factory.newImage("new image item"); //$NON-NLS-1$
		assertNotNull(handle);

		handle = factory.newLabel("new label"); //$NON-NLS-1$
		assertNotNull(handle);

		handle = factory.newLineItem("new line"); //$NON-NLS-1$
		assertNotNull(handle);

		handle = factory.newList("new list"); //$NON-NLS-1$
		assertNotNull(handle);

		handle = factory.newListGroup();
		assertNotNull(handle);

		handle = factory.newParameterGroup("new parameter group"); //$NON-NLS-1$
		assertNotNull(handle);

		handle = factory.newRectangle("new rectangle"); //$NON-NLS-1$
		assertNotNull(handle);

		handle = factory.newScalarParameter(null);
		assertEquals("NewParameter", handle.getName()); //$NON-NLS-1$

		handle = factory.newScalarParameter("new scalar parameter"); //$NON-NLS-1$
		assertNotNull(handle);

		handle = factory.newDynamicFilterParameter(null);
		assertEquals("NewDynamicFilterParameter", handle.getName()); //$NON-NLS-1$

		handle = factory.newDynamicFilterParameter("new dynamic filter parameter"); //$NON-NLS-1$
		assertNotNull(handle);

		handle = factory.newScriptDataSet("new script data set"); //$NON-NLS-1$
		assertNotNull(handle);

		handle = factory.newScriptDataSource("new script data source"); //$NON-NLS-1$
		assertNotNull(handle);

		handle = factory.newSimpleMasterPage("new simple master page"); //$NON-NLS-1$
		assertNotNull(handle);

		handle = factory.newStyle("new style"); //$NON-NLS-1$
		assertNotNull(handle);

		handle = factory.newTableColumn();
		assertNotNull(handle);

		handle = factory.newTableGroup();
		assertNotNull(handle);

		handle = factory.newTableItem("new table"); //$NON-NLS-1$
		assertNotNull(handle);

		handle = factory.newTableRow();
		assertNotNull(handle);

		handle = factory.newTextItem("new text"); //$NON-NLS-1$
		assertNotNull(handle);

	}

	/**
	 * Tests the table item with factory method.
	 */

	public void testNewTableItem() {
		assertFalse(designHandle.needsSave());

		ElementFactory factory = new ElementFactory(design);
		TableHandle tableHandle1 = factory.newTableItem("Table1", 2); //$NON-NLS-1$
		assertFalse(designHandle.needsSave());

		assertEquals("Table1", tableHandle1.getName()); //$NON-NLS-1$
		assertEquals(1, tableHandle1.getHeader().getCount());
		assertEquals(1, tableHandle1.getFooter().getCount());
		assertEquals(1, tableHandle1.getDetail().getCount());
		assertEquals(2, tableHandle1.getColumns().getCount());

		// check whether row is filled with cells and container-content
		// relationship.

		RowHandle rowHandle = (RowHandle) tableHandle1.getHeader().get(0);
		assertEquals(2, rowHandle.getCells().getCount());
		assertEquals(tableHandle1, rowHandle.getContainer());

		rowHandle = (RowHandle) tableHandle1.getFooter().get(0);
		assertEquals(2, rowHandle.getCells().getCount());
		assertEquals(tableHandle1, rowHandle.getContainer());

		rowHandle = (RowHandle) tableHandle1.getDetail().get(0);
		assertEquals(2, rowHandle.getCells().getCount());
		assertEquals(tableHandle1, rowHandle.getContainer());

		TableHandle tableHandle2 = factory.newTableItem("Table2", 3, 4, 5, 6); //$NON-NLS-1$
		assertEquals("Table2", tableHandle2.getName()); //$NON-NLS-1$
		assertEquals(3, tableHandle2.getColumns().getCount());
		assertEquals(4, tableHandle2.getHeader().getCount());
		assertEquals(5, tableHandle2.getDetail().getCount());
		assertEquals(6, tableHandle2.getFooter().getCount());

		// check to see row is filled with cells.

		// header
		for (int i = 0; i < 4; i++) {
			rowHandle = (RowHandle) tableHandle2.getHeader().get(i);
			assertEquals(3, rowHandle.getCells().getCount());
			for (int j = 0; j < 2; j++) {
				assertEquals(rowHandle, rowHandle.getCells().get(j).getContainer());
			}
		}

		// detail
		for (int i = 0; i < 5; i++) {
			rowHandle = (RowHandle) tableHandle2.getDetail().get(i);
			assertEquals(3, rowHandle.getCells().getCount());
			for (int j = 0; j < 2; j++) {
				assertEquals(rowHandle, rowHandle.getCells().get(j).getContainer());
			}
		}

		// footer
		for (int i = 0; i < 6; i++) {
			rowHandle = (RowHandle) tableHandle2.getFooter().get(i);
			assertEquals(3, rowHandle.getCells().getCount());
			for (int j = 0; j < 2; j++) {
				assertEquals(rowHandle, rowHandle.getCells().get(j).getContainer());
			}
		}

		TableHandle tableHandle3 = factory.newTableItem("Table3", -1, -1, -1, -1); //$NON-NLS-1$
		assertEquals("Table3", tableHandle3.getName()); //$NON-NLS-1$
		assertEquals(0, tableHandle3.getHeader().getCount());
		assertEquals(0, tableHandle3.getFooter().getCount());
		assertEquals(0, tableHandle3.getDetail().getCount());
		assertEquals(0, tableHandle3.getColumns().getCount());
	}

	/**
	 * Tests the grid item with factory method.
	 */

	public void testNewGridItem() {
		assertFalse(designHandle.needsSave());

		ElementFactory factory = new ElementFactory(design);
		GridHandle gridHandle1 = factory.newGridItem("Grid1", 2, 3); //$NON-NLS-1$
		assertFalse(designHandle.needsSave());

		assertEquals(2, gridHandle1.getColumns().getCount());
		assertEquals(3, gridHandle1.getRows().getCount());

		for (int i = 0; i < 2; i++) {
			ColumnHandle colHandle = (ColumnHandle) gridHandle1.getColumns().get(i);

			assertEquals(gridHandle1, colHandle.getContainer());
		}

		for (int i = 0; i < 3; i++) {
			RowHandle rowHandle = (RowHandle) gridHandle1.getRows().get(i);

			assertEquals(2, rowHandle.getCells().getCount());
			assertEquals(gridHandle1, rowHandle.getContainer());

			for (int j = 0; j < 2; j++) {
				CellHandle cell = (CellHandle) rowHandle.getCells().get(j);
				assertEquals(rowHandle, cell.getContainer());
			}
		}

		GridHandle gridHandle2 = factory.newGridItem("Grid1", -1, -1); //$NON-NLS-1$
		assertEquals(0, gridHandle2.getColumns().getCount());
		assertEquals(0, gridHandle2.getRows().getCount());

	}

	/**
	 * Case: 1. create an element with a duplicated name. 2. check whether a new
	 * name is generated for the new element.
	 * 
	 * @throws ContentException
	 * @throws NameException
	 */

	public void testMakeUniqueName() throws ContentException, NameException {

		assertFalse(designHandle.needsSave());

		ElementFactory factory = new ElementFactory(design);
		TableHandle tableHandle1 = factory.newTableItem("Table1", 2); //$NON-NLS-1$
		designHandle.getBody().add(tableHandle1);

		TableHandle tableHandle2 = factory.newTableItem("Table1", 2); //$NON-NLS-1$

		assertEquals("Table11", tableHandle2.getName()); //$NON-NLS-1$

		TableHandle tableHandle3 = factory.newTableItem(null);
		assertNull(tableHandle3.getName());

		DimensionHandle dimension = factory.newTabularDimension("a/b\\c.d:1!2;3,f"); //$NON-NLS-1$
		assertEquals("a_b_c_d:1_2_3_f", dimension.getName()); //$NON-NLS-1$
	}

	/**
	 * New extended item from library.
	 * 
	 * @throws Exception
	 */

	public void testNewExtendedItemFrom() throws Exception {
		openDesign("ElementFactoryTest_2.xml");//$NON-NLS-1$
		LibraryHandle libraryHandle = designHandle.getLibrary("ElementFactoryLibTest");//$NON-NLS-1$
		ExtendedItemHandle extendedItem = (ExtendedItemHandle) libraryHandle.getElementByID(77);

		DesignElementHandle handle = designHandle.getElementFactory().newElementFrom(extendedItem, "NewMatrix");//$NON-NLS-1$
		assertNotNull(handle);
		assertEquals("NewMatrix", handle.getName());//$NON-NLS-1$
	}

	/**
	 * New extended item from library.
	 * 
	 * @throws Exception
	 */

	public void testNewStyleNameForTheme() throws Exception {
		openLibrary("LibraryWithTheme.xml");//$NON-NLS-1$
		ThemeHandle theme = (ThemeHandle) libraryHandle.findTheme("defaultTheme"); //$NON-NLS-1$
		assertNotNull(theme.getElement());

		SharedStyleHandle newCreatedStyle = libraryHandle.getElementFactory().newStyle(theme, null);
		assertNotNull(newCreatedStyle);
		assertEquals("NewStyle1", newCreatedStyle.getName());//$NON-NLS-1$
		theme.addElement(newCreatedStyle, IThemeModel.STYLES_SLOT);

		// delete the original style in default theme, the name of the deleted
		// style should be available for default theme.

		newCreatedStyle.dropAndClear();
		SharedStyleHandle style2 = libraryHandle.getElementFactory().newStyle(theme, null);
		assertEquals("NewStyle1", style2.getName());//$NON-NLS-1$
	}

}
