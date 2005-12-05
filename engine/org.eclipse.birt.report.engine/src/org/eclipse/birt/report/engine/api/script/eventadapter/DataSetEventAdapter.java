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
import org.eclipse.birt.report.engine.api.script.IRowData;
import org.eclipse.birt.report.engine.api.script.eventhandler.IDataSetEventHandler;
import org.eclipse.birt.report.engine.script.element.DataSet;

/**
 * Default (empty) implementation of the IDataSetEventHandler interface
 */
public class DataSetEventAdapter implements IDataSetEventHandler
{

	public void beforeOpen( DataSet dataSet, IReportContext reportContext )
	{
	}

	public void afterOpen( IReportContext reportContext )
	{
	}

	public void onFetch( IRowData expressionResults,
			IReportContext reportContext )
	{
	}

	public void beforeClose( IReportContext reportContext )
	{
	}

	public void afterClose( IReportContext reportContext )
	{
	}

}
