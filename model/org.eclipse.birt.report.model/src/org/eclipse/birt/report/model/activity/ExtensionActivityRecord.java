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

package org.eclipse.birt.report.model.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.ExtensionPropertyDefinitionEvent;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.extension.IElementCommand;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ExtendedItem;

/**
 * The activity record provides the mechanism for performing a low-level change
 * to the ROM extension elements.
 */

public final class ExtensionActivityRecord extends ActivityRecord {

	/**
	 * The effective extended element command this record has.
	 */

	private IElementCommand extRecord = null;

	/**
	 * The design element, that is, a report element. All report element classes
	 * derives from DesignElement
	 */

	private DesignElement element = null;

	/**
	 * The extension property name.
	 */

	private String propName = null;

	/**
	 * Constructs the extension activity record with the effective extended element
	 * command.
	 * 
	 * @param extCommand the effective extended element command
	 */

	public ExtensionActivityRecord(IElementCommand extCommand) {
		assert extCommand != null;
		extRecord = extCommand;
		setLabel(extCommand.getLabel());

		if (extCommand.getElementHandle() != null)
			element = extCommand.getElementHandle().getElement();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#canRedo()
	 */

	public boolean canRedo() {
		return extRecord.canRedo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#canUndo()
	 */

	public boolean canUndo() {
		return extRecord.canUndo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#destroy()
	 */

	public void destroy() {
		super.destroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#getTransNo()
	 */

	public int getTransNo() {
		return super.getTransNo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#execute()
	 */

	public void execute() {
		extRecord.execute();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#undo()
	 */

	public void undo() {
		extRecord.undo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#redo()
	 */

	public void redo() {
		extRecord.redo();
	}

	/**
	 * Returns the design element.
	 * 
	 * @return the design element
	 */

	public DesignElement getTarget() {
		return element;
	}

	/**
	 * gets the event for extension element. The
	 * <code>ExtensionPropertyDefinitionEvent</code> will be returned when the
	 * extension element is not null and the dynamic property list is changed.
	 * <code>propertyEvent</code> will be returned if not the above case.
	 * 
	 * @return event
	 */
	private NotificationEvent getEvent() {
		assert element != null;
		NotificationEvent event = null;
		assert element instanceof ExtendedItem;

		IReportItem extElement = ((ExtendedItem) element).getExtendedElement();
		if (extElement != null && extElement.refreshPropertyDefinition()) {
			event = new ExtensionPropertyDefinitionEvent(element);
		} else
			event = new PropertyEvent(element, propName);
		// Use the same notification for the done/redone and undone states.
		return event;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#getEventChain()
	 */

	public void rollback() {
		undo();
		setState(ActivityRecord.UNDONE_STATE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#getPostTasks()
	 */

	protected List<RecordTask> getPostTasks() {
		if (element == null)
			return Collections.emptyList();

		List<RecordTask> retList = new ArrayList<RecordTask>();
		retList.add(new NotificationRecordTask(element, getEvent()));
		return retList;
	}
}