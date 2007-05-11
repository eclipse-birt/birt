/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter;

import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.ISelection;

/**
 * 
 */
public class LevelFilter
{

	private String dimensionName;
	private String levelName;
	private ISelection[] selections;
	
	
	/**
	 * @param level
	 * @param selections
	 */
	public LevelFilter(DimLevel level, ISelection[] selections )
	{
		this.dimensionName = level.getDimensionName( );
		this.levelName = level.getLevelName( );
		this.selections = selections;
	}

	/**
	 * @param dimensionName
	 * @param levelName
	 * @param selections
	 */
	public LevelFilter( String dimensionName, String levelName,
			ISelection[] selections )
	{
		this.dimensionName = dimensionName;
		this.levelName = levelName;
		this.selections = selections;
	}
	
	/**
	 * @return the dimensionName
	 */
	public String getDimensionName( )
	{
		return dimensionName;
	}
	
	/**
	 * @param dimensionName the dimensionName to set
	 */
	public void setDimensionName( String dimensionName )
	{
		this.dimensionName = dimensionName;
	}
	
	/**
	 * @return the levelName
	 */
	public String getLevelName( )
	{
		return levelName;
	}
	
	/**
	 * @param levelName the levelName to set
	 */
	public void setLevelName( String levelName )
	{
		this.levelName = levelName;
	}
	
	/**
	 * @return the selections
	 */
	public ISelection[] getSelections( )
	{
		return selections;
	}
	
	/**
	 * @param selections the selections to set
	 */
	public void setSelections( ISelection[] selections )
	{
		this.selections = selections;
	}
	
}
