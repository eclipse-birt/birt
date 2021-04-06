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

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.activity.NotificationRecordTask;
import org.eclipse.birt.report.model.activity.RecordTask;
import org.eclipse.birt.report.model.api.activity.IEventFilter;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.activity.TransactionOption;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.command.ContentElementInfo.Step;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.elements.ContentElement;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.CommandLabelFactory;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.StructureContextUtil;

/**
 * 
 */

class ContentElementCommand extends AbstractContentCommand {

	/**
	 * Constructs the content command with container element.
	 * 
	 * @param module        the module
	 * @param containerInfo the container infor
	 */

	public ContentElementCommand(Module module, ContainerContext containerInfo) {
		super(module, containerInfo);
	}

	/**
	 * Constructs the content command with container element.
	 * 
	 * @param module        the root
	 * @param element       the element to set value
	 * @param eventTarget   the container for the element
	 * @param containerInfo the container infor
	 */

	public ContentElementCommand(Module module, DesignElement element, ContentElementInfo eventTarget) {
		super(module, element);
		this.eventTarget = eventTarget;
	}

	/**
	 * The property is a simple value list. If property is a list property, the
	 * method will check to see if the current element has the local list value, if
	 * it has, the method returns, otherwise, a copy of the list value inherited
	 * from container or parent will be set locally on the element itself.
	 * <p>
	 * This method is supposed to be used when we need to change the value of a
	 * composite property( a simple list property ). These kind of property is
	 * inherited as a whole, so when the value changed from a child element. This
	 * method will be called to ensure that a local copy will be made, so change to
	 * the child won't affect the original value in the parent.
	 * 
	 * @param ref a reference to a list property or member.
	 */

	private DesignElement makeLocalCompositeValue(DesignElement content) {
		String propName = focus.getPropertyName();
		ElementPropertyDefn prop = element.getPropertyDefn(propName);

		return makeLocalCompositeValue(element, prop, content);
	}

	/**
	 * The property is a simple value list. If property is a list property, the
	 * method will check to see if the current element has the local list value, if
	 * it has, the method returns, otherwise, a copy of the list value inherited
	 * from container or parent will be set locally on the element itself.
	 * <p>
	 * This method is supposed to be used when we need to change the value of a
	 * composite property( a simple list property ). These kind of property is
	 * inherited as a whole, so when the value changed from a child element. This
	 * method will be called to ensure that a local copy will be made, so change to
	 * the child won't affect the original value in the parent.
	 * 
	 * @param ref a reference to a list property or member.
	 */

	private DesignElement makeLocalCompositeValue(DesignElement topElement, ElementPropertyDefn prop,
			DesignElement content) {
		// Top level property is a list.

		Object localValue = topElement.getLocalProperty(module, prop);

		if (localValue != null)
			return content;

		// Make a local copy of the inherited list value.

		Object inherited = topElement.getProperty(module, prop);

		// if the action is add, the inherited can be null.

		if (inherited == null)
			return null;

		int index = -1;

		if (content != null && inherited instanceof List)
			index = ((List) inherited).indexOf(content);

		Object newValue = ModelUtil.copyValue(prop, inherited);
		ActivityStack activityStack = module.getActivityStack();

		ContainerContext context = new ContainerContext(topElement, prop.getName());

		if (newValue instanceof List) {
			List list = new ArrayList();
			PropertyRecord propRecord = new PropertyRecord(topElement, prop, list);
			activityStack.execute(propRecord);

			list = (List) newValue;
			for (int i = 0; i < list.size(); i++) {
				DesignElement tmpContent = (DesignElement) list.get(i);
				ContentRecord addRecord = new ContentRecord(module, context, tmpContent, i);
				activityStack.execute(addRecord);
			}
		} else {
			PropertyRecord propRecord = new PropertyRecord(topElement, prop, newValue);
			activityStack.execute(propRecord);
		}

		if (index != -1)
			return (DesignElement) ((List) newValue).get(index);

		return content;
	}

	protected void doAdd(int newPos, DesignElement content) throws ContentException, NameException {
		ActivityStack stack = getActivityStack();
		stack.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.ADD_ELEMENT_MESSAGE), getTransOption());

		try {
			// for add action, the content parameter can be ignored.
			makeLocalCompositeValue(content);

			// add the element
			super.doAdd(newPos, content);
			addElementNames(content);
		} catch (NameException e) {
			stack.rollback();
			throw e;
		}

		stack.commit();

	}

	/**
	 * Does some actions when the content is removed from the design tree.
	 * 
	 * @param content            the content to remove
	 * @param unresolveReference status whether to un-resolve the references
	 * @throws SemanticException
	 */

	protected void doDelectAction(DesignElement content) throws SemanticException {
		DesignElement toRemove = makeLocalCompositeValue(content);
		super.doDelectAction(toRemove);

		removeElementFromNameSpace(toRemove);
	}

	/**
	 * Does some actions when the content is removed from the design tree.
	 * 
	 * @param content            the content to remove
	 * @param unresolveReference status whether to un-resolve the references
	 */

	protected void doMove(DesignElement content, ContainerContext toContainerInfor, int newPos) {
		ActivityStack stack = getActivityStack();

		String label = CommandLabelFactory.getCommandLabel(MessageConstants.MOVE_ELEMENT_MESSAGE);
		stack.startTrans(label, getTransOption());

		DesignElement toMove = makeLocalCompositeValue(content);
		super.doMove(toMove, toContainerInfor, newPos);

		stack.commit();
	}

	/**
	 * Does some actions when the content is removed from the design tree.
	 * 
	 * @param content            the content to remove
	 * @param unresolveReference status whether to un-resolve the references
	 * @throws SemanticException
	 */

	protected void doMovePosition(DesignElement content, int newPosn) throws ContentException {
		// Skip the step if the slotID/propName has only single content.
		if (!focus.isContainerMultipleCardinality())
			return;

		ActivityStack stack = getActivityStack();

		String label = CommandLabelFactory.getCommandLabel(MessageConstants.MOVE_ELEMENT_MESSAGE);
		stack.startTrans(label, getTransOption());

		DesignElement toMove = makeLocalCompositeValue(content);
		super.doMovePosition(toMove, newPosn);

		stack.commit();
	}

	/**
	 * The method to set property.
	 * 
	 * @param prop  the definition of the property to set.
	 * @param value the new property value.
	 * @throws SemanticException if the element is a template element and users try
	 *                           to set the value of template definition to "null"
	 *                           or a non-existing element
	 */

	protected void doSetProperty(ElementPropertyDefn prop, Object value) {

		ActivityStack stack = getActivityStack();

		TransactionOption options = getTransOption();
		stack.startTrans(null, options);

		// for add action, the content parameter can be ignored.

		DesignElement tmpElement = copyTopCompositeValue();

		PropertyRecord propRecord = new PropertyRecord(tmpElement, prop, value);
		getActivityStack().execute(propRecord);

		stack.commit();
	}

	/**
	 * The property is a simple value list. If property is a list property, the
	 * method will check to see if the current element has the local list value, if
	 * it has, the method returns, otherwise, a copy of the list value inherited
	 * from container or parent will be set locally on the element itself.
	 * <p>
	 * This method is supposed to be used when we need to change the value of a
	 * composite property( a simple list property ). These kind of property is
	 * inherited as a whole, so when the value changed from a child element. This
	 * method will be called to ensure that a local copy will be made, so change to
	 * the child won't affect the original value in the parent.
	 * 
	 * @param ref a reference to a list property or member.
	 */

	private DesignElement copyTopCompositeValue() {
		if (!(element instanceof ContentElement)) {
			return null;
		}

		DesignElement topElement = eventTarget.getElement();
		String propName = eventTarget.getPropName();
		ElementPropertyDefn prop = topElement.getPropertyDefn(propName);

		makeLocalCompositeValue(topElement, prop, null);

		return matchElement(topElement);
	}

	private DesignElement matchElement(DesignElement topElement) {
		List<Step> steps = eventTarget.stepIterator();

		DesignElement tmpElement = topElement;
		for (int i = steps.size() - 1; i >= 0; i--) {
			Step step = steps.get(i);
			PropertyDefn stepPropDefn = step.stepPropDefn;
			int index = step.index;

			Object stepValue = tmpElement.getLocalProperty(module, (ElementPropertyDefn) stepPropDefn);

			if (stepPropDefn.isListType()) {
				tmpElement = (DesignElement) ((List) stepValue).get(index);
			} else
				tmpElement = (DesignElement) stepValue;
		}

		return tmpElement;
	}

	/**
	 * @param context
	 * @param value   the value should not be structure instance.
	 */

	protected void addItem(StructureContext context, Object value) {
		ActivityStack stack = getActivityStack();

		stack.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.ADD_ITEM_MESSAGE), getTransOption());

		// for add action, the content parameter can be ignored.

		DesignElement tmpElement = copyTopCompositeValue();

		// the context must be updated, since the element in the old context may
		// be not this tmp element
		context = StructureContextUtil.getLocalStructureContext(module, tmpElement, context);
		List list = context.getList(module);

		PropertyListRecord record = null;
		if (value instanceof Structure)
			assert false;
		else
			record = new PropertyListRecord(tmpElement, context.getElementProp(), list, value, list.size());

		assert record != null;

		stack.execute(record);
		record.setEventTarget(eventTarget);

		stack.commit();
	}

	/**
	 * @param context
	 * @param value   the value should not be structure instance.
	 */

	protected void removeItem(StructureContext context, int posn) {
		ActivityStack stack = getActivityStack();
		stack.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.REMOVE_ITEM_MESSAGE), getTransOption());

		// for add action, the content parameter can be ignored.

		DesignElement tmpElement = copyTopCompositeValue();
		// the context must be updated, since the element in the old context may
		// be not this tmp element
		context = StructureContextUtil.getLocalStructureContext(module, tmpElement, context);

		List list = context.getList(module);

		PropertyDefn propDefn = context.getElementProp();
		if (context.getPropDefn() != null)
			propDefn = context.getPropDefn();

		Object value = list.get(posn);

		PropertyListRecord record = null;

		if (value instanceof Structure)
			assert false;
		else {
			record = new PropertyListRecord(tmpElement, context.getElementProp(), list, posn);
		}
		assert record != null;
		stack.execute(record);
		record.setEventTarget(eventTarget);

		if (value instanceof ElementRefValue) {
			ElementRefValue refValue = (ElementRefValue) value;
			if (refValue.isResolved()) {
				ElementRefRecord refRecord = new ElementRefRecord(element, refValue.getTargetElement(),
						propDefn.getName(), false);
				stack.execute(refRecord);

			}
		}

		stack.commit();
	}

	/**
	 * Returns the transaction option for the transaction in this command. ONLY ONE
	 * property event should be sent out.
	 * 
	 * @return
	 */

	private TransactionOption getTransOption() {
		TransactionOption options = new TransactionOption();
		options.setSendTime(TransactionOption.SELF_TRANSACTION_SEND_TIME);

		options.setEventfilter(new EventFilter(eventTarget.getElement(), eventTarget.getPropName()));

		return options;
	}

	private static class EventFilter implements IEventFilter {

		private final NotificationEvent ev;

		private EventFilter(DesignElement target, String propName) {
			ev = new PropertyEvent(target, propName);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.api.activity.IEventFilter#filter(java
		 * .util.List)
		 */

		public List<RecordTask> filter(List<RecordTask> events) {
			List<RecordTask> retList = new ArrayList<RecordTask>();
			retList.add(new NotificationRecordTask(ev.getTarget(), ev));
			return retList;
		}
	}
}
