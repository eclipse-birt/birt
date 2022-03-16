
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.data.impl;

/**
 *
 */

public class NamingUtil {
	private static final String OLAP_PREFIX = "olap/";
	private static final String CUBE_PREFIX = OLAP_PREFIX + "cube_";
	private static final String DIMENSION_PREFIX = OLAP_PREFIX + "dim_";
	private static final String HIERARCHY_PREFIX = OLAP_PREFIX + "hierarchy_";
	private static final String LEVEL_INDEX = OLAP_PREFIX + "level_index_";
	private static final String HIERARCHY_OFFSET = OLAP_PREFIX + "hierarchy_offset_";
	private static final String FACT_TABLE = OLAP_PREFIX + "fact_table_";
	private static final String FTSU_LIST = OLAP_PREFIX + "ftsu_list_";
	private static final String AGGREGATION_RS_DOC = OLAP_PREFIX + "rs_doc_";

	public static final String DERIVED_MEASURE_PREFIX = "_${DERIVED_MEASURE}$_";

	/**
	 *
	 * @param cubeName
	 * @return
	 */
	public static String getCubeDocName(String cubeName) {
		return CUBE_PREFIX + cubeName;
	}

	/**
	 *
	 * @param dimensionName
	 * @return
	 */
	public static String getDimensionDocName(String dimensionName) {
		return DIMENSION_PREFIX + dimensionName;
	}

	/**
	 *
	 * @param hierarchylName
	 * @return
	 */
	public static String getHierarchyDocName(String dimensionName, String hierarchylName) {
		return HIERARCHY_PREFIX + dimensionName + hierarchylName;
	}

	/**
	 *
	 * @param dimensionName
	 * @param levelName
	 * @return
	 */
	public static String getLevelIndexDocName(String dimensionName, String levelName) {
		return LEVEL_INDEX + dimensionName + '_' + levelName;
	}

	public static String getLevelIndexOffsetDocName(String dimensionName, String levelName) {
		return getLevelIndexDocName(dimensionName, levelName) + "_offset";
	}

	/**
	 *
	 * @param levelName
	 * @return
	 */
	public static String getHierarchyOffsetDocName(String dimensionName, String hierarchylName) {
		return HIERARCHY_OFFSET + dimensionName + hierarchylName;
	}

	/**
	 *
	 * @param cubeName
	 * @return
	 */
	public static String getFactTableName(String factTableName) {
		return FACT_TABLE + factTableName;
	}

	/**
	 * construct derived measure name with prefix DERIVED_MEASURE_PREFIX
	 */
	public static String getDerivedMeasureName(String measureName) {
		return DERIVED_MEASURE_PREFIX + measureName;
	}

	/**
	 * construct derived measure name with prefix DERIVED_MEASURE_PREFIX
	 */
	public static String getMeasureName(String name) {
		if (name != null) {
			if (name.startsWith(DERIVED_MEASURE_PREFIX)) {
				return name.substring(DERIVED_MEASURE_PREFIX.length());
			}
		}
		return name;
	}

	/**
	 * construct derived measure name with prefix DERIVED_MEASURE_PREFIX
	 */
	public static boolean isDerivedMeasureName(String name) {
		if (name != null) {
			return name.startsWith(DERIVED_MEASURE_PREFIX);
		}
		return false;
	}

	/**
	 *
	 * @param factTableName
	 * @return
	 */
	public static String getFTSUListName(String factTableName) {
		return FTSU_LIST + factTableName;
	}

	/**
	 *
	 * @param ID
	 * @return
	 */
	public static String getAggregationRSDocName(String ID) {
		return AGGREGATION_RS_DOC + ID;
	}
}
