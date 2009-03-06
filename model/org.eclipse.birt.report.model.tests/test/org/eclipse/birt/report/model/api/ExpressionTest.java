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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.elements.structures.SortKey;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.util.BaseTestCase;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * Cases for a new internal data structure Expression and its handle
 * ExpressionHandle.
 * <p>
 * For exporting cases, please see ElementExporterTest.
 * 
 */

public class ExpressionTest extends BaseTestCase
{

	/**
	 * 
	 */

	private static final String INPUT_FILE = "ExpressionTest.xml"; //$NON-NLS-1$

	/**
	 * Test cases:
	 * <ul>
	 * <li>get/set values on expression values for elements.
	 * <li>
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testGetAndSetValuesOnElement( ) throws Exception
	{
		openDesign( INPUT_FILE );

		// ScalarParameter.defaultValue

		ScalarParameterHandle param = (ScalarParameterHandle) designHandle
				.findParameter( "Param1" ); //$NON-NLS-1$
		List<Expression> values = (List<Expression>) param
				.getProperty( ScalarParameterHandle.DEFAULT_VALUE_PROP );

		assertEquals( 3, values.size( ) );
		assertTrue( equals( values.get( 0 ), "value1", ExpressionType.CONSTANT ) ); //$NON-NLS-1$
		assertTrue( equals( values.get( 1 ), "value2", ExpressionType.CONSTANT ) ); //$NON-NLS-1$
		assertTrue( equals( values.get( 2 ), "value3",//$NON-NLS-1$
				ExpressionType.JAVASCRIPT ) );

		values = new ArrayList<Expression>( );
		values.add( new Expression( "value1", ExpressionType.JAVASCRIPT ) );
		values.add( new Expression( "123", ExpressionType.CONSTANT ) );

		param.setProperty( ScalarParameterHandle.DEFAULT_VALUE_PROP, values );
		values = (List<Expression>) param
				.getListProperty( ScalarParameterHandle.DEFAULT_VALUE_PROP );
		assertEquals( 2, values.size( ) );
		assertTrue( equals( values.get( 0 ),
				"value1", ExpressionType.JAVASCRIPT ) ); //$NON-NLS-1$
		assertTrue( equals( values.get( 1 ), "123", ExpressionType.CONSTANT ) ); //$NON-NLS-1$

		// ReportItem.bookmark

		DataItemHandle tmpItem = (DataItemHandle) designHandle
				.findElement( "bodyData" ); //$NON-NLS-1$

		ExpressionHandle exprHandle = tmpItem
				.getExpressionProperty( IReportItemModel.BOOKMARK_PROP );
		assertEquals( "true", exprHandle.getExpression( ) ); //$NON-NLS-1$
		assertEquals( ExpressionType.JAVASCRIPT, exprHandle.getType( ) );

		exprHandle.setExpression( "123" ); //$NON-NLS-1$
		exprHandle.setType( ExpressionType.CONSTANT );

		Expression expr = (Expression) tmpItem.getElement( ).getProperty(
				design, IReportItemModel.BOOKMARK_PROP );
		assertTrue( equals( expr, "123", ExpressionType.CONSTANT ) ); //$NON-NLS-1$

		// ReportItem.onRender

		Object tmpValue = tmpItem
				.getProperty( IReportItemModel.ON_RENDER_METHOD );
		assertTrue( tmpValue instanceof String );
	}

	/**
	 * @param expr
	 * @param expr1
	 * @param type
	 * @return
	 */

	private static boolean equals( Expression expr, Object expr1, String type )
	{
		assert expr != null;

		if ( !ModelUtil.isEquals( expr.getExpression( ), expr1 ) )
			return false;

		if ( !ModelUtil.isEquals( expr.getType( ), type ) )
			return false;

		return true;
	}

	/**
	 * @throws Exception
	 */

	public void testCopy( ) throws Exception
	{
		openDesign( INPUT_FILE );

		ScalarParameterHandle param = (ScalarParameterHandle) designHandle
				.findParameter( "Param1" ); //$NON-NLS-1$

		List<Expression> values = (List<Expression>) param
				.getProperty( ScalarParameterHandle.DEFAULT_VALUE_PROP );

		List<Expression> cloned = (List) ModelUtil.copyValue( param
				.getPropertyDefn( ScalarParameterHandle.DEFAULT_VALUE_PROP ),
				values );

		assertEquals( 3, cloned.size( ) );
		assertTrue( equals( cloned.get( 0 ), "value1", ExpressionType.CONSTANT ) ); //$NON-NLS-1$
		assertTrue( equals( cloned.get( 1 ), "value2", ExpressionType.CONSTANT ) ); //$NON-NLS-1$
		assertTrue( equals( cloned.get( 2 ), "value3",//$NON-NLS-1$
				ExpressionType.JAVASCRIPT ) );

		assertTrue( cloned.get( 0 ) != values.get( 0 ) );
		assertTrue( cloned.get( 1 ) != values.get( 1 ) );
		assertTrue( cloned.get( 2 ) != values.get( 2 ) );
	}

	/**
	 * Tests all get/set methods for the expression value defined on the
	 * structure. Uses SortKey as the example.
	 * 
	 * @throws Exception
	 */

	public void testGetAndSetValuesOnStructure( ) throws Exception
	{
		createDesign( );

		TableHandle table = designHandle.getElementFactory( ).newTableItem(
				"table1" ); //$NON-NLS-1$
		designHandle.getBody( ).add( table );

		PropertyHandle propHandle = table
				.getPropertyHandle( ListingElement.SORT_PROP );

		SortKey sortKey = StructureFactory.createSortKey( );
		sortKey.setProperty( SortKey.KEY_MEMBER, new Expression( "expression",
				ExpressionType.JAVASCRIPT ) );
		propHandle.addItem( sortKey );

		Iterator iter = propHandle.iterator( );
		SortKeyHandle sortHandle = (SortKeyHandle) iter.next( );

		assertEquals( "expression", sortHandle.getKey( ) ); //$NON-NLS-1$
		ExpressionHandle tmpExpr = sortHandle
				.getExprssionProperty( SortKey.KEY_MEMBER );
		assertEquals( "expression", sortHandle.getKey( ) ); //$NON-NLS-1$
		assertEquals( "expression", tmpExpr.getStringExpression( ) ); //$NON-NLS-1$
		assertEquals( ExpressionType.JAVASCRIPT, tmpExpr.getType( ) );

		sortHandle.setProperty( SortKey.KEY_MEMBER, new Expression(
				"new expression", ExpressionType.JAVASCRIPT ) ); //$NON-NLS-1$
		assertEquals( "new expression", sortHandle.getKey( ) ); //$NON-NLS-1$
	}
}
