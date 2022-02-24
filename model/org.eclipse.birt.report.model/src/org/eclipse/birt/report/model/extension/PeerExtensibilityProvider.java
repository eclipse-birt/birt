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

package org.eclipse.birt.report.model.extension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.report.model.api.ModelException;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IPropertyDefinition;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.extension.IReportItemFactory;
import org.eclipse.birt.report.model.api.extension.UndefinedPropertyInfo;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.util.UnicodeUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.strategy.CopyPolicy;
import org.eclipse.birt.report.model.extension.SimplePeerExtensibilityProvider.UndefinedChildInfo;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.ExtensionModelPropertyDefn;
import org.eclipse.birt.report.model.metadata.PeerExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.ReferenceValue;
import org.eclipse.birt.report.model.parser.treebuild.IContentHandler;
import org.eclipse.birt.report.model.util.EncryptionUtil;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.ReferenceValueUtil;
import org.eclipse.birt.report.model.util.StructureContextUtil;

/**
 * Represents the extensibility provider which supports the peer extension. The
 * peer extension means the third-party can define the extension element with
 * extension properties and extension model in extension definition file. The
 * extension element has its own display name, and attributes, which makes it
 * looks like a new element. However, the extension element is based on the
 * <code>ReportItem</code>, which Model defines, and can have style properties.
 * The extension element can define two type of properties:
 * <ul>
 * <li>The Extension Property - It is also defined in extension definition file.
 * It provides the name, data type, display name key and so on, as the system
 * property works for the Model-defined element.
 * <li>The Extension Model Property - It is defined by {@link IReportItem},
 * instead in extension definition file. <code>IReportItem</code> defines its
 * own dynamic model. The extension model property definition is defined by
 * {@link IPropertyDefinition}. The dynamic model means the extension element
 * can switch from one set of extension model properties to another under some
 * case. They can be serialized to an XML-type extension property when saving to
 * design file, and deserialized from this XML-type extension property when
 * loading design file.
 * </ul>
 *
 */

public abstract class PeerExtensibilityProvider extends ModelExtensibilityProvider implements IContentHandler {

	/**
	 * Peer element for the extension. It is created when the application invokes UI
	 * for the item.
	 */

	IReportItem reportItem = null;

	/**
	 * Cached property values for extension properties, which is defined in
	 * extension definition file.
	 */

	HashMap<String, Object> extensionPropValues = new HashMap<>();

	/**
	 *
	 */
	Map<String, String> encryptionMap = null;

	/**
	 * Constructs the peer extensibility provider with the extensible element and
	 * the extension name.
	 *
	 * @param element       the extensible element
	 * @param extensionName the extension name
	 */

	public PeerExtensibilityProvider(DesignElement element, String extensionName) {
		super(element, extensionName);
	}

	/**
	 * Returns the read-only list of all property definitions, including not only
	 * those defined in Model and extension definition file, but those defined by
	 * <code>IReportItem</code>. The returned list is read-only, so no modification
	 * is allowed on this list. Each one in list is the instance of
	 * <code>IPropertyDefn</code>.
	 *
	 * @return the read-only list of all property definitions. Return empty list if
	 *         there is no property defined.
	 */

	@Override
	public List<IElementPropertyDefn> getPropertyDefns() {
		List<IElementPropertyDefn> props = super.getPropertyDefns();

		PeerExtensionElementDefn extDefn = (PeerExtensionElementDefn) getExtDefn();

		// if no extension definition exists, just return the definition on
		// extended item.

		if (extDefn == null) {
			return props;
		}

		// If the extension provides dynamic property list, add them.

		IPropertyDefinition[] dynamicProps = getExtensionModelPropertyDefns();
		if (dynamicProps != null) {
			for (int i = 0; i < dynamicProps.length; i++) {
				IPropertyDefinition extProp = dynamicProps[i];
				props.add(new ExtensionModelPropertyDefn(extProp, extDefn.getReportItemFactory().getMessages()));
			}
		}

		return props;
	}

	/**
	 * Returns the methods defined on the element. Not only the method on the
	 * extension element definition but also include those defined inside the
	 * extension model.
	 *
	 * @return the method list
	 */

	public List<IElementPropertyDefn> getModelMethodDefns() {

		// collect the methods defined in the extension plugin.xml, and
		// extends from the Model element.

		PeerExtensionElementDefn extDefn = (PeerExtensionElementDefn) getExtDefn();
		if (extDefn == null) {
			return Collections.emptyList();
		}

		List<IElementPropertyDefn> methods = new ArrayList<>();

		if (extDefn.getMethods() != null) {
			methods.addAll(extDefn.getMethods());
		}

		if (reportItem == null) {
			reportItem = ((ExtendedItem) element).getExtendedElement();
		}

		if (reportItem == null) {
			return methods;
		}

		// collect the methods defined from the dynamic properties.

		IPropertyDefinition[] dynamicProps = getExtensionModelPropertyDefns();
		if (dynamicProps != null) {
			for (int i = 0; i < dynamicProps.length; i++) {
				IPropertyDefinition prop = dynamicProps[i];
				if (prop.getType() == IPropertyType.SCRIPT_TYPE) {
					methods.add(new ExtensionModelPropertyDefn(prop, extDefn.getReportItemFactory().getMessages()));
				}
			}
		}
		return methods;
	}

	/**
	 * Returns the element property definition with the given name from Model, the
	 * extension definition file or extension model defined by
	 * <code>IReportItem</code>.
	 *
	 * @param propName name of the property
	 * @return the element property definition with the given name
	 */

	@Override
	public ElementPropertyDefn getPropertyDefn(String propName) {
		ElementPropertyDefn propDefn = super.getPropertyDefn(propName);
		if (propDefn == null) {
			PeerExtensionElementDefn extDefn = (PeerExtensionElementDefn) getExtDefn();

			IPropertyDefinition[] extProps = getExtensionModelPropertyDefns();
			if (extProps != null) {
				for (int i = 0; i < extProps.length; i++) {
					if (propName.equalsIgnoreCase(extProps[i].getName())) {
						return new ExtensionModelPropertyDefn(extProps[i],
								extDefn.getReportItemFactory().getMessages());
					}
				}
			}
		}

		return propDefn;
	}

	/**
	 * Gets all the extension model properties defined by <code>IReportItem</code>.
	 *
	 * @return the extension model properties, null if extended element is null
	 */

	private IPropertyDefinition[] getExtensionModelPropertyDefns() {
		if (reportItem == null) {
			return null;
		}

		return reportItem.getPropertyDefinitions();
	}

	/**
	 * Gets the list of style masks for Model style properties.
	 *
	 * @return the list of the style masks
	 */

	protected List<String> getStyleMasks() {
		return Collections.emptyList();
	}

	/**
	 * Returns the value of extension property or extension model property.
	 *
	 * @param module
	 * @param prop
	 * @return the value of the given property. If the property is not found, or the
	 *         value is not set, return null.
	 */

	public Object getExtensionProperty(Module module, ElementPropertyDefn prop) {
		assert prop != null;
		String propName = prop.getName();

		Object value = getReportItemExtensionProperty(module, prop, propName);
		if (value != null) {
			return value;
		}

		// handle all other property values

		value = extensionPropValues.get(propName);

		if (value == null) {
			return null;
		}
		ElementPropertyDefn defn = (ElementPropertyDefn) getPropertyDefn(propName);
		if (defn.getTypeCode() == IPropertyType.ELEMENT_REF_TYPE) {
			Module root = element.getRoot();
			if (root != null) {
				return ReferenceValueUtil.resolveElementReference(root, element, defn, (ElementRefValue) value);
			}
		}

		return defn.isEncryptable() ? EncryptionUtil.decrypt(element, defn, extensionPropValues.get(propName))
				: extensionPropValues.get(propName);
	}

	/**
	 * Returns the value of extension model property.
	 *
	 * @param module
	 * @param prop
	 * @return the value of the given property. If the property is not found, or the
	 *         value is not set, return null.
	 */

	private Object getReportItemExtensionProperty(Module module, ElementPropertyDefn prop, String propName) {
		if (isExtensionXMLProperty(propName) && prop.hasOwnModel()) {
			if (reportItem != null) {
				ByteArrayOutputStream stream = reportItem.serialize(propName);
				if (stream == null) {
					return null;
				}

				String retValue = null;
				try {
					retValue = stream.toString(UnicodeUtil.SIGNATURE_UTF_8);
				} catch (UnsupportedEncodingException e) {
					assert false;
				}
				return retValue;
			}
		} else if (isExtensionModelProperty(propName)) {
			// If this property is extension model property, the instance of
			// IReportItem must exist.

			try {
				if (reportItem == null) {
					initializeReportItem(module);
				}
			} catch (ExtendedElementException e) {
				return null;
			}

			return reportItem == null ? null : reportItem.getProperty(propName);
		}

		return null;
	}

	/**
	 * Returns the value of extension property or extension model property.
	 *
	 * @param module
	 * @param prop
	 * @return the value of the given property. If the property is not found, or the
	 *         value is not set, return null.
	 */

	public Object getNonReportItemProperty(Module module, ElementPropertyDefn prop) {
		assert prop != null;
		String propName = prop.getName();

		// handle all other property values

		Object value = extensionPropValues.get(propName);

		if (value == null) {
			return null;
		}
		ElementPropertyDefn defn = (ElementPropertyDefn) getPropertyDefn(propName);
		if (defn.getTypeCode() == IPropertyType.ELEMENT_REF_TYPE) {
			Module root = element.getRoot();
			if (root != null) {
				return ReferenceValueUtil.resolveElementReference(root, element, defn, (ElementRefValue) value);
			}
		}

		return defn.isEncryptable() ? EncryptionUtil.decrypt(element, defn, extensionPropValues.get(propName))
				: extensionPropValues.get(propName);
	}

	/**
	 * Sets the value for extension property or extension model property.
	 *
	 * @param prop  the definition of the property
	 * @param value the value to set
	 */

	public void setExtensionProperty(ElementPropertyDefn prop, Object value) {
		if (isExtensionXMLProperty(prop.getName()) && prop.hasOwnModel()) {
			if (reportItem != null) {
				try {
					if (value != null) {
						byte[] raw = null;
						try {
							raw = value.toString().getBytes(UnicodeUtil.SIGNATURE_UTF_8);
						} catch (UnsupportedEncodingException e) {
							assert false;
						}

						reportItem.deserialize(prop.getName(), new ByteArrayInputStream(raw));
					} else {
						reportItem.deserialize(prop.getName(), new ByteArrayInputStream(new byte[0]));
					}
				} catch (ExtendedElementException e) {
					assert false;
				}
			} else {
				setExtensionPropertyValue(prop, value);
			}
		} else if (isExtensionModelProperty(prop.getName())) {
			// If this property is extension model property, the instance of
			// IReportItem must exist.

			assert reportItem != null;

			reportItem.setProperty(prop.getName(), value);
		} else {
			setExtensionPropertyValue(prop, value);
		}
	}

	/**
	 * Sets the value of the given property, which is extension property.
	 *
	 * @param propName the name of the property
	 * @param value    the value to set
	 */

	private void setExtensionPropertyValue(ElementPropertyDefn prop, Object value) {
		String propName = prop.getName();
		Object oldValue = extensionPropValues.get(propName);

		if (prop.getTypeCode() == IPropertyType.ELEMENT_REF_TYPE) {
			ElementRefValue oldRef = (ElementRefValue) oldValue;
			ReferenceValueUtil.updateReference(element, oldRef, (ReferenceValue) value, prop);
		}

		// establish the context if the value is a structure or structure list.

		StructureContextUtil.setStructureContext(prop, value, element);

		if (value != null) {
			extensionPropValues.put(propName, value);
		} else {
			extensionPropValues.remove(propName);
		}

	}

	/**
	 * Initializes the extension element instance of <code>IReportItem</code>.
	 *
	 * @param module module
	 * @throws ExtendedElementException if the extension is not found or it's failed
	 *                                  to initialized the extension element
	 *                                  instance.
	 */

	public void initializeReportItem(Module module) throws ExtendedElementException {

		if (reportItem != null) {
			return;
		}

		PeerExtensionElementDefn extDefn = (PeerExtensionElementDefn) getExtDefn();
		if (extDefn == null) {
			throw new ExtendedElementException(element, ModelException.PLUGIN_ID,
					SemanticError.DESIGN_EXCEPTION_EXTENSION_NOT_FOUND, null);
		}

		IReportItemFactory elementFactory = extDefn.getReportItemFactory();
		assert elementFactory != null;

		reportItem = elementFactory.newReportItem(element.getHandle(module));

		List<IElementPropertyDefn> localPropDefns = extDefn.getLocalProperties();
		for (int i = 0; i < localPropDefns.size(); i++) {
			ElementPropertyDefn propDefn = (ElementPropertyDefn) localPropDefns.get(i);

			if (propDefn.getTypeCode() != IPropertyType.XML_TYPE || !propDefn.canInherit() || !propDefn.hasOwnModel()) {
				continue;
			}

			String propName = propDefn.getName();
			Object value = extensionPropValues.get(propName);
			if (value == null) {
				// Get the raw xml data from parent.
				ExtendedItem parent = (ExtendedItem) ModelUtil.getParent(element);
				while (parent != null) {
					// get the value from the parent provider: read from the
					// hashmap or the reporItem
					PeerExtensibilityProvider parentProvider = parent.getExtensibilityProvider();
					HashMap<String, Object> propValues = parentProvider.extensionPropValues;
					value = propValues.get(propName);
					if (value == null) {
						if (parentProvider.reportItem != null) {
							value = parentProvider.reportItem.serialize(propName);
						}
					}

					if (value != null) {
						break;
					}

					parent = (ExtendedItem) ModelUtil.getParent(parent);
				}
			} else {
				// if the item caches the property values of extension, transfer
				// them and then clear the cached values

				this.extensionPropValues.remove(propName);
			}

			if (value != null) {
				byte[] raw = null;
				try {
					// if value is ByteArrayOutputStream : the value is
					// serialized by Model property calling method
					// IReportItem.serialize(String), since value.toString may
					// change UTF-8 bytes into unicode 16 bytes
					if (value instanceof ByteArrayOutputStream) {
						raw = ((ByteArrayOutputStream) value).toByteArray();
					} else {
						raw = value.toString().getBytes(UnicodeUtil.SIGNATURE_UTF_8);
					}
				} catch (UnsupportedEncodingException e) {
					assert false;
				}

				if (reportItem != null) {
					reportItem.deserialize(propName, new ByteArrayInputStream(raw));
				}
			}
		}
	}

	/**
	 * Tests whether the property is an extension model property or not.
	 *
	 * @param propName name of the property to check
	 * @return true if the property is extension model property, otherwise false
	 */

	public boolean isExtensionModelProperty(String propName) {
		if (reportItem != null) {
			IPropertyDefinition[] extProps = reportItem.getPropertyDefinitions();
			if (extProps != null) {
				for (int i = 0; i < extProps.length; i++) {
					IPropertyDefinition extProp = extProps[i];
					assert extProp != null;

					if (propName.equals(extProp.getName())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Tests whether the property is the extension property which holds the
	 * serialized XML value for extension model properties. The property type should
	 * be XML.
	 *
	 * @param propName name of the property to check
	 * @return true if the property is XML type and holds the serialized XML value
	 *         for extension model properties, otherwise false
	 */

	public boolean isExtensionXMLProperty(String propName) {
		ExtensionElementDefn extDefn = getExtDefn();
		if (extDefn != null) {
			ElementPropertyDefn propDefn = (ElementPropertyDefn) extDefn.getProperty(propName);
			if (propDefn != null && propDefn.hasOwnModel() && IPropertyType.XML_TYPE == propDefn.getTypeCode()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Copies the extension values and extension element instance, which implements
	 * <code>IReportItem</code>.
	 *
	 * @param source the source peer extensibility provider
	 * @param policy
	 */

	public void copyFromWithElementType(PeerExtensibilityProvider source, CopyPolicy policy) {
		Iterator<String> it = source.extensionPropValues.keySet().iterator();
		while (it.hasNext()) {
			String propName = it.next();
			PropertyDefn propDefn = element.getPropertyDefn(propName);
			if (!propDefn.isElementType()) {
				continue;
			}

			Object value = source.extensionPropValues.get(propName);
			if (value == null) {
				continue;
			}

			Object valueToSet = ModelUtil.copyValue(propDefn, value, policy);

			if (valueToSet == null) {
				continue;
			}
			extensionPropValues.put(propName, valueToSet);

			// if the property is element type, then set-up the container
			// relationship

			if (propDefn.isList()) {
				List values = (List) valueToSet;
				for (int i = 0; i < values.size(); i++) {
					DesignElement item = (DesignElement) values.get(i);
					item.setContainer(element, propName);
				}
			} else {
				((DesignElement) valueToSet).setContainer(element, propName);
			}

		}
	}

	/**
	 * Copies the extension values and extension element instance, which implements
	 * <code>IReportItem</code>.
	 *
	 * @param source the source peer extensibility provider
	 */

	public void copyFromWithNonElementType(PeerExtensibilityProvider source) {
		// if the extended element is not null, just copy it

		reportItem = null;
		if (source.reportItem != null) {
			reportItem = source.reportItem.copy();
		}

		// copy encryption map

		if (source.encryptionMap != null && !source.encryptionMap.isEmpty()) {
			if (encryptionMap == null) {
				encryptionMap = new HashMap<>();
			}
			encryptionMap.putAll(source.encryptionMap);
		}

		// extension Properties has been reallocated as a new hash map. There is
		// no need to new again.

		if (extensionPropValues == null) {
			extensionPropValues = new HashMap<>();
		}

		Iterator<String> it = source.extensionPropValues.keySet().iterator();
		while (it.hasNext()) {
			String propName = it.next();
			ElementPropertyDefn propDefn = element.getPropertyDefn(propName);
			if (propDefn.isElementType()) {
				continue;
			}

			Object value = source.extensionPropValues.get(propName);
			if (value == null) {
				continue;
			}

			Object valueToSet = ModelUtil.copyValue(propDefn, value);

			if (valueToSet == null) {
				continue;
			}

			if (propDefn.getTypeCode() == IPropertyType.STRUCT_TYPE) {
				StructureContextUtil.setStructureContext(propDefn, valueToSet, element);
			}
			extensionPropValues.put(propName, valueToSet);
		}
	}

	/**
	 * Return the extension element, which implements the interface
	 * <code>IReportItem</code>.
	 *
	 * @return the extension element
	 */

	public IReportItem getExtensionElement() {
		return reportItem;
	}

	/**
	 * Gets the script definition of this extended element.
	 *
	 * @return the script definition
	 */

	public IPropertyDefinition getScriptPropertyDefinition() {
		if (reportItem != null) {
			return reportItem.getScriptPropertyDefinition();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.extension.ExtensibilityProvider#
	 * hasLocalPropertyValues()
	 */

	@Override
	public boolean hasLocalPropertyValues() {
		if (hasLocalPropertyValuesOnOwnModel()) {
			return true;
		} else if (!extensionPropValues.isEmpty()) {
			Set<String> propNames = extensionPropValues.keySet();
			// ignore the element type property values, for it is layout
			// related changes
			for (String propName : propNames) {
				PropertyDefn defn = (PropertyDefn) getPropertyDefn(propName);
				assert defn != null;
				if (defn.getTypeCode() != IPropertyType.ELEMENT_TYPE) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Clears the extended item of this ExtendedItem.
	 */

	public void clearOwnModel() {
		if (reportItem != null) {
			reportItem = null;
		}
	}

	/**
	 * Returns if this extended item has local property values on own model.
	 *
	 * @return <code>true</code> if this extended item has local property values on
	 *         own model, <code>false</code> otherwise.
	 */

	public boolean hasLocalPropertyValuesOnOwnModel() {
		List<IElementPropertyDefn> localPropDefns = getExtDefn().getLocalProperties();
		for (int i = 0; i < localPropDefns.size(); i++) {
			ElementPropertyDefn propDefn = (ElementPropertyDefn) localPropDefns.get(i);

			if (propDefn.getTypeCode() != IPropertyType.XML_TYPE || !propDefn.canInherit() || !propDefn.hasOwnModel()) {
				continue;
			}

			String propName = propDefn.getName();
			Object childValue = extensionPropValues.get(propName);
			if (childValue != null) {
				try {
					initializeReportItem(element.getRoot());
				} catch (ExtendedElementException e) {
					// do nothing
				}
			} else if (reportItem == null) {
				continue;
			}

			childValue = reportItem == null ? null : reportItem.serialize(propName);

			if (childValue == null) {
				continue;
			}

			Object parentValue = null;
			ExtendedItem parent = (ExtendedItem) ModelUtil.getParent(element);
			while (parent != null) {
				PeerExtensibilityProvider parentProvider = parent.getExtensibilityProvider();
				parentValue = parentProvider.extensionPropValues.get(propName);
				if (parentValue != null) {
					try {
						parent.initializeReportItem(parent.getRoot());
					} catch (ExtendedElementException e) {
						// do nothing
					}
				} else if (parentProvider.reportItem == null) {
					parent = (ExtendedItem) ModelUtil.getParent(parent);
					continue;
				}

				parentValue = parentProvider.reportItem == null ? null : parentProvider.reportItem.serialize(propName);

				if (parentValue != null) {
					break;
				}

				parent = (ExtendedItem) ModelUtil.getParent(parent);
			}

			// compare these two value

			if (parentValue == null) {
				return true;
			}

			if (childValue.toString().equals(parentValue.toString())) {
				continue;
			}

			return true;

		}

		return false;
	}

	/**
	 * Gets the default encryption helper for the extension property.
	 *
	 * @param propDefn
	 * @return encryption id for the given property definition
	 */
	public final String getEncryptionHelperID(ElementPropertyDefn propDefn) {
		if (propDefn == null || !propDefn.isEncryptable()) {
			return null;
		}
		if (encryptionMap != null && encryptionMap.get(propDefn.getName()) != null) {
			String encryptionID = encryptionMap.get(propDefn.getName());
			return encryptionID;
		}
		return null;
	}

	/**
	 * Sets the encryption id for the given property.
	 *
	 * @param propDefn
	 * @param encryptionID
	 */
	public void setEncryptionHelper(ElementPropertyDefn propDefn, String encryptionID) {
		String id = StringUtil.trimString(encryptionID);
		if (encryptionMap == null) {
			encryptionMap = new HashMap<>();
		}
		if (id == null) {
			encryptionMap.remove(propDefn.getName());
		} else {
			encryptionMap.put(propDefn.getName(), id);
		}
	}

	/**
	 * Handles invalid property value. The property definition is valid and
	 * extensible while the value is invalid.
	 *
	 * @param propName
	 * @param value
	 */
	abstract public void handleInvalidPropertyValue(String propName, Object value);

	/**
	 * Handles undefined property. The property definition is not found.
	 *
	 * @param propName
	 * @param value
	 */
	abstract public void handleUndefinedProperty(String propName, Object value);

	/**
	 * Handles undefined children. The child is not allowed to be inserted to the
	 * container.
	 *
	 * @param propName
	 * @param child
	 */
	abstract public void handleIllegalChildren(String propName, DesignElement child);

	/**
	 * Returns the map for properties that has invalid values.
	 *
	 * @return the map of all invalid property value
	 */
	abstract public Map<String, UndefinedPropertyInfo> getInvalidPropertyValueMap();

	/**
	 *
	 * @return the map of all undefined property
	 */

	abstract public Map<String, UndefinedPropertyInfo> getUndefinedPropertyMap();

	/**
	 *
	 * @return the map of all illegal children content
	 */
	abstract public Map<String, List<UndefinedChildInfo>> getIllegalContents();

	/**
	 * Determines whether this children needs to do parser compatibility.
	 *
	 * @return true if need check this extension, otherwise false
	 */
	public boolean needCheckCompatibility() {
		// if ( getInvalidPropertyValueMap( ) != null &&
		// !getInvalidPropertyValueMap( ).isEmpty( ) )
		// return true;
		// if ( getUndefinedPropertyMap( ) != null &&
		// !getUndefinedPropertyMap( ).isEmpty( ) )
		// return true;
		// if ( getIllegalContents( ) != null && !getIllegalContents( ).isEmpty(
		// ) )
		// return true;
		// return false;
		return true;
	}
}
