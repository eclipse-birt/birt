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

package org.eclipse.birt.report.engine.extension.internal;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.extension.IRowMetaData;
import org.eclipse.birt.report.engine.extension.IRowSet;

/**
 * 
 * 
 * @version $Revision: 1.10 $ $Date: 2006/04/06 12:35:25 $
 */
public class RowSet implements IRowSet
{

	protected IResultSet rset;
	protected IRowMetaData metaData;
	protected boolean closed;
	private boolean isOutterResultSet;
	private boolean isFirstRecord = true;

	public RowSet( IResultSet rset )
	{
		this( rset, false );
	}

	public RowSet( IResultSet rset, boolean isOutterResultSet )
	{
		this.isOutterResultSet = isOutterResultSet;
		closed = false;
		this.rset = rset;
		metaData = new IRowMetaData( ) {

			public int getColumnCount( )
			{
				return 0;
			}

			public String getColumnName( int index ) throws BirtException
			{
				return null;
			}

			public int getColumnType( int index ) throws BirtException
			{
				return -1;
			}
		};

		try
		{
			if ( rset != null)
			{
				metaData = new RowMetaData( rset.getResultMetaData( ) );
			}
		}
		catch ( BirtException ex )
		{

		}

	}

	public DataSetID getID( )
	{
		return rset.getID( );
	}

	/**
	 * returns the definition for the data row
	 * 
	 * @return the definition for the data row
	 */
	public IRowMetaData getMetaData( )
	{
		return metaData;
	}

	public boolean next( )
	{
		if ( rset != null )
		{
			if ( isFirstRecord )
			{
				isFirstRecord = false;
				if ( isOutterResultSet )
				{
					return true;
				}
			}

			return rset.next( );
		}
		return false;
	}

	public Object evaluate( String expr )
	{
		if ( rset != null )
		{
			return rset.evaluate( expr );
		}
		return null;
	}

	public Object evaluate( IBaseExpression expr )
	{
		if ( rset != null )
		{
			return rset.evaluate( expr );
		}
		return null;
	}

	public Object getValue( int columnIndex )
	{
		throw new UnsupportedOperationException( );
	}

	public Object getValue( String columnName )
	{
		throw new UnsupportedOperationException( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IRowSet#getEndingGroupLevel()
	 */
	public int getEndingGroupLevel( )
	{
		if ( rset != null )
		{
			return rset.getEndingGroupLevel( );
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IRowSet#getStartingGroupLevel()
	 */
	public int getStartingGroupLevel( )
	{
		if ( rset != null )
		{
			return rset.getStartingGroupLevel( );
		}
		return 0;
	}

	public void close( )
	{
		// If the result set is from extended item, it should be closed outside.
		if ( isOutterResultSet )
		{
			return;
		}
		if ( closed == false )
		{
			closed = true;
			if ( rset != null )
			{
				rset.close( );
			}
		}
	}

}
