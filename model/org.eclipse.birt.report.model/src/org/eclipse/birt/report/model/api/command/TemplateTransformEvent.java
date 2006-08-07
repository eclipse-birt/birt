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
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.core.DesignElement;

/**
 * Notification event that says that transformation betweem a template element
 * and a report item or data set occurs. The target element is the container.
 */

public class TemplateTransformEvent extends NotificationEvent
{

	/**
	 * The slot within the container.
	 */

	protected int slot = 0;

	/**
	 * The from element which this event transforms from.
	 */

	private DesignElement fromElement;

	/**
	 * The new element which this event transforms from.
	 */

	private DesignElement toElement;

	/**
	 * 
	 * /** Constructs the content replace event with the container element, from
	 * element, to element and the slot within this container.
	 * 
	 * @param theContainer
	 *            the container element
	 * @param from
	 *            the element which the transformation starts from
	 * @param to
	 *            the element which the transformation ends to
	 * @param theSlot
	 *            the slot within the container
	 */

	public TemplateTransformEvent( DesignElement theContainer,
			DesignElement from, DesignElement to, int theSlot )
	{
		super( theContainer );
		this.fromElement = from;
		this.toElement = to;
		this.slot = theSlot;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.activity.NotificationEvent#getEventType()
	 */

	public int getEventType( )
	{
		return TEMPLATE_TRANSFORM_EVENT;
	}

	/**
	 * Returns the slot id within the container.
	 * 
	 * @return the slot id within the container
	 */

	public int getSlot( )
	{
		return slot;
	}

	/**
	 * Returns the element which this event transforms from.
	 * 
	 * @return the element which this event transforms from.
	 */

	public IDesignElement getFrom( )
	{
		return this.fromElement;
	}

	/**
	 * Returns the element which this event transforms to.
	 * 
	 * @return the element which this event transforms to.
	 */

	public IDesignElement getTo( )
	{
		return this.toElement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.activity.NotificationEvent#isSame(org.eclipse.birt.report.model.api.activity.NotificationEvent)
	 */
	public boolean isSame( NotificationEvent event )
	{
		if ( !super.isSame( event ) )
			return false;
		TemplateTransformEvent transEvent = (TemplateTransformEvent) event;
		if ( slot != transEvent.getSlot( )
				|| fromElement != transEvent.getFrom( )
				|| toElement != transEvent.getTo( ) )
			return false;
		return true;
	}
}
