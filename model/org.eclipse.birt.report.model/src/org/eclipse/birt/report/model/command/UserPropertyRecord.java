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

package org.eclipse.birt.report.model.command;

import org.eclipse.birt.report.model.activity.SimpleRecord;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.UserPropertyEvent;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.util.CommandLabelFactory;

/**
 * ActivityRecord to add or remove a user-defined property. As with any command,
 * the caller must have verified that the operation is legal. This one command
 * handles both the add and remove operations, since they are inverse
 * operations.
 * 
 */

public class UserPropertyRecord extends SimpleRecord {

	/**
	 * Element to modify.
	 */

	private DesignElement element = null;

	/**
	 * The user property to add or remove.
	 */

	private UserPropertyDefn property = null;

	/**
	 * True to add the property, false to remove it.
	 */

	private boolean addOp = true;

	/**
	 * Constructor.
	 * 
	 * @param obj  the element to which to add or remove the user-defined property.
	 * @param prop the user-defined property.
	 * @param add  true to add the property, false to delete it.
	 */

	public UserPropertyRecord(DesignElement obj, UserPropertyDefn prop, boolean add) {
		element = obj;
		property = prop;
		addOp = add;

		if (addOp)
			label = CommandLabelFactory.getCommandLabel(MessageConstants.ADD_PROPERTY_MESSAGE,
					new String[] { prop.getDisplayName() });
		else
			label = CommandLabelFactory.getCommandLabel(MessageConstants.DROP_PROPERTY_MESSAGE,
					new String[] { prop.getDisplayName() });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.AbstractElementRecord#getEvent
	 * ()
	 */

	public NotificationEvent getEvent() {
		assert state == DONE_STATE || state == UNDONE_STATE || state == REDONE_STATE;

		int event = UserPropertyEvent.DROP;
		if (addOp && state != UNDONE_STATE || !addOp && state == UNDONE_STATE) {
			event = UserPropertyEvent.ADD;
		}
		return new UserPropertyEvent(element, property, event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.activity.SimpleRecord#perform
	 * (boolean)
	 */

	protected void perform(boolean undo) {
		if (addOp && !undo || !addOp && undo) {
			element.addUserPropertyDefn(property);
		} else {
			element.dropUserPropertyDefn(property);
		}
	}

	/**
	 * Returns whether this is an add or delete command.
	 * 
	 * @return true if add, false if delete.
	 */

	public boolean isAdd() {
		return addOp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.design.core.AbstractElementRecord#getTarget ()
	 */

	public DesignElement getTarget() {
		return element;
	}
}