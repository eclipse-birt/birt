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
 * Indicates that the contents of a name space changed.
 * 
 */

public class NameSpaceEvent extends NotificationEvent
{

	/**
	 * New name is added into some name space.
	 */

	public static final int ADD = 1;

	/**
	 * Name is removed from some name space.
	 */

	public static final int REMOVE = 2;

	/**
	 * The name space that changed.
	 */

	private int nameSpaceID = 0;

	/**
	 * The action which causes this event.
	 */

	private int action;

	/**
	 * Constructor.
	 * 
	 * @param root
	 *            the root element
	 * @param id
	 *            the id of the name space that changed
	 * @param theAction
	 *            the action causing this event
	 */

	public NameSpaceEvent( DesignElement root, int id, int theAction )
	{
		super( root );
		nameSpaceID = id;
		action = theAction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.activity.NotificationEvent#getEventType()
	 */

	public int getEventType( )
	{
		return NAME_SPACE_EVENT;
	}

	/**
	 * Returns the action which causes this event. The value can be:
	 * <ul>
	 * <li><code>ADD</code>
	 * <li><code>REMOVE</code>
	 * </ul>
	 * 
	 * @return the action causing this event.
	 */

	public int getAction( )
	{
		return action;
	}

	/**
	 * Returns the id of the name space that changed.
	 * 
	 * @return the id of the name space that changed.
	 */

	public int getNameSpaceID( )
	{
		return nameSpaceID;
	}
}
