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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.activity.RecordTask;
import org.eclipse.birt.report.model.activity.SimpleRecord;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.NameEvent;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.DataSource;
import org.eclipse.birt.report.model.elements.MasterPage;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.util.CommandLabelFactory;
import org.eclipse.birt.report.model.util.ContentIterator;

/**
 * Records a change to the name of an element.
 * 
 */

public class NameRecord extends SimpleRecord {

	/**
	 * The element to change.
	 */

	protected DesignElement element = null;

	/**
	 * The new name. Can be null.
	 */

	protected String newName = null;

	/**
	 * The old name. Can be null.
	 */

	protected String oldName = null;

	/**
	 * Constructor.
	 * 
	 * @param obj  the element to change.
	 * @param name the new name.
	 */

	public NameRecord(DesignElement obj, String name) {
		element = obj;
		newName = name;
		oldName = element.getName();

		label = CommandLabelFactory.getCommandLabel(MessageConstants.SET_NAME_MESSAGE);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.SimpleRecord#perform(boolean)
	 */

	protected void perform(boolean undo) {
		element.setName(undo ? oldName : newName);

		// if container is share dimension, then send the content event to all
		// the client tabular dimension
		updateSharedDimension(element.getRoot(), element);

		// if element is data source, we should do special handling
		if (element instanceof DataSource) {
			Module root = element.getRoot();
			if (root != null) {
				if (undo)
					root.updateCacheForRename((DataSource) element, newName, oldName);
				else
					root.updateCacheForRename((DataSource) element, oldName, newName);
			}
		}

		// If the element is referenced by name, the referencing elements need
		// to be updated.
		if (element instanceof MasterPage) {
			Module root = element.getRoot();
			if (root != null) {
				if (undo)
					updatePropertyForRename(root, IStyleModel.MASTER_PAGE_PROP, newName, oldName);
				else
					updatePropertyForRename(root, IStyleModel.MASTER_PAGE_PROP, oldName, newName);
			}
		}
	}

	private void updatePropertyForRename(Module root, String propName, String oldName, String newName) {
		ContentIterator iterator = new ContentIterator(root, root);
		while (iterator.hasNext()) {
			DesignElement elem = iterator.next();
			Object prop = elem.getProperty(root, propName);
			if (prop != null && prop.equals(oldName)) {
				elem.setProperty(propName, newName);
			}
		}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.AbstractElementRecord#getEvent
	 * ()
	 */

	public NotificationEvent getEvent() {
		return new NameEvent(element, oldName, newName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#getPostTasks()
	 */

	protected List<RecordTask> getPostTasks() {
		List<RecordTask> retValue = new ArrayList<RecordTask>();
		retValue.addAll(super.getPostTasks());

		NotificationEvent event = new NameEvent(element, oldName, newName);

		// if container is share dimension, then send the content event to all
		// the client tabular dimension
		DesignElement e = element;
		sendEventToSharedDimension(e, retValue, event);

		return retValue;
	}

}