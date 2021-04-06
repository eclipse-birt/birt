/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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