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
import org.eclipse.birt.report.engine.api.script.instance.IDataSourceInstance;

/**
 * Script event handler interface for a data source
 */
public interface IDataSourceEventHandler {

	/**
	 * Handle the beforeOpen event
	 */
	void beforeOpen(IDataSourceInstance dataSource, IReportContext reportContext) throws ScriptException;

	/**
	 * Handle the afterOpen event
	 */
	void afterOpen(IDataSourceInstance dataSource, IReportContext reportContext) throws ScriptException;

	/**
	 * Handle the beforeClose event
	 */
	void beforeClose(IDataSourceInstance dataSource, IReportContext reportContext) throws ScriptException;

	/**
	 * Handle the afterClose event
	 */
	void afterClose(IReportContext reportContext) throws ScriptException;

}
