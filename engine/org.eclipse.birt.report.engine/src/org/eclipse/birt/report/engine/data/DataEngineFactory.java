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

import org.eclipse.birt.core.archive.IDocumentArchive;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.data.dte.DataGenerationEngine;
import org.eclipse.birt.report.engine.data.dte.DataPresentationEngine;
import org.eclipse.birt.report.engine.data.dte.DteDataEngine;
import org.eclipse.birt.report.engine.executor.ExecutionContext;

/**
 * A factory class to create data engines. For now, only DtE data engine is
 * created in this factory.
 * 
 * @version $Revision: 1.8 $ $Date: 2005/11/22 19:25:37 $
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
		IReportDocument document = context.getReportDocument( );
		if ( document != null )
		{
			IDocumentArchive archive = document.getArchive( );
			if ( context.isInFactory( ) )
			{
				return new DataGenerationEngine( context, archive );
			}
			return new DataPresentationEngine( context, archive );
		}
		return new DteDataEngine( context );
	}
}
