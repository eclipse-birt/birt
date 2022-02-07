/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * <p>
 * When reload the library, the reportdesign receive event more than one time.
 * <p>
 * When reload one library file in the report design, the report designer
 * listener receive event three times, and the event type is reload library
 * type. But the listener need receive only one time.
 * <p>
 * Test description:
 * <p>
 * Make sure hen reloadLibrary() is called, only send one library message to the
 * report design and the event is type of LibraryReloadedEvent
 * <p>
 */

public class Regression_137170 extends BaseTestCase {

	private final static String INPUT = "regression_137170.xml"; //$NON-NLS-1$

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(INPUT, INPUT);

	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void test_regression_137170() throws DesignFileException, SemanticException {
		openDesign(INPUT);

		MyListener moniter = new MyListener();
		designHandle.addListener(moniter);
		designHandle.reloadLibraries();

		assertEquals(1, MyListener.count);
		assertTrue(MyListener.event instanceof org.eclipse.birt.report.model.api.command.LibraryReloadedEvent);
	}

	static class MyListener implements Listener {

		static int count = 0;
		static NotificationEvent event = null;

		public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
			count++;
			event = ev;
		}
	}
}
