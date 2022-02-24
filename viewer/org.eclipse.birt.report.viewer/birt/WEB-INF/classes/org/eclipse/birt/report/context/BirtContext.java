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

package org.eclipse.birt.report.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.birt.report.IBirtConstants;

public class BirtContext extends BaseContext {

	/**
	 * Constructor.
	 * 
	 * @param request
	 * @param response
	 */
	public BirtContext(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	/**
	 * Local init.
	 * 
	 * @return
	 */
	protected void __init() {
		this.bean = (ViewerAttributeBean) request.getAttribute(IBirtConstants.ATTRIBUTE_BEAN);
		if (bean == null) {
			bean = new ViewerAttributeBean(request);
		}
		request.setAttribute(IBirtConstants.ATTRIBUTE_BEAN, bean);
	}
}
