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

package org.eclipse.birt.report.model.tests.box;

import org.eclipse.birt.report.model.api.extension.IChoiceDefinition;

/**
 * Implements <code>IChoiceDefinition</code> for testing
 */

public class ChoiceDefinitionImpl implements IChoiceDefinition
{

	String displayNameID = null;
	Object value = null;
	String name = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IChoiceDefn#getDisplayName()
	 */
	public String getDisplayNameID( )
	{
		return displayNameID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IChoiceDefn#getName()
	 */
	public String getName( )
	{
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IChoiceDefn#getValue()
	 */
	public Object getValue( )
	{
		return value;
	}

}
