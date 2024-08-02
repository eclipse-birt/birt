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

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

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
	 * @see jakarta.servlet.RequestDispatcher#forward(jakarta.servlet.ServletRequest,
	 * jakarta.servlet.ServletResponse)
	 */
	@Override
	public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		if (target != null) {
			target.service(request, response);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.RequestDispatcher#include(jakarta.servlet.ServletRequest,
	 * jakarta.servlet.ServletResponse)
	 */
	@Override
	public void include(ServletRequest request, ServletResponse response) throws ServletException, IOException {
	}

	/**
	 * Return forward servlet name
	 *
	 * @return
	 */
	public String getForward() {
		if (target == null) {
			return null;
		}

		return target.getClass().toString();
	}
}
