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
import java.util.Collection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.eclipse.birt.core.exception.BirtException;

/**
 * Web viewer Aggregation fragment interface.
 * <p>
 *
 * @see BaseFragment
 */
public interface IFragment {
	/**
	 * Get unique id of the corresponding UI gesture.
	 *
	 * @return id
	 */
	String getClientId();

	/**
	 * Get front end client name.
	 *
	 * @return id
	 */
	String getClientName();

	/**
	 * Gets the title ID for the html page.
	 *
	 * @return title id
	 */

	String getTitle();

	/**
	 * Is called to render the fragment. This may be a page, column or even a
	 * portlet.
	 *
	 * @param request  the servlet request
	 * @param response the servlet response
	 * @exception ServletException
	 * @exception IOException
	 */
	void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, BirtException;

	/**
	 * Call back funciton to invoke children fragments' service. It should be called
	 * only by jsp pages. Control flows back to classes.
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	void callBack(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, BirtException;

	/**
	 * Add child to this fragment.
	 *
	 * @param child child fragment to be added.
	 */
	void addChild(IFragment child);

	/**
	 * Returns a collection of all child fragments
	 *
	 * @return a collection containing objects implementing the interface fragment
	 */
	Collection getChildren();

	/**
	 * Building the web viewer composite.
	 */
	void buildComposite();

	/**
	 * Set JSP page root.
	 *
	 * @param rootPath
	 */
	void setJSPRootPath(String rootPath);

	/**
	 * Get jsp page root.
	 *
	 * @return
	 */
	String getJSPRootPath();
}
