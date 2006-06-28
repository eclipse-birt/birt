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

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.document.VersionManager;

/**
 * Read the raw expression data from report document. This instance only read
 * the row one by one.
 */
class ExprDataReader1 implements IExprDataReader
{
	private int currReadIndex;
	private int currRowIndex;

	private int INT_LENGTH;

	private DataInputStream rowExprsDis;
	private RAInputStream rowExprsRAIs;
	private RAInputStream rowLenRAIs;
	
	private int rowCount;
	
	private int version;
	private Map exprValueMap;

	/**
	 * @param rowExprsRAIs
	 * @param rowLenRAIs
	 * @param version
	 */
	ExprDataReader1( RAInputStream rowExprsRAIs, RAInputStream rowLenRAIs,
			int version ) throws DataException
	{
		try
		{
			rowCount = IOUtil.readInt( rowExprsRAIs );
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_LOAD_ERROR,
					e,
					"Result Data" );
		}
		
		this.rowExprsDis = new DataInputStream( rowExprsRAIs );
		this.rowExprsRAIs = rowExprsRAIs;
		this.rowLenRAIs = rowLenRAIs;
				
		this.version = version;

		this.currReadIndex = 0;
		this.currRowIndex = -1;
		
		this.INT_LENGTH = IOUtil.INT_LENGTH;
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
		return this.getRowIndex( );
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.document.IExprResultReader#getRowIndex()
	 */
	public int getRowIndex( )
	{
		if ( this.currRowIndex >= this.rowCount )
			return this.rowCount;
		
		return this.currRowIndex;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.document.IExprResultReader#next()
	 */
	public boolean next( ) throws DataException
	{
		this.currRowIndex++;
		
		return this.currRowIndex < this.rowCount;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.document.IExprResultReader#getRowValue()
	 */
	public Map getRowValue( ) throws DataException
	{
		try
		{
			if ( currReadIndex < currRowIndex + 1 )
			{
				this.skipTo( currRowIndex );
				this.exprValueMap = this.getValueMap( );
			}
			currReadIndex = currRowIndex + 1;
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
	 * @param absoluteRowIndex
	 * @throws IOException
	 * @throws DataException
	 */
	private void skipTo( int absoluteRowIndex ) throws IOException, DataException
	{
		if ( currReadIndex == absoluteRowIndex )
			return;

		if ( version == VersionManager.VERSION_2_0 )
		{
			int exprCount;
			int gapRows = absoluteRowIndex - currReadIndex;
			for ( int j = 0; j < gapRows; j++ )
			{
				exprCount = IOUtil.readInt( rowExprsDis );
				for ( int i = 0; i < exprCount; i++ )
				{
					IOUtil.readString( rowExprsDis );
					IOUtil.readObject( rowExprsDis );
				}
			}
		}
		else
		{
			rowLenRAIs.seek( absoluteRowIndex * INT_LENGTH );
			int rowOffsetAbsolute = IOUtil.readInt( rowLenRAIs );
			// 4 is the first bytes of row length
			rowExprsRAIs.seek( rowOffsetAbsolute + 4 ); 
			rowExprsDis = new DataInputStream( rowExprsRAIs );
		}
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
				rowExprsDis = null;
			}
		}
		catch ( IOException e )
		{
			// ignore read exception
		}
	}
	
}
