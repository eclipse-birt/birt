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

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.document.stream.VersionManager;

/**
 * Save util class
 */
public class RowSaveUtil
{
	public static Integer EXCEPTION_INDICATOR = new Integer(Integer.MAX_VALUE - 100);
	private int lastRowIndex;
	private long currentOffset;

	private int rowCount;
	
	//
	private DataOutputStream rowExprsDos;
	private DataOutputStream rowLenDos;

	private boolean inited;

	private Set exprNameSet;
	private Map directColumnReferenceBinding;
	private int version;
	private Map bindingNameType;
	
	/**
	 * @param rowCount
	 * @param rowExprsOs
	 * @param rowLenOs
	 */
	RowSaveUtil( int rowCount, OutputStream rowExprsOs,
			OutputStream rowLenOs, Set exprNameSet, Map directColumnReferenceExpr, Map bindingNameType, int version )
	{
		this.rowCount = rowCount;

		this.rowExprsDos = new DataOutputStream( rowExprsOs );
		this.rowLenDos = new DataOutputStream( rowLenOs );
		this.lastRowIndex = -1;
		
		this.exprNameSet = exprNameSet;
		this.directColumnReferenceBinding = directColumnReferenceExpr;
		this.bindingNameType = bindingNameType;
		this.version = version;
	}

	/**
	 * @param currIndex
	 * @param valueMap
	 * @throws DataException
	 */
	void saveExprValue( int currIndex, Map valueMap )
			throws DataException
	{
		try
		{
			initSave( false );
			
			saveNullRowsBetween( lastRowIndex, currIndex );
			
			int rowBytes = saveExprValue( valueMap );
			IOUtil.writeLong( this.rowLenDos, currentOffset );
			currentOffset += rowBytes;
			lastRowIndex = currIndex;
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_SAVE_ERROR,
					e,
					"Result Data" );
		}
	}
	
	private int initSave( Map valueMap ) throws DataException
	{
		ByteArrayOutputStream tempBaos = new ByteArrayOutputStream( );
		BufferedOutputStream tempBos = new BufferedOutputStream( tempBaos );
		DataOutputStream tempDos = new DataOutputStream( tempBos );

		try
		{
			IOUtil.writeInt( tempDos, exprNameSet.size( ) );
			Iterator it = exprNameSet.iterator( );
			while ( it.hasNext( ) )
			{
				Object key = it.next( );
				IOUtil.writeObject( tempDos, key );
				IOUtil.writeInt( tempDos, ((Integer)this.bindingNameType.get( key )).intValue( ) );
			}
			
			IOUtil.writeInt( tempDos, this.directColumnReferenceBinding.size( ) );
			it = this.directColumnReferenceBinding.keySet( ).iterator( );
			while( it.hasNext( ) )
			{
				Object key = it.next( );
				Object value = this.directColumnReferenceBinding.get( key );
				IOUtil.writeObject( tempDos, key );
				IOUtil.writeObject(  tempDos, value );
				IOUtil.writeInt( tempDos, ((Integer)this.bindingNameType.get( key )).intValue( ) );
			}
			
			
			tempDos.flush( );
			tempBos.flush( );
			tempBaos.flush( );

			byte[] bytes = tempBaos.toByteArray( );
			int rowBytes = bytes.length;
			IOUtil.writeRawBytes( this.rowExprsDos, bytes );

			tempBaos = null;
			tempBos = null;
			tempDos = null;
			
			return rowBytes;
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_SAVE_ERROR,
					e,
					"Result Data" );
		}
	}
	
	/**
	 * @param currIndex
	 * @param exprID
	 * @param exprValue
	 * @throws DataException
	 */
	private int saveExprValue( Map valueMap ) throws DataException
	{
		ByteArrayOutputStream tempBaos = new ByteArrayOutputStream( );
		BufferedOutputStream tempBos = new BufferedOutputStream( tempBaos );
		DataOutputStream tempDos = new DataOutputStream( tempBos );

		try
		{
			IOUtil.writeInt( tempDos, exprNameSet.size( ) );
			Iterator it = exprNameSet.iterator( );
			while ( it.hasNext( ) )
			{
				Object key = it.next( );
				Object value = valueMap.get( key );
				if( value instanceof BirtException )
					value = EXCEPTION_INDICATOR;
				IOUtil.writeObject( tempDos, value );
			}

			tempDos.flush( );
			tempBos.flush( );
			tempBaos.flush( );

			byte[] bytes = tempBaos.toByteArray( );
			int rowBytes = bytes.length;
			IOUtil.writeRawBytes( this.rowExprsDos, bytes );

			tempBaos = null;
			tempBos = null;
			tempDos = null;
			
			return rowBytes;
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_SAVE_ERROR,
					e,
					"Result Data" );
		}
	}

	/**
	 * @return
	 */
	Set getExprNameSet( )
	{
		return this.exprNameSet;
	}
	
	/**
	 * @param currIndex
	 * @throws DataException
	 */
	void saveFinish( int currIndex ) throws DataException
	{
		initSave( true );

		try
		{
			saveNullRowsBetween( lastRowIndex, currIndex );

			rowExprsDos.close( );
			rowLenDos.close( );
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_SAVE_ERROR,
					e,
					"Result Data" );
		}
	}

	/**
	 * init save environment
	 */
	private void initSave( boolean finish ) throws DataException
	{
		if ( inited == true )
			return;

		inited = true;
		try
		{
			// TODO: enhance me
			IOUtil.writeInt( this.rowExprsDos, rowCount );
			
			Map map = new HashMap();
			Iterator it = exprNameSet.iterator( );
			while(it.hasNext( ))
			{
				Object value = it.next( );
				map.put( value, value );
			}
			int rowBytes = this.version >= VersionManager.VERSION_2_2_1_3
					? this.initSave( map ) : this.saveExprValue( map );
			IOUtil.writeInt( this.rowExprsDos, rowBytes );
			this.rowExprsDos.flush( );
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_SAVE_ERROR, e );
		}
	}
	
	/**
	 * @param lastRowIndex
	 * @param currIndex
	 * @throws IOException
	 */
	private void saveNullRowsBetween( int lastRowIndex, int currIndex )
			throws IOException
	{
		int gapRows = currIndex - lastRowIndex - 1;
		for ( int i = 0; i < gapRows; i++ )
		{
			IOUtil.writeInt( this.rowExprsDos, 0 );
			IOUtil.writeLong( this.rowLenDos, currentOffset );
			currentOffset += IOUtil.INT_LENGTH;
		}
	}
	
}
