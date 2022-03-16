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

import org.eclipse.birt.report.engine.api.script.IDataSetRow;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.eventhandler.IDataSetEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IDataSetInstance;

/**
 * Default (empty) implementation of the IDataSetEventHandler interface
 */
public class DataSetEventAdapter implements IDataSetEventHandler {

	@Override
	public void beforeOpen(IDataSetInstance dataSet, IReportContext reportContext) throws ScriptException {

	}

	@Override
	public void afterOpen(IDataSetInstance dataSet, IReportContext reportContext) throws ScriptException {

	}

	@Override
	public void onFetch(IDataSetInstance dataSet, IDataSetRow row, IReportContext reportContext)
			throws ScriptException {

	}

	@Override
	public void beforeClose(IDataSetInstance dataSet, IReportContext reportContext) throws ScriptException {

	}

	@Override
	public void afterClose(IReportContext reportContext) throws ScriptException {

	}

}
