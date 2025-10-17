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

package org.eclipse.birt.report.presentation.aggregation;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.api.IViewerReportService;

/**
 * Base implementation of fragment interface.
 * <p>
 * Birt viewer uses composite of fragments to control the layout. The composite
 * structure is shown below: <i>
 * <ul type=i>
 * <li>Root Fragment</li>
 * <ul type=i>
 * <li>Navigation Fragment</li>
 * <ul type=i>
 * <li>Navigation Root Fragment</li>
 * <ul type=i>
 * <li>Navigation Toolbar Fragment</li>
 * <li>Navigation Content Fragment</li>
 * <ul type=i>
 * <li>Parameter Fragment</li>
 * <ul type=i>
 * <li>Parameter Group Fragment</li>
 * <ul type=i>
 * <li>Scalar Parameter Fragment</li>
 * <ul type=i>
 * <li>TextBox Parameter Fragment</li>
 * <li>ComboBox Parameter Fragment</li>
 * <li>RadioButton Parameter Fragment</li>
 * <li>CheckBox Parameter Fragment</li>
 * </ul>
 * </ul>
 * </ul>
 * </ul>
 * </ul>
 * </ul>
 * <li>Report Fragment</li>
 * <ul type=i>
 * <li>Report Toolbar Fragment</li>
 * <ul type=i>
 * <li>Report Toolbar Root Fragment</li>
 * <ul type=i>
 * <li>Report Content Fragment</li>
 * <li>Engine Fragment</li>
 * </ul>
 * </ul>
 * </ul>
 * </ul>
 * </ul>
 * </i>
 *
 * <P>
 * All fragments except parameter-related fragments (fragments inside "Parameter
 * Fragment") has only one instance. They represents the basic Birt viewer
 * layout. They are initialized in servlet's initialization phase and shared by
 * all servlet requests.
 *
 * <P>
 * Those parameter-related fragments are created dynamically for each individual
 * servlet request. Their composite structure is determined by report design.
 *
 * <P>
 * Each framgent serves as a back-end or "code behand" class that encapsulates
 * all the logic that is necessary for renderring the interface. A jsp page with
 * the same name will serve as the front-end interface. You can have multiple
 * front-end jsp pages as you want to achieve various UI effects. All these jsp
 * page sets share the same back-end fragments.
 *
 */
public class BirtBaseFragment extends AbstractBaseFragment {
	/**
	 * Reference to root fragment
	 */
	protected static IFragment framesetFragment = null;

	/**
	 * Reference to the engine fragment
	 */
	protected static IFragment engineFragment = null;

	/**
	 * Reference to the parameter fragment
	 */
	protected static IFragment parameterFragment = null;

	/**
	 * Base class implementation of post service process.
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected String doPostService(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-store"); //$NON-NLS-1$//$NON-NLS-2$
		response.setHeader("Pragma", "no-cache"); //$NON-NLS-1$//$NON-NLS-2$
		response.setDateHeader("Expires", 0); //$NON-NLS-1$

		String className = getClass().getName().substring(getClass().getName().lastIndexOf('.') + 1);
		return JSPRootPath + "/pages/layout/" + className + ".jsp"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Get report service instance.
	 */
	@Override
	protected IViewerReportService getReportService() {
		return BirtReportServiceFactory.getReportService();
	}
}
