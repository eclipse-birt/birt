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

package org.eclipse.birt.report.model.activity;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;

/**
 * The interface to filter events.
 */

public interface IFilterCondition {

	/**
	 * Left one of the two events.
	 */

	int LEFT_EVENT = 0;

	/**
	 * Right one of the two events.
	 */

	int RIGHT_EVENT = 1;

	/**
	 * Both of the two events.
	 */

	int BOTH_EVENT = 2;

	/**
	 * None of the two events.
	 */

	int NO_EVENT = 3;

	/**
	 * Gets the event to be filtered. The returned event will be filtered.
	 *
	 * @param ev1 the first event to compare
	 * @param ev2 the second event to compare
	 * @return the event to be filtered
	 */

	int getFilterEvent(NotificationEvent ev1, NotificationEvent ev2);
}
