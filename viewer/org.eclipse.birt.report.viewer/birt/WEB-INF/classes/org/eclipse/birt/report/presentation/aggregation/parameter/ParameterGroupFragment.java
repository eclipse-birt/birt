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
import java.util.Collection;
import java.util.Iterator;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.ParameterGroupBean;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.presentation.aggregation.BirtBaseFragment;
import org.eclipse.birt.report.presentation.aggregation.IFragment;
import org.eclipse.birt.report.service.api.ParameterDefinition;
import org.eclipse.birt.report.service.api.ParameterGroupDefinition;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * Fragment help rendering parameter group.
 * <p>
 *
 * @see BaseFragment
 */
public class ParameterGroupFragment extends BirtBaseFragment {

	/**
	 * Reference to the real parameter group definition.
	 */
	protected ParameterGroupDefinition parameterGroup = null;

	/**
	 * Protected constructor.
	 *
	 * @param parameterGroup parameter group definition reference.
	 */
	public ParameterGroupFragment(ParameterGroupDefinition parameterGroup) {
		this.parameterGroup = parameterGroup;
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
	@Override
	protected void doService(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ViewerAttributeBean attrBean = (ViewerAttributeBean) request.getAttribute(IBirtConstants.ATTRIBUTE_BEAN);
		assert attrBean != null;

		assert parameterGroup != null;
		ParameterGroupBean parameterGroupBean = new ParameterGroupBean(parameterGroup);
		attrBean.setParameterBean(parameterGroupBean);

		// Display name.
		String displayName = parameterGroup.getPromptText();
		displayName = (displayName == null || displayName.length() <= 0) ? parameterGroup.getDisplayName()
				: displayName;

		displayName = ParameterAccessor.htmlEncode(displayName);
		parameterGroupBean.setDisplayName(displayName);

		// Parameters inside group.
		Collection fragments = new ArrayList();
		IFragment fragment = null;

		for (Iterator iter = parameterGroup.getParameters().iterator(); iter.hasNext();) {
			Object obj = iter.next();
			if (obj instanceof ParameterDefinition) {
				ParameterDefinition scalarParameter = (ParameterDefinition) obj;

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

				if (fragment != null) {
					fragment.setJSPRootPath(JSPRootPath);
					fragments.add(fragment);
				}
			}
		}

		request.setAttribute("fragments", fragments); //$NON-NLS-1$
	}

	/**
	 * Override implementation of doPostService.
	 */
	@Override
	protected String doPostService(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String className = getClass().getName().substring(getClass().getName().lastIndexOf('.') + 1);
		return JSPRootPath + "/pages/parameter/" + className + ".jsp"; //$NON-NLS-1$ //$NON-NLS-2$
	}
}
