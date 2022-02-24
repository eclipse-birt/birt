/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/

package org.eclipse.birt.data.engine.executor;

import java.util.HashSet;
import java.util.Set;

/**
 * <code>ResultFieldMetadata</code> contains metadata about a column that is
 * needed by <code>ResultClass</code>.
 */
public class ResultFieldMetadata {
	private int m_driverPosition;
	private String m_name; // column name defined in designed column
	private String m_label; // JDBC Label
	private String m_alias; // user-defined alias
	private Class m_dataType; // can be overwritten by column hints
	private String m_nativeTypeName;
	private boolean m_isCustom;
	private Class m_driverProvidedDataType;
	private int m_analysisType = -1;
	private String m_analysisColumn;
	private boolean m_indexColumn;
	private boolean m_compressedColumn;
	private boolean accessible = true;
	private float m_compressThrehold;
	private int m_customPosition;

	// A consolidated list of all alternate bindings to the column
	private Set<String> bindings;

	public ResultFieldMetadata(int driverPosition, String name, String label, Class dataType, String nativeTypeName,
			boolean isCustom) {
		m_driverPosition = driverPosition;
		m_name = name;
		m_label = label;
		m_alias = label;
		m_dataType = dataType;
		m_nativeTypeName = nativeTypeName;
		m_isCustom = isCustom;
		m_driverProvidedDataType = null;
		// initialize to unknown
		bindings = new HashSet<String>();
		if (name != null) {
			bindings.add(name);
		}
		if (label != null) {
			bindings.add(label);
		}
	}

	public ResultFieldMetadata(int driverPosition, String name, String label, Class dataType, String nativeTypeName,
			boolean isCustom, int analysisType) {
		this(driverPosition, name, label, dataType, nativeTypeName, isCustom);
		this.m_analysisType = analysisType;
	}

	public ResultFieldMetadata(int driverPosition, String name, String label, Class dataType, String nativeTypeName,
			boolean isCustom, int analysisType, String analysisColumn, boolean indexColumn, boolean compressedColumn) {
		this(driverPosition, name, label, dataType, nativeTypeName, isCustom);
		this.m_analysisType = analysisType;
		this.m_analysisColumn = analysisColumn;
		this.m_indexColumn = indexColumn;
		this.m_compressedColumn = compressedColumn;
	}

	public ResultFieldMetadata(int driverPosition, String name, String alias, Set<String> bindings, Class dataType,
			String nativeTypeName, boolean isCustom, int analysisType, String analysisColumn, boolean indexColumn,
			boolean compressedColumn) {
		this(driverPosition, name, alias, dataType, nativeTypeName, isCustom);
		this.m_analysisType = analysisType;
		this.m_analysisColumn = analysisColumn;
		this.m_indexColumn = indexColumn;
		this.m_compressedColumn = compressedColumn;
		this.bindings.addAll(bindings);
	}

	public int getAnalysisType() {
		return this.m_analysisType;
	}

	public void setAnalysisType(int type) {
		this.m_analysisType = type;
	}

	public String getAnalysisColumn() {
		return this.m_analysisColumn;
	}

	public void setAnalysisColumn(String columnName) {
		this.m_analysisColumn = columnName;
	}

	public boolean isIndexColumn() {
		return this.m_indexColumn;
	}

	public void setIndexColumn(boolean indexColumn) {
		this.m_indexColumn = indexColumn;
	}

	public boolean isCompressedColumn() {
		return this.m_compressedColumn;
	}

	public void setCompressedColumn(boolean compressedColumn) {
		this.m_compressedColumn = compressedColumn;
	}

	// returns the driver position from the runtime metadata
	public int getDriverPosition() {
		return m_driverPosition;
	}

	public String getName() {
		return m_name;
	}

	public void setName(String name) {
		bindings.remove(m_name);
		m_name = name;
		bindings.add(m_name);

	}

	public String getAlias() {
		return m_alias;
	}

	public void setAlias(String alias) {
		bindings.remove(m_alias);
		m_alias = alias;
		bindings.add(m_alias);
	}

	public Class getDataType() {
		if (m_dataType != null)
			return m_dataType;

		Class driverDataType = getDriverProvidedDataType();
		if (driverDataType != null)
			return driverDataType;

		// default to a String if data type is unknown
		return String.class;
	}

	public void setDataType(Class dataType) {
		/*
		 * assert( dataType == Integer.class || dataType == Double.class || dataType ==
		 * String.class || dataType == BigDecimal.class || dataType ==
		 * java.util.Date.class || // backward compatibilty dataType ==
		 * java.sql.Date.class || dataType == Time.class || dataType == Timestamp.class
		 * || dataType == IBlob.class || dataType == IClob.class || dataType ==
		 * Boolean.class );
		 */

		m_dataType = dataType;
	}

	public String getNativeTypeName() {
		return m_nativeTypeName;
	}

	public void setNativeTypeName(String nativeTypeName) {
		m_nativeTypeName = nativeTypeName;
	}

	public void setLabel(String label) {
		bindings.remove(m_label);
		this.m_label = label;
		bindings.add(m_label);
	}

	public String getLabel() {
		return m_label;
	}

	public boolean isCustom() {
		return m_isCustom;
	}

	public Class getDriverProvidedDataType() {
		return m_driverProvidedDataType;
	}

	public void setDriverProvidedDataType(Class odaDataTypeAsClass) {
		m_driverProvidedDataType = odaDataTypeAsClass;
	}

	public boolean isAccessible() {
		return this.accessible;
	}

	public void setAccessibility(boolean accessible) {
		this.accessible = accessible;
	}

	public float getCompressThrehold() {
		return this.m_compressThrehold;
	}

	public void setCompressThrehold(float threhold) {
		m_compressThrehold = threhold;
	}

	public int getCustomPosition() {
		return this.m_customPosition;
	}

	public void setCustomPosition(int pos) {
		this.m_customPosition = pos;
	}

	public Set<String> getBindings() {
		return this.bindings;
	}

}
