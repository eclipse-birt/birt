/*******************************************************************************
 * Copyright (c) 2004 ,2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.olap.cursor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.olap.OLAPException;
import javax.olap.cursor.CubeCursor;
import javax.olap.cursor.DimensionCursor;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.aggregation.BuiltInAggregationFactory;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.impl.CubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.impl.CubeQueryExecutor;
import org.eclipse.birt.data.engine.olap.query.view.BirtCubeView;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;

import testutil.BaseTestCase;

public class CursorModelTest extends BaseTestCase
{
	private Scriptable  scope;

	/*
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );
		new CubeCreator( ).createCube( );
		this.scope = new ImporterTopLevel();

	}

	/**
	 * 
	 * @throws OLAPException
	 * @throws BirtException 
	 */
	public void testCursorModel1( ) throws OLAPException, BirtException
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( CubeCreator.cubeName );

		cqd.createMeasure( "measure1" );
		cqd.createMeasure( "measure2" );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition timeDim = columnEdge.createDimension( "time" );
		IHierarchyDefinition timeHier = timeDim.createHierarchy( "timeHierarchy" );
		timeHier.createLevel( "level21" );

		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IDimensionDefinition geographyDim = rowEdge.createDimension( "geography" );
		IHierarchyDefinition geographyHier = geographyDim.createHierarchy( "geographyHierarchy" );
		geographyHier.createLevel( "level11" );
		geographyHier.createLevel( "level12" );
		geographyHier.createLevel( "level13" );

		IBinding rowGrandTotal = new Binding( "rowGrandTotal" );
		rowGrandTotal.setAggrFunction( BuiltInAggregationFactory.TOTAL_SUM_FUNC );
		rowGrandTotal.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		rowGrandTotal.addAggregateOn( "level21" );

		IBinding columnGrandTotal = new Binding( "columnGrandTotal" );
		columnGrandTotal.setAggrFunction( BuiltInAggregationFactory.TOTAL_AVE_FUNC );
		columnGrandTotal.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		columnGrandTotal.addAggregateOn( "level11" );
		columnGrandTotal.addAggregateOn( "level12" );
		columnGrandTotal.addAggregateOn( "level13" );
		
		IBinding totalGrandTotal = new Binding( "totalGrandTotal" );
		totalGrandTotal.setAggrFunction( BuiltInAggregationFactory.TOTAL_SUM_FUNC );
		totalGrandTotal.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		
		cqd.addBinding( rowGrandTotal );
		cqd.addBinding( columnGrandTotal );
		cqd.addBinding( totalGrandTotal );

		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView( new CubeQueryExecutor(cqd,this.scope,DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION, scope, null, null )) );

		CubeCursor dataCursor = cubeView.getCubeCursor( );

		List columnEdgeBindingNames = new ArrayList();
		columnEdgeBindingNames.add( "level11" );
		columnEdgeBindingNames.add( "level12" );
		columnEdgeBindingNames.add( "level13" );	
		
		List rowEdgeBindingNames = new ArrayList();
		rowEdgeBindingNames.add( "level21" );
		
		List measureBindingNames = new ArrayList( );
		measureBindingNames.add( "measure1" );
		measureBindingNames.add( "measure2" );
		
		List rowGrandTotalNames = new ArrayList();
		rowGrandTotalNames.add( "rowGrandTotal" );
				
		try
		{
			this.printCubeAlongEdge( dataCursor,
					columnEdgeBindingNames,
					rowEdgeBindingNames,
					measureBindingNames,
					rowGrandTotalNames,
					"columnGrandTotal",
					"totalGrandTotal",null );
		}
		catch ( Exception e )
		{
			fail( "fail to get here!" );
		}
	}

	/**
	 * without row edge
	 * @throws OLAPException
	 * @throws BirtException 
	 */
	public void testCursorModel2( ) throws OLAPException, BirtException
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( CubeCreator.cubeName );

		cqd.createMeasure( "measure1" );
		cqd.createMeasure( "measure2" );

		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IDimensionDefinition geographyDim = columnEdge.createDimension( "geography" );
		IHierarchyDefinition geographyHier = geographyDim.createHierarchy( "geographyHierarchy" );
		geographyHier.createLevel( "level11" );
		geographyHier.createLevel( "level12" );
		geographyHier.createLevel( "level13" );

		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView(  new CubeQueryExecutor(cqd,this.scope,DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION, scope, null, null )) );

		CubeCursor dataCursor = cubeView.getCubeCursor( );

		// retrieve the edge cursors
		EdgeCursor columnCursor = cubeView.getColumnEdgeView( ).getEdgeCursor( );

		DimensionCursor countryCursor = (DimensionCursor) columnCursor.getDimensionCursor( )
				.get( 0 );
		DimensionCursor cityCursor = (DimensionCursor) columnCursor.getDimensionCursor( )
				.get( 1 );
		DimensionCursor productCursor = (DimensionCursor) columnCursor.getDimensionCursor( )
				.get( 2 );

		printCubeAlongDimension( dataCursor,
				countryCursor,
				cityCursor,
				productCursor, null );
	}
	
	/**
	 * without column edge
	 * @throws OLAPException
	 * @throws BirtException 
	 */
	public void testCursorModel3( ) throws OLAPException, BirtException
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( CubeCreator.cubeName );

		cqd.createMeasure( "measure1" );
		cqd.createMeasure( "measure2" );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition timeDim = columnEdge.createDimension( "time" );
		IHierarchyDefinition timeHier = timeDim.createHierarchy( "timeHierarchy" );
		timeHier.createLevel( "level21" );

		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView(  new CubeQueryExecutor(cqd,this.scope,DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION, scope, null, null )) );

		CubeCursor dataCursor = cubeView.getCubeCursor( );

		// retrieve the edge cursors
		// EdgeCursor pageCursor = cubeView.getMeasureEdgeView( );
		EdgeCursor rowCursor = cubeView.getRowEdgeView( ).getEdgeCursor( );

		DimensionCursor timeCursor = (DimensionCursor) rowCursor.getDimensionCursor( )
				.get( 0 );

		printCubeAlongDimension( dataCursor, null, null, null, timeCursor );
	}
	
	
	/**
	 * test populate data along dimension cursor
	 */
	public void testCursorModel4( )
	{

		try
		{
			ICubeQueryDefinition cqd = new CubeQueryDefinition( CubeCreator.cubeName );

			cqd.createMeasure( "measure1" );
			cqd.createMeasure( "measure2" );
			IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
			IDimensionDefinition timeDim = columnEdge.createDimension( "time" );
			IHierarchyDefinition timeHier = timeDim.createHierarchy( "timeHierarchy" );
			timeHier.createLevel( "level21" );

			IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
			IDimensionDefinition geographyDim = rowEdge.createDimension( "geography" );
			IHierarchyDefinition geographyHier = geographyDim.createHierarchy( "geographyHierarchy" );
			geographyHier.createLevel( "level11" );
			geographyHier.createLevel( "level12" );
			geographyHier.createLevel( "level13" );

			IBinding rowGrandTotal = new Binding( "rowGrandTotal" );
			rowGrandTotal.setAggrFunction( BuiltInAggregationFactory.TOTAL_SUM_FUNC );
			rowGrandTotal.setExpression( new ScriptExpression( "measure1" ) );
			rowGrandTotal.addAggregateOn( "level21" );

			IBinding columnGrandTotal = new Binding( "columnGrandTotal" );
			columnGrandTotal.setAggrFunction( BuiltInAggregationFactory.TOTAL_SUM_FUNC );
			columnGrandTotal.setExpression( new ScriptExpression( "measure1" ) );
			columnGrandTotal.addAggregateOn( "level11" );
			columnGrandTotal.addAggregateOn( "level12" );
			columnGrandTotal.addAggregateOn( "level13" );

			cqd.addBinding( rowGrandTotal );
			cqd.addBinding( columnGrandTotal );

			// Create cube view.
			BirtCubeView cubeView = new BirtCubeView( new CubeQueryExecutor( cqd,
					this.scope,
					DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
							scope,
							null,
							null ) ) );

			CubeCursor dataCursor = cubeView.getCubeCursor( );

			List columnEdgeBindingNames = new ArrayList( );
			columnEdgeBindingNames.add( "level11" );
			columnEdgeBindingNames.add( "level12" );
			columnEdgeBindingNames.add( "level13" );

			List rowEdgeBindingNames = new ArrayList( );
			rowEdgeBindingNames.add( "level21" );

			List measureBindingNames = new ArrayList( );
			measureBindingNames.add( "measure1" );
			measureBindingNames.add( "measure2" );

			List dimCursorOnColumn = cubeView.getColumnEdgeView( )
					.getEdgeCursor( )
					.getDimensionCursor( );
			List dimCursorOnRow = cubeView.getRowEdgeView( )
					.getEdgeCursor( )
					.getDimensionCursor( );

			this.printCubeAlongDimension( dataCursor,
					(DimensionCursor) dimCursorOnColumn.get( 0 ),
					(DimensionCursor) dimCursorOnColumn.get( 1 ),
					(DimensionCursor) dimCursorOnColumn.get( 2 ),
					(DimensionCursor) dimCursorOnRow.get( 0 ) );
		}
		catch ( Exception e )
		{
			fail( "fail to get here!" );
		}
	}
	
	
	/**
	 * without column edge
	 * @throws OLAPException
	 * @throws BirtException 
	 */
	public void testCursorModel5( ) throws OLAPException, BirtException
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( CubeCreator.cubeName );

		cqd.createMeasure( "measure1" );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition timeDim = columnEdge.createDimension( "time" );
		IHierarchyDefinition timeHier = timeDim.createHierarchy( "timeHierarchy" );
		timeHier.createLevel( "level21" );

		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IDimensionDefinition geographyDim = rowEdge.createDimension( "geography" );
		IHierarchyDefinition geographyHier = geographyDim.createHierarchy( "geographyHierarchy" );
		geographyHier.createLevel( "level11" );
		geographyHier.createLevel( "level12" );
		geographyHier.createLevel( "level13" );

		IBinding rowGrandTotal = new Binding( "rowGrandTotal" );
		rowGrandTotal.setAggrFunction( BuiltInAggregationFactory.TOTAL_SUM_FUNC );
		rowGrandTotal.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		rowGrandTotal.addAggregateOn( "level21" );

		IBinding rowGrandAvg = new Binding( "rowGrandAvg" );
		rowGrandAvg.setAggrFunction( BuiltInAggregationFactory.TOTAL_AVE_FUNC );
		rowGrandAvg.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		rowGrandAvg.addAggregateOn( "level21" );
		
		cqd.addBinding( rowGrandTotal );
		cqd.addBinding( rowGrandAvg );

		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView( new CubeQueryExecutor(cqd,this.scope,DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION, scope, null, null )) );

		CubeCursor dataCursor = cubeView.getCubeCursor( );

		List columnEdgeBindingNames = new ArrayList();
		columnEdgeBindingNames.add( "level11" );
		columnEdgeBindingNames.add( "level12" );
		columnEdgeBindingNames.add( "level13" );	
		
		List rowEdgeBindingNames = new ArrayList();
		rowEdgeBindingNames.add( "level21" );
		
		List measureBindingNames = new ArrayList( );
		measureBindingNames.add( "measure1" );
		
		List rowGrandTotalNames = new ArrayList( );
		rowGrandTotalNames.add( "rowGrandTotal" );
		rowGrandTotalNames.add( "rowGrandAvg" );
		
		try
		{
			this.printCubeAlongEdge( dataCursor,
					columnEdgeBindingNames,
					rowEdgeBindingNames,
					measureBindingNames,
					rowGrandTotalNames,
					null,
					null,
					null);
		}
		catch ( Exception e )
		{
			fail( "fail to get here!" );
		}
	}
	
	
	/**
	 * without column edge
	 * @throws OLAPException
	 * @throws BirtException 
	 */
	public void testCursorOnCountry( ) throws OLAPException, BirtException
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( CubeCreator.cubeName );

		cqd.createMeasure( "measure1" );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition timeDim = columnEdge.createDimension( "time" );
		IHierarchyDefinition timeHier = timeDim.createHierarchy( "timeHierarchy" );
		timeHier.createLevel( "level21" );

		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IDimensionDefinition geographyDim = rowEdge.createDimension( "geography" );
		IHierarchyDefinition geographyHier = geographyDim.createHierarchy( "geographyHierarchy" );
		geographyHier.createLevel( "level11" );
		geographyHier.createLevel( "level12" );
		geographyHier.createLevel( "level13" );

		IBinding rowGrandTotal = new Binding( "countryGrandTotal" );
		rowGrandTotal.setAggrFunction( BuiltInAggregationFactory.TOTAL_SUM_FUNC );
		rowGrandTotal.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		rowGrandTotal.addAggregateOn( "level11" );
		rowGrandTotal.addAggregateOn( "level21" );
		
		cqd.addBinding( rowGrandTotal );

		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView( new CubeQueryExecutor(cqd,this.scope,DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION, scope, null, null )) );

		CubeCursor dataCursor = cubeView.getCubeCursor( );

		List columnEdgeBindingNames = new ArrayList();
		columnEdgeBindingNames.add( "level11" );
		columnEdgeBindingNames.add( "level12" );
		columnEdgeBindingNames.add( "level13" );	
		
		List rowEdgeBindingNames = new ArrayList();
		rowEdgeBindingNames.add( "level21" );
		
		List measureBindingNames = new ArrayList( );
		measureBindingNames.add( "measure1" );
		
		try
		{
			this.printCubeAlongEdge( dataCursor,
					columnEdgeBindingNames,
					rowEdgeBindingNames,
					measureBindingNames,
					null,
					null,
					null,
					"countryGrandTotal");
		}
		catch ( Exception e )
		{
			fail( "fail to get here!" );
		}
	}
	
	private void printCubeAlongEdge( CubeCursor cursor,
			List columnEdgeBindingNames, List rowEdgeBindingNames,
			List measureBindingNames, List rowGrandTotal,
			String columnGrandTotal, String totalGrandTotal,
			String countryGrandTotal ) throws Exception
	{
		EdgeCursor edge1 = (EdgeCursor) ( cursor.getOrdinateEdge( ).get( 0 ) );
		EdgeCursor edge2 = (EdgeCursor) ( cursor.getOrdinateEdge( ).get( 1 ) );

		String[] lines = new String[edge1.getDimensionCursor( ).size( )];
		String result ="";
		for ( int i = 0; i < lines.length; i++ )
		{
			lines[i] = "		";
		}

		while ( edge1.next( ) )
		{
			for ( int i = 0; i < lines.length; i++ )
			{
				DimensionCursor dimCursor = (DimensionCursor) edge1.getDimensionCursor( )
						.get( i );
				lines[i] += dimCursor.getObject( columnEdgeBindingNames.get( i )
						.toString( ) )
						+ "            ";
			}
		}

		String output = "";
		for ( int i = 0; i < lines.length; i++ )
		{
			output += "\n" + lines[i];
		}

		lines = new String[edge2.getDimensionCursor( ).size( )];
		while ( edge2.next( ) )
		{
			for ( int i = 0; i < lines.length; i++ )
			{
				DimensionCursor dimCursor = (DimensionCursor) edge2.getDimensionCursor( )
						.get( i );
				String line = dimCursor.getObject( rowEdgeBindingNames.get( i )
						.toString( ) ).toString( )
						+ "		";
				edge1.beforeFirst( );
				while ( edge1.next( ) )
				{
					DimensionCursor countryCursor = (DimensionCursor) edge1.getDimensionCursor( )
							.get( 0 );
					for ( int j = 0; j < measureBindingNames.size( ); j++ )
					{
						line += cursor.getObject( measureBindingNames.get( j )
								.toString( ) )
								+ ",";
					}
					if ( edge1.getPosition( ) == countryCursor.getEdgeEnd( )
							&& countryGrandTotal != null )
					{
						line += cursor.getObject( countryGrandTotal );
					}
					line += " | ";
				}
				if ( rowGrandTotal != null )
				for ( int j = 0; j < rowGrandTotal.size( ); j++ )
					{
						line += cursor.getObject( rowGrandTotal.get( j )
								.toString( ) ) +"&";
					}
				output += "\n" + line;
			}
		}
		if ( columnGrandTotal != null )
		{
			output += "\n" + columnGrandTotal + "    ";
			edge1.beforeFirst( );
			while ( edge1.next( ) )
			{
				output += cursor.getObject( columnGrandTotal ) + "         ";
			}
		}
		
		if ( totalGrandTotal != null )
			output += cursor.getObject( totalGrandTotal );
		
		System.out.print( output );
		testOut.print( output );
		this.checkOutputFile( );
	}
	

	private void printCubeAlongDimension( CubeCursor dataCursor,
			DimensionCursor countryCursor, DimensionCursor cityCursor,
			DimensionCursor productCursor, DimensionCursor timeCursor )
			throws OLAPException
	{
		String[] lines = new String[3];
		for ( int i = 0; i < lines.length; i++ )
		{
			lines[i] = "		";
		}
		if ( countryCursor != null
				&& cityCursor != null && productCursor != null )
		{
			countryCursor.beforeFirst( );
			while ( countryCursor.next( ) )
			{
				cityCursor.beforeFirst( );
				while ( cityCursor.next( ) )
				{
					productCursor.beforeFirst( );
					while ( productCursor.next( ) )
					{
						lines[0] += countryCursor.getObject( "level11" )
								+ "         ";
						lines[1] += cityCursor.getObject( "level12" ) + "         ";
						lines[2] += productCursor.getObject( "level13" )
								+ "         ";
					}
				}
			}
		}

		String output = "";
		for ( int i = 0; i < lines.length; i++ )
		{
			output += "\n" + lines[i];
		}

		lines = new String[1];
		lines[0] = "";
		if ( timeCursor != null
				&& countryCursor != null && cityCursor != null
				&& productCursor != null )
		{
			timeCursor.beforeFirst( );
			while ( timeCursor.next( ) )
			{
				lines[0] += timeCursor.getObject( "level21" ) + "   ";
				countryCursor.beforeFirst( );
				while ( countryCursor.next( ) )
				{
					cityCursor.beforeFirst( );
					while ( cityCursor.next( ) )
					{

						productCursor.beforeFirst( );
						while ( productCursor.next( ) )
						{
							lines[0] += dataCursor.getObject( "measure1" )
									+ ",";
							lines[0] += dataCursor.getObject( "measure2" ) +"   ";
						}
					}
				}
				lines[0] += "  \n";
			}
		}
		else if ( countryCursor != null
				&& cityCursor != null && productCursor != null )
		{
			countryCursor.beforeFirst( );
			lines[0] += "           ";
			while ( countryCursor.next( ) )
			{			
				cityCursor.beforeFirst( );
				while ( cityCursor.next( ) )
				{

					productCursor.beforeFirst( );
					while ( productCursor.next( ) )
					{
						lines[0] += dataCursor.getObject( "measure1" ) + ",";
						lines[0] += dataCursor.getObject( "measure2" ) + "|";
					}
				}
			}
			lines[0] += "  \n";
		}
		else
		{
			timeCursor.beforeFirst( );

			while ( timeCursor.next( ) )
			{
				lines[0] += timeCursor.getObject( "level21" ) + "  ";

				lines[0] += dataCursor.getObject( "measure1" ) + ",";
				lines[0] += dataCursor.getObject( "measure2" ) +"\n ";
			}
			lines[0] += "  \n";
		}
		output += "\n" + lines[0];
		System.out.print( output );
		testOut.print( output );
		try
		{
			this.checkOutputFile( );
		}
		catch ( IOException e )
		{
			fail( "should not get here" );
		}
	}
}
