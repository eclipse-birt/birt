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

package org.eclipse.birt.report.model.api.command;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.core.DesignElement;

/**
 * Events indicating that the element is just localized.
 * 
 */

public class ElementLocalizeEvent extends NotificationEvent {

	/**
	 * Constructor.
	 * 
	 * @param obj the element that is localized.
	 */
	public ElementLocalizeEvent(DesignElement obj) {
		super(obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.activity.NotificationEvent#getEventType()
	 */

	public int getEventType() {
		return ELEMENT_LOCALIZE_EVENT;
	}

}
