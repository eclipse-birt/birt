/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.viewer.utilities;

/**
 * IWebAppInfo
 */
public interface IWebAppInfo {
	/**
	 * the plugin ID
	 * 
	 * @return
	 */

	String getID();

	/**
	 * the web app name
	 * 
	 * @return
	 */
	String getName();

	/**
	 * the web app context path
	 * 
	 * @return
	 */
	String getWebAppContextPath();

	/**
	 * the web app folder
	 * 
	 * @return
	 */
	String getWebAppPath();

	boolean useCustomParamHandling();

	/**
	 * the request URL encoding
	 * 
	 * @return encoding for request URL
	 */
	String getURIEncoding();
}
