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

package org.eclipse.birt.report.model.api.activity;

import org.eclipse.birt.report.model.activity.ActivityStack;

/**
 * This event class identifies that the activity stack changes. This event
 * includes the stack that change and the action causing this event.
 *
 */

public class ActivityStackEvent {

	/**
	 * One record is executed or one transaction is committed.
	 */

	public final static int DONE = 1;

	/**
	 * The latest record or transaction is redone.
	 */

	public final static int REDONE = 2;

	/**
	 * The latest record or transaction is undone.
	 */

	public final static int UNDONE = 3;

	/**
	 * The outermost transaction is rolled back.
	 */

	public final static int ROLL_BACK = 4;

	/**
	 * The activity stack which changes.
	 */

	private ActivityStack stack = null;

	/**
	 * Identifies what action causes this event.
	 */

	private int action;

	/**
	 * Constructs with the stack that changes and the action causing this event.
	 *
	 * @param theStack  the stack that changes
	 * @param theAction the action causing this event
	 */

	public ActivityStackEvent(ActivityStack theStack, int theAction) {
		assert theAction == DONE || theAction == REDONE || theAction == UNDONE || theAction == ROLL_BACK;

		stack = theStack;
		action = theAction;
	}

	/**
	 * Returns the action which causes this event. The possible value is:
	 * <p>
	 * <ul>
	 * <li><code>DONE</code>
	 * <li><code>REDONE</code>
	 * <li><code>UNDONE</code>
	 * </ul>
	 *
	 * @return the action which causes this event.
	 */

	public int getAction() {
		return action;
	}

	/**
	 * Returns the stack that changes.
	 *
	 * @return the stack that changes
	 */

	public ActivityStack getStack() {
		return stack;
	}
}
