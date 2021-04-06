
package org.eclipse.birt.report.model.adapter.oda.impl;

import org.eclipse.birt.report.model.adapter.oda.IODADesignFactory;
import org.eclipse.birt.report.model.adapter.oda.ODADesignFactory;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.OdaDataSetParameter;
import org.eclipse.datatools.connectivity.oda.design.DataElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.DataElementUIHints;
import org.eclipse.datatools.connectivity.oda.design.DynamicValuesQuery;
import org.eclipse.datatools.connectivity.oda.design.InputElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.InputElementUIHints;
import org.eclipse.datatools.connectivity.oda.design.InputParameterAttributes;
import org.eclipse.datatools.connectivity.oda.design.InputParameterUIHints;
import org.eclipse.datatools.connectivity.oda.design.InputPromptControlStyle;
import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;
import org.eclipse.datatools.connectivity.oda.design.ScalarValueChoices;
import org.eclipse.datatools.connectivity.oda.design.StaticValues;

/**
 *
 */

class ReportParamUpdater {

	protected ScalarParameterHandle reportParam;
	protected ParameterDefinition currentParam;
	private OdaDataSetHandle setHandle;

	private OdaDataSetParameter newParam = null;
	protected final IODADesignFactory designFactory;

	ReportParamUpdater(ParameterDefinition currentParam, OdaDataSetParameter newParam,
			ScalarParameterHandle reportParam, OdaDataSetHandle setHandle) {
		this.currentParam = currentParam;
		this.reportParam = reportParam;
		this.newParam = newParam;
		this.setHandle = setHandle;
		designFactory = ODADesignFactory.getFactory();
	}

	/**
	 * Refreshes property values of the given report parameter by the given
	 * parameter definition and cached parameter definition. If values in cached
	 * parameter definition is null or values in cached parameter definition are not
	 * equal to values in parameter definition, update values in given report
	 * parameter.
	 * 
	 * @param reportParam     the report parameter
	 * @param paramDefn       the ODA parameter definition
	 * @param cachedParamDefn the cached ODA parameter definition in designerValues
	 * @param dataType        the updated data type
	 * @param setHandle       the ROM data set that has the corresponding data set
	 *                        parameter
	 * @throws SemanticException if value in the data set design is invalid
	 */

	public void process() throws SemanticException {
		String dataType = newParam.getDataType();

		CommandStack cmdStack = reportParam.getModuleHandle().getCommandStack();
		try {
			cmdStack.startTrans(null);

			// any type is not support in report parameter data type.

			if (dataType != null) {
				if (!DesignChoiceConstants.PARAM_TYPE_ANY.equalsIgnoreCase(dataType)) {
					reportParam.setDataType(dataType);
				} else {
					reportParam.setDataType(DesignChoiceConstants.PARAM_TYPE_STRING);
				}
			}

			processDataSetParameter(currentParam);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/**
	 * 
	 * @param param
	 * @throws SemanticException
	 */
	private void processDataSetParameter(ParameterDefinition param) throws SemanticException {
		// must visit data element attributes first since matching is done with
		// fields on the data attributes.

		DataElementAttributes dataAttrs = param.getAttributes();
		processDataElementAttributes(dataAttrs);

		InputParameterAttributes inputParamAttrs = param.getInputAttributes();
		processInputParameterAttributes(inputParamAttrs);
	}

	/**
	 * 
	 * @param dataAttrs
	 * @throws SemanticException
	 */
	private void processDataElementAttributes(DataElementAttributes dataAttrs) throws SemanticException {
		if (dataAttrs == null)
			return;

		reportParam.setIsRequired(!dataAttrs.allowsNull());

		DataElementUIHints dataUIHints = dataAttrs.getUiHints();
		processDataElementUIHints(dataUIHints);
	}

	/**
	 * 
	 * @param dataUiHints
	 * @throws SemanticException
	 */
	private void processDataElementUIHints(DataElementUIHints dataUiHints) throws SemanticException {
		if (dataUiHints == null)
			return;

		String text = dataUiHints.getDisplayName();
		String textKey = dataUiHints.getDisplayNameKey();

		if (text != null || textKey != null) {
			reportParam.setPromptText(text);
			reportParam.setPromptTextID(textKey);
		}

		text = dataUiHints.getDescription();
		textKey = dataUiHints.getDescriptionKey();

		if (text != null || textKey != null) {
			reportParam.setHelpText(text);
			reportParam.setHelpTextKey(textKey);
		}

	}

	/**
	 * 
	 * @param attrs
	 * @throws SemanticException
	 */
	private void processInputParameterAttributes(InputParameterAttributes attrs) throws SemanticException {
		if (attrs == null)
			return;

		InputElementAttributes inputElementAttrs = attrs.getElementAttributes();
		processInputElementAttributes(inputElementAttrs);

		InputParameterUIHints paramUIHints = attrs.getUiHints();
		processInputParameterUIHints(paramUIHints);
	}

	/**
	 * 
	 * @param paramUiHints
	 * @throws SemanticException
	 */
	private void processInputParameterUIHints(InputParameterUIHints paramUiHints) throws SemanticException {
		if (paramUiHints == null)
			return;

		if (reportParam.getContainer() instanceof ParameterGroupHandle) {
			ParameterGroupHandle paramGroup = (ParameterGroupHandle) reportParam.getContainer();

			String groupPromptDisplayName = paramUiHints.getGroupPromptDisplayName();

			paramGroup.setDisplayName(groupPromptDisplayName);
			paramGroup.setDisplayNameKey(paramUiHints.getGroupPromptDisplayNameKey());
		}
	}

	/**
	 * 
	 * @param attrs
	 * @throws SemanticException
	 */
	private void processInputElementAttributes(InputElementAttributes attrs) throws SemanticException {
		if (attrs == null)
			return;

		// isRequired -- isOptional
		reportParam.setIsRequired(!attrs.isOptional());

		// update conceal value
		reportParam.setConcealValue(attrs.isMasksValue());

		// handle default values
		StaticValues values = attrs.getDefaultValues();
		processStaticValues(values);

		// handle selection choices
		ScalarValueChoices choices = attrs.getStaticValueChoices();
		processScalarValueChoices(choices);

		// update dynamic list
		DynamicValuesQuery valueQuery = attrs.getDynamicValueChoices();
		AdapterUtil.updateROMDyanmicList(valueQuery, null, reportParam, setHandle);

		// update value type
		String valueType = DesignChoiceConstants.PARAM_VALUE_TYPE_STATIC;
		if ((valueQuery != null && valueQuery.isEnabled()) || (reportParam.getContainer() != null
				&& reportParam.getContainer() instanceof CascadingParameterGroupHandle))
			valueType = DesignChoiceConstants.PARAM_VALUE_TYPE_DYNAMIC;

		reportParam.setValueType(valueType);

		InputElementUIHints inputElementUIHints = attrs.getUiHints();
		processInputElementUIHints(inputElementUIHints);
	}

	/**
	 * 
	 * @param inputElementUiHints
	 * @throws SemanticException
	 */
	private void processInputElementUIHints(InputElementUIHints inputElementUiHints) throws SemanticException {
		if (inputElementUiHints == null)
			return;

		// update auto suggest threshold
		reportParam.setAutoSuggestThreshold(inputElementUiHints.getAutoSuggestThreshold());

		// handle control type in prompt style
		InputPromptControlStyle style = inputElementUiHints.getPromptStyle();
		processInputPromptControlStyle(style);

	}

	/**
	 * 
	 * @param style
	 * @throws SemanticException
	 */
	private void processInputPromptControlStyle(InputPromptControlStyle style) throws SemanticException {
		reportParam.setControlType(style == null ? null : AdapterUtil.newROMControlType(style));
	}

	/**
	 * 
	 * @param values
	 * @throws SemanticException
	 */
	private void processStaticValues(StaticValues values) throws SemanticException {
		if (values == null)
			return;

		AdapterUtil.updateROMDefaultValues(values, reportParam);
	}

	/**
	 * 
	 * @param choices
	 * @throws SemanticException
	 */
	private void processScalarValueChoices(ScalarValueChoices choices) throws SemanticException {
		if (choices == null)
			return;

		AdapterUtil.updateROMSelectionList(choices, null, reportParam);
	}

}
