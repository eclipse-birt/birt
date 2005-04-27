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

package org.eclipse.birt.report.engine.api.impl;

import org.eclipse.birt.report.engine.api.IParameterDefn;
/**
 * Created on Oct 26, 2004 Base class for defining parameters.
 */

public class ParameterDefn extends ParameterDefnBase implements IParameterDefn
{
	protected boolean isHidden;

	/**
	 * @param isHidden
	 */
	public void setIsHidden( boolean isHidden )
	{
		this.isHidden = isHidden;
	}

	public boolean isHidden( )
	{
		return isHidden;
	}
	
	/**
	 * Returns the parameter data type. See the ColumnDefn class for the valid
	 * data type constants.
	 * 
	 * @return the parameter data type
	 */
}