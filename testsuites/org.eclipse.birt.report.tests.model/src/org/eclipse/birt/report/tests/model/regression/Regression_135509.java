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
import org.eclipse.birt.report.model.api.ReportDesignHandle;
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
 * Can't have the same named expressions in data binding column:
 * <ol>
 * <li>Insert a data and cancel the pop-up window to ask for column binding
 * <li>Select the data and switch to Property Editor
 * <li>Add a data binding column "a = dataSetRow["xxx1"]"
 * <li>Add another data binding column "a = dataSetRow["xxx2"]"
 * <li>Double click the data, choose one data binding
 * </ol>
 * </p>
 * Test description:
 * <p>
 * Make sure that Model will do the duplicate name check of column bindings,
 * exception will be throwed when adding a binding with an existing name
 * </p>
 */
public class Regression_135509 extends BaseTestCase {

	/**
	 * @throws SemanticException
	 */

	public void test_regression_135509() throws SemanticException {
		DesignEngine engine = new DesignEngine(new DesignConfig());
		SessionHandle session = engine.newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle designHandle = session.createDesign();

		ElementFactory factory = designHandle.getElementFactory();
		DataItemHandle data = factory.newDataItem("data"); //$NON-NLS-1$

		ComputedColumn col1 = StructureFactory.createComputedColumn();
		col1.setName("a"); //$NON-NLS-1$
		col1.setExpression("dataSetRow[\"xxx1\"]"); //$NON-NLS-1$

		data.addColumnBinding(col1, true);

		ComputedColumn col2 = StructureFactory.createComputedColumn();
		col2.setName("a"); //$NON-NLS-1$
		col2.setExpression("dataSetRow[\"xxx2\"]"); //$NON-NLS-1$
		try {
			data.addColumnBinding(col1, true);
			fail();
		} catch (SemanticException e) {
			assertTrue(e instanceof PropertyValueException);
		}

	}
}
