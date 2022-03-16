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

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.ScalarParameterBean;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.service.api.IViewerReportDesignHandle;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.ParameterDefinition;
import org.eclipse.birt.report.service.api.ParameterGroupDefinition;
import org.eclipse.birt.report.service.api.ParameterSelectionChoice;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.utility.ParameterUtility;

/**
 * Fragment help rendering scalar parameter.
 * <p>
 *
 * @see org.eclipse.birt.report.presentation.aggregation.BaseFragment
 */
public class ComboBoxParameterFragment extends ScalarParameterFragment {

	/**
	 * Protected constructor.
	 *
	 * @param parameter parameter definition reference.
	 */
	public ComboBoxParameterFragment(ParameterDefinition parameter) {
		super(parameter);
	}

	/**
	 * Override implementation of prepareParameterBean method
	 *
	 * @see org.eclipse.birt.report.presentation.aggregation.parameter.ScalarParameterFragment#prepareParameterBean(javax.servlet.http.HttpServletRequest,
	 *      org.eclipse.birt.report.service.api.IViewerReportService,
	 *      org.eclipse.birt.report.context.ScalarParameterBean, java.util.Locale,
	 *      Timezone)
	 */
	@Override
	protected void prepareParameterBean(HttpServletRequest request, IViewerReportService service,
			ScalarParameterBean parameterBean, Locale locale, TimeZone timeZone) throws ReportServiceException {
		ViewerAttributeBean attrBean = (ViewerAttributeBean) request.getAttribute(IBirtConstants.ATTRIBUTE_BEAN);
		assert attrBean != null;

		InputOptions options = new InputOptions();
		options.setOption(InputOptions.OPT_REQUEST, request);
		options.setOption(InputOptions.OPT_LOCALE, attrBean.getLocale());
		options.setOption(InputOptions.OPT_TIMEZONE, attrBean.getTimeZone());

		Collection<ParameterSelectionChoice> selectionList = null;
		ParameterDefinition paramDef = parameterBean.getParameter();
		if (paramDef.getGroup() != null && paramDef.getGroup().cascade()) {
			// get parameter list from cascading group
			Map paramValues = attrBean.getParameters();
			selectionList = getParameterSelectionListForCascadingGroup(attrBean.getReportDesignHandle(request), service,
					paramValues, options);

			// Set cascade flag as true
			parameterBean.setCascade(true);
		} else {
			// get parameter list
			selectionList = service.getParameterSelectionList(attrBean.getReportDesignHandle(request), options,
					parameter.getName());

			// Set cascade flag as false
			parameterBean.setCascade(false);
		}

		ParameterUtility.makeSelectionList(selectionList, parameterBean, locale, timeZone, true);
	}

	private Collection getParameterSelectionListForCascadingGroup(IViewerReportDesignHandle design,
			IViewerReportService service, Map paramValues, InputOptions options) throws ReportServiceException {

		ParameterGroupDefinition group = (ParameterGroupDefinition) parameter.getGroup();
		int index = group.getParameters().indexOf(parameter);
		Object[] groupKeys = new Object[index];
		for (int i = 0; i < index; i++) {
			ParameterDefinition def = (ParameterDefinition) group.getParameters().get(i);
			groupKeys[i] = paramValues.get(def.getName());
		}
		return service.getSelectionListForCascadingGroup(design, group.getName(), groupKeys, options);
	}
}
