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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.elements.structures.OdaDataSetParameter;

/**
 * Represents the parameter for ODA drivers. The parameter is the part of the
 * data set definition, if defined. A parameter can be an input or output
 * parameter. A parameter can also be input and output parameter. Each data set
 * parameter has the following properties:
 * 
 * <p>
 * <dl>
 * <dt><strong>Native Name </strong></dt>
 * <dd>The name known to an ODA custom designer and runtime driver.</dd>
 * 
 * <dt><strong>Parameter Name </strong></dt>
 * <dd>An optionally linked report parameter name.</dd>
 * 
 * <dt><strong>Native Data Type </strong></dt>
 * <dd>Data type defined in the data set driver.</dd>
 * 
 * </dl>
 * 
 */

public class OdaDataSetParameterHandle extends DataSetParameterHandle {

	/**
	 * Constructs the handle of oda data set parameter.
	 * 
	 * @param valueHandle the value handle for oda data set parameter list of one
	 *                    property
	 * @param index       the position of this oda data set parameter in the list
	 */
	public OdaDataSetParameterHandle(SimpleValueHandle valueHandle, int index) {
		super(valueHandle, index);
	}

	/**
	 * Sets the report parameter name which refered by this oda dataset parameter.
	 * 
	 * @param name report parameter name.
	 */

	public void setParamName(String name) {
		setPropertySilently(OdaDataSetParameter.PARAM_NAME_MEMBER, name);
	}

	/**
	 * Sets the native name for this oda dataset parameter.
	 * 
	 * @param nativeName native name
	 */

	public void setNativeName(String nativeName) {
		setPropertySilently(OdaDataSetParameter.NATIVE_NAME_MEMBER, nativeName);
	}

	/**
	 * Returns the name of the report parameter which is referenced by this oda
	 * dataset parameter. Null if there is no report parameter referenced.
	 * 
	 * @return report parameter name.
	 */

	public String getParamName() {
		return getStringProperty(OdaDataSetParameter.PARAM_NAME_MEMBER);
	}

	/**
	 * Returns the native name of this oda dataset parameter.
	 * 
	 * @return the native name
	 */

	public String getNativeName() {
		return getStringProperty(OdaDataSetParameter.NATIVE_NAME_MEMBER);
	}

	/**
	 * Returns the native data type.
	 * 
	 * @return the parameter native data type.
	 */

	public Integer getNativeDataType() {
		return (Integer) getProperty(OdaDataSetParameter.NATIVE_DATA_TYPE_MEMBER);
	}

	/**
	 * Sets the parameter native data type.
	 * 
	 * @param dataType the native data type to set.
	 */

	public void setNativeDataType(Integer dataType) {
		setPropertySilently(OdaDataSetParameter.NATIVE_DATA_TYPE_MEMBER, dataType);
	}
}
