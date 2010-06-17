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
package org.eclipse.birt.data.engine.executor.cache;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultObject;
import org.eclipse.birt.data.engine.expression.ExpressionCompilerUtil;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * 
 */
public class ResultSetUtil
{	 	
	//----------------------service for result object save and load--------------
	
	/**
	 * Write the result object value if it is used in column binding map
	 * 
	 * @param dos
	 * @param resultObject
	 * @param nameSet
	 * @throws DataException
	 * @throws IOException
	 */
	public static int writeResultObject( DataOutputStream dos,
			IResultObject resultObject, int count, Set nameSet )
			throws DataException, IOException
	{
		if ( resultObject.getResultClass( ) == null )
			return 0;
		
		ByteArrayOutputStream tempBaos = new ByteArrayOutputStream( );
		BufferedOutputStream tempBos = new BufferedOutputStream( tempBaos );
		DataOutputStream tempDos = new DataOutputStream( tempBos );

		for ( int i = 1; i <= count; i++ )
		{
			if ( nameSet != null
					&& ( nameSet.contains( resultObject.getResultClass( )
							.getFieldName( i ) ) || nameSet.contains( resultObject.getResultClass( )
							.getFieldAlias( i ) ) ) )
				IOUtil.writeObject( tempDos, resultObject.getFieldValue( i ) );
		}
		
		tempDos.flush( );
		tempBos.flush( );
		tempBaos.flush( );

		byte[] bytes = tempBaos.toByteArray( );
		int rowBytes = bytes.length;
		IOUtil.writeRawBytes( dos, bytes );

		tempBaos = null;
		tempBos = null;
		tempDos = null;
		
		return rowBytes;
	}

	/**
	 * @param dis
	 * @param rsMeta
	 * @param count
	 * @return
	 * @throws IOException
	 */
	public static IResultObject readResultObject( DataInputStream dis,
			IResultClass rsMeta, int count ) throws IOException
	{
		Object[] obs = new Object[rsMeta.getFieldCount( )];

		for ( int i = 0; i < count; i++ )
			obs[i] = IOUtil.readObject( dis, DataEngineSession.getCurrentClassLoader( ) );

		return new ResultObject( rsMeta, obs );
	}
	
	/**
	 * Get result set column name collection from column binding map
	 * 
	 * @param cacheRequestMap
	 * @return
	 * @throws DataException
	 */
	public static Set getRsColumnRequestMap( List<IBinding> cacheRequestMap )
			throws DataException
	{
		Set resultSetNameSet = new HashSet( );
		if ( cacheRequestMap != null )
		{
			Iterator<IBinding> iter = cacheRequestMap.iterator( );
			List<String> dataSetColumnList = null;
			while ( iter.hasNext( ) )
			{
				IBinding binding = iter.next( );
				dataSetColumnList = binding == null
						? null
						: ExpressionCompilerUtil.extractDataSetColumnExpression(  binding.getExpression( ) );
				if ( dataSetColumnList != null )
				{
					resultSetNameSet.addAll( dataSetColumnList );
				}
			}
		}
		return resultSetNameSet;
	}
	
}
