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

import java.util.List;

import org.eclipse.birt.report.model.activity.SimpleRecord;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.util.CommandLabelFactory;

/**
 * Records the move of a structure within a property list or member list.
 * 
 */

public class MoveListItemRecord extends SimpleRecord {

	/**
	 * The element that contains the list.
	 */

	protected DesignElement element = null;

	/**
	 * Reference to the item to move.
	 */

	protected StructureContext itemRef = null;

	/**
	 * The old position of the item.
	 */

	protected int oldPosn = 0;

	/**
	 * The new position of the item.
	 */

	protected int newPosn = 0;

	/**
	 * Constructs a record to remove an item within a list to a new position.
	 * 
	 * @param obj     the element that contains the list
	 * @param ref     reference to the list.
	 * @param theList the list that contains the item
	 * @param from    the old position of the item
	 * @param to      the new position of the item
	 */

	public MoveListItemRecord(DesignElement obj, StructureContext ref, int from, int to) {
		assert obj != null;
		assert ref != null;

		assert obj.getPropertyDefn(ref.getElementProp().getName()) == ref.getElementProp();

		List theList = ref.getList(obj.getRoot());
		assert from >= 0 && from < theList.size();
		assert to >= 0 && to < theList.size();

		element = obj;
		itemRef = ref;

		oldPosn = from;
		newPosn = to;

		label = CommandLabelFactory.getCommandLabel(MessageConstants.MOVE_ITEM_MESSAGE);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.activity.SimpleRecord#perform
	 * (boolean)
	 */

	protected void perform(boolean undo) {
		List list = itemRef.getList(element.getRoot());
		int from = undo ? newPosn : oldPosn;
		int to = undo ? oldPosn : newPosn;

		Object value = list.remove(from);
		list.add(to, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.activity.AbstractElementRecord
	 * #getTarget()
	 */

	public DesignElement getTarget() {
		if (eventTarget != null)
			return eventTarget.getElement();

		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.AbstractElementRecord#getEvent
	 * ()
	 */

	public NotificationEvent getEvent() {
		if (eventTarget != null)
			return new PropertyEvent(eventTarget.getElement(), eventTarget.getPropName());

		// Use the same notification for the done/redone and undone states.

		return new PropertyEvent(element, itemRef.getPropDefn().getName());
	}

}