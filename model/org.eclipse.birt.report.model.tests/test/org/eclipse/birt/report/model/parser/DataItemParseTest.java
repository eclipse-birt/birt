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

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.AggregationArgumentHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.elements.DataItem;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * <table border="1" cellpadding="0" cellspacing="0" style="border-collapse:
 * collapse" bordercolor="#111111" width="100%" id="AutoNumber6">
 * 
 * <tr>
 * <td width="33%"><b>Method </b></td>
 * <td width="33%"><b>Test Case </b></td>
 * <td width="34%"><b>Expected Result </b></td>
 * </tr>
 * 
 * <tr>
 * <td width="33%">{@link #testParser()}</td>
 * <td width="33%">Test all properties</td>
 * <td width="34%">the correct value returned.</td>
 * </tr>
 * 
 * <tr>
 * <td width="33%">{@link #testWriter()}</td>
 * <td width="33%">Set new value to properties and save it.</td>
 * <td width="34%">new value should be save into the output file, and output
 * file is same as golden file.</td>
 * </tr>
 * </table>
 * 
 */

public class DataItemParseTest extends BaseTestCase
{

	String fileName = "DataItemParseTest.xml"; //$NON-NLS-1$
	String goldenFileName = "DataItemParseTest_golden.xml"; //$NON-NLS-1$
	private String semanticFile = "DataItemParseTest_1.xml"; //$NON-NLS-1$

	/*
	 * @see BaseTestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );
	}

	/**
	 * Test all properties.
	 * 
	 * @throws Exception
	 *             if opening design file failed.
	 */
	public void testParser( ) throws Exception
	{
		openDesign( fileName );
		DataItem data = (DataItem) design.findElement( "My First Data" ); //$NON-NLS-1$
		DataItemHandle dataHandle = (DataItemHandle) data.getHandle( design );

		// test default value of allowExport
		assertTrue( dataHandle.allowExport( ) );

		assertEquals( "First data value", dataHandle.getResultSetColumn( ) ); //$NON-NLS-1$
		assertNull( dataHandle.getValueExpr( ) );
		assertEquals( "First data value", dataHandle //$NON-NLS-1$
				.getResultSetExpression( ) );
		assertEquals( "data help", dataHandle.getHelpText( ) ); //$NON-NLS-1$
		assertEquals( "help", dataHandle.getHelpTextKey( ) ); //$NON-NLS-1$

		ActionHandle actionHandle = dataHandle.getActionHandle( );
		assertNotNull( actionHandle );
		assertEquals( DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK,
				actionHandle.getLinkType( ) );
		assertEquals( "http://localhost:8080/", actionHandle.getURI( ) ); //$NON-NLS-1$
		
		assertEquals( "p", dataHandle.getTagType( ) ); //$NON-NLS-1$
		assertEquals( "English", dataHandle.getLanguage( ) ); //$NON-NLS-1$
		assertEquals( "Alt Text", dataHandle.getAltTextExpression( ).getStringExpression( ) ); //$NON-NLS-1$
		assertEquals( 1, dataHandle.getOrder( ) ); //$NON-NLS-1$

		dataHandle = (DataItemHandle) designHandle.findElement( "Body Data" ); //$NON-NLS-1$
		// test default value of Role in Data
		assertEquals( "p", dataHandle.getTagType( ) ); //$NON-NLS-1$
				
		// make sure that this data exists in the body slot.

		assertEquals( ReportDesign.BODY_SLOT, dataHandle.getContainer( )
				.findContentSlot( dataHandle ) );

		assertEquals( "column1", dataHandle.getResultSetColumn( ) ); //$NON-NLS-1$
		assertNull( dataHandle.getValueExpr( ) );
		assertEquals( "column1 expr", dataHandle //$NON-NLS-1$
				.getResultSetExpression( ) );
		assertEquals( "data help", dataHandle.getHelpText( ) ); //$NON-NLS-1$
		assertEquals( "help", dataHandle.getHelpTextKey( ) ); //$NON-NLS-1$

		actionHandle = dataHandle.getActionHandle( );
		assertNotNull( actionHandle );
		assertEquals( DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK,
				actionHandle.getLinkType( ) );
		assertEquals( "http://localhost:80/", actionHandle.getURI( ) ); //$NON-NLS-1$

		// reads data binding

		Iterator columnBindings = dataHandle.columnBindingsIterator( );
		ComputedColumnHandle column = (ComputedColumnHandle) columnBindings
				.next( );
		assertEquals( "column1", column.getName( ) ); //$NON-NLS-1$
		assertEquals( "column1 expr", column.getExpression( ) ); //$NON-NLS-1$
		assertEquals( "Display data value", column.getDisplayName( ) );//$NON-NLS-1$
		assertEquals( "ResourceKey.DisplayName", column.getDisplayNameID( ) );//$NON-NLS-1$
		assertEquals( "localized test", column.getDisplayText( ) ); //$NON-NLS-1$
		assertEquals( DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER, column
				.getDataType( ) );
		assertEquals( "column1 aggre1", column.getAggregateOn( ) ); //$NON-NLS-1$
		assertEquals( "column1 aggre1, column1 aggre2", //$NON-NLS-1$
				serializeStringList( column.getAggregateOnList( ) ) );

		Iterator iter = column.argumentsIterator( );
		AggregationArgumentHandle argument = (AggregationArgumentHandle) iter
				.next( );
		assertEquals( "arg_1", argument.getName( ) ); //$NON-NLS-1$
		assertEquals( "argument_value", argument.getValue( ) ); //$NON-NLS-1$
		argument = (AggregationArgumentHandle) iter.next( );
		assertEquals( "arg_2", argument.getName( ) ); //$NON-NLS-1$
		assertEquals( "argument_value", argument.getValue( ) ); //$NON-NLS-1$

		assertEquals( DesignChoiceConstants.MEASURE_FUNCTION_SUM, column
				.getAggregateFunction( ) );
		assertEquals( "colmn1 filter expr", column.getFilterExpression( ) ); //$NON-NLS-1$

		// reads in a data that exists in the components.

		dataHandle = (DataItemHandle) designHandle
				.findElement( "componentsData" ); //$NON-NLS-1$
		assertNull( dataHandle.getValueExpr( ) );
		assertEquals( "Components data value", dataHandle //$NON-NLS-1$
				.getResultSetExpression( ) );

		// reads in a data that exists in the scratch pad.

		dataHandle = (DataItemHandle) designHandle
				.findElement( "scratchpadData" ); //$NON-NLS-1$
		assertNull( dataHandle.getValueExpr( ) );
		assertEquals( "Scratch pad data value", dataHandle //$NON-NLS-1$
				.getResultSetExpression( ) );

		// reads in a data that exists in the graphic master page.

		dataHandle = (DataItemHandle) designHandle
				.findElement( "graphicmasterpageData" ); //$NON-NLS-1$
		assertNull( dataHandle.getValueExpr( ) );
		assertEquals( "Graphic master page data value", dataHandle //$NON-NLS-1$
				.getResultSetExpression( ) );

		// test sharing column bindings.

		dataHandle = (DataItemHandle) designHandle.findElement( "Body Data1" ); //$NON-NLS-1$
		columnBindings = dataHandle.columnBindingsIterator( );
		column = (ComputedColumnHandle) columnBindings.next( );
		assertEquals( "column1", column.getName( ) ); //$NON-NLS-1$
		assertEquals( "column1 expr", column.getExpression( ) ); //$NON-NLS-1$
		assertEquals( "Display data value", column.getDisplayName( ) );//$NON-NLS-1$
		assertEquals( DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER, column
				.getDataType( ) );
		assertEquals( "column1 aggre1", column.getAggregateOn( ) ); //$NON-NLS-1$
		assertEquals( "column1 aggre1, column1 aggre2", //$NON-NLS-1$
				serializeStringList( column.getAggregateOnList( ) ) );
		assertFalse( dataHandle.allowExport( ) );
	}

	/**
	 * This test sets properties, writes the design file and compares it with
	 * golden file.
	 * 
	 * @throws Exception
	 *             if opening or saving design file failed.
	 */
	public void testWriter( ) throws Exception
	{
		openDesign( fileName );
		DataItem data = (DataItem) design.findElement( "My First Data" ); //$NON-NLS-1$
		DataItemHandle dataHandle = (DataItemHandle) data.getHandle( design );

		dataHandle.setHelpTextKey( "New key" ); //$NON-NLS-1$
		dataHandle.setHelpText( "New help" ); //$NON-NLS-1$

		ActionHandle action = dataHandle.getActionHandle( );
		assertNotNull( action );
		
		dataHandle.setTagType( "div new" ); //$NON-NLS-1$
		dataHandle.setLanguage( "English new" ); //$NON-NLS-1$
		dataHandle.setAltTextExpression( new Expression("Alt Text", ExpressionType.CONSTANT) ); //$NON-NLS-1$
		dataHandle.setOrder( 2 ); //$NON-NLS-1$

		dataHandle = (DataItemHandle) designHandle.findElement( "Body Data" ); //$NON-NLS-1$
		dataHandle.setHelpTextKey( "New body help key" ); //$NON-NLS-1$
		dataHandle.setHelpText( "New body help" ); //$NON-NLS-1$

		action = dataHandle.getActionHandle( );
		assertNotNull( action );

		Iterator columnBindings = dataHandle.columnBindingsIterator( );
		ComputedColumnHandle column = (ComputedColumnHandle) columnBindings
				.next( );
		column.setDisplayName( "New Display Name" );//$NON-NLS-1$
		column.setDisplayNameID( "new display name id" ); //$NON-NLS-1$
		column
				.setAggregateFunction( DesignChoiceConstants.MEASURE_FUNCTION_COUNT );

		data = (DataItem) design.findElement( "Body Data" ); //$NON-NLS-1$
		columnBindings = dataHandle.columnBindingsIterator( );
		column = (ComputedColumnHandle) columnBindings.next( );
		AggregationArgumentHandle argumentHandle = (AggregationArgumentHandle) column
				.argumentsIterator( ).next( );
		argumentHandle.setName( "new_" + argumentHandle.getName( ) ); //$NON-NLS-1$
		argumentHandle.setValue( "new_" + argumentHandle.getValue( ) ); //$NON-NLS-1$
			
		dataHandle = (DataItemHandle) designHandle.findElement( "Body Data1" ); //$NON-NLS-1$
		dataHandle.setAllowExport( true );

		save( );

		assertTrue( compareFile( goldenFileName ) );
	}

	/**
	 * Tests the validation on data item. Cases are:
	 * 
	 * <ul>
	 * <li>data has column name defined. However, no column binding associated
	 * with it.
	 * <li>data binding has an non-existed data set defined.
	 * </ul>
	 * 
	 * 
	 * @throws Exception
	 */

	public void testSemanticCheck( ) throws Exception
	{
		openDesign( semanticFile );
		List list = designHandle.getErrorList( );

		assertEquals( 1, list.size( ) );

		ErrorDetail error = (ErrorDetail) list.get( 0 );
		assertEquals( SemanticError.DESIGN_EXCEPTION_MISSING_COLUMN_BINDING,
				error.getErrorCode( ) );

	}

}