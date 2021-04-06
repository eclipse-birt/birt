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
import java.util.List;

import org.eclipse.birt.report.model.adapter.oda.IAmbiguousAttribute;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.OdaDataSetParameterHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.elements.structures.OdaDataSetParameter;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.datatools.connectivity.oda.design.DataElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.ElementNullability;
import org.eclipse.datatools.connectivity.oda.design.InputElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;
import org.eclipse.datatools.connectivity.oda.design.ParameterMode;
import org.eclipse.datatools.connectivity.oda.design.StaticValues;

/**
 * Checks one design parameter with oda data set parameter handle and linked
 * scalar parameter handle.
 */

class DataSetParameterChecker {

	List<IAmbiguousAttribute> ambiguousList;

	ParameterDefinition paramDefn = null;

	OdaDataSetParameterHandle paramHandle;

	/**
	 * 
	 * @param paramDefn
	 * @param paramHandle
	 */

	DataSetParameterChecker(ParameterDefinition paramDefn, OdaDataSetParameterHandle paramHandle) {
		if (paramDefn == null || paramHandle == null)
			throw new IllegalArgumentException(
					"The parameter definition and oda data set parameter handle can not be null!"); //$NON-NLS-1$
		this.paramDefn = paramDefn;
		this.paramHandle = paramHandle;
		this.ambiguousList = new ArrayList<IAmbiguousAttribute>();
	}

	/**
	 * 
	 */

	List<IAmbiguousAttribute> process() {
		// handle the members in ParameterDefinition one by one, if old value
		// and new value is not equal, then it needs updates and add it to
		// ambigousMap

		// handle attributes
		DataElementAttributes dataAttrs = paramDefn.getAttributes();
		processDataElementAttributes(dataAttrs);

		// handle in-out mode
		ParameterMode inOutMode = paramDefn.getInOutMode();
		processInOutMode(inOutMode);

		// handle input parameter attributes: must find the linked the parameter
		// element handle and then do the check, so postpone and handle this in
		// reportParamChecker
		// InputParameterAttributes attrs = paramDefn.getInputAttributes( );

		// handle input element attributes
		InputElementAttributes inputElementAttrs = paramDefn.getEditableInputElementAttributes();
		processInputElementAttributes(inputElementAttrs);

		// must visit data element attributes first since matching is done with
		// fields on the data attributes.

		processLinkedReportParameter();

		return this.ambiguousList;

	}

	/**
	 * 
	 */
	private void processDataElementAttributes(DataElementAttributes dataAttrs) {
		// check the native name

		if (dataAttrs == null)
			return;

		// compare the name with the native name in oda data set parameter

		String newValue = dataAttrs.getName();
		String oldValue = paramHandle.getNativeName();
		handleValue(newValue, oldValue, OdaDataSetParameter.NATIVE_NAME_MEMBER);

		// in this case, the position in data set parameter structure and that
		// in data element attributes is equal, so need no comparison about the
		// 'position'

		// compare it with the allowNull in parameter structure
		ElementNullability nullability = dataAttrs.getNullability();
		processElementNullability(nullability);
	}

	/**
	 * 
	 * @param nullability
	 */
	private void processElementNullability(ElementNullability nullability) {
		if (nullability == null)
			return;
		Boolean newValue = AdapterUtil.getROMNullability(nullability);
		Boolean oldValue = paramHandle.allowNull();
		handleValue(newValue, oldValue, OdaDataSetParameter.ALLOW_NULL_MEMBER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.adapter.oda.impl.IDataSetParameterProcessor #
	 * processInOutMode(org.eclipse.datatools.connectivity.oda.design.ParameterMode
	 * )
	 */
	public void processInOutMode(ParameterMode mode) {
		if (mode == null)
			return;

		Boolean newIsInput = null;
		Boolean newIsOutput = null;

		switch (mode.getValue()) {
		case ParameterMode.IN_OUT:
			newIsInput = Boolean.TRUE;
			newIsOutput = Boolean.TRUE;
			break;
		case ParameterMode.IN:
			newIsInput = Boolean.TRUE;
			newIsOutput = Boolean.FALSE;
			break;
		case ParameterMode.OUT:
			newIsInput = Boolean.FALSE;
			newIsOutput = Boolean.TRUE;
			break;
		}

		boolean oldIsInput = paramHandle.isInput();
		boolean oldIsOutput = paramHandle.isOutput();

		handleValue(newIsInput, oldIsInput, OdaDataSetParameter.IS_INPUT_MEMBER);
		handleValue(newIsOutput, oldIsOutput, OdaDataSetParameter.IS_OUTPUT_MEMBER);
	}

	/**
	 * 
	 * @param attrs
	 */
	public void processInputElementAttributes(InputElementAttributes attrs) {
		boolean withLinkedParameter = !StringUtil.isBlank(paramHandle.getParamName());

		if (!withLinkedParameter) {
			StaticValues defaultValue = attrs.getDefaultValues();
			Object newValue = null;
			if (defaultValue != null && !defaultValue.isEmpty())
				newValue = defaultValue.getValues().get(0);

			Expression newDefaultValue = AdapterUtil.createExpression(newValue);
			Expression oldDefaultExpr = (Expression) getLocalValue(OdaDataSetParameter.DEFAULT_VALUE_MEMBER);
			if (!CompareUtil.isEquals(newDefaultValue, oldDefaultExpr)) {
				ambiguousList.add(new AmbiguousAttribute(OdaDataSetParameter.DEFAULT_VALUE_MEMBER, oldDefaultExpr,
						newDefaultValue, false));
			}
		}

		// handle 'isOptional'
		boolean newIsOptional = attrs.isOptional();
		Boolean oldIsOptional = paramHandle.isOptional();
		handleValue(newIsOptional, oldIsOptional, OdaDataSetParameter.IS_OPTIONAL_MEMBER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.adapter.oda.impl.IDataSetParameterProcessor
	 * #processLinkedReportParameter()
	 */
	public void processLinkedReportParameter() {
		String reportParamName = paramHandle.getParamName();
		if (StringUtil.isBlank(reportParamName))
			return;

		ScalarParameterHandle reportParam = (ScalarParameterHandle) paramHandle.getModule().getModuleHandle()
				.findParameter(reportParamName);

		if (reportParam == null)
			return;

		ReportParamChecker tmpVisitor = new ReportParamChecker(paramDefn, reportParam);
		List<IAmbiguousAttribute> tmpList = tmpVisitor.process();

		ambiguousList.addAll(tmpList);

	}

	private Object getLocalValue(String memberName) {
		OdaDataSetParameter param = (OdaDataSetParameter) paramHandle.getStructure();
		Object localValue = param.getLocalProperty(paramHandle.getModule(), memberName);
		return localValue;
	}

	/**
	 * 
	 * @param newValue
	 * @param oldValue
	 * @param propName
	 */
	private void handleValue(String newValue, String oldValue, String propName) {
		if (!CompareUtil.isEquals(newValue, oldValue)) {
			// if new value is null and old value is null too, then we will do
			// nothing during updating, so we need not record them in the
			// ambiguous list
			if (newValue == null && getLocalValue(propName) == null)
				return;

			ambiguousList.add(new AmbiguousAttribute(propName, oldValue, newValue, false));
		}
	}

	/**
	 * 
	 * @param newValue
	 * @param oldValue
	 * @param propName
	 */
	private void handleValue(Boolean newValue, Boolean oldValue, String propName) {
		if (!CompareUtil.isEquals(newValue, oldValue)) {
			// if new value is null and old value is null too, then we will do
			// nothing during updating, so we need not record them in the
			// ambiguous list
			if (newValue == null && getLocalValue(propName) == null)
				return;

			ambiguousList.add(new AmbiguousAttribute(propName, oldValue, newValue, false));
		}
	}

}
