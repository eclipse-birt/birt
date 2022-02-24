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
 * Indicates that the contents of a name space changed.
 * 
 * @deprecated Since BIRT 2.1, there will no such event send by BIRT
 * 
 */

public class NameSpaceEvent extends NotificationEvent {

	/**
	 * New name is added into some name space.
	 */

	public static final int ADD = 1;

	/**
	 * Name is removed from some name space.
	 */

	public static final int REMOVE = 2;

	/**
	 * The element in namespace is renamed.
	 */

	public static final int ELEMENT_RENAMED = 3;

	/**
	 * The name space that changed.
	 */

	private int nameSpaceID = 0;

	/**
	 * The action which causes this event.
	 */

	private int action;

	private DesignElement element;

	/**
	 * Constructor.
	 * 
	 * @param root      the root element
	 * @param id        the id of the name space that changed
	 * @param theAction the action causing this event
	 */

	public NameSpaceEvent(DesignElement root, int id, int theAction) {
		super(root);
		nameSpaceID = id;
		action = theAction;
	}

	/**
	 * Constructor.
	 * 
	 * @param root      the root element
	 * @param id        the id of the name space that changed
	 * @param element   the element to modify
	 * @param theAction the action causing this event
	 */

	public NameSpaceEvent(DesignElement root, int id, DesignElement element, int theAction) {
		super(root);
		nameSpaceID = id;
		this.element = element;
		action = theAction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.design.activity.NotificationEvent#getEventType(
	 * )
	 */

	public int getEventType() {
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

	public int getAction() {
		return action;
	}

	/**
	 * Returns the id of the name space that changed.
	 * 
	 * @return the id of the name space that changed.
	 */

	public int getNameSpaceID() {
		return nameSpaceID;
	}

	/**
	 * Returns the element whose name is changed for <code>ELEMENT_RENAMED</code>.
	 * Otherwise, return null.
	 * 
	 * @return Returns the element.
	 */

	public DesignElement getElement() {
		return element;
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
		NameSpaceEvent nsEvent = (NameSpaceEvent) event;
		if (action != nsEvent.getAction() || nameSpaceID != nsEvent.getNameSpaceID() || element != nsEvent.getElement())
			return false;
		return true;
	}

}
