/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.adapter.oda.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.report.model.adapter.oda.IODADesignFactory;
import org.eclipse.birt.report.model.adapter.oda.ODADesignFactory;
import org.eclipse.birt.report.model.adapter.oda.util.IdentifierUtility;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaResultSetColumnHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ColumnHint;
import org.eclipse.birt.report.model.api.elements.structures.FormatValue;
import org.eclipse.birt.report.model.api.elements.structures.OdaResultSetColumn;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.datatools.connectivity.oda.design.AxisAttributes;
import org.eclipse.datatools.connectivity.oda.design.AxisType;
import org.eclipse.datatools.connectivity.oda.design.ColumnDefinition;
import org.eclipse.datatools.connectivity.oda.design.DataElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.DataElementIdentifiers;
import org.eclipse.datatools.connectivity.oda.design.DataElementUIHints;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.HorizontalAlignment;
import org.eclipse.datatools.connectivity.oda.design.OutputElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.ResultSetColumns;
import org.eclipse.datatools.connectivity.oda.design.ResultSetDefinition;
import org.eclipse.datatools.connectivity.oda.design.ResultSets;
import org.eclipse.datatools.connectivity.oda.design.ResultSubset;
import org.eclipse.datatools.connectivity.oda.design.TextWrapType;
import org.eclipse.datatools.connectivity.oda.design.ValueFormatHints;
import org.eclipse.emf.common.util.EList;

/**
 * The utility class that converts between ROM ResultSets and ODA ODA
 * ResultSetDefinition.
 * 
 * @see OdaDataSetHandle
 * @see ResultSetDefinition
 */

class ResultSetsAdapter {

	/**
	 * The data set handle.
	 */

	private final OdaDataSetHandle setHandle;

	/**
	 * The data set design.
	 */

	private final DataSetDesign setDesign;

	/**
	 * The data set handle defined parameters.
	 */

	private List setDefinedResults = null;

	/**
	 * The data set handle defined parameters.
	 */

	private List setDefinedColumnHints = null;

	/**
	 * Column hints for computed columns.
	 */

	private List<IStructure> columnHintsForComputedColumns = null;

	/**
	 * 
	 */

	private final IODADesignFactory designFactory;

	private final ResultSetCriteriaAdapter filterAdapter;

	/**
	 * The constructor.
	 * 
	 * @param setHandle the data set handle
	 * @param setDesign the data set design
	 * 
	 */

	ResultSetsAdapter(OdaDataSetHandle setHandle, DataSetDesign setDesign) {
		this.setHandle = setHandle;
		this.setDesign = setDesign;

		filterAdapter = new ResultSetCriteriaAdapter(setHandle, setDesign);

		Iterator tmpIterator = setHandle.resultSetIterator();
		setDefinedResults = new ArrayList();
		while (tmpIterator.hasNext())
			setDefinedResults.add(tmpIterator.next());

		tmpIterator = setHandle.columnHintsIterator();
		setDefinedColumnHints = new ArrayList();
		while (tmpIterator.hasNext())
			setDefinedColumnHints.add(tmpIterator.next());

		designFactory = ODADesignFactory.getFactory();
	}

	/**
	 * Creates the column hint with given column definition and the old column hint.
	 * 
	 * @param columnDefn       the latest column definition
	 * @param cachedColumnDefn the last (cached) column definition
	 * @param oldHint          the existing column hint in the data set handle
	 * @return the newly created column hint
	 */

	static ColumnHint newROMColumnHintFromColumnDefinition(ColumnDefinition columnDefn,
			ColumnDefinition cachedColumnDefn, ColumnHint oldHint, OdaResultSetColumn resultSetColumn) {
		if (columnDefn == null)
			return null;
		String columnName = resultSetColumn == null ? null : resultSetColumn.getColumnName();
		DataElementAttributes dataAttrs = columnDefn.getAttributes();

		ColumnHint newHint = null;
		if (oldHint != null) {
			newHint = (ColumnHint) oldHint.copy();
		} else if (dataAttrs != null) {
			newHint = StructureFactory.createColumnHint();
			OutputElementAttributes outputAttrs = columnDefn.getUsageHints();

			updateColumnHintFromDataAttrs(columnDefn.getAttributes(), null, newHint, resultSetColumn);
			updateColumnHintFromUsageHints(outputAttrs, null, newHint, resultSetColumn);
			updateColumnHintFromAxisAttrs(columnDefn.getMultiDimensionAttributes(), null, newHint);
		}

		if (newHint != null) {
			newHint.setProperty(ColumnHint.COLUMN_NAME_MEMBER, columnName);
		}
		return newHint;
	}

	/**
	 * Checks whether there are values for newly created column hint.
	 * 
	 * @param dataUIHints the latest data ui hints
	 * @param outputAttrs the latest output element attributes
	 * @return <code>true</code> if no column hint value is set. Otherwise
	 *         <code>false</code>.
	 */

	private static boolean hasColumnHintValue(DataElementUIHints dataUIHints, OutputElementAttributes outputAttrs,
			AxisAttributes axisAttrs) {
		if (dataUIHints == null && outputAttrs == null && axisAttrs == null)
			return false;

		boolean isValueSet = false;
		if (dataUIHints != null) {
			if (dataUIHints.getDisplayName() != null)
				isValueSet = true;
		}

		if (!isValueSet && outputAttrs != null) {
			if (outputAttrs.getHelpText() != null)
				isValueSet = true;

			if (!isValueSet) {
				ValueFormatHints formatHints = outputAttrs.getFormattingHints();
				if (formatHints != null)
					isValueSet = true;
			}

		}

		if (!isValueSet && axisAttrs != null) {
			isValueSet = axisAttrs.isSetAxisType();

			if (!isValueSet) {
				isValueSet = axisAttrs.isSetOnColumnLayout();
			}

		}
		return isValueSet;
	}

	/**
	 * Updates column hint values by given data element attributes.
	 * 
	 * @param dataAttrs       the latest data element attributes
	 * @param cachedDataAttrs the last(cached) data element attributes
	 * @param newHint         the column hint
	 */

	private static void updateColumnHintFromDataAttrs(DataElementAttributes dataAttrs,
			DataElementAttributes cachedDataAttrs, ColumnHint newHint, OdaResultSetColumn column) {
		if (dataAttrs == null)
			return;

		Object oldValue = cachedDataAttrs == null ? null : cachedDataAttrs.getName();
		Object newValue = dataAttrs.getName();
		// If column name in hint already matches column name in column, don't
		// update it even if
		// oda has a new column name. Column name in hint and column has to
		// match as model use column name
		// as identifier to relate column and hint
		if (!CompareUtil.isEquals(newHint.getProperty(null, ColumnHint.COLUMN_NAME_MEMBER), column.getColumnName())
				&& !CompareUtil.isEquals(oldValue, newValue))
			newHint.setProperty(ColumnHint.COLUMN_NAME_MEMBER, newValue);

		DataElementUIHints dataUIHints = dataAttrs.getUiHints();
		if (dataUIHints == null)
			return;

		DataElementUIHints cachedDataUIHints = cachedDataAttrs == null ? null : cachedDataAttrs.getUiHints();
		oldValue = cachedDataUIHints == null ? null : cachedDataUIHints.getDisplayName();
		newValue = dataUIHints.getDisplayName();
		if (!CompareUtil.isEquals(oldValue, newValue)) {
			newHint.setProperty(ColumnHint.DISPLAY_NAME_MEMBER, newValue);
		}

		oldValue = cachedDataUIHints == null ? null : cachedDataUIHints.getDisplayNameKey();
		newValue = dataUIHints.getDisplayNameKey();
		if (!CompareUtil.isEquals(oldValue, newValue)) {
			newHint.setProperty(ColumnHint.DISPLAY_NAME_ID_MEMBER, newValue);
		}

		// description to description in data ui hints: not support now

		/*
		 * oldValue = cachedDataUIHints == null ? null : cachedDataUIHints
		 * .getDescription( ); newValue = dataUIHints.getDescription( ); if ( oldValue
		 * == null || !oldValue.equals( newValue ) ) { newHint.setProperty(
		 * ColumnHint.DESCRIPTION_MEMBER, newValue ); }
		 * 
		 * oldValue = cachedDataUIHints == null ? null : cachedDataUIHints
		 * .getDescriptionKey( ); newValue = dataUIHints.getDescriptionKey( ); if (
		 * oldValue == null || !oldValue.equals( newValue ) ) { newHint.setProperty(
		 * ColumnHint.DESCRIPTION_ID_MEMBER, newValue ); }
		 */

	}

	/**
	 * Updates column hint values by given output element attributes.
	 * 
	 * @param outputAttrs       the latest output element attributes
	 * @param cachedOutputAttrs the last(cached) output element attributes
	 * @param newHint           the column hint
	 */

	private static void updateColumnHintFromUsageHints(OutputElementAttributes outputAttrs,
			OutputElementAttributes cachedOutputAttrs, ColumnHint newHint, OdaResultSetColumn column) {
		if (outputAttrs == null)
			return;

		// help text and key

		Object oldValue = cachedOutputAttrs == null ? null : cachedOutputAttrs.getHelpText();
		Object newValue = outputAttrs.getHelpText();
		if (!CompareUtil.isEquals(oldValue, newValue)) {
			newHint.setProperty(ColumnHint.HELP_TEXT_MEMBER, newValue);
		}

		oldValue = cachedOutputAttrs == null ? null : cachedOutputAttrs.getHelpTextKey();
		newValue = outputAttrs.getHelpTextKey();
		if (!CompareUtil.isEquals(oldValue, newValue)) {
			newHint.setProperty(ColumnHint.HELP_TEXT_ID_MEMBER, newValue);
		}

		// m_label maps to heading

		oldValue = cachedOutputAttrs == null ? null : cachedOutputAttrs.getLabel();
		newValue = outputAttrs.getLabel();
		if (!CompareUtil.isEquals(oldValue, newValue)) {
			newHint.setProperty(ColumnHint.HEADING_MEMBER, newValue);
		}

		oldValue = cachedOutputAttrs == null ? null : cachedOutputAttrs.getLabelKey();
		newValue = outputAttrs.getLabelKey();
		if (!CompareUtil.isEquals(oldValue, newValue)) {
			newHint.setProperty(ColumnHint.HEADING_ID_MEMBER, newValue);
		}

		// for values in formatting.

		ValueFormatHints formatHints = outputAttrs.getFormattingHints();
		if (formatHints == null)
			return;

		ValueFormatHints cachedFormatHints = cachedOutputAttrs == null ? null : cachedOutputAttrs.getFormattingHints();
		oldValue = cachedFormatHints == null ? null : cachedFormatHints.getDisplayFormat();

		// convert display format in oda to pattern part of value-format member
		newValue = formatHints.getDisplayFormat();
		if (!CompareUtil.isEquals(oldValue, newValue)) {
			FormatValue format = (FormatValue) newHint.getProperty(null, ColumnHint.VALUE_FORMAT_MEMBER);
			if (format == null && newValue != null) {
				format = StructureFactory.newFormatValue();
				newHint.setProperty(ColumnHint.VALUE_FORMAT_MEMBER, format);
			}

			// add logic to fix 32742: if the column is date-time, then do some
			// special handle for the format string in IO
			if (newValue != null && (column != null
					&& DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME.equals(column.getDataType()))) {
				String formatValue = (String) newValue;
				newValue = formatValue.replaceFirst("mm/", "MM/"); //$NON-NLS-1$//$NON-NLS-2$
			}
			if (format != null)
				format.setPattern((String) newValue);
		}

		// not support display length
		/*
		 * newValue = formatHints.getDisplaySize( ); oldValue = cachedFormatHints ==
		 * null ? null : cachedFormatHints .getDisplaySize( ); if ( oldValue == null ||
		 * !oldValue.equals( newValue ) ) { newHint.setProperty(
		 * ColumnHint.DISPLAY_LENGTH_MEMBER, newValue ); }
		 */

		newValue = formatHints.getHorizontalAlignment();
		oldValue = cachedFormatHints == null ? null : cachedFormatHints.getHorizontalAlignment();
		if (formatHints.isSetHorizontalAlignment() && !CompareUtil.isEquals(oldValue, newValue)) {
			newHint.setProperty(ColumnHint.HORIZONTAL_ALIGN_MEMBER,
					convertToROMHorizontalAlignment((HorizontalAlignment) newValue));
		}

		// not support word-wrap
		/*
		 * newValue = formatHints.getTextWrapType( ); oldValue = cachedFormatHints ==
		 * null ? null : cachedFormatHints .getTextWrapType( );
		 * 
		 * if ( oldValue == null || !oldValue.equals( newValue ) ) {
		 * newHint.setProperty( ColumnHint.WORD_WRAP_MEMBER, convertToROMWordWrap(
		 * (TextWrapType) newValue ) ); }
		 */

		// cannot handle text format since two objects in ODA and ROM are
		// different.
	}

	static String convertToROMHorizontalAlignment(HorizontalAlignment tmpAlign) {
		if (tmpAlign == null)
			return null;

		switch (tmpAlign.getValue()) {
		case HorizontalAlignment.AUTOMATIC:
			return null;
		case HorizontalAlignment.CENTER:
			return DesignChoiceConstants.TEXT_ALIGN_CENTER;
		case HorizontalAlignment.LEFT:
			return DesignChoiceConstants.TEXT_ALIGN_LEFT;
		case HorizontalAlignment.RIGHT:
			return DesignChoiceConstants.TEXT_ALIGN_RIGHT;
		case HorizontalAlignment.LEFT_AND_RIGHT:
			return DesignChoiceConstants.TEXT_ALIGN_JUSTIFY;
		}

		return null;
	}

	private static HorizontalAlignment convertToOdaHorizontalAlignment(String tmpAlign) {
		if (tmpAlign == null)
			return HorizontalAlignment.get(HorizontalAlignment.AUTOMATIC);

		if (DesignChoiceConstants.TEXT_ALIGN_JUSTIFY.equalsIgnoreCase(tmpAlign))
			return HorizontalAlignment.get(HorizontalAlignment.LEFT_AND_RIGHT);

		if (DesignChoiceConstants.TEXT_ALIGN_CENTER.equalsIgnoreCase(tmpAlign))
			return HorizontalAlignment.get(HorizontalAlignment.CENTER);

		if (DesignChoiceConstants.TEXT_ALIGN_LEFT.equalsIgnoreCase(tmpAlign))
			return HorizontalAlignment.get(HorizontalAlignment.LEFT);

		if (DesignChoiceConstants.TEXT_ALIGN_RIGHT.equalsIgnoreCase(tmpAlign))
			return HorizontalAlignment.get(HorizontalAlignment.RIGHT);

		return null;
	}

	static Boolean convertToROMWordWrap(TextWrapType newValue) {
		if (newValue == null)
			return null;

		switch (newValue.getValue()) {
		case TextWrapType.WORD:
			return Boolean.TRUE;
		case TextWrapType.NONE:
			return Boolean.FALSE;
		}

		return null;
	}

	private static TextWrapType convertToROMWordWrap(boolean newValue) {
		if (newValue)
			return TextWrapType.WORD_LITERAL;

		return TextWrapType.NONE_LITERAL;
	}

	/**
	 * Updates column hint values by given axis attributes.
	 * 
	 * @param outputAttrs       the latest axis attributes
	 * @param cachedOutputAttrs the last(cached) axis attributes
	 * @param newHint           the column hint
	 */
	private static void updateColumnHintFromAxisAttrs(AxisAttributes axisAttributes,
			AxisAttributes cachedAxisAttributes, ColumnHint newHint) {
		if (axisAttributes == null)
			return;

		Object newValue = axisAttributes.getAxisType();
		Object oldValue = cachedAxisAttributes == null ? null : cachedAxisAttributes.getAxisType();
		if (!CompareUtil.isEquals(oldValue, newValue)) {
			newHint.setProperty(ColumnHint.ANALYSIS_MEMBER, convertAxisTypeToAnalysisType((AxisType) newValue));
		}

		newValue = axisAttributes.isOnColumnLayout();
		oldValue = cachedAxisAttributes == null ? null : cachedAxisAttributes.isOnColumnLayout();
		if (!CompareUtil.isEquals(oldValue, newValue)) {
			newHint.setProperty(ColumnHint.ON_COLUMN_LAYOUT_MEMBER, newValue);
		}

		newValue = axisAttributes.getRelatedColumns();
		oldValue = cachedAxisAttributes == null ? null : cachedAxisAttributes.getRelatedColumns();
		if (!CompareUtil.isEquals(oldValue, newValue)) {
			String analysisColumnName = null;
			DataElementIdentifiers columns = ((ResultSubset) newValue).getColumnIdentifiers();
			if (columns != null && !columns.getIdentifiers().isEmpty())
				analysisColumnName = columns.getIdentifiers().get(0).getName();
			newHint.setProperty(ColumnHint.ANALYSIS_COLUMN_MEMBER, analysisColumnName);
		}

	}

	/**
	 * Transfers oda axis type to rom analysis type.
	 * 
	 * @param axisType the oda axis type
	 * @return the rom analysis type, or null if no such type defined in rom
	 */
	static String convertAxisTypeToAnalysisType(AxisType axisType) {
		switch (axisType) {
		case MEASURE_LITERAL:
			return DesignChoiceConstants.ANALYSIS_TYPE_MEASURE;
		case DIMENSION_ATTRIBUTE_LITERAL:
			return DesignChoiceConstants.ANALYSIS_TYPE_ATTRIBUTE;
		case DIMENSION_MEMBER_LITERAL:
			return DesignChoiceConstants.ANALYSIS_TYPE_DIMENSION;
		}
		return null;
	}

	/**
	 * Updates column hint values by given column definition.
	 * 
	 * @param columnDefn       the latest column definition
	 * @param cachedColumnDefn the last(cached) column definition
	 * @param setColumn        the oda result set column
	 * @param dataSourceId     the data source id
	 * @param dataSetId        the data set id
	 * @param columns          the iterator that includes oda result set columns
	 */

	private void updateROMOdaResultSetColumnFromColumnDefinition(ColumnDefinition columnDefn,
			ColumnDefinition cachedColumnDefn, OdaResultSetColumn setColumn, String dataSourceId, String dataSetId) {
		if (columnDefn == null)
			return;

		updateResultSetColumnFromDataAttrs(columnDefn.getAttributes(),
				cachedColumnDefn == null ? null : cachedColumnDefn.getAttributes(), setColumn, dataSourceId, dataSetId);
	}

	/**
	 * Updates result set column values by given data element attributes.
	 * 
	 * @param dataAttrs       the latest data element attributes
	 * @param cachedDataAttrs the last (cached) data element attributes
	 * @param newColumn       the result set column
	 * @param dataSourceId    the data source id
	 * @param dataSetId       the data set id
	 * @param params          the iterator that includes oda result set columns
	 */

	private void updateResultSetColumnFromDataAttrs(DataElementAttributes dataAttrs,
			DataElementAttributes cachedDataAttrs, OdaResultSetColumn newColumn, String dataSourceId,
			String dataSetId) {
		if (dataAttrs == null) {
			return;
		}

		Object oldValue = cachedDataAttrs == null ? null : cachedDataAttrs.getName();
		Object newValue = dataAttrs.getName();
		if (!CompareUtil.isEquals(oldValue, newValue)) {
			// if the native name is just empty, treat it as null

			String tmpNativeName = (String) newValue;
			if (tmpNativeName != null && tmpNativeName.length() == 0)
				tmpNativeName = null;

			newColumn.setNativeName(tmpNativeName);
		}

		oldValue = cachedDataAttrs == null ? null : Integer.valueOf(cachedDataAttrs.getPosition());
		newValue = Integer.valueOf(dataAttrs.getPosition());
		if (!CompareUtil.isEquals(oldValue, newValue)) {
			newColumn.setPosition((Integer) newValue);
		}

		oldValue = cachedDataAttrs == null ? null : Integer.valueOf(cachedDataAttrs.getNativeDataTypeCode());
		newValue = Integer.valueOf(dataAttrs.getNativeDataTypeCode());
		if (!CompareUtil.isEquals(oldValue, newValue) || newColumn.getNativeDataType() == null) {
			newColumn.setNativeDataType((Integer) newValue);
		}

		newColumn.setDataType(getROMDataType(dataSourceId, dataSetId, newColumn));
	}

	/**
	 * Returns the rom data type in string.
	 * 
	 * @param dataSourceId    the id of the data source
	 * @param dataSetId       the ide of the data set
	 * @param column          the rom data set parameter
	 * @param setHandleParams params defined in data set handle
	 * @return the rom data type in string
	 */

	private String getROMDataType(String dataSourceId, String dataSetId, OdaResultSetColumn column) {
		String name = column.getNativeName();
		Integer position = column.getPosition();
		Integer nativeDataType = column.getNativeDataType();

		OdaResultSetColumnHandle tmpParam = findOdaResultSetColumn(setDefinedResults.iterator(), name, position,
				nativeDataType);

		if (tmpParam == null)
			return AdapterUtil.convertNativeTypeToROMDataType(dataSourceId, dataSetId,
					column.getNativeDataType().intValue(), null);

		Integer tmpPosition = tmpParam.getPosition();
		if (tmpPosition == null)
			return AdapterUtil.convertNativeTypeToROMDataType(dataSourceId, dataSetId,
					column.getNativeDataType().intValue(), null);

		if (!tmpPosition.equals(column.getPosition()))
			return AdapterUtil.convertNativeTypeToROMDataType(dataSourceId, dataSetId,
					column.getNativeDataType().intValue(), null);

		Integer tmpNativeCodeType = tmpParam.getNativeDataType();
		if (tmpNativeCodeType == null || tmpNativeCodeType.equals(column.getNativeDataType()))
			return tmpParam.getDataType();

		String oldDataType = tmpParam.getDataType();
		return AdapterUtil.convertNativeTypeToROMDataType(dataSourceId, dataSetId,
				column.getNativeDataType().intValue(), oldDataType);
	}

	/**
	 * Returns the matched oda result set column with the specified name and
	 * position.
	 * 
	 * @param columns   the iterator that includes oda result set columns
	 * @param paramName the result set column name
	 * @param position  the position
	 * @return the matched oda result set column
	 */

	static OdaResultSetColumnHandle findOdaResultSetColumn(Iterator columns, String paramName, Integer position,
			Integer nativeDataType, Boolean duplicate) {
		if (position == null || nativeDataType == null)
			return null;

		while (columns.hasNext()) {
			OdaResultSetColumnHandle column = (OdaResultSetColumnHandle) columns.next();

			Integer tmpNativeDataType = column.getNativeDataType();
			String nativeName = column.getNativeName();
			// if the column name is unique, not necessary to check position
			if (duplicate == Boolean.FALSE) {
				if (!StringUtil.isBlank(nativeName) && nativeName.equalsIgnoreCase(paramName)
						&& (tmpNativeDataType == null || nativeDataType.equals(tmpNativeDataType))) {
					return column;
				}
			}
			// if same column name appears in more than one table or oda native name
			// is missing, column position needs to be checked
			if ((StringUtil.isBlank(nativeName) || nativeName.equalsIgnoreCase(paramName))
					&& (position.equals(column.getPosition()))
					&& (tmpNativeDataType == null || nativeDataType.equals(tmpNativeDataType)))
				return column;

		}
		return null;

	}

	/**
	 * Returns the matched oda result set column with the specified name and
	 * position.
	 * 
	 * @param columns   the iterator that includes oda result set columns
	 * @param paramName the result set column name
	 * @param position  the position
	 * @return the matched oda result set column
	 */

	static OdaResultSetColumnHandle findOdaResultSetColumn(Iterator columns, String paramName, Integer position,
			Integer nativeDataType) {
		if (position == null || nativeDataType == null)
			return null;

		while (columns.hasNext()) {
			OdaResultSetColumnHandle column = (OdaResultSetColumnHandle) columns.next();

			Integer tmpNativeDataType = column.getNativeDataType();
			String nativeName = column.getNativeName();
			if ((StringUtil.isBlank(nativeName) || nativeName.equalsIgnoreCase(paramName))
					&& position.equals(column.getPosition())
					&& (tmpNativeDataType == null || nativeDataType.equals(tmpNativeDataType)))
				return column;

		}
		return null;

	}

	/**
	 * Returns the matched column definition with the specified name and position.
	 * 
	 * @param columns   the ODA defined result set column definitions
	 * @param paramName the result set column name
	 * @param position  the position
	 * @return the matched oda result set column
	 */

	private static ColumnDefinition findColumnDefinition(ResultSetColumns columns, String columnName,
			Integer position) {
		if (columns == null || columnName == null)
			return null;

		EList odaColumns = columns.getResultColumnDefinitions();
		if (odaColumns == null || odaColumns.isEmpty())
			return null;

		for (int i = 0; i < odaColumns.size(); i++) {
			ColumnDefinition columnDefn = (ColumnDefinition) odaColumns.get(i);

			DataElementAttributes dataAttrs = columnDefn.getAttributes();
			if (dataAttrs == null)
				continue;

			if (columnName.equals(dataAttrs.getName())
					&& (position == null || position.intValue() == dataAttrs.getPosition()))
				return columnDefn;
		}

		return null;
	}

	/**
	 * Creates a list containing ROM ResultSetColumn according to given ODA
	 * ResultSets.
	 * 
	 * @param setDesign     the data set design
	 * @param setHandle     the data set handle
	 * @param cachedSetDefn the ODA result set in designer values
	 * @return a list containing ROM ResultSetColumn.
	 * @throws SemanticException
	 */

	List<ResultSetColumnInfo> newROMResultSets(ResultSetDefinition cachedSetDefn) throws SemanticException {
		ResultSetColumns cachedSetColumns = cachedSetDefn == null ? null : cachedSetDefn.getResultSetColumns();

		ResultSetDefinition resultDefn = setDesign.getPrimaryResultSet();
		if (resultDefn == null) {
			ResultSets resultSets = setDesign.getResultSets();
			if (resultSets != null && !resultSets.getResultSetDefinitions().isEmpty())
				resultDefn = (ResultSetDefinition) resultSets.getResultSetDefinitions().get(0);
		}

		if (resultDefn == null)
			return null;

		ResultSetColumns setColumns = resultDefn.getResultSetColumns();
		if (setColumns == null)
			return null;

		EList<ColumnDefinition> odaSetColumns = setColumns.getResultColumnDefinitions();
		if (odaSetColumns.isEmpty())
			return null;

		List<ColumnDefinition> oldColumnDefns = new ArrayList<ColumnDefinition>();
		if (cachedSetColumns != null) {
			EList<ColumnDefinition> tmpDefns = cachedSetColumns.getResultColumnDefinitions();
			for (int i = 0; i < tmpDefns.size(); i++)
				oldColumnDefns.add(tmpDefns.get(i));
		}

		List<OdaResultSetColumnHandle> oldColumns = new ArrayList<OdaResultSetColumnHandle>();
		for (int i = 0; i < setDefinedResults.size(); i++)
			oldColumns.add((OdaResultSetColumnHandle) setDefinedResults.get(i));

		List<ColumnHintHandle> oldColumnHints = new ArrayList<ColumnHintHandle>();
		for (int i = 0; i < setDefinedColumnHints.size(); i++)
			oldColumnHints.add((ColumnHintHandle) setDefinedColumnHints.get(i));

		List<ColumnDefinition> newColumnDefns = new ArrayList<ColumnDefinition>();
		for (int i = 0; i < odaSetColumns.size(); i++)
			newColumnDefns.add(odaSetColumns.get(i));

		ROMResultSetsHelper resultSetHelper = new ROMResultSetsHelper(oldColumnDefns, oldColumns, oldColumnHints,
				newColumnDefns, setDesign.getOdaExtensionDataSourceId(), setDesign.getOdaExtensionDataSetId());

		List<ResultSetColumnInfo> retList = new ArrayList<ResultSetColumnInfo>();
		ResultSetColumnInfo setInfo = null;

		for (int i = 0; i < newColumnDefns.size(); i++) {
			ROMResultColumnHelper columnHelper = resultSetHelper.getColumnHelper(i);
			OdaResultSetColumn newColumn = resultSetHelper.getNewColumn(i);
			ColumnDefinition newColumnDefn = columnHelper.getNewColumnDefn();
			ColumnHintHandle oldColumnHintHandle = columnHelper.getOldColumnHint();
			ColumnHint oldColumnHint = oldColumnHintHandle == null ? null
					: (ColumnHint) oldColumnHintHandle.getStructure();

			ColumnHint newColumnHint = newROMColumnHintFromColumnDefinition(newColumnDefn, null, oldColumnHint,
					newColumn);
			setInfo = new ResultSetColumnInfo(newColumn, newColumnHint);
			retList.add(setInfo);
		}

		updateHintsForComputedColumn();

		return retList;
	}

	/**
	 * Returns the matched column hint with the given result set column.
	 * 
	 * @param name    the name of the column hint
	 * @param columns the iterator that includes column hints
	 * @return the matched column hint
	 */

	static ResultSetColumnHandle findColumn(String name, Iterator columns) {
		if (name == null)
			return null;

		while (columns.hasNext()) {
			ResultSetColumnHandle column = (ResultSetColumnHandle) columns.next();
			if (name.equals(column.getColumnName()))
				return column;
		}

		return null;
	}

	/**
	 * Updates the ResultSetDefinition with the given ROM ResultSet columns.
	 * 
	 */

	void updateOdaResultSetDefinition() {
		setDesign.setPrimaryResultSet(newOdaResultSetDefinition());

		filterAdapter.updateODAResultSetCriteria();
	}

	/**
	 * Creates a ResultSetDefinition with the given ROM ResultSet columns.
	 * 
	 * @return the created ResultSetDefinition
	 */

	private ResultSetDefinition newOdaResultSetDefinition() {
		Iterator romSets = setDefinedResults.iterator();
		String name = setHandle.getResultSetName();

		if (!romSets.hasNext())
			return null;

		ResultSetDefinition odaSetDefn = null;
		ResultSetColumns odaSetColumns = null;

		if (!StringUtil.isBlank(name)) {
			odaSetDefn = designFactory.createResultSetDefinition();
			odaSetDefn.setName(name);
		}

		while (romSets.hasNext()) {
			if (odaSetDefn == null)
				odaSetDefn = designFactory.createResultSetDefinition();

			if (odaSetColumns == null)
				odaSetColumns = designFactory.createResultSetColumns();

			OdaResultSetColumnHandle setColumn = (OdaResultSetColumnHandle) romSets.next();

			// get the colum hint

			ColumnHintHandle hint = AdapterUtil.findColumnHint((OdaResultSetColumn) setColumn.getStructure(),
					setDefinedColumnHints.iterator());

			ColumnDefinition columnDefn = designFactory.createColumnDefinition();

			DataElementAttributes dataAttrs = designFactory.createDataElementAttributes();

			String newName = setColumn.getNativeName();
			dataAttrs.setName(newName);

			Integer position = setColumn.getPosition();
			if (position != null)
				dataAttrs.setPosition(setColumn.getPosition().intValue());

			Integer nativeDataType = setColumn.getNativeDataType();
			if (nativeDataType != null)
				dataAttrs.setNativeDataTypeCode(nativeDataType.intValue());

			columnDefn.setAttributes(dataAttrs);
			odaSetColumns.getResultColumnDefinitions().add(columnDefn);

			if (hint == null)
				continue;

			updateOdaColumnHint(columnDefn, hint);

		}

		if (odaSetDefn != null)
			odaSetDefn.setResultSetColumns(odaSetColumns);

		return odaSetDefn;
	}

	/**
	 * Updates oda filter expression by ROM filter condition
	 */
	void updateOdaFilterExpression() {
		filterAdapter.updateODAResultSetCriteria();
	}

	/**
	 * Updates rom filter condition by ODA filter expression
	 */
	void updateROMFilterCondition() throws SemanticException {
		filterAdapter.updateROMSortAndFilter();
	}

	/**
	 * Creates unique result set column names if column names are <code>null</code>
	 * or empty string.
	 * 
	 * @param resultSetColumn a list containing result set columns
	 */

	static void createUniqueResultSetColumnNames(List<ResultSetColumnInfo> columnInfo) {
		if (columnInfo == null || columnInfo.isEmpty())
			return;

		Set<String> names = new HashSet<String>();
		for (int i = 0; i < columnInfo.size(); i++) {
			ResultSetColumnInfo tmpInfo = columnInfo.get(i);
			OdaResultSetColumn column = tmpInfo.column;

			String nativeName = column.getNativeName();
			if (nativeName != null)
				names.add(nativeName);
		}

		Set<String> newNames = new HashSet<String>();
		for (int i = 0; i < columnInfo.size(); i++) {
			ResultSetColumnInfo tmpInfo = columnInfo.get(i);

			OdaResultSetColumn column = tmpInfo.column;
			String nativeName = column.getNativeName();
			String name = column.getColumnName();

			if (!StringUtil.isBlank(name)) {
				newNames.add(name);
				continue;
			}

			nativeName = StringUtil.trimString(nativeName);

			String newName = IdentifierUtility.getUniqueColumnName(names, newNames, nativeName, i);

			newNames.add(newName);
			column.setColumnName(newName);
			if (tmpInfo.hint != null)
				tmpInfo.hint.setProperty(ColumnHint.COLUMN_NAME_MEMBER, newName);
		}

		names.clear();
		newNames.clear();
	}

	/**
	 * Updates column hints for computed columns. Saved in the field.
	 * 
	 */

	private void updateHintsForComputedColumn() {
		Iterator columns = setHandle.computedColumnsIterator();
		List<String> columnNames = new ArrayList<String>();
		while (columns.hasNext()) {
			ComputedColumnHandle tmpColumn = (ComputedColumnHandle) columns.next();
			columnNames.add(tmpColumn.getName());
		}

		for (int i = 0; i < columnNames.size(); i++) {
			String columnName = columnNames.get(i);
			ColumnHintHandle hintHandle = AdapterUtil.findColumnHint(columnName, setDefinedColumnHints.iterator());
			if (hintHandle == null)
				continue;

			if (columnHintsForComputedColumns == null)
				columnHintsForComputedColumns = new ArrayList<IStructure>();

			columnHintsForComputedColumns.add(hintHandle.getStructure().copy());
		}
	}

	/**
	 * Returns column hints for computed columns.
	 * 
	 * @return a list containing column hints structures.
	 */

	List<IStructure> getHintsForComputedColumn() {
		if (columnHintsForComputedColumns == null)
			return Collections.EMPTY_LIST;

		return columnHintsForComputedColumns;
	}

	/**
	 * Updates hint-related information on ODA column definitions.
	 * 
	 */

	void updateOdaColumnHints() {

		ResultSetDefinition columnDefns = setDesign.getPrimaryResultSet();
		if (columnDefns == null)
			return;

		for (int i = 0; i < setDefinedColumnHints.size(); i++) {
			ColumnHintHandle hint = (ColumnHintHandle) setDefinedColumnHints.get(i);
			OdaResultSetColumnHandle column = (OdaResultSetColumnHandle) findColumn(hint.getColumnName(),
					setDefinedResults.iterator());

			if (column == null)
				continue;

			ColumnDefinition odaColumn = findColumnDefinition(columnDefns.getResultSetColumns(), column.getNativeName(),
					column.getPosition());

			if (odaColumn == null)
				continue;

			updateOdaColumnHint(odaColumn, hint);
		}
	}

	/**
	 * Updates hint-related information on the ODA <code>columnDefn</code>.
	 * 
	 * @param columnDefn
	 * @param hint
	 */

	private void updateOdaColumnHint(ColumnDefinition columnDefn, ColumnHintHandle hint) {
		DataElementAttributes dataAttrs = columnDefn.getAttributes();

		DataElementUIHints uiHints = null;
		// update display name

		String displayName = hint.getDisplayName();
		String displayNameKey = hint.getDisplayNameKey();

		if (displayName != null || displayNameKey != null) {
			uiHints = designFactory.createDataElementUIHints();

			uiHints.setDisplayName(displayName);
			uiHints.setDisplayNameKey(displayNameKey);
		}

		// description maps to the description in data element UI hints.
		// String desc = hint.getDescription( );
		// String descKey = hint.getDescriptionKey( );
		/*
		 * if ( desc != null || descKey != null ) { if ( uiHints == null ) uiHints =
		 * designFactory.createDataElementUIHints( );
		 * 
		 * uiHints.setDescription( desc ); uiHints.setDescriptionKey( descKey ); }
		 */

		dataAttrs.setUiHints(uiHints);

		// update usage hints.

		OutputElementAttributes outputAttrs = null;

		String helpText = hint.getHelpText();
		String helpTextKey = hint.getHelpTextKey();

		if (helpText != null || helpTextKey != null) {
			outputAttrs = designFactory.createOutputElementAttributes();
			if (helpText != null || helpTextKey != null) {
				outputAttrs.setHelpText(helpText);
				outputAttrs.setHelpTextKey(helpTextKey);
			}
		}

		// heading maps to m_label

		String heading = hint.getHeading();
		String headingKey = hint.getHeadingKey();
		if (heading != null || headingKey != null) {
			if (outputAttrs == null)
				outputAttrs = designFactory.createOutputElementAttributes();
			if (heading != null || headingKey != null) {
				outputAttrs.setLabel(heading);
				outputAttrs.setLabelKey(headingKey);
			}
		}

		// formatting related.

		FormatValue format = hint.getValueFormat();
		// int displayLength = hint.getDisplayLength( );
		// boolean wordWrap = hint.wordWrap( );
		String horizontalAlign = hint.getHorizontalAlign();

		if ((format != null && format.getPattern() != null) || horizontalAlign != null) {
			if (outputAttrs == null)
				outputAttrs = designFactory.createOutputElementAttributes();

			ValueFormatHints formatHint = designFactory.createValueFormatHints();

			if (format != null)
				formatHint.setDisplayFormat(format.getPattern());
			// formatHint.setDisplaySize( displayLength );
			formatHint.setHorizontalAlignment(convertToOdaHorizontalAlignment(horizontalAlign));
			// formatHint.setTextWrapType( convertToROMWordWrap( wordWrap ) );

			// cannot handle text format since two objects in ODA and ROM are
			// different.

			outputAttrs.setFormattingHints(formatHint);
		}

		columnDefn.setUsageHints(outputAttrs);

		// update axis attributes

		AxisAttributes axisAttrs = null;

		String analysisType = hint.getAnalysis();
		AxisType axisType = convertAnalysisTypeToAxisType(analysisType);

		if (axisType != null) {
			axisAttrs = designFactory.createAxisAttributes();
			axisAttrs.setAxisType(axisType);
			axisAttrs.setOnColumnLayout(hint.isOnColumnLayout());
			String analysisColumnName = hint.getAnalysisColumn();
			if (!StringUtil.isBlank(analysisColumnName)) {
				ResultSubset relatedColumns = designFactory.createResultSubset();
				relatedColumns.addColumnIdentifier(analysisColumnName);
				axisAttrs.setRelatedColumns(relatedColumns);
			}
		}

		columnDefn.setMultiDimensionAttributes(axisAttrs);
	}

	/**
	 * Transfers rom analysis type to oda axis type.
	 * 
	 * @param analysisType the rom analysis type
	 * @return the oda axis type, or null if no such type defined in oda
	 */
	private AxisType convertAnalysisTypeToAxisType(String analysisType) {
		AxisType axisType = null;
		if (DesignChoiceConstants.ANALYSIS_TYPE_MEASURE.equals(analysisType))
			axisType = AxisType.MEASURE_LITERAL;
		else if (DesignChoiceConstants.ANALYSIS_TYPE_ATTRIBUTE.equals(analysisType))
			axisType = AxisType.DIMENSION_ATTRIBUTE_LITERAL;
		else if (DesignChoiceConstants.ANALYSIS_TYPE_DIMENSION.equals(analysisType))
			axisType = AxisType.DIMENSION_MEMBER_LITERAL;
		return axisType;
	}

	/**
	 * The data strcuture to hold a result set column and its column hint.
	 * 
	 */

	static class ResultSetColumnInfo {

		private OdaResultSetColumn column;
		private ColumnHint hint;

		/**
		 * @param column
		 * @param hint
		 */

		ResultSetColumnInfo(OdaResultSetColumn column, ColumnHint hint) {
			this.column = column;
			this.hint = hint;
		}

		/**
		 * Distributes result set columns and column hints to different lists.
		 * 
		 * @param infos   the list containing result set column info
		 * @param columns the list containing result set column
		 * @param hints   the list containing column hint
		 */

		static void updateResultSetColumnList(List<ResultSetColumnInfo> infos, List<OdaResultSetColumn> columns,
				List<ColumnHint> hints) {
			if (infos == null || infos.isEmpty())
				return;

			for (int i = 0; i < infos.size(); i++) {
				ResultSetColumnInfo info = infos.get(i);
				if (columns != null)
					columns.add(info.column);

				if (info.hint != null && hints != null)
					hints.add(info.hint);
			}
		}
	}

}
