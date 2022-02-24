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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.report.model.adapter.oda.IAmbiguousAttribute;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.datatools.connectivity.oda.design.DataElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.DataElementUIHints;
import org.eclipse.datatools.connectivity.oda.design.InputElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.InputElementUIHints;
import org.eclipse.datatools.connectivity.oda.design.InputParameterAttributes;
import org.eclipse.datatools.connectivity.oda.design.InputParameterUIHints;
import org.eclipse.datatools.connectivity.oda.design.InputPromptControlStyle;
import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;

class ReportParamChecker {

	protected ScalarParameterHandle reportParam;
	protected ParameterDefinition currentParam;

	private List<IAmbiguousAttribute> ambiguousList;

	private Set<String> ambiguousAttrs = null;

	ReportParamChecker(ParameterDefinition currentParam, ScalarParameterHandle reportParam) {
		this.currentParam = currentParam;
		this.reportParam = reportParam;

		ambiguousAttrs = new HashSet<String>(4);
		ambiguousList = new ArrayList<IAmbiguousAttribute>();
	}

	/**
	 * 
	 */

	public List<IAmbiguousAttribute> process() {
		// must visit data element attributes first since matching is done with
		// fields on the data attributes.

		DataElementAttributes dataAttrs = currentParam.getAttributes();
		processDataElementAttributes(dataAttrs);

		InputParameterAttributes inputParamAttrs = currentParam.getInputAttributes();
		processInputParameterAttributes(inputParamAttrs);

		// return the list
		return this.ambiguousList;
	}

	/**
	 * 
	 * @param dataAttrs
	 */
	private void processDataElementAttributes(DataElementAttributes dataAttrs) {
		if (dataAttrs == null)
			return;

		boolean allowsNull = dataAttrs.allowsNull();
		boolean oldValue = reportParam.isRequired();
		// if isRequiried=true, means allowsNull=false. In such case, need
		// to step into this code snippet
		handleValue(allowsNull, oldValue, ScalarParameterHandle.IS_REQUIRED_PROP, true);

		DataElementUIHints dataElementUIHints = dataAttrs.getUiHints();
		processDataElementUIHints(dataElementUIHints);

	}

	/**
	 * 
	 * @param dataElementUiHints
	 */
	private void processDataElementUIHints(DataElementUIHints dataElementUiHints) {
		if (dataElementUiHints == null)
			return;

		// handle propmpText
		String newPromptText = dataElementUiHints.getDisplayName();
		String oldPrompText = reportParam.getPromptText();
		handleValue(newPromptText, oldPrompText, ScalarParameterHandle.PROMPT_TEXT_PROP);

		// handle helpText
		String newHelpText = dataElementUiHints.getDescription();
		String oldHelpText = reportParam.getHelpText();
		handleValue(newHelpText, oldHelpText, ScalarParameterHandle.HELP_TEXT_PROP);

	}

	/**
	 * 
	 * @param attrs
	 */
	private void processInputParameterAttributes(InputParameterAttributes attrs) {
		if (attrs == null)
			return;

		InputElementAttributes inputElementAttrs = attrs.getElementAttributes();
		processInputElementAttributes(inputElementAttrs);

		InputParameterUIHints inputParamUIHints = attrs.getUiHints();
		processInputParameterUIHints(inputParamUIHints);
	}

	/**
	 * 
	 * @param inputParamUiHints
	 */
	private void processInputParameterUIHints(InputParameterUIHints inputParamUiHints) {
		if (inputParamUiHints == null)
			return;

		if (reportParam.getContainer() instanceof ParameterGroupHandle) {
			ParameterGroupHandle groupHandle = (ParameterGroupHandle) reportParam.getContainer();
			String newValue = inputParamUiHints.getGroupPromptDisplayName();
			String oldValue = groupHandle.getDisplayName();

			handleValue(newValue, oldValue, ParameterGroupHandle.DISPLAY_NAME_PROP);
		}

	}

	/**
	 * 
	 * @param attrs
	 */
	private void processInputElementAttributes(InputElementAttributes attrs) {
		if (attrs == null)
			return;

		// update isOptional value

		boolean isOptional = attrs.isOptional();
		boolean oldValue = reportParam.isRequired();

		// if isRequiried=true, means isOptional=false. In such case, need
		// to step into this code snippet
		handleValue(isOptional, oldValue, ScalarParameterHandle.IS_REQUIRED_PROP, true);

		// maskValue -- concealValue
		boolean newConcealValue = attrs.isMasksValue();
		boolean oldConcealValue = reportParam.isConcealValue();
		handleValue(newConcealValue, oldConcealValue, ScalarParameterHandle.CONCEAL_VALUE_PROP, false);

		// TODO handle some complicated members
		// attrs.getDefaultValues( ) -- reportParam.getDefaultValueList
		// attrs.getStaticValueChoices( ) -- reportParam.choiceIterator( );
		// attrs.getDynamicValueChoices( ) -- reportParam.getDataSet( ),
		// reportParam.getValueExpr( ), reportParam.getLabelExpr( )

		InputElementUIHints inputElementUIHints = attrs.getUiHints();
		processInputElementUIHints(inputElementUIHints);
	}

	/**
	 * 
	 * @param inputElementUiHints
	 */
	private void processInputElementUIHints(InputElementUIHints inputElementUiHints) {
		if (inputElementUiHints == null)
			return;

		// handle auto suggest threshold
		int newValue = inputElementUiHints.getAutoSuggestThreshold();
		int oldValue = reportParam.getAutoSuggestThreshold();
		if (newValue != oldValue && !ambiguousAttrs.contains(ScalarParameterHandle.AUTO_SUGGEST_THRESHOLD_PROP)) {
			ambiguousList.add(new AmbiguousAttribute(ScalarParameterHandle.AUTO_SUGGEST_THRESHOLD_PROP, oldValue,
					newValue, true));
			ambiguousAttrs.add(ScalarParameterHandle.AUTO_SUGGEST_THRESHOLD_PROP);
		}

		// handle control type in prompt style
		InputPromptControlStyle style = inputElementUiHints.getPromptStyle();
		processInputPromptControlStyle(style);

	}

	/**
	 * Handles the input prompt control style with the controlType in scalar
	 * parameter handle.
	 * 
	 * @param style
	 */
	private void processInputPromptControlStyle(InputPromptControlStyle style) {
		if (style == null)
			return;

		String newControlType = AdapterUtil.newROMControlType(style);
		String oldControlType = reportParam.getControlType();

		handleValue(newControlType, oldControlType, ScalarParameterHandle.CONTROL_TYPE_PROP);
	}

	/**
	 * 
	 * @param newValue
	 * @param oldValue
	 * @param propName
	 */
	private void handleValue(String newValue, String oldValue, String propName) {
		PropertyHandle propHandle = reportParam.getPropertyHandle(propName);
		if (!CompareUtil.isEquals(newValue, oldValue) && !ambiguousAttrs.contains(propName)) {
			// if new value is null and the report parameter has no local value
			if (newValue == null && !propHandle.isLocal())
				return;

			ambiguousList.add(new AmbiguousAttribute(propName, oldValue, newValue, true));
			ambiguousAttrs.add(propName);
		}
	}

	/**
	 * 
	 * @param newValue
	 * @param oldValue
	 * @param propName
	 * @param isContary
	 */
	private void handleValue(boolean newValue, boolean oldValue, String propName, boolean isContary) {
		if (isContary)
			newValue = !newValue;
		if (!CompareUtil.isEquals(newValue, oldValue) && !ambiguousAttrs.contains(propName)) {
			ambiguousList.add(new AmbiguousAttribute(propName, oldValue, newValue, true));
			ambiguousAttrs.add(propName);
		}
	}
}
