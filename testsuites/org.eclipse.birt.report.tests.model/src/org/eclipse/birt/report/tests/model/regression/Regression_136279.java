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

import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * <p>
 * Description: "Structure" and "table" should be hidden in column data binding
 * data type choice list. Steps to reproduce: In table data binding, I saw
 * structure and table two datatypes. They are not supported now, so they should
 * be removed.
 * <p>
 * Test description:
 * <p>
 * Make sure that "columnDataType" choice set do not support "structure" and
 * "table" types.
 * <p>
 */
public class Regression_136279 extends BaseTestCase {

	/**
	 * 
	 */
	public void test_regression_136279() {
		IMetaDataDictionary dict = MetaDataDictionary.getInstance();
		IChoiceSet datatypes = dict.getChoiceSet("columnDataType"); //$NON-NLS-1$
		IChoice[] choices = datatypes.getChoices();

		boolean valid = true;
		for (int i = 0; i < choices.length; i++) {
			if ("Structure".equals(choices[i].getName()) || "table".equals(choices[i].getName())) //$NON-NLS-1$//$NON-NLS-2$
			{
				valid = false;
				break;
			}
		}

		assertTrue(valid);

	}
}
