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

import org.eclipse.birt.report.model.activity.NotificationRecordTask;
import org.eclipse.birt.report.model.activity.RecordTask;
import org.eclipse.birt.report.model.activity.SimpleRecord;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.ReferencableStructure;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.CommandLabelFactory;

/**
 * Records adding or removing an item from a property list.
 *
 */

public class PropertyListRecord extends SimpleRecord {

	/**
	 * The element that contains the property list.
	 */

	private final DesignElement element;

	/**
	 * The element that contains the property list.
	 */

	private final ElementPropertyDefn propDefn;

	/**
	 * Reference to the property list.
	 */

	private final StructureContext context;

	/**
	 * The property list itself.
	 */

	private final List<Object> list;

	/**
	 * The item to add or remove.
	 */

	private final Object value;

	/**
	 * Whether the operation is an add or remove.
	 */

	protected final boolean isAdd;

	private final int posn;

	/**
	 * Constructor for a remove operation. Removes the item given by the member
	 * reference.
	 *
	 * @param element  the element
	 * @param context  the context to add the structure
	 * @param toRemove the structure to remove
	 *
	 */

	public PropertyListRecord(DesignElement element, StructureContext context, int posn) {
		this.element = element;
		this.isAdd = false;

		this.context = context;
		this.propDefn = context.getElementProp();

		// ensure the top of the context is just the element itself
		assert element == context.getElement();

		Object valueContainer = context.getValueContainer();
		if (valueContainer instanceof Structure) {
			list = (List) ((Structure) valueContainer).getLocalProperty(null, (PropertyDefn) context.getPropDefn());
		} else {
			list = (List) ((DesignElement) valueContainer).getLocalProperty(null,
					(ElementPropertyDefn) context.getPropDefn());
		}

		this.posn = posn;
		this.value = list.get(posn);

		label = CommandLabelFactory.getCommandLabel(MessageConstants.CHANGE_PROPERTY_MESSAGE,
				new String[] { context.getElementProp().getDisplayName() });
	}

	/**
	 * Constructor for a remove operation. Removes the item given by the member
	 * reference.
	 *
	 *
	 * @param element the element
	 * @param context the context to add the structure
	 * @param toAdd   the structure to add
	 * @param posn    the position to add
	 *
	 */

	public PropertyListRecord(DesignElement element, StructureContext context, Object toAdd, int posn) {
		this.element = element;
		this.isAdd = true;

		this.context = context;
		this.value = toAdd;
		this.propDefn = (ElementPropertyDefn) context.getElementProp();

		// ensure the top of the context is just the element itself
		assert element == context.getElement();

		Object valueContainer = context.getValueContainer();
		if (valueContainer instanceof Structure) {
			list = (List) ((Structure) valueContainer).getLocalProperty(null, (PropertyDefn) context.getPropDefn());
		} else {
			list = (List) ((DesignElement) valueContainer).getLocalProperty(null,
					(ElementPropertyDefn) context.getPropDefn());
		}

		this.posn = posn;

		label = CommandLabelFactory.getCommandLabel(MessageConstants.CHANGE_PROPERTY_MESSAGE,
				new String[] { context.getElementProp().getDisplayName() });
	}

	/**
	 * Constructor for a remove operation. Removes the item given by the member
	 * reference.
	 *
	 * @param element  the design element
	 * @param propDefn the element property definition
	 *
	 * @param theList  the property list itself
	 * @param toAdd    the object to add, not the structure
	 * @param posn     the position to add
	 */

	public PropertyListRecord(DesignElement element, ElementPropertyDefn propDefn, List theList, Object toAdd,
			int posn) {
		this.element = element;
		this.isAdd = true;

		this.value = toAdd;

		this.list = theList;
		this.context = null;
		this.propDefn = propDefn;
		this.posn = posn;

		label = CommandLabelFactory.getCommandLabel(MessageConstants.CHANGE_PROPERTY_MESSAGE,
				new String[] { propDefn.getDisplayName() });
	}

	/**
	 * Constructor for a remove operation. Removes the item given by the member
	 * reference.
	 *
	 * @param element  the element
	 * @param propDefn the element property definition
	 * @param theList  the property list itself
	 * @param toRemove the object to remove, not the structure
	 */

	public PropertyListRecord(DesignElement element, ElementPropertyDefn propDefn, List theList, int posn) {
		this.element = element;
		this.isAdd = false;
		this.list = theList;
		this.posn = posn;
		this.value = list.get(posn);
		this.context = new StructureContext(element, propDefn, null);
		this.propDefn = propDefn;

		label = CommandLabelFactory.getCommandLabel(MessageConstants.CHANGE_PROPERTY_MESSAGE,
				new String[] { propDefn.getDisplayName() });
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.design.core.activity.SimpleRecord#perform
	 * (boolean)
	 */

	@Override
	protected void perform(boolean undo) {
		boolean doAdd = (undo && !isAdd || !undo && isAdd);
		if (doAdd) {
			if (!(value instanceof Structure)) {
				list.add(posn, value);
				return;
			}

			// setup the context for the structure.

			context.add(posn, (Structure) value);
		} else {
			if (!(value instanceof Structure)) {
				list.remove(posn);
				return;
			}

			context.remove(posn);
			// if the structure list is empty now, then clear value
			Object localValue = context.getLocalValue(element.getRoot());
			if (localValue instanceof List) {
				List listValue = (List) localValue;
				if (listValue.isEmpty()) {
					context.clearValue();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.design.core.activity.AbstractElementRecord
	 * #getTarget()
	 */

	@Override
	public DesignElement getTarget() {
		if (eventTarget != null) {
			return eventTarget.getElement();
		}

		return element;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.design.core.AbstractElementRecord#getEvent
	 * ()
	 */

	@Override
	public NotificationEvent getEvent() {
		if (eventTarget != null) {
			return new PropertyEvent(eventTarget.getElement(), eventTarget.getPropName());
		}

		// Use the same notification for the done/redone and undone states.

		return new PropertyEvent(element, propDefn.getName());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#getPostTasks()
	 */

	@Override
	protected List<RecordTask> getPostTasks() {
		List<RecordTask> retList = new ArrayList<>(super.getPostTasks());
		retList.add(new NotificationRecordTask(element, getEvent()));

		// if the structure is referencable, then send notification to the
		// clients

		if (value instanceof IStructure && ((IStructure) value).isReferencable()) {
			ReferencableStructure refValue = (ReferencableStructure) value;
			retList.add(new NotificationRecordTask(refValue, getEvent()));
		}

		return retList;
	}
}
