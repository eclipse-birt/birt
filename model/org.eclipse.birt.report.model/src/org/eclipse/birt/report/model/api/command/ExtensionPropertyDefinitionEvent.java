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
 * Notification event to send when the dynamic property list is changed after
 * setting an extension property value.
 */

public class ExtensionPropertyDefinitionEvent extends NotificationEvent {
	/**
	 * Constructor.
	 * 
	 * @param target the target element.
	 */

	public ExtensionPropertyDefinitionEvent(DesignElement target) {
		super(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.design.activity.NotificationEvent#getEventType(
	 * )
	 */
	public int getEventType() {
		return EXTENSION_PROPERTY_DEFINITION_EVENT;
	}

}