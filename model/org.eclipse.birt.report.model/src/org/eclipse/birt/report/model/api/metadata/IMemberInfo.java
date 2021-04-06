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

/**
 * Represents the definition of class member. The class member defines the
 * member type besides name, display name ID and tool tip ID.
 */

public interface IMemberInfo extends ILocalizableInfo {

	/**
	 * Returns the script data type of this member.
	 * 
	 * @return the script data type of this member
	 */

	public String getDataType();

	/**
	 * Returns whether this member is static.
	 * 
	 * @return <code>true</code> if this member is true.
	 */

	public boolean isStatic();

	/**
	 * Returns the member type in Class.
	 * 
	 * @return the member type
	 */

	public IClassInfo getClassType();

}
