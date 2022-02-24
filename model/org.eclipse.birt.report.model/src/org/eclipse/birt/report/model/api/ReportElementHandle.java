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
import java.util.Iterator;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.PropertyNameException;
import org.eclipse.birt.report.model.api.elements.structures.PropertyMask;
import org.eclipse.birt.report.model.command.ComplexPropertyCommand;
import org.eclipse.birt.report.model.command.PropertyCommand;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * Abstract base class for working with all elements except the report design. A
 * report element can defines property masks for its property. A property mask
 * says how to hide or lock an BIRT ERD-defined or developer-defined property.
 */

public abstract class ReportElementHandle extends DesignElementHandle {

	/**
	 * The target report element.
	 */

	protected DesignElement element;

	/**
	 * Constructs the handle for a report element with the given design and element.
	 * The application generally does not create handles directly. Instead, it uses
	 * one of the navigation methods available on other element handles.
	 *
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public ReportElementHandle(Module module, DesignElement element) {
		super(module);
		assert element != null;
		this.element = element;

		// the slot handles must be cached.

		initializeSlotHandles();

		// do not cache property handles if it is runtime.
		ModuleOption options = module.getOptions();
		if (options != null && !options.useSemanticCheck()) {
			return;
		}

		cachePropertyHandles();
	}

	// Implementation of an abstract method in the base class.

	@Override
	public DesignElement getElement() {
		return element;
	}

	/**
	 * Returns property masks on this element. This method follows these rules:
	 *
	 * <ul>
	 * <li>If any property mask exists on this element, returns property mask list
	 * of itself.
	 * <li>If no property masks on this element, returns property mask list of its
	 * parent.
	 * </ul>
	 *
	 * @return the iterator of property mask structure list
	 *
	 * @see #getPropertyMask(String)
	 */

	public Iterator propertyMaskIterator() {
		PropertyHandle propHandle = getPropertyHandle(IDesignElementModel.PROPERTY_MASKS_PROP);
		assert propHandle != null;
		return propHandle.iterator();
	}

	/**
	 * Returns the mask of the specified property given its internal name. This
	 * method follows these rules:
	 *
	 * <ul>
	 * <li>If the mask of a specified property is defined on this element, returns
	 * the mask value.
	 * <li>If the mask of a specified property is not defined on this element,
	 * returns the mask value inherited from its ancestor.
	 * <li><code>null</code> is returned if no mask value defined on this property.
	 * </ul>
	 *
	 * <p>
	 * The optional mask values are defined in <code>DesignChoiceConstants.</code>
	 * and they are
	 *
	 * <ul>
	 * <li><code>PROPERTYMASK_TYPE_CHANGE</code>
	 * <li><code>PROPERTYMASK_TYPE_LOCK</code>
	 * <li><code>PROPERTYMASK_TYPE_HIDE</code>
	 * </ul>
	 *
	 *
	 * @param propName the name of the property to get. Can be a system-defined or
	 *                 user-defined property name.
	 *
	 * @return the property mask, or null if the mask is not set.
	 *
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 */

	public String getPropertyMask(String propName) {
		ElementPropertyDefn prop = getElement().getPropertyDefn(propName);
		if (prop == null) {
			return null;
		}

		return getElement().getPropertyMask(module, propName);
	}

	/**
	 * Sets the mask of the specified property. The mask values are defined in
	 * <code>DesignChoiceConstants</code> and they are:
	 *
	 * <ul>
	 * <li><code>PROPERTYMASK_TYPE_CHANGE</code>
	 * <li><code>PROPERTYMASK_TYPE_LOCK</code>
	 * <li><code>PROPERTYMASK_TYPE_HIDE</code>
	 * </ul>
	 *
	 * <p>
	 * Note it is not allowed to set the mask on <code>PROPERTY_MASKS_PROP</code>.
	 * This method does nothing for this situation.
	 *
	 * @param propName  the property name to get. Can be a system-defined or
	 *                  user-defined property name.
	 *
	 * @param maskValue the mask value
	 *
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 *
	 * @throws SemanticException if the maskValue is not one of the above.
	 */

	public void setPropertyMask(String propName, String maskValue) throws SemanticException {
		if (IDesignElementModel.PROPERTY_MASKS_PROP.equalsIgnoreCase(propName)) {
			return;
		}

		ElementPropertyDefn maskProp = getElement().getPropertyDefn(IDesignElementModel.PROPERTY_MASKS_PROP);
		if (maskProp == null) {
			throw new PropertyNameException(element, propName);
		}

		ElementPropertyDefn prop = getElement().getPropertyDefn(propName);
		if (prop == null) {
			return;
		}

		ArrayList masks = (ArrayList) getElement().getLocalProperty(getModule(),
				IDesignElementModel.PROPERTY_MASKS_PROP);

		PropertyMask mask = null;

		if (masks == null) {
			masks = new ArrayList();
			getElement().setProperty(IDesignElementModel.PROPERTY_MASKS_PROP, masks);
		}

		for (int i = 0; i < masks.size(); i++) {
			PropertyMask tmpMask = (PropertyMask) masks.get(i);
			if (propName.equalsIgnoreCase(tmpMask.getName())) {
				mask = tmpMask;
				break;
			}
		}

		ComplexPropertyCommand cmd = new ComplexPropertyCommand(module, getElement());

		if (maskValue == null && mask != null) {
			// maskValue is null, remove the item from the structure list.

			cmd.removeItem(new StructureContext(element, maskProp, null), masks.indexOf(mask));
		} else {
			StructPropertyDefn maskDefn = (StructPropertyDefn) maskProp.getStructDefn()
					.getMember(PropertyMask.MASK_MEMBER);
			StructPropertyDefn nameDefn = (StructPropertyDefn) maskProp.getStructDefn()
					.getMember(PropertyMask.NAME_MEMBER);
			String value = maskDefn.validateValue(getModule(), getElement(), maskValue).toString();

			/*
			 * If the property has no mask related to, adds a new mask item into the
			 * structure list.
			 */

			if (mask == null) {
				mask = new PropertyMask();
				mask.setProperty(maskDefn, value);
				mask.setProperty(nameDefn, propName);
				cmd.addItem(new StructureContext(element, maskProp, null), mask);
			} else {
				// changes the mask value.

				StructureContext memberRef = new StructureContext(mask,
						(PropertyDefn) mask.getDefn().getMember(PropertyMask.MASK_MEMBER), null);
				PropertyCommand pCmd = new PropertyCommand(module, getElement());
				pCmd.setMember(memberRef, value);
			}
		}
	}

	/**
	 * Sets the resource key of the display name.
	 *
	 * @param displayNameKey the resource key of the display name
	 * @throws SemanticException if the display name resource-key property is locked
	 *                           or not defined on this element.
	 */

	public void setDisplayNameKey(String displayNameKey) throws SemanticException {
		setStringProperty(IDesignElementModel.DISPLAY_NAME_ID_PROP, displayNameKey);
	}

	/**
	 * Gets the resource key of the display name.
	 *
	 * @return the resource key of the display name
	 */

	public String getDisplayNameKey() {
		return getStringProperty(IDesignElementModel.DISPLAY_NAME_ID_PROP);
	}

	/**
	 * Sets the display name.
	 *
	 * @param displayName the display name
	 * @throws SemanticException if the display name property is locked or not
	 *                           defined on this element.
	 */

	public void setDisplayName(String displayName) throws SemanticException {
		setStringProperty(IDesignElementModel.DISPLAY_NAME_PROP, displayName);
	}

	/**
	 * Gets the display name.
	 *
	 * @return the display name
	 */

	public String getDisplayName() {
		return getStringProperty(IDesignElementModel.DISPLAY_NAME_PROP);
	}

	/**
	 * Sets the custom XML.
	 *
	 * @param customXml the custom XML to set
	 * @throws SemanticException if the custom XML is locked or not defined on this
	 *                           element.
	 */

	public void setCustomXml(String customXml) throws SemanticException {
		setStringProperty(IDesignElementModel.CUSTOM_XML_PROP, customXml);
	}

	/**
	 * Gets the custom XML.
	 *
	 * @return the custom XML
	 */

	public String getCustomXml() {
		return getStringProperty(IDesignElementModel.CUSTOM_XML_PROP);
	}

	/**
	 * Sets the comments of the report element.
	 *
	 * @param theComments the comments to set
	 * @throws SemanticException if the comments property is locked or not defined
	 *                           on this element.
	 */

	public void setComments(String theComments) throws SemanticException {
		setStringProperty(IDesignElementModel.COMMENTS_PROP, theComments);
	}

	/**
	 * Gets the comments of the report element.
	 *
	 * @return the comments of the report element
	 */

	public String getComments() {
		return getStringProperty(IDesignElementModel.COMMENTS_PROP);
	}

	/**
	 * Duplicates the extended element of this design element.
	 */

	void duplicateExtendedElement() {
		DesignElementHandle extendedElementHandle = getExtends();
		if (extendedElementHandle == null) {
			return;
		}

		try {
			DesignElement cloned;
			cloned = (DesignElement) extendedElementHandle.getElement().clone();
			element = cloned;
		} catch (CloneNotSupportedException e) {
			// All elements support clone.

			assert false;
		}
	}

	/**
	 * Checks whether the compound element is valid if the element has no extends
	 * property value or if the current element is compound elements and extends
	 * value is unresovled.
	 *
	 * @return <code>true</code> if the compound element is valid. Otherwise
	 *         <code>false</code>.
	 *
	 * @deprecated
	 */

	@Deprecated
	public boolean isValidReferenceForCompoundElement() {
		return ModelUtil.isValidReferenceForCompoundElement(getModule(), element);
	}

	/**
	 * Checks whether the compound element is valid. If a table/grid has no
	 * rows/columns, it is invalid. If the table has overlapped areas, it is
	 * invalid.
	 *
	 * @return <code>true</code> if the compound element is valid. Otherwise
	 *         <code>false</code>.
	 */

	public boolean isValidLayoutForCompoundElement() {
		return ModelUtil.isValidLayout(getModule(), element);
	}
}
