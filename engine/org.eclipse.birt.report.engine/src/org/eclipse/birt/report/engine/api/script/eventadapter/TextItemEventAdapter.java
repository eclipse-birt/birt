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
