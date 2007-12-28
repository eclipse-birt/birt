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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.document.stream.VersionManager;

/**
 * Save query definition structure into report document and loading from report
 * document.
 */
public class QueryDefnIOUtil
{	
	
	/**
	 * @param outputStream
	 * @param queryDefn
	 * @throws DataException
	 * @throws IOException
	 */
	static void saveBaseQueryDefn( OutputStream outputStream,
			IBaseQueryDefinition queryDefn, int version ) throws DataException
	{
		try
		{
			// binding column definition
			saveBinding( outputStream, queryDefn.getBindings( ), version );
			
			// filter definition
			FilterDefnUtil.saveFilterDefn( outputStream, queryDefn.getFilters( ) );
			// group definition
			GroupDefnUtil.saveGroupDefn( outputStream, queryDefn.getGroups( ), version );
			// sort definition
			saveSorts( outputStream, queryDefn.getSorts( ) );

			// misc property: max row, use details
			IOUtil.writeInt( outputStream, queryDefn.getMaxRows( ) );
			IOUtil.writeBool( outputStream, queryDefn.usesDetails( ) );
			
			// sub query name
			saveSubQuery( outputStream, queryDefn.getSubqueries( ), version );
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_SAVE_ERROR, e );
		}
		
	}
	
	/**
	 * @param outputStream
	 * @param exprMap
	 * @throws IOException
	 * @throws DataException 
	 */
	private static void saveBinding( OutputStream outputStream, Map exprMap, int version )
			throws IOException, DataException
	{
		DataOutputStream dos = new DataOutputStream( outputStream );
		
		if ( exprMap == null )
		{
			IOUtil.writeInt( dos, 0 );
		}
		else
		{
			IOUtil.writeInt( dos, exprMap.size( ) );

			Iterator it = exprMap.entrySet( ).iterator( );
			while ( it.hasNext( ) )
			{
				Map.Entry entry = (Entry) it.next( );
				IOUtil.writeString( dos, (String) entry.getKey( ) );
				
				if ( version < VersionManager.VERSION_2_2_1 )
					ExprUtil.saveBaseExpr( dos,
							( (IBinding) entry.getValue( ) ).getExpression( ) );
				else
					saveBinding( dos, (IBinding) entry.getValue( ) );
			}
		}
		
		dos.flush( );
	}
	
	/**
	 * 
	 * @param dos
	 * @param binding
	 * @throws DataException
	 */
	private static void saveBinding( DataOutputStream dos, IBinding binding ) throws DataException
	{
		int type = binding.getDataType( );
		String name = binding.getBindingName( );
		String function = binding.getAggrFunction( );
		IBaseExpression expr = binding.getExpression( );
		IBaseExpression filter = binding.getFilter( );
		List arguments = binding.getArguments( );
		List aggregateOn = binding.getAggregatOns( );
		
		try
		{
			//First write data type.
			IOUtil.writeInt( dos, type );
			
			//Then write Name
			IOUtil.writeString( dos, name );
			
			//Then write function
			IOUtil.writeString( dos, function );
			
			//Then write base expr
			ExprUtil.saveBaseExpr( dos, expr );
			
			//Then write filter
			ExprUtil.saveBaseExpr( dos, filter );
			
			//Then write argument size
			IOUtil.writeInt( dos, arguments.size( ) );
			
			for( int i = 0; i < arguments.size( ); i++ )
			{
				ExprUtil.saveBaseExpr( dos, (IBaseExpression)arguments.get( i ) );
			}
			
			IOUtil.writeInt( dos, aggregateOn.size( ) );
			
			for( int i = 0; i < aggregateOn.size( ); i++ )
			{
				IOUtil.writeString( dos, aggregateOn.get( i ).toString( ) );
			}
		}
		catch ( IOException e )
		{
			throw new DataException( e.getLocalizedMessage( ));
		}
	}
	
	private static IBinding loadBinding( DataInputStream dis ) throws IOException, DataException
	{
		int type = IOUtil.readInt( dis );
		String name = IOUtil.readString( dis );
		String function = IOUtil.readString( dis );
		IBaseExpression expr = ExprUtil.loadBaseExpr( dis );
		IBaseExpression filter = ExprUtil.loadBaseExpr( dis );
		
		Binding binding = new Binding( name );
		binding.setAggrFunction( function );
		binding.setDataType( type );
		binding.setExpression( expr );
		binding.setFilter( filter );
		
		int argSize = IOUtil.readInt( dis );
		for ( int i = 0; i < argSize; i++ )
		{
			binding.addArgument( ExprUtil.loadBaseExpr( dis ) );
		}
		
		int aggrSize = IOUtil.readInt( dis );
		for( int i = 0; i < aggrSize; i++ )
		{
			binding.addAggregateOn( IOUtil.readString( dis ) );
		}
		return binding;
	}
	
	/**
	 * @param outputStream
	 * @param sorts
	 * @throws IOException 
	 */
	public static void saveSorts( OutputStream outputStream, List sorts )
			throws IOException
	{
		DataOutputStream dos = new DataOutputStream( outputStream );

		if ( sorts == null )
		{
			IOUtil.writeInt( dos, 0 );
		}
		else
		{
			IOUtil.writeInt( dos, sorts.size( ) );

			Iterator it = sorts.iterator( );
			while ( it.hasNext( ) )
			{
				ISortDefinition sortDefn = (ISortDefinition) it.next( );
				IOUtil.writeString( dos, sortDefn.getColumn( ) );
				ExprUtil.saveBaseExpr( dos, sortDefn.getExpression( ) );
				IOUtil.writeInt( dos, sortDefn.getSortDirection( ) );
			}
		}

		dos.flush( );
	}
	
	/**
	 * @throws IOException 
	 * @throws DataException 
	 *
	 */
	static void saveSubQuery( OutputStream outputStream,
			Collection subQuery, int version ) throws DataException, IOException
	{
		DataOutputStream dos = new DataOutputStream( outputStream );

		if ( subQuery == null )
		{
			IOUtil.writeInt( dos, 0 );
		}
		else
		{
			IOUtil.writeInt( dos, subQuery.size( ) );
			
			Iterator it = subQuery.iterator( );
			while ( it.hasNext( ) )
			{
				ISubqueryDefinition subQueryDefn = (ISubqueryDefinition) it.next( );
				IOUtil.writeString( dos, subQueryDefn.getName( ) );
				IOUtil.writeBool( outputStream, subQueryDefn.applyOnGroup( ) );
				saveBaseQueryDefn( outputStream, subQueryDefn, version );
			}
		}

		dos.flush( );
	}
	
	/**
	 * @param outputStream
	 * @throws DataException
	 * @throws IOException
	 */
	static IQueryDefinition loadQueryDefn( InputStream inputStream, int version )
			throws DataException
	{
		QueryDefinition queryDefn = new QueryDefinition( );
		loadBaseQueryDefn( inputStream, queryDefn, version );
		return queryDefn;
	}
	
	/**
	 * @param inputStream
	 * @param queryDefn
	 * @throws DataException
	 */
	private static void loadBaseQueryDefn( InputStream inputStream,
			BaseQueryDefinition queryDefn, int version ) throws DataException
	{
		try
		{
			queryDefn.getBindings( )
						.putAll( loadBinding( inputStream, version ) );
			
			// filter definition
			queryDefn.getFilters( )
					.addAll( FilterDefnUtil.loadFilterDefn( inputStream ) );
			// group definition
			queryDefn.getGroups( )
					.addAll( GroupDefnUtil.loadGroupDefn( inputStream, queryDefn, version ) );
			// sort definition
			queryDefn.getSorts( ).addAll( loadSorts( inputStream ) );

			// misc property: max row, use details
			queryDefn.setMaxRows( IOUtil.readInt( inputStream ) );
			queryDefn.setUsesDetails( IOUtil.readBool( inputStream ) );
			
			// sub query name
			queryDefn.getSubqueries( ).addAll( loadSubQuery( inputStream, queryDefn, version) );
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_LOAD_ERROR, e );
		}
	}
	
	/**
	 * @param outputStream
	 * @param exprMap
	 * @throws IOException
	 * @throws DataException 
	 */
	private static Map loadBinding( InputStream inputStream, int version )
			throws IOException, DataException
	{
		DataInputStream dis = new DataInputStream( inputStream );

		Map exprMap = new HashMap( );
		int size = IOUtil.readInt( dis );

		for ( int i = 0; i < size; i++ )
		{
			String exprName = IOUtil.readString( dis );
			if ( version < VersionManager.VERSION_2_2_1 )
				exprMap.put( exprName, new Binding( exprName,
						ExprUtil.loadBaseExpr( dis ) ) );
			else
				exprMap.put( exprName, loadBinding( dis ) );
		}

		return exprMap;
	}
	
	/**
	 * @param outputStream
	 * @param sorts
	 * @throws IOException
	 */
	public static List loadSorts( InputStream inputStream ) throws IOException
	{
		DataInputStream dis = new DataInputStream( inputStream );

		List sortList = new ArrayList( );
		int size = IOUtil.readInt( dis );
		for ( int i = 0; i < size; i++ )
		{
			String sortKeyColumn = IOUtil.readString( dis );
			IScriptExpression sortKeyExpr = (IScriptExpression) ExprUtil.loadBaseExpr( dis );
			int direction = IOUtil.readInt( dis );

			SortDefinition sortDefn = new SortDefinition( );
			if ( sortKeyColumn != null )
				sortDefn.setColumn( sortKeyColumn );
			else
				sortDefn.setExpression( sortKeyExpr.getText( ) );
			sortDefn.setSortDirection( direction );
			sortList.add( sortDefn );
		}
		
		return sortList;
	}
	
	/**
	 * @param inputStream
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	static Collection loadSubQuery( InputStream inputStream, IBaseQueryDefinition parent, int version )
			throws DataException, IOException
	{
		DataInputStream dis = new DataInputStream( inputStream );

		Collection subQuerys = new ArrayList( );
		int size = IOUtil.readInt( dis );
		for ( int i = 0; i < size; i++ )
		{
			SubqueryDefinition subQueryDefn = new SubqueryDefinition( IOUtil.readString( dis ), parent );
			subQueryDefn.setApplyOnGroupFlag( IOUtil.readBool( dis ) );
			loadBaseQueryDefn( dis, subQueryDefn, version );
			subQuerys.add( subQueryDefn );
		}

		return subQuerys;
	}
	
}
