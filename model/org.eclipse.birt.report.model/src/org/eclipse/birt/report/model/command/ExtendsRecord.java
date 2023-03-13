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

package org.eclipse.birt.report.model.command;

import org.eclipse.birt.report.model.activity.SimpleRecord;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.ExtendsEvent;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.util.CommandLabelFactory;

/**
 * Sets the extends attribute of an element.
 *
 */

public class ExtendsRecord extends SimpleRecord {

	/**
	 * The element to modify.
	 */

	private DesignElement element = null;

	/**
	 * The new parent element. Can be null.
	 */

	protected ElementRefValue newParent = null;

	/**
	 * The old parent element. Can be null.
	 */

	private DesignElement oldParent = null;

	/**
	 * The old parent element name, if the name was unresolved.
	 */

	private String oldName = null;

	/**
	 * Constructor.
	 *
	 * @param obj    the element to modify.
	 * @param parent the new parent element.
	 */

	// public ExtendsRecord( DesignElement obj, DesignElement parent )
	// {
	// element = obj;
	// newParent = parent;
	// oldParent = obj.getExtendsElement( );
	// if ( oldParent == null )
	// oldName = obj.getExtendsName( );
	//
	// assert element != null;
	// assert parent == null || parent.getDefn( ) == element.getDefn( );
	// assert parent == null || !parent.isKindOf( element );
	//
	// label = ModelMessages.getMessage( MessageConstants.SET_EXTENDS_MESSAGE );
	//
	// }
	/**
	 * Constructor.
	 *
	 * @param obj    the element to modify.
	 * @param parent the style to set.
	 */

	public ExtendsRecord(DesignElement obj, ElementRefValue parent) {
		assert obj != null;

		element = obj;
		newParent = parent;
		oldParent = obj.getExtendsElement();
		if (oldParent == null) {
			oldName = obj.getExtendsName();
		}

		label = CommandLabelFactory.getCommandLabel(MessageConstants.SET_EXTENDS_MESSAGE);

	}

	/**
	 * @see org.eclipse.birt.report.model.activity.SimpleRecord#perform(boolean)
	 */

	@Override
	protected void perform(boolean undo) {
		if (undo) {
			if (oldName != null) {
				element.setExtendsName(oldName);
			} else {
				element.setExtendsElement(oldParent);
			}
		} else {
			DesignElement parent = newParent == null ? null : newParent.getElement();
			element.setExtendsElement(parent);
		}
	}

	/**
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getTarget()
	 */

	@Override
	public DesignElement getTarget() {
		return element;
	}

	/**
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getEvent()
	 */

	@Override
	public NotificationEvent getEvent() {
		return new ExtendsEvent(element);
	}

}
