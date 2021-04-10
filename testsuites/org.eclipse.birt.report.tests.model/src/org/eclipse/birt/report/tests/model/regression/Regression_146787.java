/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.util.List;

import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PredefinedStyle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Bug Description:</b>
 * <p>
 * Data item can't apply a pre-defined style
 * <p>
 * <b>Steps:</b>
 * <ol>
 * <li>Add a data
 * <li>New a style and select General
 * <li>Select Predefined style
 * <li>Drop down the list
 * </ol>
 * <b>Actual result:</b>
 * <p>
 * It can't find the data selection
 * <p>
 * <b>Test Description:</b> Find pre-define
 * <ol>
 * <li>style for data can be traced by name
 * <li>style for data is in the pre-define style list
 * </ol>
 */
public class Regression_146787 extends BaseTestCase {

	/**
	 * 
	 */
	public void test_regression_146787() {
		MetaDataDictionary instance = MetaDataDictionary.getInstance();
		PredefinedStyle dataStyle = (PredefinedStyle) instance.getPredefinedStyle("data"); //$NON-NLS-1$
		assertNotNull(dataStyle);

		boolean data = false;
		List list = instance.getPredefinedStyles();
		for (int i = 0; i < list.size(); i++) {
			PredefinedStyle style = (PredefinedStyle) list.get(i);
			if (style.getName().equals("data")) //$NON-NLS-1$
				data = true;
		}
		assertTrue(data);

	}
}
