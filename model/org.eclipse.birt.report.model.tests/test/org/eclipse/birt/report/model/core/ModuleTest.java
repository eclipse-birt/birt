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

package org.eclipse.birt.report.model.core;

import java.util.List;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.FreeFormHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * The Test Case of Class RootElement.
 * 
 * The RootElement is the root of the report design. It has some slots which
 * contain the Page, Section and Style etc. So we test the container-content
 * relationship. At the same time, the RootElement has more than one NameSpace
 * to store the associated names. We test the namespaces too.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testElementID}</td>
 * <td>add element and check it</td>
 * <td>object is the same as orginal element</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>drop element and check it</td>
 * <td>orginal element is dropped</td>
 * </tr>
 * 
 * </table>
 * 
 */
public class ModuleTest extends BaseTestCase {

	private static final String fileName = "ModuleTest.xml"; //$NON-NLS-1$
	private static final String idFileName = "ModuleTest_2.xml"; //$NON-NLS-1$

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		sessionHandle = engine.newSessionHandle(ULocale.ENGLISH);

		MetaDataDictionary dd = MetaDataDictionary.getInstance();
		dd.enableElementID();

		designHandle = sessionHandle.createDesign("myDesign"); //$NON-NLS-1$
		design = (ReportDesign) designHandle.getModule();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		design = null;
		designHandle = null;
	}

	/**
	 * Test addElementID( DesignElement ), dropElementID( DesignElement ) and
	 * DesignElement getElementByID( int ).
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>add element and check it</li>
	 * <li>drop element and check it</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>object is the same as orginal element</li>
	 * <li>orginal element is dropped</li>
	 * </ul>
	 */
	public void testElementID() {
		// add a design elementID into HashMap and find it.
		StyleElement element = new Style();
		element.setName("element"); //$NON-NLS-1$
		element.setID(design.getNextID());
		design.addElementID(element);
		Object o = design.getElementByID(element.getID());
		assertEquals(element, o);

		// drop ID from the HashMap
		design.dropElementID(element);
		o = design.getElementByID(element.getID());
		assertNull(o);
	}

	/**
	 * Tests the id of all the elements after parsing are right. Test the id map in
	 * the design is right.
	 * 
	 * @throws Exception
	 */

	public void testIDWithParser() throws Exception {
		openDesign(fileName);

		// test id of all the elements are right value.

		assertEquals(1, designHandle.getID());
		MasterPageHandle page = designHandle.findMasterPage("page"); //$NON-NLS-1$
		assertEquals(2, page.getID());
		FreeFormHandle parent = (FreeFormHandle) designHandle.findElement("parent"); //$NON-NLS-1$
		assertEquals(3, parent.getID());
		LabelHandle label = (LabelHandle) designHandle.findElement("label"); //$NON-NLS-1$
		assertEquals(4, label.getID());
		FreeFormHandle child = (FreeFormHandle) designHandle.findElement("child"); //$NON-NLS-1$
		assertEquals(5, child.getID());
		LabelHandle labelTwo = (LabelHandle) designHandle.findElement("labelTwo"); //$NON-NLS-1$
		assertEquals(6, labelTwo.getID());

		// test the id map on the design is right

		assertEquals(designHandle, designHandle.getElementByID(1));
		assertEquals(page, designHandle.getElementByID(2));
		assertEquals(parent, designHandle.getElementByID(3));
		assertEquals(label, designHandle.getElementByID(4));
		assertEquals(child, designHandle.getElementByID(5));
		assertEquals(labelTwo, designHandle.getElementByID(6));

		designHandle.close();

		// open a design file, some elements have id, some element don't

		openDesign(idFileName);
		assertNotNull(designHandle);
		save();
		assertTrue(compareFile("ModuleTest_golden_2.xml")); //$NON-NLS-1$

	}

	/**
	 * Tests the writer about the id.
	 * 
	 * @throws Exception
	 */

	public void testIDWithWriter() throws Exception {
		openDesign(fileName);

		// save it

		String goldenFileName = "ModuleTest_golden.xml"; //$NON-NLS-1$
		save();
		assertTrue(compareFile(goldenFileName));
	}

	/**
	 * Adds an element into the module and test the id issues.
	 * 
	 * @throws Exception
	 */

	public void testIDWithCommand() throws Exception {
		openDesign(fileName);

		TableHandle table = designHandle.getElementFactory().newTableItem("table", 0, 0, 1, 0); //$NON-NLS-1$
		assertNotNull(table);
		LabelHandle label = designHandle.getElementFactory().newLabel("label"); //$NON-NLS-1$
		assertNotNull(label);
		CellHandle cell = designHandle.getElementFactory().newCell();
		assertNotNull(cell);

		RowHandle row = (RowHandle) table.getDetail().get(0);
		cell.addElement(label, CellHandle.CONTENT_SLOT);
		assertEquals(0, label.getID());
		row.addElement(cell, RowHandle.CONTENT_SLOT);
		assertEquals(0, cell.getID());

		assertEquals(0, table.getID());
		designHandle.addElement(table, ReportDesign.BODY_SLOT);

		// when the table is added, table and all its contents will have id.

		assertEquals(7, table.getID());
		assertEquals(8, row.getID());
		assertEquals(9, cell.getID());
		assertEquals(10, label.getID());

		assertEquals(table, designHandle.getElementByID(7));
		assertEquals(row, designHandle.getElementByID(8));
		assertEquals(cell, designHandle.getElementByID(9));
		assertEquals(label, designHandle.getElementByID(10));

		// undo-remove the table from the design, the table and its contents
		// will be removed from the id-map too

		designHandle.getCommandStack().undo();
		assertEquals(7, table.getID());
		assertEquals(8, row.getID());
		assertEquals(9, cell.getID());
		assertEquals(10, label.getID());

		assertNull(designHandle.getElementByID(7));
		assertNull(designHandle.getElementByID(8));
		assertNull(designHandle.getElementByID(9));
		assertNull(designHandle.getElementByID(10));

		// open another file, the id is not consecutive

		designHandle.close();
		openDesign("ModuleTest_1.xml"); //$NON-NLS-1$

		table = designHandle.getElementFactory().newTableItem("table", 0, 0, 1, 0); //$NON-NLS-1$
		assertNotNull(table);
		label = designHandle.getElementFactory().newLabel("label"); //$NON-NLS-1$
		assertNotNull(label);
		cell = designHandle.getElementFactory().newCell();
		assertNotNull(cell);

		row = (RowHandle) table.getDetail().get(0);
		cell.addElement(label, CellHandle.CONTENT_SLOT);
		assertEquals(0, label.getID());
		row.addElement(cell, RowHandle.CONTENT_SLOT);
		assertEquals(0, cell.getID());

		assertEquals(0, table.getID());
		designHandle.addElement(table, ReportDesign.BODY_SLOT);

		assertEquals(12, table.getID());
		assertEquals(13, row.getID());
		assertEquals(14, cell.getID());
		assertEquals(15, label.getID());
	}

	/**
	 * Tests drag data source and data set. After draging ,should keep position
	 * right.
	 * 
	 * @throws Exception
	 */

	public void testDragDataSourceAndDataSet() throws Exception {
		openDesign("ModuleTest_3.xml"); //$NON-NLS-1$

		SlotHandle slotHandle = designHandle.getDataSources();
		DataSourceHandle dsHandle2 = designHandle.findDataSource("Data Source2");//$NON-NLS-1$

		IDesignElement ds = dsHandle2.copy();
		designHandle.rename(ds.getHandle(design));
		slotHandle.paste(ds, 1);

		DataSourceHandle dsHandle = (DataSourceHandle) slotHandle.get(0);
		assertEquals("Data Source2", dsHandle.getName());//$NON-NLS-1$
		dsHandle = (DataSourceHandle) slotHandle.get(1);
		assertEquals("Data Source21", dsHandle.getName());//$NON-NLS-1$
		dsHandle = (DataSourceHandle) slotHandle.get(2);
		assertEquals("Data Source3", dsHandle.getName());//$NON-NLS-1$
		dsHandle = (DataSourceHandle) slotHandle.get(3);
		assertEquals("Data Source4", dsHandle.getName());//$NON-NLS-1$

		slotHandle = designHandle.getDataSets();
		DataSetHandle setHandle1 = designHandle.findDataSet("Data Set");//$NON-NLS-1$
		IDesignElement set = setHandle1.copy();
		designHandle.rename(set.getHandle(design));
		slotHandle.paste(set, 1);

		DataSetHandle setHandle = (DataSetHandle) slotHandle.get(0);
		assertEquals("Data Set", setHandle.getName());//$NON-NLS-1$
		setHandle = (DataSetHandle) slotHandle.get(1);
		assertEquals("Data Set3", setHandle.getName());//$NON-NLS-1$
		setHandle = (DataSetHandle) slotHandle.get(2);
		assertEquals("Data Set1", setHandle.getName());//$NON-NLS-1$
		setHandle = (DataSetHandle) slotHandle.get(3);
		assertEquals("Data Set2", setHandle.getName());//$NON-NLS-1$

	}

	/**
	 * Tests refer to external resource file.
	 * 
	 * @throws Exception
	 */

	public void testGetMessage() throws Exception {
		openDesign("ModuleTest_4.xml", ULocale.ENGLISH); //$NON-NLS-1$

		LabelHandle handle1 = (LabelHandle) designHandle.findElement("label1");//$NON-NLS-1$
		assertEquals("label1 in i18n", handle1.getRoot().getMessage( //$NON-NLS-1$
				handle1.getTextKey()));

		LabelHandle handle2 = (LabelHandle) designHandle.findElement("label2");//$NON-NLS-1$
		assertEquals("label2 in i18n", handle2.getRoot().getMessage( //$NON-NLS-1$
				handle2.getTextKey()));

	}

	/**
	 * Tests the message keys.
	 * 
	 * @throws Exception
	 */

	public void testGetMessageKeys() throws Exception {
		openDesign("ModuleTest_4.xml", ULocale.ENGLISH); //$NON-NLS-1$

		List keyList = designHandle.getMessageKeys();
		assertEquals("a1", keyList.get(0)); //$NON-NLS-1$
		assertEquals("a2", keyList.get(1)); //$NON-NLS-1$

	}

}
