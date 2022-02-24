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
import org.eclipse.birt.report.model.api.IllegalOperationException;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.CssException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferencableStructure;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.css.CssStyle;
import org.eclipse.birt.report.model.elements.ContentElement;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.CommandLabelFactory;
import org.eclipse.birt.report.model.util.StructureContextUtil;

/**
 * Complex property command to handle all list related operations, such as,
 * addItem, removeItem.
 */

public class ComplexPropertyCommand extends AbstractPropertyCommand {

	/**
	 * Constructor.
	 * 
	 * @param module the root of <code>obj</code>
	 * @param obj    the element to modify.
	 */

	public ComplexPropertyCommand(Module module, DesignElement obj) {
		super(module, obj);
	}

	/**
	 * Adds an item to a structure list.
	 * <ul>
	 * <li>If the property is currently unset anywhere up the inheritance hierarchy,
	 * then a new list is created on this element, and the list contains the only
	 * the new item.</li>
	 * <li>If the property is currently set on this element, then the item is added
	 * to the existing list.</li>
	 * <li>If the list is not set on this element, but is set by an ancestor
	 * element, then the list is <strong>copied </strong> onto this element, and the
	 * new element is then appended to the copy.</li>
	 * </ul>
	 * 
	 * @param ref  context to the list into which to add the structure
	 * @param item the structure to add to the list
	 * @throws SemanticException if the item to add is invalid.
	 */

	private IStructure addItem(StructureContext context, IStructure item) throws SemanticException {
		assert context != null;
		checkAllowedOperation();
		if (item == null)
			return null;

		Structure struct = (Structure) item;
		if (struct.getContext() != null)
			struct = (Structure) struct.copy();

		// for the new structure, establish the context for its nested
		// structures.

		StructureContextUtil.setupStructureContext(struct);

		PropertyDefn propDefn = context.getElementProp();
		assert propDefn != null;
		assertExtendedElement(module, element, propDefn);

		if (struct.isReferencable())
			assert !((ReferencableStructure) struct).hasReferences();

		checkListMemberRef(context);
		checkItem(context, struct);

		List list = context.getList(module);
		PropertyDefn memberDefn = context.getPropDefn();
		if (memberDefn != null)
			element.checkStructureList(module, memberDefn, list, struct);
		else
			element.checkStructureList(module, propDefn, list, struct);

		ActivityStack stack = getActivityStack();
		stack.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.ADD_ITEM_MESSAGE));

		context = makeLocalCompositeValue(context);
		list = context.getList(module);
		if (null == list) {
			list = new ArrayList();
			MemberRecord memberRecord = new MemberRecord(module, element, context, list);
			stack.execute(memberRecord);
		}

		PropertyListRecord record = constructStructureRecord(context, struct, list.size());

		record.setEventTarget(getEventTarget());
		stack.execute(record);
		stack.commit();

		return struct;
	}

	/**
	 * Adds an item to a structure list.
	 * <ul>
	 * <li>If the property is currently unset anywhere up the inheritance hierarchy,
	 * then a new list is created on this element, and the list contains the only
	 * the new item.</li>
	 * <li>If the property is currently set on this element, then the item is added
	 * to the existing list.</li>
	 * <li>If the list is not set on this element, but is set by an ancestor
	 * element, then the list is <strong>copied </strong> onto this element, and the
	 * new element is then appended to the copy.</li>
	 * </ul>
	 * 
	 * @param context context to the list into which to add the structure
	 * @param item    the structure to add to the list
	 * @throws SemanticException if the item to add is invalid.
	 */

	public Object addItem(StructureContext context, Object item) throws SemanticException {
		if (item instanceof IStructure) {
			return addItem(context, (IStructure) item);
		}

		assert context != null;
		checkAllowedOperation();
		PropertyDefn memberDefn = context.getPropDefn();

		// can not insert null structure to structure list; however, support
		// null to simple value list
		if (item == null && memberDefn.getTypeCode() == IPropertyType.STRUCT_TYPE)
			return null;

		// this method is not called for structure list property

		assert !(item instanceof IStructure);
		PropertyDefn prop = context.getElementProp();

		assertExtendedElement(module, element, prop);

		if (memberDefn != null)
			prop = memberDefn;

		// check the property type is list and do some validation about the item

		checkListProperty(prop);
		Object value = checkItem(prop, item);

		if (element instanceof ContentElement) {
			if (!((ContentElement) element).isLocal()) {
				ContentElementCommand attrCmd = new ContentElementCommand(module, element,
						((ContentElement) element).getValueContainer());

				attrCmd.addItem(context, value);
				return value;
			}
		}

		// check whether the value in the list is unique when the sub-type is
		// element reference value

		List list = context.getList(module);
		if (prop.getTypeCode() == IPropertyType.LIST_TYPE)
			element.checkSimpleList(module, prop, list, value);

		ActivityStack stack = getActivityStack();
		stack.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.ADD_ITEM_MESSAGE));

		context = makeLocalCompositeValue(context);

		list = context.getList(module);
		if (null == list) {
			list = new ArrayList();
			if (context.getValueContainer() instanceof DesignElement) {
				PropertyRecord record = new PropertyRecord(element, context.getElementProp(), list);
				stack.execute(record);
			} else {
				MemberRecord memberRecord = new MemberRecord(module, element, context, list);
				stack.execute(memberRecord);
			}
		}

		PropertyListRecord record = new PropertyListRecord(element, context.getElementProp(), list, value, list.size());

		record.setEventTarget(getEventTarget());

		stack.execute(record);

		if (value instanceof ElementRefValue) {
			ElementRefValue refValue = (ElementRefValue) value;
			if (refValue.isResolved()) {
				ElementRefRecord refRecord = new ElementRefRecord(element, refValue.getTargetElement(), prop.getName(),
						true);
				stack.execute(refRecord);
			}
		}

		stack.commit();

		return value;
	}

	/**
	 * Inserts an item to a structure list.
	 * <ul>
	 * <li>If the property is currently unset anywhere up the inheritance hierarchy,
	 * then a new list is created on this element, and the list contains the only
	 * the new item.</li>
	 * <li>If the property is currently set on this element, then the item is
	 * inserted into the existing list.</li>
	 * <li>If the list is not set on this element, but is set by an ancestor
	 * element, then the list is <strong>copied </strong> onto this element, and the
	 * new element is then inserted into the copy.</li>
	 * </ul>
	 * 
	 * @param context context to the list into which to insert the new item
	 * @param item    the item to insert
	 * @param posn    the position at which to insert the item
	 * @throws SemanticException         if the item to add is invalid.
	 * @throws IndexOutOfBoundsException if the given posn is out of range
	 *                                   <code>(index &lt; 0 || index &gt; list.size())</code>.
	 */

	public IStructure insertItem(StructureContext context, IStructure item, int posn) throws SemanticException {
		assert context != null;
		checkAllowedOperation();
		if (item == null)
			return null;

		Structure struct = (Structure) item;
		if (struct.getContext() != null)
			struct = (Structure) struct.copy();

		PropertyDefn propDefn = context.getElementProp();
		assert propDefn != null;
		assertExtendedElement(module, element, propDefn);

		checkListMemberRef(context);
		checkItem(context, struct);

		List list = context.getList(module);
		element.checkStructureList(module, context.getPropDefn(), list, struct);

		ActivityStack stack = getActivityStack();

		stack.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.INSERT_ITEM_MESSAGE));

		context = makeLocalCompositeValue(context);
		list = context.getList(module);
		if (null == list) {
			list = new ArrayList();
			MemberRecord memberRecord = new MemberRecord(module, element, context, list);
			stack.execute(memberRecord);
		}

		if (posn < 0 || posn > list.size())
			throw new IndexOutOfBoundsException("Posn: " + posn + ", List Size: " + list.size()); //$NON-NLS-1$//$NON-NLS-2$

		PropertyListRecord record = constructStructureRecord(context, struct, posn);

		record.setEventTarget(getEventTarget());

		stack.execute(record);
		stack.commit();

		return struct;
	}

	/**
	 * Removes an item from a structure list.
	 * <ul>
	 * <li>The element must exist in the current effective value for the list. This
	 * means the list must be set on this element or a ancestor element.</li>
	 * <li>If the property is set on this element, then the element is simply
	 * removed.</li>
	 * <li>If the property is set on an ancestor element, then the inherited list is
	 * first <strong>copied </strong> into this element. Then, the copy of the
	 * target item is removed from the copy of the list.</li>
	 * </ul>
	 * 
	 * @param ref  context to the list in which to remove an item.
	 * @param posn position of the item to be removed from the list.
	 * @throws SemanticException         if the item to remove is not found.
	 * @throws IndexOutOfBoundsException if the given posn is out of range
	 *                                   <code>(index &lt; 0 || index &gt;= list.size())</code>.
	 */

	public void removeItem(StructureContext context, int posn) throws SemanticException {
		assert context != null;
		PropertyDefn propDefn = context.getElementProp();

		checkAllowedOperation();

		assert propDefn != null;
		assertExtendedElement(module, element, propDefn);

		PropertyDefn memberDefn = context.getPropDefn();
		List list = null;

		if (memberDefn != null)
			propDefn = memberDefn;

		if (propDefn.getTypeCode() == IPropertyType.LIST_TYPE) {
			// do not need to do checkListProperty( memberDefn );

			list = context.getList(module);
		} else {
			checkListMemberRef(context);
			list = context.getList(module);
		}

		if (list == null)
			throw new PropertyValueException(element, context.getPropDefn(), null,
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND);

		if (posn < 0 || posn >= list.size())
			throw new IndexOutOfBoundsException("Posn: " + posn + ", List Size: " + list.size()); //$NON-NLS-1$//$NON-NLS-2$

		if (element instanceof ContentElement) {
			if (!((ContentElement) element).isLocal()) {
				ContentElementCommand attrCmd = new ContentElementCommand(module, element,
						((ContentElement) element).getValueContainer());

				attrCmd.removeItem(context, posn);
				return;
			}
		}

		doRemoveItem(context, posn);
	}

	/**
	 * Removes an item from a structure list.
	 * <ul>
	 * <li>The element must exist in the current effective value for the list. This
	 * means the list must be set on this element or a ancestor element.</li>
	 * <li>If the property is set on this element, then the element is simply
	 * removed.</li>
	 * <li>If the property is set on an ancestor element, then the inherited list is
	 * first <strong>copied </strong> into this element. Then, the copy of the
	 * target item is removed from the copy of the list.</li>
	 * </ul>
	 * 
	 * @param context   the structure list context
	 * @param structure the item to remove
	 * @throws PropertyValueException if the item to remove is not found.
	 */

	public void removeItem(StructureContext context, IStructure structure) throws PropertyValueException {
		checkAllowedOperation();
		PropertyDefn propDefn = context.getElementProp();
		assert propDefn != null;
		assertExtendedElement(module, element, propDefn);

		checkListMemberRef(context);
		List list = context.getList(module);
		if (list == null)
			throw new PropertyValueException(element, context.getPropDefn(), null,
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND);

		int posn = getIndex(structure, list);
		if (posn == -1)
			throw new PropertyValueException(element, context.getPropDefn().getName(), null,
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND);

		doRemoveItem(context, posn);
	}

	private int getIndex(IStructure structure, List list) {
		// As the structure.equals() has been overwrite. compare the reference first.
		int posn = -1;
		int index = -1;
		for (Object item : list) {
			index++;
			if (item instanceof IStructure && item == structure) {
				posn = index;
			}
		}
		if (posn == -1) {
			posn = list.indexOf(structure);
		}
		return posn;
	}

	/**
	 * Removes structure from structure list.
	 * 
	 * @param memberContext context to the item to remove
	 */

	private void doRemoveItem(StructureContext memberContext, int posn) {
		String label = CommandLabelFactory.getCommandLabel(MessageConstants.REMOVE_ITEM_MESSAGE);

		ActivityStack stack = module.getActivityStack();
		stack.startTrans(label);

		memberContext = makeLocalCompositeValue(memberContext);
		List list = memberContext.getList(module);
		assert list != null;

		Structure struct = memberContext.getStructureAt(module, posn);
		if (struct != null) {
			if (struct.isReferencable())
				adjustReferenceClients((ReferencableStructure) struct);

			// handle the structure member refers to other elements.

			adjustReferenceClients(struct, memberContext);
		}

		Object item = list.get(posn);

		PropertyListRecord record = null;

		if (struct != null)
			record = new PropertyListRecord(element, struct.getContext(), posn);
		else {
			record = new PropertyListRecord(element, memberContext.getElementProp(), list, posn);
		}

		record.setEventTarget(getEventTarget());
		stack.execute(record);

		if (item instanceof ElementRefValue) {
			ElementRefValue refValue = (ElementRefValue) item;
			if (refValue.isResolved()) {
				ElementRefRecord refRecord = new ElementRefRecord(element, refValue.getTargetElement(),
						memberContext.getPropDefn().getName(), false);
				stack.execute(refRecord);

			}
		}

		stack.commit();
	}

	/**
	 * Replaces an item from a structure list with the new one.
	 * <ul>
	 * <li>The element must exist in the effective value for the list. This means
	 * the list must be set on this element or a ancestor element.</li>
	 * <li>If the property is set on this element, then the element is simply
	 * replaced</li>
	 * <li>If the property is set on an ancestor element, then the inherited list is
	 * first copied into this element. Then, the copy of the target item is removed
	 * from the copy of the list.</li>
	 * </ul>
	 * 
	 * @param context The structure list context.
	 * @param oldItem the old item to be replaced
	 * @param newItem the new item reference.
	 * @throws SemanticException if the old item is not found or this property type
	 *                           is not structure list.
	 */

	public void replaceItem(StructureContext context, IStructure oldItem, IStructure newItem) throws SemanticException {
		assert context != null;
		checkAllowedOperation();
		PropertyDefn propDefn = context.getElementProp();
		assert propDefn != null;
		assertExtendedElement(module, element, propDefn);

		checkListMemberRef(context);

		List list = context.getList(module);
		if (list == null)
			throw new PropertyValueException(element, context.getPropDefn(), null,
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND);

		Structure struct = (Structure) newItem;

		if (newItem != null) {
			if (struct.getContext() != null)
				struct = (Structure) struct.copy();

			checkItem(context, struct);
			element.checkStructureList(module, context.getPropDefn(), list, struct);
		}

		ActivityStack stack = module.getActivityStack();

		stack.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.REPLACE_ITEM_MESSAGE));

		context = makeLocalCompositeValue(context);
		list = context.getList(module);
		assert list != null;

		int index = getIndex(oldItem, list);
		if (index == -1)
			throw new PropertyValueException(element, context.getPropDefn().getName(), oldItem,
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND);

		PropertyReplaceRecord record = new PropertyReplaceRecord(element, context, list, index, struct);

		record.setEventTarget(getEventTarget());
		stack.execute(record);

		if (oldItem.isReferencable())
			adjustReferenceClients((ReferencableStructure) oldItem);

		stack.commit();
	}

	/**
	 * Removes all contents of the list. This is different from simply clearing the
	 * property. Removing all the contents leaves the property set to an empty list.
	 * 
	 * @param context context to the list to clear
	 * @throws SemanticException if the property is not a structure list property
	 */

	public void removeAllItems(StructureContext context) throws SemanticException {
		checkAllowedOperation();
		checkListMemberRef(context);

		PropertyDefn propDefn = context.getElementProp();
		assert propDefn != null;
		assertExtendedElement(module, element, propDefn);

		if (context.getValueContainer() instanceof DesignElement) {
			PropertyCommand cmd = new PropertyCommand(module, element);
			cmd.setProperty(context.getElementProp(), null);
		} else {
			PropertyCommand cmd = new PropertyCommand(module, element);
			cmd.setMember(context, null);
		}
	}

	/**
	 * Moves an item within a list from one position to a new position.
	 * <ul>
	 * <li>The element must exist in the current effective value for the list. This
	 * means the list must be set on this element or a ancestor element.</li>
	 * <li>If the property is set on this element, then the element is simply
	 * moved.</li>
	 * <li>If the property is set on an ancestor element, then the inherited list is
	 * first <strong>copied </strong> into this element. Then, the copy of the
	 * target item is moved within the copy of the list.</li>
	 * </ul>
	 * 
	 * <p>
	 * For example, if a list has A, B, C structures in order, when move A structure
	 * to <code>newPosn</code> with the value 2, the sequence becomes B, A, C.
	 * 
	 * 
	 * @param context reference to the list in which to do the move the item.
	 * @param oldPosn the old position of the item.
	 * @param newPosn new position of the item. Note that the range of
	 *                <code>to</code> is from 0 to the number of structures in the
	 *                list.
	 * 
	 * @throws PropertyValueException    if the property is not a structure list
	 *                                   property, or the list value is not set.
	 * @throws IndexOutOfBoundsException if the given from or to index is out of
	 *                                   range
	 *                                   <code>(index &lt; 0 || index &gt;= list.size())</code>.
	 */

	public void moveItem(StructureContext context, int oldPosn, int newPosn) throws PropertyValueException {
		assert context != null;
		checkAllowedOperation();
		PropertyDefn propDefn = context.getElementProp();
		assert propDefn != null;
		checkListMemberRef(context);

		List list = context.getList(module);
		if (list == null)
			throw new PropertyValueException(element, context.getPropDefn(), null,
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND);

		ActivityStack stack = getActivityStack();
		String label = CommandLabelFactory.getCommandLabel(MessageConstants.MOVE_ITEM_MESSAGE);

		int adjustedNewPosn = checkAndAdjustPosition(oldPosn, newPosn, list.size());
		if (oldPosn == adjustedNewPosn)
			return;

		stack.startTrans(label);

		context = makeLocalCompositeValue(context);
		list = context.getList(module);
		assert list != null;

		MoveListItemRecord record = new MoveListItemRecord(element, context, oldPosn, adjustedNewPosn);
		record.setEventTarget(getEventTarget());

		stack.execute(record);
		stack.commit();
	}

	/**
	 * Check to see whether the reference points to a list.
	 * 
	 * @param prop the property definition to check whether it is list type
	 * @throws PropertyValueException if the property definition is not a list type
	 */

	private void checkListProperty(PropertyDefn prop) throws PropertyValueException {
		if (prop.getTypeCode() != IPropertyType.LIST_TYPE)
			throw new PropertyValueException(element, prop, null,
					PropertyValueException.DESIGN_EXCEPTION_NOT_LIST_TYPE);
	}

	/**
	 * Check to see whether the reference points to a list.
	 * 
	 * @param context context to the list into which to add the structure
	 * @throws PropertyValueException if the <code>ref</code> doesn't refer a list
	 *                                property or member.
	 */

	protected void checkListMemberRef(StructureContext context) throws PropertyValueException {
		if (!context.isListRef())
			throw new PropertyValueException(element, context.getPropDefn(), null,
					PropertyValueException.DESIGN_EXCEPTION_NOT_LIST_TYPE);
	}

	/**
	 * Check operation is allowed or not. Now if element is css style instance ,
	 * forbidden its operation.
	 * 
	 */

	private void checkAllowedOperation() {
		if (element != null && element instanceof CssStyle) {
			throw new IllegalOperationException(CssException.DESIGN_EXCEPTION_READONLY);
		}
	}

	private PropertyListRecord constructStructureRecord(StructureContext context, Structure struct, int posn) {
		PropertyListRecord record = null;
		Object parentStruct = context.getStructure();

		PropertyDefn tmpPropDefn = context.getPropDefn();
		if (tmpPropDefn == null)
			tmpPropDefn = context.getElementProp();

		StructureContext tmpContext = null;
		if (parentStruct == null)
			tmpContext = new StructureContext(element, (ElementPropertyDefn) tmpPropDefn, null);
		else
			tmpContext = new StructureContext((Structure) parentStruct, tmpPropDefn, null);

		record = new PropertyListRecord(element, tmpContext, struct, posn);

		return record;
	}

}
