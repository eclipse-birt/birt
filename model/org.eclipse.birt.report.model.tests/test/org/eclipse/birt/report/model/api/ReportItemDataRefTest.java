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

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test ReportItemHandle.
 * 
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testDataSet()}</td>
 * <td>check free-form element which contains attribute data-set</td>
 * <td>dataset name is myDataSet</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>check list element which doesn't contain attribute data-set</td>
 * <td>null</td>
 * </tr>
 * 
 * <tr>
 * <td>testReadVisibilityRules()</td>
 * <td>Gets visibility rules in elements and tests whether values match with
 * those defined the design file.</td>
 * <td>Returned values match with the design file. If "format" values are not
 * defined, the default value "all" is used.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>The number of visibility rules in elements.</td>
 * <td>The number is 2.</td>
 * </tr>
 * 
 * <tr>
 * <td>testWriteVisibilityRules</td>
 * <td>The default format value in the visibility rule.</td>
 * <td>The default value can be written out to the design file.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Sets "format" and "valueExpr" properties of a visibility rule.</td>
 * <td>"format" and "valueExpr" can be written out and the output file matches
 * with the golden file.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testCssProperties()}</td>
 * <td>Tests the special "vertical-align" property.</td>
 * <td>If the property is defined on the row, cell, elements in cells can get
 * the "vertical-align" value.</td>
 * </tr>
 * 
 * </table>
 */

public class ReportItemDataRefTest extends BaseTestCase
{

	/**
	 * Tests the function for adding bound data columns.
	 * 
	 * @throws Exception
	 */

	public void testDataBindingRef( ) throws Exception
	{
		openDesign( "ReportItemHandleTest_2.xml" ); //$NON-NLS-1$ 

		DataItemHandle data1 = (DataItemHandle) designHandle
				.findElement( "myData1" ); //$NON-NLS-1$

		Iterator columns = data1.columnBindingsIterator( );
		ComputedColumnHandle column = (ComputedColumnHandle) columns.next( );
		verifyColumnValues( column );

		DataItemHandle data2 = (DataItemHandle) designHandle
				.findElement( "myData2" ); //$NON-NLS-1$
		columns = data2.columnBindingsIterator( );
		column = (ComputedColumnHandle) columns.next( );
		verifyColumnValues( column );

		assertEquals( "myData1", data2.getDataBindingReferenceName( ) ); //$NON-NLS-1$

		DataItemHandle newData = (DataItemHandle) designHandle
				.findElement( "myData3" ); //$NON-NLS-1$
		newData.setDataBindingReference( data2 );

		columns = newData.columnBindingsIterator( );
		column = (ComputedColumnHandle) columns.next( );
		verifyColumnValues( column );

		try
		{
			newData.setDataBindingReference( newData );
			fail( );
		}
		catch ( SemanticException e )
		{
			assertEquals(
					SemanticError.DESIGN_EXCEPTION_CIRCULAR_ELEMENT_REFERNECE,
					e.getErrorCode( ) );
		}

		try
		{
			data1.setDataBindingReference( data2 );
			fail( );
		}
		catch ( SemanticException e )
		{
			assertEquals(
					SemanticError.DESIGN_EXCEPTION_CIRCULAR_ELEMENT_REFERNECE,
					e.getErrorCode( ) );
		}

		// parameter biding in both data and table, should get value from the
		// table

		Iterator paramBindings = data2.paramBindingsIterator( );
		ParamBindingHandle paramBinding = (ParamBindingHandle) paramBindings
				.next( );
		assertEquals( "table value1", paramBinding.getExpression( ) ); //$NON-NLS-1$

		TableHandle table2 = (TableHandle) designHandle
				.findElement( "myTable2" ); //$NON-NLS-1$
		Iterator filters = table2.filtersIterator( );
		FilterConditionHandle filter = (FilterConditionHandle) filters.next( );
		assertEquals( "table 1 filter expression", filter.getExpr( ) ); //$NON-NLS-1$

		Iterator sorts = table2.sortsIterator( );
		SortKeyHandle sort = (SortKeyHandle) sorts.next( );
		assertEquals( "table 1 name", sort.getKey( ) ); //$NON-NLS-1$
	}

	/**
	 * Tests getDataBindingType() and getAvailableDataBindingReferenceList.
	 * 
	 * @throws Exception
	 */

	public void testgetAvailableDataBindingReferenceList( ) throws Exception
	{
		openDesign( "ReportItemHandleBindingDataTypeTest.xml" ); //$NON-NLS-1$ 

		TextItemHandle text = (TextItemHandle) designHandle
				.findElement( "myText" ); //$NON-NLS-1$
		assertEquals( ReportItemHandle.DATABINDING_TYPE_NONE, text
				.getDataBindingType( ) );

		ListHandle list = (ListHandle) designHandle.findElement( "my list" ); //$NON-NLS-1$
		assertEquals( ReportItemHandle.DATABINDING_TYPE_DATA, list
				.getDataBindingType( ) );

		ExtendedItemHandle extendedItem = (ExtendedItemHandle) designHandle
				.findElement( "ex1" ); //$NON-NLS-1$
		assertEquals( ReportItemHandle.DATABINDING_TYPE_DATA, extendedItem
				.getDataBindingType( ) );

		TableHandle table = (TableHandle) designHandle.findElement( "table" ); //$NON-NLS-1$
		assertEquals( ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF, table
				.getDataBindingType( ) );

		List handleList = list.getAvailableDataBindingReferenceList( );
		assertEquals( 1, handleList.size( ) );
		assertTrue( handleList.get( 0 ) instanceof ExtendedItemHandle );
	}

	private void verifyColumnValues( ComputedColumnHandle column )
	{
		assertEquals( "CUSTOMERNUMBER", column.getName( ) ); //$NON-NLS-1$
		assertEquals( "dataSetRow[\"CUSTOMERNUMBER\"]", column.getExpression( ) ); //$NON-NLS-1$
		assertEquals( DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER, column
				.getDataType( ) );
	}

	/**
	 * Tests the property search algorithm for data groups that have data
	 * binding reference.
	 * 
	 * @throws Exception
	 */

	public void testPropsOfDataGroupRef( ) throws Exception
	{
		openDesign( "DataGroupRef_1.xml" ); //$NON-NLS-1$

		TableHandle table2 = (TableHandle) designHandle
				.findElement( "myTable2" ); //$NON-NLS-1$
		TableGroupHandle group2 = (TableGroupHandle) table2.getGroups( )
				.get( 0 );
		assertEquals( "row[\"CUSTOMERNAME\"]", group2.getKeyExpr( ) ); //$NON-NLS-1$
		assertEquals( "group1", group2.getName( ) ); //$NON-NLS-1$

		Iterator iter1 = group2.filtersIterator( );
		FilterConditionHandle filter = (FilterConditionHandle) iter1.next( );
		assertEquals( "table 1 filter expression", filter.getExpr( ) ); //$NON-NLS-1$
		assertEquals( DesignChoiceConstants.FILTER_OPERATOR_LT, filter
				.getOperator( ) );

		iter1 = group2.sortsIterator( );
		SortKeyHandle sort = (SortKeyHandle) iter1.next( );
		assertEquals( "table 1 name", sort.getKey( ) ); //$NON-NLS-1$
		assertEquals( DesignChoiceConstants.SORT_DIRECTION_ASC, sort
				.getDirection( ) );

		TableHandle table1 = (TableHandle) designHandle
				.findElement( "myTable1" ); //$NON-NLS-1$

		TableGroupHandle group1 = (TableGroupHandle) table1.getGroups( )
				.get( 0 );
		group1.setKeyExpr( "the new expression" ); //$NON-NLS-1$
		assertEquals( "the new expression", group2.getKeyExpr( ) ); //$NON-NLS-1$

	}

	/**
	 * Tests the command to add, remove and move the group element.
	 * 
	 * @throws Exception
	 */

	public void testAddandRemoveDataGroup( ) throws Exception
	{
		openDesign( "DataGroupRef_1.xml" ); //$NON-NLS-1$

		TableHandle table1 = (TableHandle) designHandle
				.findElement( "myTable1" ); //$NON-NLS-1$

		GroupHandle newGroup = designHandle.getElementFactory( )
				.newTableGroup( );
		table1.addElement( newGroup, TableHandle.GROUP_SLOT );

		save( );
		compareFile( "DataGroupAdded_golden.xml" ); //$NON-NLS-1$

		newGroup.drop( );
		save( );
		compareFile( "DataGroupDropped_golden.xml" ); //$NON-NLS-1$

		designHandle.getCommandStack( ).undo( );
		save( );
		compareFile( "DataGroupUndoDrop_golden.xml" ); //$NON-NLS-1$

		newGroup.getContainerSlotHandle( ).shift( newGroup, 0 );
		save( );
		compareFile( "DataGroupShiftPosition_golden.xml" ); //$NON-NLS-1$
	}

	/**
	 * Tests canEdit(), canDrop() methods for the shared data group.
	 * canContain() should be true always.
	 * 
	 * @throws Exception
	 */

	public void testCanMumbleForDataGroup( ) throws Exception
	{
		openDesign( "DataGroupRef_1.xml" ); //$NON-NLS-1$

		TableHandle table2 = (TableHandle) designHandle
				.findElement( "myTable2" ); //$NON-NLS-1$
		TableGroupHandle group2 = (TableGroupHandle) table2.getGroups( )
				.get( 0 );

		assertFalse( group2.canDrop( ) );

		assertTrue( group2.canContain( GroupHandle.HEADER_SLOT,
				ReportDesignConstants.ROW_ELEMENT ) );

		assertFalse( table2.canContain( TableHandle.GROUP_SLOT, designHandle
				.getElementFactory( ).newTableGroup( ) ) );
		
		assertFalse( table2.canContain( TableHandle.GROUP_SLOT, group2
				.getDefn( ).getName( ) ) );
	}

	/**
	 * @throws Exception
	 */

	public void testEstablishDataGroup( ) throws Exception
	{
		openDesign( "DataGroupRef_2.xml" ); //$NON-NLS-1$

		TableHandle table2 = (TableHandle) designHandle
				.findElement( "myTable2" ); //$NON-NLS-1$

		TableHandle table1 = (TableHandle) designHandle
				.findElement( "myTable1" ); //$NON-NLS-1$

		table2.setDataBindingReference( table1 );
		save( );
		compareFile( "SetDataGroupRef_golden.xml" ); //$NON-NLS-1$

		designHandle.getCommandStack( ).undo( );
		save( );
		compareFile( "SetDataGroupRefUndo_golden.xml" ); //$NON-NLS-1$

		designHandle.getCommandStack( ).redo( );
		save( );
		compareFile( "SetDataGroupRefRedo_golden.xml" ); //$NON-NLS-1$

		table2.setDataBindingReference( null );
		save( );
		compareFile( "SetDataGroupRefNull_golden.xml" ); //$NON-NLS-1$

		designHandle.getCommandStack( ).undo( );
		save( );
		compareFile( "SetDataGroupRefNullUndo_golden.xml" ); //$NON-NLS-1$

		table2.setDataBindingReference( designHandle.getElementFactory( )
				.newTableItem( "myTable3" ) ); //$NON-NLS-1$
		save( );
		compareFile( "SetDataGroupRefInvalid_golden.xml" ); //$NON-NLS-1$

	}

	/**
	 * @throws Exception
	 */

	public void testParseInconsistentDataGroup( ) throws Exception
	{
		openDesign( "DataGroupRef_3.xml" ); //$NON-NLS-1$

		// assertEquals( 0, designHandle.getErrorList( ).size( ) );
		assertEquals( 2, designHandle.getWarningList( ).size( ) );

		List warnings = designHandle.getWarningList( );
		assertEquals(
				"The data binding reference of the element Table(\"myTable3\") has different number of groups with element Table(\"myTable1\") it refers to.", //$NON-NLS-1$
				( (ErrorDetail) warnings.get( 0 ) ).getMessage( ) );

		assertEquals(
				"The data binding reference of the element Table(\"myTable2\") has different number of groups with element Table(\"myTable1\") it refers to.", //$NON-NLS-1$
				( (ErrorDetail) warnings.get( 1 ) ).getMessage( ) );

		save( );
		compareFile( "ParseInconsistentDataGroup_golden.xml" ); //$NON-NLS-1$
	}
}