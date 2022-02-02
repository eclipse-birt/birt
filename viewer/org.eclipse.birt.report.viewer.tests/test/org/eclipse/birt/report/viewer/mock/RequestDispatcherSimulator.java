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

package org.eclipse.birt.report.viewer.mock;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Mock a RequestDispatcher for Viewer UnitText
 * 
 */
public class RequestDispatcherSimulator implements RequestDispatcher {

	/**
	 * Forward Servlet
	 */
	private Servlet target;

	/**
	 * Constructor
	 * 
	 * @param target
	 */
	public RequestDispatcherSimulator(Servlet target) {
		this.target = target;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.RequestDispatcher#forward(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse)
	 */
	public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		if (target != null)
			target.service(request, response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.RequestDispatcher#include(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse)
	 */
	public void include(ServletRequest request, ServletResponse response) throws ServletException, IOException {
	}

	/**
	 * Return forward servlet name
	 * 
	 * @return
	 */
	public String getForward() {
		if (target == null)
			return null;

		return target.getClass().toString();
	}
}
