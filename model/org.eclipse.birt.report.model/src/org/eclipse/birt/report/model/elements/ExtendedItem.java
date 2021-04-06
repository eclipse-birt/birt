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

package org.eclipse.birt.report.model.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.extension.CompatibilityStatus;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.ICompatibleReportItem;
import org.eclipse.birt.report.model.api.extension.IPropertyDefinition;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.extension.IStyleDeclaration;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.validators.ExtensionValidator;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IExtendedItemModel;
import org.eclipse.birt.report.model.elements.interfaces.ISupportThemeElement;
import org.eclipse.birt.report.model.elements.strategy.CopyPolicy;
import org.eclipse.birt.report.model.elements.strategy.ExtendedItemPropSearchStrategy;
import org.eclipse.birt.report.model.extension.DummyPeerExtensibilityProvider;
import org.eclipse.birt.report.model.extension.IExtendableElement;
import org.eclipse.birt.report.model.extension.PeerExtensibilityProvider;
import org.eclipse.birt.report.model.extension.PeerExtensibilityProviderFactory;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;
import org.eclipse.birt.report.model.util.ContentIterator;

/**
 * This class represents an extended item element. The extended report item
 * allows third-party developers to create report items that work within BIRT
 * virtually identically to BIRT-defined items. Extended items can use the
 * user-properties discussed above to define properties, can use a "black-box"
 * approach, or a combination of the two. Extended items are defined in a Java
 * plug-in that contributes behavior to the Eclipse Report Developer, to the
 * Factory and to the Presentation Engine. The extended item can fully
 * participate with the other BIRT extension facilities, meaning that report
 * developers can additional properties and scripts to an extended item,
 * providing a very powerful way to create application-specific functionality.
 * An extended item is defined by a plug-in. The plug-in is specific to BIRT,
 * and is different from an Eclipse plug-in. Each item plug-in has four parts:
 * <ul>
 * <li>Design: handles the model that describes the report item.
 * <li>User Interface: the UI displayed for the item. This is in the form of an
 * Eclipse plug-in.
 * <li>Factory: how to gather the data for the extended item, and compute its
 * default size in the Factory.
 * <li>Presentation: how to render the extended item when rendering the report
 * to HTML, PDF or other formats.
 * </ul>
 * 
 * 
 */

public class ExtendedItem extends ReportItem implements IExtendableElement, IExtendedItemModel, ISupportThemeElement {

	/**
	 * Extended item can support extension. It has a unique name to identify the
	 * extension. Using this name, BIRT can get the extension definition. The name
	 * is an internal name for an implementation of extension.
	 * <p>
	 * The name does not occur in a name space.
	 */

	protected String extensionName = null;

	/**
	 * The version of the extended element model. It is individual from the version
	 * of the report design.
	 */

	protected String extensionVersion = null;

	/**
	 * The extensibility provider which provides the functionality of this
	 * extensible element.
	 */

	protected PeerExtensibilityProvider provider = null;

	/**
	 * Default constructor.
	 */

	public ExtendedItem() {
		this(null);
	}

	/**
	 * Constructs the extended item with an optional name.
	 * 
	 * @param theName optional item name
	 */

	public ExtendedItem(String theName) {
		super(theName);
		provider = new DummyPeerExtensibilityProvider(this, null);
		cachedPropStrategy = ExtendedItemPropSearchStrategy.getInstance();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt
	 * .report.model.elements.ElementVisitor)
	 */

	public void apply(ElementVisitor visitor) {
		visitor.visitExtendedItem(this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName() {
		return ReportDesignConstants.EXTENDED_ITEM;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getHandle(org.eclipse
	 * .birt.report.model.elements.ReportDesign)
	 */

	public DesignElementHandle getHandle(Module module) {
		return handle(module);
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param module the report design
	 * 
	 * @return an API handle for this element.
	 */

	public ExtendedItemHandle handle(Module module) {
		if (handle == null) {
			handle = new ExtendedItemHandle(module, this);
			IReportItem item = provider.getExtensionElement();
			if (item != null) {
				item.setHandle((ExtendedItemHandle) handle);
			}
		}
		return (ExtendedItemHandle) handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#hasLocalPropertyValues()
	 */

	public boolean hasLocalPropertyValues() {
		if (super.hasLocalPropertyValues())
			return true;

		return provider.hasLocalPropertyValues();
	}

	/**
	 * Gets a property value given its definition. This version checks not only this
	 * one object, but also the extended element this item has. That is, it gets the
	 * "local" property value. The property name must also be valid for this object.
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getLocalProperty(Module,
	 *      org.eclipse.birt.report.model.metadata.ElementPropertyDefn)
	 */

	public Object getLocalProperty(Module module, ElementPropertyDefn prop) {
		assert prop != null;

		if (!prop.isExtended())
			return super.getLocalProperty(module, prop);

		return provider.getExtensionProperty(module, prop);
	}

	/**
	 * Sets the value of a property. The value must have already been validated, and
	 * must be of the correct type for the property. The property must be valid for
	 * this object. The property can be a system, user-defined property and a
	 * property from the extended element of this item. The value is set locally. If
	 * the value is null, then the property is "unset."
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#setProperty(org.eclipse.birt.report.model.metadata.ElementPropertyDefn,
	 *      java.lang.Object)
	 */

	public void setProperty(ElementPropertyDefn prop, Object value) {
		assert prop != null;

		if (!prop.isExtended())
			super.setProperty(prop, value);
		else
			provider.setExtensionProperty(prop, value);
	}

	/**
	 * Gets the property data for either a system-defined, user-defined property or
	 * extension property from extended element of this item.
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getPropertyDefn(java.lang.String)
	 */

	public ElementPropertyDefn getPropertyDefn(String propName) {
		assert propName != null;

		ElementPropertyDefn propDefn = super.getPropertyDefn(propName);
		if (propDefn != null)
			return propDefn;

		return (ElementPropertyDefn) provider.getPropertyDefn(propName);
	}

	/**
	 * Gets the list of property definitions available to this element. Includes all
	 * properties defined for this element, all user-defined properties defined on
	 * this element or its ancestors, any style properties that this element
	 * supports and extension properties that the extended element of this item
	 * supports.
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getPropertyDefns()
	 */

	public List<IElementPropertyDefn> getPropertyDefns() {
		return provider.getPropertyDefns();
	}

	/**
	 * Gets the definition of the extension element.
	 * 
	 * @return the definition of the extension element if found, or null if the
	 *         extended item is not extensible or the extension element is not
	 *         registered in BIRT
	 */

	public ExtensionElementDefn getExtDefn() {
		return provider.getExtDefn();
	}

	/**
	 * Creates an instance of <code>IReportItem</code> to store the information of
	 * the peer extension. When the application invokes UI for the extended item, it
	 * calls this method to get the instance of the peer extension and reads the
	 * information--property values from the BIRT ROM properties. If there is no
	 * instance of peer for the item before the calling and then it is successfully
	 * created. If the item has no extension peer for it or the peer instance has
	 * been created before, then there is no operation.
	 * 
	 * @param module the module the peer element has
	 * @throws ExtendedElementException if the serialized model is invalid
	 */

	public void initializeReportItem(Module module) throws ExtendedElementException {
		provider.initializeReportItem(module);
	}

	/**
	 * Returns the extensibility provider which provides the functionality of this
	 * extensible element.
	 * 
	 * @return the extensibility provider which provides the functionality of this
	 *         extensible element.
	 */

	public PeerExtensibilityProvider getExtensibilityProvider() {
		return provider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getIntrinsicProperty
	 * (java.lang.String)
	 */

	protected Object getIntrinsicProperty(String propName) {
		if (EXTENSION_NAME_PROP.equals(propName))
			return extensionName;
		if (EXTENSION_VERSION_PROP.equals(propName))
			return extensionVersion;
		return super.getIntrinsicProperty(propName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#setIntrinsicProperty
	 * (java.lang.String, java.lang.Object)
	 */

	protected void setIntrinsicProperty(String propName, Object value) {
		if (EXTENSION_NAME_PROP.equals(propName)) {
			setExtensionName((String) value);
		} else if (EXTENSION_VERSION_PROP.equals(propName)) {
			this.extensionVersion = (String) value;
		} else {
			super.setIntrinsicProperty(propName, value);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse
	 * .birt.report.model.elements.ReportDesign)
	 */

	public List<SemanticException> validate(Module module) {
		List<SemanticException> list = super.validate(module);

		list.addAll(ExtensionValidator.getInstance().validate(module, this));

		return list;
	}

	/**
	 * Gets the effective extension element of this extended item.
	 * 
	 * @return the effective extension element
	 */

	public IReportItem getExtendedElement() {
		return provider.getExtensionElement();
	}

	/**
	 * Tests whether the property is a dynamic property of extended element or not.
	 * 
	 * @param propName the property name to check
	 * @return true if the property is one of the dynamic properties for extended
	 *         element, otherwise false
	 */

	public boolean isExtensionModelProperty(String propName) {
		return provider.isExtensionModelProperty(propName);
	}

	/**
	 * Tests whether the property is just the model property for the extended
	 * element, and its type is XML.
	 * 
	 * @param propName the property name to check
	 * @return true if the property is XML type and it is model property for
	 *         extended element, otherwise false
	 */

	public boolean isExtensionXMLProperty(String propName) {
		return provider.isExtensionXMLProperty(propName);
	}

	/**
	 * returns the methods defined on this element and defined in the extension
	 * model.
	 * 
	 * @return the method list
	 */
	public List<IElementPropertyDefn> getMethods() {
		return provider.getModelMethodDefns();
	}

	/**
	 * Returns the script property name of this extended item.
	 * 
	 * @return the script property name
	 */

	public String getScriptPropertyName() {
		IPropertyDefinition defn = provider.getScriptPropertyDefinition();
		return defn == null ? null : defn.getName();
	}

	/**
	 * Sets the extension name for this extended item. At the same time, initialize
	 * the extension provider and slot.
	 * 
	 * @param extension the extension name to set
	 */

	private void setExtensionName(String extension) {
		extensionName = extension;
		provider = PeerExtensibilityProviderFactory.createProvider(this, extensionName);
		ExtensionElementDefn defn = provider.getExtDefn();
		if (defn != null) {
			cachedDefn = defn;
		}
		initSlots();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getSlot(int)
	 */

	public ContainerSlot getSlot(int slot) {
		assert slot >= 0 && slot < cachedDefn.getSlotCount();
		return slots[slot];
	}

	/**
	 * Returns if this extended item has local property values on own model.
	 * 
	 * @return <code>true</code> if this extended item has local property values on
	 *         own model, <code>false</code> otherwise.
	 */

	public boolean hasLocalPropertyValuesOnOwnModel() {
		return provider.hasLocalPropertyValuesOnOwnModel();
	}

	/**
	 * Returns the predefined styles that are provided by the
	 * ExtendedItem.reportItem.
	 * 
	 * @param module the root of the extended item
	 * @return a list containing predefined selectors in string
	 */

	public List<Object> getReportItemDefinedSelectors(Module module) {
		IReportItem reportItem = getExtendedElement();
		try {
			if (reportItem == null) {
				initializeReportItem(module);
				reportItem = getExtendedElement();
			}
		} catch (ExtendedElementException e) {
		}
		if (reportItem == null)
			return Collections.emptyList();

		return reportItem.getPredefinedStyles();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getLocalEncryptionID
	 * (org.eclipse.birt.report.model.metadata.ElementPropertyDefn)
	 */
	public String getLocalEncryptionID(ElementPropertyDefn propDefn) {
		if (propDefn == null || !propDefn.isEncryptable())
			return null;
		if (!propDefn.isExtended())
			return super.getLocalEncryptionID(propDefn);

		return provider.getEncryptionHelperID(propDefn);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#setEncryptionHelper(
	 * org.eclipse.birt.report.model.metadata.ElementPropertyDefn, java.lang.String)
	 */

	public void setEncryptionHelper(ElementPropertyDefn propDefn, String encryptionID) {
		if (!propDefn.isExtended())
			super.setEncryptionHelper(propDefn, encryptionID);

		provider.setEncryptionHelper(propDefn, encryptionID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#hasLocalValue(org.eclipse
	 * .birt.report.model.metadata.ElementPropertyDefn)
	 */
	protected boolean hasLocalValue(ElementPropertyDefn propDefn) {
		if (propDefn == null)
			return false;
		if (!propDefn.isExtended())
			return super.hasLocalValue(propDefn);
		return provider.getExtensionProperty(getRoot(), propDefn) != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.StyledElement#getFactoryProperty(org
	 * .eclipse.birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.metadata.ElementPropertyDefn)
	 */

	public Object getFactoryProperty(Module module, ElementPropertyDefn prop) {
		// this method has to be overridden since IReportItem.getProperty() may
		// make calls to FactoryPropertyHandle. The idea is that if the
		// useOwnSearch = true, do not delegate to IReportItem.getProperty().

		if (!prop.enableContextSearch() || !prop.isStyleProperty())
			return super.getFactoryProperty(module, prop);

		// only the style property with enableContextSearch = true

		return ((ExtendedItemPropSearchStrategy) ExtendedItemPropSearchStrategy.getInstance())
				.getMetaFactoryProperty(module, this, prop);
	}

	/**
	 * Checks the compatibilities for this extended item.
	 * 
	 * @param module
	 * @return the status infor for compatibility
	 */
	public StatusInfo checkCompatibility(Module module) {

		// check this element itself
		StatusInfo status = doCheck(module);
		List<SemanticException> errors = new ArrayList<SemanticException>();
		boolean hasCompatibilities = false;
		if (status != null) {
			errors.addAll(status.getErrors());
			hasCompatibilities = status.hasCompatibilities();
		}

		// if this extended-item has parent, then check all virtual children
		if (getExtendsElement() != null) {
			ContentIterator iter = new ContentIterator(module, this);
			while (iter.hasNext()) {
				DesignElement content = iter.next();
				if (content instanceof ExtendedItem) {
					status = ((ExtendedItem) content).doCheck(module);
					if (status != null) {
						errors.addAll(status.getErrors());
						if (!hasCompatibilities && status.hasCompatibilities())
							hasCompatibilities = true;
					}
				}
			}
		}

		return new StatusInfo(errors, hasCompatibilities);
	}

	private StatusInfo doCheck(Module module) {
		if (!provider.needCheckCompatibility())
			return new StatusInfo(new ArrayList<SemanticException>(), false);
		try {
			initializeReportItem(module);
		} catch (ExtendedElementException e) {
			return new StatusInfo(new ArrayList<SemanticException>(), false);
		}
		IReportItem item = getExtendedElement();

		if (item instanceof ICompatibleReportItem) {
			CompatibilityStatus status = ((ICompatibleReportItem) item).checkCompatibility();
			boolean hasCompatibilities = false;
			List<SemanticException> errors = Collections.emptyList();
			if (status != null) {
				errors = status.getErrors();
				hasCompatibilities = status.getStatusType() == CompatibilityStatus.OK_TYPE ? false : true;
			}
			return new StatusInfo(errors, hasCompatibilities);
		}

		return new StatusInfo(new ArrayList<SemanticException>(), false);
	}

	/**
	 * Inner class to record the check result.
	 */
	public static class StatusInfo {

		private List<SemanticException> errors;
		private boolean hasCompatibilities = false;

		/**
		 * Constructs the status information with the error list and hasCompatibilities
		 * status.
		 * 
		 * @param errors
		 * @param hasCompatibilities
		 */
		public StatusInfo(List<SemanticException> errors, boolean hasCompatibilities) {
			this.errors = errors;
			this.hasCompatibilities = hasCompatibilities;
		}

		/**
		 * 
		 * @return
		 */
		public List<SemanticException> getErrors() {
			return (List<SemanticException>) (this.errors == null ? Collections.emptyList() : errors);
		}

		/**
		 * 
		 * @return
		 */
		public boolean hasCompatibilities() {
			return this.hasCompatibilities;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.ReferencableStyledElement#doClone(
	 * org.eclipse.birt.report.model.elements.strategy.CopyPolicy)
	 */

	public Object doClone(CopyPolicy policy) throws CloneNotSupportedException {
		// The parameter policy should be set as null to avoid the definition of
		// the extended item property to be copied.
		ExtendedItem clonedElement = (ExtendedItem) super.doClone(null);
		clonedElement.provider.copyFromWithElementType(provider, policy);

		// To copy correctly, the additional operation should be done.
		if (policy != null)
			policy.execute(this, clonedElement);
		return clonedElement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#baseClone()
	 */

	protected Object baseClone() throws CloneNotSupportedException {
		ExtendedItem clonedElement = (ExtendedItem) super.baseClone();
		clonedElement.provider = PeerExtensibilityProviderFactory.createProvider(clonedElement,
				clonedElement.extensionName);
		clonedElement.provider.copyFromWithNonElementType(provider);

		return clonedElement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementSelector()
	 */
	public List<String> getElementSelectors() {
		List<String> list = new ArrayList<String>();

		String selector = null;

		// get extension element definition of the extended item.

		ElementDefn elementDefn = getExtDefn();
		if (elementDefn != null)
			selector = elementDefn.getSelector();

		if (selector != null)
			list.add(selector);

		List tmpSelectors = getReportItemDefinedSelectors(getRoot());
		for (int i = 0; i < tmpSelectors.size(); i++) {
			Object styleObject = tmpSelectors.get(i);

			if (styleObject instanceof IStyleDeclaration)
				selector = ((IStyleDeclaration) styleObject).getName();
			else
				selector = (String) styleObject;

			if (selector != null)
				list.add(selector);

		}

		return list;
	}

}