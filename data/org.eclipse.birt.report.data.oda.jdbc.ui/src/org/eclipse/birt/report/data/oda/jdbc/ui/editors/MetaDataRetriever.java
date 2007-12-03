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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.data.oda.jdbc.ui.provider.OdaConnectionProvider;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

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
	
	private static Logger logger = Logger.getLogger( MetaDataRetriever.class.getName( ) );	

	
	/**
	 * 
	 * @param odaMetaDataProvider
	 * @param queryText
	 * @param dataSetType
	 */
	MetaDataRetriever( OdaConnectionProvider odaMetaDataProvider,
			String queryText, String dataSetType )
	{
		if ( odaMetaDataProvider != null )
		{
			try
			{
				IConnection con = odaMetaDataProvider.getConnection( );
				if ( con != null && con.isOpen( ) )
				{
					query = con.newQuery( dataSetType );
					try
					{
						query.prepare( queryText );
						this.paramMeta = query.getParameterMetaData( );
					}
					catch ( OdaException ex )
					{
						this.paramMeta = null;
					}
					this.resultMeta = query.getMetaData( );
				}
			}
			catch ( OdaException e )
			{
				this.resultMeta = null;
				logger.log( Level.INFO, e.getMessage( ), e );
				return;
			}
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
	 * Release IQuery object
	 */
	void close( )
	{
		if ( query != null )
		{
			try
			{
				query.close( );
				query = null;
			}
			catch ( OdaException e )
			{
				logger.log( Level.WARNING, e.getMessage( ), e );
			}
		}
	}
}
