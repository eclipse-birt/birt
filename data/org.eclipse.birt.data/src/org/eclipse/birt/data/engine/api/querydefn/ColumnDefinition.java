/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
 *
 *************************************************************************
 */
package org.eclipse.birt.data.engine.api.querydefn;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.api.IColumnDefinition;

/**
 * Default implementation of
 * {@link org.eclipse.birt.data.engine.api.IColumnDefinition} interface.
 * <p>
 */
public class ColumnDefinition implements IColumnDefinition {
	private String name, nativeName;
	private int position = -1;
	private int dataType = DataType.UNKNOWN_TYPE;
	private int nativeDataType = 0; // unknown
	private String alias;
	private int searchHint = NOT_SEARCHABLE;
	private int exportHint = DONOT_EXPORT;
	private int analysisType = -1;
	private String analysisColumn;
	private boolean indexColumn;
	private boolean compressedColumn;
	private String displayName;

	/**
	 * Construct a Column definition for a named column
	 */
	public ColumnDefinition(String name) {
		this.name = name;
	}

	/**
	 * Assigns the indexed position to a Column definition.
	 *
	 * @param position 1-based position of column in the data row
	 */
	public void setColumnPosition(int position) {
		this.position = position;
	}

	/**
	 * Gets the column name
	 */
	@Override
	public String getColumnName() {
		return name;
	}

	/**
	 * Gets the column position
	 */
	@Override
	public int getColumnPosition() {
		return position;
	}

	/**
	 * Gets the data type of the column.
	 *
	 * @return Data type as an integer.
	 */
	@Override
	public int getDataType() {
		return dataType;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IColumnDefinition#getNativeDataType()
	 */
	@Override
	public int getNativeDataType() {
		return nativeDataType;
	}

	/**
	 * Gets the alias of the column. An alias is a string that can be used
	 * interchangably as the name to refer to a column.
	 */
	@Override
	public String getAlias() {
		return alias;
	}

	/**
	 * Gets the search hint for the column
	 */
	@Override
	public int getSearchHint() {
		return searchHint;
	}

	@Override
	public int getAnalysisType() {
		return this.analysisType;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IColumnDefinition#getAnalysisName()
	 */
	@Override
	public String getAnalysisColumn() {
		return this.analysisColumn;
	}

	public void setAnalysisColumn(String columnName) {
		this.analysisColumn = columnName;
	}

	/**
	 * Gets the export hint for the column
	 */
	@Override
	public int getExportHint() {
		return exportHint;
	}

	/**
	 * @param alias The alias to set.
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * @param dataType The dataType to set.
	 */
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	/**
	 * Set the column native data type.
	 *
	 * @param typeCode a data type code defined by an underlying data source.
	 */
	public void setNativeDataType(int typeCode) {
		nativeDataType = typeCode;
	}

	/**
	 * @param exportHint The exportHint to set.
	 */
	public void setExportHint(int exportHint) {
		this.exportHint = exportHint;
	}

	/**
	 * @param searchHint The searchHint to set.
	 */
	public void setSearchHint(int searchHint) {
		this.searchHint = searchHint;
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IColumnDefinition#getColumnNativeName()
	 */
	@Override
	public String getColumnNativeName() {
		return this.nativeName;
	}

	public void setColumnNativeName(String nativeName) {
		this.nativeName = nativeName;
	}

	public void setAnalysisType(int analysisType) {
		this.analysisType = analysisType;
	}

	@Override
	public boolean isIndexColumn() {
		return indexColumn;
	}

	public void setIndexColumn(boolean indexColumn) {
		this.indexColumn = indexColumn;
	}

	@Override
	public boolean isCompressedColumn() {
		return this.compressedColumn;
	}

	public void setCompressedColumn(boolean compressedColumn) {
		this.compressedColumn = compressedColumn;
	}

	@Override
	public String getDisplayName() {
		return this.displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setColumnName(String columnName) {
		this.name = columnName;
	}
}
