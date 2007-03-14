
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

public class NamingUtil
{
	private static final String CUBE_PREFIX = "cube_";
	private static final String DIMENSION_PREFIX = "dim_";
	private static final String HIERARCHY_PREFIX = "hierarchy_";
	private static final String LEVEL_INDEX = "level_index_";
	private static final String LEVEL_OFFSET = "level_offset_";
	private static final String FACT_TABLE = "fact_table_";

	/**
	 * 
	 * @param cubeName
	 * @return
	 */
	public static String getCubeDocName( String cubeName )
	{
		return CUBE_PREFIX + cubeName;
	}
	
	/**
	 * 
	 * @param dimensionName
	 * @return
	 */
	public static String getDimensionDocName( String dimensionName )
	{
		return DIMENSION_PREFIX + dimensionName;
	}
	
	/**
	 * 
	 * @param levelName
	 * @return
	 */
	public static String getHierarchyDocName( String levelName )
	{
		return HIERARCHY_PREFIX + levelName;
	}
	
	/**
	 * 
	 * @param levelName
	 * @return
	 */
	public static String getLevelIndexDocName( String levelName )
	{
		return LEVEL_INDEX + levelName;
	}
	
	/**
	 * 
	 * @param levelName
	 * @return
	 */
	public static String getHierarchyOffsetDocName( String levelName )
	{
		return LEVEL_OFFSET + levelName;
	}
	
	/**
	 * 
	 * @param cubeName
	 * @return
	 */
	public static String getFactTableName( String cubeName )
	{
		return FACT_TABLE + cubeName;
	}
}
