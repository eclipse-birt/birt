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

package org.eclipse.birt.report.model.api;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.elements.structures.AggregationArgument;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * API test cases for ComputedColumnHandle.
 * 
 */

public class ComputedColumnHandleTest extends BaseTestCase
{

	/**
	 * To test add/remove methods on aggregateOn and argument lists.
	 * 
	 * @throws Exception
	 */

	public void testSimpleListProperties( ) throws Exception
	{
		createDesign( );

		DataItemHandle data = designHandle.getElementFactory( ).newDataItem(
				"data1" ); //$NON-NLS-1$
		designHandle.getBody( ).add( data );

		ComputedColumn column = StructureFactory.createComputedColumn( );
		column.setName( "column 1" ); //$NON-NLS-1$
		column.setExpression( "expression 1" ); //$NON-NLS-1$

		// test clear aggregate on string list on structure

		column.setAggregateOn( "agg 1" ); //$NON-NLS-1$
		column.addAggregateOn( "agg 2" ); //$NON-NLS-1$
		assertEquals( 2, column.getAggregateOnList( ).size( ) );
		column.clearAggregateOnList( );
		assertEquals( 0, column.getAggregateOnList( ).size( ) );

		ComputedColumnHandle columnHandle = data.addColumnBinding( column,
				false );
		AggregationArgument argument = new AggregationArgument( );
		argument.setName( "arg_1" ); //$NON-NLS-1$
		argument.setValue( "argument 1" ); //$NON-NLS-1$

		columnHandle.addArgument( argument );

		Iterator iter = columnHandle.argumentsIterator( );
		assertTrue( iter.hasNext( ) );

		columnHandle.removeArgument( argument );
		iter = columnHandle.argumentsIterator( );
		assertFalse( iter.hasNext( ) );

		columnHandle.addAggregateOn( "group 1" ); //$NON-NLS-1$
		List aggregates = columnHandle.getAggregateOnList( );
		assertEquals( 1, aggregates.size( ) );

		columnHandle.removeAggregateOn( "group 1" ); //$NON-NLS-1$
		aggregates = columnHandle.getAggregateOnList( );
		assertEquals( 0, aggregates.size( ) );

		// test clear aggregate on stirng list on handle

		columnHandle.setAggregateOn( "agg 1" ); //$NON-NLS-1$
		columnHandle.addAggregateOn( "agg 2" ); //$NON-NLS-1$
		assertEquals( 2, columnHandle.getAggregateOnList( ).size( ) );
		columnHandle.clearAggregateOnList( );
		assertEquals( 0, columnHandle.getAggregateOnList( ).size( ) );
	}

	/**
	 * To test add arguments on the ComputedColumn structure.
	 * 
	 * @throws Exception
	 */

	public void testArguments( ) throws Exception
	{
		createDesign( );

		DataItemHandle data = designHandle.getElementFactory( ).newDataItem(
				"data1" ); //$NON-NLS-1$
		designHandle.getBody( ).add( data );

		ComputedColumn column = StructureFactory.createComputedColumn( );
		AggregationArgument argument = StructureFactory
				.createAggregationArgument( );
		argument.setName( "argu1" ); //$NON-NLS-1$
		argument.setValue( "value1" );//$NON-NLS-1$ 
		column.addArgument( argument );

		argument = StructureFactory.createAggregationArgument( );
		argument.setName( "argu2" ); //$NON-NLS-1$
		argument.setValue( "value2" ); //$NON-NLS-1$
		column.addArgument( argument );

		column.setName( "column1" ); //$NON-NLS-1$
		column.setExpression( "expression 1" ); //$NON-NLS-1$

		data.addColumnBinding( column, false );

		column = StructureFactory.createComputedColumn( );
		argument = StructureFactory.createAggregationArgument( );
		argument.setName( "argu3" ); //$NON-NLS-1$
		argument.setValue( "value3" );//$NON-NLS-1$ 
		column.addArgument( argument );

		// the argument is null, exception should be thrown

		argument = StructureFactory.createAggregationArgument( );
		argument.setValue( "value4" ); //$NON-NLS-1$
		column.addArgument( argument );

		column.setName( "column2" ); //$NON-NLS-1$
		column.setExpression( "expression 2" ); //$NON-NLS-1$

		try
		{
			data.addColumnBinding( column, false );
			fail( );
		}
		catch ( PropertyValueException e )
		{
			assertEquals(
					PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED, e
							.getErrorCode( ) );
		}

	}

	/**
	 * For bug 200645, skip choice check in ComputedColumn structure.
	 * 
	 * @throws Exception
	 */

	public void testSkipComputedColumnValidation( ) throws Exception
	{
		openDesign( "ReportItemHandle_ComputedColumn.xml" );//$NON-NLS-1$
		ScalarParameterHandle paramHandle = (ScalarParameterHandle) designHandle
				.findParameter( "MyParam1" );//$NON-NLS-1$
		
		ComputedColumnHandle columnHandle = (ComputedColumnHandle) paramHandle
				.getColumnBindings( ).get( 0 );
		assertEquals( "sum 2", columnHandle.getAggregateFunction( ) );//$NON-NLS-1$

		ComputedColumnHandle columnHandle2 = (ComputedColumnHandle) paramHandle
				.getColumnBindings( ).get( 1 );
		assertEquals( "MAX", columnHandle2.getAggregateFunction( ) );//$NON-NLS-1$

		ComputedColumn column = StructureFactory.createComputedColumn( );
		column.setProperty( ComputedColumn.AGGREGATEON_FUNCTION_MEMBER,
				"count 2" );//$NON-NLS-1$
		column.setName( "column3" );//$NON-NLS-1$

		PropertyHandle propHandle = paramHandle.getColumnBindings( );
		propHandle.addItem( column );

		ComputedColumnHandle columnHandle3 = (ComputedColumnHandle) propHandle
				.get( 2 );
		assertEquals( "count 2", columnHandle3.getAggregateFunction( ) );//$NON-NLS-1$
		
		save();
		assertTrue( compareFile( "ReportItemHandle_ComputedColumn_golden.xml" ) );//$NON-NLS-1$
	}

}
