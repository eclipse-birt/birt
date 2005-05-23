/*
 *****************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *
 ******************************************************************************
 */ 

package org.eclipse.birt.data.engine.odaconsumer;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.oda.IDataSetMetaData;
import org.eclipse.birt.data.oda.OdaException;

/**
 * Information about the data set capabilities.
 */
public class DataSetCapabilities
{
	private IDataSetMetaData m_dsMetaData;

	// trace logging variables
	private static String sm_className = DataSetCapabilities.class.getName();
	private static String sm_loggerName = ConnectionManager.sm_packageName;
	private static Logger sm_logger = Logger.getLogger( sm_loggerName );
	
	DataSetCapabilities( IDataSetMetaData dsMetaData )
	{
		String methodName = "DataSetCapabilities";		
		sm_logger.entering( sm_className, methodName, dsMetaData );
		
		assert( dsMetaData != null );
		m_dsMetaData = dsMetaData;

		sm_logger.exiting( sm_className, methodName, this );
}
	
	/**
	 * Returns the mode supported by this data set for dynamic sorting functionality.
	 * @return	the dynamic sorting mode supported by the data set.
	 */
	public int getSortMode( )
	{
		return m_dsMetaData.getSortMode( );
	}
	
	/**
	 * Returns whether a <code>Statement</code> can simultaneously get multiple 
	 * result sets.
	 * @return	true if a <code>Statement</code> can get multiple result sets 
	 * 			simultaneously; false otherwise.
	 * @throws DataException	if data source error occurs.
	 */
	public boolean supportsMultipleOpenResults( ) throws DataException
	{
		String methodName = "supportsMultipleOpenResults";		

		try
		{
			return m_dsMetaData.supportsMultipleOpenResults( );
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
					"Cannot determine support of multiple open results.", ex );
			throw new DataException( ResourceConstants.CANNOT_DETERMINE_SUPPORT_FOR_MULTIPLE_OPEN_RESULTS, ex );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.INFO, sm_className, methodName, 
					"Cannot determine support of multiple open results.  Default to false.", ex );
			return false;
		}
	}
	
	/**
	 * Returns whether a <code>Statement</code> can support getting multiple result sets.
	 * @return	true if a <code>Statement</code> can support getting multiple result sets; 
	 * 			false otherwise.
	 * @throws DataException	if data source error occurs.
	 */
	public boolean supportsMultipleResultSets( ) throws DataException
	{
		String methodName = "supportsMultipleResultSets";		
		try
		{
			return m_dsMetaData.supportsMultipleResultSets( );
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
					"Cannot determine support of multiple results.", ex );
			throw new DataException( ResourceConstants.CANNOT_DETERMINE_SUPPORT_FOR_MULTIPLE_RESULT_SETS, ex );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.INFO, sm_className, methodName, 
					"Cannot determine support of multiple results.  Default to false.", ex );
			return false;
		}
	}
	
	/**
	 * Returns whether a <code>Statement</code> can support named result sets.
	 * @return	true if a <code>Statement</code> can support named result sets;
	 * 			false otherwise.
	 * @throws DataException	if data source error occurs.
	 */
	public boolean supportsNamedResultSets( ) throws DataException
	{
		String methodName = "supportsNamedResultSets";		
		try
		{
			return m_dsMetaData.supportsNamedResultSets( );
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
					"Cannot determine support of named results.", ex );
			throw new DataException( ResourceConstants.CANNOT_DETERMINE_SUPPORT_FOR_NAMED_RESULT_SETS, ex );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.INFO, sm_className, methodName, 
					"Cannot determine support of named results.  Default to false.", ex );
			return false;
		}
	}
	
	/**
	 * Returns whether a <code>Statement</code> can support named parameters.
	 * @return	true if a <code>Statement</code> can support named parameters; 
	 * 			false otherwise.
	 * @throws DataException	if data source error occurs.
	 */
	public boolean supportsNamedParameters( ) throws DataException
	{
		String methodName = "supportsNamedParameters";		
		try
		{
			return m_dsMetaData.supportsNamedParameters( );
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
					"Cannot determine support of named parameters.", ex );
			throw new DataException( ResourceConstants.CANNOT_DETERMINE_SUPPORT_FOR_NAMED_PARAMETERS, ex );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.INFO, sm_className, methodName, 
					"Cannot determine support of named parameters. Default to false.", ex );
			return false;
		}
	}
	
	/**
	 * Returns whether a <code>Statement</code> can support input parameters.
	 * @return	true if a <code>Statement</code> can support input parameters;
	 * 			false otherwise.
	 * @throws DataException	if data source error occurs.
	 */
	public boolean supportsInParameters( ) throws DataException
	{
		String methodName = "supportsInParameters";		
		try
		{
			return m_dsMetaData.supportsInParameters( );
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
					"Cannot determine support of input parameters.", ex );
			throw new DataException( ResourceConstants.CANNOT_DETERMINE_SUPPORT_FOR_IN_PARAMETERS, ex );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.INFO, sm_className, methodName, 
					"Cannot determine support of input parameters. Default to false.", ex );
			return false;
		}
	}
	
	/**
	 * Returns whether a <code>Statement</code> can support output parameters.
	 * @return	true if a <code>Statement</code> can support output parameters;
	 * 			false otherwise.
	 * @throws DataException	if data source error occurs.
	 */
	public boolean supportsOutParameters( ) throws DataException
	{
		String methodName = "supportsOutParameters";		
		try
		{
			return m_dsMetaData.supportsOutParameters( );
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
					"Cannot determine support of output parameters.", ex );
			throw new DataException( ResourceConstants.CANNOT_DETERMINE_SUPPORT_FOR_OUT_PARAMETERS, ex );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.INFO, sm_className, methodName, 
					"Cannot determine support of output parameters. Default to false.", ex );
			return false;
		}
	}
}
