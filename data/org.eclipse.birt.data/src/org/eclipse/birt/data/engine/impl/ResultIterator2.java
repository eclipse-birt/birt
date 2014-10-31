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

package org.eclipse.birt.data.engine.impl;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.cache.ResultObjectUtil;
import org.eclipse.birt.data.engine.expression.ExpressionCompilerUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.document.QueryResultInfo;
import org.eclipse.birt.data.engine.impl.document.stream.StreamManager;
import org.eclipse.birt.data.engine.impl.document.stream.VersionManager;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.mozilla.javascript.Scriptable;

/**
 * When useDetails==false, this class is used.
 */
class ResultIterator2 extends ResultIterator
{

	// the value of lower group level
	private int lowestGroupLevel;

	private int currRowIndex;

	private int cachedRowId;

	private boolean isSummary;
	private SummaryGroupLevelCalculator groupLevelCalculator;
	private static Logger logger = Logger.getLogger( ResultIterator2.class.getName( ) );
	private StreamManager streamManager = null;
	private DataOutputStream dataSetStream = null;
	private DataOutputStream dataSetLenStream = null;
	private RAOutputStream raDataSet = null;
	private long offset = 4;
	private long rowCountOffset = 0;
	private boolean saveToDoc = false;
	private List<IBinding> bindings = null;
	
	private DataEngineContext dtContext;

	/**
	 * @param context
	 * @param queryResults
	 * @param queryResultID
	 * @param useDetails
	 * @param lowestGroupLevel
	 * @throws DataException
	 */
	ResultIterator2( IServiceForResultSet rService,
			org.eclipse.birt.data.engine.odi.IResultIterator odiResult,
			Scriptable scope, int rawIdStartingValue ) throws DataException
	{
		super( rService, odiResult, scope, rawIdStartingValue );
		Object[] params = {
				rService, odiResult, scope
		};
		logger.entering( ResultIterator2.class.getName( ),
				"ResultIterator2",
				params );

		this.lowestGroupLevel = rService.getQueryDefn( ).getGroups( ).size( );
		this.currRowIndex = -1;
		this.cachedRowId = 0;
		this.dtContext = rService.getSession( ).getEngineContext( );
		
		this.isSummary = ( rService.getQueryDefn( ) instanceof IQueryDefinition )
				? ( (IQueryDefinition) rService.getQueryDefn( ) ).isSummaryQuery( )
				: false;
		if ( this.isSummary )
		{
			if ( lowestGroupLevel == 0 )
				this.groupLevelCalculator = new SummaryGroupLevelCalculator( null );
			else
			{
				int[][] groupIndex = new int[lowestGroupLevel + 1][];
				for ( int i = 0; i <= lowestGroupLevel; i++ )
				{
					groupIndex[i] = this.odiResult.getGroupStartAndEndIndex( i );
				}

				this.groupLevelCalculator = new SummaryGroupLevelCalculator( groupIndex );
			}
			if ( rService.getSession( ).getEngineContext( ).getMode( ) == DataEngineContext.MODE_GENERATION )
			{
				this.saveToDoc = true;
				streamManager = new StreamManager( rService.getSession( )
						.getEngineContext( ),
						new QueryResultInfo( rService.getQueryResults( )
								.getID( ), null, 0 ) );
				try
				{
					bindings = findSavedBinding( rService.getQueryDefn( ).getBindings( ) );
					this.doSaveResultClass( streamManager.getOutStream( DataEngineContext.DATASET_META_STREAM,
							StreamManager.ROOT_STREAM,
							StreamManager.SELF_SCOPE ),
							bindings );

					raDataSet = (RAOutputStream) streamManager.getOutStream( DataEngineContext.DATASET_DATA_STREAM,
							StreamManager.ROOT_STREAM,
							StreamManager.SELF_SCOPE );
					rowCountOffset = raDataSet.getOffset( );
					dataSetStream = new DataOutputStream( raDataSet );
					IOUtil.writeInt( dataSetStream, -1 );
					dataSetLenStream = new DataOutputStream( streamManager.getOutStream( DataEngineContext.DATASET_DATA_LEN_STREAM,
							StreamManager.ROOT_STREAM,
							StreamManager.SELF_SCOPE ) );
				}
				catch ( Exception e )
				{
					throw new DataException( e.getLocalizedMessage( ) );
				}
			}
		}
		logger.exiting( ResultIterator2.class.getName( ), "ResultIterator2" );
	}

	private List<IBinding> findSavedBinding( Map bindingMap )
	{
		Iterator bindingIt = bindingMap.values( ).iterator( );
		List<IBinding> bindingList = new ArrayList<IBinding>( );

		while ( bindingIt.hasNext( ) )
		{
			IBinding binding = (IBinding) bindingIt.next( );
			List<String> referencedBindings = new ArrayList<String>( );
			
			try
			{
				if ( binding.getAggrFunction( ) != null )
				{
					IBaseExpression expr = binding.getExpression( );
					if ( expr != null )
					{
						referencedBindings = ExpressionCompilerUtil.extractColumnExpression( binding.getExpression( ),
								ExpressionUtil.DATASET_ROW_INDICATOR );
					}
					if ( !referencedBindings.isEmpty( ) )
					{
						bindingList.add( binding );
						continue;
					}
					for ( IBaseExpression argExpr : (List<IBaseExpression>) binding.getArguments( ) )
					{
						referencedBindings = ExpressionCompilerUtil.extractColumnExpression( argExpr,
								ExpressionUtil.DATASET_ROW_INDICATOR );
						if ( !referencedBindings.isEmpty( ) )
							break;
					}
					if ( !referencedBindings.isEmpty( ) )
					{
						bindingList.add( binding );
						continue;
					}

					boolean needRecalcualte = false;
					if ( expr != null )
					{
						referencedBindings = ExpressionCompilerUtil.extractColumnExpression( binding.getExpression( ),
								ExpressionUtil.ROW_INDICATOR );
						for ( int i = 0; i < referencedBindings.size( ); i++ )
						{
							IBinding b = (IBinding) bindingMap.get( referencedBindings.get( i ) );
							if ( b != null && b.getAggrFunction( ) != null )
							{
								needRecalcualte = true;
								break;
							}
						}
					}
					if( needRecalcualte )
					{
						continue;
					}
					for ( IBaseExpression argExpr : (List<IBaseExpression>) binding.getArguments( ) )
					{
						referencedBindings = ExpressionCompilerUtil.extractColumnExpression( argExpr,
								ExpressionUtil.ROW_INDICATOR );

						needRecalcualte = needRecalculate( referencedBindings, bindingMap ) ;
						
						if( needRecalcualte )
							break;
					}
					if ( !needRecalcualte )
					{
						bindingList.add( binding );
					}
				}
				else
				{
					bindingList.add( binding );
				}
			}
			catch ( DataException e )
			{
				bindingList.add( binding );
			}
		}
		return bindingList;
	}
	
	
	private boolean needRecalculate(List<String> referencedBindings, Map bindingMap) throws DataException
	{
		for ( int i = 0; i < referencedBindings.size( ); i++ )
		{
			IBinding b = (IBinding) bindingMap.get( referencedBindings.get( i ) );
			if ( b != null && b.getAggrFunction( ) != null )
			{
				return true;
			}
			
			if ( b.getExpression( ) != null )
			{
				return needRecalculate( ExpressionCompilerUtil.extractColumnExpression( b.getExpression( ),
						ExpressionUtil.ROW_INDICATOR ), bindingMap );
			}
		}
		
		return false;
	}
	
	

	private void doSaveResultClass( OutputStream outputStream,
			List<IBinding> requestColumnMap ) throws BirtException
	{
		assert outputStream != null;

		DataOutputStream dos = new DataOutputStream( outputStream );
		try
		{
			IOUtil.writeInt( outputStream, requestColumnMap.size( ) );

			for ( int i = 0; i < requestColumnMap.size( ); i++ )
			{
				IBinding binding = requestColumnMap.get( i );
				IOUtil.writeInt( dos, i + 1 );
				IOUtil.writeString( dos, binding.getBindingName( ) );
				IOUtil.writeString( dos, null );
				IOUtil.writeString( dos, null );
				IOUtil.writeString( dos, getDataTypeClass( binding ).getName( ) );
				IOUtil.writeString( dos, null );
				IOUtil.writeBool( dos, false );
				IOUtil.writeString( dos, null );
				if( streamManager.getVersion( ) >= VersionManager.VERSION_2_5_2_0 )
				{
					IOUtil.writeInt( dos, -1 );
					IOUtil.writeString( dos, null );
					IOUtil.writeBool( dos, false );
					IOUtil.writeBool( dos, false );
				}
			}

			dos.close( );
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_SAVE_ERROR,
					e,
					"Result Class" );
		}
	}

	private Class getDataTypeClass( IBinding binding ) throws DataException
	{
		Class clazz = DataType.getClass( binding.getDataType( ) );
		return clazz == null ? String.class : clazz;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.ResultIterator#next()
	 */
	public boolean next( ) throws BirtException
	{
		boolean hasNext = super.next( );
		if ( hasNext )
			currRowIndex++;
		else if ( currRowIndex == -1 )
		{
			// If empty result set, the cachedRowId should be -1.
			this.cachedRowId = -1;
		}
		return hasNext;
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.ResultIterator#hasNextRow()
	 */
	protected boolean hasNextRow( ) throws DataException
	{
		boolean result = false;

		int index = this.odiResult.getCurrentResultIndex( );
		this.odiResult.last( lowestGroupLevel );

		if ( this.isSummary )
		{
			result = this.odiResult.next( );
			if ( this.saveToDoc )
			{
				try
				{
					IOUtil.writeLong( dataSetLenStream, offset );
					offset += this.writeResultObject( this.boundColumnValueMap );
				}
				catch ( IOException e )
				{
					throw new DataException( e.getLocalizedMessage( ) );
				}
			}

		}
		else
		{
			boolean shouldMoveForward = false;
			if ( index != this.odiResult.getCurrentResultIndex( ) )
			{
				result = odiResult.getCurrentResult( ) == null ? false : true;
				shouldMoveForward = false;
			}
			else
			{
				shouldMoveForward = true;
			}

			if ( shouldMoveForward )
			{
				result = this.odiResult.next( );
			}
		}
		if ( result )
		{
			// cachedStartingGroupLevel = odiResult.getStartingGroupLevel( );

			if ( rowIDUtil == null )
				rowIDUtil = new RowIDUtil( );

			if ( this.rowIDUtil.getMode( this.odiResult ) == RowIDUtil.MODE_NORMAL )
				cachedRowId = this.odiResult.getCurrentResultIndex( );
			else
			{
				IResultObject ob = this.odiResult.getCurrentResult( );
				if ( ob == null )
					cachedRowId = -1;
				else
				{
					if(ob.getFieldValue( rowIDUtil.getRowIdPos( ) ) != null)
						cachedRowId = ( (Integer) ob.getFieldValue( rowIDUtil.getRowIdPos( ) ) ).intValue( );
					else
						cachedRowId = -1;
				}
			}
		}

		return result;

	}

	private int writeResultObject( Map valueMap ) throws DataException,
			IOException
	{

		ByteArrayOutputStream tempBaos = new ByteArrayOutputStream( );
		BufferedOutputStream tempBos = new BufferedOutputStream( tempBaos );
		DataOutputStream tempDos = new DataOutputStream( tempBos );

		for ( IBinding binding : bindings )
		{
			if ( this.streamManager.getVersion( ) > VersionManager.VERSION_3_7_2_1
					|| "4.2.0.v20120611".equals( this.dtContext.getBundleVersion( ) )
					|| "4.2.1.v20120820".equals( this.dtContext.getBundleVersion( ) ) )
			{
				ResultObjectUtil.writeObject( tempDos,
						valueMap.get( binding.getBindingName( ) ),
						this.getDataTypeClass( binding ), this.streamManager.getVersion( ) );
			}
			else
			{
				IOUtil.writeObject( tempDos,
						valueMap.get( binding.getBindingName( ) ) );				
			}
		}

		tempDos.flush( );
		tempBos.flush( );
		tempBaos.flush( );

		byte[] bytes = tempBaos.toByteArray( );
		int rowBytes = bytes.length;
		IOUtil.writeRawBytes( dataSetStream, bytes );

		tempBaos = null;
		tempBos = null;
		tempDos = null;

		return rowBytes;
	}

	public void close () throws BirtException
	{
		super.close( );
		if ( this.saveToDoc )
		{
			try
			{
				this.saveToDoc = false;
				raDataSet.seek( this.rowCountOffset );
				IOUtil.writeInt( dataSetStream, this.currRowIndex + 1 );
				dataSetLenStream.close( );
				dataSetStream.close( );
			}
			catch ( Exception e )
			{
				//ignore
			}
		}
	}
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getRowId()
	 */
	public int getRowId( ) throws BirtException
	{
		return this.cachedRowId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.impl.ResultIterator#getStartingGroupLevel()
	 * 
	 * public int getStartingGroupLevel( ) throws DataException { return
	 * this.odiResult.getStartingGroupLevel( ); }
	 */

	public int getEndingGroupLevel( ) throws DataException
	{
		// make sure that the ending group level value is also correct
		if ( this.isSummary )
		{
			return this.groupLevelCalculator.getEndingGroupLevel( this.odiResult.getCurrentResultIndex( ) );
		}

		return super.getEndingGroupLevel( );
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getRowIndex()
	 */
	public int getRowIndex( ) throws BirtException
	{
		return currRowIndex;
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#moveTo(int)
	 */
	public void moveTo( int rowIndex ) throws BirtException
	{
		if ( rowIndex < 0 || rowIndex < this.currRowIndex )
			throw new DataException( ResourceConstants.INVALID_ROW_INDEX,
					Integer.valueOf( rowIndex ) );
		else if ( rowIndex == currRowIndex )
			return;

		int gapRows = rowIndex - currRowIndex;
		for ( int i = 0; i < gapRows; i++ )
		{
			if ( this.next( ) == false )
				throw new DataException( ResourceConstants.INVALID_ROW_INDEX,
						Integer.valueOf( rowIndex ) );
		}
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.impl.ResultIterator#goThroughGapRows(int)
	 */
	protected void goThroughGapRows( int groupLevel ) throws DataException,
			BirtException
	{
		odiResult.last( groupLevel );
	}
}
