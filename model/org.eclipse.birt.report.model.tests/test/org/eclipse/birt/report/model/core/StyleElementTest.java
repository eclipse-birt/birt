/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.core;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.ExtendsEvent;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * The Test Case of StyleElement.
 *
 * The operation in StyleElement is all about the container-client relationship.
 * And we test the add-remove client functions and the broadcast function to
 * handle notification events.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 *
 * <tr>
 * <td>{@link #testAddAndDropClient}</td>
 * <td>add two clients</td>
 * <td>contain clients</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>drop one client</td>
 * <td>can't find dropped client</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>add one client</td>
 * <td>find added client</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testBroadcast}</td>
 * <td>register listener to style element and its clients</td>
 * <td>they contain the listener</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>broadcast the event</td>
 * <td>the listener registered to style is notified, and those registered to its
 * clients are also notified.</td>
 * </tr>
 *
 * </table>
 *
 */

public class StyleElementTest extends BaseTestCase {

	StyledElement label1;
	StyledElement label2;
	StyleElement style;

	TableHandle table = null;
	LabelHandle label = null;
	ListHandle list1 = null;
	ListHandle list2 = null;
	MyActionListener clientListenerTable = null;
	MyActionListener clientListenerLabel = null;
	MyActionListener clientListenerList1 = null;
	MyActionListener clientListenerList2 = null;
	StyleHandle tableSelector = null;
	StyleHandle listSelector = null;
	SessionHandle sessionHandle = null;

	/*
	 * @see TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		SessionHandle sessionHandle = new DesignEngine(new DesignConfig()).newSessionHandle((ULocale) null);
		designHandle = sessionHandle.createDesign();

		DesignElementHandle handle = designHandle.getElementFactory().newLabel("label1"); //$NON-NLS-1$
		designHandle.getBody().add(handle);
		label1 = (Label) handle.getElement();

		handle = designHandle.getElementFactory().newLabel("label2"); //$NON-NLS-1$
		designHandle.getBody().add(handle);
		label2 = (Label) handle.getElement();

		handle = designHandle.getElementFactory().newStyle("style"); //$NON-NLS-1$
		designHandle.getStyles().add(handle);
		style = (StyleElement) handle.getElement();
	}

	/**
	 * Tests adding client and dropping client.
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>add two clients</li>
	 * <li>drop one client</li>
	 * <li>add one client</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>contain clients</li>
	 * <li>can't find dropped client</li>
	 * <li>find added client</li>
	 * </ul>
	 */
	public void testAddAndDropClient() {
		label1.setStyle(style);
		label2.setStyle(style);
		assertEquals(2, style.getClientList().size());
		assertEquals(label1, ((BackRef) style.getClientList().get(0)).getElement());
		assertEquals(label2, ((BackRef) style.getClientList().get(1)).getElement());

		style.dropClient(label1);
		assertEquals(1, style.getClientList().size());
		assertFalse(style.getClientList().contains(label1));
		assertEquals(style, label1.getStyle());

		style.addClient(label1, (String) null);
		assertEquals(label1, ((BackRef) style.getClientList().get(1)).getElement());

	}

	/**
	 * Test broadcast( NotificationEvent ).
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>register listener to style element and its clients</li>
	 * <li>broadcast the event</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>contain listener</li>
	 * <li>the listener registered to style is notified, and those registered to its
	 * clients are also notified.</li>
	 * </ul>
	 */
	public void testBroadcast() {
		MyActionListener styleListener = new MyActionListener();
		MyActionListener clientListener1 = new MyActionListener();
		MyActionListener clientListener2 = new MyActionListener();

		style.addListener(styleListener);
		label1.addListener(clientListener1);
		label2.addListener(clientListener2);

		assertTrue(CoreTestUtil.getListeners(label1).contains(clientListener1));
		assertTrue(CoreTestUtil.getListeners(label2).contains(clientListener2));

		// Set new style and broadcast

		label1.setStyle(style);
		label2.setStyle(style);

		NotificationEvent ev = new ExtendsEvent(style);
		style.broadcast(ev);

		// Check listeners

		assertTrue(styleListener.done);
		assertEquals(NotificationEvent.DIRECT, styleListener.path);

		assertTrue(clientListener1.done);
		assertEquals(NotificationEvent.STYLE_CLIENT, clientListener1.path);

		assertTrue(clientListener2.done);
		assertEquals(NotificationEvent.STYLE_CLIENT, clientListener2.path);
	}

	/**
	 * Use the style on a table. Drop the styel then the table will recieve the
	 * style dropped event.
	 *
	 * @throws SemanticException
	 */

	public void testBroadcastAfterDropStyle() throws SemanticException {

		TableHandle table = designHandle.getElementFactory().newTableItem("table"); //$NON-NLS-1$

		SharedStyleHandle myStyle = designHandle.getElementFactory().newStyle("myStyle"); //$NON-NLS-1$
		designHandle.getBody().add(table);
		designHandle.getStyles().add(myStyle);
		table.setStyle(myStyle);

		MyActionListener styleListener = new MyActionListener();
		table.addListener(styleListener);

		assertFalse(styleListener.done);
		designHandle.getStyles().dropAndClear(myStyle);
		assertTrue(styleListener.done);
	}

	/**
	 * Tests the broadcast when a selector style changes.
	 *
	 * @throws Exception if any exception
	 */

	public void testBroadcastFromSimpleSelectorStyle() throws Exception {

		style.setName("label"); //$NON-NLS-1$

		MyActionListener clientListener1 = new MyActionListener();
		label1.addListener(clientListener1);

		style.getHandle(design).setProperty(Style.BACKGROUND_COLOR_PROP, "red"); //$NON-NLS-1$
		assertTrue(clientListener1.done);
		assertEquals(NotificationEvent.STYLE_CLIENT, clientListener1.path);
	}

	/**
	 * Tests the broadcast when a selector style representing container/slot
	 * selector changes.
	 *
	 * @throws Exception if any exception
	 */

	public void testBroadcastFromContainerSlotSelectorStyle() throws Exception {
		designHandle.getStyles().dropAndClear(style.getHandle(designHandle.getModule()));
		designHandle.getBody().dropAndClear(label1.getHandle(designHandle.getModule()));

		style.setName("list-header"); //$NON-NLS-1$

		ListHandle listHandle = designHandle.getElementFactory().newList("list1"); //$NON-NLS-1$

		designHandle.getBody().add(listHandle);
		designHandle.getStyles().add(style.getHandle(design));
		listHandle.getHeader().add(label1.getHandle(design));

		MyActionListener clientListener1 = new MyActionListener();
		label1.addListener(clientListener1);

		style.getHandle(design).setProperty(Style.COLOR_PROP, "red"); //$NON-NLS-1$
		assertTrue(clientListener1.done);
		assertEquals(NotificationEvent.STYLE_CLIENT, clientListener1.path);
	}

	// the group style selectors are not supported by R1.
	// /**
	// * Tests the broadcast when a selector style representing container/slot
	// * indexed selector changes.
	// *
	// * @throws Exception
	// * if any exception
	// */
	//
	// public void testBroadcastFromContainerSlotSelectorStyleWithIndex( )
	// throws Exception
	// {
	//
	// style.setName( "list-group-header-2" ); //$NON-NLS-1$
	// designHandle.getBody( ).dropAndClear(
	// label1.getHandle( (ReportDesign) designHandle.getElement( ) ) );
	// ListHandle listHandle = designHandle.getElementFactory( ).newList(
	// "list1" ); //$NON-NLS-1$
	//
	// designHandle.getBody( ).add( listHandle );
	//
	// GroupHandle group1 = listHandle.getElementFactory( ).newListGroup( );
	// GroupHandle group2 = listHandle.getElementFactory( ).newListGroup( );
	//
	// listHandle.getGroups( ).add( group1 );
	// listHandle.getGroups( ).add( group2 );
	//
	// group2.getSlot( ListGroup.HEADER_SLOT )
	// .add( label1.getHandle( design ) );
	//
	// MyActionListener clientListener1 = new MyActionListener( );
	// label1.addListener( clientListener1 );
	//
	// style.getHandle( design ).setProperty( Style.COLOR_PROP, "red" );
	// //$NON-NLS-1$
	// assertTrue( clientListener1.done );
	// assertEquals( NotificationEvent.STYLE_CLIENT, clientListener1.path );
	// }
	private StyleHandle reportSelector = null;

	private void prepareForSelectorBroadCastTest() throws ContentException, NameException {

		sessionHandle = new DesignEngine(new DesignConfig()).newSessionHandle((ULocale) null);
		designHandle = sessionHandle.createDesign();

		tableSelector = designHandle.getElementFactory().newStyle("table"); //$NON-NLS-1$
		listSelector = designHandle.getElementFactory().newStyle("list"); //$NON-NLS-1$
		reportSelector = designHandle.getElementFactory().newStyle("report"); //$NON-NLS-1$

		designHandle.getStyles().add(reportSelector);
		designHandle.getStyles().add(tableSelector);
		designHandle.getStyles().add(listSelector);

		table = designHandle.getElementFactory().newTableItem("my Tabel"); //$NON-NLS-1$
		label = designHandle.getElementFactory().newLabel("My Label"); //$NON-NLS-1$
		RowHandle row = designHandle.getElementFactory().newTableRow();
		CellHandle cell = designHandle.getElementFactory().newCell();

		designHandle.getBody().add(table);
		table.getDetail().add(row);
		row.getCells().add(cell);
		cell.getContent().add(label);

		list1 = designHandle.getElementFactory().newList("list1"); //$NON-NLS-1$
		list2 = designHandle.getElementFactory().newList("list2"); //$NON-NLS-1$

		cell.getContent().add(list1);
		list1.getDetail().add(list2);

		clientListenerTable = new MyActionListener();
		table.addListener(clientListenerTable);

		clientListenerLabel = new MyActionListener();
		label.addListener(clientListenerLabel);

		clientListenerList1 = new MyActionListener();
		list1.addListener(clientListenerList1);

		clientListenerList2 = new MyActionListener();
		list2.addListener(clientListenerList2);

	}

	/**
	 * test after add a selector style into design, the top level elements which use
	 * the style will receive a notifications. But those elements under the top one
	 * will not.
	 *
	 * @throws SemanticException
	 */

	public void testBroadcastAfterAddSelector() throws SemanticException {

		prepareForSelectorBroadCastTest();

		designHandle.getStyles().dropAndClear(tableSelector);
		designHandle.getStyles().dropAndClear(listSelector);

		table.removeListener(clientListenerTable);
		label.removeListener(clientListenerLabel);
		list1.removeListener(clientListenerList1);
		list2.removeListener(clientListenerList2);

		clientListenerTable = new MyActionListener();
		clientListenerLabel = new MyActionListener();
		clientListenerList1 = new MyActionListener();
		clientListenerList2 = new MyActionListener();

		table.addListener(clientListenerTable);
		label.addListener(clientListenerLabel);
		list1.addListener(clientListenerList1);
		list2.addListener(clientListenerList2);

		designHandle.getStyles().add(tableSelector);

		assertTrue(clientListenerTable.done);
		assertEquals(NotificationEvent.STYLE_CLIENT, clientListenerTable.path);
		assertFalse(clientListenerLabel.done);

		designHandle.getStyles().add(listSelector);

		assertTrue(clientListenerList1.done);
		assertEquals(NotificationEvent.STYLE_CLIENT, clientListenerList1.path);
		assertFalse(clientListenerList2.done);
	}

	/**
	 * test after dropping a selector style, the top level element will receive the
	 * notification.
	 *
	 * @throws SemanticException
	 */
	public void testBroadcastAfterDropSelector() throws SemanticException {

		prepareForSelectorBroadCastTest();

		assertFalse(clientListenerTable.done);

		designHandle.getStyles().dropAndClear(tableSelector);

		assertTrue(clientListenerTable.done);
		assertEquals(2, clientListenerTable.path);
		assertFalse(clientListenerLabel.done);

		assertFalse(clientListenerList1.done);
		designHandle.getStyles().dropAndClear(listSelector);

		assertTrue(clientListenerList1.done);
		assertEquals(2, clientListenerList1.path);
		assertFalse(clientListenerList2.done);

	}

	/**
	 * test after renaming a selector style, the top level element which uses the
	 * style will receive notification.
	 *
	 * @throws ContentException
	 * @throws NameException
	 */

	public void testBroadcastAfterRenameSelector() throws ContentException, NameException {

		// test when the selector style is renamed, the top element will receive
		// notification

		prepareForSelectorBroadCastTest();
		assertFalse(clientListenerTable.done);

		tableSelector.setName("table-style"); //$NON-NLS-1$

		assertTrue(clientListenerTable.done);
		assertEquals(NotificationEvent.STYLE_CLIENT, clientListenerTable.path);
		assertFalse(clientListenerLabel.done);

		listSelector.setName("list-style"); //$NON-NLS-1$

		assertTrue(clientListenerList1.done);
		assertEquals(NotificationEvent.STYLE_CLIENT, clientListenerList1.path);
		assertFalse(clientListenerList2.done);

	}

	/**
	 * test after modifing the property of a selector style, the top level element
	 * which uses this style will receive a notification.
	 *
	 * @throws SemanticException
	 */

	public void testBroadcastAfterSelectorPropertyChanged() throws SemanticException {

		// set the name back. change the style property value. the top level
		// element will receive the notification

		prepareForSelectorBroadCastTest();

		assertFalse(clientListenerTable.done);
		tableSelector.getBackgroundColor().setStringValue("yellow"); //$NON-NLS-1$

		assertTrue(clientListenerTable.done);
		assertEquals(NotificationEvent.STYLE_CLIENT, clientListenerTable.path);
		assertFalse(clientListenerLabel.done);

		assertFalse(clientListenerList1.done);
		listSelector.getBackgroundColor().setStringValue("red"); //$NON-NLS-1$

		assertTrue(clientListenerList1.done);
		assertEquals(NotificationEvent.STYLE_CLIENT, clientListenerList1.path);
		assertFalse(clientListenerList2.done);
	}

	/**
	 *
	 * @throws SemanticException
	 */
	public void testBroadcastForReportSelector() throws SemanticException {

		prepareForSelectorBroadCastTest();
		MyActionListener designListener = new MyActionListener();
		designHandle.addListener(designListener);

		assertFalse(designListener.done);
		reportSelector.getColor().setStringValue("yellow"); //$NON-NLS-1$
		assertTrue(designListener.done);

	}

	/**
	 * When a selector is renamed to another selector, both of the elements which
	 * apply the old selector and the elements which apply the new selector should
	 * be notified.
	 *
	 * @throws NameException
	 * @throws ContentException
	 *
	 */

	public void testBroadcastWhenRenameSelector() throws ContentException, NameException {

		TableHandle table = designHandle.getElementFactory().newTableItem("table"); //$NON-NLS-1$
		ListHandle list = designHandle.getElementFactory().newList("list"); //$NON-NLS-1$

		designHandle.getBody().add(table);
		designHandle.getBody().add(list);

		StyleHandle style = designHandle.getElementFactory().newStyle("table"); //$NON-NLS-1$

		designHandle.getStyles().add(style);

		MyActionListener clientListener1 = new MyActionListener();
		MyActionListener clientListener2 = new MyActionListener();
		table.addListener(clientListener1);
		list.addListener(clientListener2);

		assertFalse(clientListener1.done);
		assertFalse(clientListener2.done);

		style.setName("list"); //$NON-NLS-1$

		assertTrue(clientListener1.done);
		assertTrue(clientListener2.done);
	}

	/**
	 * Tests broadcast when predefined style of the table header cell is modified.
	 *
	 * @throws Exception
	 */
	public void testBroadcastPredefinedStyle() throws Exception {
		openDesign("BroadcastPredefinedStyleTest.xml"); //$NON-NLS-1$

		DesignElementHandle style = designHandle.findStyle("table-header-cell");//$NON-NLS-1$

		DesignElementHandle cellInHeader = designHandle.getElementByID(10);
		DesignElementHandle cellInGroupHeader = designHandle.getElementByID(125);

		MyActionListener styleListener = new MyActionListener();
		MyActionListener clientListener1 = new MyActionListener();
		MyActionListener clientListener2 = new MyActionListener();

		style.addListener(styleListener);
		cellInHeader.addListener(clientListener1);
		cellInGroupHeader.addListener(clientListener2);

		style.setProperty(IStyleModel.FONT_STYLE_PROP, DesignChoiceConstants.FONT_STYLE_ITALIC);

		// the style property was changed, the style and the cell in table
		// header should be notified.
		assertTrue(styleListener.done);
		assertEquals(NotificationEvent.DIRECT, styleListener.path);

		assertTrue(clientListener1.done);
		assertEquals(NotificationEvent.STYLE_CLIENT, clientListener1.path);

		// the style property was changed,the cell in table group
		// header should not be notified.
		assertFalse(clientListener2.done);

		style = designHandle.findStyle("table-group-header-cell");//$NON-NLS-1$
		style.setProperty(IStyleModel.FONT_STYLE_PROP, DesignChoiceConstants.FONT_STYLE_ITALIC);

		// the style property was changed, the style and the cell in table group
		// header should be notified.
		assertTrue(styleListener.done);
		assertEquals(NotificationEvent.DIRECT, styleListener.path);

		assertTrue(clientListener2.done);
		assertEquals(NotificationEvent.STYLE_CLIENT, clientListener2.path);

	}

	/**
	 * Mock up the listener.
	 */

	class MyActionListener implements Listener {

		boolean done = false;
		int path = -1;

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.core.Listener#notify(org.eclipse.birt
		 * .report.model.core.DesignElement,
		 * org.eclipse.birt.report.model.activity.NotificationEvent)
		 */
		@Override
		public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
			done = true;
			path = ev.getDeliveryPath();
		}
	}
}
