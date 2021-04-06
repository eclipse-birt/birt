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

import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * The FilterCond structure has an operator member that is of type
 * filterOperator. The names of some of the choices have issues:
 * 
 * null: should be is-null not-null: fine, or change to is-not-null true: should
 * be is-true false: should be is-false
 * </p>
 * Test description:
 * <p>
 * Check that choice names are properly worded
 */
public class Regression_78796 extends BaseTestCase {

	/**
	 * 
	 */

	public void test_regression_78796() {
		MetaDataDictionary instance = MetaDataDictionary.getInstance();

		IChoiceSet choiceset = instance.getChoiceSet("filterOperator"); //$NON-NLS-1$

		IChoice[] naturedSortedChoices = choiceset.getChoices(null);
		assertEquals("is-null", naturedSortedChoices[9].getName()); //$NON-NLS-1$
		assertEquals("is-not-null", naturedSortedChoices[8].getName());//$NON-NLS-1$
		assertEquals("is-true", naturedSortedChoices[10].getName());//$NON-NLS-1$
		assertEquals("is-false", naturedSortedChoices[7].getName());//$NON-NLS-1$
	}
}
