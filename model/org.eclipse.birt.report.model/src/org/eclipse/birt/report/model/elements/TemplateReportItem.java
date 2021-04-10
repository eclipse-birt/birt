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
import org.eclipse.birt.report.model.api.TemplateReportItemHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.core.Module;

/**
 * Represents template report items. A template report item is a place holder to
 * generate a real report item.
 * 
 * @see org.eclipse.birt.report.model.elements.TemplateElement
 */

public class TemplateReportItem extends TemplateElement {
	/**
	 * Name of the visibility property. Define hide rules for an template item.
	 */

	public static final String VISIBILITY_PROP = "visibility"; //$NON-NLS-1$

	/**
	 * Default constructor.
	 */

	public TemplateReportItem() {
	}

	/**
	 * Constructs the template report item with an optional name.
	 * 
	 * @param theName the name
	 */

	public TemplateReportItem(String theName) {
		super(theName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.
	 * report.model.elements.ElementVisitor)
	 */

	public void apply(ElementVisitor visitor) {
		visitor.visitTemplateReportItem(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName() {
		return ReportDesignConstants.TEMPLATE_REPORT_ITEM;
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
	 * @param module the report design of the template report item
	 * 
	 * @return an API handle for this element
	 */

	public TemplateReportItemHandle handle(Module module) {
		if (handle == null) {
			handle = new TemplateReportItemHandle(module, this);
		}
		return (TemplateReportItemHandle) handle;
	}

}
