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
import java.util.TimeZone;

import jakarta.servlet.http.HttpServletRequest;

import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.ScalarParameterBean;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.ParameterDefinition;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.utility.ParameterUtility;

/**
 * Fragment help rendering scalar parameter.
 * <p>
 *
 * @see org.eclipse.birt.report.presentation.aggregation.BaseFragment
 */
public class RadioButtonParameterFragment extends ScalarParameterFragment {

	/**
	 * Protected constructor.
	 *
	 * @param parameter parameter definition reference.
	 */
	public RadioButtonParameterFragment(ParameterDefinition parameter) {
		super(parameter);
	}

	@Override
	protected void prepareParameterBean(HttpServletRequest request, IViewerReportService service,
			ScalarParameterBean parameterBean, Locale locale, TimeZone timeZone) throws ReportServiceException {
		ViewerAttributeBean attrBean = (ViewerAttributeBean) request.getAttribute(IBirtConstants.ATTRIBUTE_BEAN);
		assert attrBean != null;

		InputOptions options = new InputOptions();
		options.setOption(InputOptions.OPT_REQUEST, request);
		options.setOption(InputOptions.OPT_LOCALE, attrBean.getLocale());
		options.setOption(InputOptions.OPT_TIMEZONE, attrBean.getTimeZone());

		Collection selectionList = service.getParameterSelectionList(attrBean.getReportDesignHandle(request), options,
				parameterBean.getName());

		ParameterUtility.makeSelectionList(selectionList, parameterBean, locale, timeZone, true);
	}
}
