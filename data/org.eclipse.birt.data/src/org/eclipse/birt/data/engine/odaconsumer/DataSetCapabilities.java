/*******************************************************************************
* Copyright (c) 2004, 2005 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/ 

package org.eclipse.birt.data.engine.odaconsumer;

import org.eclipse.birt.data.oda.IDataSetMetaData;
import org.eclipse.birt.data.oda.OdaException;

/**
 * Information about the data set capabilities.
 */
public class DataSetCapabilities
{
	private IDataSetMetaData m_dsMetaData;
	
	DataSetCapabilities( IDataSetMetaData dsMetaData )
	{
		assert( dsMetaData != null );
		m_dsMetaData = dsMetaData;
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
	 * @throws OdaException	if data source error occurs.
	 */
	public boolean supportsMultipleOpenResults( ) throws OdaException
	{
		return m_dsMetaData.supportsMultipleOpenResults( );
	}
	
	/**
	 * Returns whether a <code>Statement</code> can support getting multiple result sets.
	 * @return	true if a <code>Statement</code> can support getting multiple result sets; 
	 * 			false otherwise.
	 * @throws OdaException	if data source error occurs.
	 */
	public boolean supportsMultipleResultSets( ) throws OdaException
	{
		return m_dsMetaData.supportsMultipleResultSets( );
	}
	
	/**
	 * Returns whether a <code>Statement</code> can support named result sets.
	 * @return	true if a <code>Statement</code> can support named result sets;
	 * 			false otherwise.
	 * @throws OdaException	if data source error occurs.
	 */
	public boolean supportsNamedResultSets( ) throws OdaException
	{
		return m_dsMetaData.supportsNamedResultSets( );
	}
	
	/**
	 * Returns whether a <code>Statement</code> can support named parameters.
	 * @return	true if a <code>Statement</code> can support named parameters; 
	 * 			false otherwise.
	 * @throws OdaException	if data source error occurs.
	 */
	public boolean supportsNamedParameters( ) throws OdaException
	{
		return m_dsMetaData.supportsNamedParameters( );
	}
	
	/**
	 * Returns whether a <code>Statement</code> can support input parameters.
	 * @return	true if a <code>Statement</code> can support input parameters;
	 * 			false otherwise.
	 * @throws OdaException	if data source error occurs.
	 */
	public boolean supportsInParameters( ) throws OdaException
	{
		return m_dsMetaData.supportsInParameters( );
	}
	
	/**
	 * Returns whether a <code>Statement</code> can support output parameters.
	 * @return	true if a <code>Statement</code> can support output parameters;
	 * 			false otherwise.
	 * @throws OdaException	if data source error occurs.
	 */
	public boolean supportsOutParameters( ) throws OdaException
	{
		return m_dsMetaData.supportsOutParameters( );
	}
}
