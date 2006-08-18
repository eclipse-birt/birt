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

import java.sql.Types;
import java.util.logging.Level;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * ResultSetMetaData contains the result set metadata retrieved during 
 * runtime.
 */
public class ResultSetMetaData
{
	private IResultSetMetaData m_metadata;
	private String m_driverName;
	private String m_dataSetType;

	// trace logging variables
	private static final String sm_className = ResultSetMetaData.class.getName();
	private static final String sm_loggerName = ConnectionManager.sm_packageName;
	private static LogHelper sm_logger = LogHelper.getInstance( sm_loggerName );

	ResultSetMetaData( IResultSetMetaData metadata, String driverName,
					   String dataSetType )
	{
		final String methodName = "ResultSetMetaData";
		if( sm_logger.isLoggingEnterExitLevel() )
		    sm_logger.entering( sm_className, methodName, 
            		new Object[] { metadata, driverName, dataSetType } );

		m_metadata = metadata;
		m_driverName = driverName;
		m_dataSetType = dataSetType;

	    sm_logger.exiting( sm_className, methodName, this );
	}
	
	/**
	 * Returns the number of columns in the corresponding result set.
	 * @return	the number of columns in the result set.
	 * @throws DataException	if data source error occurs.
	 */
	public int getColumnCount( ) throws DataException
	{
	    final String methodName = "getColumnCount";
		try
		{
		    if( m_metadata == null )
		        return 0;
			return m_metadata.getColumnCount( );
		}
		catch( OdaException ex )
		{
		    sm_logger.logp( Level.SEVERE, sm_className, methodName,
            				"Cannot get column count.", ex );
			throw new DataException( ResourceConstants.CANNOT_GET_COLUMN_COUNT, ex );
		}
		catch( UnsupportedOperationException ex )
		{
		    sm_logger.logp( Level.SEVERE, sm_className, methodName,
            				"Cannot get column count.", ex );
			throw new DataException( ResourceConstants.CANNOT_GET_COLUMN_COUNT, ex );
		}
	}
	
	/**
	 * Returns the column name at the specified column index.
	 * @param index	the column index.
	 * @return		the column name at the specified column index.
	 * @throws DataException	if data source error occurs.
	 */
	public String getColumnName( int index ) throws DataException
	{
	    final String methodName = "getColumnName";

	    verifyHasRuntimeMetaData();
		try
		{
			return m_metadata.getColumnName( index );
		}
		catch( OdaException ex )
		{
		    sm_logger.logp( Level.SEVERE, sm_className, methodName,
            				"Cannot get column name.", ex );
			throw new DataException( ResourceConstants.CANNOT_GET_COLUMN_NAME, ex, 
			                         new Object[] { new Integer( index ) } );
		}
		catch( UnsupportedOperationException ex )
		{
		    sm_logger.logp( Level.WARNING, sm_className, methodName,
    						"Cannot get column name.", ex );
		    return "";
		}
	}
	
	/**
	 * Returns the column label at the specified column index.
	 * @param index	the column index.
	 * @return		the column label at the specified column index.
	 * @throws DataException	if data source error occurs.
	 */
	public String getColumnLabel( int index ) throws DataException
	{
	    final String methodName = "getColumnLabel";

	    verifyHasRuntimeMetaData();
		try
		{
			return m_metadata.getColumnLabel( index );
		}
		catch( OdaException ex )
		{
		    sm_logger.logp( Level.SEVERE, sm_className, methodName,
    						"Cannot get column label.", ex );
			throw new DataException( ResourceConstants.CANNOT_GET_COLUMN_LABEL, ex, 
			                         new Object[] { new Integer( index ) } );
		}
		catch( UnsupportedOperationException ex )
		{
		    sm_logger.logp( Level.INFO, sm_className, methodName,
    						"Cannot get column label.", ex );
		    return "";
		}
	}
	
	/**
	 * Returns the ODA type at the specified column index.
	 * @param index	the column index.
	 * @return		the ODA type, in <code>java.sql.Types</code> value, 
	 * 				at the specified column index; or Types.NULL
     *              if runtime data type is unknown
	 * @throws DataException	if data source error occurs.
	 */
	public int getColumnType( int index ) throws DataException
	{
	    final String methodName = "getColumnType";
	    
		int nativeType = doGetNativeColumnType( index );

        // if the native type of the column is unknown (Types.NULL) at runtime, 
        // we can't simply default to the ODA character type because we may 
        // have a design hint that could provide the type
        int odaType = ( nativeType == Types.NULL ) ?
                Types.NULL :
 		        DataTypeUtil.toOdaType( nativeType, m_driverName, m_dataSetType );

		if( sm_logger.isLoggable( Level.FINEST ) )
		    sm_logger.logp( Level.FINEST, sm_className, methodName, 
		            		"Column at index {0} has ODA data type {1}.",
		            		new Object[] { new Integer( index ), new Integer( odaType ) } );

		return odaType;
	}
	
	/**
	 * Returns the native type name at the specified column index.
	 * @param index	the column index.
	 * @return		the native type name.
	 * @throws DataException	if data source error occurs.
	 */
	public String getColumnNativeTypeName( int index ) throws DataException
	{
	    final String methodName = "getColumnNativeTypeName";

	    verifyHasRuntimeMetaData();
		try
		{
			return m_metadata.getColumnTypeName( index );
		}
		catch( OdaException ex )
		{
		    sm_logger.logp( Level.SEVERE, sm_className, methodName,
							"Cannot get column native type name.", ex );
			throw new DataException( ResourceConstants.CANNOT_GET_COLUMN_NATIVE_TYPE_NAME, ex, 
									 new Object[] { new Integer( index ) } );
		}
		catch( UnsupportedOperationException ex )
		{
		    sm_logger.logp( Level.WARNING, sm_className, methodName,
							"Cannot get column native type name.", ex );
		    return "";
		}
	}

	private int doGetNativeColumnType( int index ) throws DataException
	{
	    final String methodName = "doGetNativeColumnType";

	    verifyHasRuntimeMetaData();
		try
		{
			return m_metadata.getColumnType( index );
		}
		catch( OdaException ex )
		{
		    sm_logger.logp( Level.SEVERE, sm_className, methodName,
							"Cannot get column native type code.", ex );
			throw new DataException( ResourceConstants.CANNOT_GET_COLUMN_TYPE, ex, 
			                         new Object[] { new Integer( index ) } );
		}
		catch( UnsupportedOperationException ex )
		{
		    sm_logger.logp( Level.SEVERE, sm_className, methodName,
							"Cannot get column native type code.", ex );
			throw new DataException( ResourceConstants.CANNOT_GET_COLUMN_TYPE, ex, 
			                         new Object[] { new Integer( index ) } );			
		}
	}
	
	Class getColumnTypeAsJavaClass( int index ) throws DataException
	{
		int odaType = getColumnType( index );
		return DataTypeUtil.toTypeClass( odaType );
	}
	
	private void verifyHasRuntimeMetaData() throws DataException
	{
	    if( m_metadata == null )
	        throw new DataException( ResourceConstants.CANNOT_GET_RESULTSET_METADATA );
	}
    
    String getOdaDataSourceId()
    {
        return m_driverName;
    }
    
    String getDataSetType()
    {
        return m_dataSetType;
    }
    
}
