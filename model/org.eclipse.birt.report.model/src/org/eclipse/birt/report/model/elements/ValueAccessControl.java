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

import org.eclipse.birt.report.model.api.AccessControlHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ValueAccessControlHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.core.Module;

/**
 * Represents a autotext report item. A autotext item supports page number and
 * total page . The autotext has the following properties:
 * 
 * <ul>
 * <li>An autotext choice type counts the page number or total page number
 * </ul>
 * 
 * @deprecated since BIRT 2.5.1
 */

public class ValueAccessControl extends AccessControl {

	/**
	 * Default constructor.
	 */

	public ValueAccessControl() {

	}

	/**
	 * Constructs the autotext item with an optional name.
	 * 
	 * @param theName the optional name
	 */

	public ValueAccessControl(String theName) {
		super(theName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.
	 * report.model.elements.ElementVisitor)
	 */

	public void apply(ElementVisitor visitor) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName() {
		return ReportDesignConstants.VALUE_ACCESS_CONTROL;
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param module the report design
	 * @return an API handle for this element
	 */

	public AccessControlHandle handle(Module module) {
		return new ValueAccessControlHandle(module, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.core.IDesignElement#getHandle(org.eclipse.
	 * birt.report.model.core.Module)
	 */
	public DesignElementHandle getHandle(Module module) {
		return handle(module);
	}
}
