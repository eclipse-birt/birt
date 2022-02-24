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

package org.eclipse.birt.report.model.validators;

import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.validators.DataSetRequiredValidator;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * Tests <code>DataSetRequiredValidator</code>.
 */

public class DataSetRequiredValidatorTest extends ValidatorTestCase {

	MyListener listener = new MyListener();

	/**
	 * Tests <code>DataSetRequiredValidator</code>.
	 * 
	 * @throws Exception if any exception
	 */

	public void testDataSetRequiredValidator() throws Exception {
		createDesign();
		MetaDataDictionary.getInstance().setUseValidationTrigger(true);

		TableHandle tableHandle = designHandle.getElementFactory().newTableItem("table1"); //$NON-NLS-1$
		designHandle.addValidationListener(listener);
		designHandle.getBody().add(tableHandle);
		assertTrue(listener.hasError(tableHandle, DataSetRequiredValidator.getInstance().getName(),
				SemanticError.DESIGN_EXCEPTION_MISSING_DATA_SET));

		OdaDataSetHandle dataSetHandle = designHandle.getElementFactory().newOdaDataSet("dataset1"); //$NON-NLS-1$
		designHandle.getDataSets().add(dataSetHandle);
		assertTrue(listener.hasError(tableHandle, DataSetRequiredValidator.getInstance().getName(),
				SemanticError.DESIGN_EXCEPTION_MISSING_DATA_SET));

		// Set DATA_SET_PROP

		tableHandle.setDataSet(dataSetHandle);
		assertFalse(listener.hasError(tableHandle, DataSetRequiredValidator.getInstance().getName(),
				SemanticError.DESIGN_EXCEPTION_MISSING_DATA_SET));

	}
}
