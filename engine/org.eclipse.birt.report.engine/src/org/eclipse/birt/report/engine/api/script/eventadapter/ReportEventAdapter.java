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
import org.eclipse.birt.report.engine.api.script.element.IReportDesign;
import org.eclipse.birt.report.engine.api.script.eventhandler.IReportEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IPageInstance;

/**
 * Default (empty) implementation of the IReportEventHandler interface
 */
public class ReportEventAdapter implements IReportEventHandler {

	public void initialize(IReportContext reportContext) throws ScriptException {

	}

	public void beforeFactory(IReportDesign report, IReportContext reportContext) throws ScriptException {

	}

	public void afterFactory(IReportContext reportContext) throws ScriptException {

	}

	public void beforeRender(IReportContext reportContext) throws ScriptException {

	}

	public void afterRender(IReportContext reportContext) throws ScriptException {

	}

	public void onPrepare(IReportContext reportContext) throws ScriptException {

	}

	public void onPageStart(IPageInstance page, IReportContext reportContext) throws ScriptException {

	}

	public void onPageEnd(IPageInstance page, IReportContext reportContext) throws ScriptException {

	}

}
