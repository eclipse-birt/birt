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
import org.eclipse.birt.report.model.api.TemplateDataSetHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.core.Module;

/**
 * Represents template data sets. A template data set is a place holder to
 * generate a real data set element.
 * 
 * @see org.eclipse.birt.report.model.elements.TemplateElement
 */

public class TemplateDataSet extends TemplateElement {
	/**
	 * Default constructor.
	 */

	public TemplateDataSet() {
	}

	/**
	 * Constructs the template data set with a required name.
	 * 
	 * @param theName the name
	 */

	public TemplateDataSet(String theName) {
		super(theName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.
	 * report.model.elements.ElementVisitor)
	 */

	public void apply(ElementVisitor visitor) {
		visitor.visitTemplateDataSet(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName() {
		return ReportDesignConstants.TEMPLATE_DATA_SET;
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

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param module the report design of the template data set
	 * 
	 * @return an API handle for this element
	 */

	public TemplateDataSetHandle handle(Module module) {
		if (handle == null) {
			handle = new TemplateDataSetHandle(module, this);
		}
		return (TemplateDataSetHandle) handle;
	}

}
