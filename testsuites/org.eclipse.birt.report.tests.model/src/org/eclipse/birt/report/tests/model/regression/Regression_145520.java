/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Regression description:</b>
 * <p>
 * Group name will not show up in group list if the group use default name and
 * key expression has no value
 * <p>
 * Description:
 * <p>
 * Group name will not show up in group list if key expression has no value.
 * <p>
 * <b>Steps to reproduce:</b>
 * <ol>
 * <li>Insert a table into report, bind it to a data set
 * <li>Switch to property editor -> groups
 * <li>Click on add button to open new group dialog, then press ok button
 * </ol>
 * <b>Expected result:</b>
 * <p>
 * A new group with default name will be added to group list Actual result: A
 * new group is added to group list, but without group name. But when let group
 * tab lost focus and get focus again, you can see the group name.
 * <p>
 * <b>Test description:</b>
 * <p>
 * Make sure that when add a table group, an event of that the unique group name
 * created is notified to the listener on the group, so that UI can make the
 * refresh.
 * <p>
 */
public class Regression_145520 extends BaseTestCase {

	private final static String REPORT = "regression_145520.xml"; //$NON-NLS-1$

	/**
	 * @throws DesignFileException
	 * @throws NameException
	 * @throws ContentException
	 */

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(REPORT, REPORT);
	}

	public void tearDown() {
		removeResource();
	}

	public void test_regression_145520() throws DesignFileException, ContentException, NameException {
		this.openDesign(REPORT);
		TableHandle table1 = (TableHandle) designHandle.findElement("table1"); //$NON-NLS-1$

		MyListener tableListener = new MyListener();
		table1.addListener(tableListener);

		ElementFactory factory = this.designHandle.getElementFactory();
		TableGroupHandle group = factory.newTableGroup();

		MyListener groupListener = new MyListener();
		group.addListener(groupListener);

		table1.getGroups().add(group);

		// make sure that the group has been notified of the groupname change
		// event.

		assertTrue(groupListener.event instanceof PropertyEvent);
		PropertyEvent propEvent = (PropertyEvent) groupListener.event;

		assertEquals("groupName", propEvent.getPropertyName()); //$NON-NLS-1$
		assertEquals(group.getElement(), propEvent.getTarget());
	}

	static class MyListener implements Listener {

		DesignElementHandle focus = null;
		NotificationEvent event = null;

		public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
			this.focus = focus;
			this.event = ev;
		}
	}
}
