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

import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.SortKeyHandle;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.MultiElementSlot;
import org.eclipse.birt.report.model.elements.FreeForm;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.ListGroup;
import org.eclipse.birt.report.model.elements.ListItem;

/**
 * The test case of <code>ListItem</code> parser and writer.
 * <p>
 * <code>ListGroup</code> is also tested in this test case.
 * 
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testParser()}</td>
 * <td>Test all slots of <code>ListItem</code> after parsing design file
 * </td>
 * <td>All slots are right</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Test all properties of Sorting after parsing design file</td>
 * <td>Sorting is not implemented</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Test all slots of <code>ListGroup</code> after parsing design file
 * </td>
 * <td>All properties are right</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testWriter()}</td>
 * <td>Compare the written file with the golden file</td>
 * <td>Two files are same</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testSemanticCheck()}</td>
 * <td>List is placed in header slot of table item</td>
 * <td>Context containment error found</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Filter has no column, operator, and filter expression.</td>
 * <td>Three value required error found</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>List has no data set in the List/Table container of any level.</td>
 * <td>Missing Data set error found</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGroupNameSemanticCheck()}</td>
 * <td>All the groups names must be unique if the groups share a given named
 * data set.</td>
 * <td>Errors are found.</td>
 * </tr>
 * </table>
 * 
 * 
 * @see ListItem
 */

public class ListItemParseTest extends ParserTestCase
{

	String fileName = "ListItemParseTest.xml"; //$NON-NLS-1$
	String outFileName = "ListItemParseTest_out.xml"; //$NON-NLS-1$
	String goldenFileName = "ListItemParseTest_golden.xml"; //$NON-NLS-1$
	String semanticCheckFileName = "ListItemParseTest_1.xml"; //$NON-NLS-1$

	/*
	 * @see BaseTestCase#setUp()
	 */

	protected void setUp( ) throws Exception
	{
		super.setUp( );
		openDesign( fileName );
	}

	/**
	 * Test the slots of List item.
	 * 
	 * @throws Exception
	 *             if any exception
	 */
	public void testParser( ) throws Exception
	{

		ListItem list = (ListItem) design.findElement( "My List" ); //$NON-NLS-1$
		assertNotNull( list );

		ListHandle listHandle = list.handle( design );
		assertEquals( "my list on create", listHandle.getOnCreate( ) ); //$NON-NLS-1$
		assertEquals( "my list on prepare", listHandle.getOnPrepare( ) ); //$NON-NLS-1$
		assertEquals( "my list on render", listHandle.getOnRender( ) ); //$NON-NLS-1$
		assertEquals( "my list on page break", listHandle.getOnPageBreak( ) ); //$NON-NLS-1$
		
		assertEquals( "Sect", listHandle.getTagType( ) ); //$NON-NLS-1$
		assertEquals( "English", listHandle.getLanguage( ) ); //$NON-NLS-1$
		assertEquals( "Alt Text", listHandle.getAltTextExpression( ).getStringExpression( ) ); //$NON-NLS-1$

		assertEquals( 120, listHandle.getPageBreakInterval( ) );

		// test sorting

		Iterator sortKeys = listHandle.sortsIterator( );

		SortKeyHandle sortKeyHandle = (SortKeyHandle) sortKeys.next( );
		assertEquals( "age", sortKeyHandle.getKey( ) ); //$NON-NLS-1$
		assertEquals( "asc", sortKeyHandle.getDirection( ) ); //$NON-NLS-1$

		sortKeyHandle = (SortKeyHandle) sortKeys.next( );

		assertEquals( "grade", sortKeyHandle.getKey( ) ); //$NON-NLS-1$
		assertEquals( "desc", sortKeyHandle.getDirection( ) ); //$NON-NLS-1$

		assertNull( sortKeys.next( ) );

		// test filter

		Iterator filters = listHandle.filtersIterator( );
		FilterConditionHandle filterHandle = (FilterConditionHandle) filters
				.next( );

		assertEquals( "lt", filterHandle.getOperator( ) ); //$NON-NLS-1$
		assertEquals( "filter expression", filterHandle.getExpr( ) ); //$NON-NLS-1$
		assertEquals( "value1 expression", filterHandle.getValue1( ) ); //$NON-NLS-1$
		assertEquals( "value2 expression", filterHandle.getValue2( ) ); //$NON-NLS-1$

		// header slot

		MultiElementSlot header = (MultiElementSlot) list
				.getSlot( ListItem.HEADER_SLOT );
		FreeForm form = (FreeForm) header.getContent( 0 );
		assertEquals( "Header Section", form.getStringProperty( design, //$NON-NLS-1$
				FreeForm.COMMENTS_PROP ) );

		// detail slot

		MultiElementSlot detail = (MultiElementSlot) list
				.getSlot( ListItem.DETAIL_SLOT );
		form = (FreeForm) detail.getContent( 0 );
		assertEquals( "Detail Section", form.getStringProperty( design, //$NON-NLS-1$
				FreeForm.COMMENTS_PROP ) );

		// header slot

		MultiElementSlot footer = (MultiElementSlot) list
				.getSlot( ListItem.FOOTER_SLOT );
		form = (FreeForm) footer.getContent( 0 );
		assertEquals( "Footer Section", form.getStringProperty( design, //$NON-NLS-1$
				FreeForm.COMMENTS_PROP ) );

		// group slot

		MultiElementSlot groupSlot = (MultiElementSlot) list
				.getSlot( ListItem.GROUP_SLOT );
		ListGroup group = (ListGroup) groupSlot.getContent( 0 );

		// group filter

		ListGroupHandle groupHandle = (ListGroupHandle) group
				.getHandle( design );

		assertEquals( "2005/05/20", groupHandle.getGroupStart( ) ); //$NON-NLS-1$
		assertEquals( "2005 statistics", groupHandle.getTocExpression( ) ); //$NON-NLS-1$
		assertTrue( groupHandle.hideDetail( ) );
		assertFalse( groupHandle.showDetailFilter( ) );

		filters = groupHandle.filtersIterator( );

		filterHandle = (FilterConditionHandle) filters.next( );

		assertEquals( "lt", filterHandle.getOperator( ) ); //$NON-NLS-1$
		assertEquals( "filter expression", filterHandle.getExpr( ) ); //$NON-NLS-1$
		assertEquals( "value1 expression", filterHandle.getValue1( ) ); //$NON-NLS-1$
		assertEquals( "value2 expression", filterHandle.getValue2( ) ); //$NON-NLS-1$

		// group header

		header = (MultiElementSlot) group.getSlot( ListGroup.HEADER_SLOT );
		form = (FreeForm) header.getContent( 0 );
		assertEquals( "Group Header Section", form.getStringProperty( design, //$NON-NLS-1$
				FreeForm.COMMENTS_PROP ) );

		// group footer

		footer = (MultiElementSlot) group.getSlot( ListGroup.FOOTER_SLOT );
		form = (FreeForm) footer.getContent( 0 );
		assertEquals( "Group Footer Section", form.getStringProperty( design, //$NON-NLS-1$
				FreeForm.COMMENTS_PROP ) );

		assertEquals( "week", groupHandle.getInterval( ) ); //$NON-NLS-1$
		assertTrue( 3.0 == groupHandle.getIntervalRange( ) );
		assertEquals( "desc", groupHandle.getSortDirection( ) ); //$NON-NLS-1$
		assertEquals( "complex-sort", groupHandle.getSortType( ) ); //$NON-NLS-1$
		assertEquals( "[Country]", groupHandle.getKeyExpr( ) ); //$NON-NLS-1$
		assertEquals( "group test", groupHandle.getName( ) ); //$NON-NLS-1$
		assertEquals( "create", groupHandle.getOnCreate( ) ); //$NON-NLS-1$
		assertEquals( "prepare", groupHandle.getOnPrepare( ) ); //$NON-NLS-1$
		assertEquals( "render", groupHandle.getOnRender( ) ); //$NON-NLS-1$
		assertEquals( "page break", groupHandle.getOnPageBreak( ) ); //$NON-NLS-1$

		// check sort under group.

		sortKeys = groupHandle.sortsIterator( );

		sortKeyHandle = (SortKeyHandle) sortKeys.next( );

		assertEquals( "name", sortKeyHandle.getKey( ) ); //$NON-NLS-1$
		assertEquals( "asc", sortKeyHandle.getDirection( ) ); //$NON-NLS-1$

		sortKeyHandle = (SortKeyHandle) sortKeys.next( );

		assertEquals( "birthday", sortKeyHandle.getKey( ) ); //$NON-NLS-1$
		assertEquals( "desc", sortKeyHandle.getDirection( ) ); //$NON-NLS-1$

		// test bookmark property in group

		assertEquals( "\"bookmark\"", groupHandle.getBookmark( ) ); //$NON-NLS-1$
		
		ListItem list2 = (ListItem) design.findElement( "My List2" ); //$NON-NLS-1$
		ListHandle list2Handle = list2.handle( design );
		// test default value of Role in Text
		assertEquals( "sect", list2Handle.getTagType( ) ); //$NON-NLS-1$
	}

	/**
	 * Test writer.
	 * 
	 * @throws Exception
	 *             if any exception
	 */

	public void testWriter( ) throws Exception
	{
		ListHandle listHandle = (ListHandle) designHandle
				.findElement( "My List" ); //$NON-NLS-1$
		assertNotNull( listHandle );

		listHandle.setOnPrepare( "new prepare on the list" ); //$NON-NLS-1$
		listHandle.setOnCreate( "new create on the list" ); //$NON-NLS-1$
		listHandle.setOnRender( null );
		listHandle.setPageBreakInterval( 100 );
		listHandle.setOnPageBreak( "new list page break" );//$NON-NLS-1$
		
		listHandle.setTagType( "Sect" ); //$NON-NLS-1$
		listHandle.setLanguage( "English" ); //$NON-NLS-1$
		listHandle.setAltTextExpression( new Expression("Alt Text", ExpressionType.CONSTANT) ); //$NON-NLS-1$

		GroupHandle groupHandle = (GroupHandle) listHandle.getGroups( ).get( 0 );
		groupHandle.setGroupStart( "2006/01/01" ); //$NON-NLS-1$
		groupHandle.setTocExpression( "toc1" ); //$NON-NLS-1$
		groupHandle.setSortType( "none" ); //$NON-NLS-1$
		groupHandle.setOnPageBreak( "new page break" );//$NON-NLS-1$
		groupHandle.setOnCreate( "new create" );//$NON-NLS-1$
		groupHandle.setOnRender( "new render" );//$NON-NLS-1$
		groupHandle.setShowDetailFilter( true );

		assertEquals( "\"bookmark\"", groupHandle.getBookmark( ) ); //$NON-NLS-1$
		groupHandle.setBookmark( "\"newbookmark\"" );//$NON-NLS-1$

		save( );
		assertTrue( compareFile( goldenFileName ) );
	}

	/**
	 * Test semantic check.
	 * 
	 * @throws Exception
	 * 
	 */

	public void testSemanticCheck( ) throws Exception
	{
		openDesign( semanticCheckFileName );

		List errors = design.getErrorList( );

		int i = 0;

		assertEquals( 4, errors.size( ) );

		ErrorDetail error = ( (ErrorDetail) errors.get( i++ ) );
		assertEquals( "My First Table", error.getElement( ).getName( ) ); //$NON-NLS-1$
		assertEquals(
				ContentException.DESIGN_EXCEPTION_INVALID_CONTEXT_CONTAINMENT,
				error.getErrorCode( ) );

		error = ( (ErrorDetail) errors.get( i++ ) );
		assertEquals( "My First List", error.getElement( ).getName( ) ); //$NON-NLS-1$
		assertEquals( PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED,
				error.getErrorCode( ) );

		error = ( (ErrorDetail) errors.get( i++ ) );
		assertEquals( "Second table", error.getElement( ).getName( ) ); //$NON-NLS-1$
		assertEquals( SemanticError.DESIGN_EXCEPTION_MISSING_DATA_SET, error
				.getErrorCode( ) );

		error = ( (ErrorDetail) errors.get( i++ ) );
		assertEquals( "Second inner list", error.getElement( ).getName( ) ); //$NON-NLS-1$
		assertEquals( SemanticError.DESIGN_EXCEPTION_MISSING_DATA_SET, error
				.getErrorCode( ) );
	}

	/**
	 * Test semantic check.
	 * 
	 * @throws Exception
	 * 
	 */

	public void testGroupNameSemanticCheck( ) throws Exception
	{
		openDesign( "ListItemParseTest_2.xml" ); //$NON-NLS-1$

		List<ErrorDetail> errors = design.getErrorList( );

		int i = 0;

		assertEquals( 3, errors.size( ) );

		ErrorDetail error = errors.get( i++ );
		assertEquals( "group 2", //$NON-NLS-1$ 
				error.getElement( ).getLocalProperty( design,
						GroupElement.GROUP_NAME_PROP ) );
		assertEquals( PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED,
				error.getErrorCode( ) );

		error = errors.get( i++ );
		assertEquals( "group 5", //$NON-NLS-1$ 
				error.getElement( ).getLocalProperty( design,
						GroupElement.GROUP_NAME_PROP ) );
		assertEquals( PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED,
				error.getErrorCode( ) );

		error = errors.get( i++ );
		assertEquals( "group 4", //$NON-NLS-1$ 
				error.getElement( ).getLocalProperty( design,
						GroupElement.GROUP_NAME_PROP ) );
		assertEquals( PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED,
				error.getErrorCode( ) );

	}

	/**
	 * Test the compatible pageBreakInterval property in ListingElement.
	 * 
	 * @throws Exception
	 */
	public void testPageBreakInterval( ) throws Exception
	{

		// tests parse list item in version 3.2.16
		openDesign( "ListItemParseTest_3.xml" ); //$NON-NLS-1$

		ListItem list = (ListItem) design.findElement( "My List" ); //$NON-NLS-1$

		ListHandle listHandle = list.handle( design );

		assertEquals( 120, listHandle.getPageBreakInterval( ) );

		ListItem list1 = (ListItem) design.findElement( "My List1" ); //$NON-NLS-1$
		listHandle = list1.handle( design );
		assertEquals( 40, listHandle.getPageBreakInterval( ) );

		// tests parse list item in version 3.2.15
		openDesign( "ListItemParseTest_4.xml" ); //$NON-NLS-1$
		list = (ListItem) design.findElement( "My List" ); //$NON-NLS-1$

		listHandle = list.handle( design );

		assertEquals( 120, listHandle.getPageBreakInterval( ) );

		list1 = (ListItem) design.findElement( "My List1" ); //$NON-NLS-1$
		listHandle = list1.handle( design );
		assertEquals( 40, listHandle.getPageBreakInterval( ) );

	}
}