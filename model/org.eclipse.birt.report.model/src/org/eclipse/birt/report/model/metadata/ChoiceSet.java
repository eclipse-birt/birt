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

package org.eclipse.birt.report.model.metadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.UserChoice;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.Module;

/**
 * This class represents a set of choices on a predefined or user defined
 * property type or property definition.
 * 
 * 
 */

public class ChoiceSet implements Cloneable, IChoiceSet {

	/**
	 * Name of the choice set. Used for choice sets defined in rom.def and shared by
	 * several types.
	 */

	protected String name = null;

	/**
	 * The set of valid choices.
	 */

	protected ArrayList<IChoice> choices = null;

	/**
	 * Constructor.
	 * 
	 * @param theName the name of ChoiceSet
	 */

	public ChoiceSet(String theName) {
		name = theName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */

	public Object clone() throws CloneNotSupportedException {
		ChoiceSet set = (ChoiceSet) super.clone();
		// new instance of cloned ChoiceSet choices
		set.choices = new ArrayList<IChoice>();

		if (choices != null) {
			for (int i = 0; i < choices.size(); i++)
				set.choices.add((choices.get(i)).copy());
		}
		return set;
	}

	/**
	 * Create a choice set with no name.
	 * 
	 */

	public ChoiceSet() {
	}

	/**
	 * Returns true if the choice set has a match for the given name.
	 * 
	 * @param choiceName the choice name to match
	 * @return true if the name matches a choice, false otherwise
	 */

	public boolean contains(String choiceName) {
		return findChoice(choiceName) != null;
	}

	/**
	 * Tests whether this is a user-defined choice set or not.
	 * 
	 * @return <code>true</code> if the choice set is defined by user. Otherwise,
	 *         <code>false</code>.
	 */

	public boolean isUserDefined() {
		if (choices == null || choices.size() == 0)
			return false;

		return (choices.get(0) instanceof UserChoice);
	}

	/**
	 * Sets the array of choices.
	 * 
	 * @param choiceArray the choices to set
	 */

	public void setChoices(IChoice[] choiceArray) {
		if (choices == null)
			choices = new ArrayList<IChoice>();

		for (int i = 0; i < choiceArray.length; i++)
			choices.add(choiceArray[i]);
	}

	/**
	 * Returns the name of this ChoiceSet.
	 * 
	 * @return the name of the ChoiceSet
	 */

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the array containing choices.
	 * 
	 * @return the array of choices
	 */

	public IChoice[] getChoices() {
		if (choices == null || choices.isEmpty())
			return null;

		IChoice[] retChoices = new IChoice[choices.size()];
		choices.toArray(retChoices);

		return retChoices;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IChoiceSet#getChoices(java
	 * .util.Comparator)
	 */

	public IChoice[] getChoices(Comparator<Object> c) {
		if (choices == null || choices.isEmpty())
			return null;

		IChoice[] retChoices = new IChoice[choices.size()];
		choices.toArray(retChoices);

		Arrays.sort(retChoices, c);
		return retChoices;
	}

	/**
	 * Finds a Choice in the <code>ChoiceSet</code> for the given choice name.
	 * 
	 * @param name the name of a Choice.
	 * @return the instance of the Choice that matches or <code>null</code> if
	 *         choice not found.
	 * 
	 */

	public IChoice findChoice(String name) {
		IChoice choice = null;
		for (int i = 0; i < choices.size(); i++) {
			choice = choices.get(i);

			if (choice.getName().equalsIgnoreCase(name)) {
				return choice;
			}
		}

		return null;
	}

	/**
	 * Finds a Choice in the <code>ChoiceSet</code> for its display name. For a user
	 * defined choice, the display name can be <code>null</code>.
	 * 
	 * @param name display name of a Choice.
	 * @return the instance of the Choice that matches or <code>null</code> if
	 *         choice is not found.
	 */

	public IChoice findChoiceByDisplayName(String name) {
		IChoice choice = null;
		for (int i = 0; i < choices.size(); i++) {
			choice = choices.get(i);

			String displayName = choice.getDisplayName();

			if ((displayName != null) && (displayName.equalsIgnoreCase(name))) {
				return choice;
			}
		}

		return null;
	}

	/**
	 * Finds a Choice in the <code>ChoiceSet</code> for its display name. For a user
	 * defined choice, the display name can be <code>null</code>.
	 * 
	 * @param module the report design
	 * @param name   display name of a Choice.
	 * @return the instance of the Choice that matches or <code>null</code> if
	 *         choice is not found.
	 */

	public UserChoice findUserChoiceByDisplayName(Module module, String name) {
		UserChoice choice = null;
		for (int i = 0; i < choices.size(); i++) {
			choice = (UserChoice) choices.get(i);
			String displayName = module.getMessage(choice.getDisplayNameKey());

			if ((displayName != null) && (displayName.equalsIgnoreCase(name))) {
				return choice;
			}
			displayName = choice.getDisplayName();
			if ((displayName != null) && (displayName.equalsIgnoreCase(name))) {
				return choice;
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */

	public String toString() {
		if (!StringUtil.isBlank(getName()))
			return getName();
		return super.toString();
	}
}