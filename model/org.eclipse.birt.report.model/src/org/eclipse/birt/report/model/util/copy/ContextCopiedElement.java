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

package org.eclipse.birt.report.model.util.copy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.elements.structures.PropertyBinding;
import org.eclipse.birt.report.model.api.util.IElementCopy;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.strategy.DummyCopyPolicy;

/**
 * 
 */

class ContextCopiedElement implements IElementCopy, Cloneable {

	private final DesignElement copy;
	private final DesignElement localizedCopy;

	private final String rootLocation;
	private final String xpath;
	private final String libLocation;
	private final long extendsElementID;
	private final List<PropertyBinding> bindings;

	/**
	 * Default constructor.
	 * 
	 * @param element          the element.
	 * @param localizedElement the localized element
	 * @param xpath            the xpath of the element
	 * @param rootLocation     the location of the corresponding module
	 * @param libLocation      the location of the corresponding library
	 * @param libNameSpace     the name space of the library
	 * @param extendsElementID the element id of the library
	 * @param bindings         the list of property bindings of the element
	 */
	ContextCopiedElement(DesignElement element, DesignElement localizedElement, String xpath, String rootLocation,
			String libLocation, long extendsElementID, List<PropertyBinding> bindings) {
		this.copy = element;
		this.localizedCopy = localizedElement;
		this.rootLocation = rootLocation;
		this.xpath = xpath;
		this.libLocation = libLocation;
		this.extendsElementID = extendsElementID;
		if (bindings == null || bindings.isEmpty())
			this.bindings = Collections.emptyList();
		else {
			this.bindings = new ArrayList<PropertyBinding>(bindings.size());
			for (PropertyBinding propBinding : bindings) {
				this.bindings.add((PropertyBinding) propBinding.copy());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */

	public Object clone() throws CloneNotSupportedException {
		DesignElement newCopy = null;

		if (copy != null)
			newCopy = (DesignElement) copy.doClone(DummyCopyPolicy.getInstance());

		DesignElement newLocalized = (DesignElement) localizedCopy.doClone(DummyCopyPolicy.getInstance());

		ContextCopiedElement retValue = new ContextCopiedElement(newCopy, newLocalized, xpath, rootLocation,
				libLocation, extendsElementID, bindings);

		return retValue;
	}

	/**
	 * Returns the copied element.
	 * 
	 * @return the copied
	 */

	DesignElement getCopy() {
		if (extendsElementID != DesignElement.NO_ID)
			return copy;

		return localizedCopy;
	}

	/**
	 * Returns the location of the corresponding module.
	 * 
	 * @return the rootLocation
	 */

	String getRootLocation() {
		return rootLocation;
	}

	/**
	 * Returns the localized element of which the extends value is null.
	 * 
	 * @return the localized element
	 */

	DesignElement getLocalizedCopy() {
		return localizedCopy;
	}

	/**
	 * Returns the unlocalized copy.
	 * 
	 * @return the element which is not localized
	 */

	DesignElement getUnlocalizedCopy() {
		return copy;
	}

	/**
	 * Returns the location of library.
	 * 
	 * @return the location of library
	 */
	String getLibLocation() {
		return libLocation;
	}

	/**
	 * Gets the element id in the library.
	 * 
	 * @return the element id in the library.
	 */
	long getExtendsElementID() {
		return extendsElementID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.util.IElementCopy#getHandle(org.eclipse
	 * .birt.report.model.api.ModuleHandle)
	 */

	public DesignElementHandle getHandle(ModuleHandle handle) {
		return getCopy().getHandle(handle.getModule());
	}

	/**
	 * Gets the list of property bindings of the element
	 * 
	 * @return the list of property bindings.
	 */
	List<PropertyBinding> getPropertyBindings() {
		return bindings;
	}

	/**
	 * Gets the xpath of the copied element.
	 * 
	 * @return the xpath
	 */
	String getXPath() {
		return xpath;
	}
}
