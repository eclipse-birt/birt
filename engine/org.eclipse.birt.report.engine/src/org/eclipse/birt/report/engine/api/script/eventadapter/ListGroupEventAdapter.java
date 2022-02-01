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
import org.eclipse.birt.report.engine.api.script.element.IListGroup;
import org.eclipse.birt.report.engine.api.script.eventhandler.IListGroupEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IReportElementInstance;

public class ListGroupEventAdapter implements IListGroupEventHandler {

	public void onPrepare(IListGroup listGroup, IReportContext context) throws ScriptException {

	}

	public void onCreate(IReportElementInstance listGroup, IReportContext context) throws ScriptException {

	}

	public void onRender(IReportElementInstance listGroup, IReportContext context) throws ScriptException {

	}

	public void onPageBreak(IReportElementInstance listGroup, IReportContext context) throws ScriptException {

	}
}
