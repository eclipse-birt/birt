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
 * Indicates that an element was deleted. The target element is the one that is
 * deleted. By the time the event has been sent, the element is no longer part
 * of the design, so the client should not access the target. Just compare the
 * target to an object reference in the client code.
 * 
 */

public class ElementDeletedEvent extends NotificationEvent {

	/**
	 * Container element.
	 */

	private DesignElement container = null;

	/**
	 * Constructor.
	 * 
	 * @param obj the deleted element.
	 */

	public ElementDeletedEvent(DesignElement obj) {
		super(obj);
	}

	/**
	 * Constructor.
	 * 
	 * @param container container element
	 * @param deleted   the deleted element
	 */

	public ElementDeletedEvent(DesignElement container, DesignElement deleted) {
		super(deleted);
		this.container = container;
	}

	/**
	 * Returns the container element.
	 * 
	 * @return the container element.
	 */

	public DesignElement getContainer() {
		return container;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.design.activity.NotificationEvent#getEventType(
	 * )
	 */
	public int getEventType() {
		return ELEMENT_DELETE_EVENT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.activity.NotificationEvent#isSame(org.
	 * eclipse.birt.report.model.api.activity.NotificationEvent)
	 */
	public boolean isSame(NotificationEvent event) {
		if (!super.isSame(event))
			return false;
		ElementDeletedEvent edEvent = (ElementDeletedEvent) event;
		if (container != edEvent.getContainer())
			return false;
		return true;
	}

}
