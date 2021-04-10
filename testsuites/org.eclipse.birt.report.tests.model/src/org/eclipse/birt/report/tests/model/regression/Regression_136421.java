/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * The bound column names must be unique within the entire table. Therefore a
 * group and the table cannot share the same bound column name.
 * </p>
 * Test description:
 * <p>
 * Make sure that model will avoid adding a duplicate bound column name within a
 * table, the table has binding name "CUSTOMERNAME", we test add binding with
 * the same name in table, table-row-data and table-group-row-data; Make sure
 * exception will throw at both cases.
 * </p>
 * 
 * @deprecated : Column binding namespace for compound element is changed, only
 *             search itself.
 */
public class Regression_136421 extends BaseTestCase {

	private final static String INPUT = "regression_136421.xml"; //$NON-NLS-1$

	/**
	 * 
	 */
	public void test_regression_136421() {
	}
	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	// public void test_regression_136421( ) throws DesignFileException,
	// SemanticException
	// {
	// openDesign( INPUT );
	// TableHandle table = (TableHandle) designHandle.findElement( "table1" );
	// //$NON-NLS-1$
	//
	// // table already has a binding "CUSTOMERNUMBER"
	//
	// Iterator iter = table.getColumnBindings( ).iterator( );
	// ComputedColumnHandle binding1 = (ComputedColumnHandle) iter.next( );
	// assertEquals( "CUSTOMERNUMBER", binding1.getName( ) ); //$NON-NLS-1$
	//
	// ComputedColumn col = StructureFactory.createComputedColumn( );
	// col.setName( "CUSTOMERNUMBER" ); //$NON-NLS-1$
	// col.setExpression( "dataSetRow[\"CUSTOMERNUMBER\"]" ); //$NON-NLS-1$
	//
	// // 1. add duplicate name on table.
	//
	// try
	// {
	// table.addColumnBinding( col, true );
	// fail( );
	// }
	// catch ( SemanticException e )
	// {
	// System.out.println( e );
	// assertTrue( e instanceof PropertyValueException );
	// }
	//
	// // 2. add duplicate name on table row data
	//
	// DataItemHandle tableRowData = (DataItemHandle) ( (CellHandle) (
	// (RowHandle) table
	// .getDetail( )
	// .get( 0 ) ).getCells( ).get( 0 ) ).getContent( ).get( 0 );
	//
	// try
	// {
	// tableRowData.addColumnBinding( col, true );
	// fail( );
	// }
	// catch ( SemanticException e )
	// {
	// assertTrue( e instanceof PropertyValueException );
	// }
	//
	// // 3. rename an existing data binding, the name duplicate with an
	// // table's binding.
	//
	// try
	// {
	// ComputedColumnHandle binding = (ComputedColumnHandle) tableRowData
	// .getColumnBindings( )
	// .getAt( 0 );
	// binding.setName( "CUSTOMERNUMBER" ); //$NON-NLS-1$
	// fail( );
	// }
	// catch ( SemanticException e )
	// {
	// assertTrue( e instanceof PropertyValueException );
	// }
	//
	// // 4. add duplicate name on table->group->row->data
	//
	// TableGroupHandle group = (TableGroupHandle) table.getGroups( ).get( 0 );
	// DataItemHandle data = (DataItemHandle) ( (CellHandle) ( (RowHandle) group
	// .getHeader( )
	// .get( 0 ) ).getCells( ).get( 0 ) ).getContent( ).get( 0 );
	// try
	// {
	// data.addColumnBinding( col, true );
	// fail( );
	// }
	// catch ( SemanticException e )
	// {
	// assertTrue( e instanceof PropertyValueException );
	// }
	//
	// }
}
