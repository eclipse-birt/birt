/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
