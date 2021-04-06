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

import java.util.List;

import org.eclipse.birt.report.model.api.extension.IPropertyDefinition;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.birt.report.model.metadata.PropertyType;

public class ExtensionPropertyDefn implements IPropertyDefinition {

	String name = null;

	String displayNameID = null;

	int type = -1;

	boolean isList = false;

	List choices = null;

	List members = null;

	Object defaultValue = null;

	String groupNameID = null;

	boolean canInherit = true;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IPropertyDefn#getGroupName()
	 */
	public String getGroupNameID() {
		return groupNameID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IPropertyDefn#getName()
	 */
	public String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IPropertyDefn#getDisplayName()
	 */
	public String getDisplayName() {
		return ROMExtension.getMessage(displayNameID);
	}

	/**
	 * Sets the resource key for display name
	 * 
	 * @param displayNameID
	 */

	public void setDisplayNameID(String displayNameID) {
		this.displayNameID = displayNameID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IPropertyDefn#getType()
	 */
	public int getType() {
		return type;
	}

	void setType(int type) {
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IPropertyDefn#isList()
	 */
	public boolean isList() {
		return isList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IPropertyDefn#getChoices()
	 */
	public List getChoices() {
		if (type != PropertyType.CHOICE_TYPE)
			return null;
		return choices;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IPropertyDefn#getMembers()
	 */
	public List getMembers() {
		if (type != PropertyType.STRUCT_TYPE)
			return null;
		return members;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IPropertyDefn#getDefaultValue()
	 */
	public Object getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param groupID The group to set.
	 */
	public void setGroup(String groupID) {
		this.groupNameID = groupID;
	}

	/**
	 * @param isList The isList to set.
	 */
	public void setIsList(boolean isList) {
		this.isList = isList;
	}

	/**
	 * @param choices
	 */
	public void setChoices(List choices) {
		this.choices = choices;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.extension.IPropertyDefinition#getDisplayNameID(
	 * )
	 */
	public String getDisplayNameID() {
		return displayNameID;
	}

	public IMethodInfo getMethodInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isReadOnly() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isVisible() {
		// TODO Auto-generated method stub
		return false;
	}

}