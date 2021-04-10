/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
import org.eclipse.birt.report.engine.api.script.element.IAutoText;
import org.eclipse.birt.report.engine.api.script.eventhandler.IAutoTextEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IAutoTextInstance;

/**
 * Default (empty) implementation of the IAutoTextEventHandler interface
 */
public class AutoTextEventAdapter implements IAutoTextEventHandler {

	public void onCreate(IAutoTextInstance autoTextInstance, IReportContext reportContext) throws ScriptException {
	}

	public void onPageBreak(IAutoTextInstance autoTextInstance, IReportContext reportContext) throws ScriptException {
	}

	public void onPrepare(IAutoText autoText, IReportContext reportContext) throws ScriptException {
	}

	public void onRender(IAutoTextInstance autoTextInstance, IReportContext reportContext) throws ScriptException {
	}

}
