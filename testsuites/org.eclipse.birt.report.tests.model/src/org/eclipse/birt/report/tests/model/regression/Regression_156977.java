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

import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Bug description:</b>
 * </p>
 * Using the nightly build of 2.1.1 a NullPointerException occurs while
 * generating reports. With 2.1.0 everything works fine. ds.getPropertyHandle(
 * ScriptDataSetHandle.RESULT_SET_PROP ) returns null.
 * <p>
 * ScriptDataSetHandle ds = design.getElementFactory().newScriptDataSet(
 * dataSetName ); ...
 *
 * <pre>
 * PropertyHandle ph = ds.getPropertyHandle(ScriptDataSetHandle.RESULT_SET_PROP);
 * for (int i = 0; i &lt; columns.length; i++) {
 * 	ResultSetColumn rsc = StructureFactory.createResultSetColumn();
 * 	rsc.setPosition(new Integer(i + 1));
 * 	rsc.setColumnName(columns[i].getName());
 * 	rsc.setDataType(columns[i].getType().dataType);
 * 	ph.addItem(rsc); // &lt;= NPE
 * }
 * </pre>
 *
 * <b>Test description:</b>
 * <p>
 * In ScriptDataSet, result set hints has replaced the result set property.
 * Besides this, codes need to provide API compatibility.
 * <p>
 * Test if getPropertyHandle(RESULT_SET_PROP) returns
 * getPropertyHandle(RESULT_SET_HINTS_PROP) and no exception
 *
 */
public class Regression_156977 extends BaseTestCase

{

	/**
	 * @throws SemanticException
	 */
	public void test_regression_156977() throws SemanticException {
		createDesign();

		ScriptDataSetHandle ds = designHandle.getElementFactory().newScriptDataSet("dataSet1"); //$NON-NLS-1$

		PropertyHandle ph = ds.getPropertyHandle(ScriptDataSetHandle.RESULT_SET_PROP);

		assertNotNull(ph);

		for (int i = 0; i < 2; i++) {
			ResultSetColumn rsc = StructureFactory.createResultSetColumn();
			rsc.setPosition(new Integer(i + 1));
			rsc.setColumnName("COLUMN_" + i); //$NON-NLS-1$
			rsc.setDataType(DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL);

			ph.addItem(rsc);
		}
	}
}
