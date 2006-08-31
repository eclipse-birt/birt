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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * Save/Load filter definition information.
 */
public class FilterDefnUtil
{
	
	/**
	 * @param outputStream
	 * @param filterList
	 * @throws DataException
	 */
	static void saveFilterDefn( OutputStream outputStream,
			List filterList ) throws DataException
	{
		DataOutputStream dos = new DataOutputStream( outputStream );

		int size = filterList == null ? 0 : filterList.size( );
		try
		{
			IOUtil.writeInt( dos, size );
			for ( int i = 0; i < size; i++ )
			{
				IFilterDefinition filterDefn = (IFilterDefinition) filterList.get( i );
				ExprUtil.saveBaseExpr( dos, filterDefn.getExpression( ) );
			}

			dos.flush( );
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_SAVE_ERROR, e );
		}
	}
	
	/**
	 * @param inputStream
	 * @return
	 * @throws DataException 
	 */
	static List loadFilterDefn( InputStream inputStream )
			throws DataException
	{
		List filterList = new ArrayList( );
		DataInputStream dis = new DataInputStream( inputStream );
		try
		{
			int size = IOUtil.readInt( inputStream );
			for ( int i = 0; i < size; i++ )
			{
				IBaseExpression baseExpr = ExprUtil.loadBaseExpr( dis );
				filterList.add( new FilterDefinition( baseExpr ) );
			}
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_LOAD_ERROR, e );
		}

		return filterList;
	}
	
	/**
	 * @param filterDefn1
	 * @param filterDefn2
	 * @return
	 */
	public static boolean isEqualFilter( IFilterDefinition filterDefn1,
			IFilterDefinition filterDefn2 )
	{
		if ( filterDefn1 == filterDefn2 )
			return true;

		if ( filterDefn1 == null || filterDefn2 == null )
			return false;

		return ExprUtil.isEqualExpression( filterDefn1.getExpression( ),
				filterDefn2.getExpression( ) );
	}
	
	/**
	 * 
	 * @param filters1
	 * @param filters2
	 * @return
	 */
	public static boolean isEqualFilter( List filters1, List filters2 )
	{
		if ( filters1 == null && filters2 == null )
			return true;
		if ( filters1 == null && filters2 != null )
			return false;
		if ( filters1 != null && filters2 == null )
			return false;
		if ( filters1.size( ) != filters2.size( ) )
			return false;
		for ( int i = 0; i < filters1.size( ); i++ )
		{
			if ( !isEqualFilter( (IFilterDefinition) filters1.get( i ),
					(IFilterDefinition) filters2.get( i ) ) )
				return false;
		}
		return true;
	}
	
	/**
	 * @param oldFilterList
	 * @param newFilterList
	 * @return
	 */
	public static boolean isConflictFilter( List oldFilterList, List newFilterList )
	{
		if ( oldFilterList == null || oldFilterList.size( ) == 0 )
			return false;

		if ( newFilterList == null
				|| newFilterList.size( ) < oldFilterList.size( ) )
			return true;

		for ( int i = 0; i < oldFilterList.size( ); i++ )
		{
			IFilterDefinition oldFilter = (IFilterDefinition) oldFilterList.get( i );
			IFilterDefinition newFilter = (IFilterDefinition) newFilterList.get( i );
			if ( FilterDefnUtil.isEqualFilter( oldFilter, newFilter ) == false )
				return true;
		}
		
		return false;
	}
	
	/**
	 * @param oldFilterList
	 * @param newFilerList
	 * @return
	 * @throws DataException
	 */
	public static List getRealFilterList( List oldFilterList, List newFilterList )
			throws DataException
	{
		if ( oldFilterList == null || oldFilterList.size( ) == 0 )
			return newFilterList;

		if ( newFilterList == null
				|| newFilterList.size( ) < oldFilterList.size( ) )
			throw new DataException( ResourceConstants.RD_INVALID_FILTER );

		for ( int i = 0; i < oldFilterList.size( ); i++ )
		{
			IFilterDefinition oldFilter = (IFilterDefinition) oldFilterList.get( i );
			IFilterDefinition newFilter = (IFilterDefinition) newFilterList.get( i );
			if ( FilterDefnUtil.isEqualFilter( oldFilter, newFilter ) == false )
				throw new DataException( ResourceConstants.RD_INVALID_FILTER );
		}

		List updatedList = new ArrayList( );
		for ( int i = oldFilterList.size( ); i < newFilterList.size( ); i++ )
			updatedList.add( newFilterList.get( i ) );

		return updatedList;
	}
	
	/**
	 * @param filterDefn
	 * @return
	 */
	public static int hashCode( List filterList )
	{
		int hashValue = 0;

		if ( filterList == null || filterList.size( ) == 0 )
		{
			hashValue = 0;
		}
		else
		{
			for ( int i = 0; i < filterList.size( ); i++ )
			{
				IFilterDefinition filterDefn = (IFilterDefinition) filterList.get( i );
				IBaseExpression baesExpr = filterDefn.getExpression( );
				hashValue += ExprUtil.hashCode( baesExpr );
			}
		}

		return hashValue;
	}
	
}
