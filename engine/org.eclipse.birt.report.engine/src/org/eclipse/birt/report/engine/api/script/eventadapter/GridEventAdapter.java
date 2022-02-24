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
package org.eclipse.birt.report.engine.api.script.eventadapter;

import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.IGrid;
import org.eclipse.birt.report.engine.api.script.eventhandler.IGridEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IGridInstance;

/**
 * Default (empty) implementation of the IGridEventHandler interface
 */
public class GridEventAdapter implements IGridEventHandler {

	public void onPrepare(IGrid grid, IReportContext reportContext) throws ScriptException {

	}

	public void onCreate(IGridInstance grid, IReportContext reportContext) throws ScriptException {

	}

	public void onRender(IGridInstance grid, IReportContext reportContext) throws ScriptException {

	}

	public void onPageBreak(IGridInstance grid, IReportContext reportContext) throws ScriptException {

	}

}
