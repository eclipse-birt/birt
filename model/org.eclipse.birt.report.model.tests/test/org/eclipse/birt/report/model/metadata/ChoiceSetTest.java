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

package org.eclipse.birt.report.model.metadata;

import java.util.Comparator;

import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test case for ChoiceSetType.
 * 
 */
public class ChoiceSetTest extends BaseTestCase {

	private ChoiceSet choiceSet = null;
	private Choice[] choice = new Choice[3];

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		choiceSet = new ChoiceSet("ChoiceSetName"); //$NON-NLS-1$
		choice[0] = new Choice("ChoiceOneName", "ChoiceOne"); //$NON-NLS-1$//$NON-NLS-2$
		choice[1] = new Choice("ChoiceTwoName", "ChoiceTwo"); //$NON-NLS-1$//$NON-NLS-2$
		choice[2] = new Choice("ChoiceThreeName", "ChoiceThree"); //$NON-NLS-1$//$NON-NLS-2$
	}

	/**
	 * test getters and setters.
	 * 
	 */
	public void testGetterAndSetter() {
		choiceSet.setChoices(choice);
		assertEquals("ChoiceSetName", choiceSet.getName()); //$NON-NLS-1$
		assertEquals(3, choiceSet.getChoices().length);

		assertEquals("ChoiceOneName", choiceSet.findChoice("ChoiceOneName") //$NON-NLS-1$//$NON-NLS-2$
				.getName());
		assertEquals("ChoiceTwoName", choiceSet.findChoice("ChoiceTwoName") //$NON-NLS-1$//$NON-NLS-2$
				.getName());
		assertEquals("ChoiceThreeName", choiceSet.findChoice("ChoiceThreeName") //$NON-NLS-1$//$NON-NLS-2$
				.getName());

		assertTrue(choiceSet.contains("ChoiceOneName")); //$NON-NLS-1$
		assertTrue(choiceSet.contains("ChoiceTwoName")); //$NON-NLS-1$
		assertTrue(choiceSet.contains("ChoiceThreeName")); //$NON-NLS-1$
		assertFalse(choiceSet.contains("NotExisting")); //$NON-NLS-1$
	}

	public void testGetChoices() {
		ChoiceSet choiceSet = new ChoiceSet();
		Choice[] choices = new Choice[3];
		choices[0] = new Choice("One", "value1"); //$NON-NLS-1$//$NON-NLS-2$
		choices[1] = new Choice("Two", "value2"); //$NON-NLS-1$//$NON-NLS-2$
		choices[2] = new Choice("Three", "value3"); //$NON-NLS-1$//$NON-NLS-2$
		choiceSet.setChoices(choices);

		// internal list order

		IChoice[] internalChoices = choiceSet.getChoices();
		assertEquals("One", internalChoices[0].getName()); //$NON-NLS-1$
		assertEquals("Two", internalChoices[1].getName()); //$NON-NLS-1$
		assertEquals("Three", internalChoices[2].getName()); //$NON-NLS-1$

		// naturally sorted by internal name.

		IChoice[] naturedSortedChoices = choiceSet.getChoices(null);
		assertEquals("One", naturedSortedChoices[0].getName()); //$NON-NLS-1$
		assertEquals("Three", naturedSortedChoices[1].getName()); //$NON-NLS-1$
		assertEquals("Two", naturedSortedChoices[2].getName()); //$NON-NLS-1$

		// customized comparator, in reverse order.

		IChoice[] sortedChoices = choiceSet.getChoices(new Comparator() {

			public int compare(Object o1, Object o2) {
				IChoice choice1 = (IChoice) o1;
				IChoice choice2 = (IChoice) o2;

				return choice2.getName().compareTo(choice1.getName());
			}
		});
		assertEquals("Two", sortedChoices[0].getName()); //$NON-NLS-1$
		assertEquals("Three", sortedChoices[1].getName()); //$NON-NLS-1$
		assertEquals("One", sortedChoices[2].getName()); //$NON-NLS-1$

	}

	/**
	 * Tests clone choice set.
	 * 
	 * @throws Exception
	 */
	public void testClone() throws Exception {
		choiceSet.setChoices(choice);
		ChoiceSet newChoiceSet = (ChoiceSet) choiceSet.clone();
		assertEquals(newChoiceSet.getChoices().length, choiceSet.getChoices().length);
	}

}
