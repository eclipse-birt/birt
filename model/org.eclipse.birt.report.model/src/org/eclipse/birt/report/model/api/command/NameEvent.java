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
 * Identifies that the name of an element changed.
 * 
 */

public class NameEvent extends NotificationEvent {

	private String oldName = null;

	private String newName = null;

	/**
	 * Constructor.
	 * 
	 * @param obj     the element that changed.
	 * @param oldName the new name causing the event
	 * @param newName the old name of the event
	 */

	public NameEvent(DesignElement obj, String oldName, String newName) {
		super(obj);
		this.oldName = oldName;
		this.newName = newName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.design.activity.NotificationEvent#getEventType(
	 * )
	 */
	public int getEventType() {
		return NAME_EVENT;
	}

	/**
	 * Gets the new name causing the event.
	 * 
	 * @return Returns the newName.
	 */

	public String getNewName() {
		return newName;
	}

	/**
	 * Gets the old name of the event.
	 * 
	 * @return Returns the oldName.
	 */

	public String getOldName() {
		return oldName;
	}
}