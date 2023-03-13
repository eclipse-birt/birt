/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
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
