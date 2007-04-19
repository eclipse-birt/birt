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

import java.util.List;

import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
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

		ComputedColumnHandle columnHandle = data.addColumnBinding( column,
				false );

		columnHandle.addArgument( "argument 1" ); //$NON-NLS-1$

		List arguments = columnHandle.getArgumentList( );
		assertEquals( 1, arguments.size( ) );

		columnHandle.removeArgument( "argument 1" ); //$NON-NLS-1$
		arguments = columnHandle.getArgumentList( );
		assertEquals( 0, arguments.size( ) );

		columnHandle.addAggregateOn( "group 1" ); //$NON-NLS-1$
		List aggregates = columnHandle.getAggregateOnList( );
		assertEquals( 1, aggregates.size( ) );

		columnHandle.removeAggregateOn( "group 1" ); //$NON-NLS-1$
		aggregates = columnHandle.getAggregateOnList( );
		assertEquals( 0, aggregates.size( ) );

	}
}
