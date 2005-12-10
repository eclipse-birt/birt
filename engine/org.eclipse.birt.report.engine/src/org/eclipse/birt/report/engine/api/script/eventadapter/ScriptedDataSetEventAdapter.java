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
import org.eclipse.birt.report.engine.api.script.eventhandler.IScriptedDataSetEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IDataSetInstance;

/**
 * Default (empty) implementation of the IScriptedDataSetEventHandler interface
 */
public class ScriptedDataSetEventAdapter implements
		IScriptedDataSetEventHandler
{

	public void open( IDataSetInstance dataSet )
	{

	}

	public boolean fetch( IDataSetInstance dataSet, IDataSetRow row )
	{
		return false;
	}

	public void close( IDataSetInstance dataSet )
	{

	}

	public void beforeOpen( IDataSetInstance dataSet,
			IReportContext reportContext )
	{

	}

	public void afterOpen( IDataSetInstance dataSet,
			IReportContext reportContext )
	{

	}

	public void onFetch( IDataSetInstance dataSet, IDataSetRow row,
			IReportContext reportContext )
	{

	}

	public void beforeClose( IDataSetInstance dataSet,
			IReportContext reportContext )
	{

	}

	public void afterClose( IReportContext reportContext )
	{

	}

}
