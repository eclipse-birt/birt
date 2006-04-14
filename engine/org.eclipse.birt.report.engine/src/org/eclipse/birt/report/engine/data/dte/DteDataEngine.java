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

package org.eclipse.birt.report.engine.data.dte;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.Report;
import org.mozilla.javascript.Scriptable;

/**
 * implments IDataEngine interface, using birt's data transformation engine
 * (DtE)
 * 
 * @version $Revision: 1.38 $ $Date: 2006/04/13 11:04:45 $
 */
public class DteDataEngine extends AbstractDataEngine
{

	/*
	 * need not be stored in report document.
	 */
	protected HashMap queryMap = new HashMap( );

	/**
	 * creates data engine, by first look into the directory specified by
	 * configuration variable odadriver, for oda configuration file. The oda
	 * configuration file is at $odadriver/drivers/driverType/odaconfig.xml.
	 * <p>
	 * 
	 * If the config variable is not set, search configuration file at
	 * ./drivers/driverType/odaconfig.xml.
	 * 
	 * @param context
	 */
	public DteDataEngine( ExecutionContext context )
	{
		super( context );

		try
		{
			// create the DteData engine.
			DataEngineContext dteContext = DataEngineContext.newInstance(
					DataEngineContext.DIRECT_PRESENTATION, context
							.getSharedScope( ), null, null );

			dteEngine = DataEngine.newDataEngine( dteContext );
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "can't create the DTE data engine", ex );
			ex.printStackTrace( );
		}
	}

	protected void doPrepareQuery( Report report, Map appContext )
	{
		// prepare report queries
		List queries = report.getQueries( );
		for ( int i = 0; i < queries.size( ); i++ )
		{
			IQueryDefinition query = (IQueryDefinition) queries.get( i );
			try
			{
				IPreparedQuery preparedQuery = dteEngine.prepare( query,
						appContext );
				queryMap.put( query, preparedQuery );
			}
			catch ( BirtException e )
			{
				logger.log( Level.SEVERE, e.getMessage( ), e );
				context.addException( e );
			}
		} // end of prepare
	}

	protected IResultSet doExecuteQuery( IBaseQueryDefinition query )
	{
		assert query instanceof IQueryDefinition;

		IPreparedQuery pQuery = (IPreparedQuery) queryMap.get( query );
		if ( pQuery == null )
		{
			return null;
		}

		try
		{
			Scriptable scope = context.getSharedScope( );

			DteResultSet resultSet = (DteResultSet) getResultSet( );
			IQueryResults dteResults; // the dteResults of this query
			if ( resultSet == null )
			{
				// this is the root query
				dteResults = pQuery.execute( scope );
			}
			else
			{
				// this is the nest query, execute the query in the
				// parent results
				dteResults = pQuery.execute( resultSet.getQueryResults( ),
						scope );
			}

			resultSet = new DteResultSet( dteResults.getID(),
					dteResults.getResultIterator( ), this, context );
			rsets.addFirst( resultSet );

			return resultSet;
		}
		catch ( BirtException be )
		{
			logger.log( Level.SEVERE, be.getMessage( ) );
			context.addException( be );
		}

		return null;

	}
}