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
import org.eclipse.birt.report.engine.api.script.element.IRow;
import org.eclipse.birt.report.engine.api.script.eventhandler.IRowEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IRowInstance;

/**
 * Default (empty) implementation of the ITableDetailRowEventHandler interface
 */
public class RowEventAdapter implements IRowEventHandler {

	public void onPrepare(IRow rowHandle, IReportContext reportContext) throws ScriptException {

	}

	public void onCreate(IRowInstance rowInstance, IReportContext reportContext) throws ScriptException {

	}

	public void onRender(IRowInstance rowInstance, IReportContext reportContext) throws ScriptException {

	}

	public void onPageBreak(IRowInstance rowInstance, IReportContext reportContext) throws ScriptException {

	}
}
