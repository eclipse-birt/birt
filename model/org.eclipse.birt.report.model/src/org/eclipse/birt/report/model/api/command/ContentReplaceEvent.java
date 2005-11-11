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
 * Notification event that says that one content of a container have been
 * replaced by another one. The target element is the container.
 */

public class ContentReplaceEvent extends NotificationEvent
{

	/**
	 * The slot within the container.
	 */

	protected int slot = 0;

	/**
	 * The old element replaced and causing the event.
	 */

	private DesignElement oldElement;

	/**
	 * The new element replacing and causing the event.
	 */

	private DesignElement newElement;

	/**
	 * 
	 * /** Constructs the content replace event with the container element, old
	 * element, new element and the slot within this container.
	 * 
	 * @param theContainer
	 *            the container element
	 * @param theOld
	 *            the old element replaced
	 * @param theNew
	 *            the new element
	 * @param theSlot
	 *            the slot within the container
	 */

	public ContentReplaceEvent( DesignElement theContainer,
			DesignElement theOld, DesignElement theNew, int theSlot )
	{
		super( theContainer );
		this.oldElement = theOld;
		this.newElement = theNew;
		this.slot = theSlot;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.activity.NotificationEvent#getEventType()
	 */

	public int getEventType( )
	{
		return CONTENT_REPLACE_EVENT;
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
	 * Returns the old element causing this event.
	 * 
	 * @return the old element causing this event.
	 */

	public IDesignElement getOldElement( )
	{
		return this.oldElement;
	}

	/**
	 * Returns the new element causing this event.
	 * 
	 * @return the new element causing this event.
	 */

	public IDesignElement getNewElement( )
	{
		return this.newElement;
	}

}
