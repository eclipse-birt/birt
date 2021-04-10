/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.command.ViewsContentEvent;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests case for multiple view.
 */

public class MultiViewHandleTest extends BaseTestCase {

	/**
	 * Tests cases about parser and API related.
	 * 
	 * @throws Exception
	 */

	public void testAPIs() throws Exception {
		createDesign();

		TableHandle table1 = designHandle.getElementFactory().newTableItem("table1"); //$NON-NLS-1$
		designHandle.getBody().add(table1);
		table1.setName("HostTable"); //$NON-NLS-1$

		MyListener tmpListener = new MyListener();
		table1.addListener(tmpListener);

		ExtendedItemHandle box1 = designHandle.getElementFactory().newExtendedItem("box1", "TestingBox"); //$NON-NLS-1$//$NON-NLS-2$
		table1.addView(box1);

		// make sure table receives the event
		assertTrue(tmpListener.isChanged());
		tmpListener.resetFlag();

		table1.setCurrentView(box1);

		// make sure table receives the event
		assertTrue(tmpListener.isChanged());
		tmpListener.resetFlag();
		assertTrue(box1.getContainer().getContainer() == table1);
		assertTrue(box1.getHostViewHandle() == table1);
		assertNull(table1.getHostViewHandle());

		List<ReportItemHandle> views = table1.getViews();
		assertEquals(1, views.size());
		assertTrue(box1 == views.get(0));

		assertTrue(box1 == table1.getCurrentView());
		assertEquals(table1, box1.getViewHost());
		assertNull(table1.getViewHost());

		table1.dropView(box1);

		// make sure table receives the event
		assertTrue(tmpListener.isChanged());
		tmpListener.resetFlag();

		assertNull(table1.getCurrentView());
		views = table1.getViews();
		assertEquals(0, views.size());
		assertNull(box1.getViewHost());

		table1.setCurrentView(box1);

		// make sure table receives the event
		assertTrue(tmpListener.isChanged());
		tmpListener.resetFlag();

		assertTrue(box1 == table1.getCurrentView());
		table1.setCurrentView(null);

		views = table1.getViews();
		assertEquals(1, views.size());
		assertNull(table1.getCurrentView());
	}

	/**
	 * Tests property search algorithm on bookmark, toc, visibility values for sub
	 * view elements.
	 * 
	 * @throws Exception
	 */

	public void testHostRelatedPropertySearch() throws Exception {
		openDesign("MultiViewHandleTest.xml"); //$NON-NLS-1$

		ExtendedItemHandle box1 = (ExtendedItemHandle) designHandle.findElement("box1"); //$NON-NLS-1$
		assertEquals("\"a\"", box1.getBookmark()); //$NON-NLS-1$
		assertEquals("1+1", box1.getTOC().getExpression()); //$NON-NLS-1$

		Iterator<HideRuleHandle> rules = box1.visibilityRulesIterator();
		assertTrue(rules.hasNext());

		PropertyHandle propHandle = box1.getPropertyHandle(ExtendedItemHandle.VISIBILITY_PROP);
		assertTrue(propHandle.isReadOnly());

		assertEquals(DesignChoiceConstants.PAGE_BREAK_BEFORE_ALWAYS,
				box1.getStringProperty(IStyleModel.PAGE_BREAK_BEFORE_PROP));
		assertEquals(DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS,
				box1.getStringProperty(IStyleModel.PAGE_BREAK_AFTER_PROP));
		assertEquals(DesignChoiceConstants.PAGE_BREAK_INSIDE_AUTO,
				box1.getStringProperty(IStyleModel.PAGE_BREAK_INSIDE_PROP));
		assertEquals("Simple MasterPage", box1 //$NON-NLS-1$
				.getStringProperty(IStyleModel.MASTER_PAGE_PROP));

	}

	private static class MyListener implements Listener {

		private boolean propertyChanged = false;

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.core.Listener#notify(org.eclipse.birt
		 * .report.model.core.DesignElement,
		 * org.eclipse.birt.report.model.activity.NotificationEvent)
		 */
		public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
			if (ev instanceof PropertyEvent) {
				if (((PropertyEvent) ev).getPropertyName() == IReportItemModel.MULTI_VIEWS_PROP)
					propertyChanged = true;
			}
			if (ev instanceof ViewsContentEvent) {
				propertyChanged = true;
			}
		}

		boolean isChanged() {
			return propertyChanged;
		}

		void resetFlag() {
			propertyChanged = false;
		}
	}
}
