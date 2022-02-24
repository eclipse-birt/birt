/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.presentation.aggregation.parameter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.ScalarParameterBean;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.presentation.aggregation.BirtBaseFragment;
import org.eclipse.birt.report.service.api.IViewerReportDesignHandle;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.ParameterDefinition;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.utility.ParameterAccessor;
import org.eclipse.birt.report.utility.DataUtil;

/**
 * Fragment help rendering scalar parameter.
 * <p>
 * 
 * @see BaseFragment
 */
public class ScalarParameterFragment extends BirtBaseFragment {

	/**
	 * Reference to the real parameter definition.
	 */
	protected ParameterDefinition parameter = null;

	/**
	 * Protected constructor.
	 * 
	 * @param parameter parameter definition reference.
	 */
	protected ScalarParameterFragment(ParameterDefinition parameter) {
		this.parameter = parameter;
	}

	/**
	 * Get report parameters from engine.
	 * 
	 * @param request  incoming http request
	 * @param response http response
	 * @return target jsp pages
	 * @exception ServletException
	 * @exception IOException
	 */
	protected void doService(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ViewerAttributeBean attrBean = (ViewerAttributeBean) request.getAttribute(IBirtConstants.ATTRIBUTE_BEAN);
		assert attrBean != null;
		assert parameter != null;

		ScalarParameterBean parameterBean = new ScalarParameterBean(parameter);
		attrBean.setParameterBean(parameterBean);

		Locale locale = attrBean.getLocale();
		boolean isDesigner = attrBean.isDesigner();
		try {
			prepareParameterBean(attrBean.getReportDesignHandle(request), getReportService(), request, parameterBean,
					parameter, locale, isDesigner);
			// Prepare additional parameter properties.
			prepareParameterBean(request, getReportService(), parameterBean, locale, attrBean.getTimeZone());
		} catch (ReportServiceException e) {
			// TODO: What to do with exception?
			e.printStackTrace();
		}
	}

	/**
	 * Override implementation of doPostService.
	 */
	protected String doPostService(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String className = getClass().getName().substring(getClass().getName().lastIndexOf('.') + 1);
		return JSPRootPath + "/pages/parameter/" + className + ".jsp"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Prepare parameter bean
	 * 
	 * @param designHandle
	 * @param service
	 * @param request
	 * @param parameterBean
	 * @param parameter
	 * @param locale
	 * @param isDesigner
	 * @throws ReportServiceException
	 */
	public static void prepareParameterBean(IViewerReportDesignHandle designHandle, IViewerReportService service,
			HttpServletRequest request, ScalarParameterBean parameterBean, ParameterDefinition parameter, Locale locale,
			boolean isDesigner) throws ReportServiceException {
		// Display name
		String displayName = parameter.getPromptText();
		displayName = (displayName == null || displayName.length() <= 0) ? parameter.getDisplayName() : displayName;
		displayName = (displayName == null || displayName.length() <= 0) ? parameter.getName() : displayName;
		displayName = ParameterAccessor.htmlEncode(displayName);
		parameterBean.setDisplayName(displayName);

		// isRequired
		parameterBean.setRequired(parameter.isRequired());

		// Directly get parameter values from AttributeBean
		ViewerAttributeBean attrBean = (ViewerAttributeBean) request.getAttribute(IBirtConstants.ATTRIBUTE_BEAN);
		assert attrBean != null;

		// parameter value.
		if (attrBean.getParametersAsString() != null) {
			Object paramObj = attrBean.getParametersAsString().get(parameterBean.getName());
			if (paramObj instanceof List)
				parameterBean.setValueList((List) paramObj);
			else
				parameterBean.setValue((String) paramObj);
		}

		// Set parameter display text
		Map displayTexts = attrBean.getDisplayTexts();
		if (displayTexts.containsKey(parameterBean.getName())) {
			parameterBean.setDisplayTextInReq(true);
			parameterBean.setDisplayText(DataUtil.getString(displayTexts.get(parameterBean.getName())));
		}

		// Set parameter default value
		Map defaultValues = attrBean.getDefaultValues();
		Object defaultValue = defaultValues.get(parameter.getName());
		if (defaultValue != null) {
			if (defaultValue instanceof Object[]) {
				Object[] paramDefaultValues = (Object[]) defaultValue;
				List<String> convertedDefaultValues = new ArrayList<String>(paramDefaultValues.length);
				for (int i = 0; i < paramDefaultValues.length; i++) {
					Object value = paramDefaultValues[i];
					convertedDefaultValues.add(DataUtil.getDisplayValue(value, attrBean.getTimeZone()));
				}
				parameterBean.setDefaultValues(convertedDefaultValues);
			} else {
				parameterBean.setDefaultValue(DataUtil.getDisplayValue(defaultValue, attrBean.getTimeZone()));
			}
			parameterBean.setDefaultDisplayText(DataUtil.getDisplayValue(null, parameter.getPattern(), defaultValue,
					locale, attrBean.getTimeZone()));
		}

		// parameter map
		Map params = attrBean.getParameters();
		if (params != null && params.containsKey(parameter.getName())) {
			Object param = params.get(parameter.getName());
			if (param != null) {
				Object displayTextObj = null;
				if (param instanceof Object[]) {
					Object[] values = (Object[]) param;
					List paramList = new ArrayList();
					for (int i = 0; i < values.length; i++) {
						String value = DataUtil.getDisplayValue(values[i], attrBean.getTimeZone());
						paramList.add(value);
					}
					parameterBean.setValueList(paramList);
					if (values.length > 0)
						displayTextObj = values[0];
				} else {
					displayTextObj = param;
					String value = DataUtil.getDisplayValue(param, attrBean.getTimeZone());
					parameterBean.setValue(value);
				}

				// display text
				if (!displayTexts.containsKey(parameterBean.getName())) {
					parameterBean.setDisplayTextInReq(false);
					String displayText = DataUtil.getDisplayValue(null, parameter.getPattern(), displayTextObj, locale,
							attrBean.getTimeZone());
					parameterBean.setDisplayText(displayText);
				}
			}
		}
	}

	/**
	 * For implementation to extend parameter bean properties
	 * 
	 * @param request
	 * @param service
	 * @param parameterBean
	 * @param locale
	 * @param timeZone
	 * @throws ReportServiceException
	 */
	protected void prepareParameterBean(HttpServletRequest request, IViewerReportService service,
			ScalarParameterBean parameterBean, Locale locale, TimeZone timeZone) throws ReportServiceException {
	}
}
