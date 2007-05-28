
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.data.impl;

/**
 * 
 */

public class DimColumn
{
	private String dimensionName;
	private String levelName;
	private String columnName;
	
	public DimColumn( String dimensionName, String levelName, String columnName )
	{
		this.dimensionName = dimensionName;
		this.levelName = levelName;
		this.columnName = columnName;
	}

	
	public String getDimensionName( )
	{
		return dimensionName;
	}

	
	public String getLevelName( )
	{
		return levelName;
	}

	
	public String getColumnName( )
	{
		return columnName;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals( Object other )
	{
		DimColumn otherCol = (DimColumn)other;
		if ( equals( otherCol.getDimensionName( ), this.dimensionName ) &&
				equals( otherCol.getLevelName( ), this.levelName ) &&
				equals( otherCol.getColumnName( ), this.columnName ) )
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	private boolean equals( String s1, String s2 )
	{
		if ( ( s1 == null && s2 == null ) ||
				( s1 != null && s2 != null && s1.equals( s2 ) ) )
		{
			return true;
		}
		return false;
	}
}
