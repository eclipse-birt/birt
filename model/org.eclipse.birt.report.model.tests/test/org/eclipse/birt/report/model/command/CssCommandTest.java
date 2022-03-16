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

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.CssEvent;
import org.eclipse.birt.report.model.api.command.CssReloadedEvent;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Unit test for class CssCommand.
 * <tr>
 * <td>test undo and redo can work when add or drop css
 * </tr>
 *
 * <tr>
 * <td>test send message is ok
 * </tr>
 */

public class CssCommandTest extends BaseTestCase {

	/**
	 * Tests undo , redo operation when add/drop css.
	 *
	 * @throws Exception
	 */

	public void testUndoAddAndDropCss() throws Exception {
		openDesign("BlankReportDesign.xml"); //$NON-NLS-1$

		// test undo redo operation

		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("label");//$NON-NLS-1$

		assertEquals("left", labelHandle.getStyle().getTextAlign());//$NON-NLS-1$

		CssStyleSheetHandle sheetHandle = designHandle.openCssStyleSheet("reslove.css");//$NON-NLS-1$
		designHandle.addCss(sheetHandle);

		assertEquals("center", labelHandle.getStyle().getTextAlign());//$NON-NLS-1$

		designHandle.getCommandStack().undo();
		assertEquals("left", labelHandle.getStyle().getTextAlign());//$NON-NLS-1$

		designHandle.getCommandStack().redo();
		assertEquals("center", labelHandle.getStyle().getTextAlign());//$NON-NLS-1$

		// drop reslove.css
		designHandle.dropCss(sheetHandle);
		assertEquals("left", labelHandle.getStyle().getTextAlign());//$NON-NLS-1$

		designHandle.getCommandStack().undo();
		assertEquals("center", labelHandle.getStyle().getTextAlign());//$NON-NLS-1$

		designHandle.getCommandStack().redo();
		assertEquals("left", labelHandle.getStyle().getTextAlign());//$NON-NLS-1$

	}

	/**
	 * Tests undo , redo operation when reload css.
	 *
	 * @throws Exception
	 */

	public void testUndoReloadCss() throws Exception {
		openDesign("CssCommandTest_Reload.xml"); //$NON-NLS-1$

		// test undo redo operation

		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("label");//$NON-NLS-1$

		assertEquals("center", labelHandle.getStyle().getTextAlign());//$NON-NLS-1$

		CssStyleSheetHandle sheetHandle = (CssStyleSheetHandle) designHandle.getAllCssStyleSheets().get(0);
		designHandle.reloadCss(sheetHandle);
		assertEquals("center", labelHandle.getStyle().getTextAlign());//$NON-NLS-1$

		// position in design file is the same
		sheetHandle = (CssStyleSheetHandle) designHandle.getAllCssStyleSheets().get(0);
		assertEquals("base.css", sheetHandle.getFileName());//$NON-NLS-1$

		assertFalse(designHandle.needsSave());

		assertFalse(designHandle.getCommandStack().canRedo());
		assertFalse(designHandle.getCommandStack().canUndo());
	}

	/**
	 * Test addCss method.
	 *
	 * @throws Exception
	 */

	public void testAdd() throws Exception {

		openDesign("BlankReportDesign.xml"); //$NON-NLS-1$

		MyListener listener = new MyListener();
		designHandle.addListener(listener);

		CssStyleSheetHandle sheetHandle = designHandle.openCssStyleSheet("reslove.css");//$NON-NLS-1$
		designHandle.addCss(sheetHandle);

		assertEquals(NotificationEvent.CSS_EVENT, listener.getEventType());
		assertEquals(CssEvent.ADD, listener.getAction());
		assertEquals(2, listener.getEventCount());

		listener.clearEventCount();

		sheetHandle = (CssStyleSheetHandle) designHandle.getAllCssStyleSheets().get(0);
		designHandle.reloadCss(sheetHandle);

		assertEquals(NotificationEvent.CSS_RELOADED_EVENT, listener.getEventType());
		assertEquals(1, listener.getEventCount());

		listener.clearEventCount();

		sheetHandle = (CssStyleSheetHandle) designHandle.getAllCssStyleSheets().get(0);
		designHandle.dropCss(sheetHandle);

		assertEquals(CssEvent.DROP, listener.getAction());
		assertEquals(2, listener.getEventCount());
	}

	class MyListener implements Listener {

		int action = CssEvent.ADD;
		int eventType = NotificationEvent.CSS_EVENT;
		int count = 0;

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
			if (ev.getEventType() == NotificationEvent.CSS_EVENT) {
				CssEvent event = (CssEvent) ev;
				action = event.getAction();
				eventType = event.getEventType();
				++count;
			} else if (ev.getEventType() == NotificationEvent.CSS_RELOADED_EVENT) {
				CssReloadedEvent event = (CssReloadedEvent) ev;
				eventType = event.getEventType();
				++count;
			}

		}

		/**
		 * Gets event count.
		 *
		 * @return event count
		 */

		public int getEventCount() {
			return count;
		}

		/**
		 * Set event count to zero
		 *
		 */

		public void clearEventCount() {
			count = 0;
		}

		/**
		 * Gets action code
		 *
		 * @return action
		 *
		 */

		public int getAction() {
			return action;
		}

		/**
		 * Gets event type
		 *
		 * @return event type
		 */

		public int getEventType() {
			return eventType;
		}
	}
}
