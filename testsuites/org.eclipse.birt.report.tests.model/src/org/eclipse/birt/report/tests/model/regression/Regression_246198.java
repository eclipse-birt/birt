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

import java.util.List;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Bug Description:</b>
 * <p>
 * The user property value can not be set for crosstab.
 * <p>
 * <b>Test Description:</b>
 * <p>
 * get property will get user-defined property
 * </p>
 */
public class Regression_246198 extends BaseTestCase {

	private final static String INPUT = "regression_246198.xml";

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		copyResource_INPUT(INPUT, INPUT);
	}

	public void test_regression_246198() throws DesignFileException {
		openDesign(INPUT);
		ExtendedItemHandle xtabHandle = (ExtendedItemHandle) designHandle.getElementByID(14);
		List propDefns = ((ExtendedItem) xtabHandle.getElement()).getPropertyDefns();
		assertTrue(propDefns.contains(xtabHandle.getPropertyDefn("test_up")));
	}

}
