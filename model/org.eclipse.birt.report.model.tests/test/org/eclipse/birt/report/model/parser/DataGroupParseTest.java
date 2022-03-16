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

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.DataGroupHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.SortKeyHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.util.BaseTestCase;

public class DataGroupParseTest extends BaseTestCase {

	/**
	 * The extension element that uses variable element.
	 */

	private static final String FILE_NAME = "DataGroupParseTest.xml"; //$NON-NLS-1$

	/**
	 * Tests to get values for data group element.
	 *
	 * @throws Exception
	 */

	public void testParser() throws Exception {
		openDesign(FILE_NAME);

		ExtendedItemHandle action1 = (ExtendedItemHandle) designHandle.findElement("action1"); //$NON-NLS-1$

		List groups = action1.getListProperty("dataGroups"); //$NON-NLS-1$
		DataGroupHandle group = (DataGroupHandle) groups.get(0);

		// test getters
		assertEquals("group test", group.getGroupName()); //$NON-NLS-1$
		assertEquals("[Country]", group.getKeyExpr()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.INTERVAL_WEEK, group.getInterval());
		assertTrue(3.0 == group.getIntervalRange());
		assertEquals("2008-1-1", group.getIntervalBase()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.SORT_DIRECTION_DESC, group.getSortDirection());
		assertEquals(DesignChoiceConstants.SORT_TYPE_SORT_ON_GROUP_KEY, group.getSortType());

		// test sort
		Iterator sorts = group.sortsIterator();

		SortKeyHandle sortHandle = (SortKeyHandle) sorts.next();

		assertEquals("name", sortHandle.getKey()); //$NON-NLS-1$
		assertEquals("asc", sortHandle.getDirection()); //$NON-NLS-1$

		sortHandle = (SortKeyHandle) sorts.next();

		assertEquals("birthday", sortHandle.getKey()); //$NON-NLS-1$
		assertEquals("desc", sortHandle.getDirection()); //$NON-NLS-1$

		// test filter
		Iterator filters = group.filtersIterator();

		FilterConditionHandle filterHandle = (FilterConditionHandle) filters.next();

		assertEquals("lt", filterHandle.getOperator()); //$NON-NLS-1$
		assertEquals("filter expression", filterHandle.getExpr()); //$NON-NLS-1$
		assertEquals("value1 expression", filterHandle.getValue1()); //$NON-NLS-1$
		assertEquals("value2 expression", filterHandle.getValue2()); //$NON-NLS-1$
	}

	/**
	 * Tests setters and writer for data group element.
	 *
	 * @throws Exception
	 */
	public void testWriter() throws Exception {
		openDesign(FILE_NAME);

		ExtendedItemHandle action1 = (ExtendedItemHandle) designHandle.findElement("action1"); //$NON-NLS-1$

		List groups = action1.getListProperty("dataGroups"); //$NON-NLS-1$
		DataGroupHandle group = (DataGroupHandle) groups.get(0);

		String updatedPrefix = "updated "; //$NON-NLS-1$
		group.setGroupName(updatedPrefix + group.getGroupName());
		group.setKeyExpr(updatedPrefix + group.getKeyExpr());
		group.setInterval(DesignChoiceConstants.INTERVAL_DAY);
		group.setIntervalRange(1);
		group.setIntervalBase(updatedPrefix + group.getIntervalBase());
		group.setSortDirection(DesignChoiceConstants.SORT_DIRECTION_ASC);
		group.setSortType(DesignChoiceConstants.SORT_TYPE_COMPLEX_SORT);

		save();

		assertTrue(compareFile("DataGroupParseTest_golden.xml")); //$NON-NLS-1$
	}
}
