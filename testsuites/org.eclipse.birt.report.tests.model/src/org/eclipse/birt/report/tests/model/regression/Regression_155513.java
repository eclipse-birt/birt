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

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Regression description:</b>
 * </p>
 * The rule is that: if a content element defines data set or column bindings,
 * to refer bindings on container, it must use syntax like row._outer.
 * <p>
 * So, ONLY check the data binding to which the new column is being added. That
 * is, binding names can be same for content and container elements.
 * <p>
 * For example, if the column binding is added to the data, only check column
 * bindings on data. Do not need to concern column bindings on its container
 * like table item.
 * <p>
 * <b>Test description:</b>
 * <p>
 * Column binding with the same name can't be added to the container itself but
 * can be added to its inner element
 */
public class Regression_155513 extends BaseTestCase {

	private String filename = "Regression_155513.xml"; //$NON-NLS-1$

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(filename, filename);
	}

	public void tearDown() {
		removeResource();
	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	public void test_regression_155513() throws DesignFileException, SemanticException {
		openDesign(filename);
		TableHandle table = (TableHandle) designHandle.findElement("table"); //$NON-NLS-1$
		ComputedColumn column = StructureFactory.createComputedColumn();
		column.setName("a"); //$NON-NLS-1$
		column.setExpression("b"); //$NON-NLS-1$

		// Table can't have two column bindings with the same name
		try {

			table.addColumnBinding(column, false);
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS, e.getErrorCode());
		}

		// Column binding on data can be the same as table
		DataItemHandle data = (DataItemHandle) designHandle.findElement("data"); //$NON-NLS-1$
		data.addColumnBinding(column, false);

	}
}
