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
import org.eclipse.birt.report.engine.api.script.element.ICell;
import org.eclipse.birt.report.engine.api.script.eventhandler.ICellEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.ICellInstance;

/**
 * Default (empty) implementation of the ICellEventHandler interface
 */
public class CellEventAdapter implements ICellEventHandler {

	public void onPrepare(ICell cell, IReportContext reportContext) throws ScriptException {
	}

	public void onCreate(ICellInstance cellInstance, IReportContext reportContext) throws ScriptException {
	}

	public void onRender(ICellInstance cellInstance, IReportContext reportContext) throws ScriptException {
	}

	public void onPageBreak(ICellInstance cellInstance, IReportContext reportContext) throws ScriptException {
	}

}
