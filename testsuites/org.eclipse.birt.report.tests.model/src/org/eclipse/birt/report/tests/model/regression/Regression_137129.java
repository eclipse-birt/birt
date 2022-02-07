/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * Steps to reproduce:
 * <ol>
 * <li>Add a sample data source and a data set with table "CUSTOMERS"
 * <li>Add a grid, and drag a data set column "CUSTOMERNUMBER" from palette to
 * layout. We see a data binding "CUSTOMERNUMBER = DataSetRow["CUSTOMERNUMBER"]"
 * has been added to the data
 * <li>Choose the data, click "Binding" tab, choose data set from the drop-down
 * list
 * <li>Click "Generate All"
 * </ol>
 * <b>Expected result:</b>
 * <p>
 * All data binding name for the data item should be unique
 * <p>
 * <b>Actual result:</b>
 * <p>
 * Two data bindings have the same name "CUSTOMERNUMER"
 * <p>
 * <b>Test description:</b>
 * <p>
 * Name exception when adding two databinding with the same name to a data item
 * </p>
 */

public class Regression_137129 extends BaseTestCase {

	/**
	 * @throws SemanticException
	 */

	public void test_regression_137129() throws SemanticException {
		SessionHandle session = new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.ENGLISH);
		designHandle = session.createDesign();
		ElementFactory factory = designHandle.getElementFactory();

		DataItemHandle data = factory.newDataItem("data"); //$NON-NLS-1$
		designHandle.getBody().add(data);

		ComputedColumn column1 = StructureFactory.newComputedColumn(data, "a"); //$NON-NLS-1$
		column1.setExpression("expression1"); //$NON-NLS-1$
		data.addColumnBinding(column1, true);

		// Duplicated name will be changed to a unique name
		ComputedColumn column2 = StructureFactory.newComputedColumn(data, "a"); //$NON-NLS-1$
		column2.setExpression("expression2"); //$NON-NLS-1$
		assertEquals("a_1", column2.getName()); //$NON-NLS-1$

		try {
			column2.setName("a"); //$NON-NLS-1$
			data.addColumnBinding(column2, true);
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS, e.getErrorCode());
		}

	}
}
