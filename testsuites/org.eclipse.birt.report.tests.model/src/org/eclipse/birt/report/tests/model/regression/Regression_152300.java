/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.activity.ActivityStackEvent;
import org.eclipse.birt.report.model.api.activity.ActivityStackListener;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.LibraryReloadedEvent;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * <p>
 * Send LibraryReloadedEvent before the transaction is committed in
 * LibraryCommand.reloadLibrary()
 * <p>
 * Test description:
 * <p>
 * <p>
 */
public class Regression_152300 extends BaseTestCase {

	private final static String REPORT = "regression_152300.xml"; //$NON-NLS-1$
	static int seed = 0;

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(REPORT, REPORT);
	}

	public void tearDown() {
		removeResource();
	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void test_regression_152300() throws DesignFileException, SemanticException {
		openDesign(REPORT);
		ElementListener elementListener = new ElementListener();
		StackListener stackListener = new StackListener();

		designHandle.addListener(elementListener);
		designHandle.getCommandStack().addListener(stackListener);

		designHandle.reloadLibraries();

		// fire LibraryReloadedEvent before stack event.

		assertTrue(elementListener.id < stackListener.id);
		assertTrue(elementListener.event instanceof LibraryReloadedEvent);
		assertTrue(stackListener.event instanceof ActivityStackEvent);
	}
}

class ElementListener implements Listener {

	DesignElementHandle focus = null;
	NotificationEvent event = null;
	int id = 0;

	public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
		this.id = ++Regression_152300.seed;
		this.focus = focus;
		this.event = ev;
	}
}

class StackListener implements ActivityStackListener {

	int id = 0;
	ActivityStackEvent event = null;

	public void stackChanged(ActivityStackEvent event) {
		this.id = ++Regression_152300.seed;
		this.event = event;
	}
}
