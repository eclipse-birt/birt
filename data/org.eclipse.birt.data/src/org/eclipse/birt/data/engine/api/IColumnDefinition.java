/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */
package org.eclipse.birt.data.engine.api;

/**
 * Describes a column that appears in the data row of a data set. The report
 * designer uses this class to define columns for two purposes: to provide
 * result set metadata for those data sets whose result set metadata cannot be
 * obtained from the driver, and to provide a processing hint to the data
 * engine. <br>
 * A column definition includes a name or a 1-based position to identify the
 * column in the data row. It provides information such as data type, alias,
 * export and search hints about the specified column.
 */
public interface IColumnDefinition {
	public static final int ALWAYS_SEARCHABLE = 1;
	public static final int SEARCHABLE_IF_INDEXED = 2;
	public static final int NOT_SEARCHABLE = 3;

	public static final int DONOT_EXPORT = 1;
	public static final int EXPORT_IF_REALIZED = 2;
	public static final int ALWAYS_EXPORT = 3;

	public static final int ANALYSIS_DIMENSION = 0;
	public static final int ANALYSIS_MEASURE = 1;
	public static final int ANALYSIS_ATTRIBUTE = 2;

	/**
	 * Gets the column name. Column name uniquely identifies a column in the data
	 * row.
	 * 
	 * @return Name of column. If column is unnamed, returns null.
	 */
	public String getColumnName();

	/**
	 * Get the display name of column.
	 * 
	 * @return
	 */
	public String getDisplayName();

	/**
	 * Gets the column native name. Column native name identifies a column in the
	 * meta data.
	 * 
	 * @return Native Name of column. If column native name is unnamed, returns
	 *         null.
	 */
	public String getColumnNativeName();

	/**
	 * Gets the column position.
	 * 
	 * @return 1-based position of column. If column is identified by name, returns
	 *         -1.
	 */
	public int getColumnPosition();

	/**
	 * Gets the data type of the column.
	 * 
	 * @return Data type as an integer.
	 */
	public int getDataType();

	/**
	 * Get the data analysis type.
	 * 
	 * @return
	 */
	public int getAnalysisType();

	/**
	 * Get the data analysis name.
	 * 
	 * @return
	 */
	public String getAnalysisColumn();

	/**
	 * Return whether the column should be generated with index.
	 * 
	 * @return
	 */
	public boolean isIndexColumn();

	/**
	 * Return whether this column need to be compressed.
	 * 
	 * @return
	 */
	public boolean isCompressedColumn();

	/**
	 * Gets the column's native data type as defined by the underlying data source.
	 * The native data type code value is implementation-specific. Default value is
	 * 0 for none or unknown value.
	 * 
	 * @return the native data type code of this column.
	 */
	public int getNativeDataType();

	/**
	 * Gets the alias of the column. An alias is a string that can be used
	 * interchangably as the name to refer to a column.
	 */
	public String getAlias();

	/**
	 * Gets the search hint for the column
	 */
	public int getSearchHint();

	/**
	 * Gets the export hint for the column
	 */
	public int getExportHint();
}
