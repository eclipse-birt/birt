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

/**
 * Viewer representation of a parameter selection choice
 * 
 */
public class ParameterSelectionChoice
{

	private Object value;

	private String label;

	public ParameterSelectionChoice( String label, Object value )
	{
		this.label = label;
		this.value = value;
	}

	public Object getValue( )
	{
		return value;
	}

	public String getLabel( )
	{
		return label;
	}

}
