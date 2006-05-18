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

package org.eclipse.birt.report.model.util;


/**
 * Provides the API compatibility. The version control is to remember the
 * version number and corresponding elements. So that, provide specified
 * functions on the elements.
 * <p>
 */

public class VersionControlMgr
{

	/**
	 * The design file version.
	 */

	private String version = null;

	/**
	 * Returns the current version. If the design file is created through codes,
	 * the version is null.
	 * 
	 * @return the version
	 */

	public String getVersion( )
	{
		return version;
	}

	/**
	 * Sets the design file version.
	 * 
	 * @param version
	 *            the version to set
	 */

	public void setVersion( String version )
	{
		this.version = version;
	}
}
