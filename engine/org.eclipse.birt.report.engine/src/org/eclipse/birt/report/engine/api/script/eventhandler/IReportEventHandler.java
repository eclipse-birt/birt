/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api.script.eventhandler;

import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.IReportDesign;
import org.eclipse.birt.report.engine.api.script.instance.IPageInstance;

/**
 * Script event handler interface for a report
 */
public interface IReportEventHandler {
	/**
	 * Handle the initialize event
	 */
	void initialize(IReportContext reportContext) throws ScriptException;

	/**
	 * Handle the beforeFactory event
	 */
	void beforeFactory(IReportDesign report, IReportContext reportContext) throws ScriptException;

	/**
	 * Handle the afterFactory event
	 */
	void afterFactory(IReportContext reportContext) throws ScriptException;

	/**
	 * Handle the beforeRender event
	 */
	void beforeRender(IReportContext reportContext) throws ScriptException;

	/**
	 * Handle the afterRender event
	 */
	void afterRender(IReportContext reportContext) throws ScriptException;

	/**
	 * Handle the onPrepare event
	 */
	void onPrepare(IReportContext reportContext) throws ScriptException;

	/**
	 *
	 * Handle the onPageStart event
	 */
	void onPageStart(IPageInstance page, IReportContext reportContext) throws ScriptException;

	/**
	 *
	 * Handle the onPageEnd event
	 */
	void onPageEnd(IPageInstance page, IReportContext reportContext) throws ScriptException;

}
