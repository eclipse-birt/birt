/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.impl;

import java.io.File;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.IncreDataSetCacheObject;
import org.eclipse.birt.data.engine.executor.cache.CacheUtil;
import org.eclipse.birt.data.engine.odi.IQuery;

/**
 * 
 */

public class PreparedIncreCacheDSQuery extends PreparedOdaDSQuery
		implements
			IPreparedQuery
{

	PreparedIncreCacheDSQuery( DataEngineImpl dataEngine,
			IQueryDefinition queryDefn, IBaseDataSetDesign dataSetDesign,
			Map appContext ) throws DataException
	{
		super( dataEngine, queryDefn, dataSetDesign, appContext );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.impl.PreparedOdaDSQuery#newExecutor()
	 */
	protected QueryExecutor newExecutor( )
	{

		String tmpdir = dataEngine.getContext( ).getTmpdir( );
		IIncreCacheDataSetDesign icDataSetDesign = (IIncreCacheDataSetDesign) dataSetDesign;
		String cacheDir = CacheUtil.createIncrementalTempDir( tmpdir,
				icDataSetDesign.getConfigFileUrl( ).toString( ),
				icDataSetDesign.getName( ) );
		logger.log( Level.INFO, "Create incremental cache directory: " + cacheDir );
		getDataSetCacheManager( ).registerCacheDir( icDataSetDesign, cacheDir );
		return new IncreCacheDSQueryExecutor( cacheDir );
	}

	/**
	 * 
	 */
	public class IncreCacheDSQueryExecutor extends OdaDSQueryExecutor
	{

		private String cacheDir;

		public IncreCacheDSQueryExecutor( String cacheDir )
		{
			this.cacheDir = cacheDir;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.impl.PreparedOdaDSQuery.OdaDSQueryExecutor#createOdiQuery()
		 */
		protected IQuery createOdiQuery( ) throws DataException
		{
			OdaDataSetRuntime extDataSet = (OdaDataSetRuntime) dataSet;
			assert extDataSet != null;
			assert odiDataSource != null;
			IIncreCacheDataSetDesign icDataSetDesign = (IIncreCacheDataSetDesign) dataSetDesign;
			String queryText = null;
			try
			{
				long lasttime = CacheUtil.getLastTimestamp( cacheDir );
				queryText = icDataSetDesign.getQueryForUpdate( lasttime );
			}
			catch ( DataException e )
			{
				File dataFile = new File( cacheDir,
						IncreDataSetCacheObject.DATA_DATA );
				if ( dataFile.exists( ) )
				{
					dataFile.delete( );
					logger.log( Level.WARNING,
							"Incremental cache data file was deleted! path: "
									+ dataFile.getAbsolutePath( ) );
				}
				queryText = icDataSetDesign.getQueryText( );
			}
			String dataSetType = extDataSet.getExtensionID( );
			logger.log( Level.INFO, "Execute SQL: " + queryText );
			odiQuery = odiDataSource.newQuery( dataSetType, queryText );
			return odiQuery;
		}
	}
}
