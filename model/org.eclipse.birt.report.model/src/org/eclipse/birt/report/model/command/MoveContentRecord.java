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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.activity.LayoutRecordTask;
import org.eclipse.birt.report.model.activity.NotificationRecordTask;
import org.eclipse.birt.report.model.activity.RecordTask;
import org.eclipse.birt.report.model.activity.SimpleRecord;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.ContentEvent;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.elements.table.LayoutUtil;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.util.CommandLabelFactory;
import org.eclipse.birt.report.model.validators.ValidationExecutor;
import org.eclipse.birt.report.model.validators.ValidationNode;

/**
 * Moves a content element within its container.
 * 
 */

public class MoveContentRecord extends SimpleRecord {

	/**
	 * 
	 */
	protected Module module = null;

	/**
	 * The content element to move.
	 */

	protected DesignElement content = null;

	/**
	 * The source of this move action where the content is moved from.
	 */

	protected ContainerContext from = null;

	/**
	 * The destination of this move action where the content is moved to.
	 */
	protected ContainerContext to = null;

	/**
	 * The new position of the content.
	 */

	protected int newPosn = 0;

	/**
	 * The original position of the content.
	 */

	protected int oldPosn = 0;

	/**
	 * Constructor.The move action is done to move the position in the same
	 * container context.
	 * 
	 * @param theModule
	 * 
	 * @param containerInfor the container information.
	 * @param obj            the content element to move.
	 * @param posn           the new position of the content element.
	 */

	public MoveContentRecord(Module theModule, ContainerContext containerInfor, DesignElement obj, int posn) {
		init(theModule, containerInfor, containerInfor, obj, posn);
	}

	/**
	 * Constructs the move content record. The move action is done between diffrent
	 * container context.
	 * 
	 * @param theModule
	 * @param from
	 * @param to
	 * @param obj
	 * @param posn
	 */
	public MoveContentRecord(Module theModule, ContainerContext from, ContainerContext to, DesignElement obj,
			int posn) {
		init(theModule, from, to, obj, posn);
	}

	private void init(Module theModule, ContainerContext from, ContainerContext to, DesignElement obj, int posn) {
		module = theModule;
		this.from = from;
		this.to = to;
		content = obj;
		newPosn = posn;

		assert from != null;
		assert to != null;
		assert from.getContainerDefn() != null;
		assert to.getContainerDefn() != null;
		assert content != null;
		assert from.contains(module, content);
		assert from == to || !to.contains(module, content);

		this.oldPosn = from.indexOf(module, content);
		int count = to.getContentCount(module);
		this.newPosn = (posn == -1 || count < posn) ? count : posn;

		label = CommandLabelFactory.getCommandLabel(MessageConstants.MOVE_CONTENT_MESSAGE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.design.core.activity.SimpleRecord#perform()
	 */

	protected void perform(boolean undo) {
		if (this.from == this.to) {
			int from = undo ? newPosn : oldPosn;
			int to = undo ? oldPosn : newPosn;
			this.from.move(module, from, to);
		} else {
			if (!undo) {
				from.remove(module, content);
				to.add(module, content, newPosn);
			} else {
				to.remove(module, content);
				from.add(module, content, oldPosn);
			}
		}

		DesignElement fromElement = from.getElement();
		DesignElement toElement = to.getElement();
		if (fromElement == toElement)
			updateSharedDimension(module, fromElement);
		else {
			updateSharedDimension(module, fromElement);
			updateSharedDimension(module, toElement);
		}
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

		return to.getElement();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.activity.AbstractElementRecord
	 * #getEvent()
	 */

	public NotificationEvent getEvent() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#getValidators()
	 */

	public List<ValidationNode> getValidators() {
		List<ValidationNode> list = ValidationExecutor.getValidationNodes(from.getElement(),
				from.getTriggerSetForContainerDefn(), false);
		if (to != from)
			list.addAll(
					ValidationExecutor.getValidationNodes(to.getElement(), to.getTriggerSetForContainerDefn(), false));
		// Validate the content.

		ElementDefn contentDefn = (ElementDefn) content.getDefn();
		list.addAll(ValidationExecutor.getValidationNodes(content, contentDefn.getTriggerDefnSet(), false));

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#getPostTasks()
	 */

	protected List<RecordTask> getPostTasks() {
		if (from == to) {
			List<RecordTask> retValue = new ArrayList<RecordTask>();
			retValue.addAll(super.getPostTasks());
			NotificationEvent event = null;

			// if the element works like properties, return property event
			// instead of content event.
			if (eventTarget != null) {
				event = new PropertyEvent(eventTarget.getElement(), eventTarget.getPropName());
			} else {
				event = new ContentEvent(from, content, ContentEvent.SHIFT);
			}
			retValue.add(new NotificationRecordTask(getTarget(), event));

			// send event to cube dimension if the share dimension is changed
			sendEventToSharedDimension(getTarget(), retValue, event);

			if (!(content instanceof TableGroup || content instanceof TableRow || content instanceof Cell))
				return retValue;

			ReportItem compoundElement = LayoutUtil.getCompoundContainer(from.getElement());
			if (compoundElement == null)
				return retValue;

			retValue.add(new LayoutRecordTask(compoundElement.getRoot(), compoundElement));
			return retValue;
		}

		// now the source and destination container context is different
		List<RecordTask> retValue = new ArrayList<RecordTask>();
		retValue.addAll(super.getPostTasks());

		// if the element works like properties, return property event
		// instead of content event.
		if (eventTarget != null) {
			NotificationEvent event = new PropertyEvent(eventTarget.getElement(), eventTarget.getPropName());

			retValue.add(new NotificationRecordTask(from.getElement(), event));

			// TODO: the eventTarget is calculated for from, it is wrong, we
			// should get event target for 'to'
			retValue.add(new NotificationRecordTask(to.getElement(), event));

			return retValue;
		}

		// if from and to is related some layout changes in table or grid, then
		// send the layout event
		ReportItem fromLayout = getLayoutElement(from);
		ReportItem toLayout = getLayoutElement(to);
		if (fromLayout != null)
			retValue.add(new LayoutRecordTask(module, fromLayout));
		if (toLayout != null && toLayout != fromLayout)
			retValue.add(new LayoutRecordTask(module, toLayout));

		// Send the content changed event to the container.
		NotificationEvent event = null;

		// if do or redo, then send content remove to 'from', otherwise the
		// reverse
		boolean isDo = isDo();
		NotificationEvent styleSelectorEvent = null;
		int action = isDo ? ContentEvent.REMOVE : ContentEvent.ADD;

		event = new ContentEvent(from, content, action);
		// if undo, then the 'from' receive content add notification, therefore,
		// the style selector should be this event
		if (!isDo)
			styleSelectorEvent = event;
		if (state == DONE_STATE)
			event.setSender(sender);
		retValue.add(new NotificationRecordTask(from.getElement(), event));
		// if do or redo, send content add to 'from', otherwise the reverse
		action = isDo ? ContentEvent.ADD : ContentEvent.REMOVE;
		event = new ContentEvent(to, content, action);
		// if do or redo, then the 'to' receive content add notification,
		// therefore, the style selector should be this event
		if (isDo)
			styleSelectorEvent = event;
		if (state == DONE_STATE)
			event.setSender(sender);
		retValue.add(new NotificationRecordTask(to.getElement(), event));

		// If the content was added, then send an element added
		// event to the content.
		if (isSelector(content)) {
			assert styleSelectorEvent != null;
			retValue.add(new NotificationRecordTask(content, event, from.getElement().getRoot()));
		}

		return retValue;
	}

	/**
	 * Indicate whether the given <code>content</code> is a CSS-selecter.
	 * 
	 * @param content a given design element
	 * @return <code>true</code> if it is a predefined style.
	 */

	private boolean isSelector(DesignElement content) {
		if (!(content instanceof StyleElement))
			return false;

		return MetaDataDictionary.getInstance().getPredefinedStyle(content.getName()) != null;
	}

	/**
	 * Determines the record is done or undone.
	 * 
	 * @return true if record is done or redone, otherwise false
	 */
	boolean isDo() {
		if (state == DONE_STATE || state == REDONE_STATE)
			return true;
		return false;
	}

	/**
	 * Gets the layout element that the focus will affect.
	 * 
	 * @param focus
	 * @return
	 */
	private ReportItem getLayoutElement(ContainerContext focus) {
		DesignElement container = focus.getElement();
		if (container instanceof TableItem || container instanceof GridItem || container instanceof TableGroup
				|| container instanceof TableRow) {
			ReportItem compoundElement = LayoutUtil.getCompoundContainer(container);
			return compoundElement;
		}

		return null;
	}
}
