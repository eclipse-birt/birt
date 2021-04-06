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

package org.eclipse.birt.report.designer.tests.example.matrix;

import org.eclipse.birt.report.model.api.extension.IChoiceDefinition;

/**
 * 
 */
public class ChoiceDefn implements IChoiceDefinition {

	String displayNameID = null;
	Object value = null;
	String name = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IChoiceDefn#getDisplayName()
	 */
	public String getDisplayNameID() {
		return displayNameID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IChoiceDefn#getName()
	 */
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IChoiceDefn#getValue()
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param displayNameID
	 */
	public void setDisplayNameID(String displayNameID) {
		this.displayNameID = displayNameID;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param value The value to set.
	 */
	public void setValue(Object value) {
		this.value = value;
	}
}