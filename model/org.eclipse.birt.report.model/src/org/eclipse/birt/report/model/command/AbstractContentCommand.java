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

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.activity.AbstractElementCommand;
import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ContentElement;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.MultiViews;
import org.eclipse.birt.report.model.elements.interfaces.IMultiViewsModel;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.IContainerDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.SlotDefn;
import org.eclipse.birt.report.model.util.CommandLabelFactory;
import org.eclipse.birt.report.model.util.ContentExceptionFactory;
import org.eclipse.birt.report.model.util.LevelContentIterator;

/**
 * This class adds, deletes and moves content elements. Adding a content element
 * to a container is the only way to add a new element to the design. Similarly,
 * removing an element from its container is the only way to delete an element
 * from the design.
 * <p>
 * Note: be sure to use the move operation if your intent is to move an element
 * from one place to another. Do not use a drop followed by an add. The move
 * command verifies that the move can be done before starting the action. If you
 * instead do a drop followed by an add, you'll end up with the element deleted
 * if it cannot be added into its new location.
 * 
 */

abstract class AbstractContentCommand extends AbstractElementCommand {

	/**
	 * Containment infromation.
	 */

	protected final ContainerContext focus;

	/**
	 * Where to send the event.
	 */

	protected ContentElementInfo eventTarget;

	/**
	 * 
	 * @param module
	 * @param obj
	 */
	public AbstractContentCommand(Module module, DesignElement obj) {
		super(module, obj);
		focus = null;
	}

	/**
	 * Constructs the content command with container element.
	 * 
	 * @param module        the module
	 * @param containerInfo the container infor
	 */

	public AbstractContentCommand(Module module, ContainerContext containerInfo) {
		super(module, containerInfo.getElement());
		this.focus = containerInfo;

		eventTarget = getEventTarget();
	}

	/**
	 * Adds a new element into a container and specifies the position in the
	 * container. Virtually all elements must reside in a container. Containers are
	 * identified by a container ID. The application creates the element object,
	 * then adds it to the container here. The undo of this operation effectively
	 * deletes the element.
	 * 
	 * @param content the element to add
	 * @param newPos  the position index at which the content to be inserted. If
	 *                it's -1, the content will be inserted at the end of the slot.
	 * @throws ContentException if the content cannot be added into this container.
	 * @throws NameException    if the name of the content exists in name space.
	 */

	public final void add(DesignElement content, int newPos) throws ContentException, NameException {
		if (newPos < 0 && newPos != -1)
			throw ContentExceptionFactory.createContentException(focus,
					ContentException.DESIGN_EXCEPTION_INVALID_POSITION);
		;

		if (content.getContainer() != null)
			throw ContentExceptionFactory.createContentException(focus, content,
					ContentException.DESIGN_EXCEPTION_CONTENT_ALREADY_INSERTED);

		if (eventTarget != null && !(this instanceof ContentElementCommand)) {
			ContentElementCommand attrCmd = new ContentElementCommand(module, focus);

			attrCmd.add(content, newPos);
			return;
		}

		if (content instanceof GroupElement && element instanceof ListingElement
				&& !(this instanceof GroupElementCommand)) {
			GroupElementCommand attrCmd = new GroupElementCommand(module, focus);

			attrCmd.add(content, newPos);
			return;
		}

		try {
			checkBeforeAdd(content);
		} catch (ContentException e) {
			throw e;
		} catch (NameException e) {
			throw e;
		}

		doAdd(newPos, content);
	}

	/**
	 * Validates the context before adding an element.
	 * 
	 * @param content the element to add
	 * @throws ContentException if <code>content</code> cannot resides in
	 * @throws NameException    if the name of <code>content</code> duplicates with
	 *                          others
	 */

	protected void checkBeforeAdd(DesignElement content) throws ContentException, NameException {
		// Ensure that the content can be put into the container.

		ElementDefn metaData = (ElementDefn) element.getDefn();
		if (!metaData.isContainer())
			throw ContentExceptionFactory.createContentException(focus,
					ContentException.DESIGN_EXCEPTION_NOT_CONTAINER);
		IContainerDefn containerDefn = focus.getContainerDefn();
		if (containerDefn == null)
			throw ContentExceptionFactory.createContentException(focus,
					ContentException.DESIGN_EXCEPTION_SLOT_NOT_FOUND);
		if (!containerDefn.canContain(content))
			throw ContentExceptionFactory.createContentException(focus, content,
					ContentException.DESIGN_EXCEPTION_WRONG_TYPE);

		// This element is already the content of the element to add.

		if (element.isContentOf(content))
			throw ContentExceptionFactory.createContentException(focus, content,
					ContentException.DESIGN_EXCEPTION_RECURSIVE);

		// If this is a single-item slot, ensure that the slot is empty.

		if (!focus.isContainerMultipleCardinality() && focus.getContentCount(module) > 0) {
			throw ContentExceptionFactory.createContentException(focus, ContentException.DESIGN_EXCEPTION_SLOT_IS_FULL);
		}
	}

	/**
	 * Performs the action to add the given element.
	 * 
	 * @param newPos  the new position to add
	 * @param content the element
	 * @throws ContentException
	 * @throws NameException
	 */

	protected void doAdd(int newPos, DesignElement content) throws ContentException, NameException {
		// Add the item to the container.

		ContentRecord addRecord;
		if (newPos == -1) {
			addRecord = new ContentRecord(module, focus, content, true);
		} else {
			addRecord = new ContentRecord(module, focus, content, newPos);
		}

		addRecord.setEventTarget(eventTarget);

		ActivityStack stack = getActivityStack();
		stack.execute(addRecord);
	}

	/**
	 * Adds a new element into a container. Virtually all elements must reside in a
	 * container. Containers are identified by a container ID. The application
	 * creates the element object, then adds it to the container here. The undo of
	 * this operation effectively deletes the element.
	 * 
	 * @param content the element to add
	 * @param slotID  the slot in which to add the component
	 * @throws ContentException if the content cannot be added into this container.
	 * @throws NameException    if the name of the content exists in name space.
	 */

	public final void add(DesignElement content) throws ContentException, NameException {
		add(content, -1);
	}

	/**
	 * Removes an item from its container. This is equivalent to deleting the
	 * element from the design. Because the element is being deleted, we must clean
	 * up all references to or from the element. References include:
	 * <p>
	 * <ul>
	 * <li>The elements that this content extends.
	 * <li>The elements that extend this content.
	 * <li>The style that this content uses.
	 * <li>The elements that use this style.
	 * <li>The elements that this content contains.
	 * <li>The name space that contains this content.
	 * </ul>
	 * 
	 * @param content the element to remove
	 * @param slotID  the slot from which to remove the content
	 * @throws SemanticException if this content cannot be removed from container.
	 */

	public final void remove(DesignElement content) throws SemanticException {

		assert content != null;

		if (eventTarget != null && !(this instanceof ContentElementCommand)) {
			ContentElementCommand attrCmd = new ContentElementCommand(module, focus);

			attrCmd.remove(content);
			return;
		}

		if (content instanceof GroupElement && element instanceof ListingElement
				&& !(this instanceof GroupElementCommand)) {

			boolean flag = ((ContentCommand) this).flag;
			boolean unresolveReference = ((ContentCommand) this).unresolveReference;
			GroupElementCommand attrCmd = new GroupElementCommand(module, focus, flag, unresolveReference);

			attrCmd.remove(content);
			return;
		}

		checkBeforeRemove(content);

		doRemove(content);
	}

	/**
	 * Validates the context before removing an element.
	 * 
	 * @param content the element to remove
	 * @throws SemanticException if <code>content</code> does not reside in the
	 *                           current context.
	 */

	protected void checkBeforeRemove(DesignElement content) throws SemanticException {
		// Ensure that the content can be dropped from the container.

		if (!element.getDefn().isContainer())
			throw ContentExceptionFactory.createContentException(focus,
					ContentException.DESIGN_EXCEPTION_NOT_CONTAINER);
		IContainerDefn containerDefn = focus.getContainerDefn();
		if (containerDefn == null)
			throw ContentExceptionFactory.createContentException(focus,
					ContentException.DESIGN_EXCEPTION_SLOT_NOT_FOUND);
		if (!focus.contains(module, content))
			throw ContentExceptionFactory.createContentException(focus, content,
					ContentException.DESIGN_EXCEPTION_CONTENT_NOT_FOUND);

	}

	private void doRemove(DesignElement content) throws SemanticException {
		// Prepare the transaction.

		ActivityStack stack = getActivityStack();

		stack.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.DROP_ELEMENT_MESSAGE));

		stack.startFilterEventTrans("Drop elements"); //$NON-NLS-1$

		try {
			doDelectAction(content);
		} catch (SemanticException ex) {
			stack.rollback();
			stack.rollback();
			throw ex;
		}

		// Remove the element itself.

		ContentRecord dropRecord = new ContentRecord(module, focus, content, false);
		dropRecord.setEventTarget(eventTarget);
		stack.execute(dropRecord);

		stack.commit();

		// if the container is multi-views, and now it is empty, then delete the
		// multi-views
		if (this.focus.getElement() instanceof MultiViews) {
			DesignElement multiViews = focus.getElement();
			// if not virtual element, then do delete
			if (!multiViews.isVirtualElement()) {
				ContainerContext context = multiViews.getContainerInfo();
				if (context != null) {
					List views = multiViews.getListProperty(module, IMultiViewsModel.VIEWS_PROP);
					if (views == null || views.isEmpty()) {
						stack.startTrans("Drop multiView"); //$NON-NLS-1$
						try {
							ContentCommand cmd = new ContentCommand(module, context);
							cmd.remove(multiViews);
							stack.commit();
						} catch (SemanticException ex) {
							stack.rollback();
							stack.rollback();
							throw ex;
						}
					}
				}
			}
		}

		stack.commit();
	}

	/**
	 * Does some actions when the content is removed from the design tree.
	 * 
	 * @param content the content to remove
	 * @throws SemanticException
	 */

	protected void doDelectAction(DesignElement content) throws SemanticException {
		// Skip this step if the element is not a container.

		ElementDefn metaData = (ElementDefn) content.getDefn();
		if (!metaData.isContainer())
			return;

		LevelContentIterator iter = new LevelContentIterator(module, content, 1);
		while (iter.hasNext()) {
			DesignElement tmpContent = iter.next();

			// there is only one case that the level content iterator will
			// return the element that is not content of the target: shared
			// dimension, we do not localize the hierarchies, so the level
			// content iterator can retrieve them however the children do not
			// really lie in it
			if (!tmpContent.isContentOf(content))
				continue;

			AbstractContentCommand cmd = null;
			if (this instanceof ContentCommand)
				cmd = new ContentCommand(module, tmpContent.getContainerInfo(), true,
						((ContentCommand) this).unresolveReference);
			else if (this instanceof ContentElementCommand)
				cmd = new ContentElementCommand(module, tmpContent.getContainerInfo());
			if (cmd != null)
				cmd.remove(tmpContent);
		}
	}

	/**
	 * Moves an element from one slot to another. The destination slot can be in the
	 * same element (unusual) or a different element (usual case.) Use the other
	 * form of this method to move an element within the same slot.
	 * 
	 * @param content          The element to move.
	 * @param toContainerInfor the destination container information.
	 * @throws ContentException
	 */

	public final void move(DesignElement content, ContainerContext toContainerInfor) throws ContentException {
		move(content, toContainerInfor, -1);
	}

	/**
	 * Moves an element from one slot to another at the specified position. The
	 * destination slot can be in the same element (unusual) or a different element
	 * (usual case). Use the other form of this method to move an element within the
	 * same slot.
	 * 
	 * @param content          The element to move.
	 * @param toContainerInfor the destination container information.
	 * @param newPos           the position in the target slot to which the content
	 *                         will be moved. If it is greater than the size of the
	 *                         target slot, the content will be appended at the end
	 *                         of the slot.
	 * @throws ContentException
	 */

	public final void move(DesignElement content, ContainerContext toContainerInfor, int newPos)
			throws ContentException {
		// if the source and destination is the same, then do the move position
		if (focus.equals(toContainerInfor)) {
			movePosition(content, newPos);
			return;
		}
		if (eventTarget != null && !(this instanceof ContentElementCommand)) {
			ContentElementCommand attrCmd = new ContentElementCommand(module, focus);

			attrCmd.move(content, toContainerInfor, newPos);
			return;
		}

		checkBeforeMove(content, toContainerInfor);

		doMove(content, toContainerInfor, newPos);
	}

	/**
	 * Moves an element from one slot/property to another at the specified position.
	 * The destination slot can be in the same element (unusual) or a different
	 * element (usual case). Use the other form of this method to move an element
	 * within the same slot.
	 * 
	 * @param content          the element to move.
	 * @param toContainerInfor the destination
	 * @param newPos           the position in the target slot to which the content
	 *                         will be moved. If it is -1 or greater than the size
	 *                         of the target slot, the content will be appended at
	 *                         the end of the slot.
	 */

	protected void doMove(DesignElement content, ContainerContext toContainerInfor, int newPos) {
		MoveContentRecord record = new MoveContentRecord(module, focus, toContainerInfor, content, newPos);

		record.setEventTarget(eventTarget);

		getActivityStack().execute(record);
	}

	/**
	 * Validates the context before moving an element.
	 * 
	 * @param content          the element to move
	 * @param toContainerInfor the destination
	 * @throws ContentException if <code>content</code> cannot resides in
	 *                          <code>toContainerInfor</code>
	 */

	protected void checkBeforeMove(DesignElement content, ContainerContext toContainerInfor) throws ContentException {
		assert content != null;
		assert toContainerInfor != null;

		// Cannot put an element inside itself.
		if (toContainerInfor.getElement().isContentOf(content))
			throw ContentExceptionFactory.createContentException(toContainerInfor, content,
					ContentException.DESIGN_EXCEPTION_RECURSIVE);

		// Ensure that the content can be put into the container.

		if (!element.getDefn().isContainer())
			throw ContentExceptionFactory.createContentException(focus,
					ContentException.DESIGN_EXCEPTION_NOT_CONTAINER);
		if (!toContainerInfor.getElement().getDefn().isContainer())
			throw ContentExceptionFactory.createContentException(toContainerInfor,
					ContentException.DESIGN_EXCEPTION_NOT_CONTAINER);
		IContainerDefn containerDefn = focus.getContainerDefn();
		if (containerDefn == null)
			throw ContentExceptionFactory.createContentException(focus,
					ContentException.DESIGN_EXCEPTION_SLOT_NOT_FOUND);
		containerDefn = toContainerInfor.getContainerDefn();
		if (containerDefn == null)
			throw ContentExceptionFactory.createContentException(toContainerInfor,
					ContentException.DESIGN_EXCEPTION_SLOT_NOT_FOUND);
		if (!focus.contains(module, content))
			throw ContentExceptionFactory.createContentException(focus, content,
					ContentException.DESIGN_EXCEPTION_CONTENT_NOT_FOUND);

		containerDefn = toContainerInfor.getContainerDefn();
		if (!containerDefn.canContain(content))
			throw ContentExceptionFactory.createContentException(toContainerInfor, content,
					ContentException.DESIGN_EXCEPTION_WRONG_TYPE);
		if (!toContainerInfor.isContainerMultipleCardinality() && toContainerInfor.getContentCount(module) > 0)
			throw ContentExceptionFactory.createContentException(toContainerInfor,
					ContentException.DESIGN_EXCEPTION_SLOT_IS_FULL);
		return;
	}

	/**
	 * Moves an element from one position to another within the same slot.
	 * <p>
	 * For example, if a slot has A, B, C elements in order, when move A element to
	 * <code>newPosn</code> with the value 2, the sequence becomes B, A, C.
	 * 
	 * 
	 * @param content The element to move.
	 * @param slotID  The slot that contains the element.
	 * @param newPosn The new position within the slot. Note that the range of
	 *                <code>newPos</code> is from 0 to the number of element in the
	 *                slot with the ID <code>slotID</code>.
	 * @throws ContentException if the content cannot be moved to new container.
	 */

	public final void movePosition(DesignElement content, int newPosn) throws ContentException {
		assert content != null;

		if (eventTarget != null && !(this instanceof ContentElementCommand)) {
			ContentElementCommand attrCmd = new ContentElementCommand(module, focus);

			attrCmd.movePosition(content, newPosn);
			return;
		}

		if (content instanceof GroupElement && element instanceof ListingElement
				&& !(this instanceof GroupElementCommand)) {
			GroupElementCommand attrCmd = new GroupElementCommand(module, focus);

			attrCmd.movePosition(content, newPosn);
			return;
		}

		checkBeforeMovePosition(content, newPosn);

		doMovePosition(content, newPosn);

	}

	/**
	 * Validates the context before moving an element.
	 * 
	 * @param content the element to move
	 * @param newPosn the new position
	 * @throws ContentException if <code>content</code> cannot resides in the new
	 *                          position
	 * 
	 */

	protected void checkBeforeMovePosition(DesignElement content, int newPosn) throws ContentException {
		assert content != null;

		// Ensure that the content can be put into the container.

		if (!element.getDefn().isContainer())
			throw ContentExceptionFactory.createContentException(focus,
					ContentException.DESIGN_EXCEPTION_NOT_CONTAINER);
		IContainerDefn defn = focus.getContainerDefn();
		if (defn == null)
			throw ContentExceptionFactory.createContentException(focus,
					ContentException.DESIGN_EXCEPTION_SLOT_NOT_FOUND);
		if (!content.isContentOf(element))
			throw ContentExceptionFactory.createContentException(focus, content,
					ContentException.DESIGN_EXCEPTION_CONTENT_NOT_FOUND);
	}

	/**
	 * Moves the element from one position to the new place. The element stays in
	 * the same slot/property.
	 * 
	 * @param content the element to move
	 * @param newPosn the new position
	 * @throws ContentException if <code>content</code> cannot resides in the new
	 *                          position
	 * 
	 */

	protected void doMovePosition(DesignElement content, int newPosn) throws ContentException {

		// Skip the step if the slotID/propName has only single content.
		if (!focus.isContainerMultipleCardinality())
			return;

		int oldPosn = focus.indexOf(module, content);
		int adjustedNewPosn = checkAndAdjustPosition(oldPosn, newPosn, focus.getContentCount(module));
		if (oldPosn == adjustedNewPosn)
			return;

		MoveContentRecord record = new MoveContentRecord(module, focus, content, adjustedNewPosn);

		record.setEventTarget(eventTarget);

		getActivityStack().execute(record);
	}

	/**
	 * Adds the element name and names of nested element in it to name spaces.
	 * 
	 * @param content the content to add
	 * @throws NameException if any element has duplicate name with elements already
	 *                       on the design tree.
	 */

	protected void addElementNames(DesignElement content) throws NameException {
		// before handle the names for the content and its children, the content
		// is added into the container first

		assert content.getContainer() != null;

		// if the content is managed by namespace, then check the name and add
		// it to the namespace, otherwise do nothing

		NameCommand nameCmd = new NameCommand(module, content);
		nameCmd.addElement();

		// recursively check the contents and add them

		if (content.getDefn().isContainer()) {
			Iterator<DesignElement> iter = new LevelContentIterator(module, content, 1);
			while (iter.hasNext()) {
				DesignElement tmpElement = iter.next();
				addElementNames(tmpElement);
			}
		}
	}

	/**
	 * Returns the target element for the notification event.
	 * 
	 * @return the event target
	 */

	private ContentElementInfo getEventTarget() {
		IContainerDefn tmpContainerDefn = focus.getContainerDefn();
		if (tmpContainerDefn instanceof SlotDefn)
			return null;

		DesignElement tmpElement = focus.getElement();
		PropertyDefn tmpPropDefn = (PropertyDefn) tmpContainerDefn;

		ContentElementInfo retTarget = new ContentElementInfo(true);
		while (tmpElement != null && tmpPropDefn != null) {
			retTarget.pushStep(tmpPropDefn, -1);

			if (tmpPropDefn.getTypeCode() == IPropertyType.CONTENT_ELEMENT_TYPE
					&& !(tmpElement instanceof ContentElement)) {
				retTarget.setTopElement(tmpElement);
				return retTarget;
			}

			ContainerContext context = tmpElement.getContainerInfo();
			if (context == null)
				break;

			tmpElement = tmpElement.getContainer();
			tmpPropDefn = tmpElement.getPropertyDefn(context.getPropertyName());
		}

		return null;
	}

	/**
	 * Remove element from the name space.
	 * 
	 * @param element the design element.
	 */
	protected void removeElementFromNameSpace(DesignElement element) {
		assert element != null;

		if (element.getName() != null) {
			NameCommand nameCmd = new NameCommand(module, element);
			nameCmd.dropElement();
		}
	}
}
