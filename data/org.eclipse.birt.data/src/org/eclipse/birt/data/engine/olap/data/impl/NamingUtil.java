
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
	private static final String OLAP_PREFIX = "olap/";
	private static final String CUBE_PREFIX = OLAP_PREFIX + "cube_";
	private static final String DIMENSION_PREFIX = OLAP_PREFIX + "dim_";
	private static final String HIERARCHY_PREFIX = OLAP_PREFIX + "hierarchy_";
	private static final String LEVEL_INDEX = OLAP_PREFIX + "level_index_";
	private static final String HIERARCHY_OFFSET = OLAP_PREFIX + "hierarchy_offset_";
	private static final String FACT_TABLE = OLAP_PREFIX + "fact_table_";
	private static final String FTSU_LIST = OLAP_PREFIX + "ftsu_list_";

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
	 * @param hierarchylName
	 * @return
	 */
	public static String getHierarchyDocName( String hierarchylName )
	{
		return HIERARCHY_PREFIX + hierarchylName;
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
	public static String getHierarchyOffsetDocName( String hierarchylName )
	{
		return HIERARCHY_OFFSET + hierarchylName;
	}
	
	/**
	 * 
	 * @param cubeName
	 * @return
	 */
	public static String getFactTableName( String factTableName )
	{
		return FACT_TABLE + factTableName;
	}
	
	
	public static String getFTSUListName( String factTableName )
	{
		return FTSU_LIST + factTableName;
	}
}
