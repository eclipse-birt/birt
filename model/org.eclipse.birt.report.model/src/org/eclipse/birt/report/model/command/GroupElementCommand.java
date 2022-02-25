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

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.core.namespace.NameExecutor;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.ListGroup;
import org.eclipse.birt.report.model.elements.ListItem;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;
import org.eclipse.birt.report.model.util.CommandLabelFactory;
import org.eclipse.birt.report.model.util.ContentExceptionFactory;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * This class adds, deletes and moves group elements. Group elements are treated
 * specially since data groups can be shared among report items.
 *
 */

public class GroupElementCommand extends ContentCommand {

	/**
	 * Constructs the content command with container element.
	 *
	 * @param module        the module
	 * @param containerInfo the container information
	 */

	public GroupElementCommand(Module module, ContainerContext containerInfo) {
		super(module, containerInfo);
	}

	/**
	 * Constructs the content command with container element.
	 *
	 * @param module        the module
	 * @param containerInfo the container information
	 * @param flag
	 */

	GroupElementCommand(Module module, ContainerContext containerInfo, boolean flag) {
		super(module, containerInfo, flag);
	}

	/**
	 * Constructs the content command with container element.
	 *
	 * @param module             the module
	 * @param containerInfo      the container information
	 * @param flag
	 * @param unresolveReference
	 */

	GroupElementCommand(Module module, ContainerContext containerInfo, boolean flag, boolean unresolveReference) {
		super(module, containerInfo, flag, unresolveReference);
	}

	/**
	 * Sets name of group element.
	 *
	 * @param content group element.
	 * @param stack   activity stack.
	 * @param name    new group name.
	 */

	private void addDataGroups(ListingElement tmpContainer, int groupLevel, GroupElement content)
			throws ContentException, NameException {
		List<DesignElement> tmpElements = tmpContainer.findReferredListingElements(getModule());

		for (int i = 0; i < tmpElements.size(); i++) {
			ListingElement tmpElement = (ListingElement) tmpElements.get(i);
			assert tmpElement.getRoot() == getModule();

			GroupElement tmpGroup = createNewGroupElement(tmpElement);

			GroupElementCommand cmd = new GroupElementCommand(module, newContainerContext(tmpElement), true);
			cmd.add(tmpGroup);
		}
	}

	/**
	 * @param group
	 * @param module
	 * @return
	 */

	private static List<GroupElement> createNewGroupElement(ListingElement tmpElement, int groupCount) {
		List<GroupElement> groupsToAdd = new ArrayList<>();

		for (int i = 0; i < groupCount; i++) {
			groupsToAdd.add(createNewGroupElement(tmpElement));
		}

		return groupsToAdd;
	}

	private static GroupElement createNewGroupElement(ListingElement tmpElement) {
		if (tmpElement instanceof TableItem) {
			return new TableGroup();
		} else if (tmpElement instanceof ListItem) {
			return new ListGroup();
		} else {
			assert false;
			return null;
		}
	}

	/**
	 * Sets name of group element.
	 *
	 * @param content group element.
	 * @param stack   activity stack.
	 * @param name    new group name.
	 */

	private void deleteDataGroups(ListingElement tmpContainer, int groupIndex) throws SemanticException {
		List<DesignElement> tmpElements = tmpContainer.findReferredListingElements(getModule());

		for (int i = 0; i < tmpElements.size(); i++) {
			ListingElement tmpElement = (ListingElement) tmpElements.get(i);
			assert tmpElement.getRoot() == getModule();

			GroupElement tmpGroup = (GroupElement) tmpElement.getGroups().get(groupIndex);
			GroupElementCommand cmd = new GroupElementCommand(module, newContainerContext(tmpElement), true,
					unresolveReference);
			cmd.remove(tmpGroup);
		}
	}

	private void handleColumnBinding(DesignElement content) {
		// needn't handle shared column binding.
		if (element instanceof ListingElement && content instanceof GroupElement) {
			ListingElement tmpContainer = (ListingElement) element;
			if (tmpContainer.isDataBindingReferring(module)) {
				return;
			}
		}
		// Ted 62466
		List<Object> boundColumns = null;
		Object boundColumnsObject = element.getLocalProperty(module, IReportItemModel.BOUND_DATA_COLUMNS_PROP);
		if (boundColumnsObject == null) {
			return;
		}
		if (boundColumnsObject instanceof List) {
			boundColumns = (List<Object>) boundColumnsObject;
		} else {
			return;
		}

		if (boundColumns.isEmpty()) {
			return;
		}

		String groupName = (String) content.getProperty(module, IGroupElementModel.GROUP_NAME_PROP);
		List<Integer> toCleared = new ArrayList<>();
		for (int i = 0; i < boundColumns.size(); i++) {
			ComputedColumn column = (ComputedColumn) boundColumns.get(i);
			String aggregateGroup = column.getAggregateOn();
			if (aggregateGroup != null && aggregateGroup.equals(groupName)) {
				toCleared.add(i);
			}
		}

		StructPropertyDefn structPropDefn = (StructPropertyDefn) MetaDataDictionary.getInstance()
				.getStructure(ComputedColumn.COMPUTED_COLUMN_STRUCT).getMember(ComputedColumn.AGGREGATEON_MEMBER);

		try {
			for (int i = 0; i < toCleared.size(); i++) {
				int columnIndex = (toCleared.get(i)).intValue();

				StructureContext memberRef = new StructureContext((Structure) boundColumns.get(columnIndex),
						structPropDefn, null);

				PropertyCommand propCmd = new PropertyCommand(module, element);
				propCmd.setMember(memberRef, null);
			}
		} catch (SemanticException e) {
			// should have no exception
		}
	}

	@Override
	protected void checkBeforeAdd(DesignElement content) throws ContentException, NameException {
		super.checkBeforeAdd(content);

		if (!flag && element instanceof ListingElement && content instanceof GroupElement) {
			ListingElement tmpContainer = (ListingElement) element;
			if (tmpContainer.isDataBindingReferring(module)) {
				throw ContentExceptionFactory.createContentException(focus, content,
						ContentException.DESIGN_EXCEPTION_GROUPS_CHANGE_FORBIDDEN);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.command.ContentCommand#checkBeforeMovePosition
	 * (org.eclipse.birt.report.model.core.DesignElement, int)
	 */
	@Override
	protected void checkBeforeMovePosition(DesignElement content, int newPosn) throws ContentException {
		super.checkBeforeMovePosition(content, newPosn);

		if (!flag && element instanceof ListingElement && content instanceof GroupElement) {
			ListingElement tmpContainer = (ListingElement) element;
			if (tmpContainer.isDataBindingReferring(module)) {
				throw ContentExceptionFactory.createContentException(focus, content,
						ContentException.DESIGN_EXCEPTION_GROUPS_CHANGE_FORBIDDEN);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.command.ContentCommand#checkBeforeRemove
	 * (org.eclipse.birt.report.model.core.DesignElement)
	 */

	@Override
	protected void checkBeforeRemove(DesignElement content) throws SemanticException {
		super.checkBeforeRemove(content);

		if (!flag && element instanceof ListingElement && content instanceof GroupElement) {
			ListingElement tmpContainer = (ListingElement) element;
			if (tmpContainer.isDataBindingReferring(module)) {
				throw ContentExceptionFactory.createContentException(focus, content,
						ContentException.DESIGN_EXCEPTION_GROUPS_CHANGE_FORBIDDEN);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.command.ContentCommand#doAdd(int,
	 * org.eclipse.birt.report.model.core.DesignElement)
	 */

	@Override
	protected void doAdd(int newPos, DesignElement content) throws ContentException, NameException {
		ActivityStack stack = getActivityStack();

		stack.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.ADD_ELEMENT_MESSAGE));

		try {

			super.doAdd(newPos, content);

			// special cases for the group name. Group name must be unique in
			// the scope of its container table/list. Do not support undo/redo.

			NameExecutor executor = new NameExecutor(module, content);

			String name = executor.getUniqueName();

			// if the flag is true, means current group is shared data group.
			// thus no need to create a unique name for it

			if (!flag && name != null && !name.equals(content.getName())) {
				PropertyRecord propertyRecord = new PropertyRecord(content, IGroupElementModel.GROUP_NAME_PROP, name);
				stack.execute(propertyRecord);
			}

			addDataGroups((ListingElement) element, ((GroupElement) content).getGroupLevel(), (GroupElement) content);
		} catch (NameException | ContentException e) {
			stack.rollback();
			throw e;
		}

		stack.commit();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.command.ContentCommand#doDelectAction(org
	 * .eclipse.birt.report.model.core.DesignElement, boolean)
	 */
	@Override
	protected void doDelectAction(DesignElement content) throws SemanticException {
		int groupIndex = -1;
		if (content instanceof GroupElement) {
			groupIndex = ((GroupElement) content).getGroupLevel() - 1;
		}

		super.doDelectAction(content);

		// special cases for column binding for the Group.

		deleteDataGroups((ListingElement) element, groupIndex);

		handleColumnBinding(content);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.command.ContentCommand#doMovePosition(org
	 * .eclipse.birt.report.model.core.DesignElement, int)
	 */

	@Override
	protected void doMovePosition(DesignElement content, int newPosn) throws ContentException {

		int oldPosn = focus.indexOf(module, content);

		ActivityStack stack = getActivityStack();
		stack.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.MOVE_CONTENT_MESSAGE));

		try {
			super.doMovePosition(content, newPosn);

			ListingElement tmpContainer = (ListingElement) element;
			List<DesignElement> tmpElements = tmpContainer.findReferredListingElements(getModule());

			for (int i = 0; i < tmpElements.size(); i++) {
				ListingElement tmpElement = (ListingElement) tmpElements.get(i);
				assert tmpElement.getRoot() == getModule();

				GroupElement tmpContent = (GroupElement) tmpElement.getGroups().get(oldPosn);

				GroupElementCommand tmpCmd = new GroupElementCommand(module, newContainerContext(tmpElement), true);
				tmpCmd.movePosition(tmpContent, newPosn);
			}
		} catch (ContentException e) {
			stack.rollback();
			throw e;
		}

		stack.commit();
	}

	/**
	 * Removes current group elements and adds new group elements when the data
	 * binding reference is set between two listing elements.
	 * <p>
	 *
	 * @param targetElement
	 * @throws SemanticException
	 */

	public void setupSharedDataGroups(DesignElement targetElement) throws SemanticException {
		if (!ModelUtil.isCompatibleDataBindingElements(element, targetElement)) {
			return;
		}

		List<DesignElement> groupsToRemove = new ArrayList<>(((ListingElement) element).getGroups());
		for (int i = 0; i < groupsToRemove.size(); i++) {
			GroupElementCommand tmpCmd = new GroupElementCommand(module,
					new ContainerContext(element, ListingElement.GROUP_SLOT), true, unresolveReference);
			tmpCmd.remove(groupsToRemove.get(i));
		}

		List<DesignElement> targetGroups = ((ListingElement) targetElement).getGroups();
		List<GroupElement> groupsToAdd = createNewGroupElement((ListingElement) element, targetGroups.size());

		for (int i = 0; i < groupsToAdd.size(); i++) {
			GroupElementCommand tmpCmd = new GroupElementCommand(module,
					new ContainerContext(element, ListingElement.GROUP_SLOT), true);
			tmpCmd.add(groupsToAdd.get(i));
		}
	}

	private ContainerContext newContainerContext(DesignElement tmpElement) {
		ContainerContext tmpContext = focus.createContext(tmpElement);
		if (tmpContext == null) {
			tmpContext = new ContainerContext(tmpElement, focus.getSlotID());
		}

		return tmpContext;

	}

	/**
	 * Updates the data group slot when we changes the value of
	 * <code>dataBindingRef</code>.
	 *
	 * @param oldValue
	 * @param value
	 * @throws SemanticException
	 */
	void updateBindingRef(ElementRefValue oldValue, ElementRefValue value) throws SemanticException {
		if (!(element instanceof ListingElement)) {
			return;
		}
		if (value != null && value.isResolved()) {
			setupSharedDataGroups(value.getElement());
		}
		// for dropAndClear case, value is null and if old value is resolved, we
		// should localize the groups too
		else if (value == null && oldValue.isResolved()) {
			setupSharedDataGroups(oldValue.getElement());
		}
	}
}
