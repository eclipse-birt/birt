/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.activity;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;

/**
 * The interface to filter events.
 */

public interface IFilterCondition {

	/**
	 * Left one of the two events.
	 */

	final static int LEFT_EVENT = 0;

	/**
	 * Right one of the two events.
	 */

	final static int RIGHT_EVENT = 1;

	/**
	 * Both of the two events.
	 */

	final static int BOTH_EVENT = 2;

	/**
	 * None of the two events.
	 */

	final static int NO_EVENT = 3;

	/**
	 * Gets the event to be filtered. The returned event will be filtered.
	 * 
	 * @param ev1 the first event to compare
	 * @param ev2 the second event to compare
	 * @return the event to be filtered
	 */

	int getFilterEvent(NotificationEvent ev1, NotificationEvent ev2);
}
