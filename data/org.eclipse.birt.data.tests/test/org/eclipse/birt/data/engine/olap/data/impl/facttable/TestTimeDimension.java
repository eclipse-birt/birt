
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: 
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.data.impl.facttable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.olap.OLAPException;
import javax.olap.cursor.CubeCursor;
import javax.olap.cursor.EdgeCursor;


import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.api.ICubeQueryResults;
import org.eclipse.birt.data.engine.olap.api.IPreparedCubeQuery;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMeasureDefinition;
import org.eclipse.birt.data.engine.olap.data.api.cube.DocManagerMap;
import org.eclipse.birt.data.engine.olap.data.api.cube.DocManagerReleaser;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDatasetIterator;
import org.eclipse.birt.data.engine.olap.data.api.cube.IHierarchy;
import org.eclipse.birt.data.engine.olap.data.api.cube.ILevelDefn;
import org.eclipse.birt.data.engine.olap.data.document.DocumentManagerFactory;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.impl.Cube;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Dimension;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionFactory;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.LevelDefinition;
import org.eclipse.birt.data.engine.olap.data.util.DataType;
import org.eclipse.birt.data.engine.olap.impl.query.CubeQueryDefinition;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

/**
 * 
 */

public class TestTimeDimension {
	private static final String OUTPUT_FOLDER = "DtETestTempDataoutput";
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
/*
	 * @see TestCase#tearDown()
	 */
private static String[] distinct( String[] sValues )
	{
		Arrays.sort( sValues );
		List tempList = new ArrayList( );
		tempList.add( sValues[0] );
		for ( int i = 1; i < sValues.length; i++ )
		{
			if ( !sValues[i].equals( sValues[i - 1] ) )
			{
				tempList.add( sValues[i] );
			}
		}
		String[] result = new String[tempList.size( )];
		for ( int i = 0; i < result.length; i++ )
		{
			result[i] = (String)tempList.get( i );
		}
		return result;
	}
	
	/**
	 * 
	 */
	@Ignore("Ignoring since TimeDimension is not currently used if the product is used in a normal way")
	@Test
    public void testTimeDimension( ) throws Exception
	{
		String cubeName = "cube";
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		IDocumentManager documentManager = DocumentManagerFactory
				.createFileDocumentManager(engine.getSession().getTempDir());
		DocManagerMap.getDocManagerMap( )
				.set( String.valueOf( engine.hashCode( ) ),
						engine.getSession( ).getTempDir( )
								+ engine.getSession( ).getEngine( ).hashCode( ),
						documentManager );
		engine.addShutdownListener( new DocManagerReleaser( engine ) );
		
		createCube( documentManager );
		
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "l1" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "year" );
		hier2.createLevel( "month" );
		hier2.createLevel( "day" );

		IMeasureDefinition measure = cqd.createMeasure( "m1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );
		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"l1\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge2level1" );
		binding2.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"year\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding4 = new Binding( "edge2level2" );
		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"month\"]" ) );
		cqd.addBinding( binding4 );
		IBinding binding41 = new Binding( "edge2level3" );
		binding41.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"day\"]" ) );
		cqd.addBinding( binding41 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"m1\"]" ) );
		cqd.addBinding( binding5 );

		IFilterDefinition filter = new FilterDefinition( new ConditionalExpression( "dimension[\"dimension2\"][\"day\"]",
				IConditionalExpression.OP_LE,
				"13" ) );
		cqd.addFilter( filter );
		
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null, null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );
		rowEdgeBindingNames.add( "edge2level2" );
		rowEdgeBindingNames.add( "edge2level3" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1" );
		
		engine.shutdown( );
		
		documentManager.close( );
	}
	
	protected static String getTempDir()
	{
		return getOutputFolder( ).getAbsolutePath( ) + File.separator + "DataEngineSessionTemp" + File.separator;
	}
	
	/** return output folder */
	protected static File getOutputFolder()
	{
		return new File( new File(System.getProperty("java.io.tmpdir")),
				OUTPUT_FOLDER );
	}
	
	private DataEngineContext createPresentationContext( ) throws BirtException
	{
		DataEngineContext context =  DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
				null,
				null,
				null );
		context.setTmpdir( this.getTempDir( ) );
		return context;
	}
	
	private void createCube( IDocumentManager documentManager ) throws IOException, BirtException
	{
		Dimension[] dimensions = new Dimension[2];
		
		ILevelDefn[] levelDefs = new ILevelDefn[1];
		
		IDatasetIterator iterator = new Dataset2( );
		levelDefs[0] = new LevelDefinition( "l1", new String[]{"l1"}, null );
		dimensions[0] = (Dimension) DimensionFactory.createDimension( "dimension1", documentManager, iterator, levelDefs, false, new StopSign() );
		IHierarchy hierarchy = dimensions[0].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension1" );
		
		iterator = new Dataset2( );
		levelDefs[0] = new LevelDefinition( "l2", new String[]{"l2"}, null );
		dimensions[1] = (Dimension) DimensionFactory.createDimension( "dimension2", documentManager, iterator, levelDefs, true, new StopSign() );
		hierarchy = dimensions[1].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension2" );
		
		Cube cube = new Cube( "cube", documentManager );
		cube.create( new String[][]{ new String[]{ "l1" }, new String[]{ "l2" } },
				dimensions,
				new Dataset2( ),
				new String[]{ "m1" },
				new StopSign( ) );

		cube.close( );
		documentManager.flush( );
		
		
	}
	
	private void printCube( CubeCursor cursor, List columnEdgeBindingNames,
			List rowEdgeBindingNames, String measureBindingNames )
			throws Exception
	{
		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				measureBindingNames,
				null,
				null,
				null );
	}
	
	private void printCube( CubeCursor cursor, List columnEdgeBindingNames,
			List rowEdgeBindingNames, String measureBindingNames,
			String columnAggr, String rowAggr, String overallAggr )
			throws Exception
	{
		this.printCube( cursor, columnEdgeBindingNames,
			rowEdgeBindingNames, measureBindingNames,
			columnAggr, rowAggr, overallAggr, true );
	}
	
	private void printCube( CubeCursor cursor, List columnEdgeBindingNames,
			List rowEdgeBindingNames, String measureBindingNames,
			String columnAggr, String rowAggr, String overallAggr, boolean checkOutput )
			throws Exception
	{
		String output = getOutputFromCursor( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				measureBindingNames,
				columnAggr,
				rowAggr,
				overallAggr );
		System.out.println( output );
		close( cursor );
	}
	
	private void close( CubeCursor dataCursor ) throws OLAPException
	{
		for ( int i = 0; i < dataCursor.getOrdinateEdge( ).size( ); i++ )
		{
			EdgeCursor edge = (EdgeCursor) ( dataCursor.getOrdinateEdge( ).get( i ) );
			edge.close( );
		}
		dataCursor.close( );
	}
	
	private void printCube( CubeCursor cursor, List columnEdgeBindingNames,
			List rowEdgeBindingNames, String measureBindingName,
			String[] columnAggrs)
			throws Exception
	{
		String output = getOutputFromCursor(
				cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				measureBindingName,
				columnAggrs);
	}
	
	private String getOutputFromCursor( CubeCursor cursor,
			List columnEdgeBindingNames, List rowEdgeBindingNames,
			String measureBindingName, String[] columnAggrs
			) throws OLAPException
	{
		EdgeCursor edge1 = (EdgeCursor) ( cursor.getOrdinateEdge( ).get( 0 ) );
		EdgeCursor edge2 = (EdgeCursor) ( cursor.getOrdinateEdge( ).get( 1 ) );

		String[] lines = new String[columnEdgeBindingNames.size( )];
		for ( int i = 0; i < columnEdgeBindingNames.size( ); i++ )
		{
			lines[i] = "		";
		}

		while ( edge1.next( ) )
		{
			for ( int i = 0; i < columnEdgeBindingNames.size( ); i++ )
			{
				lines[i] += cursor.getObject( columnEdgeBindingNames.get( i )
						.toString( ) )
						+ "		";
			}
		}


		String output = "";
		for ( int i = 0; i < lines.length; i++ )
		{
			output += "\n" + lines[i];
		}

		while ( edge2.next( ) )
		{
			String line = "";
			for ( int i = 0; i < rowEdgeBindingNames.size( ); i++ )
			{
				line += cursor.getObject( rowEdgeBindingNames.get( i )
						.toString( ) ).toString( )
						+ "		";
			}
			edge1.beforeFirst( );
			while ( edge1.next( ) )
			{
				line += cursor.getObject( measureBindingName ) + "		";
			}
			output += "\n" + line;
		}

		String line = "total" + "		";
		edge1.beforeFirst( );
		edge2.first( );
		while( edge1.next( ) )
		{
			line+= cursor.getObject( "total" )+ "		";
		}
		output +="\n" + line;
		
		line = "maxTotal1" + "	";
		edge1.beforeFirst( );
		edge2.first( );
		while (edge1.next( ))
		{
			line+= cursor.getObject( "maxTotal1" )+ "		";
		}
		output +="\n" + line;
		
		line = "maxTotal2" + "	";
		edge1.beforeFirst( );
		edge2.first( );
		while (edge1.next( ))
		{
			line+= cursor.getObject( "maxTotal2" )+ "		";
		}
		output +="\n" + line;
		
		line = "sumTotal1" + "	";
		edge1.beforeFirst( );
		edge2.first( );
		while( edge1.next( ) )
		{
			line+= cursor.getObject( "sumTotal1" )+ "		";
		}
		output +="\n" + line;
		
		line = "sumTotal2" + "	";
		edge1.beforeFirst( );
		edge2.first( );
		while( edge1.next( ) )
		{
			line+= cursor.getObject( "sumTotal2" )+ "		";
		}
		output +="\n" + line;
		
		line = "sumSumTotal1" + "	";
		edge1.beforeFirst( );
		edge2.first( );
		while( edge1.next( ) )
		{
			line+= cursor.getObject( "sumSumTotal1" )+ "		";
		}
		output +="\n" + line + "";
		
		return output;
	}
	
	private String getOutputFromCursor( CubeCursor cursor,
			List columnEdgeBindingNames, List rowEdgeBindingNames,
			String measureBindingNames, String columnAggr, String rowAggr,
			String overallAggr ) throws OLAPException
	{
		EdgeCursor edge1 = (EdgeCursor) ( cursor.getOrdinateEdge( ).get( 0 ) );
		EdgeCursor edge2 = (EdgeCursor) ( cursor.getOrdinateEdge( ).get( 1 ) );

		String[] lines = new String[columnEdgeBindingNames.size( )];
		for ( int i = 0; i < columnEdgeBindingNames.size( ); i++ )
		{
			lines[i] = "		";
		}

		while ( edge1.next( ) )
		{
			for ( int i = 0; i < columnEdgeBindingNames.size( ); i++ )
			{
				lines[i] += cursor.getObject( columnEdgeBindingNames.get( i )
						.toString( ) )
						+ "		";
			}
		}

		if ( rowAggr != null )
			lines[lines.length - 1] += "Total";

		String output = "";
		for ( int i = 0; i < lines.length; i++ )
		{
			output += "\n" + lines[i];
		}

		while ( edge2.next( ) )
		{
			String line = "";
			for ( int i = 0; i < rowEdgeBindingNames.size( ); i++ )
			{
				line += cursor.getObject( rowEdgeBindingNames.get( i )
						.toString( ) ).toString( )
						+ "		";
			}
			edge1.beforeFirst( );
			while ( edge1.next( ) )
			{
				line += cursor.getObject( measureBindingNames ) + "		";
			}

			if ( rowAggr != null )
				line += cursor.getObject( rowAggr );
			output += "\n" + line;
		}

		if ( columnAggr != null )
		{
			String line = "Total" + "		";
			edge1.beforeFirst( );
			while ( edge1.next( ) )
			{
				line += cursor.getObject( columnAggr ) + "		";
			}
			if ( overallAggr != null )
				line += cursor.getObject( overallAggr );

			output += "\n" + line;
		}
		
		return output;
	}
}

class Dataset2 implements IDatasetIterator
{

	int ptr = -1;
	static int[] L1Col = {
			1, 1, 2, 2, 3, 3
	};
	static Date[] L2Col = {
			(new java.util.GregorianCalendar(1998, 10 ,13)).getTime(),
			(new java.util.GregorianCalendar(1999, 10 ,14)).getTime(),
			(new java.util.GregorianCalendar(1999, 10 ,11)).getTime(),
			(new java.util.GregorianCalendar(1999, 11 ,1)).getTime(),
			(new java.util.GregorianCalendar(2000, 12 ,8)).getTime(),
			(new java.util.GregorianCalendar(2000, 1 ,9)).getTime()
	};

	public void close( ) throws BirtException
	{
		// TODO Auto-generated method stub

	}

	public Boolean getBoolean( int fieldIndex ) throws BirtException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Date getDate( int fieldIndex ) throws BirtException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Double getDouble( int fieldIndex ) throws BirtException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public int getFieldIndex( String name ) throws BirtException
	{
		if ( name.equals( "l1" ) )
		{
			return 0;
		}
		else if ( name.equals( "l2" ) )
		{
			return 1;
		}
		else if ( name.equals( "m1" ) )
		{
			return 2;
		}
		return -1;
	}

	public int getFieldType( String name ) throws BirtException
	{
		if ( name.equals( "l1" ) )
		{
			return DataType.INTEGER_TYPE;
		}
		else if ( name.equals( "l2" ) )
		{
			return DataType.DATE_TYPE;
		}
		else if ( name.equals( "m1" ) )
		{
			return DataType.INTEGER_TYPE;
		}
		return -1;
	}

	public Integer getInteger( int fieldIndex ) throws BirtException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getString( int fieldIndex ) throws BirtException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object getValue( int fieldIndex ) throws BirtException
	{
		if ( fieldIndex == 0 )
		{
			return new Integer( L1Col[ptr] );
		}
		else if ( fieldIndex == 1 )
		{
			return L2Col[ptr];
		}
		else if ( fieldIndex == 2 )
		{
			return L1Col[ptr] + 1;
		}
		return null;
	}

	public boolean next( ) throws BirtException
	{
		ptr++;
		if ( ptr >= L1Col.length )
		{
			return false;
		}
		return true;
	}
}
