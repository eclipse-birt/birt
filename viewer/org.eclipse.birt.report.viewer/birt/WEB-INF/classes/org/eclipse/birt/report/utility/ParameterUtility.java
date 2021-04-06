/*************************************************************************************
 * Copyright (c) 2004-2008 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/
package org.eclipse.birt.report.utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.ScalarParameterBean;
import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.service.api.ParameterDefinition;
import org.eclipse.birt.report.service.api.ParameterSelectionChoice;
import org.eclipse.birt.report.soapengine.api.SelectItemChoice;

/**
 * Utility class for parameter handling
 */
public class ParameterUtility {
	/**
	 * Processes the given selection list, and adds the element to the given
	 * parameter bean's selection list.
	 * 
	 * @param selectionList  original selection list to be processed
	 * @param parameterBean  scalar parameter bean
	 * @param locale         locale (for data conversion)
	 * @param timeZone       time zone (for data conversion)
	 * @param processDefault update "selected" fields in parameterBean
	 * @return processed selection list
	 */
	public static List<ParameterSelectionChoice> makeSelectionList(Collection<ParameterSelectionChoice> selectionList,
			ScalarParameterBean parameterBean, Locale locale, TimeZone timeZone, boolean processDefault) {
		// TODO: refactor according to the code path which depends on processDefault
		boolean nullValueFound = false;
		List<ParameterSelectionChoice> processedList = parameterBean.getSelectionList();
		ParameterDefinition paramDef = parameterBean.getParameter();

		List<String> defaultValues = null;
		if (parameterBean.getDefaultValues() != null) {
			// make a copy, so the values can be removed one by one
			// after processing
			defaultValues = new ArrayList<String>(parameterBean.getDefaultValues());
		}

		parameterBean.setValueInList(false);
		if (selectionList != null) {
			boolean isDisplayTextInList = false;
			for (ParameterSelectionChoice selectionItem : selectionList) {
				if (selectionItem == null)
					continue;

				Object value = selectionItem.getValue();
				try {
					// try convert value to parameter definition data type
					value = DataUtil.convert(value, paramDef.getDataType());
				} catch (Exception e) {
					value = null;
				}

				// Convert parameter value using standard format
				String displayValue = DataUtil.getDisplayValue(value, timeZone);

				String label = selectionItem.getLabel();
				if (label == null || label.length() <= 0) {
					// If label is null or blank, then use the format parameter
					// value for display
					label = DataUtil.getDisplayValue(null, paramDef.getPattern(), value, locale, timeZone);
				}

				// if parameter is required
				if (paramDef.isRequired()) {
					// discard null values, and if the parameter is a string,
					// then also discard empty strings
					if (value == null || ("".equals(value) && //$NON-NLS-1$
							paramDef.getDataType() == IScalarParameterDefn.TYPE_STRING)) {
						continue;
					}
				}

				if (value == null) {
					nullValueFound = true;
					if (label == null) {
						label = IBirtConstants.NULL_VALUE_DISPLAY;
					}
				}

				// TODO: warning, replacing values in the same list!
				selectionItem.setLabel(label);
				selectionItem.setValue(displayValue);
				processedList.add(selectionItem);

				// TODO: below code not required for cascading params, move out?
				if (processDefault) {
					// If parameter value is in the selection list
					if (!paramDef.isMultiValue() && DataUtil.equals(displayValue, parameterBean.getValue())) {
						parameterBean.setValueInList(true);

						// check whether parameter display text is in the label list
						if (!DataUtil.equals(label, parameterBean.getDisplayText())) {
							if (parameterBean.getParameter().isDistinct() && parameterBean.isDisplayTextInReq()) {
								selectionItem.setLabel(parameterBean.getDisplayText());
								isDisplayTextInList = true;
							}
						} else {
							isDisplayTextInList = true;
						}
					}

					// Find out whether parameter default value is in the selection list

					// if is multivalue and the default value is an array
					if (paramDef.isMultiValue() && defaultValues != null) {
						if (DataUtil.contain((List<?>) defaultValues, displayValue, true)) {
							parameterBean.setDefaultValueInList(true);

							// remove current value from the defaultvalues list

							defaultValues.remove(displayValue);

						}
					}
					// if it is a single default value
					else if (DataUtil.equals(displayValue, parameterBean.getDefaultValue())) {
						parameterBean.setDefaultValueInList(true);

						// remove current value from the defaultvalues list
						// If the default values is one, the defaultValues is null
						if (defaultValues != null) {
							defaultValues.remove(displayValue);
						}
					}
				}
			}

			if (processDefault) {
				// add new item
				if (parameterBean.isValueInList() && parameterBean.isDisplayTextInReq() && !isDisplayTextInList) {
					processedList.add(
							new ParameterSelectionChoice(parameterBean.getDisplayText(), parameterBean.getValue()));
					isDisplayTextInList = true;
				}

				// handle multiple default values
				if (defaultValues != null && defaultValues.size() > 0) {
					for (int i = 0; i < defaultValues.size(); i++) {
						// add these default values which are not in the selectionList as string values,
						// for those values have already been evaluated and are of string type.
						processedList.add(i, new ParameterSelectionChoice(defaultValues.get(i), defaultValues.get(i)));
					}
				}

				parameterBean.setDisplayTextInList(isDisplayTextInList);
			}
		}

		if (!nullValueFound && !parameterBean.isRequired()) {
			// add null value if none exists in the list
			ParameterSelectionChoice selectionItem = new ParameterSelectionChoice(IBirtConstants.NULL_VALUE_DISPLAY,
					null);
			processedList.add(0, selectionItem);
		}
		return processedList;
	}

	/**
	 * Convert a list of ParameterSelectionChoice to a list of SelectItemChoice (for
	 * SOAP)
	 * 
	 * @param paramChoices list of ParameterSelectionChoice
	 * @return list of SelectItemChoice
	 */
	public static List<SelectItemChoice> toSelectItemChoice(List<ParameterSelectionChoice> paramChoices) {
		List<SelectItemChoice> soapChoices = new ArrayList<SelectItemChoice>();
		soapChoices.add(SelectItemChoice.EMPTY_VALUE);
		for (ParameterSelectionChoice element : paramChoices) {
			SelectItemChoice itemChoice = new SelectItemChoice((String) element.getValue(), element.getLabel());

			if (itemChoice.getValue() == null) {
				itemChoice.setValue(IBirtConstants.NULL_VALUE);
			}
			soapChoices.add(itemChoice);
		}
		return soapChoices;
	}

}
