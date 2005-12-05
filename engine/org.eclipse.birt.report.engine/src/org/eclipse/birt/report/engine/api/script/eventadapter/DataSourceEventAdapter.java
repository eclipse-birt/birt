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
import org.eclipse.birt.report.engine.api.script.eventhandler.IDataSourceEventHandler;
import org.eclipse.birt.report.engine.script.element.DataSource;

/**
 * Default (empty) implementation of the IDataSourceEventHandler interface
 */
public class DataSourceEventAdapter implements IDataSourceEventHandler
{

	public void beforeOpen( DataSource dataSource, IReportContext reportContext )
	{

	}

	public void afterOpen( IReportContext reportContext )
	{

	}

	public void beforeClose( IReportContext reportContext )
	{

	}

	public void afterClose( IReportContext reportContext )
	{

	}

}
