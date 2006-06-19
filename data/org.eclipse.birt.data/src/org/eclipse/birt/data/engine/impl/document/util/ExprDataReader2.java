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
package org.eclipse.birt.data.engine.impl.document.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.document.viewing.RowIndexUtil;

/**
 * Read expression result when this report document is processed before. For
 * example, if there is an original query which generates a report document, and
 * a new query which is running based the report document. So the new query
 * result may be not the same as the result before.
 */
class ExprDataReader2 implements IExprDataReader
{
	private RAInputStream rowExprsIs;
	private RAInputStream rowLenIs;
	
	private BufferedInputStream rowBuffExprsDis;
	private BufferedInputStream rowBuffLenDis;
	private BufferedInputStream rowInfoBuffDis;
	
	private DataInputStream rowExprsDis;
	
	protected int rowCount;
	
	private int lastRowIndex;
	private int currRowIndex;
	
	private int currRowLenReadIndex;
	
	private RowIndexUtil rowInfoUtil;
	
	private int nextDestIndex; // TODO: enhanceme
	
	private Map exprValueMap;	
	private Map rowIDMap;
	
	/**
	 * @param rowExprsIs
	 * @param rowLenIs
	 */
	ExprDataReader2( RAInputStream rowExprsIs, RAInputStream rowLenIs )
	{
		this.rowExprsIs = rowExprsIs;
		this.rowLenIs = rowLenIs;
		
		this.currRowIndex = -1;
		this.lastRowIndex = -1;
		this.currRowLenReadIndex = -1;
		
		this.rowIDMap = new HashMap();
	}
	
	/**
	 * @param rowExprsIs
	 * @param rowLenIs
	 * @param rowInfoIs
	 * @throws DataException
	 */
	ExprDataReader2( RAInputStream rowExprsIs, RAInputStream rowLenIs,
			RAInputStream rowInfoIs ) throws DataException
	{
		this( rowExprsIs, rowLenIs );
		this.rowInfoBuffDis = new BufferedInputStream( rowInfoIs );
		this.rowInfoUtil = new RowIndexUtil( rowInfoBuffDis );
		
		try
		{
			this.rowCount = (int) ( rowInfoIs.length( ) / 4 );
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_LOAD_ERROR,
					e,
					"Result Data" );
		}		
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.document.IExprResultReader#getRowCount()
	 */
	public int getCount( )
	{
		return this.rowCount;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.document.IExprResultReader#getRowId()
	 */
	public int getRowId( )
	{
		int destIndex = ( (Integer) rowIDMap.get( new Integer( currRowIndex ) ) ).intValue( );
		return destIndex;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.document.IExprResultReader#getRowIndex()
	 */
	public int getRowIndex( )
	{
		if ( this.currRowIndex >= this.rowCount )
			return this.rowCount;

		return currRowIndex;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.document.IExprResultReader#next()
	 */
	public boolean next( )
	{
		this.currRowIndex++;
		
		boolean hasNext = this.currRowIndex < this.rowCount;
		if ( hasNext )
		{
			this.nextDestIndex = getNextDestIndex( currRowIndex );
			this.rowIDMap.put( new Integer( currRowIndex ),
					new Integer( nextDestIndex ) );
		}
		return hasNext;
	}
	
	/**
	 *
	 */
	protected int getNextDestIndex( int currIndex )
	{
		return rowInfoUtil.read( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.document.IExprResultReader#getRowValue()
	 */
	public Map getRowValue( ) throws DataException
	{
		if ( lastRowIndex == currRowIndex )
			return this.exprValueMap;

		lastRowIndex = currRowIndex;
		try
		{
			this.skipTo( nextDestIndex );
			this.exprValueMap = this.getValueMap( );
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_LOAD_ERROR,
					e,
					"Result Data" );
		}

		return exprValueMap;
	}
	
	/**
	 * @param absoluteIndex
	 * @throws IOException
	 * @throws DataException
	 */
	private void skipTo( int absoluteIndex ) throws IOException, DataException
	{
		if ( currRowLenReadIndex == absoluteIndex )
			return;
		
		currRowLenReadIndex = absoluteIndex + 1;
		
		rowLenIs.seek( absoluteIndex * 4 );
		rowBuffLenDis = new BufferedInputStream( rowLenIs );
		
		rowExprsIs.seek( IOUtil.readInt( rowBuffLenDis ) + 4 );
		rowBuffExprsDis = new BufferedInputStream( rowExprsIs );
		rowExprsDis = new DataInputStream( rowBuffExprsDis );
	}

	/**
	 * @throws IOException
	 */
	private Map getValueMap( ) throws IOException
	{
		Map valueMap = new HashMap( );

		int exprCount = IOUtil.readInt( rowExprsDis );
		for ( int i = 0; i < exprCount; i++ )
		{
			String exprID = IOUtil.readString( rowExprsDis );
			Object exprValue = IOUtil.readObject( rowExprsDis );
			valueMap.put( exprID, exprValue );
		}

		return valueMap;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.document.IExprResultReader#close()
	 */
	public void close( )
	{
		try
		{			
			if ( rowExprsDis != null )
			{
				rowExprsDis.close( );
				rowBuffExprsDis.close( );
			}
			if ( rowBuffLenDis != null )
			{
				rowBuffLenDis.close( );				
			}
			if ( rowInfoBuffDis != null )
			{
				rowInfoBuffDis.close( );
			}
		}
		catch ( IOException e )
		{
			// ignore read exception
		}
	}
	
}
