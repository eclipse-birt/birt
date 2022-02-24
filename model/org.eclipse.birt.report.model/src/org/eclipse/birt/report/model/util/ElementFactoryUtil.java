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

package org.eclipse.birt.report.model.util;

import java.lang.reflect.Constructor;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.IllegalOperationException;
import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.interfaces.IExtendedItemModel;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PeerExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.PeerExtensionLoader;

/**
 * Creates a new report elements and returns handles to it. Use this to create
 * elements. After creating an element, the element should not be renamed as
 * unique name.
 */

public class ElementFactoryUtil {

	/**
	 * Creates a design element specified by the element type name. Element type
	 * names are defined in rom.def or extension elements. They are managed by the
	 * meta-data system. The created element should not be renamed as unique name.
	 *
	 * @param module
	 *
	 * @param elementTypeName the element type name
	 * @param name            the optional element name
	 * @param reName          rename the element or not
	 *
	 * @return design element, <code>null</code> returned if the element definition
	 *         name is not a valid element type name.
	 */

	static public DesignElementHandle newElement(Module module, String elementTypeName, String name, boolean reName) {

		ElementDefn elemDefn = (ElementDefn) MetaDataDictionary.getInstance().getExtension(elementTypeName);

		// try extension first
		if (elemDefn != null) {
			DesignElementHandle extension = newExtensionElement(module, elementTypeName, name, reName);
			if (extension != null) {
				return extension;
			}
		}

		// try other system definitions
		elemDefn = (ElementDefn) MetaDataDictionary.getInstance().getElement(elementTypeName);
		if (elemDefn != null) {
			DesignElement element = newElementExceptExtensionElement(module, elementTypeName, name, reName);
			if (element == null) {
				return null;
			}
			return element.getHandle(module);
		}
		return null;
	}

	/**
	 * Creates a design element specified by the element type name. Element type
	 * names are defined in rom.def or extension elements. They are managed by the
	 * meta-data system.
	 *
	 * @param module          the module to create an element
	 * @param elementTypeName the element type name
	 * @param name            the optional element name
	 *
	 * @return design element, <code>null</code> returned if the element definition
	 *         name is not a valid element type name.
	 */

	public static DesignElement newElementExceptExtendedItem(Module module, String elementTypeName, String name) {
		return newElementExceptExtensionElement(module, elementTypeName, name, true);
	}

	/**
	 * Creates a design element specified by the element type name. Element type
	 * names are defined in rom.def or extension elements. They are managed by the
	 * meta-data system.
	 *
	 * @param module          the module to create an element
	 * @param elementTypeName the element type name
	 * @param name            the optional element name
	 *
	 * @return design element, <code>null</code> returned if the element definition
	 *         name is not a valid element type name.
	 */

	public static DesignElement newElement(String elementTypeName, String name) {

		ElementDefn elemDefn = (ElementDefn) MetaDataDictionary.getInstance().getElement(elementTypeName);

		String javaClass = elemDefn.getJavaClass();
		if (javaClass == null) {
			return null;
		}

		try {
			Class<? extends Object> c = Class.forName(javaClass);
			DesignElement element = null;

			try {
				Constructor<? extends Object> constructor = c.getConstructor(new Class[] { String.class });
				element = (DesignElement) constructor.newInstance(new String[] { name });
				return element;
			} catch (NoSuchMethodException e1) {
				element = (DesignElement) c.newInstance();
				element.setName(name);
				return element;
			}

		} catch (Exception e) {
			// Impossible.

			assert false;
		}

		return null;
	}

	/**
	 * Creates a design element specified by the element type name. Element type
	 * names are defined in rom.def or extension elements. They are managed by the
	 * meta-data system.
	 *
	 * @param module          the module to create an element
	 * @param elementTypeName the element type name
	 * @param name            the optional element name
	 * @param reName          renames the new element or not.
	 *
	 * @return design element, <code>null</code> returned if the element definition
	 *         name is not a valid element type name.
	 */

	public static DesignElement newElementExceptExtensionElement(Module module, String elementTypeName, String name,
			boolean reName) {
		DesignElement element = newElement(elementTypeName, name);
		if (element != null && module != null && reName) {
			module.makeUniqueName(element);
		}
		return element;
	}

	/**
	 * Creates an extension element specified by the extension type name.
	 *
	 * @param elementTypeName the element type name
	 * @param name            the optional element name
	 * @param reName          rename the element or not
	 *
	 * @return design element, <code>null</code> returned if the extension with the
	 *         given type name is not found
	 */

	static private DesignElementHandle newExtensionElement(Module module, String elementTypeName, String name,
			boolean reName) {
		MetaDataDictionary dd = MetaDataDictionary.getInstance();
		ExtensionElementDefn extDefn = (ExtensionElementDefn) dd.getExtension(elementTypeName);
		if (extDefn == null) {
			return null;
		}
		String extensionPoint = extDefn.getExtensionPoint();
		if (PeerExtensionLoader.EXTENSION_POINT.equalsIgnoreCase(extensionPoint)) {
			return newExtendedItem(module, name, elementTypeName, reName);
		}

		return null;
	}

	/**
	 * Creates a new extended item.
	 *
	 * @param name          the optional extended item name. Can be
	 *                      <code>null</code>.
	 * @param extensionName the required extension name
	 * @param reName        rename the element or not
	 *
	 * @return a handle to extended item, return <code>null</code> if the definition
	 *         with the given extension name is not found
	 */

	static private ExtendedItemHandle newExtendedItem(Module module, String name, String extensionName,
			boolean reName) {
		try {
			return newExtendedItem(module, name, extensionName, null, reName);
		} catch (ExtendsException e) {
			assert false;
			return null;
		}
	}

	/**
	 * Creates a new extended item which extends from a given parent.
	 *
	 * @param name          the optional extended item name. Can be
	 *                      <code>null</code>.
	 * @param extensionName the required extension name
	 * @param parent        a given parent element.
	 * @return a handle to extended item, return <code>null</code> if the definition
	 *         with the given extension name is not found
	 * @param reName rename the element or not
	 * @throws ExtendsException
	 */

	static private ExtendedItemHandle newExtendedItem(Module module, String name, String extensionName,
			ExtendedItemHandle parent, boolean reName) throws ExtendsException {
		MetaDataDictionary dd = MetaDataDictionary.getInstance();
		ExtensionElementDefn extDefn = (ExtensionElementDefn) dd.getExtension(extensionName);
		if (extDefn == null) {
			return null;
		}

		if (parent != null) {
			assert ((ExtendedItem) parent.getElement()).getExtDefn() == extDefn;
		}

		if (!(extDefn instanceof PeerExtensionElementDefn)) {
			throw new IllegalOperationException("Only report item extension can be created through this method."); //$NON-NLS-1$
		}

		ExtendedItem element = new ExtendedItem(name);

		// init provider.

		element.setProperty(IExtendedItemModel.EXTENSION_NAME_PROP, extensionName);

		if (parent != null) {
			element.getHandle(module).setExtends(parent);
		}

		if (reName) {
			module.makeUniqueName(element);
		}

		ExtendedItemHandle handle = element.handle(module);

		try {
			handle.loadExtendedElement();
		} catch (ExtendedElementException e) {
			// It's impossible to fail when deserializing.

			assert false;
		}
		return handle;
	}

}
