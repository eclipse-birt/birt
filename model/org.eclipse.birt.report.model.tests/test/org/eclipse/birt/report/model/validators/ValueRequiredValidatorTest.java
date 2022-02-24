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
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.validators.ValueRequiredValidator;
import org.eclipse.birt.report.model.elements.SimpleDataSet;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * Tests <code>ValueRequiredValidator</code>.
 */

public class ValueRequiredValidatorTest extends ValidatorTestCase {

	MyListener listener = new MyListener();

	/**
	 * Tests <code>ValueRequiredValidator</code>.
	 * 
	 * @throws Exception if any exception
	 */

	public void testTriggers() throws Exception {
		createDesign();
		MetaDataDictionary.getInstance().setUseValidationTrigger(true);

		OdaDataSetHandle dataSetHandle = designHandle.getElementFactory().newOdaDataSet("dataset1"); //$NON-NLS-1$
		designHandle.addValidationListener(listener);
		designHandle.getDataSets().add(dataSetHandle);
		assertTrue(listener.hasError(dataSetHandle, ValueRequiredValidator.getInstance().getName(),
				SimpleDataSet.DATA_SOURCE_PROP, PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED));

		OdaDataSourceHandle dataSourceHandle = designHandle.getElementFactory().newOdaDataSource("dataSource1"); //$NON-NLS-1$
		designHandle.getDataSources().add(dataSourceHandle);
		dataSetHandle.setDataSource(dataSourceHandle.getName());
		assertFalse(listener.hasError(dataSetHandle, ValueRequiredValidator.getInstance().getName(),
				SimpleDataSet.DATA_SOURCE_PROP, PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED));

		dataSetHandle.setDataSource(null);
		assertTrue(listener.hasError(dataSetHandle, ValueRequiredValidator.getInstance().getName(),
				SimpleDataSet.DATA_SOURCE_PROP, PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED));
	}
}
