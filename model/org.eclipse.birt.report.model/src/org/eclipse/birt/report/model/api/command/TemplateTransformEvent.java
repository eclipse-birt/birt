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
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;

/**
 * Notification event that says that transformation betweem a template element
 * and a report item or data set occurs. The target element is the container.
 */

public class TemplateTransformEvent extends NotificationEvent {

	/**
	 * The slot within the container.
	 */

	protected final ContainerContext focus;

	/**
	 * The from element which this event transforms from.
	 */

	private final DesignElement fromElement;

	/**
	 * The new element which this event transforms from.
	 */

	private final DesignElement toElement;

	/**
	 * 
	 * /** Constructs the content replace event with the container element, from
	 * element, to element and the slot within this container.
	 * 
	 * @param containerInfo the container information
	 * @param from          the element which the transformation starts from
	 * @param to            the element which the transformation ends to
	 */

	public TemplateTransformEvent(ContainerContext containerInfo, DesignElement from, DesignElement to) {
		super(containerInfo.getElement());
		this.fromElement = from;
		this.toElement = to;
		this.focus = containerInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.activity.NotificationEvent#getEventType()
	 */

	public int getEventType() {
		return TEMPLATE_TRANSFORM_EVENT;
	}

	/**
	 * Returns the slot id within the container.
	 * 
	 * @return the slot id within the container
	 */

	public int getSlot() {
		return focus.getSlotID();
	}

	/**
	 * Returns the element which this event transforms from.
	 * 
	 * @return the element which this event transforms from.
	 */

	public IDesignElement getFrom() {
		return this.fromElement;
	}

	/**
	 * Returns the element which this event transforms to.
	 * 
	 * @return the element which this event transforms to.
	 */

	public IDesignElement getTo() {
		return this.toElement;
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
		TemplateTransformEvent transEvent = (TemplateTransformEvent) event;
		if (!focus.equals(transEvent.focus) || fromElement != transEvent.getFrom() || toElement != transEvent.getTo())
			return false;
		return true;
	}
}
