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

import org.eclipse.birt.report.engine.api.script.IDataSetRow;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.eventhandler.IDataSetEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IDataSetInstance;

/**
 * Default (empty) implementation of the IDataSetEventHandler interface
 */
public class DataSetEventAdapter implements IDataSetEventHandler {

	public void beforeOpen(IDataSetInstance dataSet, IReportContext reportContext) throws ScriptException {

	}

	public void afterOpen(IDataSetInstance dataSet, IReportContext reportContext) throws ScriptException {

	}

	public void onFetch(IDataSetInstance dataSet, IDataSetRow row, IReportContext reportContext)
			throws ScriptException {

	}

	public void beforeClose(IDataSetInstance dataSet, IReportContext reportContext) throws ScriptException {

	}

	public void afterClose(IReportContext reportContext) throws ScriptException {

	}

}
