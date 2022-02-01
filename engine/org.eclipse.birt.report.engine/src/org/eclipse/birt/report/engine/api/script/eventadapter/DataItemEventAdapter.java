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
import org.eclipse.birt.report.engine.api.script.element.IDataItem;
import org.eclipse.birt.report.engine.api.script.eventhandler.IDataItemEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IDataItemInstance;

/**
 * Default (empty) implementation of the IDataItemEventHandler interface
 */
public class DataItemEventAdapter implements IDataItemEventHandler {

	public void onPrepare(IDataItem dataItemHandle, IReportContext reportContext) throws ScriptException {
	}

	public void onCreate(IDataItemInstance data, IReportContext reportContext) throws ScriptException {
	}

	public void onRender(IDataItemInstance data, IReportContext reportContext) throws ScriptException {
	}

	public void onPageBreak(IDataItemInstance data, IReportContext reportContext) throws ScriptException {
	}

}
