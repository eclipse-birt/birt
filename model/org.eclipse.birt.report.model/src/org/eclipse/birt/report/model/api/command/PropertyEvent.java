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
 * Notification event to send when a property changes. The listener can find out
 * which property changed by calling {@link #getPropertyName}( ). The listener
 * can get the new property value from the focus object.
 *
 */

public class PropertyEvent extends NotificationEvent {

	/**
	 * The name of the property that has changed.
	 */

	protected String propertyName;

	/**
	 * Constructor.
	 *
	 * @param target   the target element.
	 * @param propName the name of the changed property.
	 */

	public PropertyEvent(DesignElement target, String propName) {
		super(target);
		propertyName = propName;
	}

	/**
	 * Returns the name of the property that changed. The name is the internal,
	 * non-localized property id.
	 *
	 * @return the property name.
	 */

	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * Sets the property name. Should be called only by the command that created the
	 * event.
	 *
	 * @param propName the propertyName to set.
	 */

	public void setPropertyName(String propName) {
		propertyName = propName;
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
		return PROPERTY_EVENT;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.activity.NotificationEvent#isSame(org.
	 * eclipse.birt.report.model.api.activity.NotificationEvent)
	 */
	@Override
	public boolean isSame(NotificationEvent event) {
		if (!super.isSame(event)) {
			return false;
		}
		PropertyEvent propEvent = (PropertyEvent) event;
		if ((propertyName != null && !propertyName.equals(propEvent.getPropertyName())) || (propertyName == null && propEvent.getPropertyName() != null)) {
			return false;
		}
		return true;
	}

}
