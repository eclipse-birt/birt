/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.presentation.aggregation.layout;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.eclipse.birt.report.presentation.aggregation.BirtBaseFragment;

public class RequesterFragment extends BirtBaseFragment {
	/**
	 * Override implementation of doPostService.
	 */
	@Override
	protected String doPostService(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String className = getClass().getName().substring(getClass().getName().lastIndexOf('.') + 1);
		return JSPRootPath + "/pages/layout/" + className + ".jsp"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Override build method.
	 */
	@Override
	protected void build() {
		addChild(new ParameterFragment());
	}
}
