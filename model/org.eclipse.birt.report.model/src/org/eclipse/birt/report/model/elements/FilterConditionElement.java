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

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.FilterConditionElementHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.core.Module;

/**
 *
 */
public class FilterConditionElement extends ContentElement {

	/**
	 * Default constructor.
	 */

	public FilterConditionElement() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.
	 * report.model.elements.ElementVisitor)
	 */

	public void apply(ElementVisitor visitor) {
		visitor.visitFilterConditionElement(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName() {
		return ReportDesignConstants.FILTER_CONDITION_ELEMENT;
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param module the report design
	 * @return an API handle for this element
	 */

	public FilterConditionElementHandle handle(Module module) {
		return new FilterConditionElementHandle(module, this);
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
