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

package org.eclipse.birt.report.item.crosstab.core.re.executor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.olap.cursor.CubeCursor;
import javax.olap.cursor.EdgeCursor;

import junit.framework.TestCase;

import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.re.DummyCubeCursor;
import org.eclipse.birt.report.item.crosstab.core.re.DummyDimensionCursor;
import org.eclipse.birt.report.item.crosstab.core.re.DummyEdgeCursor;
import org.eclipse.birt.report.item.crosstab.core.re.SimpleMixedEdgeCursor;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabExtendedItemFactory;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.IDesignEngine;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

import com.ibm.icu.util.ULocale;

/**
 * 
 */

public class TestColumnWalker extends TestCase implements ICrosstabConstants
{

	private IDesignEngine engine = null;
	private List crosstabs;

	protected void setUp( ) throws Exception
	{
		super.setUp( );
		ThreadResources.setLocale( ULocale.ENGLISH );

		if ( engine == null )
		{
			engine = new DesignEngine( new DesignConfig( ) );
			MetaDataDictionary.reset( );
			// initialize the metadata.

			engine.getMetaData( );
		}

		SessionHandle sh = engine.newSessionHandle( ULocale.getDefault( ) );
		ReportDesignHandle rdh = sh.createDesign( );
		ModuleHandle mh = rdh.getModuleHandle( );

		crosstabs = new ArrayList( );

		crosstabs.add( createCrosstab1( mh ) );
//		crosstabs.add( createCrosstab2( mh ) );
//		crosstabs.add( createCrosstab3( mh ) );
//		crosstabs.add( createCrosstab4( mh ) );
//		crosstabs.add( createCrosstab5( mh ) );
	}

	public void testColumnWalker( )
	{
		try
		{
			int i = 0;
			for ( Iterator itr = crosstabs.iterator( ); itr.hasNext( ); )
			{
				ColumnWalker cw = new ColumnWalker( (CrosstabReportItemHandle) itr.next( ),
						(EdgeCursor) createCursor( ).getOrdinateEdge( ).get( 0 ) );

				System.out.println( "Start "
						+ i
						+ ":=============================================" );

				while ( cw.hasNext( ) )
				{
					ColumnEvent ce = cw.next( );

					System.out.println( ce );
				}

				System.out.println( "End "
						+ i++
						+ ":=============================================" );
			}
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
		}
	}

	private CrosstabReportItemHandle createCrosstab5( ModuleHandle module )
	{
		try
		{
			CrosstabReportItemHandle crosstabItem = (CrosstabReportItemHandle) CrosstabUtil.getReportItem( CrosstabExtendedItemFactory.createCrosstabReportItem( module,
					null ) );
			// crosstabItem.setMeasureDirection( MEASURE_DIRECTION_VERTICAL );

			crosstabItem.addGrandTotal( COLUMN_AXIS_TYPE );

			crosstabItem.insertDimension( null, ROW_AXIS_TYPE, -1 );
			crosstabItem.insertDimension( null, COLUMN_AXIS_TYPE, -1 );
			crosstabItem.insertDimension( null, COLUMN_AXIS_TYPE, -1 );

			DimensionViewHandle dvh = crosstabItem.getDimension( ROW_AXIS_TYPE,
					0 );
			dvh.insertLevel( null, -1 );
			dvh.insertLevel( null, -1 );

			dvh = crosstabItem.getDimension( COLUMN_AXIS_TYPE, 0 );
			dvh.insertLevel( null, -1 );
			// dvh.insertLevel( null, -1 );
			// dvh.insertLevel( null, -1 );

			dvh.getLevel( 0 ).addAggregationHeader( );
			dvh.getLevel( 0 )
					.setAggregationHeaderLocation( AGGREGATION_HEADER_LOCATION_BEFORE );

			// lvh1.setAggregationHeaderLocation(
			// AGGREGATION_HEADER_LOCATION_AFTER );
			// lvh2.addAggregationHeader( );
			// lvh2.setAggregationHeaderLocation(
			// AGGREGATION_HEADER_LOCATION_AFTER );

			dvh = crosstabItem.getDimension( COLUMN_AXIS_TYPE, 1 );
			dvh.insertLevel( null, -1 );
			dvh.insertLevel( null, -1 );

			dvh.getLevel( 0 ).addAggregationHeader( );
			// dvh.getLevel( 0 ).setAggregationHeaderLocation(
			// AGGREGATION_HEADER_LOCATION_BEFORE );

			crosstabItem.insertMeasure( null, -1 );
			crosstabItem.insertMeasure( null, -1 );
			//
			// MeasureViewHandle mvh = crosstabItem.getMeasure( 0 );
			// mvh.addHeader( );

			return crosstabItem;
		}
		catch ( SemanticException e )
		{
			e.printStackTrace( );
		}

		return null;
	}

	/**
	 * 2 column dimensions, first has 1 level, second has 2 levels, 1 row
	 * dimension with 2 levels, 2 measure, CD1L1 has no total, CD2L1 has total
	 * after, has column grand total, no measure header
	 */
	private CrosstabReportItemHandle createCrosstab4( ModuleHandle module )
	{
		try
		{
			CrosstabReportItemHandle crosstabItem = (CrosstabReportItemHandle) CrosstabUtil.getReportItem( CrosstabExtendedItemFactory.createCrosstabReportItem( module,
					null ) );
			// crosstabItem.setMeasureDirection( MEASURE_DIRECTION_VERTICAL );

			crosstabItem.addGrandTotal( COLUMN_AXIS_TYPE );

			crosstabItem.insertDimension( null, ROW_AXIS_TYPE, -1 );
			crosstabItem.insertDimension( null, COLUMN_AXIS_TYPE, -1 );
			crosstabItem.insertDimension( null, COLUMN_AXIS_TYPE, -1 );

			DimensionViewHandle dvh = crosstabItem.getDimension( ROW_AXIS_TYPE,
					0 );
			dvh.insertLevel( null, -1 );
			dvh.insertLevel( null, -1 );

			dvh = crosstabItem.getDimension( COLUMN_AXIS_TYPE, 0 );
			dvh.insertLevel( null, -1 );

			// dvh.getLevel( 0 ).addAggregationHeader( );

			dvh = crosstabItem.getDimension( COLUMN_AXIS_TYPE, 1 );
			dvh.insertLevel( null, -1 );
			dvh.insertLevel( null, -1 );

			dvh.getLevel( 0 ).addAggregationHeader( );

			crosstabItem.insertMeasure( null, -1 );
			crosstabItem.insertMeasure( null, -1 );

			return crosstabItem;
		}
		catch ( SemanticException e )
		{
			e.printStackTrace( );
		}

		return null;
	}

	/**
	 * 1 column dimension with 3 levels, 1 row dimension with 2 levels, 1
	 * measure, CD1L1 has no total, CD1L2 has total after, has column grand
	 * total and vertical measure header
	 */
	private CrosstabReportItemHandle createCrosstab3( ModuleHandle module )
	{
		try
		{
			CrosstabReportItemHandle crosstabItem = (CrosstabReportItemHandle) CrosstabUtil.getReportItem( CrosstabExtendedItemFactory.createCrosstabReportItem( module,
					null ) );
			crosstabItem.setMeasureDirection( MEASURE_DIRECTION_VERTICAL );

			crosstabItem.addGrandTotal( COLUMN_AXIS_TYPE );

			crosstabItem.insertDimension( null, ROW_AXIS_TYPE, -1 );
			crosstabItem.insertDimension( null, COLUMN_AXIS_TYPE, -1 );
			// crosstabItem.insertDimension( null, COLUMN_AXIS_TYPE, -1 );

			DimensionViewHandle dvh = crosstabItem.getDimension( ROW_AXIS_TYPE,
					0 );
			dvh.insertLevel( null, -1 );
			dvh.insertLevel( null, -1 );

			dvh = crosstabItem.getDimension( COLUMN_AXIS_TYPE, 0 );
			dvh.insertLevel( null, -1 );
			dvh.insertLevel( null, -1 );
			dvh.insertLevel( null, -1 );

			LevelViewHandle lvh1 = dvh.getLevel( 0 );
			LevelViewHandle lvh2 = dvh.getLevel( 1 );

			// dvh.getLevel( 0 ).addAggregationHeader( );
			dvh.getLevel( 1 ).addAggregationHeader( );

			crosstabItem.insertMeasure( null, -1 );

			MeasureViewHandle mvh = crosstabItem.getMeasure( 0 );
			mvh.addHeader( );

			return crosstabItem;
		}
		catch ( SemanticException e )
		{
			e.printStackTrace( );
		}

		return null;
	}

	/**
	 * 2 column dimensions, first has 1 level, second has 2 levels, 1 row
	 * dimension with 2 levels, 2 measure, CD1L1 has total after, CD2L1 has
	 * total after, has column grand total, no measure header
	 */
	private CrosstabReportItemHandle createCrosstab2( ModuleHandle module )
	{
		try
		{
			CrosstabReportItemHandle crosstabItem = (CrosstabReportItemHandle) CrosstabUtil.getReportItem( CrosstabExtendedItemFactory.createCrosstabReportItem( module,
					null ) );
			// crosstabItem.setMeasureDirection( MEASURE_DIRECTION_VERTICAL );

			crosstabItem.addGrandTotal( COLUMN_AXIS_TYPE );

			crosstabItem.insertDimension( null, ROW_AXIS_TYPE, -1 );
			crosstabItem.insertDimension( null, COLUMN_AXIS_TYPE, -1 );
			crosstabItem.insertDimension( null, COLUMN_AXIS_TYPE, -1 );

			DimensionViewHandle dvh = crosstabItem.getDimension( ROW_AXIS_TYPE,
					0 );
			dvh.insertLevel( null, -1 );
			dvh.insertLevel( null, -1 );

			dvh = crosstabItem.getDimension( COLUMN_AXIS_TYPE, 0 );
			dvh.insertLevel( null, -1 );

			LevelViewHandle lvh1 = dvh.getLevel( 0 );

			lvh1.addAggregationHeader( );

			dvh = crosstabItem.getDimension( COLUMN_AXIS_TYPE, 1 );
			dvh.insertLevel( null, -1 );
			dvh.insertLevel( null, -1 );

			dvh.getLevel( 0 ).addAggregationHeader( );

			crosstabItem.insertMeasure( null, -1 );
			crosstabItem.insertMeasure( null, -1 );

			return crosstabItem;
		}
		catch ( SemanticException e )
		{
			e.printStackTrace( );
		}

		return null;
	}

	/**
	 * 1 column dimension with 3 levels, 1 row dimension with 2 levels, 1
	 * measure, CD1L1 has total after, CD1L2 has total after, has column grand
	 * total and vertical measure header
	 */
	private CrosstabReportItemHandle createCrosstab1( ModuleHandle module )
	{
		try
		{
			CrosstabReportItemHandle crosstabItem = (CrosstabReportItemHandle) CrosstabUtil.getReportItem( CrosstabExtendedItemFactory.createCrosstabReportItem( module,
					null ) );
			crosstabItem.setMeasureDirection( MEASURE_DIRECTION_VERTICAL );

			crosstabItem.addGrandTotal( COLUMN_AXIS_TYPE );

			crosstabItem.insertDimension( null, ROW_AXIS_TYPE, -1 );
			crosstabItem.insertDimension( null, COLUMN_AXIS_TYPE, -1 );
			// crosstabItem.insertDimension( null, COLUMN_AXIS_TYPE, -1 );

			DimensionViewHandle dvh = crosstabItem.getDimension( ROW_AXIS_TYPE,
					0 );
			dvh.insertLevel( null, -1 );
			dvh.insertLevel( null, -1 );

			dvh = crosstabItem.getDimension( COLUMN_AXIS_TYPE, 0 );
			dvh.insertLevel( null, -1 );
			dvh.insertLevel( null, -1 );
			dvh.insertLevel( null, -1 );

			LevelViewHandle lvh1 = dvh.getLevel( 0 );
			LevelViewHandle lvh2 = dvh.getLevel( 1 );

			lvh1.addAggregationHeader( );
			lvh2.addAggregationHeader( );

			crosstabItem.insertMeasure( null, -1 );

			MeasureViewHandle mvh = crosstabItem.getMeasure( 0 );
			mvh.addHeader( );

			return crosstabItem;
		}
		catch ( SemanticException e )
		{
			e.printStackTrace( );
		}

		return null;
	}

	private CubeCursor createCursor( )
	{
		DummyDimensionCursor ddc1 = new DummyDimensionCursor( 2 );
		DummyDimensionCursor ddc2 = new DummyDimensionCursor( 2 );
		DummyDimensionCursor ddc3 = new DummyDimensionCursor( 2 );

		DummyEdgeCursor dec = new DummyEdgeCursor( 8 );
		dec.addDimensionCursor( ddc1 );
		dec.addDimensionCursor( ddc2 );
		dec.addDimensionCursor( ddc3 );

		DummyCubeCursor dcc = new DummyCubeCursor( );
		dcc.addOrdinateEdgeCursor( dec );

		// =======================================

		dcc = new DummyCubeCursor( );
		dcc.addOrdinateEdgeCursor( new SimpleMixedEdgeCursor( ) );

		return dcc;
	}

}
