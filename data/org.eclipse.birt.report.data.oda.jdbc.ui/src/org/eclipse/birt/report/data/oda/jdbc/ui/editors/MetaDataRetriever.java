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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.data.oda.jdbc.ParameterMetaData;
import org.eclipse.birt.report.data.oda.jdbc.ResultSetMetaData;
import org.eclipse.birt.report.data.oda.jdbc.ui.provider.IMetaDataProvider;
import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
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
	private IMetaDataProvider metaDataProvider;
	private IParameterMetaData paramMeta;
	private IResultSetMetaData resultMeta;
	private PreparedStatement statement;
	private ResultSet resultset;
	
	private static Logger logger = Logger.getLogger( MetaDataRetriever.class.getName( ) );	

	/**
	 * Constructor of MetaDataRetriever
	 * 
	 * @param metaDataProvider
	 * @param queryText
	 * @throws OdaException
	 */
	MetaDataRetriever( IMetaDataProvider metaDataProvider, String queryText )
	{
		this.metaDataProvider = metaDataProvider;
		if( this.metaDataProvider!= null )
		{
			try
			{
				statement = this.metaDataProvider.getConnection( )
						.prepareStatement( queryText );
			}
			catch ( SQLException e )
			{
				logger.log( Level.INFO, e.getMessage( ), e );
				return;
			}

			if ( statement != null )
			{
				try
				{
					this.paramMeta = new ParameterMetaData( statement.getParameterMetaData( ) );
				}
				catch ( SQLException e )
				{
					// second try
					try
					{
						resultset = statement.executeQuery( );
					}
					catch ( SQLException e2 )
					{
						logger.log( Level.INFO, e2.getMessage( ), e2 );
						return;
					}
					try
					{
						this.paramMeta = new ParameterMetaData( statement.getParameterMetaData( ) );
					}
					catch ( OdaException e2 )
					{
						// impossible
					}
					catch ( SQLException e2 )
					{
						logger.log( Level.INFO, e2.getMessage( ), e2 );
						this.paramMeta = null;
					}

					try
					{
						this.resultMeta = new ResultSetMetaData( resultset.getMetaData( ) );
					}
					catch ( SQLException e1 )
					{
						logger.log( Level.INFO, e1.getMessage( ), e1 );
						return;
					}
					catch ( OdaException e3 )
					{
						// impossible
					}
				}
				catch ( OdaException e )
				{
					//impossible
				}
				if ( this.resultMeta == null )
				{
					try
					{
						resultset = statement.executeQuery( );
					}
					catch ( SQLException e1 )
					{
						logger.log( Level.INFO, e1.getMessage( ), e1 );
						return;
					}
					
					try
					{
						this.resultMeta = new ResultSetMetaData( resultset.getMetaData( ) );
					}
					catch ( SQLException e )
					{
						logger.log( Level.INFO, e.getMessage( ), e );
						return;
					}
					catch ( OdaException e )
					{
						//impossible
					}
				}
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
	 * Release all the connection resources
	 * 
	 */
	void close( )
	{
		if( this.resultset!= null )
		{
			try
			{
				this.resultset.close( );
			}
			catch ( SQLException e )
			{
				logger.log( Level.FINER, e.getMessage( ), e );
				this.resultset = null;
			}
		}
		if( this.statement!= null )
		{
			try
			{
				this.statement.close( );
			}
			catch ( SQLException e )
			{
				logger.log( Level.FINER, e.getMessage( ), e );
				this.statement = null;
			}
		}
	}
	
}
