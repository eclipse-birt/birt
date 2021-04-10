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
 * Indicates that the extends attribute of an element changed.
 * 
 */

public class ExtendsEvent extends NotificationEvent {

	/**
	 * Constructor.
	 * 
	 * @param obj the element that changed.
	 */

	public ExtendsEvent(DesignElement obj) {
		super(obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.design.activity.NotificationEvent#getEventType(
	 * )
	 */

	public int getEventType() {
		return EXTENDS_EVENT;
	}

}
