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

package org.eclipse.birt.report.model.api.extension;

import java.util.List;

import org.eclipse.birt.report.model.api.metadata.IMethodInfo;

/**
 * Extension adapter class for the IPropertyDefintion. By default, the extension
 * property definition has no resource key for the display name, no choices, no
 * members, no default value, is not a list and is not in any property group of
 * the extension. At the same time, the subclasses should override the some
 * getters about the required name of the property, required display name key of
 * the property and the required type,which is one of those defined in
 * {@link org.eclipse.birt.report.model.metadata.PropertyType}.
 */

abstract public class PropertyDefinition implements IPropertyDefinition {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.extension.IPropertyDefinition#getGroupNameID ()
	 */

	public String getGroupNameID() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IPropertyDefinition#getName()
	 */
	abstract public String getName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.extension.IPropertyDefinition#getDisplayNameID
	 * ()
	 */

	public String getDisplayNameID() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IPropertyDefinition#getType()
	 */

	abstract public int getType();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IPropertyDefinition#isList()
	 */

	public boolean isList() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IPropertyDefinition#getChoices()
	 */

	public List<IChoiceDefinition> getChoices() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IPropertyDefinition#getMembers()
	 */
	public IMethodInfo getMethodInfo() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.extension.IPropertyDefinition#getDefaultValue
	 * ()
	 */

	public Object getDefaultValue() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.extension.IPropertyDefinition#getMembers ()
	 */

	public List<IPropertyDefinition> getMembers() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.extension.IPropertyDefinition#isReadOnly ()
	 */

	public boolean isReadOnly() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.extension.IPropertyDefinition#isVisible ()
	 */

	public boolean isVisible() {
		return true;
	}

}