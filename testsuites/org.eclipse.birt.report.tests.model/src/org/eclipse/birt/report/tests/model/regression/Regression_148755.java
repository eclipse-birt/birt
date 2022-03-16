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

import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.activity.ActivityStackEvent;
import org.eclipse.birt.report.model.api.activity.ActivityStackListener;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * <p>
 * Add the status to the ActivityStackEvent, if the the event rollback, the
 * ActivityStackEvent status should be ROLL_BACK.
 * <p>
 * Test description:
 * <p>
 * Create fake activity stack, record and listener, make sure the event should
 * be typed ROLL_BACK when rollback the transaction.
 * <p>
 */
public class Regression_148755 extends BaseTestCase {

	private final static String INPUT = "regression_148755.xml"; //$NON-NLS-1$

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	@Override
	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(INPUT, INPUT);
	}

	@Override
	public void tearDown() {
		removeResource();
	}

	public void test_regression_148755() throws DesignFileException, SemanticException {
		openDesign(INPUT);

		MyActivityStackListener stackListener = new MyActivityStackListener();

		CommandStack commandStack = designHandle.getCommandStack();
		commandStack.addListener(stackListener);

		LabelHandle label1 = (LabelHandle) designHandle.findElement("label1"); //$NON-NLS-1$

		commandStack.startTrans("trans1"); //$NON-NLS-1$
		label1.setText("updated text"); //$NON-NLS-1$
		label1.setHeight("24pt"); //$NON-NLS-1$
		commandStack.rollback();

		assertEquals("Sample Label", label1.getText()); //$NON-NLS-1$

		assertEquals(ActivityStackEvent.ROLL_BACK, stackListener.event.getAction());
	}

	class MyActivityStackListener implements ActivityStackListener {

		ActivityStackEvent event = null;

		@Override
		public void stackChanged(ActivityStackEvent event) {
			this.event = event;
		}
	}
}
