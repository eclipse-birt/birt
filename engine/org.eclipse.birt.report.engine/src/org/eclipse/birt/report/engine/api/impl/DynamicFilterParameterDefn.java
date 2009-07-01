/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api.impl;

import java.util.List;

import org.eclipse.birt.report.engine.api.IDynamicFilterParameterDefn;

public class DynamicFilterParameterDefn extends ParameterDefn
		implements
			IDynamicFilterParameterDefn
{

	private String column;
	private int displayType;
	private List<String> operators;

	public String getColumn( )
	{
		return column;
	}

	public int getDisplayType( )
	{
		return displayType;
	}

	public List<String> getFilterOperatorList( )
	{
		return operators;
	}

	public void setColumn( String column )
	{
		this.column = column;
	}

	public void setDisplayType( int display )
	{
		this.displayType = display;
	}

	public void setFilterOperatorList( List<String> operators )
	{
		this.operators = operators;
	}
}
