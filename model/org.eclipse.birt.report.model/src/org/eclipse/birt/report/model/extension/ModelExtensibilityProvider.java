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

package org.eclipse.birt.report.model.extension;

import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.command.WrongTypeException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.interfaces.IExtendedItemModel;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * Abstract extensibility provider for Model extension point.
 */

public abstract class ModelExtensibilityProvider extends ExtensibilityProvider {

	/**
	 * The name of the extension which is used to extend the extendable element.
	 */

	String extensionName = null;

	/**
	 * Constructs this provider with the element to extend, and extension name.
	 * 
	 * @param element       the element to extend
	 * @param extensionName the extension name
	 */

	public ModelExtensibilityProvider(DesignElement element, String extensionName) {
		super(element);

		this.extensionName = extensionName;

		cachedExtDefn = (ExtensionElementDefn) MetaDataDictionary.getInstance().getElement(extensionName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IExtendableElement#getExtDefn()
	 */

	public final ExtensionElementDefn getExtDefn() {
		if (extensionName == null)
			return null;

		return cachedExtDefn;
	}

	/**
	 * Checks whether the extendable element this provider supports can extends from
	 * the given parent element.
	 * 
	 * @param parent the parent element to check
	 * @throws ExtendsException if the extendable element this provide supports can
	 *                          not extends from the given parent element.
	 */

	public void checkExtends(DesignElement parent) throws ExtendsException {
		String parentExt = (String) parent.getProperty(null, IExtendedItemModel.EXTENSION_NAME_PROP);

		assert extensionName != null;
		if (!extensionName.equalsIgnoreCase(parentExt))
			throw new WrongTypeException(element, parent, WrongTypeException.DESIGN_EXCEPTION_WRONG_EXTENSION_TYPE);
	}
}
