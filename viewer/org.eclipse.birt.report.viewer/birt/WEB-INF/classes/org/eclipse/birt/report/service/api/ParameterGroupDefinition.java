/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.service.api;

import java.util.List;

/**
 * Viewer representation of a parameter group
 * 
 * TODO: Add more javadoc
 * 
 */
public class ParameterGroupDefinition
{

	private String name;

	private String displayName;

	private List parameters;

	public ParameterGroupDefinition( String name, String displayName,
			List parameters )
	{
		this.name = name;
		this.displayName = displayName;
		this.parameters = parameters;
	}

	public String getName( )
	{
		return name;
	}

	public String getDisplayName( )
	{
		return displayName;
	}

	public List getParameters( )
	{
		return parameters;
	}

	public int getParameterCount( )
	{
		if ( parameters != null )
			return parameters.size( );
		return 0;
	}

}
