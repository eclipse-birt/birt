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

package org.eclipse.birt.report.model.api.metadata;

import org.eclipse.birt.report.model.elements.ReportDesign;

/**
 * Represents a set of choices on a predefined or user defined property type or
 * property definition.
 */

public interface IChoiceSet
{

	/**
	 * Returns true if the choice set has a match for the given name.
	 * 
	 * @param choiceName
	 *            the choice name to match
	 * @return true if the name matches a choice, false otherwise
	 */

	public boolean contains( String choiceName );

	/**
	 * Tests whether this is a user-defined choice set or not.
	 * 
	 * @return <code>true</code> if the choice set is defined by user.
	 *         Otherwise, <code>false</code>.
	 */

	public boolean isUserDefined( );

	/**
	 * Returns the name of this ChoiceSet.
	 * 
	 * @return the name of the ChoiceSet
	 */

	public String getName( );

	/**
	 * Returns the array containing choices.
	 * 
	 * @return the array of choices
	 */

	public IChoice[] getChoices( );

	/**
	 * Finds a Choice in the <code>ChoiceSet</code> for the given choice name.
	 * 
	 * @param name
	 *            the name of a Choice.
	 * @return the instance of the Choice that matches or <code>null</code> if
	 *         choice not found.
	 */

	public IChoice findChoice( String name );

	/**
	 * Finds a IChoice in the <code>IChoiceSet</code> for its display name. For
	 * a user defined choice, the display name can be <code>null</code>.
	 * 
	 * @param name
	 *            display name of a IChoice.
	 * @return the instance of the Choice that matches or <code>null</code> if
	 *         choice is not found.
	 */

	public IChoice findChoiceByDisplayName( String name );

	/**
	 * Finds a UserChoice in the <code>IChoiceSet</code> for its display name. For
	 * a user defined choice, the display name can be <code>null</code>.
	 * 
	 * @param design
	 *            the report design
	 * @param name
	 *            display name of a UserChoice.
	 * @return the instance of the UserChoice that matches or <code>null</code> if
	 *         choice is not found.
	 */

	public UserChoice findUserChoiceByDisplayName( ReportDesign design,
			String name );

}
