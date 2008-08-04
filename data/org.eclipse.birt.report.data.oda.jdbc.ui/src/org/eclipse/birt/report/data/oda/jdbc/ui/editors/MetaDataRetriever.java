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
package org.eclipse.birt.report.data.oda.jdbc.ui.editors;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.data.oda.jdbc.OdaJdbcDriver;
import org.eclipse.datatools.connectivity.oda.IAdvancedQuery;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDriver;
import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;

/**
 * 
 * This class serves to provide the updated ParameterMetaData and ResultSetMetaData information
 * according to the specified updated query text
 *  
 */
class MetaDataRetriever
{
	private IResultSetMetaData resultMeta;
	private IParameterMetaData paramMeta;
	private IQuery query;
	private IConnection connection;
	
	private static Logger logger = Logger.getLogger( MetaDataRetriever.class.getName( ) );	

	MetaDataRetriever( DataSetDesign dataSetDesign )
	{
		DataSourceDesign dataSourceDesign = dataSetDesign.getDataSourceDesign( );
		IDriver jdbcDriver = new OdaJdbcDriver( );
		try
		{
			connection = jdbcDriver.getConnection( dataSourceDesign.getOdaExtensionId( ) );
			Properties prop = DesignSessionUtil.getEffectiveDataSourceProperties( dataSourceDesign );
			connection.open( prop );
			
			query = connection.newQuery( dataSetDesign.getOdaExtensionDataSetId( ) );
			query.prepare( dataSetDesign.getQueryText( ) );
			paramMeta = query.getParameterMetaData( );
			if ( query instanceof IAdvancedQuery )
			{
				resultMeta = query.getMetaData( );
			}
			
		}
		catch ( OdaException e )
		{
			logger.log( Level.WARNING, e.getLocalizedMessage( ), e );
		}

	}

	
	/**
	 * Get the ParameterMetaData object
	 * 
	 * @return IParameterMetaData
	 */
	IParameterMetaData getParameterMetaData( )
	{
		return this.paramMeta;
	}
	
	/**
	 * Get the ResultSetMetaData object
	 * 
	 * @return IResultSetMetaData
	 */
	IResultSetMetaData getResultSetMetaData( )
	{
		return this.resultMeta;
	}
	
	/**
	 * Release
	 */
	void close( )
	{
		try
		{
			if ( query != null )
			{
				query.close( );
			}
			if ( connection != null )
			{
				connection.close( );
			}
		}
		catch ( OdaException e )
		{
			//ignore it
		}
	}
}
