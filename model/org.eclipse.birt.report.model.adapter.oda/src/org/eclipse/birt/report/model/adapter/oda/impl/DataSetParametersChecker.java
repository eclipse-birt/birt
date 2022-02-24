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

import org.eclipse.birt.report.model.adapter.oda.IAmbiguousAttribute;
import org.eclipse.birt.report.model.adapter.oda.IAmbiguousParameterNode;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetParameterHandle;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.datatools.connectivity.oda.design.DataElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSetParameters;
import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;

/**
 * Class to check all the parameter definition defined by data set design.
 */
class DataSetParametersChecker {

	/**
	 * Whether it is ambiguous for the current data set parameter.
	 */

	private boolean ambiguous = false;

	/**
	 * 
	 */
	private final DataSetDesign setDesign;

	private final Iterator setDefinedParamIter;

	/**
	 * @param setDesign
	 * @param cachedParameters
	 * @param setHandle
	 */

	DataSetParametersChecker(DataSetDesign setDesign, OdaDataSetHandle setHandle) {
		this.setDesign = setDesign;
		this.setDefinedParamIter = setHandle.parametersIterator();
	}

	/**
	 * 
	 */

	List<IAmbiguousParameterNode> process() {
		DataSetParameters params = setDesign.getParameters();
		if (params == null)
			return Collections.emptyList();

		List<IAmbiguousParameterNode> ambiguousParameters = new ArrayList<IAmbiguousParameterNode>(4);

		OdaDataSetParameterHandle existingParamHandle = null;

		List<ParameterDefinition> tmpParams = params.getParameterDefinitions();
		for (int i = 0; i < tmpParams.size(); i++) {
			ambiguous = false;

			ParameterDefinition paramDefn = tmpParams.get(i);

			DataElementAttributes dataAttrs = paramDefn.getAttributes();
			if (dataAttrs != null) {
				existingParamHandle = findDataSetParameterByName(dataAttrs.getName(),
						Integer.valueOf(dataAttrs.getPosition()), Integer.valueOf(dataAttrs.getNativeDataTypeCode()),
						setDefinedParamIter);
			}

			// if the name is equal or nothing is found, then no need to do
			// check further
			if (ambiguous == false || existingParamHandle == null)
				continue;

			DataSetParameterChecker oneChecker = new DataSetParameterChecker(paramDefn, existingParamHandle);

			List<IAmbiguousAttribute> attrs = oneChecker.process();
			if (attrs == null || attrs.isEmpty())
				continue;

			IAmbiguousParameterNode node = new AmbiguousParameterNode(existingParamHandle, attrs);
			ambiguousParameters.add(node);
		}

		return ambiguousParameters;
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
				ambiguous = true;
				return param;
			}
		}

		return null;
	}

}
