/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
