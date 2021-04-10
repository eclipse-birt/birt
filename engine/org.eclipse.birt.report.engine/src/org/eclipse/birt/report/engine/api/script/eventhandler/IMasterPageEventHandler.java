/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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
import org.eclipse.birt.report.engine.api.script.instance.IPageInstance;

public interface IMasterPageEventHandler {
	/**
	 *
	 * Handle the onPageStart event
	 */
	void onPageStart(IPageInstance page, IReportContext reportContext) throws ScriptException;

	/**
	 *
	 * Handle the onPageEnd event
	 */
	void onPageEnd(IPageInstance page, IReportContext reportContext) throws ScriptException;

}
