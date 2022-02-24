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

package org.eclipse.birt.report.item.crosstab.core.de;

import java.util.Iterator;

import org.eclipse.birt.report.item.crosstab.core.BaseTestCase;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FilterConditionElementHandle;

/**
 * Test <code>MeasureViewHandle</code>
 *
 */

public class MeasureViewHandleTest extends BaseTestCase {
	/**
	 * Tests filter property.
	 * 
	 * @throws Exception
	 */

	public void testFilters() throws Exception {
		openDesign("MeasureViewHandleTest.xml"); //$NON-NLS-1$
		ExtendedItemHandle extendedItem = (ExtendedItemHandle) designHandle.getElementByID(72l);
		MeasureViewHandle measure = (MeasureViewHandle) extendedItem.getReportItem();
		Iterator iterator = measure.filtersIterator();
		FilterConditionElementHandle filter = (FilterConditionElementHandle) iterator.next();

		assertEquals("data[\"EMPLOYEENUMBER\"]", filter.getExpr().trim());//$NON-NLS-1$

		FilterConditionElementHandle newFilter = designHandle.getElementFactory().newFilterConditionElement();
		newFilter.setExpr("expr"); //$NON-NLS-1$
		newFilter.setOperator("gt");//$NON-NLS-1$
		newFilter.setValue1("100");//$NON-NLS-1$

		extendedItem.getPropertyHandle(MeasureViewHandle.FILTER_PROP).add(newFilter);
		iterator = measure.filtersIterator();

		FilterConditionElementHandle temp = (FilterConditionElementHandle) iterator.next();
		assertEquals("data[\"EMPLOYEENUMBER\"]", temp.getExpr().trim());//$NON-NLS-1$

		temp = (FilterConditionElementHandle) iterator.next();
		assertEquals("expr", temp.getExpr().trim());//$NON-NLS-1$

		save(designHandle.getModuleHandle());
		compareFile("MeasureViewHandleTest_golden.xml");//$NON-NLS-1$
	}
}
