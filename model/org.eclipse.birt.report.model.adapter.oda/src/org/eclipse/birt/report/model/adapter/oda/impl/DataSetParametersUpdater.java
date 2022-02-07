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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetParameterHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.OdaDataSetParameter;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.datatools.connectivity.oda.design.DataElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSetParameters;
import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;

/**
 * Updates all the design parameters to oda data set parameter handle and linked
 * scalar parameter handle.
 */
class DataSetParametersUpdater {

	/**
	 * The data set handle defined parameters.
	 */

	private List<OdaDataSetParameterHandle> toUpdateParams = null;

	/**
	 * The data set handle defined parameters.
	 */

	private List<OdaDataSetParameterHandle> setDefinedParams = null;

	/**
	 * The new parameter list.
	 */

	private List<OdaDataSetParameter> newParams = null;

	private String dataSourceId = null;
	private String dataSetId = null;

	private OdaDataSetHandle setHandle = null;

	/**
	 * @param setDesign
	 * @param cachedParameters
	 * @param setHandle
	 */

	DataSetParametersUpdater(DataSetDesign setDesign, OdaDataSetHandle setHandle,
			List<OdaDataSetParameter> updateParams) {

		this.setHandle = setHandle;

		Iterator<OdaDataSetParameterHandle> tmpParams = setHandle.parametersIterator();
		setDefinedParams = new ArrayList<OdaDataSetParameterHandle>();
		while (tmpParams.hasNext())
			setDefinedParams.add(tmpParams.next());

		dataSourceId = setDesign.getOdaExtensionDataSourceId();
		dataSetId = setDesign.getOdaExtensionDataSetId();

		this.toUpdateParams = buildUpdateParams(updateParams);

	}

	private List<OdaDataSetParameterHandle> buildUpdateParams(List<OdaDataSetParameter> updateParams) {
		if (updateParams == null)
			return Collections.emptyList();

		List<OdaDataSetParameterHandle> retList = new ArrayList<OdaDataSetParameterHandle>();
		for (int i = 0; i < updateParams.size(); i++) {
			OdaDataSetParameter param = updateParams.get(i);
			for (int j = 0; j < setDefinedParams.size(); j++) {
				OdaDataSetParameterHandle paramHandle = setDefinedParams.get(j);
				if (paramHandle.getStructure() == param && !retList.contains(paramHandle))
					retList.add(paramHandle);

			}
		}
		return retList;
	}

	/**
	 * 
	 */

	public void processDataSetParameters(DataSetParameters params) throws SemanticException {
		if (params == null)
			return;

		if (newParams == null)
			newParams = new ArrayList<OdaDataSetParameter>();

		List<ParameterDefinition> tmpParams = params.getParameterDefinitions();

		OdaDataSetParameterHandle foundParam = null;
		OdaDataSetParameterHandle oldSetParam = null;
		OdaDataSetParameter newParam = null;
		for (int i = 0; i < tmpParams.size(); i++) {
			ParameterDefinition paramDefn = tmpParams.get(i);

			DataElementAttributes dataAttrs = paramDefn.getAttributes();

			foundParam = findDataSetParameterByName(dataAttrs.getName(), Integer.valueOf(dataAttrs.getPosition()),
					Integer.valueOf(dataAttrs.getNativeDataTypeCode()), toUpdateParams.iterator());

			// if foundParam == null, could be two cases: 1. no need to update;
			// 2. this is a new ODA parameter

			if (foundParam == null) {
				oldSetParam = findDataSetParameterByName(dataAttrs.getName(), Integer.valueOf(dataAttrs.getPosition()),
						Integer.valueOf(dataAttrs.getNativeDataTypeCode()), setDefinedParams.iterator());
			}

			if (foundParam == null) {
				// this is a new ODA parameter, need to update from the scratch

				if (oldSetParam == null) {
					newParam = StructureFactory.createOdaDataSetParameter();
				} else {
					// just copy it since no need to update this one

					newParams.add((OdaDataSetParameter) oldSetParam.getStructure().copy());

					continue;
				}
			} else {
				newParam = (OdaDataSetParameter) foundParam.getStructure().copy();
			}

			newParams.add(newParam);

			DataSetParameterUpdater oneUpdater = new DataSetParameterUpdater(newParam, paramDefn, setHandle,
					dataSourceId, dataSetId, setDefinedParams);
			oneUpdater.process();
		}

		// now clear all the old parameters and then set it to newParams
		PropertyHandle propHandle = setHandle.getPropertyHandle(OdaDataSetHandle.PARAMETERS_PROP);
		propHandle.clearValue();
		for (int i = 0; i < newParams.size(); i++) {
			propHandle.addItem(newParams.get(i));
		}
	}

	/**
	 * Returns the matched data set parameter by given name and position.
	 * 
	 * @param dataSetParamName the data set parameter name
	 * @param position         the position
	 * @param params           the iterator of data set parameters
	 * @return the matched data set parameter
	 */

	private OdaDataSetParameterHandle findDataSetParameterByName(String dataSetParamName, Integer position,
			Integer nativeDataType, Iterator params) {
		if (position == null)
			return null;

		while (params.hasNext()) {
			OdaDataSetParameterHandle param = (OdaDataSetParameterHandle) params.next();

			Integer tmpNativeDataType = param.getNativeDataType();
			String tmpNativeName = param.getNativeName();

			// nativeName/name, position and nativeDataType should match.

			// case 1: if the native name is not blank, just use it.

			if (!StringUtil.isBlank(tmpNativeName) && tmpNativeName.equals(dataSetParamName))
				return param;

			// case 2: if the native name is blank, match native data type and
			// position

			if (StringUtil.isBlank(tmpNativeName) && position.equals(param.getPosition())
					&& (tmpNativeDataType == null || tmpNativeDataType.equals(nativeDataType))) {
				return param;
			}
		}

		return null;
	}
}
