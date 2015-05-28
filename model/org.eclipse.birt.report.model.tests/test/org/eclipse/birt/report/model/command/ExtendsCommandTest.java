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

package org.eclipse.birt.report.model.command;

import java.io.IOException;
import java.util.List;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.CircularExtendsException;
import org.eclipse.birt.report.model.api.command.ExtendsEvent;
import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.command.ExtendsForbiddenException;
import org.eclipse.birt.report.model.api.command.InvalidParentException;
import org.eclipse.birt.report.model.api.command.WrongTypeException;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.elements.structures.NumberFormatValue;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.ListGroup;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * The unit test code for ExtendsCommand. The following is the case summary.
 * <p>
 * 
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testSetExtendsName()}</td>
 * <td>The name of parent element is null</td>
 * <td>Extends value is null.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>The name of parent element is ""</td>
 * <td>Extends value is null.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Normal case with API call and undo/redo.</td>
 * <td>Pass the test case.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testSetExtendsExceptions()}</td>
 * <td>Parent element does not exist in the name space.</td>
 * <td>Throws an exception with <code>ExtendsException.NOT_FOUND</code>.
 * </td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Parent element is not the right type.</td>
 * <td>Throws an exception with <code>ExtendsException.WRONG_TYPE</code>.
 * </td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Self extends.</td>
 * <td>Throws an exception with <code>ExtendsException.SELF_EXTEND</code>.
 * </td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>The element is non-extendable.</td>
 * <td>Throws an exception with <code>ExtendsException.CANT_EXTEND</code>.
 * </td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testCirclarExtends()}</td>
 * <td>Circlar extends with 2 or 3 elements.</td>
 * <td>Throws an exception with <code>ExtendsException.CIRCULAR</code>.
 * </td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testSetExtendsElement()}</td>
 * <td>Parent is null</td>
 * <td>Extends value is <code>null</code>.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>The name of parent element is null.</td>
 * <td>Extends value is <code>null</code>.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Normal cases with API call and undo/redo.</td>
 * <td>Test case was passed.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testSendNotifications()}</td>
 * <td>Extends Event notified in DIRECT</td>
 * <td>Design Elements must be notified with DeliveryPath: DIRECT.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Extends Event notified in DESCENDENT</td>
 * <td>Derived Elements must be notified with DeliveryPath: DESCENDENT.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Extends Event notified in STYLE_CLIENT</td>
 * <td>Referred Elements must be notified with DeliveryPath: STYLE_CLIENT.
 * </td>
 * </tr>
 * 
 * </table>
 * 
 * <p>
 * 
 * 
 * 
 */

public class ExtendsCommandTest extends BaseTestCase
{

	/**
	 * The DesignElement instance.
	 */
	private DesignElement element;

	/**
	 * ListGroup does not have 'extends' property.
	 */
	private ListGroup lg;

	/**
	 * @see BaseTestCase#setUp()
	 */

	protected void setUp( ) throws Exception
	{
		super.setUp( );

		// import a design from a xml file.

		openDesign( "ExtendsCommandTest.xml" ); //$NON-NLS-1$
		assertNotNull( design );

		element = new Label( "Happy" ); //$NON-NLS-1$
		NameSpace ns = design.getNameHelper( ).getNameSpace(
				( (ElementDefn) element.getDefn( ) ).getNameSpaceID( ) );
		ns.insert( element );

		lg = new ListGroup( );
	}

	/**
	 * Unit test for <code>ExtendsCommand#setExtendsName(String)</code>.
	 * 
	 * Test case:
	 * <ul>
	 * <li>ExtendsName is <code>null</code> <li>ExtendsName is "" <li>Normal
	 * case with API call and undo/redo.
	 * </ul>
	 * 
	 * @throws ExtendsException
	 */

	public void testSetExtendsName( ) throws ExtendsException
	{
		// not set, but it should be null.

		assertNull( element.getExtendsElement( ) );

		// tests ExtendsName with null value.

		element.getHandle( design ).setExtendsName( null );
		assertNull( element.getExtendsElement( ) );

		// tests ExtendsName with empty value.

		element.getHandle( design ).setExtendsName( " " ); //$NON-NLS-1$
		assertNull( element.getExtendsElement( ) );

		/*
		 * tests the normal case from DesignElementHandle.setExtendsName with
		 * undo/redo.
		 */

		DesignElement parent = design.findElement( "base" ); //$NON-NLS-1$

		element.getHandle( design ).setExtendsName( parent.getName( ) );
		assertEquals( "base", //$NON-NLS-1$
				element.getHandle( design ).getExtends( ).getName( ) );

		CommandStack cs = designHandle.getCommandStack( );

		// test undoable and redoable
		assertFalse( cs.canRedo( ) );
		assertTrue( cs.canUndo( ) );

		// make calles from API level with undo/redo.

		cs.undo( );
		assertNull( element.getExtendsElement( ) );
		assertFalse( cs.canUndo( ) );

		assertTrue( cs.canRedo( ) );
		cs.redo( );
		assertFalse( cs.canRedo( ) );
		assertEquals( "base", //$NON-NLS-1$
				element.getHandle( design ).getExtends( ).getName( ) );

		cs.undo( ); // undo again.
		assertNull( element.getExtendsElement( ) );

	}

	/**
	 * Unit test for <code>ExtendsCommand#setExtendsName(String)</code>.
	 * 
	 * <p>
	 * Test case:
	 * <ul>
	 * <li>Parent element does not exist in the namespace.
	 * <li>Parent element is not the right type
	 * <li>Self extends.
	 * <li>The design element does not have the 'extends' property.
	 * </ul>
	 * 
	 */

	public void testSetExtendsExceptions( )
	{
		// 1. Parent element does not exist in the namespace.

		try
		{
			element.getHandle( design ).setExtendsName( "hello" ); //$NON-NLS-1$
			fail( );
		}
		catch ( ExtendsException e )
		{
			assertEquals( e.getErrorCode( ),
					InvalidParentException.DESIGN_EXCEPTION_PARENT_NOT_FOUND );
		}

		// 2. Parent can not be the real father of element, WRONG_TYPE

		DesignElement parent = new Style( );
		parent.setName( "parent_label" ); //$NON-NLS-1$

		NameSpace ns = design.getNameHelper( ).getNameSpace(
				( (ElementDefn) element.getDefn( ) ).getNameSpaceID( ) );
		ns.insert( parent );

		try
		{
			element.getHandle( design ).setExtendsName( parent.getName( ) );
			fail( );
		}
		catch ( ExtendsException e1 )
		{
			assertEquals( e1.getErrorCode( ),
					WrongTypeException.DESIGN_EXCEPTION_WRONG_TYPE );
		}

		// 3. Self extends.

		try
		{
			element.getHandle( design ).setExtendsName( element.getName( ) );

			fail( );
		}
		catch ( ExtendsException e2 )
		{
			assertEquals( e2.getErrorCode( ),
					CircularExtendsException.DESIGN_EXCEPTION_SELF_EXTEND );
		}

		// tests non-extendable element.

		try
		{
			lg.getHandle( design ).setExtendsName( "hello" ); //$NON-NLS-1$
			// cmdListGroup.setExtendsName( "hello" ); //$NON-NLS-1$
			fail( );
		}
		catch ( ExtendsException e )
		{
			assertEquals( e.getErrorCode( ),
					ExtendsForbiddenException.DESIGN_EXCEPTION_CANT_EXTEND );
		}

	}

	/**
	 * Unit test for circlar extends.
	 * 
	 * <p>
	 * 
	 * Test case: Circlar extends with 2 or 3 elements.
	 * 
	 */

	public void testCirclarExtends( )
	{
		Label parent = (Label) design.findElement( "base" ); //$NON-NLS-1$
		assertNotNull( parent );

		Label superParent = (Label) design.findElement( "child1" ); //$NON-NLS-1$
		assertNotNull( superParent );

		Label me = (Label) design.findElement( "child2" ); //$NON-NLS-1$
		assertNotNull( me );

		// "me" already has parent. Set extends is forbidden.

		try
		{
			me.getHandle( design ).setExtendsName( parent.getName( ) );
			parent.getHandle( design ).setExtendsName( me.getName( ) );
		}
		catch ( ExtendsException e1 )
		{
			assertEquals( e1.getErrorCode( ),
					CircularExtendsException.DESIGN_EXCEPTION_CIRCULAR );
		}

		// circular references among three elements

		try
		{
			me.getHandle( design ).setExtendsElement( parent );
			parent.getHandle( design ).setExtendsElement( superParent );
			superParent.getHandle( design ).setExtendsElement( me );

			fail( );
		}
		catch ( ExtendsException e1 )
		{
			assertEquals( e1.getErrorCode( ),
					CircularExtendsException.DESIGN_EXCEPTION_CIRCULAR );
		}
	}

	/**
	 * Unit test for setExtendsElement(DesignElement).
	 * 
	 * <p>
	 * 
	 * <code>setExtendsElement(DesignElement)</code> actually makes a call to
	 * <code>setExtendsName(String)</code>. Hence, ExtendsException issue is not
	 * tested in this method.
	 * 
	 * <p>
	 * Test case:
	 * <ul>
	 * <li>The parent is null.
	 * <li>The parent name is null.
	 * <li>Normal case with API call and redo/undo.
	 * </ul>
	 * 
	 * @throws ExtendsException
	 * @see #testSetExtendsExceptions()
	 */

	public void testSetExtendsElement( ) throws ExtendsException
	{
		// the parent element is null.

		element.getHandle( design ).setExtendsElement( null );
		assertNull( element.getExtendsElement( ) );

		// the parent name is null.

		try
		{
			Style parent = new Style( );

			element.getHandle( design ).setExtendsElement( parent );
			fail( );

		}
		catch ( ExtendsException e )
		{
			assertEquals( e.getErrorCode( ),
					ExtendsException.DESIGN_EXCEPTION_UNNAMED_PARENT );
		}

		assertNull( element.getExtendsElement( ) );

		/*
		 * tests the normal case from DesignElementHandle.setExtendsElement with
		 * undo/redo.
		 */

		Label parent = (Label) design.findElement( "base" ); //$NON-NLS-1$

		element.getHandle( design ).setExtendsElement( parent );

		CommandStack cs = designHandle.getCommandStack( );

		// test undoable and redoable

		assertFalse( cs.canRedo( ) );
		assertTrue( cs.canUndo( ) );

		assertEquals( "base", //$NON-NLS-1$
				element.getHandle( design ).getExtends( ).getName( ) );
		cs.undo( );
		assertNull( element.getExtendsElement( ) ); // cannot undo.

		cs.redo( );
		assertEquals( "base", //$NON-NLS-1$	
				element.getHandle( design ).getExtends( ).getName( ) );
		assertFalse( cs.canRedo( ) ); // cannot redo.

		// virtual element can not set extends property.

		GridHandle grid1 = (GridHandle) designHandle.findElement( "grid1" ); //$NON-NLS-1$
		LabelHandle label = (LabelHandle) ( (CellHandle) ( (RowHandle) grid1
				.getRows( ).get( 0 ) ).getCells( ).get( 0 ) ).getContent( )
				.get( 0 );
		try
		{
			assertTrue( label.getElement( ).isVirtualElement( ) );
			label.setExtends( parent.getHandle( design ) );
		}
		catch ( ExtendsException e )
		{
			assertEquals(
					ExtendsForbiddenException.DESIGN_EXCEPTION_EXTENDS_FORBIDDEN,
					e.getErrorCode( ) );
		}

		// child element can not change extends.

		try
		{
			grid1.setExtends( null );
		}
		catch ( ExtendsException e )
		{
			assertEquals(
					ExtendsForbiddenException.DESIGN_EXCEPTION_EXTENDS_FORBIDDEN,
					e.getErrorCode( ) );
		}

	}

	/**
	 * Test localize element.
	 * 
	 * @throws DesignFileException
	 * @throws IOException
	 * @throws SemanticException
	 */

	public void testLocalizeElement( ) throws DesignFileException, IOException,
			SemanticException
	{
		openDesign( "ExtendsCommandTest2.xml" ); //$NON-NLS-1$
		Module module = designHandle.getModule( );

		// 1. Test localize a simple element (Label)

		LabelHandle label1Handle = (LabelHandle) designHandle
				.findElement( "Label1" ); //$NON-NLS-1$
		Label label1 = (Label) label1Handle.getElement( );

		assertEquals( "baseLabel", label1Handle.getExtends( ).getName( ) ); //$NON-NLS-1$

		// Local property.

		assertEquals(
				"8pt", label1.getLocalProperty( module, Label.WIDTH_PROP ).toString( ) ); //$NON-NLS-1$

		// Defined on parent.

		assertEquals( null, label1.getLocalProperty( module, Label.TEXT_PROP ) );

		// Defined on style3.

		assertEquals( null, label1.getLocalProperty( module,
				Style.FONT_SIZE_PROP ) );
		assertEquals( null, label1.getLocalProperty( module,
				Style.BORDER_LEFT_COLOR_PROP ) );
		assertEquals( null, label1.getLocalProperty( module,
				Style.BACKGROUND_COLOR_PROP ) );

		// Defined on "label" selector

		assertEquals(
				"left", label1.getStringProperty( module, Style.TEXT_ALIGN_PROP ) ); //$NON-NLS-1$

		label1Handle.localize( );

		assertNull( label1Handle.getExtends( ) );
		assertEquals(
				"8pt", label1.getLocalProperty( module, Label.WIDTH_PROP ).toString( ) ); //$NON-NLS-1$

		// Copied from parent.
		assertEquals(
				"First Page", label1.getLocalProperty( module, Label.TEXT_PROP ) ); //$NON-NLS-1$

		// Copied from style3. "BackGroudColor" and "BorderLeftColor" should also extends from parent.

		assertEquals(
				"x-small", label1.getLocalProperty( module, Style.FONT_SIZE_PROP ).toString( ) ); //$NON-NLS-1$
		assertEquals( "black", label1.getLocalProperty( module,
				Style.BORDER_LEFT_COLOR_PROP ) );
		assertEquals( "red", label1.getLocalProperty( module,
				Style.BACKGROUND_COLOR_PROP ) );

		// From selector is not copied.

		assertEquals(
				"left", label1.getLocalProperty( module, Style.TEXT_ALIGN_PROP ) ); //$NON-NLS-1$

		// 2. Test localize a compound element (Grid)

		GridHandle grid1Handle = (GridHandle) designHandle
				.findElement( "Grid1" ); //$NON-NLS-1$
		GridItem grid1 = (GridItem) grid1Handle.getElement( );

		assertEquals( "baseGrid", grid1.getExtendsName( ) ); //$NON-NLS-1$
		assertEquals(
				"24pt", grid1.getLocalProperty( module, GridItem.HEIGHT_PROP ).toString( ) ); //$NON-NLS-1$
		assertEquals(
				"40mm", grid1.getProperty( module, GridItem.WIDTH_PROP ).toString( ) ); //$NON-NLS-1$

		RowHandle innerRow1 = (RowHandle) grid1Handle.getRows( ).get( 0 );
		CellHandle innerCell1 = (CellHandle) innerRow1.getCells( ).get( 0 );

		LabelHandle innerLabel1 = (LabelHandle) innerCell1.getContent( )
				.get( 0 );
		GridHandle innerGrid1 = (GridHandle) innerCell1.getContent( ).get( 2 );

		assertTrue( innerRow1.getElement( ).isVirtualElement( ) );
		assertTrue( innerLabel1.getElement( ).isVirtualElement( ) );
		assertEquals( 9, innerLabel1.getElement( ).getBaseId( ) );

		assertEquals(
				"New Address", innerLabel1.getElement( ).getLocalProperty( module, Label.TEXT_PROP ).toString( ) ); //$NON-NLS-1$

		// defined on "Style1"

		assertEquals( null, innerLabel1.getElement( ).getLocalProperty( module,
				Style.COLOR_PROP ) );
		assertEquals( null, innerLabel1.getElement( ).getLocalProperty( module,
				Style.FONT_SIZE_PROP ) );

		// list property on inner grid item.

		assertEquals( null, innerGrid1.getElement( ).getLocalProperty( module,
				Style.MAP_RULES_PROP ) );
		List mapRules = (List) innerGrid1.getProperty( Style.MAP_RULES_PROP );
		assertEquals( 1, mapRules.size( ) );

		grid1Handle.localize( );

		assertNull( grid1.getExtendsElement( ) );
		assertEquals(
				"24pt", grid1.getLocalProperty( module, GridItem.HEIGHT_PROP ).toString( ) ); //$NON-NLS-1$
		assertEquals(
				"40mm", grid1.getLocalProperty( module, GridItem.WIDTH_PROP ).toString( ) ); //$NON-NLS-1$

		List localMapRules = (List) grid1.getLocalProperty( module,
				Style.MAP_RULES_PROP );
		assertEquals( 2, localMapRules.size( ) );

		Structure numberFormat = (Structure) grid1.getLocalProperty( module,
				Style.NUMBER_FORMAT_PROP );
		assertEquals( "Currency", numberFormat.getProperty( module, //$NON-NLS-1$
				NumberFormatValue.CATEGORY_MEMBER ) );

		save( );

		design.getActivityStack( ).undo( );
		design.getActivityStack( ).undo( );
		save( );
	}

	/**
	 * Tests with notification through <code>ExtendsEvent</code>.
	 * <p>
	 * Test case:
	 * <p>
	 * A special case is used to test the notification mechanism. Please see
	 * source codes for details.
	 * 
	 * <ul>
	 * <li><code>ExtendsEvent</code> with <code>NotifcationEvent.DIRECT</code>
	 * notification.
	 * <li><code>ExtendsEvent</code> with
	 * <code>NotifcationEvent.DESCENDENT</code> notification.
	 * </ul>
	 * 
	 * @throws Exception
	 *             if any exception
	 */

	public void testSendNotifications( ) throws Exception
	{
		element = (Label) design.findElement( "base" ); //$NON-NLS-1$	
		Label tmp = (Label) design.findElement( "tmp" ); //$NON-NLS-1$		
		assertNotNull( element );
		assertNotNull( tmp );

		Label child1 = (Label) design.findElement( "child1" ); //$NON-NLS-1$		
		Label child2 = (Label) design.findElement( "child2" ); //$NON-NLS-1$		
		Label grandchild1 = (Label) design.findElement( "grandchild1" ); //$NON-NLS-1$		
		Label grandchild2 = (Label) design.findElement( "grandchild2" ); //$NON-NLS-1$		
		Label grandchild3 = (Label) design.findElement( "grandchild3" ); //$NON-NLS-1$		
		assertNotNull( child1 );
		assertNotNull( child2 );
		assertNotNull( grandchild1 );
		assertNotNull( grandchild2 );
		assertNotNull( grandchild3 );

		// Tests with extended event and message notification.

		MyExtendsListener listener = new MyExtendsListener( );
		MyExtendsListener listener1 = new MyExtendsListener( );
		MyExtendsListener listener2 = new MyExtendsListener( );
		MyExtendsListener grandListener1 = new MyExtendsListener( );
		MyExtendsListener grandListener2 = new MyExtendsListener( );
		MyExtendsListener grandListener3 = new MyExtendsListener( );

		element.addListener( listener );
		child1.addListener( listener1 );
		child2.addListener( listener2 );
		grandchild1.addListener( grandListener1 );
		grandchild2.addListener( grandListener2 );
		grandchild3.addListener( grandListener3 );

		element.getHandle( design ).setExtendsName( tmp.getName( ) );

		assertEquals( MyExtendsListener.EXTENDED, listener.action );
		assertEquals( NotificationEvent.DIRECT, listener.path );

		assertEquals( MyExtendsListener.EXTENDED, listener1.action );
		assertEquals( NotificationEvent.DESCENDENT, listener1.path );

		assertEquals( MyExtendsListener.EXTENDED, listener2.action );
		assertEquals( NotificationEvent.DESCENDENT, listener2.path );

		assertEquals( MyExtendsListener.EXTENDED, grandListener1.action );
		assertEquals( NotificationEvent.DESCENDENT, grandListener1.path );

		assertEquals( MyExtendsListener.EXTENDED, grandListener2.action );
		assertEquals( NotificationEvent.DESCENDENT, grandListener2.path );

		assertEquals( MyExtendsListener.EXTENDED, grandListener3.action );
		assertEquals( NotificationEvent.DESCENDENT, grandListener3.path );

	}

	// the listener to test the notification.

	class MyExtendsListener implements Listener
	{

		static final int NA = 0;
		static final int EXTENDED = 1;

		int action = NA;
		int path = NotificationEvent.DIRECT;

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.report.model.core.Listener#notify(org.eclipse.birt
		 * .report.model.core.DesignElement,
		 * org.eclipse.birt.report.model.activity.NotificationEvent)
		 */
		public void elementChanged( DesignElementHandle focus,
				NotificationEvent ev )
		{
			ExtendsEvent event = (ExtendsEvent) ev;

			action = EXTENDED;
			path = event.getDeliveryPath( );
		}
	}
}