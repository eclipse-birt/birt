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

package org.eclipse.birt.report.model.api.core;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.core.Module;

/**
 * Notification event that says that the module is disposed. The listener can
 * find out which module changed by calling
 * {@link org.eclipse.birt.report.model.api.activity.NotificationEvent#getTarget}(
 * ).
 */

public class DisposeEvent extends NotificationEvent {

	/**
	 * Constructs a dispose event with the changed module.
	 * 
	 * @param module the changed module
	 */

	public DisposeEvent(Module module) {
		super(module);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.activity.NotificationEvent#getEventType()
	 */
	public int getEventType() {
		return DISPOSE_EVENT;
	}
}