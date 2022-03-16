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
	@Override
	public int getEventType() {
		return EXTENSION_PROPERTY_DEFINITION_EVENT;
	}

}
