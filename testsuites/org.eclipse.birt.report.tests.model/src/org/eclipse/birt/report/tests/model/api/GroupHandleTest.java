/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.api;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Tests GroupHandle.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * <tr>
 * <td>{@link #testProperties()}</td>
 * <td>Tests to read and set properties on a list group.</td>
 * <td>Reads expected values and Values are set correctly.</td>
 * </tr>
 * </table>
 */

public class GroupHandleTest extends BaseTestCase
{

	/**
	 * @param name
	 */
	public GroupHandleTest( String name )
	{
		super( name );
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return
	 */
	public static Test suite( )
	{
		return new TestSuite( GroupHandleTest.class );

	}

	/**
	 * Tests to read and set properties on a GroupElement.
	 * 
	 * @throws Exception
	 *             if errors occur when opens the design file
	 */
	public void setUp( ) throws Exception
	{
		super.setUp( );
		removeResource( );
		copyResource_INPUT( "GroupHandleTest.xml" , "GroupHandleTest.xml" );
	}

	public void tearDown( )
	{
		removeResource( );
	}
	
	public void testProperties( ) throws Exception
	{
		openDesign( "GroupHandleTest.xml" ); //$NON-NLS-1$

		ListHandle list = (ListHandle) designHandle.findElement( "My List" ); //$NON-NLS-1$
		assertNotNull( list );

		// group slot

		SlotHandle groupSlot = list.getGroups( );
		GroupHandle group = (GroupHandle) groupSlot.get( 0 );
		group.setName( "group1" ); //$NON-NLS-1$
		assertEquals( "group1", group //$NON-NLS-1$
				.getDisplayLabel( DesignElement.FULL_LABEL ) );

		assertEquals( "2004/12/12", group.getIntervalBase( ) ); //$NON-NLS-1$

		group.setName( "  " ); //$NON-NLS-1$
		assertEquals( null, group.getDisplayLabel( DesignElement.USER_LABEL ) );

		group.setName( "" ); //$NON-NLS-1$
		assertEquals( null, group.getDisplayLabel( DesignElement.USER_LABEL ) );

		// hideDetail property
		assertFalse( group.hideDetail( ) );
		group.setHideDetail( true );
		assertTrue( group.hideDetail( ) );
		designHandle.getCommandStack( ).undo( );
		assertFalse( group.hideDetail( ) );
		designHandle.getCommandStack( ).redo( );
		assertTrue( group.hideDetail( ) );

		ElementFactory factory = new ElementFactory( designHandle.getModule( ) );
		TableGroupHandle tablegroup = factory.newTableGroup( );
		assertFalse( tablegroup.hideDetail( ) );

		// group filter

		group.setInterval( DesignChoiceConstants.INTERVAL_PREFIX );
		assertEquals( DesignChoiceConstants.INTERVAL_PREFIX, group
				.getInterval( ) );
		group.setIntervalRange( 0.1234 );
		assertTrue( 0.1234 == group.getIntervalRange( ) );

		group.setKeyExpr( "new key expression" ); //$NON-NLS-1$
		assertEquals( "new key expression", group.getKeyExpr( ) ); //$NON-NLS-1$

		group.setSortDirection( DesignChoiceConstants.SORT_DIRECTION_DESC );
		assertEquals( DesignChoiceConstants.SORT_DIRECTION_DESC, group
				.getSortDirection( ) );

		// group.setOnCreate( "new create on the group" ); //$NON-NLS-1$
		group.setOnPrepare( "new prepare on the group" ); //$NON-NLS-1$
		// group.setOnRender( "new render on the group" ); //$NON-NLS-1$
		// assertEquals( "new create on the group", group.getOnCreate( ) );
		// //$NON-NLS-1$
		assertEquals( "new prepare on the group", group.getOnPrepare( ) ); //$NON-NLS-1$
		// assertEquals( "new render on the group", group.getOnRender( ) );
		// //$NON-NLS-1$
	}

	/**
	 * Test case for testing whether the header slot and footer slot is existed
	 * in the group element.
	 * 
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void testHasHeaderAndFooter( ) throws DesignFileException,
			SemanticException
	{

		openDesign( "GroupHandleTest.xml" ); //$NON-NLS-1$
		ListHandle list = (ListHandle) designHandle.findElement( "My List" ); //$NON-NLS-1$
		SlotHandle groupSlot = list.getGroups( );
		GroupHandle group = (GroupHandle) groupSlot.get( 0 );

		assertEquals( true, group.hasHeader( ) );

		group.clearContents( GroupElement.HEADER_SLOT );
		assertEquals( false, group.hasHeader( ) );
		assertEquals( true, group.hasFooter( ) );
		group.clearContents( GroupElement.FOOTER_SLOT );
		assertEquals( false, group.hasHeader( ) );

	}

	/**
	 * refer to bug #161174, support setIntervalRange(String)
	 * 
	 * @throws SemanticException
	 */
	public void testIntervalRange( ) throws SemanticException
	{
		createDesign( );
		ElementFactory factory = designHandle.getElementFactory( );
		TableHandle table = factory.newTableItem( "table" ); //$NON-NLS-1$
		designHandle.getBody( ).add( table );

		TableGroupHandle group = factory.newTableGroup( );
		table.getGroups( ).add( group );

		group.setKeyExpr( "row[\"abc\"]" ); //$NON-NLS-1$
		group.setIntervalRange( "1.234567" ); //$NON-NLS-1$

		assertEquals( "1.234567", group //$NON-NLS-1$
				.getStringProperty( GroupHandle.INTERVAL_RANGE_PROP ) );

		group.setIntervalRange( "1,234.567" ); //$NON-NLS-1$
		assertEquals( "1234.567", group //$NON-NLS-1$
				.getStringProperty( GroupHandle.INTERVAL_RANGE_PROP ) );

		group.setIntervalRange( "1234567E-6" ); //$NON-NLS-1$
		assertEquals( "1.234567", group //$NON-NLS-1$
				.getStringProperty( GroupHandle.INTERVAL_RANGE_PROP ) );

		try
		{
			group.setIntervalRange( "abc" ); //$NON-NLS-1$
		}
		catch ( SemanticException e )
		{
			assertEquals(
					PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
					e.getErrorCode( ) );
		}

		// Interval range is locale-dependent
		ThreadResources.setLocale( ULocale.GERMANY );
		group.setIntervalRange( "1.234567" ); //$NON-NLS-1$
		assertEquals(
				"1234567.0", group.getStringProperty( GroupHandle.INTERVAL_RANGE_PROP ) ); //$NON-NLS-1$

		group.setIntervalRange( "1,234.567" ); //$NON-NLS-1$
		assertEquals( "1.234", group //$NON-NLS-1$
				.getStringProperty( GroupHandle.INTERVAL_RANGE_PROP ) );

		group.setIntervalRange( "1234567E-6" ); //$NON-NLS-1$
		assertEquals(
				"1.234567", group.getStringProperty( GroupHandle.INTERVAL_RANGE_PROP ) ); //$NON-NLS-1$
	}
}