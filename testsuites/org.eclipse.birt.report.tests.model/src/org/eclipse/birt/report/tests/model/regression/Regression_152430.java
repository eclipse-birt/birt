/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Key can modify to empty string.
 * <p>
 * Steps to produce:
 * <ol>
 * <li>Drag a table to layout;
 * <li>On property editor page, select "Sorting" tab;
 * <li>Click "Add" button, then an item is added;
 * <li>Modify key column to empty string, and no exception is thrown.
 * </ol>
 * It menas that the key column of this item CAN be set to empty. But in step 3,
 * when adding a sortkey, if I modify the source code to set key property to be
 * empty, then this item can NOT be added, and it throws an exception to tell me
 * that the "key" can not be empty. they're inconsistent.
 * <p>
 * Can you forbid to set empty string to key property or throw an exception when
 * setting empty string. The same problem occurs in the "Expression" column on
 * page "Filter".
 * </ol>
 * <p>
 * Test description:
 * <p>
 * Ensure that Model will throw out semantic exception when empty string sets to
 * structure member which is value required.
 * </p>
 */
public class Regression_152430 extends BaseTestCase {

	/**
	 * @throws NameException
	 * @throws ContentException
	 */

	public void test_regression_152430() throws ContentException, NameException {
		ReportDesignHandle reportHandle = this.createDesign();
		ElementFactory factory = reportHandle.getElementFactory();

		TableHandle table = factory.newTableItem("newTable"); //$NON-NLS-1$
		reportHandle.getBody().add(table);

		// add sorting and filter to table.

		SortKey sortKey = StructureFactory.createSortKey();
		sortKey.setKey("  "); //$NON-NLS-1$
		sortKey.setDirection(DesignChoiceConstants.SORT_DIRECTION_ASC);

		try {
			table.getPropertyHandle(TableHandle.SORT_PROP).addItem(sortKey);
			fail();
		} catch (SemanticException e) {
			assertTrue(e instanceof PropertyValueException);
		}

		FilterCondition filter = StructureFactory.createFilterCond();

		filter.setExpr("  "); //$NON-NLS-1$
		filter.setOperator(DesignChoiceConstants.FILTER_OPERATOR_BETWEEN);
		filter.setValue1("a"); //$NON-NLS-1$
		filter.setValue2("b"); //$NON-NLS-1$

		try {
			table.getPropertyHandle(TableHandle.FILTER_PROP).addItem(filter);
			fail();
		} catch (SemanticException e) {
			assertTrue(e instanceof PropertyValueException);
		}

	}
}
