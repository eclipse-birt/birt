/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.data;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentWriter;
import org.eclipse.birt.report.engine.data.dte.DataGenerationEngine;
import org.eclipse.birt.report.engine.data.dte.DataInteractiveEngine;
import org.eclipse.birt.report.engine.data.dte.DataPresentationEngine;
import org.eclipse.birt.report.engine.data.dte.DteDataEngine;
import org.eclipse.birt.report.engine.executor.ExecutionContext;

/**
 * A factory class to create data engines. For now, only DtE data engine is
 * created in this factory.
 * 
 * @version $Revision: 1.11 $ $Date: 2005/12/05 09:01:12 $
 */
public class DataEngineFactory
{
	/**
	 * static factory instance
	 */
	static protected DataEngineFactory sm_instance;

	/**
	 * private constractor
	 */
	private DataEngineFactory( )
	{
	}

	/**
	 * get instance of factory
	 * 
	 * @return the factory instance
	 */
	synchronized public static DataEngineFactory getInstance( )
	{
		if ( sm_instance == null )
		{
			sm_instance = new DataEngineFactory( );
		}
		return sm_instance;
	}

	/**
	 * create a <code>DataEngine</code> given an execution context
	 * 
	 * @param context
	 *            the execution context
	 * @return a data engine instance
	 */
	public IDataEngine createDataEngine( ExecutionContext context )
	{
		//first we must test if we have the data source
		IDocArchiveReader dataSource = context.getDataSource( );
		if (dataSource != null)
		{
			return new DataInteractiveEngine( context, 
					dataSource );
		}
		//if get the report document writer is not null, that means we are in the g
		ReportDocumentWriter writer = context.getReportDocWriter();
		if (writer != null)
		{
			return new DataGenerationEngine( context, 
					context.getReportDocWriter().getArchive() );
		}
		
		IReportDocument document = context.getReportDocument( );
		if ( document != null )
		{
			return new DataPresentationEngine( context, 
					context.getReportDocument().getArchive() );
		}
		return new DteDataEngine( context );
	}
}
