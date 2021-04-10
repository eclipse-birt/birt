/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.metadata.BooleanPropertyType;
import org.eclipse.birt.report.model.metadata.DateTimePropertyType;
import org.eclipse.birt.report.model.metadata.FloatPropertyType;
import org.eclipse.birt.report.model.metadata.StringPropertyType;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Types of user properties should be limited to the following simple types:
 * <ul>
 * <li>String
 * <li>Boolean
 * <li>DateTime
 * <li>Float
 * </ul>
 * </p>
 * Test description:
 * <p>
 * Check that user property definision support the four types described.
 * </p>
 */
public class Regression_121154 extends BaseTestCase {

	/**
	 * Check that user property definision support String, Boolean, DateTime, Float
	 * type.
	 */

	public void test_regression_121154() {
		assertTrue(UserPropertyDefn.getAllowedTypes().contains(new StringPropertyType()));
		assertTrue(UserPropertyDefn.getAllowedTypes().contains(new BooleanPropertyType()));
		assertTrue(UserPropertyDefn.getAllowedTypes().contains(new DateTimePropertyType()));
		assertTrue(UserPropertyDefn.getAllowedTypes().contains(new FloatPropertyType()));
	}
}
