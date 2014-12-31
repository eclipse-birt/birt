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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.ResultFieldMetadata;
import org.eclipse.birt.data.engine.executor.cache.ResultSetUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.StringTable;
import org.eclipse.birt.data.engine.impl.index.IOrderedIntSet;
import org.eclipse.birt.data.engine.impl.index.IOrderedIntSetIterator;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * The raw result set which will retrieve the raw data of data set from the
 * report document.
 */
public class DataSetResultSet implements IDataSetResultSet
{
	private int rowIndex;
	private int rowCount;
	
	private int version;
	
	private RAInputStream inputStream;
	private BufferedInputStream bis;
	private DataInputStream dis;
	
	private IResultClass rsMetaData;
	private int colCount;
	
	private IResultObject currentObject; 
	private RAInputStream dataSetRowLensStream;
	private DataInputStream disRowLensStream;
	private long initPos;
	private IOrderedIntSet prefilteredRowIds;
	private Map index;
	private Map<String, StringTable> stringTableMap;
	private boolean includeInnerID = true;
	private boolean readInnerId = false;
	private IOrderedIntSetIterator rowIdIterator;


	/**
	 * @param inputStream
	 * @throws DataException 
	 */
	public DataSetResultSet( RAInputStream inputStream,
			RAInputStream lensStream, IResultClass rsMetaData,
			IOrderedIntSet prefilteredRows, Map<String, StringTable> stringTableMap, Map index, int version )
			throws DataException
	{
		this( inputStream,
				lensStream,
				rsMetaData,
				prefilteredRows,
				stringTableMap,
				index,
				version,
				true,
				false );
	}
	
	/**
	 * @param inputStream
	 * @throws DataException 
	 */
	public DataSetResultSet( RAInputStream inputStream,
			RAInputStream lensStream, IResultClass rsMetaData,
			IOrderedIntSet prefilteredRows,
			Map<String, StringTable> stringTableMap, Map index, int version,
			boolean includeInnerID, boolean readInnerId ) throws DataException
	{
		assert inputStream != null;
		assert rsMetaData != null;

		this.inputStream = inputStream;
		this.rowIndex = -1;
		this.includeInnerID = includeInnerID;
		this.version = version;

		this.dataSetRowLensStream = lensStream;
		if ( lensStream != null )
			this.disRowLensStream = new DataInputStream( this.dataSetRowLensStream );
		try
		{
			this.initPos = this.inputStream.getOffset( );
		}
		catch ( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}

		this.rsMetaData = populateResultClass( rsMetaData );
		// Notice we should use column count in original metadata
		this.colCount = rsMetaData.getFieldCount( );

		this.prefilteredRowIds = prefilteredRows;
		if( this.prefilteredRowIds != null )
			this.rowIdIterator = this.prefilteredRowIds.iterator();
		
		this.index = index;
		this.stringTableMap = stringTableMap;
		this.readInnerId = readInnerId;
		this.initLoad( );
	}

	private IResultClass populateResultClass( IResultClass meta )
			throws DataException
	{
		List<ResultFieldMetadata> list = new ArrayList<ResultFieldMetadata>( );
		for ( int i = 1; i <= meta.getFieldCount( ); i++ )
		{
			list.add( meta.getFieldMetaData( i ) );
		}
		if ( includeInnerID )
		{
			ResultFieldMetadata rfm = new ResultFieldMetadata( 0,
					ExprMetaUtil.POS_NAME,
					null,
					Integer.class,
					null,
					true,
					-1 );
			list.add( rfm );
		}
		return new ResultClass( list );
	}

	/**
	 * 
	 * @return
	 */
	public int getRowCount( )
	{
		if ( this.prefilteredRowIds != null )
		{
			return this.prefilteredRowIds.size( );
		}
		return this.rowCount;
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.IDataSetPopulator#next()
	 */
	public IResultObject next( ) throws DataException
	{
		if ( this.prefilteredRowIds != null )
		{
			if ( !this.rowIdIterator.hasNext() )
				return null;
			this.skipTo( this.rowIdIterator.next() );
			return this.getResultObject( );
		}

		if ( this.rowIndex < this.rowCount - 1 || this.rowCount == -1 )
		{
			try
			{
				rowIndex++;
				this.currentObject = ResultSetUtil.readResultObject( dis,
						rsMetaData,
						colCount,
						this.stringTableMap,
						this.index, version, readInnerId );
				if ( this.includeInnerID && !readInnerId )
				{
					this.currentObject.setCustomFieldValue( ExprMetaUtil.POS_NAME,
							this.getCurrentIndex( ) );					
				}
			}
			catch ( Exception e )
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

	public IResultObject getResultObject( )
	{
		return this.currentObject;
	}

	public int getCurrentIndex( )
	{
		return rowIndex;
	}

	public void skipTo( int index ) throws DataException
	{
		try
		{
			if ( this.rowIndex == index )
				return;

			if ( this.rowIndex < this.rowCount || this.rowCount == -1 )
			{
				if ( this.dataSetRowLensStream != null )
				{
					this.dataSetRowLensStream.seek( index * 8L );
					long position = IOUtil.readLong( this.disRowLensStream );
					this.rowIndex = index;
					this.inputStream.seek( position + this.initPos );
					this.dis = new DataInputStream( inputStream );
					this.currentObject = ResultSetUtil.readResultObject( dis,
							rsMetaData,
							colCount,
							this.stringTableMap,
							this.index, version, readInnerId );
					if ( this.includeInnerID && !readInnerId )
					{
						this.currentObject.setCustomFieldValue( ExprMetaUtil.POS_NAME,
								this.getCurrentIndex( ) );
					}
					return;
				}
			}
		}
		catch ( IOException e )
		{
			throw new DataException( e.getLocalizedMessage( ), e );
		}
		/*
		 * while( this.rowIndex - 1 < index && this.rowIndex < this.rowCount )
		 * this.next( );
		 */
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
				// this.initPos = inputStream.getOffset( );
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
	public IResultClass getResultClass( )
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
			if( disRowLensStream != null )
				disRowLensStream.close( );
		}
		catch ( IOException e )
		{
			// ignore throw new DataException( "error in close" );
		}
	}

}
