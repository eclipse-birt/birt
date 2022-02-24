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
import org.eclipse.birt.report.model.api.command.NameEvent;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.core.namespace.INameHelper;
import org.eclipse.birt.report.model.core.namespace.NameExecutor;

/**
 * Record for renaming element in name space.
 */

class RenameInNameSpaceRecord extends SimpleRecord {

	private DesignElement element = null;
	private String oldName = null;
	private String newName = null;
	private String nameSpaceID;
	private INameHelper nameHelper = null;

	/**
	 * Constructs the record for renaming element in name space.
	 * 
	 * @param module
	 * 
	 * @param element the element for renaming
	 * @param oldName old name
	 * @param newName new name
	 */

	RenameInNameSpaceRecord(Module module, DesignElement element, String oldName, String newName) {
		this.element = element;
		this.oldName = oldName;
		this.newName = newName;
		NameExecutor executor = new NameExecutor(module, element);
		this.nameHelper = executor.getNameHelper();
		this.nameSpaceID = executor.getNameSpaceId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.SimpleRecord#perform(boolean)
	 */

	protected void perform(boolean undo) {
		NameSpace ns = nameHelper.getNameSpace(nameSpaceID);

		if (undo) {
			ns.rename(element, newName, oldName);
		} else {
			ns.rename(element, oldName, newName);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getTarget()
	 */

	public DesignElement getTarget() {
		return nameHelper.getElement();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getEvent()
	 */

	public NotificationEvent getEvent() {
		return new NameEvent(element, oldName, newName);
	}

}
