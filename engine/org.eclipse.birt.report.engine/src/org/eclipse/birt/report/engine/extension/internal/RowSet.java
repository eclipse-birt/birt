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
import org.eclipse.birt.report.engine.data.dte.DteResultSet;
import org.eclipse.birt.report.engine.extension.IRowMetaData;
import org.eclipse.birt.report.engine.extension.IRowSet;


/**
 *
 * @version $Revision: 1.6 $ $Date: 2005/05/11 11:59:31 $
 */
public class RowSet implements IRowSet
{
	protected DteResultSet rset;
	protected IRowMetaData metaData;
	protected boolean closed;
	
	public RowSet(DteResultSet rset)
	{
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

			public String getColumnExpression( int index ) throws BirtException
			{
				return null;
			}
		};

		try
		{
			if ( rset != null && rset.getQr( ) != null )
			{
				metaData = new RowMetaData( rset.getQr( ).getResultMetaData( ) );
			}
		}
		catch(BirtException ex)
		{
			
		}
		
	}
	
	
	/**
	 * returns the definition for the data row
	 * 
	 * @return the definition for the data row
	 */
	public IRowMetaData getMetaData()
	{
		return metaData;
	}
	
	public boolean next()
	{
		if ( rset != null )
		{
			return rset.next( );
		}
		return false;
	}
	
	public Object evaluate(IBaseExpression expr)
	{
		if ( rset != null )
		{
			return rset.evaluate( expr );
		}
		return null;
	}
	
	public Object getValue(int columnIndex)
	{
		throw new UnsupportedOperationException();
	}
	
	public Object getValue(String columnName)
	{
		throw new UnsupportedOperationException();
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.extension.IRowSet#getEndingGroupLevel()
	 */
	public int getEndingGroupLevel( )
	{
		if (rset != null)
		{
			return rset.getEndingGroupLevel();
		}
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.extension.IRowSet#getStartingGroupLevel()
	 */
	public int getStartingGroupLevel( )
	{
		if (rset != null)
		{
			return rset.getStartingGroupLevel();
		}
		return 0;
	}
	
	public void close()
	{
		if (closed == false)
		{
			closed = true;
			if ( rset != null )
			{
				rset.close( );
			}
		}
	}
	
}
