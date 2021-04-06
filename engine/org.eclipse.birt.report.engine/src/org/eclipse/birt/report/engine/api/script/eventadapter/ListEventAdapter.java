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
import org.eclipse.birt.report.engine.api.script.element.IList;
import org.eclipse.birt.report.engine.api.script.eventhandler.IListEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IListInstance;

/**
 * Default (empty) implementation of the IListEventHandler interface
 */
public class ListEventAdapter implements IListEventHandler {

	public void onPrepare(IList listHandle, IReportContext reportContext) throws ScriptException {

	}

	public void onCreate(IListInstance list, IReportContext reportContext) throws ScriptException {

	}

	public void onRender(IListInstance list, IReportContext reportContext) throws ScriptException {

	}

	public void onPageBreak(IListInstance list, IReportContext reportContext) throws ScriptException {

	}

}
