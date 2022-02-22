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

package org.eclipse.birt.report.model.api.elements.table;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.elements.ReportItem;

/**
 * Notification event that says that the layout of the table element has been
 * changed. The target element is the table.
 *
 */

public class LayoutChangedEvent extends NotificationEvent {

	/**
	 * Identifies a change to existing data.
	 */

	public static final int UPDATE = 1;

	/**
	 * The type of the event.
	 */

	private int type;

	/**
	 *
	 * @param compoundElement
	 */

	public LayoutChangedEvent(ReportItem compoundElement) {
		super(compoundElement);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.activity.NotificationEvent#getEventType()
	 */

	@Override
	public int getEventType() {
		return LAYOUT_CHANGED_EVENT;
	}

	/**
	 * Returns the type of event: UPDATE.
	 *
	 * @return the type of event
	 */

	protected int getType() {
		return type;
	}
}
