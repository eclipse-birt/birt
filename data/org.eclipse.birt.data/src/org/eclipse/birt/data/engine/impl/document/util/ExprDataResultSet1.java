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

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.document.VersionManager;
import org.eclipse.birt.data.engine.impl.document.viewing.ExprMetaInfo;

/**
 * The raw result set which will retrieve the raw data of expression value from
 * the report document. This class is used when the query is running based on a
 * first created report document, which has such a characteristic that all
 * expression rows are valid row, and then there is no row index information.
 */
public class ExprDataResultSet1 extends BaseExprDataResultSet
{
	private InputStream rowIs;
	private BufferedInputStream rowBuffDis;
	
	/**
	 * @param rowIs,
	 *            the input stream for expression row
	 * @param inExprMetas,
	 *            the expression meta data
	 * @throws DataException 
	 */
	public ExprDataResultSet1( InputStream rowIs, ExprMetaInfo[] inExprMetas )
			throws DataException
	{
		super( inExprMetas );

		this.rowIs = rowIs;
		this.rowBuffDis = new BufferedInputStream( rowIs );
		this.exprDataReader = new ExprDataReader1( rowBuffDis,
				null,
				VersionManager.VERSION_2_1 );
		this.rowCount = exprDataReader.getCount( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.document.viewing.IExprDataResultSet#close()
	 */
	public void close( )
	{		
		try
		{
			if ( rowIs != null )
			{
				exprDataReader.close( );
				rowBuffDis.close( );
				rowIs.close( );
				rowIs = null;
			}
		}
		catch ( IOException e )
		{
			// ignore
		}
	}
	
}
