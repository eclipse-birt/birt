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
import org.eclipse.birt.report.engine.api.script.element.ITableGroup;
import org.eclipse.birt.report.engine.api.script.eventhandler.ITableGroupEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IReportElementInstance;

public class TableGroupEventAdapter implements ITableGroupEventHandler {

	@Override
	public void onPrepare(ITableGroup tableGroup, IReportContext context) throws ScriptException {

	}

	@Override
	public void onCreate(IReportElementInstance tableGroup, IReportContext context) throws ScriptException {

	}

	@Override
	public void onRender(IReportElementInstance tableGroup, IReportContext context) throws ScriptException {

	}

	@Override
	public void onPageBreak(IReportElementInstance tableGroup, IReportContext context) throws ScriptException {

	}

}
