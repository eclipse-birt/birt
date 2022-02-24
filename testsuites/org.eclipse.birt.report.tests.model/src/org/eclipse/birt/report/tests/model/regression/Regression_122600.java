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
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Top n, bottom n, top percent, bottom percent, match and like are not found in
 * "edit highlight" dialog.
 * </p>
 * Test description:
 * <p>
 * Check that those value are listed in highlight(map) operator choice Note:top
 * percent, bottom percent are removed
 */
public class Regression_122600 extends BaseTestCase {

	/**
	 * 
	 */
	public void test_regression_122600() {
		MetaDataDictionary instance = MetaDataDictionary.getInstance();

		IChoiceSet choiceset = instance.getChoiceSet("mapOperator"); //$NON-NLS-1$

		IChoice[] naturedSortedChoices = choiceset.getChoices(null);

		boolean topN = false;
		boolean bottomN = false;
		boolean like = false;
		boolean match = false;

		for (int i = 0; i < naturedSortedChoices.length; i++) {
			IChoice choice = (IChoice) naturedSortedChoices[i];

			if ("top-n".equals(choice.getName())) //$NON-NLS-1$
				topN = true;
			else if ("bottom-n".equals(choice.getName())) //$NON-NLS-1$
				bottomN = true;
			else if ("like".equals(choice.getName())) //$NON-NLS-1$
				like = true;
			else if ("match".equals(choice.getName())) //$NON-NLS-1$
				match = true;

		}
		assertTrue(topN);
		assertTrue(bottomN);
		assertTrue(like);
		assertTrue(match);
	}
}
