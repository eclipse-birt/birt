/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.parser;

import java.util.List;

import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.MultiViewsHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.TableHandle;

/**
 * Tests case for multiple view.
 */

public class MultiViewParseTest extends ParserTestCase {

	private static final String INPUT_FILE = "MultiViewParseTest.xml"; //$NON-NLS-1$
	private static final String GOLDEN_FILE = "MultiViewParseTest_golden.xml"; //$NON-NLS-1$

	/**
	 * Tests cases about parser and API related.
	 * 
	 * @throws Exception
	 */

	public void testParser() throws Exception {
		openDesign(INPUT_FILE);

		TableHandle table1 = (TableHandle) designHandle.findElement("MyTable1"); //$NON-NLS-1$
		MultiViewsHandle view1 = (MultiViewsHandle) table1.getProperty(TableHandle.MULTI_VIEWS_PROP);
		assertNotNull(view1);

		List views = view1.getListProperty(MultiViewsHandle.VIEWS_PROP);
		assertEquals(2, views.size());

		ExtendedItemHandle box1 = (ExtendedItemHandle) views.get(0);
		assertEquals("firstDataSet", box1.getDataSet().getName()); //$NON-NLS-1$

		// the data related properties are read only.

		PropertyHandle prop = box1.getPropertyHandle(ReportItemHandle.DATA_SET_PROP);
		assertTrue(prop.isReadOnly());

		prop = box1.getPropertyHandle(ExtendedItemHandle.FILTER_PROP);
		assertTrue(prop.isReadOnly());
	}

	/**
	 * Tests cases about writer.
	 * 
	 * @throws Exception
	 */

	public void testWriter() throws Exception {
		openDesign(INPUT_FILE);

		TableHandle table2 = designHandle.getElementFactory().newTableItem("table2", 3); //$NON-NLS-1$
		designHandle.getBody().add(table2);
		table2.setDataSet(designHandle.findDataSet("firstDataSet")); //$NON-NLS-1$

		MultiViewsHandle view2 = designHandle.getElementFactory().newMultiView();
		table2.getPropertyHandle(TableHandle.MULTI_VIEWS_PROP).add(view2);

		ExtendedItemHandle box3 = designHandle.getElementFactory().newExtendedItem("box3", "TestingBox"); //$NON-NLS-1$//$NON-NLS-2$

		view2.add(MultiViewsHandle.VIEWS_PROP, box3);
		view2.setCurrentViewIndex(0);

		assertEquals("firstDataSet", box3.getDataSet().getName()); //$NON-NLS-1$

		save();
		assertTrue(compareFile(GOLDEN_FILE));
	}
}
