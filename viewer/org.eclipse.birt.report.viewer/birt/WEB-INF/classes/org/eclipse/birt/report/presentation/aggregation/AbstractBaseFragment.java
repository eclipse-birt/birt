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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.service.api.IViewerReportService;

/**
 * Abstract base implementation of fragment interface.
 */
abstract public class AbstractBaseFragment implements IFragment {
	/**
	 * Root path for JSP pages.
	 */
	protected String JSPRootPath = null;

	/**
	 * Fragment's children.
	 */
	protected ArrayList children = new ArrayList();

	/**
	 * Base class implementation of post service process.
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	abstract protected String doPostService(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException;

	/**
	 * Get report service instance.
	 * 
	 * @return
	 */
	abstract protected IViewerReportService getReportService();

	/**
	 * Service provided by the fragment. This is the entry point of each framgent.
	 * It generally includes a JSP page to render a certain part of web viewer.
	 * 
	 * @param request  incoming http request
	 * @param response http response
	 * @exception ServletException
	 * @exception IOException
	 */
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, BirtException {
		doPreService(request, response);
		doService(request, response);
		String target = doPostService(request, response);

		if (target != null && target.length() > 0) {
			RequestDispatcher rd = request.getRequestDispatcher(target);
			rd.include(request, response);
		}
	}

	/**
	 * Call back funciton to invoke children fragments' service. It should be called
	 * only by jsp pages,
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void callBack(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, BirtException {
		if (children != null) {
			for (int i = 0; i < children.size(); i++) {
				IFragment fragment = (IFragment) children.get(i);

				if (fragment != null) {
					fragment.service(request, response);
				}
			}

		}
	}

	/**
	 * Base pre service implementation.
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws BirtException
	 */
	protected void doPreService(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8"); //$NON-NLS-1$
	}

	/**
	 * Base class implementation. This is the method performs fragment (code behand
	 * class)'s major logic.
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doService(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, BirtException {
		request.setAttribute("fragment", this); //$NON-NLS-1$
	}

	/**
	 * Get unique id of the corresponding UI gesture.
	 * 
	 * @return id
	 */
	public String getClientId() {
		return null;
	}

	/**
	 * Get front end client name.
	 */
	public String getClientName() {
		return null;
	}

	/**
	 * Gets the title ID for the html page.
	 * 
	 * @return title id
	 */

	public String getTitle() {
		return null;
	}

	/**
	 * Get children fragments.
	 * 
	 * @return collection of children
	 */
	public Collection getChildren() {
		return children;
	}

	/**
	 * Add child to the children list.
	 * 
	 * @param child child fragment
	 */
	public void addChild(IFragment child) {
		children.add(child);
	}

	/**
	 * Build web viewer composite.
	 * 
	 * @return
	 */
	public void buildComposite() {
		build();
		if (children != null) {
			Iterator i = children.iterator();
			while (i.hasNext()) {
				((IFragment) i.next()).buildComposite();
			}
		}
	}

	/**
	 * Default build implementation. Needs to be override.
	 * 
	 * @return
	 */
	protected void build() {
	}

	/**
	 * Propagate root path.
	 */
	public void setJSPRootPath(String rootPath) {
		JSPRootPath = rootPath;
		if (children != null) {
			Iterator i = children.iterator();
			while (i.hasNext()) {
				((IFragment) i.next()).setJSPRootPath(rootPath);
			}
		}
	}

	/**
	 * Accessor.
	 * 
	 * @return
	 */
	public String getJSPRootPath() {
		return JSPRootPath;
	}
}
