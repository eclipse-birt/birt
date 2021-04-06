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

package org.eclipse.birt.report.model.metadata;

/**
 * PropertyDefn fake class.
 */

class PropertyDefnFake extends PropertyDefn {

	PropertyDefnFake() {
		super();
		setDisplayNameID("Element.ReportDesign.author"); //$NON-NLS-1$

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.design.core.metadata.ValueDefn#getValueType ()
	 */
	public int getValueType() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#setTrimOption(int)
	 */
	public void setTrimOption(int trimOption) {
		super.setTrimOption(trimOption);
	}

}