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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IGroupInstanceInfo;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryExecutionHints;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.GroupInstanceInfo;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryExecutionHints;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.document.stream.VersionManager;

import com.ibm.icu.util.ULocale;

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
	public static void saveBaseQueryDefn( OutputStream outputStream,
			IBaseQueryDefinition queryDefn, int version, String bundleVersion ) throws DataException
	{
		try
		{
			// binding column definition
			saveBinding( outputStream, queryDefn.getBindings( ), version );
			
			// filter definition
			FilterDefnUtil.saveFilterDefn( outputStream, queryDefn.getFilters( ), version );
			// group definition
			GroupDefnUtil.saveGroupDefn( outputStream, queryDefn.getGroups( ), version, bundleVersion );
			// sort definition
			saveSorts( outputStream, queryDefn.getSorts( ), version );

			// misc property: max row, use details
			IOUtil.writeInt( outputStream, queryDefn.getMaxRows( ) );
			IOUtil.writeBool( outputStream, queryDefn.usesDetails( ) );
			if ( version >= VersionManager.VERSION_2_5_2_1 && !"2.6.1.v20100915".equals( bundleVersion ) )
				IOUtil.writeBool( outputStream, queryDefn.cacheQueryResults( ) );
			
			// sub query name
			saveSubQuery( outputStream, queryDefn.getSubqueries( ), version, bundleVersion );
			
			// query execution hint
			saveQueryExecutionHints( outputStream, queryDefn.getQueryExecutionHints( ), version );
			
			saveSummaryTableInfo( outputStream, queryDefn, version );
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_SAVE_ERROR, e );
		}
		
	}

	private static void saveSummaryTableInfo( OutputStream outputStream,
			IBaseQueryDefinition queryDefn, int version ) throws IOException
	{
		if( version >= VersionManager.VERSION_2_5_1_1 && queryDefn instanceof IQueryDefinition)
		{
			IOUtil.writeBool( outputStream, ((IQueryDefinition)queryDefn).isSummaryQuery( )); 
		}
	}
	
	/**
	 * 
	 * @param outputStream
	 * @param hint
	 * @param version
	 * @throws DataException
	 */
	private static void saveQueryExecutionHints( OutputStream outputStream, IQueryExecutionHints hint, int version ) throws DataException
	{
		if( version < VersionManager.VERSION_2_3_2_1 )
			return;
		try
		{
			IOUtil.writeBool( outputStream, hint == null? true:hint.doSortBeforeGrouping( ) );
			List<IGroupInstanceInfo> list = hint == null? new ArrayList<IGroupInstanceInfo>():hint.getTargetGroupInstances( );
			IOUtil.writeInt( outputStream, list.size( ) );
			for( IGroupInstanceInfo info: list )
			{
				IOUtil.writeInt( outputStream, info.getGroupLevel( ) );
				IOUtil.writeInt( outputStream, info.getRowId( ) );
			}
		}
		catch ( IOException e )
		{
			throw new DataException( e.getLocalizedMessage( ),e );
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
					BindingIOUtil.saveBinding( dos, (IBinding) entry.getValue( ), version );
			}
		}
		
		dos.flush( );
	}
	
	/**
	 * @param outputStream
	 * @param sorts
	 * @throws IOException 
	 */
	public static void saveSorts( OutputStream outputStream, List sorts, int version )
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
				if ( version >= VersionManager.VERSION_2_3_1 )
					IOUtil.writeInt( dos, sortDefn.getSortStrength( ) );
				if ( version >= VersionManager.VERSION_2_5_0_1 )
					IOUtil.writeString( dos, sortDefn.getSortLocale( ) == null? null:sortDefn.getSortLocale( ).getBaseName( ) );
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
			Collection subQuery, int version, String bundleVersion ) throws DataException, IOException
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
				saveBaseQueryDefn( outputStream, subQueryDefn, version, bundleVersion );
			}
		}

		dos.flush( );
	}
	
	/**
	 * @param outputStream
	 * @throws DataException
	 * @throws IOException
	 */
	public static IQueryDefinition loadQueryDefn( InputStream inputStream, int version, String bundleVersion )
			throws DataException
	{
		QueryDefinition queryDefn = new QueryDefinition( );
		loadBaseQueryDefn( inputStream, queryDefn, version, bundleVersion );
		return queryDefn;
	}
	
	/**
	 * @param inputStream
	 * @param queryDefn
	 * @throws DataException
	 */
	private static void loadBaseQueryDefn( InputStream inputStream,
			BaseQueryDefinition queryDefn, int version, String bundleVersion ) throws DataException
	{
		try
		{
			queryDefn.getBindings( )
						.putAll( loadBinding( inputStream, version ) );
			
			// filter definition
			queryDefn.getFilters( )
					.addAll( FilterDefnUtil.loadFilterDefn( inputStream, version ) );
			// group definition
			queryDefn.getGroups( )
					.addAll( GroupDefnUtil.loadGroupDefn( inputStream, queryDefn, version, bundleVersion ) );
			// sort definition
			queryDefn.getSorts( ).addAll( loadSorts( inputStream, version ) );

			// misc property: max row, use details
			queryDefn.setMaxRows( IOUtil.readInt( inputStream ) );
			queryDefn.setUsesDetails( IOUtil.readBool( inputStream ) );
			if ( version >= VersionManager.VERSION_2_5_2_1 && !"2.6.1.v20100915".equals( bundleVersion ) )
				queryDefn.setCacheQueryResults( IOUtil.readBool( inputStream ) );

			// sub query name
			queryDefn.getSubqueries( ).addAll( loadSubQuery( inputStream, queryDefn, version, bundleVersion ) );
			
			// QueryExecutionHints
			queryDefn.setQueryExecutionHints( loadQueryExecutionHints( inputStream, version ) );
			
			if( queryDefn instanceof IQueryDefinition && version >= VersionManager.VERSION_2_5_1_1 )
			{
				((QueryDefinition)queryDefn).setIsSummaryQuery( IOUtil.readBool( inputStream ) );
			}
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_LOAD_ERROR, e );
		}
	}
	
	/**
	 * This method loads the query execution hints
	 * @param inputStream
	 * @param version
	 * @return
	 * @throws DataException
	 */
	private static IQueryExecutionHints loadQueryExecutionHints(
			InputStream inputStream, int version ) throws DataException
	{
		if( version < VersionManager.VERSION_2_3_2_1 )
			return null;
		try
		{
			QueryExecutionHints hints = new QueryExecutionHints();
			hints.setSortBeforeGrouping( IOUtil.readBool( inputStream ) );
			
			int groupInstanceInfoSize = IOUtil.readInt( inputStream );
			for( int i = 0; i < groupInstanceInfoSize; i++ )
			{
				hints.addTargetGroupInstance( new GroupInstanceInfo( IOUtil.readInt( inputStream ), IOUtil.readInt( inputStream )) );
			}

			return hints;
		}
		catch ( IOException e )
		{
			throw new DataException( e.getLocalizedMessage( ),e );
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

		Map exprMap = new LinkedHashMap( );
		int size = IOUtil.readInt( dis );

		for ( int i = 0; i < size; i++ )
		{
			String exprName = IOUtil.readString( dis );
			if ( version < VersionManager.VERSION_2_2_1 )
				exprMap.put( exprName, new Binding( exprName,
						ExprUtil.loadBaseExpr( dis ) ) );
			else
				exprMap.put( exprName, BindingIOUtil.loadBinding( dis, version ) );
		}

		return exprMap;
	}
	
	/**
	 * @param outputStream
	 * @param sorts
	 * @throws IOException
	 */
	public static List loadSorts( InputStream inputStream, int version ) throws IOException
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
				sortDefn.setExpression( sortKeyExpr );
				
			sortDefn.setSortDirection( direction );
			
			if ( version >= VersionManager.VERSION_2_3_1 )
				sortDefn.setSortStrength( IOUtil.readInt( dis ));
			
			if ( version >= VersionManager.VERSION_2_5_0_1 )
			{
				String locale = IOUtil.readString( dis );
				if( locale != null )
					sortDefn.setSortLocale( new ULocale( locale ) );
			}
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
	static Collection loadSubQuery( InputStream inputStream, IBaseQueryDefinition parent, int version, String bundleVersion )
			throws DataException, IOException
	{
		DataInputStream dis = new DataInputStream( inputStream );

		Collection subQuerys = new ArrayList( );
		int size = IOUtil.readInt( dis );
		for ( int i = 0; i < size; i++ )
		{
			SubqueryDefinition subQueryDefn = new SubqueryDefinition( IOUtil.readString( dis ), parent );
			subQueryDefn.setApplyOnGroupFlag( IOUtil.readBool( dis ) );
			loadBaseQueryDefn( dis, subQueryDefn, version, bundleVersion );
			subQuerys.add( subQueryDefn );
		}

		return subQuerys;
	}
	
}
