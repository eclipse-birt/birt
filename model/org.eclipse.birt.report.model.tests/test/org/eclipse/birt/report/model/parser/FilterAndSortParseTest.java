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

package org.eclipse.birt.report.model.parser;

import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.FilterConditionElementHandle;
import org.eclipse.birt.report.model.api.MemberValueHandle;
import org.eclipse.birt.report.model.api.SortElementHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.MemberValue;
import org.eclipse.birt.report.model.elements.interfaces.IMemberValueModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test cases for sort element and filter element.
 */
public class FilterAndSortParseTest extends BaseTestCase
{

	private static final String FILE_NAME = "FilterAndSortParseTest.xml"; //$NON-NLS-1$

	/**
	 * 
	 * @throws Exception
	 */
	public void testParser( ) throws Exception
	{
		openDesign( FILE_NAME );
		DesignElementHandle testTable = designHandle.findElement( "testTable" ); //$NON-NLS-1$
		assertNotNull( testTable );

		// test filter properties
		List valueList = testTable.getListProperty( "filters" ); //$NON-NLS-1$
		assertEquals( 2, valueList.size( ) );
		FilterConditionElementHandle filter = (FilterConditionElementHandle) valueList
				.get( 0 );
		assertEquals( DesignChoiceConstants.FILTER_OPERATOR_LT, filter
				.getOperator( ) );
		assertEquals( "filter expression", filter.getExpr( ) ); //$NON-NLS-1$
		assertEquals( "value1 expression", filter.getValue1( ) ); //$NON-NLS-1$
		assertEquals( "value2 expression", filter.getValue2( ) ); //$NON-NLS-1$
		assertEquals( DesignChoiceConstants.FILTER_TARGET_RESULT_SET, filter
				.getFilterTarget( ) );

		// test member value in filter
		MemberValueHandle memberValue = filter.getMember( );
		assertEquals( "value_1", memberValue.getValue( ) ); //$NON-NLS-1$
		assertEquals( "testDimension/testLevel", memberValue.getCubeLevelName( ) ); //$NON-NLS-1$
		assertNotNull( memberValue.getLevel( ) );
		memberValue = (MemberValueHandle) memberValue.getContent(
				IMemberValueModel.MEMBER_VALUES_PROP, 0 );
		assertEquals( "value_2", memberValue.getValue( ) ); //$NON-NLS-1$
		assertNull( memberValue.getLevel( ) );
		assertNull( memberValue.getCubeLevelName( ) );

		// test sort properties
		valueList = testTable.getListProperty( "sorts" ); //$NON-NLS-1$
		assertEquals( 2, valueList.size( ) );
		SortElementHandle sort = (SortElementHandle) valueList.get( 0 );
		assertEquals( "key_1", sort.getKey( ) ); //$NON-NLS-1$
		assertEquals( DesignChoiceConstants.SORT_DIRECTION_DESC, sort
				.getDirection( ) );

		// test member value in sort
		memberValue = sort.getMember( );
		assertEquals( "value_1", memberValue.getValue( ) ); //$NON-NLS-1$
		assertEquals( "testDimension/testLevel", memberValue.getCubeLevelName( ) ); //$NON-NLS-1$
		assertNotNull( memberValue.getLevel( ) );
		memberValue = (MemberValueHandle) memberValue.getContent(
				IMemberValueModel.MEMBER_VALUES_PROP, 0 );
		assertEquals( "value_2", memberValue.getValue( ) ); //$NON-NLS-1$
		assertNull( memberValue.getLevel( ) );
		assertNull( memberValue.getCubeLevelName( ) );
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testWriter( ) throws Exception
	{
		openDesign( FILE_NAME );
		DesignElementHandle testTable = designHandle.findElement( "testTable" ); //$NON-NLS-1$
		assertNotNull( testTable );

		String valuePrix = "new "; //$NON-NLS-1$

		// test filter properties
		List valueList = testTable.getListProperty( "filters" ); //$NON-NLS-1$
		assertEquals( 2, valueList.size( ) );
		FilterConditionElementHandle filter = (FilterConditionElementHandle) valueList
				.get( 0 );
		filter.setOperator( DesignChoiceConstants.FILTER_OPERATOR_GE );
		filter.setExpr( valuePrix + filter.getExpr( ) );
		filter.setValue1( valuePrix + filter.getValue1( ) );
		filter.setValue2( valuePrix + filter.getValue2( ) );
		filter.setFilterTarget( DesignChoiceConstants.FILTER_TARGET_DATA_SET );

		// test member value in filter
		MemberValueHandle memberValue = filter.getMember( );
		memberValue.setValue( valuePrix + memberValue.getValue( ) );
		memberValue.setLevel( designHandle
				.findLevel( "testDimension/testLevel_one" ) ); //$NON-NLS-1$

		// test sort properties
		valueList = testTable.getListProperty( "sorts" ); //$NON-NLS-1$
		assertEquals( 2, valueList.size( ) );
		SortElementHandle sort = (SortElementHandle) valueList.get( 0 );
		sort.setKey( valuePrix + sort.getKey( ) );
		sort.setDirection( DesignChoiceConstants.SORT_DIRECTION_ASC );

		// test member value in sort
		memberValue = sort.getMember( );
		memberValue.setValue( valuePrix + memberValue.getValue( ) );
		memberValue.setLevel( designHandle
				.findLevel( "testDimension/testLevel_one" ) ); //$NON-NLS-1$

		save( );
		assertTrue( compareFile( "FilterAndSortParseTest_golden.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * MemberValueHandle.add should not throw exception.
	 * 
	 * @throws Exception
	 */

	public void testMemberValue( ) throws Exception
	{
		openDesign( FILE_NAME );
		DesignElementHandle testTable = designHandle.findElement( "testTable" ); //$NON-NLS-1$
		assertNotNull( testTable );

		// test filter properties
		List valueList = testTable.getListProperty( "filters" ); //$NON-NLS-1$
		assertEquals( 2, valueList.size( ) );
		FilterConditionElementHandle filter = (FilterConditionElementHandle) valueList
				.get( 0 );

		// test member value in filter

		MemberValueHandle memberValue = filter.getMember( );

		MemberValueHandle newValue = designHandle.getElementFactory( )
				.newMemberValue( );
		newValue.setLevel( designHandle
				.findLevel( "testDimension/testLevel_one" ) ); //$NON-NLS-1$
		memberValue.add( MemberValueHandle.MEMBER_VALUES_PROP, newValue );
	}
}
