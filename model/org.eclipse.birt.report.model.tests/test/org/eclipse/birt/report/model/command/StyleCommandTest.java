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
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.StyleException;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.Parameter;
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Unit test for class StyleCommand.
 * 
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td width="33%" height="16">{@link #testSetStyle()}</td>
 * <td width="33%" height="16">The design element is not a StyledElement</td>
 * <td>catch StyleException and the error code of StyleException is equal to
 * StyleException.FORBIDDEN(value is 1)</td>
 * </tr>
 * 
 * <tr>
 * <td width="33%" height="16"></td>
 * <td width="33%" height="16">Design element is a StyledElement, but the style
 * name isn't exist.</td>
 * <td>catch StyleException and the error code of StyleException is equal to
 * StyleException.NOT_FOUND(value is 2) and Style name is changed accordingly.
 * </td>
 * </tr>
 * 
 * <tr>
 * <td width="33%" height="16"></td>
 * <td width="33%" height="16">Undo and redo operation on style element and
 * boardcast all listeners.</td>
 * <td>No exception throw out and label.getStyle() is equal to targeted style.
 * </td>
 * </tr>
 * 
 * <tr>
 * <td width="33%" height="16">{@link #testSetStyleElement()}</td>
 * <td width="33%" height="16">Normal case with API call and redo/undo.</td>
 * <td width="34%" height="16">Test case passed.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testNotification}</td>
 * <td>Use listener to test if notification works or not.</td>
 * <td>value of property displayName change to listener</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testExtendsAndClients}</td>
 * <td>One style has 1 and 2 clients.</td>
 * <td>The number of clients is 1 or 2.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>An element label that uses one style. Element label1 extends the label.
 * </td>
 * <td>Shared style of label1 is null.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>An element label that uses style X. Another element label1 extends
 * element label. Sets element label1 to use style X. Now both label and label1
 * show up on style's client list. If changes element label to use Style T, then
 * element label1 should still use style X.</td>
 * <td>Element label1 uses style X.</td>
 * </tr>
 * 
 * </table>
 * 
 */

public class StyleCommandTest extends BaseTestCase {

	SharedStyleHandle style;

	private static final String fileName = "StyleCommandTest.xml";//$NON-NLS-1$

	/*
	 * @see TestCase#setUp()
	 */

	protected void setUp() throws Exception {
		super.setUp();

		openDesign(fileName);
		assertNotNull(design);
		assertNotNull(designHandle);

		style = designHandle.findStyle("My-Style"); //$NON-NLS-1$
	}

	/**
	 * Unit test for method setStyleName(name).
	 * 
	 * <p>
	 * Test Case:
	 * <ul>
	 * <li>The design element is not a StyledElement
	 * 
	 * <li>Design element is a StyledElement, but the style name isn't exist.
	 * 
	 * <li>undo and redo operation on style element and broadcast all listeners
	 * 
	 * </ul>
	 * 
	 * @throws Exception if any exception
	 */

	public void testSetStyle() throws Exception {
		// set scalar element's style

		Parameter param = new ScalarParameter();
		try {
			param.getHandle(design).setStyleName("helloStyle"); //$NON-NLS-1$
			fail();
		} catch (StyleException e) {
			assertEquals(StyleException.DESIGN_EXCEPTION_FORBIDDEN, e.getErrorCode());
		}

		// set label element's style

		Label label = new Label();
		try {
			label.getHandle(design).setStyleName("worldStyle"); //$NON-NLS-1$
			fail();
		} catch (StyleException e) {
			assertEquals(StyleException.DESIGN_EXCEPTION_NOT_FOUND, e.getErrorCode());
		}

		label.getHandle(design).setStyleName(style.getName());
		assertEquals(style.getName(), label.getStyleName());

		Label newLabel = new Label();
		setStyleMethod(newLabel);
	}

	/**
	 * test for setStyleElement method.
	 * 
	 * @param element
	 * @throws StyleException
	 * @throws Exception
	 */

	private void setStyleMethod(DesignElement element) throws StyleException, Exception {
		// clear all undoStack and redoStack
		// in order not to influence results

		ActivityStack cs = (ActivityStack) designHandle.getCommandStack();
		cs.flush();

		// set style value to helloStyle

		element.getHandle(design).setStyle(style);

		assertEquals(style.getElement(), element.getStyle());

		// execute undo and redo operate to test if value is changed or not

		undoOperate(element, cs);
		redoOperate(element, cs);

		element.getHandle(design).setStyle(null);

		assertNull(element.getStyle());

	}

	/**
	 * Unit test for the listener.
	 * 
	 * <p>
	 * 
	 * Test Case:
	 * <ul>
	 * <li>Use listener to test if notification works or not.</li>
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testNotification() throws Exception {
		SharedStyleHandle style1 = designHandle.findStyle("Style1"); //$NON-NLS-1$
		assertNull(style1.getProperty(Style.HIGHLIGHT_RULES_PROP));

		LabelHandle label = designHandle.getElementFactory().newLabel(null);
		designHandle.getBody().add(label);
		MyStyleListener listener = new MyStyleListener();

		label.addListener(listener);

		label.setStyle(style1);

		assertTrue(listener.styleChanged);

		label.removeListener(listener);
	}

	/**
	 * Once operation on undo to test if value is changed to null or not.
	 * 
	 * @param labelElement DesignElement this is Label
	 * @param cs           CommonStack
	 */

	private void undoOperate(DesignElement labelElement, ActivityStack cs) {
		assertTrue(cs.canUndo());
		assertFalse(cs.canRedo());

		// undo setStyle method , then style is not helloStyle

		cs.undo();
		assertNull(labelElement.getStyle());

		assertFalse(cs.canUndo());
		assertTrue(cs.canRedo());

		cs.redo();
		cs.undo();

	}

	/**
	 * multiple operation on redo to test if value is changed to helloStyle or not.
	 * 
	 * @param labelElement DesignElement this is Label
	 * @param cs           CommonStack
	 */

	private void redoOperate(DesignElement labelElement, ActivityStack cs) {
		assertFalse(cs.canUndo());
		assertTrue(cs.canRedo());

		// first redo , then style is helloStyle

		cs.redo();
		assertEquals(style.getElement(), labelElement.getStyle());
		assertFalse(cs.canRedo());
		assertTrue(cs.canUndo());

		cs.undo();
		cs.redo();

	}

	/**
	 * Unit test for method setStyleElement( StyleElement ).
	 * 
	 * <p>
	 * Test Case:
	 * <ul>
	 * <li>Normal case with API call and redo/undo.
	 * </ul>
	 * 
	 * @throws Exception if any exception.
	 */

	public void testSetStyleElement() throws Exception {
		Label label = new Label();
		setStyleMethod(label);
	}

	/**
	 * Unit test for style related extends and clients.
	 * 
	 * <p>
	 * Test Case:
	 * <ul>
	 * <li><code>SetStyle</code> of elements with the style's clients and extend
	 * elements.
	 * </ul>
	 * 
	 * @throws Exception if any exception.
	 */

	public void testExtendsAndClients() throws Exception {
		Label label = new Label();
		label.setName("newLabel"); //$NON-NLS-1$

		// first should add label to the components
		// then you can extends it

		designHandle.getComponents().add(label.getHandle(design));

		// no clients, must be 0

		assertEquals(((StyleElement) style.getElement()).getClientList().size(), 0);

		// sets one client to this style.

		label.getHandle(design).setStyle(style);
		assertEquals(((StyleElement) style.getElement()).getClientList().size(), 1);

		style.setName("new_style"); //$NON-NLS-1$
		assertEquals("new_style", //$NON-NLS-1$
				label.getStyleName());

		// remove the style from its client.

		label.getHandle(design).setStyle(null);
		assertEquals(((StyleElement) style.getElement()).getClientList().size(), 0);

		// restore style name.
		String styleName = "new-named-style"; //$NON-NLS-1$
		style = designHandle.getElementFactory().newStyle(styleName);
		designHandle.getSlot(IReportDesignModel.STYLE_SLOT).add(style);
		label.getHandle(design).setStyle(style);

		Label label1 = new Label();
		label.getHandle(design).setName("label"); //$NON-NLS-1$

		label1.getHandle(design).setExtendsElement(label);

		/*
		 * We have an element label that uses style. Element label1 extends label.
		 * 
		 * When we ask label1 for its shared style, we get null (because Y does not
		 * explicitly set a style.)
		 */

		assertNull(label1.getHandle(design).getStyle());

		/*
		 * Suppose we have element label that uses style. We define element label1 that
		 * extends element label. We set element label1 to use style S. Now both label
		 * and label1 show up on style's client list. If we change style X to use Style
		 * T, then style Y should still use style S.
		 */

		label1.getHandle(design).setStyle(style);
		assertEquals(((StyleElement) style.getElement()).getClientList().size(), 2);
		assertEquals(styleName, label1.getStyleName());

		Style tmpStyle = new Style();
		styleName = "another-new-style"; //$NON-NLS-1$
		tmpStyle.getHandle(design).setName(styleName);
		designHandle.getSlot(IReportDesignModel.STYLE_SLOT).add(tmpStyle.getHandle(design));
		label.getHandle(design).setStyleElement(tmpStyle);
		assertEquals(styleName, label.getStyleName());
		assertEquals("new-named-style", //$NON-NLS-1$
				label1.getStyleName());

	}

	class MyStyleListener implements Listener {

		boolean styleChanged = false;

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.core.Listener#notify(org.eclipse.birt
		 * .report.model.core.DesignElement,
		 * org.eclipse.birt.report.model.activity.NotificationEvent)
		 */
		public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
			styleChanged = true;
		}
	}
}
