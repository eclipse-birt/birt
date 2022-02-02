/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.extension.IChoiceDefinition;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.metadata.Choice;
import org.eclipse.birt.report.model.metadata.ChoiceSet;
import org.eclipse.birt.report.model.metadata.ExtensionChoice;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * In ChoiceSet.getChoices(Comparator), if comparator is null, NPE is thrown. It
 * should skip the sorting when comparator is null. This NPE blocks chart
 * extension to get the choiceset for dynamic extended properties.
 * </p>
 * Test description:
 * <p>
 * Access a choice set with Null-Comparator, the choice set contains Extension
 * choice. Ensure the returned list are in nature-order.
 * </p>
 */
public class Regression_118590 extends BaseTestCase {
	/**
	 * 
	 */

	public void test_regression_118590() {
		ChoiceSet choiceSet = new ChoiceSet();
		Choice[] choices = new Choice[3];
		choices[0] = new Choice("one", "value1"); //$NON-NLS-1$//$NON-NLS-2$
		choices[1] = new Choice("two", "value2"); //$NON-NLS-1$//$NON-NLS-2$

		choices[2] = new ExtensionChoice(new IChoiceDefinition() {

			public String getDisplayNameID() {
				return "extID"; //$NON-NLS-1$
			}

			public String getName() {
				return "extName"; //$NON-NLS-1$
			}

			public Object getValue() {
				return "extValue"; //$NON-NLS-1$
			}
		}, null);

		choiceSet.setChoices(choices);

		// access with Null-Comparator

		IChoice[] naturedSortedChoices = choiceSet.getChoices(null);
		assertEquals("extName", naturedSortedChoices[0].getName()); //$NON-NLS-1$
		assertEquals("one", naturedSortedChoices[1].getName()); //$NON-NLS-1$
		assertEquals("two", naturedSortedChoices[2].getName()); //$NON-NLS-1$
	}
}
