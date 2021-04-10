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