/*******************************************************************************
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
 *******************************************************************************/

package org.eclipse.birt.report.model.adapter.oda.impl;

import org.eclipse.birt.report.model.adapter.oda.impl.ResultSetCriteriaAdapter.DynamicFilter;
import org.eclipse.birt.report.model.api.DynamicFilterParameterHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.FilterExpressionType;
import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;

/**
 * Converts values between a report scalar parameter and ODA Design Session
 * Request.
 * 
 */

class DynamicFilterParameterAdapter extends AbstractReportParameterAdapter {

	/**
	 * The data set handle.
	 */

	private final OdaDataSetHandle setHandle;

	/**
	 * The data set design.
	 */

	private final DataSetDesign setDesign;

	/**
	 * Default constructor.
	 * 
	 * @param setHandle
	 * @param setDesign
	 */

	DynamicFilterParameterAdapter(OdaDataSetHandle setHandle, DataSetDesign setDesign) {
		this.setHandle = setHandle;
		this.setDesign = setDesign;
	}

	/**
	 * Updates ODA values with the given BIRT values.
	 * 
	 * @param paramDefn
	 * @param defaultType
	 * @param dynamicParamHandle
	 */

	protected void updateODADynamicFilter(ParameterDefinition paramDefn, FilterExpressionType defaultType,
			DynamicFilterParameterHandle dynamicParamHandle) {
		updateParameterDefinitionFromReportParam(paramDefn, dynamicParamHandle, setDesign);
	}

	/**
	 * Updates BIRT dynamic filter parameter with the given ODA dynamic filter.
	 * 
	 * @param filterConditionHandle
	 * @param dynamicFilter
	 * @param dynamicFilterParamHandle
	 * @throws SemanticException
	 */

	protected void updateROMDynamicFilterParameter(DynamicFilter dynamicFilter,
			DynamicFilterParameterHandle dynamicFilterParamHandle) throws SemanticException {
		updateAbstractScalarParameter(dynamicFilterParamHandle, dynamicFilter.exprParamDefn.getDynamicInputParameter(),
				null, setHandle);
	}
}
