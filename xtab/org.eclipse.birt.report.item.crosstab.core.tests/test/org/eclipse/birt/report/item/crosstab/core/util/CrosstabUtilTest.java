
package org.eclipse.birt.report.item.crosstab.core.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.item.crosstab.core.BaseTestCase;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;

/**
 * Tests CrosstabUtil method
 * 
 */

public class CrosstabUtilTest extends BaseTestCase
{

	/**
	 * Test addAggregationHeader method.
	 * 
	 * @throws Exception
	 */

	public void testAddAggregationHeader( ) throws Exception
	{
		createDesign( );

		CrosstabReportItemHandle crossReportItem = createSimpleCrosstab( designHandle
				.getModuleHandle( ) );
		// test levelView is null
		assertNull( CrosstabUtil.addAggregationHeader( null, new ArrayList( ),
				new ArrayList( ) ) );

		// test in initial state no aggregation header.
		DimensionViewHandle dimensionHandle = crossReportItem.getDimension(
				ICrosstabConstants.ROW_AXIS_TYPE, 0 );

		LevelViewHandle levelHandle = dimensionHandle.getLevel( 1 );
		assertNull( levelHandle.getAggregationHeader( ) );

		// test inValid parameter
		List testFunctions = new ArrayList( );
		testFunctions.add( "fail test" );//$NON-NLS-1$
		assertNull( CrosstabUtil.addAggregationHeader( levelHandle,
				testFunctions, new ArrayList( ) ) );

		// if it is innermost, do nothing.
		assertNull( CrosstabUtil.addAggregationHeader( levelHandle,
				new ArrayList( ), new ArrayList( ) ) );

		// Prepare measures and function parameter
		List measures = new ArrayList( );
		MeasureViewHandle viewHandle = crossReportItem.getMeasure( 0 );// quantity_price
		measures.add( viewHandle );
		List functions = new ArrayList( );
		functions.add( DesignChoiceConstants.MEASURE_FUNCTION_SUM );

		// Add aggregation header.
		levelHandle = dimensionHandle.getLevel( 0 );
		assertEquals( "CUSTOMER_SEX", levelHandle.getCubeLevelName( ) );//$NON-NLS-1$
		CrosstabCellHandle headerHandle = CrosstabUtil.addAggregationHeader(
				levelHandle, functions, measures );
		assertNotNull( headerHandle );
		assertNotNull( levelHandle.getAggregationHeader( ) );
		AggregationCellHandle cellHandle = viewHandle.getAggregationCell( 0 );

		// check DataItem in aggregation cell.
		DataItemHandle dataItemHandle = (DataItemHandle) cellHandle
				.getContents( ).get( 0 );
		assertNotNull( dataItemHandle );
		assertEquals(
				"QUANTITY_PRICE_CUSTOMER_SEX_PRODUCT_NAME", dataItemHandle.getResultSetColumn( ) );//$NON-NLS-1$

		// Check ResultSetColumn
		ComputedColumnHandle columnHandle = (ComputedColumnHandle) ( (ReportItemHandle) crossReportItem
				.getModelHandle( ) ).getColumnBindings( ).get( 0 );
		assertEquals( DesignChoiceConstants.MEASURE_FUNCTION_SUM, columnHandle
				.getAggregateFunction( ) );
		assertEquals(
				"CUSTOMER_SEX", (String) columnHandle.getAggregateOnList( ).get( 0 ) );//$NON-NLS-1$
		assertEquals(
				"PRODUCT_NAME", (String) columnHandle.getAggregateOnList( ).get( 1 ) );//$NON-NLS-1$

		// Undo,Redo
		designHandle.getCommandStack( ).undo( );
		assertNull( levelHandle.getAggregationHeader( ) );

		designHandle.getCommandStack( ).redo( );
		assertNotNull( levelHandle.getAggregationHeader( ) );

		// remove dimension in column axis type

		crossReportItem
				.removeDimension( ICrosstabConstants.COLUMN_AXIS_TYPE, 0 );
		levelHandle.removeAggregationHeader( );

		// add aggregation to grand total
		headerHandle = CrosstabUtil.addAggregationHeader( levelHandle,
				functions, measures );
		assertNotNull( headerHandle );

		columnHandle = (ComputedColumnHandle) ( (ReportItemHandle) crossReportItem
				.getModelHandle( ) ).getColumnBindings( ).get( 0 );

		assertEquals( DesignChoiceConstants.MEASURE_FUNCTION_SUM, columnHandle
				.getAggregateFunction( ) );
		assertEquals(
				"CUSTOMER_SEX", (String) columnHandle.getAggregateOnList( ).get( 0 ) );//$NON-NLS-1$
		assertEquals(
				"PRODUCT_NAME", (String) columnHandle.getAggregateOnList( ).get( 1 ) );//$NON-NLS-1$

	}

	/**
	 * Tests addGrandTotal method
	 * 
	 * @throws Exception
	 */

	public void testAddGrandTotal( ) throws Exception
	{
		createDesign( );

		CrosstabReportItemHandle crossReportItem = createSimpleCrosstab( designHandle
				.getModuleHandle( ) );
		
		// Prepare measures and function parameter
		List measures = new ArrayList( );

		MeasureViewHandle viewHandle = crossReportItem.getMeasure( 0 );
		measures.add( viewHandle );
		List functions = new ArrayList( );
		functions.add( DesignChoiceConstants.MEASURE_FUNCTION_COUNT );

		//add grand total
		CrosstabCellHandle cellHandle = CrosstabUtil.addGrandTotal(
				crossReportItem, ICrosstabConstants.ROW_AXIS_TYPE, functions,
				measures );
		assertNotNull( cellHandle );

		//check extenditem in grand total property
		CrosstabViewHandle crosstabView = crossReportItem
				.getCrosstabView( ICrosstabConstants.ROW_AXIS_TYPE );
		PropertyHandle propHandle = crosstabView.getGrandTotalProperty( );
		ExtendedItemHandle extendedHandle = (ExtendedItemHandle) propHandle
				.getContent( 0 );
		assertNotNull( extendedHandle );

		//check column binding
		ComputedColumnHandle columnHandle = (ComputedColumnHandle) ( (ReportItemHandle) crossReportItem
				.getModelHandle( ) ).getColumnBindings( ).get( 0 );
		assertEquals( DesignChoiceConstants.MEASURE_FUNCTION_COUNT,
				columnHandle.getAggregateFunction( ) );
		assertNull( (String) columnHandle.getAggregateOnList( ).get( 0 ) );
		assertEquals(
				"PRODUCT_NAME", (String) columnHandle.getAggregateOnList( ).get( 1 ) );//$NON-NLS-1$
	}

	/**
	 * Test getAggregationMeasures method.
	 * 
	 * @throws Exception
	 */

	public void testGetAggregationFunctions( ) throws Exception
	{
		openDesign( "input/CrosstabUtilTest.xml" ); //$NON-NLS-1$

		CrosstabReportItemHandle crossReportItem = (CrosstabReportItemHandle) CrosstabUtil
				.getReportItem( designHandle.getBody( ).get( 0 ) );

		assertNull( CrosstabUtil.getAggregationFunction( null, null ) );

		// test in initial state no aggregation header.
		DimensionViewHandle dimensionHandle = crossReportItem.getDimension(
				ICrosstabConstants.ROW_AXIS_TYPE, 0 );

		LevelViewHandle levelHandle = dimensionHandle.getLevel( 0 );

		// Prepare measures and function parameter
		List measures = new ArrayList( );
		MeasureViewHandle measureViewHandle = crossReportItem.getMeasure( 0 );// quantity_price
		measures.add( measureViewHandle );
		List functions = new ArrayList( );
		functions.add( DesignChoiceConstants.MEASURE_FUNCTION_SUM );

		// Add aggregation header.

		CrosstabUtil.addAggregationHeader( levelHandle, functions, measures );
		assertEquals(
				"sum", CrosstabUtil.getAggregationFunction( levelHandle, measureViewHandle ) ); //$NON-NLS-1$
	}

	/**
	 * Tests getAggregationMeasures method.
	 * 
	 * @throws Exception
	 */

	public void testGetAggregationMeasures( ) throws Exception
	{
		openDesign( "input/CrosstabUtilTest.xml" );//$NON-NLS-1$

		CrosstabReportItemHandle crossReportItem = (CrosstabReportItemHandle) CrosstabUtil
				.getReportItem( designHandle.getBody( ).get( 0 ) );

		// test in initial state no aggregation header.
		DimensionViewHandle dimensionHandle = crossReportItem.getDimension(
				ICrosstabConstants.COLUMN_AXIS_TYPE, 0 );
		LevelViewHandle levelHandle = dimensionHandle.getLevel( 0 );
		MeasureViewHandle measureViewHandle = crossReportItem.getMeasure( 0 );// QUANTITY

		//get Aggregation measure.
		List resultList = CrosstabUtil.getAggregationMeasures( levelHandle );
		
		//check result
		assertEquals( 1, resultList.size( ) );
		measureViewHandle = (MeasureViewHandle) resultList.get( 0 );
		assertEquals( "QUANTITY", measureViewHandle.getCubeMeasureName( ) ); //$NON-NLS-1$

	}

	/**
	 * Tests canContain method.
	 * 
	 * @throws Exception
	 */
	public void testCanContain( ) throws Exception
	{
		createDesign( );

		CrosstabReportItemHandle crossReportItem = createSimpleCrosstab( designHandle
				.getModuleHandle( ) );

		// test canContain dimension
		assertFalse( CrosstabUtil.canContain( crossReportItem,
				(DimensionHandle) null ) );
		// not the same cube
		DimensionHandle dimensionHandle = designHandle.getElementFactory( )
				.newTabularDimension( "test dimension" );//$NON-NLS-1$
		assertFalse( CrosstabUtil.canContain( crossReportItem, dimensionHandle ) );

		// already exist in cube
		DimensionViewHandle dimensionViewHandle = crossReportItem.getDimension(
				ICrosstabConstants.ROW_AXIS_TYPE, 0 );
		assertNotNull( dimensionViewHandle );
		assertFalse( CrosstabUtil.canContain( crossReportItem,
				dimensionViewHandle.getCubeDimension( ) ) );

		// can contain dimension
		CubeHandle cubeHandle = (CubeHandle) designHandle.getCubes( ).get( 0 );
		cubeHandle.add( CubeHandle.DIMENSIONS_PROP, dimensionHandle );
		assertTrue( CrosstabUtil.canContain( crossReportItem, dimensionHandle ) );

		// test canContain measure

		assertFalse( CrosstabUtil.canContain( crossReportItem,
				(MeasureHandle) null ) );

		// test not the same cube
		MeasureHandle measureHandle = designHandle.getElementFactory( )
				.newTabularMeasure( "measure" );//$NON-NLS-1$
		assertFalse( CrosstabUtil.canContain( crossReportItem, measureHandle ) );

		// test exist in cube
		MeasureViewHandle measureViewHandle = crossReportItem.getMeasure( 0 );
		assertFalse( CrosstabUtil.canContain( crossReportItem,
				measureViewHandle.getCubeMeasure( ) ) );

		// test can contain measure
		MeasureGroupHandle groupHandle = (MeasureGroupHandle) cubeHandle
				.getContent( CubeHandle.MEASURE_GROUPS_PROP, 0 );
		groupHandle.add( MeasureGroupHandle.MEASURES_PROP, measureHandle );
		assertTrue( CrosstabUtil.canContain( crossReportItem, measureHandle ) );
	}

}
