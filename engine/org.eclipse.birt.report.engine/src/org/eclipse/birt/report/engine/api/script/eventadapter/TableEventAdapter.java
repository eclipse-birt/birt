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
import org.eclipse.birt.report.engine.api.script.element.ITable;
import org.eclipse.birt.report.engine.api.script.eventhandler.ITableEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.ITableInstance;

/**
 * Default (empty) implementation of the ITableEventHandler interface
 */
public class TableEventAdapter implements ITableEventHandler {

	public void onPrepare(ITable tableHandle, IReportContext reportContext) throws ScriptException {

	}

	public void onCreate(ITableInstance table, IReportContext reportContext) throws ScriptException {

	}

	public void onRender(ITableInstance table, IReportContext reportContext) throws ScriptException {

	}

	public void onPageBreak(ITableInstance table, IReportContext reportContext) throws ScriptException {

	}

}
