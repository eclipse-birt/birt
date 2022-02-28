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
import org.eclipse.birt.report.engine.api.script.element.ILabel;
import org.eclipse.birt.report.engine.api.script.eventhandler.ILabelEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.ILabelInstance;

/**
 * Default (empty) implementation of the ILabelEventHandler interface
 */
public class LabelEventAdapter implements ILabelEventHandler {

	@Override
	public void onPrepare(ILabel labelHandle, IReportContext reportContext) throws ScriptException {

	}

	@Override
	public void onCreate(ILabelInstance label, IReportContext reportContext) throws ScriptException {

	}

	@Override
	public void onRender(ILabelInstance label, IReportContext reportContext) throws ScriptException {

	}

	@Override
	public void onPageBreak(ILabelInstance label, IReportContext reportContext) throws ScriptException {

	}

}
