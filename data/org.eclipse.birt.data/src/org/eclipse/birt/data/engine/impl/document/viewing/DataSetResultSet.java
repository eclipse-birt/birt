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
import java.io.InputStream;

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
	
	private InputStream inputStream;
	private BufferedInputStream bis;
	private DataInputStream dis;
	
	private IResultClass rsMetaData;
	private int colCount;
	
	/**
	 * @param inputStream
	 */
	public DataSetResultSet( InputStream inputStream, IResultClass rsMetaData )
	{
		assert inputStream != null;
		assert rsMetaData != null;
		
		this.inputStream = inputStream;
		this.rsMetaData = rsMetaData;
		this.colCount = rsMetaData.getFieldCount( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.odi.IDataSetPopulator#next()
	 */
	public IResultObject next( ) throws DataException
	{
		initLoad( );

		if ( this.rowIndex == this.rowCount )
			return null;

		try
		{
			rowIndex++;
			return ResultSetUtil.readResultObject( dis, rsMetaData, colCount );
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_LOAD_ERROR,
					e,
					"Result Data" );
		}
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
