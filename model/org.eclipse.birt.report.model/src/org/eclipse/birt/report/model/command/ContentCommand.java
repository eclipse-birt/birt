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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.command.TemplateException;
import org.eclipse.birt.report.model.api.command.UserPropertyException;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.elements.structures.PropertyBinding;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.BackRef;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.IReferencableElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.core.namespace.NameExecutor;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.TemplateElement;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;
import org.eclipse.birt.report.model.elements.interfaces.ITabularDimensionModel;
import org.eclipse.birt.report.model.elements.olap.Cube;
import org.eclipse.birt.report.model.elements.olap.Dimension;
import org.eclipse.birt.report.model.elements.olap.TabularDimension;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.IContainerDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.StructRefValue;
import org.eclipse.birt.report.model.util.CommandLabelFactory;
import org.eclipse.birt.report.model.util.ContentExceptionFactory;

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

public class ContentCommand extends AbstractContentCommand {

	/**
	 *
	 */

	protected boolean unresolveReference;

	/**
	 * <code>true</code> is to manipulate group elements that refers to other groups
	 * or virtual elements.
	 */

	protected boolean flag;

	/**
	 * Constructs the content command with container element.
	 *
	 * @param module        the module
	 * @param containerInfo the container infor
	 */

	public ContentCommand(Module module, ContainerContext containerInfo) {
		super(module, containerInfo);
	}

	/**
	 * Constructs the content command with container element.
	 *
	 * @param module        the module
	 * @param containerInfo the container information
	 * @param flag
	 */

	ContentCommand(Module module, ContainerContext containerInfo, boolean flag) {
		super(module, containerInfo);
		this.flag = flag;
	}

	/**
	 * Constructs the content command with container element.
	 *
	 * @param module             the module
	 * @param containerInfo      the container information
	 * @param flag
	 * @param unresolveReference
	 */

	public ContentCommand(Module module, ContainerContext containerInfo, boolean flag, boolean unresolveReference) {
		super(module, containerInfo);
		this.flag = flag;
		this.unresolveReference = unresolveReference;
	}

	/**
	 * Adds a new element into a container. Virtually all elements must reside in a
	 * container. (The term "container" here is generic, it is not the same as the
	 * Container element defined in the XML schema.) Containers are identified by a
	 * container ID. The application creates the element object, then adds it to the
	 * container here. The undo of this operation effectively deletes the element.
	 *
	 * @param content the element to add
	 * @param slotID  the slot in which to add the component
	 * @param newPos  the position index at which the content to be inserted. If
	 *                it's -1, the content will be inserted at the end of the slot.
	 * @throws ContentException if the content cannot be added into this container.
	 * @throws NameException    if the name of the content exists in name space.
	 */

	@Override
	protected void checkBeforeAdd(DesignElement content) throws ContentException, NameException {
		assert content.getContainer() == null;

		super.checkBeforeAdd(content);

		// Can not change the structure of child element or a virtual element(
		// inside the child ).

		if (element.isVirtualElement() || element.getExtendsName() != null) {
			throw ContentExceptionFactory.createContentException(focus, content,
					ContentException.DESIGN_EXCEPTION_STRUCTURE_CHANGE_FORBIDDEN);
		}

		if (focus.getSlotID() == IModuleModel.COMPONENT_SLOT) {
			if (StringUtil.isBlank(content.getName())) {
				throw ContentExceptionFactory.createContentException(focus, content,
						ContentException.DESIGN_EXCEPTION_CONTENT_NAME_REQUIRED);
			}
		}

		if (!flag) {
			checkContainmentContext(content);
		}
	}

	@Override
	protected void doAdd(int newPos, DesignElement content) throws ContentException, NameException {

		// Add the item to the container.

		ContentRecord addRecord;
		if (newPos == -1) {
			addRecord = new ContentRecord(module, focus, content, true);
		} else {
			addRecord = new ContentRecord(module, focus, content, newPos);
		}

		ActivityStack stack = getActivityStack();
		stack.startTrans(addRecord.getLabel());

		try {
			// add the template parameter definition first

			TemplateCommand cmd = new TemplateCommand(module, focus);
			cmd.checkAdd(content);

			checkEmptyName(content);

			// add the element

			super.doAdd(newPos, content);

			// check the name of the content and all its children and add names
			// of them to the namespace

			addElementNames(content);
		} catch (NameException e) {
			stack.rollback();
			throw e;
		}

		stack.commit();
	}

	protected void checkEmptyName(DesignElement content) {
		ElementDefn metaData = (ElementDefn) content.getDefn();
		String name = content.getName();
		if (name == null && metaData.getNameOption() == MetaDataConstants.REQUIRED_NAME) {
			NameExecutor executor = new NameExecutor(module, focus, content);
			executor.makeUniqueName();
		}
	}

	/**
	 * Determines if the slot can contain a given element with considering the
	 * context.
	 *
	 * @param slotId  the slot id
	 * @param element the element to insert
	 * @throws NameException    if name duplicate occurs
	 * @throws ContentException the slot cannot contain the given element.
	 */

	private void checkContainmentContext(DesignElement content) throws NameException, ContentException {
		List<SemanticException> errors = focus.checkContainmentContext(module, content);
		if (!errors.isEmpty()) {
			SemanticException e = errors.get(0);
			assert e instanceof NameException || e instanceof ContentException;

			if (e instanceof NameException) {
				throw (NameException) e;
			}

			if (e instanceof ContentException) {
				throw (ContentException) e;
			}
		}
	}

	/**
	 * @see #remove(DesignElement, int, boolean)
	 * @param content            the element to remove
	 * @param slotID             the slot from which to remove the content
	 * @param unresolveReference status whether to un-resolve the references
	 * @param flag               Use the flag to indicate whether this method is do
	 *                           with the element itself or do with its contents.
	 * @throws SemanticException if this content cannot be removed from container.
	 */

	@Override
	protected void checkBeforeRemove(DesignElement content) throws SemanticException {
		assert content != null;

		// Ensure that the content can be dropped from the container.

		super.checkBeforeRemove(content);

		// Can not drop a virtual element. However, if it is called when
		// dropping the child element, the check should be ignored.

		if (!flag && !content.canDrop(module)) {
			throw ContentExceptionFactory.createContentException(focus, content,
					ContentException.DESIGN_EXCEPTION_STRUCTURE_CHANGE_FORBIDDEN);
		}

		// if the content is in component slot of report design and it has
		// children, then the operation is forbidden.

		if (hasDescendents(content)) {
			throw ContentExceptionFactory.createContentException(new ContainerContext(content, focus.getSlotID()),
					ContentException.DESIGN_EXCEPTION_HAS_DESCENDENTS);
		}
	}

	/**
	 * Changes derived elements to derive from the parent (if any) instead.
	 *
	 * @param obj the element to clean up
	 * @throws ExtendsException if an error occurs, but the operation should not
	 *                          fail under normal conditions
	 */

	private void adjustDerived(DesignElement obj) throws ExtendsException {
		// Skip if this element does not have derived elements.

		Collection<DesignElement> derived = obj.getDerived();
		if (derived.isEmpty()) {
			return;
		}

		DesignElement parent = obj.getExtendsElement();
		Iterator<DesignElement> iter = derived.iterator();
		while (iter.hasNext()) {
			DesignElement child = iter.next();
			ExtendsCommand childCmd = new ExtendsCommand(module, child);
			childCmd.setExtendsElement(parent);
		}
	}

	/**
	 * Adjusts references to an element that is to be deleted. The element to be
	 * deleted is one that has references in the form of element reference
	 * properties on other elements. These other elements, called "clients", each
	 * contain a property of type element reference and that property refers to this
	 * element. Each reference is recorded with a "back pointer" from the referenced
	 * element to the client. That back pointer has both a pointer to the client
	 * element, and the property within that element that holds the reference. There
	 * are two algorithms to handle this reference property, which can be selected
	 * by <code>unresolveReference</code>. If <code>unresolveReference</code> is
	 * <code>true</code>, the reference property is unresolved. Otherwise, it's
	 * cleared.
	 *
	 * @param referred           the element to be deleted
	 * @param unresolveReference the flag indicating the reference property should
	 *                           be unresolved, instead of cleared
	 * @throws SemanticException if an error occurs, but the operation should not
	 *                           fail under normal conditions
	 *
	 * @see #adjustReferredClients(DesignElement)
	 */

	private void adjustReferenceClients(IReferencableElement referred) throws SemanticException {
		List<BackRef> clients = new ArrayList<>(referred.getClientList());

		boolean isDimension = referred instanceof Dimension;

		Iterator<BackRef> iter = clients.iterator();
		while (iter.hasNext()) {
			BackRef ref = iter.next();
			DesignElement client = ref.getElement();
			if (isDimension && ITabularDimensionModel.INTERNAL_DIMENSION_RFF_TYPE_PROP.equals(ref.getPropertyName())) {
				// when the share dimension is drop, drop the cube dimension
				ContainerContext context = client.getContainerInfo();
				assert client instanceof TabularDimension && context.getElement() instanceof Cube;
				ContentCommand command = new ContentCommand(client.getRoot(), context);
				command.remove(client);
			} else if (unresolveReference) {
				BackRefRecord record = null;
				if (client != null) {
					record = new ElementBackRefRecord(module, referred, client, ref.getPropertyName());
				} else {
					record = new ElementBackRefRecord(module, referred, ref.getStructure(), ref.getPropertyName());
				}
				getActivityStack().execute(record);
			} else if (((DesignElement) referred).isStyle()) {
				Structure struct = ref.getStructure();

				if (struct != null) {
					PropertyCommand cmd = new PropertyCommand(module, client);
					cmd.setMember(new StructureContext(struct,
							(PropertyDefn) struct.getDefn().getMember(ref.getPropertyName()), struct), null);
				} else {
					StyleCommand clientCmd = new StyleCommand(module, client);
					clientCmd.setStyleElement((DesignElement) null);

				}
			} else {
				Structure struct = ref.getStructure();
				if (struct != null) {
					ComplexPropertyCommand cmd = new ComplexPropertyCommand(module, client);
					cmd.removeItem(struct.getContext(), struct);
				} else if (ref.getPropertyName() != null) {
					String propName = ref.getPropertyName();
					ElementPropertyDefn propDefn = client.getPropertyDefn(propName);
					if (propDefn.getTypeCode() == IPropertyType.LIST_TYPE
							&& propDefn.getSubTypeCode() == IPropertyType.ELEMENT_REF_TYPE) {
						// for this case, make sure only one corresponding
						// element reference value is removed, not the whole
						// list. Otherwise, some back references may be
						// corrupted.

						clearMatchedElementRefInList(client, propDefn, referred);
					} else {
						PropertyCommand cmd = new PropertyCommand(module, client);
						cmd.setProperty(ref.getPropertyName(), null);
					}
				}
			}
		}
	}

	/**
	 * Clears one item that matches given elements in the element reference list.
	 *
	 * @param client   the element
	 * @param propDefn the property definition
	 * @param referred the referred element
	 * @throws SemanticException
	 */

	private void clearMatchedElementRefInList(DesignElement client, ElementPropertyDefn propDefn,
			IReferencableElement referred) throws SemanticException {

		assert propDefn.getTypeCode() == IPropertyType.LIST_TYPE
				&& propDefn.getSubTypeCode() == IPropertyType.ELEMENT_REF_TYPE;

		List<Object> values = (List) client.getProperty(module, propDefn);
		if (values == null || values.isEmpty()) {
			return;
		}

		int index = -1;
		for (int i = 0; i < values.size(); i++) {
			ElementRefValue refValue = (ElementRefValue) values.get(i);
			if (refValue.getElement() == referred) {
				index = i;
				break;
			}
		}
		assert index != -1;

		ComplexPropertyCommand cmd = new ComplexPropertyCommand(module, client);
		cmd.removeItem(new StructureContext(client, propDefn, null), index);

	}

	/**
	 * Clears references of elements that are referred by the to-be-deleted element,
	 * except for extends and style element references. Unlike the method
	 * {@link #adjustReferenceClients(ReferenceableElement,boolean)}, this method
	 * removes references from those elements that are referred.
	 *
	 * @param element the element to be deleted
	 *
	 */

	private void adjustReferredClients(DesignElement element) {
		List<IElementPropertyDefn> propDefns = element.getPropertyDefns();

		for (Iterator<IElementPropertyDefn> iter = propDefns.iterator(); iter.hasNext();) {
			PropertyDefn propDefn = (PropertyDefn) iter.next();

			// DO NOT consider extends and style property since this has been
			// handled in remove method.

			if (IDesignElementModel.EXTENDS_PROP.equalsIgnoreCase(propDefn.getName())
					|| IStyledElementModel.STYLE_PROP.equalsIgnoreCase(propDefn.getName())) {
				continue;
			}

			if (propDefn.getTypeCode() == IPropertyType.ELEMENT_REF_TYPE
					|| propDefn.getTypeCode() == IPropertyType.STRUCT_REF_TYPE) {
				Object value = element.getLocalProperty(module, (ElementPropertyDefn) propDefn);

				if (value != null) {
					if ((value instanceof StructRefValue) || ((ElementRefValue) value).isResolved()) {
						try {
							// Clear all element reference property for dropped
							// element

							PropertyCommand cmd = new PropertyCommand(module, element);
							cmd.setProperty(propDefn.getName(), null);
						} catch (SemanticException e) {
							assert false;
						}
					}
				}
			} else if (propDefn.getTypeCode() == IPropertyType.LIST_TYPE
					&& propDefn.getSubTypeCode() == IPropertyType.ELEMENT_REF_TYPE) {
				List<Object> valueList = (List) element.getLocalProperty(module, (ElementPropertyDefn) propDefn);
				if (valueList != null) {
					StructureContext tmpMemberRef = new StructureContext(element, (ElementPropertyDefn) propDefn, null);
					for (int i = valueList.size() - 1; i >= 0; i--) {
						ElementRefValue item = (ElementRefValue) valueList.get(i);
						if (item.isResolved()) {
							try {
								// Clear all element reference property for
								// dropped element

								ComplexPropertyCommand cmd = new ComplexPropertyCommand(module, element);
								cmd.removeItem(tmpMemberRef, i);
							} catch (SemanticException e) {
								assert false;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Deletes user properties. This will also remove property values from this
	 * element and derived elements.
	 *
	 * @param obj the element to clean up
	 * @throws UserPropertyException if an error occurs, but the operation should
	 *                               not fail under normal conditions
	 */

	private void dropUserProperties(DesignElement obj) throws UserPropertyException {
		Collection<UserPropertyDefn> props = obj.getLocalUserProperties();
		if (props != null) {
			UserPropertyCommand propCmd = new UserPropertyCommand(module, obj);
			Iterator<UserPropertyDefn> iter = props.iterator();
			while (iter.hasNext()) {
				UserPropertyDefn prop = iter.next();
				propCmd.dropUserProperty(prop.getName());
			}
		}
	}

	/**
	 * Moves an element from one slot to another at the specified position. The
	 * destination slot can be in the same element (unusual) or a different element
	 * (usual case). Use the other form of this method to move an element within the
	 * same slot.
	 *
	 * @param content the element to move.
	 * @param newPos  the position in the target slot to which the content will be
	 *                moved. If it is -1 or greater than the size of the target
	 *                slot, the content will be appended at the end of the slot.
	 * @throws ContentException if the content cannot be moved to new container.
	 */

	@Override
	protected void checkBeforeMove(DesignElement content, ContainerContext toContainerInfor) throws ContentException {
		assert content != null;
		assert toContainerInfor != null;

		super.checkBeforeMove(content, toContainerInfor);

		// Can not change the structure of child element or a virtual element(
		// inside the child ).
		if (content.isVirtualElement() || content.getExtendsName() != null) {
			throw ContentExceptionFactory.createContentException(focus, content,
					ContentException.DESIGN_EXCEPTION_STRUCTURE_CHANGE_FORBIDDEN);
		}

		// if the content is in component slot of report design and it has
		// children, then the operation is forbidden.

		if (hasDescendents(content)) {
			throw ContentExceptionFactory.createContentException(new ContainerContext(content, focus.getSlotID()),
					ContentException.DESIGN_EXCEPTION_HAS_DESCENDENTS);
		}
	}

	@Override
	protected void checkBeforeMovePosition(DesignElement content, int newPosn) throws ContentException {
		assert content != null;

		super.checkBeforeMovePosition(content, newPosn);

		// Can not change the structure of child element or a virtual element(
		// inside the child ).

		if (content.isVirtualElement()) {
			throw ContentExceptionFactory.createContentException(focus, content,
					ContentException.DESIGN_EXCEPTION_STRUCTURE_CHANGE_FORBIDDEN);
		}

		if (!canMovePosition(content, newPosn)) {
			throw ContentExceptionFactory.createContentException(focus,
					ContentException.DESIGN_EXCEPTION_MOVE_FORBIDDEN);
		}
	}

	/**
	 * Returns false if the content has ancestor behind the new position or has
	 * children before the new position.
	 *
	 * @param content the element to move position
	 * @param newPosn the new position within the slot.
	 * @return true if the content can be moved within the slot, otherwise false.
	 */

	private boolean canMovePosition(DesignElement content, int newPosn) {
		if (element instanceof ReportDesign && focus.getSlotID() == IModuleModel.COMPONENT_SLOT) {
			List<DesignElement> derived = content.getDerived();
			// ContainerSlot slot = element.getSlot( slotID );
			Iterator<DesignElement> iter = derived.iterator();
			while (iter.hasNext()) {
				DesignElement child = iter.next();
				if (focus.contains(module, child)) {
					// if content has child before the new position
					// then the move of new position is forbidden.
					if (focus.indexOf(module, child) <= newPosn) {
						return false;
					}
				}
			}
			DesignElement e = content.getExtendsElement();
			while (e != null) {
				// if the content has ancestor behind the new position
				// then the move of new position is forbidden.
				if (focus.indexOf(module, e) >= newPosn) {
					return false;
				}
				e = e.getExtendsElement();
			}
		}
		return true;
	}

	/**
	 * Returns true if content in component slot of report design has descendants,
	 * otherwise false.
	 *
	 * @param content the content to handle
	 * @return true if content has descendants, otherwise false.
	 */

	private boolean hasDescendents(DesignElement content) {
		return element instanceof ReportDesign && focus.getSlotID() == IModuleModel.COMPONENT_SLOT
				&& content.hasDerived();
	}

	/**
	 * Does some transformation between template elements and report items or data
	 * sets. Virtually all elements must reside in a container. Containers are
	 * identified by a container ID.
	 *
	 * @param from               the old element to replace
	 * @param to                 the new element to replace
	 * @param slotID             the slot from which to replace the old element
	 * @param unresolveReference status whether to un-resolve the references or set
	 *                           the reference to null
	 * @throws SemanticException if the old element cannot be replaced by the new
	 *                           element into this container.
	 */

	public void transformTemplate(DesignElement from, DesignElement to, boolean unresolveReference)
			throws SemanticException {
		doReplace(from, to, unresolveReference);
	}

	/**
	 * @see #transformTemplate(DesignElement, DesignElement, int, boolean)
	 *
	 * @param oldElement         the old element to replace
	 * @param newElement         the new element to replace
	 * @param slotID             the slot from which to replace the old element
	 * @param unresolveReference status whether to un-resolve the references or set
	 *                           the reference to null
	 * @throws SemanticException if the old element cannot be replaced by the new
	 *                           element into this container.
	 */

	private void doReplace(DesignElement oldElement, DesignElement newElement, boolean unresolveReference)
			throws SemanticException {
		this.unresolveReference = unresolveReference;
		assert newElement.getContainer() == null;

		// Ensure that the new element can be put into the container.

		ElementDefn metaData = (ElementDefn) element.getDefn();
		if (!metaData.isContainer()) {
			throw ContentExceptionFactory.createContentException(focus,
					ContentException.DESIGN_EXCEPTION_NOT_CONTAINER);
		}
		IContainerDefn containerDefn = focus.getContainerDefn();
		if (containerDefn == null) {
			throw ContentExceptionFactory.createContentException(focus,
					ContentException.DESIGN_EXCEPTION_SLOT_NOT_FOUND);
		}
		if (!containerDefn.canContain(newElement)) {
			throw ContentExceptionFactory.createContentException(focus, newElement,
					ContentException.DESIGN_EXCEPTION_WRONG_TYPE);
		}

		// This element is already the content of the element to add.
		if (element.isContentOf(newElement)) {
			throw ContentExceptionFactory.createContentException(focus, newElement,
					ContentException.DESIGN_EXCEPTION_RECURSIVE);
		}

		if (focus.getSlotID() == IModuleModel.COMPONENT_SLOT) {
			if (StringUtil.isBlank(newElement.getName())) {
				throw ContentExceptionFactory.createContentException(focus, newElement,
						ContentException.DESIGN_EXCEPTION_CONTENT_NAME_REQUIRED);
			}
		}

		// do some checks about the element to be replaced
		if (!focus.contains(module, oldElement)) {
			throw ContentExceptionFactory.createContentException(focus, oldElement,
					ContentException.DESIGN_EXCEPTION_CONTENT_NOT_FOUND);
		}

		// do all checks about the transformation state
		if (oldElement instanceof TemplateElement) {
			if (!oldElement.canDrop(module) || !oldElement.isTemplateParameterValue(module) || !newElement.getDefn()
					.isKindOf(((TemplateElement) oldElement).getDefaultElement(module).getDefn())) {
				throw new TemplateException(oldElement,
						TemplateException.DESIGN_EXCEPTION_REVERT_TO_TEMPLATE_FORBIDDEN);
			}
		} else if (!oldElement.canTransformToTemplate(module) || !(newElement instanceof TemplateElement)) {
			throw ContentExceptionFactory.createContentException(focus,
					ContentException.DESIGN_EXCEPTION_TEMPLATE_TRANSFORM_FORBIDDEN);
		}

		// if the old element is in component slot of report design and it has
		// children, then the operation is forbidden.

		if (hasDescendents(oldElement)) {
			throw ContentExceptionFactory.createContentException(new ContainerContext(oldElement, focus.getSlotID()),
					ContentException.DESIGN_EXCEPTION_HAS_DESCENDENTS);
		}

		// Prepare the transaction.

		ContentReplaceRecord replaceRecord = new TemplateTransformRecord(module, focus, oldElement, newElement);

		ActivityStack stack = getActivityStack();

		stack.startFilterEventTrans(replaceRecord.getLabel());

		try {
			doDelectAction(oldElement);

			// do some checks about the template issues

			TemplateCommand cmd = new TemplateCommand(module, focus);
			cmd.checkAdd(newElement);

			// Remove the element itself.

			stack.execute(replaceRecord);

			addElementNames(newElement);
		} catch (SemanticException e) {
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

	@Override
	protected void doDelectAction(DesignElement content) throws SemanticException {
		// if element is table/list, we must localize the group before they are
		// removed, so handle this special case
		if (content instanceof ListingElement) {
			if (content.hasReferences()) {
				adjustReferenceClients((IReferencableElement) content);
			}
		}

		super.doDelectAction(content);

		// Clean up references to or from the element.
		dropUserProperties(content);
		if (!(element instanceof ListingElement) && content.hasReferences()) {
			adjustReferenceClients((IReferencableElement) content);
		}
		adjustReferredClients(content);
		adjustDerived(content);

		// Drop the style...

		if (content.getStyle(module) != null) {
			StyleCommand styleCmd = new StyleCommand(module, content);
			styleCmd.setStyle(null);
		}

		// Drop the extends...

		if (content.getExtendsElement() != null) {
			ExtendsCommand extendsCmd = new ExtendsCommand(module, content);
			extendsCmd.setExtendsName(null);
		}

		removeElementFromNameSpace(content);

		// Drop the property binding

		List<PropertyBinding> propertyBindings = module.getPropertyBindings(content);
		for (int i = 0; i < propertyBindings.size(); i++) {
			PropertyBinding propBinding = propertyBindings.get(i);
			ElementPropertyDefn propDefn = module.getPropertyDefn(IModuleModel.PROPERTY_BINDINGS_PROP);
			ComplexPropertyCommand propCommand = new ComplexPropertyCommand(module, module);
			propCommand.removeItem(new StructureContext(module, propDefn, null), propBinding);
		}
	}

	/**
	 * Does some actions when the content is removed from the design tree.
	 *
	 * @param content            the content to remove
	 * @param unresolveReference status whether to un-resolve the references
	 */

	@Override
	protected void doMove(DesignElement content, ContainerContext toContainerInfor, int newPos) {
		ActivityStack stack = getActivityStack();

		String label = CommandLabelFactory.getCommandLabel(MessageConstants.MOVE_ELEMENT_MESSAGE);
		stack.startTrans(label);

		super.doMove(content, toContainerInfor, newPos);

		stack.commit();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.command.AbstractContentCommand#doMovePosition
	 * (org.eclipse.birt.report.model.core.DesignElement, int)
	 */

	@Override
	protected void doMovePosition(DesignElement content, int newPosn) throws ContentException {

		// Skip the step if the slotID/propName has only single content.
		if (!focus.isContainerMultipleCardinality()) {
			return;
		}

		int oldPosn = focus.indexOf(module, content);
		int adjustedNewPosn = checkAndAdjustPosition(oldPosn, newPosn, focus.getContentCount(module));
		if (oldPosn == adjustedNewPosn) {
			return;
		}

		super.doMovePosition(content, newPosn);
	}

}
