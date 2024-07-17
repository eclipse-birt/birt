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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

abstract public class BaseContext implements IContext {
	/**
	 * Thread local.
	 */
	protected static ThreadLocal contextTraker = new ThreadLocal();

	/**
	 * Reference to the viewer attribute bean.
	 */
	protected BaseAttributeBean bean = null;

	/**
	 * Reference to the current request.
	 */
	protected HttpServletRequest request = null;

	/**
	 * Reference to the current response.
	 */
	protected HttpServletResponse response = null;

	/**
	 * Abstract methods.
	 */
	abstract protected void __init();

	/**
	 * Static accessor.
	 *
	 * @return
	 */
	public static IContext getInstance() {
		return ((IContext) contextTraker.get());
	}

	/**
	 * Default constructor.
	 */
	public BaseContext() {
	}

	/**
	 * Constructor.
	 *
	 * @param request
	 * @param response
	 */
	public BaseContext(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		__init();
		contextTraker.set(this);
	}

	/**
	 * @return Returns the bean.
	 */
	@Override
	public BaseAttributeBean getBean() {
		return bean;
	}

	/**
	 * Access request instance.
	 *
	 * @return Returns the request.
	 */
	@Override
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * Access response instance.
	 *
	 * @return
	 */
	@Override
	public HttpServletResponse getResponse() {
		return response;
	}
}
