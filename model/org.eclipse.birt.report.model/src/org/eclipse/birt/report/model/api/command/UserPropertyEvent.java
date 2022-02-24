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

package org.eclipse.birt.report.model.api.command;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.core.DesignElement;

/**
 * Indicates a change to a user-defined property. Use the {@link #getAction}
 * method to determine the specific kind of change.
 * 
 */

public class UserPropertyEvent extends NotificationEvent {

	/**
	 * The property was added.
	 */

	public static final int ADD = 0;

	/**
	 * The property was dropped.
	 */

	public static final int DROP = 1;

	/**
	 * The property that changed.
	 */

	private UserPropertyDefn property;

	/**
	 * The type of change. One of {@link #ADD}, or {@link #DROP}.
	 */

	private int action;

	/**
	 * Constructor.
	 * 
	 * @param obj       the element that changed.
	 * @param prop      the property that changed.
	 * @param theAction the action which causes this event: {@link #ADD}, or
	 *                  {@link #DROP}.
	 */

	public UserPropertyEvent(DesignElement obj, UserPropertyDefn prop, int theAction) {
		super(obj);
		property = prop;
		action = theAction;
	}

	/**
	 * Returns the action that causes this event. One of {@link #ADD}, or
	 * {@link #DROP}.
	 * 
	 * @return the action that causes this event.
	 */

	public int getAction() {
		return action;
	}

	/**
	 * Returns the definition of the user-defined property that changed.
	 * 
	 * @return the user-defined property definition.
	 */

	public UserPropertyDefn getProperty() {
		return property;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.design.activity.NotificationEvent#getEventType(
	 * )
	 */
	public int getEventType() {
		return USER_PROP_EVENT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.activity.NotificationEvent#isSame(org.
	 * eclipse.birt.report.model.api.activity.NotificationEvent)
	 */

	public boolean isSame(NotificationEvent event) {
		if (!super.isSame(event))
			return false;
		UserPropertyEvent userPropertyEvent = (UserPropertyEvent) event;
		if (action != userPropertyEvent.getAction())
			return false;
		if (property != null && !property.equals(userPropertyEvent.getProperty()))
			return false;
		if (property == null && userPropertyEvent.getProperty() != null)
			return false;
		return true;
	}
}
