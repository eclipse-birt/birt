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
package org.eclipse.birt.data.engine.impl.document;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IResultIterator;

/**
 * Save expression value of every row into report document. The output format in
 * stream is, <expressCount, [expression id, expression value]*>.
 * 
 * When no value of one row is saved, the expressionCount will automatically be
 * ouptput as 0. It will happen when caller does not call getValue on this row
 * or call skipToEnd method.
 */
class RDSave implements IRDSave
{
	//	 
	private DataEngineContext context;
	
	//
	private OutputStream rowExprsOs;
	private OutputStream rowLenOs;
	
	//
	private int rowCount;
	// TODO: enhance me, this set should be extracted from queryDefn
	private IBaseQueryDefinition queryDefn;
	private Set exprNameSet;
	
	//
	private RowSaveUtil rowSaveUtil;
	private StreamManager streamManager;	
	private RDSaveUtil rdSaveUtil;
	
	/**
	 * @param context
	 * @param queryResultID
	 * @param subQueryName
	 * @param subQueryIndex
	 * @throws DataException
	 */
	RDSave( DataEngineContext context, IBaseQueryDefinition queryDefn,
			int rowCount, QueryResultInfo queryResultInfo )
			throws DataException
	{
		this.context = context;
		this.rowCount = rowCount;
		this.queryDefn = queryDefn;
		
		this.streamManager = new StreamManager( context, queryResultInfo );
		this.rdSaveUtil = new RDSaveUtil( this.context.getMode( ),
				queryDefn,
				this.streamManager );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.document.IRDSave#saveExprValue(int,
	 *      java.lang.String, java.lang.Object)
	 */
	public void saveExprValue( int currIndex, Map valueMap )
			throws DataException
	{
		if ( rowSaveUtil == null )
			this.initSaveRowUtil( );

		rowSaveUtil.saveExprValue( currIndex, valueMap );
	}
	
	/**
	 * @throws DataException
	 */
	private void initSaveRowUtil( ) throws DataException
	{
		rowExprsOs = streamManager.getOutStream( DataEngineContext.EXPR_VALUE_STREAM,
				StreamManager.ROOT_STREAM,
				StreamManager.SELF_SCOPE );
		rowLenOs = streamManager.getOutStream( DataEngineContext.EXPR_ROWLEN_STREAM,
				StreamManager.ROOT_STREAM,
				StreamManager.SELF_SCOPE );

		this.rowSaveUtil = new RowSaveUtil( rowCount, rowExprsOs, rowLenOs, this.getExprNameSet( ) );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.document.IRDSave#saveFinish(int)
	 */
	public void saveFinish( int currIndex ) throws DataException
	{
		if ( rowSaveUtil == null )
			this.initSaveRowUtil( );

		exprNameSet = rowSaveUtil.getExprNameSet( );
		rowSaveUtil.saveFinish( currIndex );

		this.closeSaveRowUtil( );

		this.saveForIV( );
	}

	/**
	 * @throws DataException
	 */
	private void closeSaveRowUtil( ) throws DataException
	{
		try
		{
			this.rowExprsOs.close( );
			this.rowLenOs.close( );
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_SAVE_ERROR,
					e,
					"Result Data" );
		}
	}
	
	/**
	 * @throws DataException
	 */
	private void saveForIV( ) throws DataException
	{
		if ( exprNameSet.size( ) == 0 )
		{
			// indicates there is no row in result set
			exprNameSet = getExprNameSet( );
		}
		
		// save expression metadata and transformation info
		this.rdSaveUtil.saveExprMetadata( exprNameSet );
	
	}
	
	/* 
	 * @see org.eclipse.birt.data.engine.impl.document.IRDSave#saveResultIterator(org.eclipse.birt.data.engine.odi.IResultIterator, int, int[])
	 */
	public void saveResultIterator( IResultIterator odiResult, int groupLevel,
			int[] subQueryInfo ) throws DataException
	{
		VersionManager.setVersion( context, VersionManager.VERSION_2_1 );
		this.rdSaveUtil.saveResultIterator( odiResult, groupLevel, subQueryInfo );
	}
	
	/**
	 * @return
	 */
	private Set getExprNameSet( )
	{
		Set set = new HashSet( );
		Iterator it = this.queryDefn.getResultSetExpressions( )
				.entrySet( )
				.iterator( );
		while ( it.hasNext( ) )
		{
			Map.Entry entry = (Map.Entry) it.next( );
			set.add( entry.getKey( ) );
		}

		return set;
	}

	public void saveStart( ) throws DataException
	{
		if ( this.streamManager.isSubquery( ) == false )
			this.rdSaveUtil.saveQueryDefn( );
		
	}
	
}
