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
 * @version $Revision: 1.2 $ $Date: 2005/04/22 03:46:34 $
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
		return rset.next();
	}
	
	public Object evaluate(IBaseExpression expr)
	{
		return rset.evaluate(expr);
	}
	
	public Object getValue(int columnIndex)
	{
		throw new UnsupportedOperationException();
	}
	
	public Object getValue(String columnName)
	{
		throw new UnsupportedOperationException();
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
