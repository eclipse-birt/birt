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
import org.eclipse.birt.report.engine.api.script.element.IGrid;
import org.eclipse.birt.report.engine.api.script.instance.IGridInstance;

/**
 * Script event handler interface for a grid
 */
public interface IGridEventHandler {
	/**
	 * Handle the onPrepare event
	 */
	void onPrepare(IGrid grid, IReportContext reportContext) throws ScriptException;

	/**
	 * Handle the onCreate event
	 */
	void onCreate(IGridInstance grid, IReportContext reportContext) throws ScriptException;

	/**
	 * Handle the onRender event
	 */
	void onRender(IGridInstance grid, IReportContext reportContext) throws ScriptException;

	/**
	 * Handle the onPageBreak event
	 */
	void onPageBreak(IGridInstance grid, IReportContext reportContext) throws ScriptException;

}
