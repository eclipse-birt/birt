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
