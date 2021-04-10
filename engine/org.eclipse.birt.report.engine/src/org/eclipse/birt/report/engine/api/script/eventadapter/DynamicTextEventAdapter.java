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
import org.eclipse.birt.report.engine.api.script.element.IDynamicText;
import org.eclipse.birt.report.engine.api.script.eventhandler.IDynamicTextEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IDynamicTextInstance;

/**
 * Default (empty) implementation of the IDynamicTextEventHandler interface
 */
public class DynamicTextEventAdapter implements IDynamicTextEventHandler {

	public void onPrepare(IDynamicText textData, IReportContext reportContext) throws ScriptException {

	}

	public void onCreate(IDynamicTextInstance text, IReportContext reportContext) throws ScriptException {

	}

	public void onRender(IDynamicTextInstance text, IReportContext reportContext) throws ScriptException {

	}

	public void onPageBreak(IDynamicTextInstance text, IReportContext reportContext) throws ScriptException {

	}

}
