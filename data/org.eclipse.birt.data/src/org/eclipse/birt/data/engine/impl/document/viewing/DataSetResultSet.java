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
package org.eclipse.birt.data.engine.impl.document.viewing;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.cache.ResultSetUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IDataSetPopulator;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * The raw result set which will retrieve the raw data of data set from the
 * report document.
 */
public class DataSetResultSet implements IDataSetPopulator
{
	private int rowIndex;
	private int rowCount;
	
	private RAInputStream inputStream;
	private BufferedInputStream bis;
	private DataInputStream dis;
	
	private IResultClass rsMetaData;
	private int colCount;
	
	private IResultObject currentObject; 
	private RAInputStream dataSetRowLensStream;
	private DataInputStream disRowLensStream;
	private long initPos;

	/**
	 * @param inputStream
	 * @throws DataException 
	 */
	public DataSetResultSet( RAInputStream inputStream, RAInputStream lensStream, IResultClass rsMetaData, int version ) throws DataException
	{
		assert inputStream != null;
		assert rsMetaData != null;
		
		this.inputStream = inputStream;
		this.rowIndex = -1;
		
		this.dataSetRowLensStream = lensStream; 
		if( lensStream!= null )
			this.disRowLensStream = new DataInputStream( this.dataSetRowLensStream);
		try
		{
			this.initPos = this.inputStream.getOffset( );
		}
		catch ( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.rsMetaData = rsMetaData;
		this.colCount = rsMetaData.getFieldCount( );
		this.initLoad( );
	}
	
	/**
	 * 
	 * @return
	 */
	public int getRowCount()
	{
		return this.rowCount;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.odi.IDataSetPopulator#next()
	 */
	public IResultObject next( ) throws DataException
	{
		if ( this.rowIndex < this.rowCount - 1 || this.rowCount == -1 )
		{
			try
			{
				rowIndex++;
				this.currentObject = ResultSetUtil.readResultObject( dis,
						rsMetaData,
						colCount );
			}
			catch ( IOException e )
			{
				throw new DataException( ResourceConstants.RD_LOAD_ERROR,
						e,
						"Result Data" );
			}
		}
		else
		{
			this.currentObject = null;
		}
		return this.currentObject;
	}
	
	public IResultObject getResultObject()
	{
		return this.currentObject;
	}
	
	public int getCurrentIndex()
	{
		return rowIndex;
	}
	
	public void skipTo( int index ) throws DataException, IOException
	{
		if( this.rowIndex == index )
			return;
		
		if ( this.rowIndex < this.rowCount || this.rowCount == -1 )
		{
			if( this.dataSetRowLensStream!= null )
			{
				this.dataSetRowLensStream.seek( index * 8 );
				long position = IOUtil.readLong( this.disRowLensStream );
				this.rowIndex = index;
				this.inputStream.seek( position + this.initPos );
				this.dis = new DataInputStream( inputStream );
				this.currentObject = ResultSetUtil.readResultObject( dis, rsMetaData, colCount );
				return;
			}
		}
		/*
		while( this.rowIndex - 1 < index && this.rowIndex < this.rowCount )
			this.next( );*/
	}
	
	/**
	 * @throws DataException
	 */
	private void initLoad( ) throws DataException
	{
		if ( dis == null )
		{
			bis = new BufferedInputStream( inputStream );
			try
			{
				dis = new DataInputStream( bis );
				this.rowCount = IOUtil.readInt( dis );
			//	this.initPos = inputStream.getOffset( );
			}
			catch ( IOException e )
			{
				throw new DataException( ResourceConstants.RD_LOAD_ERROR,
						e,
						"result data" );
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public IResultClass getResultClass()
	{
		return this.rsMetaData;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.odi.ICustomDataSet#close()
	 */
	public void close( )
	{
		try
		{
			if ( dis != null )
			{
				dis.close( );
				bis.close( );
			}
		}
		catch ( IOException e )
		{
			// ignore throw new DataException( "error in close" );
		}
	}
	
}
