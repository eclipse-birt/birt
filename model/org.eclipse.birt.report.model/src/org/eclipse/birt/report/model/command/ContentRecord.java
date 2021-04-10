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
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.activity.LayoutRecordTask;
import org.eclipse.birt.report.model.activity.NotificationRecordTask;
import org.eclipse.birt.report.model.activity.RecordTask;
import org.eclipse.birt.report.model.activity.SimpleRecord;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.ContentEvent;
import org.eclipse.birt.report.model.api.command.ElementDeletedEvent;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.elements.table.LayoutUtil;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.core.BackRef;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.IReferencableElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.DataSource;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.elements.MultiViews;
import org.eclipse.birt.report.model.elements.Parameter;
import org.eclipse.birt.report.model.elements.ParameterGroup;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.SimpleDataSet;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;
import org.eclipse.birt.report.model.elements.olap.Cube;
import org.eclipse.birt.report.model.elements.olap.TabularDimension;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.ReferenceValue;
import org.eclipse.birt.report.model.metadata.StructRefValue;
import org.eclipse.birt.report.model.util.CommandLabelFactory;
import org.eclipse.birt.report.model.validators.ValidationExecutor;
import org.eclipse.birt.report.model.validators.ValidationNode;

/**
 * Records adding a content into a container, or removing content from a
 * container. Removing a content from a container effectively deletes the
 * content from the report design .
 * 
 */

public class ContentRecord extends SimpleRecord {

	/**
	 * Container information of this record.
	 */
	protected ContainerContext containerInfo = null;

	/**
	 * The content element.
	 */

	protected DesignElement content = null;

	/**
	 * Whether to add or remove the element.
	 */

	protected boolean add = true;

	/**
	 * Memento for the old element position when deleting the element.
	 */

	protected int oldPosn = -1;

	/**
	 * The module set when using element IDs.
	 */

	protected final Module module;

	/**
	 * Constructs the record with container element, slot id, content element, and
	 * flag for adding or dropping.
	 * 
	 * @param module         the module in which this record executes
	 * @param containerInfor The container information
	 * @param contentObj     The content object to add or remove.
	 * @param isAdd          Whether to add or remove the item.
	 */

	public ContentRecord(Module module, ContainerContext containerInfor, DesignElement contentObj, boolean isAdd) {
		init(containerInfor, contentObj, -1, isAdd);
		this.module = module;
		assert module != null;
	}

	/**
	 * Constructs the record for adding with container element, slot id, content
	 * element, and position in container.
	 * 
	 * @param module        the module in which this record executes
	 * @param containerInfo The container information
	 * @param contentObj    The content object to add or remove.
	 * @param newPos        The position index where to insert the content.
	 */

	public ContentRecord(Module module, ContainerContext containerInfo, DesignElement contentObj, int newPos) {
		init(containerInfo, contentObj, newPos, true);
		this.module = module;
		assert module != null;
	}

	/**
	 * Initializes the record.
	 * 
	 * @param containerObj the container element
	 * @param theSlot      the slotID in which to put the content
	 * @param contentObj   the content object to add or remove
	 * @param newPos       the position index at which the new content is to be
	 *                     inserted
	 * @param isAdd        whether to add or remove the item
	 */

	private void init(ContainerContext theContainerInfo, DesignElement contentObj, int newPos, boolean isAdd) {
		this.containerInfo = theContainerInfo;
		content = contentObj;
		add = isAdd;

		// Verify invariants.
		assert newPos >= -1;
		assert containerInfo != null;
		assert content != null;
		assert isAdd && content.getContainer() == null || !isAdd && content.getContainer() != null;
		assert containerInfo.getContainerDefn().canContain(content);

		if (isAdd) {
			int count = containerInfo.getContentCount(module);
			oldPosn = (newPos == -1 || count < newPos) ? count : newPos;
		} else {
			oldPosn = containerInfo.indexOf(module, content);
			assert oldPosn != -1;
		}

		if (add)
			label = CommandLabelFactory.getCommandLabel(MessageConstants.ADD_ELEMENT_MESSAGE);
		else
			label = CommandLabelFactory.getCommandLabel(MessageConstants.DROP_ELEMENT_MESSAGE);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.design.core.activity.SimpleRecord#perform()
	 */

	public DesignElement getTarget() {
		if (eventTarget != null)
			return eventTarget.getElement();

		return containerInfo.getElement();
	}

	/**
	 * Not used in this class.
	 * 
	 * @return null is always returned.
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getEvent()
	 */

	public NotificationEvent getEvent() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.activity.SimpleRecord#perform
	 * (boolean)
	 */

	protected void perform(boolean undo) {
		if (add && !undo || !add && undo) {
			containerInfo.add(module, content, oldPosn);

			// Add the item to the element ID map if we are using
			// element IDs.

			if (content.getRoot() != null)
				module.manageId(content, true);
		} else {
			// Remove the element from the ID map if we are using
			// IDs.

			if (content.getRoot() != null)
				module.manageId(content, false);

			oldPosn = containerInfo.indexOf(module, content);
			containerInfo.remove(module, content);

			// for the content may be copied and pasted to the tree, so we must
			// handle the back-reference relationship

			// first, this content is referred by other elements
			if (content.hasReferences()) {
				adjustReferenceClients((IReferencableElement) content);
			}

			// second, handle the content refers other element
			adjustReferredClients(content);

			// if the content is data-source, we should do special handling
			if (content instanceof DataSource) {
				module.updateCacheForDrop((DataSource) content);
			}

		}

		// for some cube and tabular dimension, we should do localization for
		// container properties
		if (content instanceof Cube) {
			((Cube) content).updateLayout(module);
		} else if (content instanceof TabularDimension) {
			((TabularDimension) content).updateLayout(module);
		}

		DesignElement container = containerInfo.getElement();
		updateSharedDimension(module, container);
	}

	private void adjustReferenceClients(IReferencableElement referred) {
		List<BackRef> clients = new ArrayList<BackRef>(referred.getClientList());

		Iterator<BackRef> iter = clients.iterator();
		while (iter.hasNext()) {
			BackRef ref = iter.next();
			DesignElement client = ref.getElement();

			if (client != null)
				ElementBackRefRecord.unresolveBackRef(module, client, referred, ref.getPropertyName());
			else
				ElementBackRefRecord.unresolveBackRef(module, ref.getStructure(), referred, ref.getPropertyName());
		}
	}

	private void adjustReferredClients(DesignElement element) {
		List<IElementPropertyDefn> propDefns = element.getPropertyDefns();

		StyleElement style = element.getStyle(module);
		if (style != null) {
			ElementBackRefRecord.unresolveBackRef(module, element, style, IStyledElementModel.STYLE_PROP);
		}
		for (Iterator<IElementPropertyDefn> iter = propDefns.iterator(); iter.hasNext();) {
			PropertyDefn propDefn = (PropertyDefn) iter.next();

			// DO NOT consider extends and style property since this has been
			// handled in remove method.

			if (IDesignElementModel.EXTENDS_PROP.equalsIgnoreCase(propDefn.getName())
					|| IStyledElementModel.STYLE_PROP.equalsIgnoreCase(propDefn.getName()))
				continue;

			if (propDefn.getTypeCode() == IPropertyType.ELEMENT_REF_TYPE
					|| propDefn.getTypeCode() == IPropertyType.STRUCT_REF_TYPE) {
				ReferenceValue value = (ReferenceValue) element.getLocalProperty(module,
						(ElementPropertyDefn) propDefn);

				if (value != null && value.isResolved()) {
					if (value instanceof ElementRefValue) {
						((ElementRefValue) value).getTargetElement().dropClient(element);
					} else {
						((StructRefValue) value).getTargetStructure().dropClient(element);
					}
					value.unresolved(value.getName());
				}
			} else if (propDefn.getTypeCode() == IPropertyType.LIST_TYPE
					&& propDefn.getSubTypeCode() == IPropertyType.ELEMENT_REF_TYPE) {
				List<Object> valueList = (List) element.getLocalProperty(module, (ElementPropertyDefn) propDefn);
				if (valueList != null) {
					for (int i = valueList.size() - 1; i >= 0; i--) {
						ElementRefValue item = (ElementRefValue) valueList.get(i);
						if (item.isResolved()) {
							item.getTargetElement().dropClient(element);
							item.unresolved(item.getName());
						}
					}
				}
			}
		}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.validators.core.IValidatorProvider#
	 * getValidators()
	 */

	public List<ValidationNode> getValidators() {
		List<ValidationNode> list = ValidationExecutor.getValidationNodes(this.containerInfo.getElement(),
				containerInfo.getTriggerSetForContainerDefn(), false);

		// Validate the content.

		if (add && state != UNDONE_STATE || !add && state == UNDONE_STATE) {
			ElementDefn contentDefn = (ElementDefn) content.getDefn();
			list.addAll(ValidationExecutor.getValidationNodes(content, contentDefn.getTriggerDefnSet(), false));
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#getPostTasks()
	 */

	protected List<RecordTask> getPostTasks() {
		List<RecordTask> retValue = new ArrayList<RecordTask>();
		retValue.addAll(super.getPostTasks());

		DesignElement container = containerInfo.getElement();

		// if the element works like properties, return property event instead
		// of content event.
		if (eventTarget != null) {
			NotificationEvent event = new PropertyEvent(eventTarget.getElement(), eventTarget.getPropName());

			retValue.add(new NotificationRecordTask(container, event));
			content.clearListeners();

			return retValue;
		}

		if ((container instanceof TableItem && !(content instanceof MultiViews)) || container instanceof GridItem
				|| container instanceof TableGroup || container instanceof TableRow) {
			ReportItem compoundElement = LayoutUtil.getCompoundContainer(container);
			if (compoundElement != null) {
				retValue.add(new LayoutRecordTask(module, compoundElement));
			}
		}

		// Send the content changed event to the container.

		NotificationEvent event = null;
		if (add && state != UNDONE_STATE || !add && state == UNDONE_STATE)
			event = new ContentEvent(containerInfo, content, ContentEvent.ADD);
		else
			event = new ContentEvent(containerInfo, content, ContentEvent.REMOVE);

		if (state == DONE_STATE)
			event.setSender(sender);

		retValue.add(new NotificationRecordTask(container, event));

		// if container is share dimension, then send the content event to all
		// the client tabular dimension
		DesignElement e = container;
		sendEventToSharedDimension(e, retValue, event);

		// If the content was added, then send an element added
		// event to the content.

		if (add && state != UNDONE_STATE || !add && state == UNDONE_STATE) {
			if (isSelector(content))
				// content.broadcast( event, container.getRoot( ) );

				retValue.add(new NotificationRecordTask(content, event, container.getRoot()));

			return retValue;
		}

		// Broadcast to the content element of the deleted event if this content
		// is parameter or parameter group.

		if (content instanceof Parameter || content instanceof ParameterGroup || content instanceof SimpleDataSet
				|| content instanceof StyleElement) {
			event = new ElementDeletedEvent(container, content);
			if (state == DONE_STATE)
				event.setSender(sender);

			retValue.add(new NotificationRecordTask(content, event, container.getRoot()));
		} else
			content.clearListeners();

		return retValue;
	}
}