/*
 *************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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
package org.eclipse.birt.report.data.adapter.internal.adapter;

import org.eclipse.birt.data.engine.api.IColumnDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ColumnDefinition;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * Adapts a Model Column definition
 */
public class ColumnAdapter extends ColumnDefinition {
	/**
	 * Adapts a column from Model ResultSetColumnHandle
	 */
	public ColumnAdapter(ResultSetColumnHandle modelColumn) {
		super(modelColumn.getColumnName());
		if (modelColumn.getPosition() != null)
			setColumnPosition(modelColumn.getPosition().intValue());
		if (modelColumn.getNativeDataType() != null)
			setNativeDataType(modelColumn.getNativeDataType().intValue());
		setDataType(
				org.eclipse.birt.report.data.adapter.api.DataAdapterUtil.adaptModelDataType(modelColumn.getDataType()));
	}

	/**
	 * Adapts a column from Model ColumnHintHandle
	 */
	public ColumnAdapter(ColumnHintHandle modelColumnHint) {
		super(modelColumnHint.getColumnName());
		DataAdapterUtil.updateColumnDefn(this, modelColumnHint);
		this.setAnalysisType(this.acquireAnalysisType(modelColumnHint.getAnalysis()));
	}

	public static int acquireAnalysisType(String type) {
		if (DesignChoiceConstants.ANALYSIS_TYPE_DIMENSION.equals(type))
			return IColumnDefinition.ANALYSIS_DIMENSION;
		else if (DesignChoiceConstants.ANALYSIS_TYPE_MEASURE.equals(type))
			return IColumnDefinition.ANALYSIS_MEASURE;
		else if (DesignChoiceConstants.ANALYSIS_TYPE_ATTRIBUTE.equals(type))
			return IColumnDefinition.ANALYSIS_ATTRIBUTE;
		return -1;
	}
}
