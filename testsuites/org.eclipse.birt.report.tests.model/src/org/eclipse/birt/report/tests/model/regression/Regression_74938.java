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

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Report design with undefined properties can't be open
 * </p>
 * Test description:
 * <p>
 * Open a report design with undefined properties
 * </p>
 */

public class Regression_74938 extends BaseTestCase
{

	private String filename = "Regression_74938.xml";

	public void test_74938( ) throws DesignFileException
	{
		openDesign( filename );

	}
}
