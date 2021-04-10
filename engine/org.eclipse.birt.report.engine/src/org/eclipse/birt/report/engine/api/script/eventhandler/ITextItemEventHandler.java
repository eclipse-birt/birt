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

package org.eclipse.birt.report.engine.api.script.eventhandler;

import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.ITextItem;
import org.eclipse.birt.report.engine.api.script.instance.ITextItemInstance;

/**
 * Script event handler interface for a text item
 */
public interface ITextItemEventHandler {

	/**
	 * Handle the onPrepare event
	 */
	void onPrepare(ITextItem textItemHandle, IReportContext reportContext) throws ScriptException;

	/**
	 * Handle the onCreate event
	 */
	void onCreate(ITextItemInstance text, IReportContext reportContext) throws ScriptException;

	/**
	 * Handle the onRender event
	 */
	void onRender(ITextItemInstance text, IReportContext reportContext) throws ScriptException;

	/**
	 * Handle the onPageBreak event
	 */
	void onPageBreak(ITextItemInstance text, IReportContext reportContext) throws ScriptException;

}
