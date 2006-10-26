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

package org.eclipse.birt.report.engine.data.dte;

import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.eclipse.birt.core.archive.FileArchiveReader;
import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.parser.ReportParser;
import org.eclipse.birt.report.model.api.DesignFileException;

public class DataEngineTest extends TestCase
{

	private static final int MODE_GENERATION = 0;
	private static final int MODE_PRESENTATION = 1;

	private String SINGLE_DATASET_DESIGN = "SingleDataSet.xml";
	private String NESTED_DATASET_DESIGN = "NestedDataSet.xml";
	private String SUBQUERY_DATASET_DESIGN = "SubqueryDataSet.xml";

	private String ARCHIVE_PATH = "docArchive";
	private String ARCHIVE_METANAME = "metaName";

	private Report getReport( String designName ) throws DesignFileException
	{
		InputStream in = this.getClass( ).getResourceAsStream( designName );
		assertTrue( in != null );
		ReportParser parser = new ReportParser( );
		Report report = parser.parse( "", in );
		assertTrue( report != null );

		return report;
	}

	IDocArchiveWriter archWriter;
	IDocArchiveReader archReader;

	private IDataEngine getDataEngine( Report report, String archivePath,
			String archiveMetaName, int mode ) throws Exception
	{
		ExecutionContext context = new ExecutionContext( 0 );

		if ( mode == MODE_GENERATION )
		{
			archWriter = new FileArchiveWriter( archivePath );
			archWriter.initialize( );
			DataGenerationEngine dataGenEngine = new DataGenerationEngine(
					context, archWriter );
			dataGenEngine.prepare( report, null );
			return dataGenEngine;
		}
		else if ( mode == MODE_PRESENTATION )
		{
			archReader = new FileArchiveReader( archivePath );
			archReader.open( );
			DataPresentationEngine dataPresEngine = new DataPresentationEngine(
					context, archReader );
			dataPresEngine.prepare( report, null );
			return dataPresEngine;
		}
		else
		{
			return null;
		}
	}

	protected String loadResource( String resourceName ) throws Exception
	{
		InputStream in = this.getClass( ).getResourceAsStream( resourceName );
		assertTrue( in != null );
		byte[] buffer = new byte[in.available( )];
		in.read( buffer );
		return new String( buffer );
	}

	private void delete( File dir )
	{
		if ( dir.isFile( ) )
		{
			dir.delete( );
		}

		if ( dir.isDirectory( ) )
		{
			File[] files = dir.listFiles( );
			for ( int i = 1; i < files.length; i++ )
			{
				delete( files[i] );
			}
			dir.delete( );
		}

	}

	public void tearDown( )
	{
		File file = new File( ARCHIVE_PATH );
		if ( file.exists( ) )
		{
			delete( file );
		}
		file = new File( ARCHIVE_METANAME );
		if ( file.exists( ) )
		{
			delete( file );
		}

	}

	public void test( ) throws Exception
	{
		doTestSingleQGeneration( );
		doTestSingleQPresentation( );
		doTestNestedQGeneration( );
		doTestNestedQPresentation( );
		doTestSubqueryGeneration( );
		doTestSubqueryPresentation( );
	}

	public void doTestSingleQGeneration( ) throws Exception
	{
		Report report = getReport( SINGLE_DATASET_DESIGN );
		IDataEngine dataEngine = getDataEngine( report, ARCHIVE_PATH,
				ARCHIVE_METANAME, MODE_GENERATION );

		Iterator iter = report.getQueries( ).iterator( );
		IResultSet resultSet = null;

		String goldenFile = "SingleDataSet.txt";
		String goldenStr = loadResource( goldenFile );
		String resultStr = "";
		while ( iter.hasNext( ) )
		{
			IQueryDefinition query = (IQueryDefinition) iter.next( );
			resultSet = (IResultSet) dataEngine.execute( query );
			int i = 0;
			while ( resultSet.next( ) && i < 3 )
			{
				Map map = query.getResultSetExpressions( );
				Iterator it = map.keySet( ).iterator( );
				while ( it.hasNext( ) )
				{
					String next = (String) it.next( );
					resultStr += resultSet.getString( next );
				}
				i++;
			}
		}
		resultSet.close( );
		dataEngine.shutdown( );
		archWriter.finish( );
		assertEquals( goldenStr, resultStr );
	}

	public void doTestSingleQPresentation( ) throws Exception
	{
		Report report = getReport( SINGLE_DATASET_DESIGN );
		IDataEngine dataEngine = getDataEngine( report, ARCHIVE_PATH,
				ARCHIVE_METANAME, MODE_PRESENTATION );

		Iterator iter = report.getQueries( ).iterator( );
		IResultSet resultSet = null;

		String goldenFile = "SingleDataSet.txt";
		String goldenStr = loadResource( goldenFile );
		String resultStr = "";
		while ( iter.hasNext( ) )
		{
			IQueryDefinition query = (IQueryDefinition) iter.next( );
			resultSet = (IResultSet) dataEngine.execute( query );
			int i = 0;
			while ( resultSet.next( ) && i < 3 )
			{
				Map map = query.getResultSetExpressions( );
				Iterator it = map.keySet( ).iterator( );
				while ( it.hasNext( ) )
				{
					String next = (String) it.next( );
					resultStr += resultSet.getString( next );
				}
				i++;
			}
		}
		resultSet.close( );
		dataEngine.shutdown( );
		archReader.close( );
		assertEquals( goldenStr, resultStr );
	}

	public void doTestNestedQGeneration( ) throws Exception
	{
		Report report = getReport( NESTED_DATASET_DESIGN );
		IDataEngine dataEngine = getDataEngine( report, ARCHIVE_PATH,
				ARCHIVE_METANAME, MODE_GENERATION );

		Iterator iter = report.getQueries( ).iterator( );

		String goldenFile = "NestedDataSet.txt";
		String goldenStr = loadResource( goldenFile );
		String resultStr = "";
		IQueryDefinition parentQuery = (IQueryDefinition) iter.next( );
		IQueryDefinition childQuery = (IQueryDefinition) iter.next( );
		IResultSet parentRSet = null;
		IResultSet childRSet = null;

		parentRSet = (IResultSet) dataEngine.execute( parentQuery );

		while ( parentRSet.next( ) )
		{
			Map parentMap = parentQuery.getResultSetExpressions( );
			Iterator parentIter = parentMap.keySet( ).iterator( );
			while ( parentIter.hasNext( ) )
			{
				String nextPar = (String) parentIter.next( );
				resultStr += parentRSet.getString( nextPar );
			}
			childRSet = (IResultSet) dataEngine
					.execute( parentRSet, childQuery );
			while ( childRSet.next( ) )
			{
				Map childMap = childQuery.getResultSetExpressions( );
				Iterator childIter = childMap.keySet( ).iterator( );
				while ( childIter.hasNext( ) )
				{
					String nextChi = (String) childIter.next( );
					resultStr += childRSet.getString( nextChi );
				}
			}
			childRSet.close( );
		}

		parentRSet.close( );
		dataEngine.shutdown( );
		archWriter.finish( );
		assertEquals( goldenStr, resultStr );
	}

	public void doTestNestedQPresentation( ) throws Exception
	{
		Report report = getReport( NESTED_DATASET_DESIGN );
		IDataEngine dataEngine = getDataEngine( report, ARCHIVE_PATH,
				ARCHIVE_METANAME, MODE_PRESENTATION );

		Iterator iter = report.getQueries( ).iterator( );

		String goldenFile = "NestedDataSet.txt";
		String goldenStr = loadResource( goldenFile );

		String resultStr = "";
		IQueryDefinition parentQuery = (IQueryDefinition) iter.next( );
		IQueryDefinition childQuery = (IQueryDefinition) iter.next( );
		IResultSet parentRSet = null;
		IResultSet childRSet = null;

		parentRSet = dataEngine.execute( parentQuery );

		while ( parentRSet.next( ) )
		{
			Map parentMap = parentQuery.getResultSetExpressions( );
			Iterator parentIter = parentMap.keySet( ).iterator( );
			while ( parentIter.hasNext( ) )
			{
				String nextPar = (String) parentIter.next( );
				resultStr += parentRSet.getString( nextPar );
			}
			childRSet = (IResultSet) dataEngine
					.execute( parentRSet, childQuery );
			while ( childRSet.next( ) )
			{
				Map childMap = childQuery.getResultSetExpressions( );
				Iterator childIter = childMap.keySet( ).iterator( );
				while ( childIter.hasNext( ) )
				{
					String nextChi = (String) childIter.next( );
					resultStr += childRSet.getString( nextChi );
				}
			}
			childRSet.close( );
		}

		parentRSet.close( );
		dataEngine.shutdown( );
		archReader.close( );
		assertEquals( goldenStr, resultStr );
	}

	public void doTestSubqueryGeneration( ) throws Exception
	{
		Report report = getReport( SUBQUERY_DATASET_DESIGN );
		IDataEngine dataEngine = getDataEngine( report, ARCHIVE_PATH,
				ARCHIVE_METANAME, MODE_GENERATION );

		Iterator iter = report.getQueries( ).iterator( );
		IResultSet resultSet = null;

		String goldenFile = "SubqueryDataSet.txt";
		String goldenStr = loadResource( goldenFile );
		String resultStr = "";
		while ( iter.hasNext( ) )
		{
			IBaseQueryDefinition query = (IBaseQueryDefinition) iter.next( );
			resultSet = dataEngine.execute( query );
			while ( resultSet.next( ) )
			{
				int startGroup = resultSet.getStartingGroupLevel( );
				if ( startGroup == 0 || startGroup == 1 )
				{
					Iterator groupIterator = query.getGroups( ).iterator( );

					IGroupDefinition group = (IGroupDefinition) groupIterator
							.next( );
					Iterator subQueryIter = group.getSubqueries( ).iterator( );
					IBaseQueryDefinition subQuery = (IBaseQueryDefinition) subQueryIter
							.next( );
					IResultSet subResultSet = dataEngine.execute( resultSet,
							subQuery );
					Map map = subQuery.getResultSetExpressions( );
					resultStr += getResultSet( subResultSet, map.keySet( ) );
					subResultSet.close( );

				}
			}
		}
		resultSet.close( );
		dataEngine.shutdown( );
		archWriter.finish( );
		assertEquals( goldenStr, resultStr );
	}

	private String getResultSet( IResultSet resultSet, Set columnsSet )
			throws Exception
	{
		String res = "";

		while ( resultSet.next( ) )
		{
			Iterator columns = columnsSet.iterator( );
			while ( columns.hasNext( ) )
			{
				res += resultSet.getString( (String) columns.next( ) );
			}
		}
		return res;
	}

	public void doTestSubqueryPresentation( ) throws Exception
	{
		Report report = getReport( SUBQUERY_DATASET_DESIGN );
		IDataEngine dataEngine = getDataEngine( report, ARCHIVE_PATH,
				ARCHIVE_METANAME, MODE_PRESENTATION );

		Iterator iter = report.getQueries( ).iterator( );
		IResultSet resultSet = null;

		String goldenFile = "SubqueryDataSet.txt";
		String goldenStr = loadResource( goldenFile );
		String resultStr = "";

		while ( iter.hasNext( ) )
		{
			IBaseQueryDefinition query = (IBaseQueryDefinition) iter.next( );
			resultSet = dataEngine.execute( query );
			while ( resultSet.next( ) )
			{
				int startGroup = resultSet.getStartingGroupLevel( );
				if ( startGroup == 0 || startGroup == 1 )
				{
					Iterator groupIterator = query.getGroups( ).iterator( );

					IGroupDefinition group = (IGroupDefinition) groupIterator
							.next( );
					Iterator subQueryIter = group.getSubqueries( ).iterator( );
					IBaseQueryDefinition subQuery = (IBaseQueryDefinition) subQueryIter
							.next( );
					IResultSet subResultSet = dataEngine.execute( resultSet,
							subQuery );
					Map map = subQuery.getResultSetExpressions( );
					resultStr += getResultSet( subResultSet, map.keySet( ) );
					subResultSet.close( );

				}
			}
		}
		resultSet.close( );
		dataEngine.shutdown( );
		archReader.close( );
		assertEquals( goldenStr, resultStr );
	}
}
