/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api;

import org.eclipse.birt.report.engine.api.script.IReportContext;

/**
 * Defines an interface for action handler used in HTML format
 */
public interface IHTMLActionHandler {

	/**
	 * Generates a URL based on the action definition.
	 * 
	 * @param actionDefn definition of an action
	 * @param context    the context for creating the hyper link
	 * @return the URL based on an action
	 */
	public String getURL(IAction actionDefn, Object context);

	/**
	 * Generates a URL based on the action definition.
	 * 
	 * @param actionDefn definition of an action
	 * @param context    the context for creatino the hyper link
	 * @return the URL based on an action
	 */
	public String getURL(IAction actionDefn, IReportContext context);
}
