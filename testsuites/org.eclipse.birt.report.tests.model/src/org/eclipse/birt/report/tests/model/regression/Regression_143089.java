/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.util.Date;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.metadata.ValidationValueException;
import org.eclipse.birt.report.model.api.util.ParameterValidationUtil;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * <b>Regression description:</b> DateTime type report parameter did not work
 * <p>
 * I try to add a report parameter for the datetime type and it did not return
 * any values. However, if i change the report parameter to string type and type
 * in the date time, it returns value. The column is datetime type in mySQL DB.
 * <p>
 * In ParameterValidationUtil class in model code, the validation of values of
 * datatime type is too strong so that cannot recognize a number of valid
 * values.
 * <p>
 * <b>Test description:</b>
 * <p>
 * Use ParameterValidationUtil to validate the valid datetime.
 * <p>
 */
public class Regression_143089 extends BaseTestCase {

	/**
	 * @throws ValidationValueException
	 */
	public void test_regression_143089() throws ValidationValueException {
		// Test two kind of date format .

		Object obj = ParameterValidationUtil.validate(DesignChoiceConstants.PARAM_TYPE_DATETIME, null,
				"1/1/1999 4:50:10 am", ULocale.US); //$NON-NLS-1$
		assertNotNull(obj);
		assertTrue(obj instanceof Date);

		try {
			ParameterValidationUtil.validate(DesignChoiceConstants.PARAM_TYPE_DATETIME, null, "1999-2-27", ULocale.US); //$NON-NLS-1$
			fail();

		} catch (ValidationValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}
	}
}
