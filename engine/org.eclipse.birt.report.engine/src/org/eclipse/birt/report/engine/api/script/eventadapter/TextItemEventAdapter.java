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
import org.eclipse.birt.report.engine.api.script.element.ITextItem;
import org.eclipse.birt.report.engine.api.script.eventhandler.ITextItemEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.ITextItemInstance;

/**
 * Default (empty) implementation of the ITextItemEventHandler interface
 */
public class TextItemEventAdapter implements ITextItemEventHandler {

	public void onPrepare(ITextItem textItemHandle, IReportContext reportContext) throws ScriptException {

	}

	public void onCreate(ITextItemInstance text, IReportContext reportContext) throws ScriptException {

	}

	public void onRender(ITextItemInstance text, IReportContext reportContext) throws ScriptException {

	}

	public void onPageBreak(ITextItemInstance text, IReportContext reportContext) throws ScriptException {

	}

}
