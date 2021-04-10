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
import org.eclipse.birt.report.model.api.MemberValueHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.core.Module;

/**
 * 
 */
public class MemberValue extends ContentElement {

	/**
	 * Default constructor.
	 */

	public MemberValue() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.
	 * report.model.elements.ElementVisitor)
	 */

	public void apply(ElementVisitor visitor) {
		visitor.visitMemberValue(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName() {
		return ReportDesignConstants.MEMBER_VALUE_ELEMENT;
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param module the report design
	 * @return an API handle for this element
	 */

	public MemberValueHandle handle(Module module) {
		return new MemberValueHandle(module, this);
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
