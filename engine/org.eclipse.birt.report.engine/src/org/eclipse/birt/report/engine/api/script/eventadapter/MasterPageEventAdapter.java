/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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
import org.eclipse.birt.report.engine.api.script.eventhandler.IMasterPageEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IPageInstance;

public class MasterPageEventAdapter implements IMasterPageEventHandler {

	@Override
	public void onPageStart(IPageInstance page, IReportContext reportContext) throws ScriptException {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageEnd(IPageInstance page, IReportContext reportContext) throws ScriptException {
		// TODO Auto-generated method stub

	}

}
