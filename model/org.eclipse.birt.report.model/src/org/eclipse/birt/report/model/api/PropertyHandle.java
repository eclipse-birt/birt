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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.util.PropertyHandleHelper;

/**
 * A handle for working with a top-level property of an element.
 * 
 * @see org.eclipse.birt.report.model.metadata.PropertyDefn
 * @see org.eclipse.birt.report.model.metadata.PropertyType
 */

public class PropertyHandle extends PropertyHandleImpl {
	/**
	 * Constructs the handle for a top-level property with the given element handle
	 * and property name.
	 * 
	 * @param element  a handle to a report element
	 * @param propName the name of the property
	 */
	public PropertyHandle(DesignElementHandle element, String propName) {
		super(element, propName);
	}

	/**
	 * Constructs the handle for a top-level property with the given element handle
	 * and the definition of the property.
	 * 
	 * @param element a handle to a report element
	 * @param prop    the definition of the property.
	 */

	public PropertyHandle(DesignElementHandle element, ElementPropertyDefn prop) {
		super(element, prop);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#isReadOnly()
	 */

	public boolean isReadOnly() {
		boolean isReadOnly = false;

		Module root = getElementHandle().getModule();
		assert root != null;
		if (root.isReadOnly())
			isReadOnly = true;
		else {
			switch (propDefn.getValueType()) {
			case IPropertyDefn.SYSTEM_PROPERTY:
			case IPropertyDefn.EXTENSION_PROPERTY:
			case IPropertyDefn.ODA_PROPERTY:
				IElementDefn elementDefn = getElementHandle().getDefn();
				if (elementDefn.isPropertyReadOnly(propDefn.getName()))
					isReadOnly = true;
				break;
			case IPropertyDefn.EXTENSION_MODEL_PROPERTY:
				if (propDefn.isReadOnly())
					isReadOnly = true;
				break;
			}
		}

		if (isReadOnly)
			return true;

		return PropertyHandleHelper.getInstance().isReadOnlyInContext(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#isVisible()
	 */

	public boolean isVisible() {
		boolean isVisible = true;
		switch (propDefn.getValueType()) {
		case IPropertyDefn.SYSTEM_PROPERTY:
		case IPropertyDefn.EXTENSION_PROPERTY:
		case IPropertyDefn.ODA_PROPERTY:
			IElementDefn elementDefn = getElementHandle().getDefn();
			if (!elementDefn.isPropertyVisible(propDefn.getName()))
				isVisible = false;
			break;
		case IPropertyDefn.EXTENSION_MODEL_PROPERTY:
			if (!propDefn.isVisible())
				isVisible = false;
			break;
		case IPropertyDefn.USER_PROPERTY:
			if (!propDefn.isVisible())
				isVisible = false;
			break;
		}

		if (!isVisible)
			return false;

		return PropertyHandleHelper.getInstance().isVisibleInContext(this);
	}
}