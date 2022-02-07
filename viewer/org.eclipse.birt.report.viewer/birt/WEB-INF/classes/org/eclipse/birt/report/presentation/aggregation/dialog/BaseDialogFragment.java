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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.birt.report.presentation.aggregation.BirtBaseFragment;

/**
 * Fragment for report tool bar.
 * <p>
 * 
 * @see BaseFragment
 */
public class BaseDialogFragment extends BirtBaseFragment {
	/**
	 * Base class implementation of post service process.
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected String doPostService(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String className = getClass().getName().substring(getClass().getName().lastIndexOf('.') + 1);
		return JSPRootPath + "/pages/dialog/" + className + ".jsp"; //$NON-NLS-1$ //$NON-NLS-2$
	}
}
