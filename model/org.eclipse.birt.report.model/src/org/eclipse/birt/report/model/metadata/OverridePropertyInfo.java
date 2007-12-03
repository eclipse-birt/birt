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
 * OverrideProperty Information
 * see schema #overrideproperty
 *
 */

public class OverridePropertyInfo
{
	private String allowedUnits = null;
	private boolean useOwnModel = false;
	private String allowedChoices = null;
	
	/**
	 * Get allowedUnits.
	 * for example : in,mm,pt
	 * @return
	 */
	
	public String getAllowedUnits( )
	{
		return allowedUnits;
	}
	
	/**
	 * Sets allowedUnits.
	 * @param allowedUnits
	 */
	
	public void setAllowedUnits( String allowedUnits )
	{
		this.allowedUnits = allowedUnits;
	}
	
	/**
	 * Get allowedUnits.
	 * for example : in,mm,pt
	 * @return
	 */
	
	public String getAllowedChoices( )
	{
		return allowedChoices;
	}
	
	/**
	 * Sets allowedUnits.
	 * @param allowedUnits
	 */
	
	void setAllowedChoices( String allowedChoices )
	{
		this.allowedChoices = allowedChoices;
	}

	/**
	 * Returns useOwnModel.
	 * @return
	 */
	
	public boolean isUseOwnModel( )
	{
		return useOwnModel;
	}

	/**
	 * Sets useOwnModel.
	 * @param useOwnModel
	 */
	
	public void setUseOwnModel( boolean useOwnModel )
	{
		this.useOwnModel = useOwnModel;
	}
	
}
