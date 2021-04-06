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
import org.eclipse.birt.report.engine.api.script.element.ITableGroup;
import org.eclipse.birt.report.engine.api.script.eventhandler.ITableGroupEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IReportElementInstance;

public class TableGroupEventAdapter implements ITableGroupEventHandler {

	public void onPrepare(ITableGroup tableGroup, IReportContext context) throws ScriptException {

	}

	public void onCreate(IReportElementInstance tableGroup, IReportContext context) throws ScriptException {

	}

	public void onRender(IReportElementInstance tableGroup, IReportContext context) throws ScriptException {

	}

	public void onPageBreak(IReportElementInstance tableGroup, IReportContext context) throws ScriptException {

	}

}
