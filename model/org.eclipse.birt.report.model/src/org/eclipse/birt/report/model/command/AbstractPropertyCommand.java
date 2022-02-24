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
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.activity.AbstractElementCommand;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.elements.structures.PropertyBinding;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.validators.StructureListValidator;
import org.eclipse.birt.report.model.core.BackRef;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.IReferencableElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferencableStructure;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.ReferenceValue;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;
import org.eclipse.birt.report.model.util.EncryptionUtil;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.StructureContextUtil;

/**
 * Abstract property command to do all property value change operations.
 */

abstract public class AbstractPropertyCommand extends AbstractElementCommand {

	/**
	 * Constructor.
	 * 
	 * @param module the root of <code>obj</code>
	 * @param obj    the element to modify.
	 */

	public AbstractPropertyCommand(Module module, DesignElement obj) {
		super(module, obj);
	}

	/**
	 * Justifies whether the extended element is created if the UI invokes some
	 * operations to change the extension properties.
	 * 
	 * Note that <code>PropertyCommand</code> do not support structure operations
	 * like addItem, removeItem, etc. for extension elements. This method is kept
	 * but NERVER call it in <code>doSetProperty</code>.
	 * 
	 * @param module  the module
	 * @param element the extended item that holds the extended element
	 * @param prop    the extension property definition to change
	 */

	protected void assertExtendedElement(Module module, DesignElement element, PropertyDefn prop) {
		if (element instanceof ExtendedItem) {
			ExtendedItem extendedItem = (ExtendedItem) element;
			if (extendedItem.isExtensionModelProperty(prop.getName())
					|| extendedItem.isExtensionXMLProperty(prop.getName())) {
				assert ((ExtendedItem) element).getExtendedElement() != null;
			}
		}
	}

	/**
	 * Validates the values of the item members.
	 * 
	 * @param context context to a list.
	 * @param item    the item to check
	 * @throws SemanticException if the item has any member with invalid value or if
	 *                           the given structure is not of a valid type that can
	 *                           be contained in the list.
	 */

	protected void checkItem(StructureContext context, IStructure item) throws SemanticException {
		checkItem(context.getElementProp(), context.getPropDefn(), item);
	}

	/**
	 * Validates the structure list. Currently it only support
	 * structure.structureList or structureList.structureList.
	 * 
	 * @param propDefn   the property definition
	 * @param memberDefn the structure member definition. It should be
	 *                   ref.getMemeberDefn().
	 * @param items      an array list containing structures that will be added
	 * @throws SemanticException
	 */

	private void checkItems(PropertyDefn propDefn, PropertyDefn memberDefn, List<Object> items)
			throws SemanticException {
		if (items == null)
			return;

		List<Object> currentList = new ArrayList<Object>();

		for (int i = 0; i < items.size(); i++) {
			IStructure struct = (IStructure) items.get(i);
			checkItem(propDefn, (StructPropertyDefn) memberDefn, struct);

			List<SemanticException> errors = StructureListValidator.getInstance()
					.validateForAdding(element.getHandle(module), memberDefn, currentList, struct);

			if (!errors.isEmpty())
				throw errors.get(0);

			currentList.add(struct);
		}
	}

	/**
	 * Validates the values of the item members.
	 * 
	 * @param ref  reference to a list.
	 * @param item the item to check
	 * @throws SemanticException if the item has any member with invalid value or if
	 *                           the given structure is not of a valid type that can
	 *                           be contained in the list.
	 */

	private void checkItem(PropertyDefn propDefn, PropertyDefn memberDefn, IStructure item) throws SemanticException {
		assert item != null;

		PropertyDefn currentDefn = propDefn;

		if (memberDefn == null) {
			if (item.getDefn() != propDefn.getStructDefn()) {
				throw new PropertyValueException(element, propDefn, item,
						PropertyValueException.DESIGN_EXCEPTION_WRONG_ITEM_TYPE);
			}
		} else {
			if (item.getDefn() != memberDefn.getStructDefn()) {
				throw new PropertyValueException(element, propDefn, propDefn, item,
						PropertyValueException.DESIGN_EXCEPTION_WRONG_ITEM_TYPE);
			}
			currentDefn = memberDefn;
		}

		for (Iterator<IPropertyDefn> iter = item.getDefn().propertiesIterator(); iter.hasNext();) {
			PropertyDefn tmpMemberDefn = (PropertyDefn) iter.next();
			if (ReferencableStructure.LIB_REFERENCE_MEMBER.equals(tmpMemberDefn.getName()))
				continue;

			Object value = ((Structure) item).getLocalProperty(module, tmpMemberDefn);

			// if the user calls Structure.setProperty(), the string element
			// name will be saved as ElementRefValue. So, need to resolve it as
			// string again since ElementRefPropertyType do not accept element
			// reference value

			if (value instanceof ElementRefValue && tmpMemberDefn.getTypeCode() == IPropertyType.ELEMENT_REF_TYPE) {
				ElementRefValue refValue = (ElementRefValue) value;

				// this is a special case, if the caller sets a resolved element
				// on the structure, need to make it unresolved. For example,
				// HighlightRule.style property.

				if (refValue.isResolved()) {
					refValue.unresolved(refValue.getElement().getName());
				}

				value = tmpMemberDefn.validateValue(module, element, refValue.getQualifiedReference());

				checkRecursiveElementReference(tmpMemberDefn, (ElementRefValue) value);
			} else {
				if (tmpMemberDefn.isList() && tmpMemberDefn.getStructDefn() != null) {
					checkItems(currentDefn, tmpMemberDefn, (List<Object>) value);
				} else
					value = tmpMemberDefn.validateValue(module, element, value);
			}

			// do some special handle for binding value
			if (item instanceof PropertyBinding && tmpMemberDefn.getName().equals(PropertyBinding.VALUE_MEMBER)) {
				EncryptionUtil.setEncryptionBindingValue(module, (Structure) item, tmpMemberDefn, value);
			} else
				item.setProperty(tmpMemberDefn, value);
		}

		if (item instanceof Structure) {
			List<SemanticException> errorList = ((Structure) item).validate(module, element);
			if (!errorList.isEmpty()) {
				throw errorList.get(0);
			}
		}

	}

	/**
	 * Adjusts references to a structure that is to be deleted. The structure to be
	 * deleted is one that has references in the form of structure reference
	 * properties on other elements. These other elements, called "clients", each
	 * contain a property of type structure reference and that property refers to
	 * this structure. Each reference is recorded with a "back pointer" from the
	 * referenced structure to the client. That back pointer has both a pointer to
	 * the client element, and the property within that element that holds the
	 * reference. The reference property is unresolved.
	 * 
	 * @param struct the structure to be deleted
	 */

	protected void adjustReferenceClients(ReferencableStructure struct) {
		assert struct != null;
		if (!struct.hasReferences())
			return;

		List<BackRef> clients = new ArrayList<BackRef>(struct.getClientList());

		Iterator<BackRef> iter = clients.iterator();
		while (iter.hasNext()) {
			BackRef ref = iter.next();
			DesignElement client = ref.getElement();

			BackRefRecord record = new StructBackRefRecord(module, struct, client, ref.getPropertyName());
			getActivityStack().execute(record);

		}
	}

	/**
	 * Checks whether recursive element reference occurs.
	 * 
	 * @param memberDefn the property/member definition
	 * @param refValue   the element reference value
	 * @throws SemanticException
	 */

	protected void checkRecursiveElementReference(PropertyDefn memberDefn, ElementRefValue refValue)
			throws SemanticException {
		assert refValue != null;

		if (refValue.isResolved() && element instanceof IReferencableElement) {
			DesignElement reference = refValue.getElement();
			if (ModelUtil.isRecursiveReference(reference, (IReferencableElement) element))

				throw new SemanticError(element, new String[] { reference.getIdentifier() },
						SemanticError.DESIGN_EXCEPTION_CIRCULAR_ELEMENT_REFERNECE);
		}

	}

	/**
	 * Validates the values of the item members.
	 * 
	 * @param memberContext context to a list.
	 * @param item          the item to check
	 * @throws SemanticException if the item has any member with invalid value or if
	 *                           the given structure is not of a valid type that can
	 *                           be contained in the list.
	 */

	protected void checkItemName(StructureContext memberContext, String newName) throws SemanticException {
		PropertyDefn propDefn = memberContext.getElementProp();

		Structure structure = memberContext.getStructure();

		List<SemanticException> errors = StructureListValidator.getInstance().validateForRenaming(
				element.getHandle(module), propDefn, memberContext.getList(module), structure,
				memberContext.getPropDefn(), newName);

		if (!errors.isEmpty()) {
			throw (PropertyValueException) errors.get(0);
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
	 * @param memberContext
	 * @param unresolveReference the flag indicating the reference property should
	 *                           be unresolved, instead of cleared
	 * @throws SemanticException if an error occurs, but the operation should not
	 *                           fail under normal conditions
	 * 
	 * @see #adjustReferredClients(DesignElement)
	 */

	protected void adjustReferenceClients(Structure referred, StructureContext memberContext) {
		IStructureDefn structDefn = referred.getDefn();
		Iterator<IPropertyDefn> memberDefns = structDefn.propertiesIterator();

		while (memberDefns.hasNext()) {
			StructPropertyDefn memberDefn = (StructPropertyDefn) memberDefns.next();
			if (memberDefn.getTypeCode() != IPropertyType.ELEMENT_REF_TYPE)
				continue;

			ReferenceValue refValue = (ReferenceValue) referred.getLocalProperty(module, memberDefn);

			if (refValue == null || !refValue.isResolved())
				continue;

			IReferencableElement client = (IReferencableElement) ((ElementRefValue) refValue).getElement();

			BackRefRecord record = new ElementBackRefRecord(module, client, referred, memberDefn.getName());
			getActivityStack().execute(record);
		}
	}

	/**
	 * The top level element property referenced by a member reference can be a list
	 * property or a structure property.
	 * <p>
	 * <li>If references a list property, the method will check to see if the
	 * current element has the local list value, if it has, the method returns,
	 * otherwise, a copy of the list value inherited from container or parent will
	 * be set locally on the element itself.
	 * <li>If references a structure property, the method will check to see if the
	 * current element has the local structure value, if it has, the method returns,
	 * otherwise, a copy of the structure value inherited from container or parent
	 * will be set locally on the element itself.
	 * <p>
	 * This method is supposed to be used when we need to change the value of a
	 * composite property( a list property or a structure property ). These kind of
	 * property is inherited as a whole, so when the value changed from a child
	 * element. This method will be called to ensure that a local copy will be made,
	 * so change to the child won't affect the original value in the parent.
	 * 
	 * @param context a context to a list property or member.
	 */

	StructureContext makeLocalCompositeValue(StructureContext context) {
		assert context != null;

		// make local composite value from the top level element property
		ElementPropertyDefn propDefn = context.getElementProp();
		// for there is overridden property definition case, we should get
		// definition locally
		propDefn = element.getPropertyDefn(propDefn.getName());

		if (propDefn.isListType()) {
			// Top level property is a list.

			List list = (ArrayList) element.getLocalProperty(module, propDefn);

			if (list != null)
				return context;

			// Make a local copy of the inherited list value.
			ArrayList inherited = (ArrayList) element.getProperty(module, propDefn);

			if (inherited != null) {
				list = (List) ModelUtil.copyValue(propDefn, inherited);

				// establish context when add items.
				if (propDefn.getTypeCode() == IPropertyType.STRUCT_TYPE)
					StructureContextUtil.setStructureContext(propDefn, list, element);
			} else
				list = new ArrayList();

			// Set the list value on the element itself.

			PropertyRecord propRecord = new PropertyRecord(element, propDefn, list);
			getActivityStack().execute(propRecord);

			// update the structure context to refer the new value
			context = StructureContextUtil.getLocalStructureContext(module, element, context);
			return context;
		}

		// Top level property is a structure.

		Structure struct = (Structure) element.getLocalProperty(module, propDefn);

		if (struct != null)
			return context;

		// Make a local copy of the inherited list value.

		Structure inherited = (Structure) element.getProperty(module, propDefn);

		if (inherited != null) {
			IStructure copy = inherited.copy();

			StructureContextUtil.setupStructureContext((Structure) copy);
			PropertyRecord propRecord = new PropertyRecord(element, propDefn, copy);
			getActivityStack().execute(propRecord);

			// update the structure context to refer the new value
			context = StructureContextUtil.getLocalStructureContext(module, element, context);
		}

		return context;
	}

	/**
	 * Validates the values of the item members.
	 * 
	 * @param prop
	 * 
	 * @param ref  reference to a list.
	 * @param item the item to check
	 * @return the validated item
	 * @throws PropertyValueException if the item has any member with invalid value
	 *                                or if the given structure is not of a valid
	 *                                type that can be contained in the list.
	 */

	protected Object checkItem(PropertyDefn prop, Object item) throws PropertyValueException {
		assert prop.getTypeCode() == IPropertyType.LIST_TYPE;
		Object value = item;
		if (item instanceof DesignElementHandle)
			value = ((DesignElementHandle) item).getElement();

		// make use of the sub-type to get the validated value

		PropertyType type = prop.getSubType();
		assert type != null;
		Object result = type.validateValue(module, element, prop, value);
		// if ( result instanceof ElementRefValue
		// && !( (ElementRefValue) result ).isResolved( ) )
		// {
		// throw new SemanticError( element,
		// SemanticError.DESIGN_EXCEPTION_INVALID_ELEMENT_REF );
		// }

		if (result instanceof ElementRefValue && element instanceof ReferenceableElement) {
			ElementRefValue refValue = (ElementRefValue) result;
			if (refValue.isResolved()) {
				ReferenceableElement target = (ReferenceableElement) refValue.getTargetElement();
				assert target != null;

				List<BackRef> clients = ((ReferenceableElement) element).getClientList();
				if (clients != null) {
					for (BackRef client : clients) {
						// circular reference
						if (client.getElement() == target) {
							throw new PropertyValueException(target.getIdentifier(),
									PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
									IPropertyType.ELEMENT_REF_TYPE);
						}
					}
				}

			}
		}
		return result;

	}

	/**
	 * Returns the target element for the notification event.
	 * 
	 * @return the event target.
	 */

	protected ContentElementInfo getEventTarget() {
		DesignElement tmpContainer = element.getContainer();
		if (tmpContainer == null)
			return null;

		String tmpPropName = element.getContainerInfo().getPropertyName();
		if (tmpPropName == null)
			return null;

		return ModelUtil.getContentContainer(element, tmpContainer.getPropertyDefn(tmpPropName));
	}
}
