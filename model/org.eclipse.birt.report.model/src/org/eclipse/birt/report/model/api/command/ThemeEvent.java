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
import org.eclipse.birt.report.model.core.DesignElement;

/**
 * Notifies that the style of an element has changed.
 * 
 */

public class ThemeEvent extends NotificationEvent {

	/**
	 * Constructor.
	 * 
	 * @param obj the element that changed.
	 */

	public ThemeEvent(DesignElement obj) {
		super(obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.design.activity.NotificationEvent#getEventType
	 * ()
	 */

	public int getEventType() {
		return THEME_EVENT;
	}

}
