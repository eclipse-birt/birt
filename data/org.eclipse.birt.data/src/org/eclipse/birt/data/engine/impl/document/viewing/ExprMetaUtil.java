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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * 
 */
public class ExprMetaUtil
{
	//
	public final static String POS_NAME = "_dte_inner_row_ori_position_";
	
	private static ExprMetaUtil instance = new ExprMetaUtil( );
	
	private Set nameSet;
	
	/**
	 * @throws DataException
	 * @throws DataException
	 * @throws IOException
	 */
	public static void saveExprMetaInfo( IBaseQueryDefinition queryDefn,
			Set nameSet, OutputStream outputStream ) throws DataException
	{
		instance.nameSet = nameSet;
		List exprMetaList = instance.prepareQueryDefn( queryDefn );

		DataOutputStream dos = new DataOutputStream( outputStream );
		try
		{
			int size = exprMetaList.size( );
			IOUtil.writeInt( dos, size );

			for ( int i = 0; i < size; i++ )
			{
				ExprMetaInfo exprMeta = (ExprMetaInfo) exprMetaList.get( i );
				IOUtil.writeString( dos, exprMeta.getName( ) );
				IOUtil.writeInt( dos, exprMeta.getGroupLevel( ) );
				IOUtil.writeInt( dos, exprMeta.getDataType( ) );
				IOUtil.writeInt( dos, exprMeta.getType( ) );
				IOUtil.writeString( dos, exprMeta.getJSText( ) );
			}
			dos.close( );
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_SAVE_ERROR,
					"expression metadata" );
		}
	}
	
	/**
	 * Extract the expression information from query definition
	 * 
	 * @return queryDefn
	 */
	private List prepareQueryDefn( IBaseQueryDefinition queryDefn )
	{
		List exprMetaList = new ArrayList( );

		prepareGroup( queryDefn, exprMetaList );
	
		return exprMetaList;
	}
	
	/**
	 * Extract the expression information from group definition
	 * 
	 * @param trans
	 * @param groupLevel
	 * @param exprMetaList
	 * @throws DataException
	 */
	private void prepareGroup( IBaseQueryDefinition trans, List exprMetaList )
	{
		Map exprMap = trans.getResultSetExpressions( );
		if ( exprMap == null )
			return;
		
		Iterator it = exprMap.keySet( ).iterator( );
		while ( it.hasNext( ) )
		{
			String exprName = (String) it.next( );
			if ( nameSet.contains( exprName ) == false )
				continue;
			
			IBaseExpression baseExpr = (IBaseExpression) exprMap.get( exprName );

			ExprMetaInfo exprMeta = new ExprMetaInfo( );
			exprMeta.setDataType( baseExpr.getDataType( ) );
			exprMeta.setGroupLevel( 0 );
			exprMeta.setName( exprName );
			
			if ( baseExpr instanceof IScriptExpression )
			{
				exprMeta.setType( ExprMetaInfo.SCRIPT_EXPRESSION );
				exprMeta.setJSText( ((IScriptExpression)baseExpr).getText( ) );
			}

			exprMetaList.add( exprMeta );
		}
	}
	
	/**
	 * @param inputStream
	 * @throws DataException
	 */
	public static ExprMetaInfo[] loadExprMetaInfo( InputStream inputStream )
			throws DataException
	{
		ExprMetaInfo[] exprMetas = null;

		DataInputStream dis = new DataInputStream( inputStream );
		try
		{
			int size = IOUtil.readInt( dis );
			exprMetas = new ExprMetaInfo[size + 2];
			for ( int i = 0; i < size; i++ )
			{
				exprMetas[i] = new ExprMetaInfo( );
				exprMetas[i].setName( IOUtil.readString( dis ) );
				exprMetas[i].setGroupLevel( IOUtil.readInt( dis ) );
				exprMetas[i].setDataType( IOUtil.readInt( dis ) );
				exprMetas[i].setType( IOUtil.readInt( dis ) );
				exprMetas[i].setJSText( IOUtil.readString( dis ) );
			}
			
			final String FILTER_NAME = "_dte_inner_row_filter_";

			exprMetas[size] = new ExprMetaInfo( );
			exprMetas[size].setName( POS_NAME );
			exprMetas[size].setGroupLevel( 0 );
			exprMetas[size].setDataType( DataType.INTEGER_TYPE );
			exprMetas[size].setType( ExprMetaInfo.SCRIPT_EXPRESSION );
			exprMetas[size].setJSText( "dataSetRow._rowPosition" );

			exprMetas[size + 1] = new ExprMetaInfo( );
			exprMetas[size + 1].setName( FILTER_NAME );
			exprMetas[size + 1].setGroupLevel( 0 );
			exprMetas[size + 1].setType( ExprMetaInfo.SCRIPT_EXPRESSION );
			exprMetas[size + 1].setDataType( DataType.BOOLEAN_TYPE );
			exprMetas[size + 1].setJSText( null );

			dis.close( );
			return exprMetas;
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_LOAD_ERROR, e );
		}
	}
}
