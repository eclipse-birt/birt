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

/**
 * Represents the definition of class member. The class member defines the
 * member type besides name, display name ID and tool tip ID.
 */

public class MemberInfo extends LocalizableInfo
{

	/**
	 * The script data type
	 */

	private String dataType;

	/**
	 * Returns the script data type of this member.
	 * 
	 * @return the script data type of this member
	 */

	public String getDataType( )
	{
		return dataType;
	}

	/**
	 * Sets the script data type of this member.
	 * 
	 * @param type
	 *            the script data type to set
	 */

	void setDataType( String type )
	{
		this.dataType = type;
	}
}