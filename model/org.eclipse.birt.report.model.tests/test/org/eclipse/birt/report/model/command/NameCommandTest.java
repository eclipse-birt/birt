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

package org.eclipse.birt.report.model.command;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.command.NameSpaceEvent;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.GraphicMasterPage;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.MasterPage;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Unit test for class NameCommand.
 *
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 *
 * <tr>
 * <td>{@link #testNameSpace()}</td>
 * <td>Set name for a style, change its name. Check the style name space</td>
 * <td>The new name was added into the style namespace</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Set a name for a materPage, change its name</td>
 * <td>The new name was added into the page namespace</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>set a name for a label, change its name.</td>
 * <td>The new name was added into the element namespace</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testSetName}</td>
 * <td>the name that is setting for element is duplicate in the namespace.</td>
 * <td>duplicate name can not be added into the namespace and an exception threw
 * out.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>the element definition is not allowed to set name.</td>
 * <td>the name can not be set and an exception threw out.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>the setting name is null when the name property is required by the
 * element def.</td>
 * <td>An exception is throws with NAME_REQUIRED.</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testSetNameUndoRedo()}</td>
 * <td>Normal case with undo/redo.</td>
 * <td>The undo and redo operation does work.</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testNotification}</td>
 * <td>Use listener to test if notification works or not.</td>
 * <td>value of property displayName change to listener</td>
 * </tr>
 *
 * </table>
 *
 */

public class NameCommandTest extends BaseTestCase {

	DesignElementHandle styleHandle;

	static final String fileName = "StyleCommandTest.xml";//$NON-NLS-1$

	/*
	 * @see TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {

		super.setUp();

		// open design file

		openDesign(fileName);
		assertNotNull(design);
		assertNotNull(designHandle);

		styleHandle = designHandle.getElementFactory().newStyle("myStyle"); //$NON-NLS-1$
		designHandle.getStyles().add(styleHandle);
	}

	/**
	 * Unit test for method setName( String name).
	 * <p>
	 * Test case:
	 *
	 * <ul>
	 * <li>set name for a style, change its name
	 * <li>set a name for a materPage, change its name
	 * <li>set a name for a label, change its name
	 * </ul>
	 * <p>
	 *
	 * Expected result:
	 * <ul>
	 * <li>the old name was removed from the name space, the new name was added into
	 * the corresponding namespace
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testNameSpace() throws Exception {
		// test for stylen namespace

		Style newStyle = new Style();
		newStyle.getHandle(design).setName("newStyle");//$NON-NLS-1$
		assertEquals("newStyle", newStyle.getName()); //$NON-NLS-1$

		newStyle.getHandle(design).setName("changeStyle");//$NON-NLS-1$
		assertEquals("changeStyle", newStyle.getName()); //$NON-NLS-1$

		// check whether the old name has beed moved from name space
		assertFalse((design.getNameHelper().getNameSpace(Module.STYLE_NAME_SPACE)).contains("newStyle")); //$NON-NLS-1$
		// check whether the new name has beed added into name space.
		assertFalse((design.getNameHelper().getNameSpace(Module.STYLE_NAME_SPACE)).contains("changeStyle")); //$NON-NLS-1$

		// Test for Page name space

		MasterPage masterPage = new GraphicMasterPage();

		// set name of MasterPage to page

		masterPage.getHandle(design).setName("page"); //$NON-NLS-1$
		assertFalse((design.getNameHelper().getNameSpace(Module.PAGE_NAME_SPACE)).contains("page")); //$NON-NLS-1$
		// set name of MasterPage to newPage

		masterPage.getHandle(design).setName("newPage"); //$NON-NLS-1$
		assertFalse((design.getNameHelper().getNameSpace(Module.PAGE_NAME_SPACE)).contains("page")); //$NON-NLS-1$
		assertFalse((design.getNameHelper().getNameSpace(Module.PAGE_NAME_SPACE)).contains("newPage")); //$NON-NLS-1$

		// Test for element namespace

		Label label = new Label();

		// set name of label to label

		label.getHandle(design).setName("label");//$NON-NLS-1$
		assertFalse((design.getNameHelper().getNameSpace(Module.ELEMENT_NAME_SPACE)).contains("label")); //$NON-NLS-1$

		// set name of label to newLabel

		label.getHandle(design).setName("newLabel");//$NON-NLS-1$
		assertFalse((design.getNameHelper().getNameSpace(Module.ELEMENT_NAME_SPACE)).contains("label")); //$NON-NLS-1$
		assertFalse((design.getNameHelper().getNameSpace(Module.ELEMENT_NAME_SPACE)).contains("newLabel")); //$NON-NLS-1$
	}

	/**
	 * Unit test for method setName( String name).
	 * <p>
	 * Test Case:
	 * <ul>
	 * <li>the name that is setting for element is duplicate in the namespace.
	 *
	 * <li>set name to GroupElement which name forbides to be set
	 *
	 *
	 * <li>the setting name is null when the name property is required by the
	 * element def.
	 * </ul>
	 *
	 * <p>
	 * Expected result:
	 * <ul>
	 * <li>duplicate name can not be added into the namespace and an exception threw
	 * out.
	 * <li>the name can not be set and an exception threw out.
	 *
	 * <li>An exception threw out with NAME_REQUIRED.
	 * </ul>
	 *
	 */
	public void testSetName() {
		Style testStyle = new Style();

		// set name of Style to testStyle

		testStyle.setName("testStyle"); //$NON-NLS-1$
		design.getNameHelper().getNameSpace(Module.STYLE_NAME_SPACE).insert(testStyle);
		try {
			// set duplicate name and throw out exception

			styleHandle.setName("testStyle");//$NON-NLS-1$
			fail("testSetName1 method cann't throw out NameException !"); //$NON-NLS-1$
		} catch (NameException e) {
			assertEquals(NameException.DESIGN_EXCEPTION_DUPLICATE, e.getErrorCode());
		}

		Cell cell = new Cell();

		try {
			// set name to GroupElement , it results in exception

			cell.getHandle(design).setName("testStyle");//$NON-NLS-1$
			fail("testSetName2 method cann't throw out NameException! "); //$NON-NLS-1$
		} catch (NameException e) {
			assertEquals(NameException.DESIGN_EXCEPTION_NAME_FORBIDDEN, e.getErrorCode());
		}

		try {
			// set name to null and should throw out exception

			styleHandle.setName(null);

			fail("testSetName3 method cann't throw out NameException! "); //$NON-NLS-1$
		} catch (NameException e) {
			assertEquals(NameException.DESIGN_EXCEPTION_NAME_REQUIRED, e.getErrorCode());
		}

	}

	/**
	 * Unit test for mornal set name and the undo, redo,transition.
	 * <p>
	 * Test Case:
	 * <ul>
	 * <li>Normal case with Set name for style and undo, redo.
	 * </ul>
	 * <p>
	 * Expected result:
	 * <ul>
	 * <li>The undo and redo operation does work.
	 * </ul>
	 *
	 * @throws Exception
	 *
	 *
	 */
	public void testSetNameUndoRedo() throws Exception {
		// create new style which name is null

		Style newStyle = new Style();
		ActivityStack as = design.getActivityStack();

		// set new name to style and check it

		newStyle.getHandle(design).setName("style"); //$NON-NLS-1$
		assertEquals("style", newStyle.getName());//$NON-NLS-1$

		// test undo and redo

		assertTrue(as.canUndo());
		as.undo();
		assertNull(newStyle.getName());
		assertTrue(as.canRedo());
		assertTrue(as.canUndo());
		as.redo();
		assertEquals("style", newStyle.getName());//$NON-NLS-1$

		// to drop style which name is newStyleName
		// and check it is exist or not

		newStyle.getHandle(design).setName("newStyleName"); //$NON-NLS-1$
		//
		// assertEquals( "newStyleName", newStyle.getName( ) );//$NON-NLS-1$
		// assertFalse( design.getNameHelper( ).getNameSpace(
		// RootElement.STYLE_NAME_SPACE )
		// .contains( "newStyleName" ) ); //$NON-NLS-1$

		// undo again and style name back to style
		as.undo();
		assertEquals("style", newStyle.getName()); //$NON-NLS-1$
		assertFalse((design.getNameHelper().getNameSpace(Module.STYLE_NAME_SPACE)).contains("style")); //$NON-NLS-1$

		// redo and style name back to newStyleName

		as.redo();
		assertEquals("newStyleName", newStyle.getName());//$NON-NLS-1$

		assertFalse((design.getNameHelper().getNameSpace(Module.STYLE_NAME_SPACE)).contains("newStyleName")); //$NON-NLS-1$
	}

	/**
	 * Unit test for the listener.
	 *
	 * <p>
	 *
	 * Test Case:
	 * <ul>
	 * <li>Use listener to test if notification works or not.
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testNotification() throws Exception {
		StyleElement myStyle = design.findStyle("My-Style"); //$NON-NLS-1$

		MyNameListener nameListener = new MyNameListener();
		myStyle.addListener(nameListener);

		MyNameListener labelListener = new MyNameListener();
		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("myLabel"); //$NON-NLS-1$
		labelHandle.addListener(labelListener);
		DataSetHandle dataSetHandle = designHandle.findDataSet("firstDataSet"); //$NON-NLS-1$
		dataSetHandle.setName("New DataSet Name"); //$NON-NLS-1$
		assertFalse(labelListener.nameChanged);

		// name space listener should be registered in ReportDesign

		MyNameSpaceListener nameSpaceListener = new MyNameSpaceListener();
		design.addListener(nameSpaceListener);

		myStyle.getHandle(design).setName("hello"); //$NON-NLS-1$
		assertTrue(nameListener.nameChanged);

		myStyle.removeListener(nameListener);

	}

	class MyNameListener implements Listener {

		boolean nameChanged = false;

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.birt.report.model.core.Listener#notify(org.eclipse.birt.report.
		 * model.core.DesignElement,
		 * org.eclipse.birt.report.model.activity.NotificationEvent)
		 */
		@Override
		public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
			nameChanged = true;
		}

	}

	class MyNameSpaceListener implements Listener {

		static final int NA = 0;
		static final int ADDED = 1;
		static final int REMOVED = 2;
		static final int RENAMED = 3;

		NameSpaceEvent event = null;

		int action = NA;

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.birt.report.model.core.Listener#notify(org.eclipse.birt.report.
		 * model.core.DesignElement,
		 * org.eclipse.birt.report.model.activity.NotificationEvent)
		 */
		@Override
		public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
			if (ev.getEventType() == NotificationEvent.NAME_SPACE_EVENT) {
				event = (NameSpaceEvent) ev;

				if (action == REMOVED && event.getAction() == NameSpaceEvent.ADD) {
					action = RENAMED;
				} else {
					switch (event.getAction()) {
					case NameSpaceEvent.ADD:
						action = ADDED;
						break;
					case NameSpaceEvent.REMOVE:
						action = REMOVED;
						break;
					}
				}
			}

		}

	}
}
