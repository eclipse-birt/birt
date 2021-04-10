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

public interface IContext {
	/**
	 * Get contained bean.
	 * 
	 * @return Returns the bean.
	 */
	public BaseAttributeBean getBean();

	/**
	 * Access request instacne.
	 * 
	 * @return Returns the request.
	 */
	public HttpServletRequest getRequest();

	/**
	 * Access response instance.
	 * 
	 * @return Returns the request.
	 */
	public HttpServletResponse getResponse();
}