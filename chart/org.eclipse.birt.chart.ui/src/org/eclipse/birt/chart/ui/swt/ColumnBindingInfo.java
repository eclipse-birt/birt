/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt;

/**
 * The class stores column binding information for UI layout.
 * @since 2.3
 */
public class ColumnBindingInfo
{
	
	public static final int COMMON_COLUMN = 0;
	
	public static final int GROUP_COLUMN = 1;
	
	public static final int AGGREGATE_COLUMN = 2;
	
	private String fName;
	
	private String fExpression;
	
	private int fColumnType = COMMON_COLUMN;
	
	private String fImageName;
	
	private String fTooltip;
	
	private Object fObjectHandle;
	
	private String fChartAggExpression;
	
	public ColumnBindingInfo(String name, String expression, int columnType, String imageName, String tooltip, Object objHandle )
	{
		fName = name;
		fExpression = expression;
		fColumnType = columnType;
		fImageName = imageName;
		fTooltip = tooltip;
		fObjectHandle = objHandle;
	}
	
	public ColumnBindingInfo(String name, String expression, String imageName, String tooltip, Object objHandle)
	{
		this( name, expression, COMMON_COLUMN, imageName, tooltip, objHandle );
	}
	
	public ColumnBindingInfo(String name, String imageName, String tooltip, Object objHandle)
	{
		this( name, name, imageName, tooltip, objHandle );
	}
	
	public String getName( )
	{
		return fName;
	}

	
	public String getExpression( )
	{
		return fExpression;
	}

	
	public String getImageName( )
	{
		return fImageName;
	}

	
	public String getTooltip( )
	{
		return fTooltip;
	}
	
	public int getColumnType( )
	{
		return fColumnType;
	}

	public Object getObjectHandle( )
	{
		return fObjectHandle;
	}

	public String getChartAggExpression( )
	{
		return fChartAggExpression;
	}

	
	public void setChartAggExpression( String chartAggExpression )
	{
		fChartAggExpression = chartAggExpression;
	}
}
