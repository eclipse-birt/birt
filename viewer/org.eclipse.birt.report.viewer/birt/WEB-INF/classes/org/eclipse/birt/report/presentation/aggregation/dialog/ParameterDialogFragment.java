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

package org.eclipse.birt.report.presentation.aggregation.dialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.BaseAttributeBean;
import org.eclipse.birt.report.presentation.aggregation.IFragment;
import org.eclipse.birt.report.presentation.aggregation.parameter.CheckboxParameterFragment;
import org.eclipse.birt.report.presentation.aggregation.parameter.ComboBoxParameterFragment;
import org.eclipse.birt.report.presentation.aggregation.parameter.HiddenParameterFragment;
import org.eclipse.birt.report.presentation.aggregation.parameter.ParameterGroupFragment;
import org.eclipse.birt.report.presentation.aggregation.parameter.RadioButtonParameterFragment;
import org.eclipse.birt.report.presentation.aggregation.parameter.TextBoxParameterFragment;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.ParameterDefinition;
import org.eclipse.birt.report.service.api.ParameterGroupDefinition;
import org.eclipse.birt.report.service.api.ReportServiceException;

/**
 * Fragment help rendering parameter page in side bar.
 * <p>
 * 
 * @see BaseFragment
 */
public class ParameterDialogFragment extends BaseDialogFragment {

	/**
	 * Get unique id of the corresponding UI gesture.
	 * 
	 * @return id
	 */
	public String getClientId() {
		return "parameterDialog"; //$NON-NLS-1$
	}

	/**
	 * Get name of the corresponding UI gesture.
	 * 
	 * @return id
	 */
	public String getClientName() {
		return "Parameter"; //$NON-NLS-1$
	}

	/**
	 * Gets the title ID for the html page.
	 * 
	 * @return title id
	 */

	public String getTitle() {
		return BirtResources.getMessage(ResourceConstants.PARAMETER_DIALOG_TITLE);
	}

	protected void doService(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Collection fragments = new ArrayList();
		IViewerReportService service = getReportService();
		Collection parameters = null;

		BaseAttributeBean attrBean = (BaseAttributeBean) request.getAttribute(IBirtConstants.ATTRIBUTE_BEAN);
		assert attrBean != null;

		InputOptions options = new InputOptions();
		options.setOption(InputOptions.OPT_REQUEST, request);
		options.setOption(InputOptions.OPT_LOCALE, attrBean.getLocale());
		options.setOption(InputOptions.OPT_TIMEZONE, attrBean.getTimeZone());

		try {
			parameters = service.getParameterDefinitions(attrBean.getReportDesignHandle(request), options, true);
		} catch (ReportServiceException e) {
			// TODO What to do here???
			e.printStackTrace();
		}

		if (parameters != null) {
			Iterator iParameters = parameters.iterator();
			while (iParameters != null && iParameters.hasNext()) {
				Object parameter = iParameters.next();
				if (parameter == null) {
					continue;
				}

				IFragment fragment = null;
				if (parameter instanceof ParameterGroupDefinition) {
					fragment = new ParameterGroupFragment((ParameterGroupDefinition) parameter);
				} else if (parameter instanceof ParameterDefinition) {
					ParameterDefinition scalarParameter = (ParameterDefinition) parameter;

					if (!scalarParameter.isHidden()) {
						switch (scalarParameter.getControlType()) {
						case ParameterDefinition.TEXT_BOX: {
							fragment = new TextBoxParameterFragment(scalarParameter);
							break;
						}
						case ParameterDefinition.LIST_BOX: {
							fragment = new ComboBoxParameterFragment(scalarParameter);
							break;
						}
						case ParameterDefinition.RADIO_BUTTON: {
							fragment = new RadioButtonParameterFragment(scalarParameter);
							break;
						}
						case ParameterDefinition.CHECK_BOX: {
							fragment = new CheckboxParameterFragment(scalarParameter);
							break;
						}
						}
					} else {
						// handle hidden parameter
						fragment = new HiddenParameterFragment(scalarParameter);
					}
				}

				if (fragment != null) {
					fragment.setJSPRootPath(JSPRootPath);
					fragments.add(fragment);
				}
			}
		}

		request.setAttribute("fragments", fragments); //$NON-NLS-1$
	}
}
