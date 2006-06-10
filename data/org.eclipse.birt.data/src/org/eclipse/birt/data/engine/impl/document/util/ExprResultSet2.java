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
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.document.RDGroupUtil;
import org.eclipse.birt.data.engine.impl.document.StreamManager;

/**
 * Used for reading subquery result which is generated from existing report
 * document.
 */
public class ExprResultSet2 extends ExprResultSet
{

	/**
	 * @param streamManager
	 * @param rdGroupUtil
	 * @param version
	 * @param isBasedOnSecondRD
	 * @throws DataException
	 */
	public ExprResultSet2( StreamManager streamManager, int version,
			boolean isBasedOnSecondRD ) throws DataException
	{
		super( streamManager, null, version, isBasedOnSecondRD );
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.document.ExprResultSet#prepare()
	 */
	protected void prepare( ) throws DataException
	{
		int parentIndex = 0;
		try
		{
			InputStream inputStream = streamManager.getInStream( DataEngineContext.SUBQUERY_PARENTINDEX_STREAM,
					StreamManager.SUB_QUERY_STREAM,
					StreamManager.SELF_SCOPE );
			parentIndex = IOUtil.readInt( inputStream );
		}
		catch ( IOException e )
		{
			throw new DataException( e.getMessage( ) );
		}
		System.out.println( parentIndex );

		rowExprsRAIs = streamManager.getInStream2( DataEngineContext.EXPR_VALUE_STREAM,
				StreamManager.SUB_QUERY_STREAM,
				StreamManager.BASE_SCOPE,
				parentIndex );
		rowLenRAIs = streamManager.getInStream2( DataEngineContext.EXPR_ROWLEN_STREAM,
				StreamManager.SUB_QUERY_STREAM,
				StreamManager.BASE_SCOPE,
				parentIndex );
		rowInfoRAIs = streamManager.getInStream( DataEngineContext.ROW_INDEX_STREAM,
				StreamManager.SUB_QUERY_STREAM,
				StreamManager.SELF_SCOPE );
		this.exprResultReader = new ExprDataReader2( rowExprsRAIs,
				rowLenRAIs,
				rowInfoRAIs );
		
		this.rowCount = this.exprResultReader.getCount( );
		this.rdGroupUtil = this.loadGroupUtil( StreamManager.ROOT_STREAM,
				StreamManager.SELF_SCOPE );
	}
	
	/**
	 * @return
	 * @throws DataException
	 */
	private RDGroupUtil loadGroupUtil( int streamPos, int streamScope ) throws DataException
	{
		InputStream stream = streamManager.getInStream( DataEngineContext.GROUP_INFO_STREAM,
				streamPos,
				streamScope );
		BufferedInputStream buffStream = new BufferedInputStream( stream );
		RDGroupUtil rdGroupUtil = new RDGroupUtil( buffStream );
		try
		{
			buffStream.close( );
			stream.close( );
		}
		catch ( IOException e )
		{
			// ignore it
		}

		return rdGroupUtil;
	}
	
}
