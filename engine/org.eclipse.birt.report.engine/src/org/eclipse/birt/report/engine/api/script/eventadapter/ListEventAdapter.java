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
import org.eclipse.birt.report.engine.api.script.element.IList;
import org.eclipse.birt.report.engine.api.script.eventhandler.IListEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IListInstance;

/**
 * Default (empty) implementation of the IListEventHandler interface
 */
public class ListEventAdapter implements IListEventHandler {

	@Override
	public void onPrepare(IList listHandle, IReportContext reportContext) throws ScriptException {

	}

	@Override
	public void onCreate(IListInstance list, IReportContext reportContext) throws ScriptException {

	}

	@Override
	public void onRender(IListInstance list, IReportContext reportContext) throws ScriptException {

	}

	@Override
	public void onPageBreak(IListInstance list, IReportContext reportContext) throws ScriptException {

	}

}
