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

package org.eclipse.birt.report.model.api.metadata;

import java.util.Comparator;

import org.eclipse.birt.report.model.core.Module;

/**
 * Represents a set of choices on a predefined or user defined property type or
 * property definition.
 */

public interface IChoiceSet {

	/**
	 * Returns true if the choice set has a match for the given name.
	 * 
	 * @param choiceName the choice name to match
	 * @return true if the name matches a choice, false otherwise
	 */

	public boolean contains(String choiceName);

	/**
	 * Tests whether this is a user-defined choice set or not.
	 * 
	 * @return <code>true</code> if the choice set is defined by user. Otherwise,
	 *         <code>false</code>.
	 */

	public boolean isUserDefined();

	/**
	 * Returns the name of this ChoiceSet.
	 * 
	 * @return the name of the ChoiceSet
	 */

	public String getName();

	/**
	 * Returns an array containing the choices, the return choices are in their
	 * internal sequence.
	 * 
	 * @return the array of choices
	 */

	public IChoice[] getChoices();

	/**
	 * Returns an array containing the choices, the return choices are sorted
	 * according to the given comparator. All elements in the array must be mutually
	 * comparable by the specified comparator (that is, c.compare(e1, e2) must not
	 * throw a ClassCastException for any elements e1 and e2 in the array).
	 * 
	 * @param c the comparator that will be used to sort the choices. A
	 *          <tt>null</tt> value indicates that the choices are <i>natural
	 *          ordered </i> by their name.
	 * @return the array of choices sorted according to the given comparator.
	 */

	public IChoice[] getChoices(Comparator<Object> c);

	/**
	 * Finds a Choice in the <code>ChoiceSet</code> for the given choice name.
	 * 
	 * @param name the name of a Choice.
	 * @return the instance of the Choice that matches or <code>null</code> if
	 *         choice not found.
	 */

	public IChoice findChoice(String name);

	/**
	 * Finds a IChoice in the <code>IChoiceSet</code> for its display name. For a
	 * user defined choice, the display name can be <code>null</code>.
	 * 
	 * @param name display name of a IChoice.
	 * @return the instance of the Choice that matches or <code>null</code> if
	 *         choice is not found.
	 */

	public IChoice findChoiceByDisplayName(String name);

	/**
	 * Finds a UserChoice in the <code>IChoiceSet</code> for its display name. For a
	 * user defined choice, the display name can be <code>null</code>.
	 * 
	 * @param module the module
	 * @param name   display name of a UserChoice.
	 * @return the instance of the UserChoice that matches or <code>null</code> if
	 *         choice is not found.
	 */

	public UserChoice findUserChoiceByDisplayName(Module module, String name);

}
