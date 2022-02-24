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

package org.eclipse.birt.report.model.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.core.IAccessControl;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.command.ComplexPropertyCommand;
import org.eclipse.birt.report.model.command.ContentCommand;
import org.eclipse.birt.report.model.command.EncryptionCommand;
import org.eclipse.birt.report.model.command.PropertyCommand;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * A handle for working with a top-level property of an element.
 * 
 * @see org.eclipse.birt.report.model.metadata.PropertyDefn
 * @see org.eclipse.birt.report.model.metadata.PropertyType
 */

public class PropertyHandleImpl extends SimpleValueHandle {

	/**
	 * Definition of the property.
	 */

	protected ElementPropertyDefn propDefn;

	/**
	 * Constructs the handle for a top-level property with the given element handle
	 * and property name.
	 * 
	 * @param element  a handle to a report element
	 * @param propName the name of the property
	 */

	public PropertyHandleImpl(DesignElementHandle element, String propName) {
		super(element);
		propDefn = element.getElement().getPropertyDefn(propName);
	}

	/**
	 * Constructs the handle for a top-level property with the given element handle
	 * and the definition of the property.
	 * 
	 * @param element a handle to a report element
	 * @param prop    the definition of the property.
	 */

	public PropertyHandleImpl(DesignElementHandle element, ElementPropertyDefn prop) {
		super(element);
		propDefn = prop;
	}

	// Implementation of abstract method defined in base class.

	public IElementPropertyDefn getPropertyDefn() {
		return propDefn;
	}

	// Implementation of abstract method defined in base class.

	public IPropertyDefn getDefn() {
		return propDefn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#getRawValue()
	 */
	protected Object getRawValue() {
		return elementHandle.getElement().getProperty(getModule(), propDefn);
	}

	/**
	 * Implementation of abstract method defined in base class.
	 */

	public void setValue(Object value) throws SemanticException {
		PropertyCommand cmd = new PropertyCommand(getModule(), getElement());
		cmd.setProperty(propDefn, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ValueHandle#getContext()
	 */
	public StructureContext getContext() {
		return new StructureContext(getElement(), propDefn, null);
	}

	/**
	 * Determines whether this property value is set for this element. It is set if
	 * it is defined on this element property or any of its parents, or in the
	 * element's private style property. It is considered unset if it is set on a
	 * shared style.
	 * 
	 * @return <code>true</code> if the value is set, <code>false</code> if it is
	 *         not set
	 */

	public boolean isSet() {
		FactoryPropertyHandle handle = new FactoryPropertyHandle(elementHandle, propDefn);
		return handle.isSet();
	}

	/**
	 * Determines whether this property value is set locally for this element. It is
	 * set if and only if it is defined on this element local property.
	 * 
	 * @return <code>true</code> if the local value is set, otherwise
	 *         <code>false</code>.
	 * 
	 */

	public boolean isLocal() {
		Object value = getElement().getLocalProperty(getModule(), propDefn);
		return (value != null);
	}

	/**
	 * Returns true if the two property handle has the same element and the same
	 * property.
	 * 
	 * @param propertyHandle the property handle
	 * @return true if the two property handles are same.
	 */
	public boolean equals(Object propertyHandle) {
		if (!(propertyHandle instanceof PropertyHandleImpl))
			return false;

		DesignElement element = ((PropertyHandleImpl) propertyHandle).getElement();
		IPropertyDefn propDefn = ((PropertyHandleImpl) propertyHandle).getDefn();

		return (element == getElement()) && equalsPropertyDefn(propDefn);

	}

	private boolean equalsPropertyDefn(IPropertyDefn defn) {
		if (defn == null)
			return false;
		if ((propDefn.getName().equals(defn.getName())) && (propDefn.getTypeCode() == defn.getTypeCode()))
			return true;
		return false;
	}

	/**
	 * returns the element reference value list if the property is element
	 * referenceable type.
	 * 
	 * @return list of the reference element value.
	 */

	public List getReferenceableElementList() {
		if (propDefn.getTypeCode() != IPropertyType.ELEMENT_REF_TYPE
				&& propDefn.getSubTypeCode() != IPropertyType.ELEMENT_REF_TYPE)
			return Collections.EMPTY_LIST;

		List list = new ArrayList();

		ElementDefn elementDefn = (ElementDefn) propDefn.getTargetElementType();
		assert elementDefn != null;

		ModuleHandle moduleHandle = ((ModuleHandle) getModule().getHandle(getModule()));

		if (ReportDesignConstants.DATA_SET_ELEMENT.equals(elementDefn.getName()))
			return moduleHandle.getVisibleDataSets();

		else if (getElementHandle() instanceof ReportItemHandle
				&& ReportItemHandle.DATA_BINDING_REF_PROP.equalsIgnoreCase(propDefn.getName()))
			return ((ReportItemHandle) getElementHandle()).getNamedDataBindingReferenceList();

		else if (ReportDesignConstants.DATA_SOURCE_ELEMENT.equals(elementDefn.getName()))
			return moduleHandle.getVisibleDataSources();

		else if (ReportDesignConstants.STYLE_ELEMENT.equals(elementDefn.getName()))
			return ((ReportDesignHandle) moduleHandle).getAllStyles();
		else if (ReportDesignConstants.THEME_ITEM.equals(elementDefn.getName()))
			return moduleHandle.getVisibleThemes(IAccessControl.DIRECTLY_INCLUDED_LEVEL);
		else if (ReportDesignConstants.CUBE_ELEMENT.equals(elementDefn.getName()))
			return moduleHandle.getVisibleCubes();
		else if (ReportDesignConstants.REPORT_ITEM_THEME_ELEMENT.equals(elementDefn.getName())) {
			String matchedType = MetaDataDictionary.getInstance().getThemeType(elementHandle.getDefn());
			if (matchedType == null)
				return list;

			return moduleHandle.getVisibleReportItemThemes(IAccessControl.DIRECTLY_INCLUDED_LEVEL, matchedType);
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#removeItem(int)
	 */
	public void removeItem(int posn) throws PropertyValueException {
		try {
			ComplexPropertyCommand cmd = new ComplexPropertyCommand(getModule(), getElement());
			cmd.removeItem(getContext(), posn);
		} catch (PropertyValueException e) {
			throw e;
		} catch (SemanticException e) {
			assert false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#addItem(java.lang
	 * .Object)
	 */
	public void addItem(Object item) throws SemanticException {
		if (item instanceof IStructure) {
			super.addItem((IStructure) item);
			return;
		}

		ComplexPropertyCommand cmd = new ComplexPropertyCommand(getModule(), getElement());
		cmd.addItem(getContext(), item);
	}

	/**
	 * Returns whether the property value is read-only in the report context.
	 * 
	 * @return <code>true</code> if the value is read-only. Otherwise
	 *         <code>false</code>.
	 */

	/**
	 * Adds a report item to the property with the given element handle. The report
	 * item must not be newly created and not yet added to the design.
	 * 
	 * @param content handle to the newly created element
	 * @throws SemanticException if the element is not allowed to insert
	 */

	public void add(DesignElementHandle content) throws SemanticException {
		if (content == null)
			return;
		ContentCommand cmd = new ContentCommand(getModule(), new ContainerContext(getElement(), propDefn.getName()));
		cmd.add(content.getElement());
	}

	/**
	 * Adds a report item to this property at the given position. The item must not
	 * be newly created and not yet added to the design.
	 * 
	 * @param content handle to the newly created element
	 * @param newPos  the position index at which the content to be inserted,
	 *                0-based integer
	 * @throws SemanticException if the element is not allowed to insert
	 */

	public void add(DesignElementHandle content, int newPos) throws SemanticException {
		if (content == null)
			return;

		ContentCommand cmd = new ContentCommand(getModule(), new ContainerContext(getElement(), propDefn.getName()));
		cmd.add(content.getElement(), newPos);
	}

	/**
	 * Pastes a report item to this property. The item must be newly created and not
	 * yet added to the design.
	 * 
	 * @param content the newly created element handle
	 * @return a list containing all errors for the pasted element
	 * @throws SemanticException if the element is not allowed to paste
	 */

	public List paste(DesignElementHandle content) throws SemanticException {
		if (content == null)
			return Collections.EMPTY_LIST;

		add(content);

		return getElementHandle().checkPostPasteErrors(content.getElement());
	}

	/**
	 * Pastes a report item to this property. The item must be newly created and not
	 * yet added to the design.
	 * 
	 * @param content the newly created element
	 * @return a list containing all errors for the pasted element
	 * @throws SemanticException if the element is not allowed to paste
	 */

	public List paste(IDesignElement content) throws SemanticException {
		if (content == null)
			return Collections.EMPTY_LIST;
		add(content.getHandle(getModule()));

		return getElementHandle().checkPostPasteErrors((DesignElement) content);
	}

	/**
	 * Pastes a report item to the slot. The item must be newly created and not yet
	 * added to the design.
	 * 
	 * @param content the newly created element handle
	 * @param newPos  the position index at which the content to be inserted.
	 * @return a list containing all errors for the pasted element
	 * @throws SemanticException if the element is not allowed in the slot
	 */

	public List paste(DesignElementHandle content, int newPos) throws SemanticException {
		if (content == null)
			return Collections.EMPTY_LIST;
		add(content, newPos);

		return Collections.EMPTY_LIST;
		// return checkPostPasteErrors( content.getElement( ) );
	}

	/**
	 * Pastes a report item to the property. The item must be newly created and not
	 * yet added to the design.
	 * 
	 * @param content the newly created element
	 * @param newPos  the position index at which the content to be inserted.
	 * @return a list containing all errors for the pasted element
	 * @throws SemanticException if the element is not allowed in the property
	 */

	public List paste(IDesignElement content, int newPos) throws SemanticException {
		if (content == null)
			return Collections.EMPTY_LIST;
		add(content.getHandle(getModule()), newPos);

		return getElementHandle().checkPostPasteErrors((DesignElement) content);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#getListValue()
	 */
	public ArrayList getListValue() {
		if (propDefn.isElementType()) {
			Object value = getValue();
			if (value == null)
				return new ArrayList();
			if (value instanceof DesignElementHandle) {
				ArrayList result = new ArrayList();
				result.add(value);
				return result;
			} else if (value instanceof ArrayList) {
				ArrayList retValue = new ArrayList();
				retValue.addAll((ArrayList) value);
				return retValue;
			}
		}
		return super.getListValue();
	}

	/**
	 * Returns the a list with contents.Items are handles to the contents and in
	 * order by position.
	 * 
	 * @return a list with property contents, items of the list are handles to the
	 *         contents.
	 */

	public List getContents() {
		if (propDefn.isElementType())
			return getListValue();
		return Collections.EMPTY_LIST;
	}

	/**
	 * Returns the number of elements in the property.
	 * 
	 * @return the count of contents in the property
	 */

	public int getContentCount() {
		return getContents().size();
	}

	/**
	 * Moves the position of a content element within the slot.
	 * 
	 * @param content handle to the content to move
	 * @param toPosn  the new position
	 * @throws ContentException if the content is not in the slot, or if the to
	 *                          position is not valid.
	 */

	public void shift(DesignElementHandle content, int toPosn) throws ContentException {
		if (content == null)
			return;
		ContentCommand cmd = new ContentCommand(getModule(), new ContainerContext(getElement(), propDefn.getName()));
		cmd.movePosition(content.getElement(), toPosn);
	}

	/**
	 * Moves a content element into a slot in another container element.
	 * 
	 * @param content      a handle to the element to move
	 * @param newContainer a handle to the new container element
	 * @param propName     the target property name where the element will be moved
	 *                     to.
	 * @throws ContentException if the content is not in this slot or if the new
	 *                          container is not, in fact, a container, or if the
	 *                          content cannot go into the target slot.
	 */

	public void move(DesignElementHandle content, DesignElementHandle newContainer, String propName)
			throws ContentException {
		if (content == null || newContainer == null)
			return;
		ContentCommand cmd = new ContentCommand(getModule(), new ContainerContext(getElement(), propDefn.getName()));
		cmd.move(content.getElement(), new ContainerContext(newContainer.getElement(), propName));
	}

	/**
	 * Moves a content element into a slot in another container element at the
	 * specified position.
	 * 
	 * @param content      a handle to the element to move
	 * @param newContainer a handle to the new container element
	 * @param propName     the target property name where the element will be moved
	 *                     to.
	 * @param newPos       the position to which the content will be moved. If it is
	 *                     greater than the current size of the target slot, the
	 *                     content will be appended at the end of the target slot.
	 * @throws ContentException if the content is not in this slot or if the new
	 *                          container is not, in fact, a container, or if the
	 *                          content cannot go into the target slot.
	 */

	public void move(DesignElementHandle content, DesignElementHandle newContainer, String propName, int newPos)
			throws ContentException {
		if (content == null || newContainer == null)
			return;
		ContentCommand cmd = new ContentCommand(getModule(), new ContainerContext(getElement(), propDefn.getName()));
		cmd.move(content.getElement(), new ContainerContext(newContainer.getElement(), propName), newPos);
	}

	/**
	 * Drops a content element from the slot, and clear any reference property which
	 * refers the element to drop.
	 * 
	 * @param content a handle to the content to drop
	 * @throws SemanticException if the content is not within the slot
	 */

	public void dropAndClear(DesignElementHandle content) throws SemanticException {
		if (content == null)
			return;
		ContentCommand cmd = new ContentCommand(getModule(), new ContainerContext(getElement(), propDefn.getName()),
				false, false);
		cmd.remove(content.getElement());
	}

	/**
	 * Drops a content element from the slot, and unresolve any reference property
	 * which refers the element to drop.
	 * 
	 * @param content a handle to the content to drop
	 * @throws SemanticException if the content is not within the slot
	 */

	public void drop(DesignElementHandle content) throws SemanticException {
		if (content == null)
			return;
		ContentCommand cmd = new ContentCommand(getModule(), new ContainerContext(getElement(), propDefn.getName()),
				false, true);
		cmd.remove(content.getElement());
	}

	/**
	 * Drops a content element at the given position from the slot, and clear any
	 * reference property which refers the element to drop.
	 * 
	 * @param posn the position of the content to drop
	 * @throws SemanticException if the position is out of range
	 */

	public void dropAndClear(int posn) throws SemanticException {
		if (propDefn.getTypeCode() == IPropertyType.ELEMENT_TYPE) {
			DesignElementHandle content = (DesignElementHandle) get(posn);
			dropAndClear(content);
		} else {
			removeItem(posn);
		}
	}

	/**
	 * Drops a content element at the given position from the slot, and unresolve
	 * any reference property which refers the element to drop.
	 * 
	 * @param posn the position of the content to drop
	 * @throws SemanticException if the position is out of range
	 */

	public void drop(int posn) throws SemanticException {
		if (propDefn.isElementType()) {
			DesignElementHandle content = (DesignElementHandle) get(posn);
			drop(content);
		} else {
			removeItem(posn);
		}
	}

	/**
	 * Determines if the slot can contain an element with the type of
	 * <code>type</code>.
	 * 
	 * @param type the name of the element type, like "Table", "List", etc.
	 * @return <code>true</code> if the slot can contain the an element with
	 *         <code>type</code> type, otherwise <code>false</code>.
	 * 
	 */

	public boolean canContain(String type) {
		return getElementHandle().canContain(propDefn.getName(), type);
	}

	/**
	 * Determines if the given slot can contain the <code>content</code>.
	 * 
	 * @param content the design element handle to check
	 * 
	 * @return <code>true</code> if the slot with the given <code>slotId</code> can
	 *         contain the <code>content</code>, otherwise <code>false</code>.
	 */

	public boolean canContain(DesignElementHandle content) {
		return getElementHandle().canContain(propDefn.getName(), content);
	}

	/**
	 * Gets the content at the given position.
	 * 
	 * @param posn the index where the content resides
	 * @return the corresponding element
	 */

	public DesignElementHandle getContent(int posn) {
		Object value = get(posn);
		if (value instanceof DesignElementHandle)
			return (DesignElementHandle) value;

		return null;
	}

	/**
	 * 
	 * @param encryptionID
	 * @throws SemanticException
	 */
	public void setEncryption(String encryptionID) throws SemanticException {
		EncryptionCommand cmd = new EncryptionCommand(getModule(), getElement());
		cmd.setEncryption(propDefn, encryptionID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#getItems()
	 */
	public List getItems() {
		if (propDefn.isListType() && propDefn.getSubTypeCode() == IPropertyType.ELEMENT_REF_TYPE) {
			ArrayList listValue = new ArrayList();
			Object rawValue = getRawValue();
			if (rawValue instanceof List) {
				for (Object value : (List) rawValue) {
					assert value instanceof ElementRefValue;
					ElementRefValue refValue = (ElementRefValue) value;
					DesignElementHandle refHandle = null;
					if (refValue.isResolved())
						refHandle = refValue.getElement().getHandle(getModule());
					listValue.add(refHandle);
				}
			}
			return listValue;
		}
		return super.getItems();
	}

	@Override
	public boolean isVisible() {
		return false;
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}
}
