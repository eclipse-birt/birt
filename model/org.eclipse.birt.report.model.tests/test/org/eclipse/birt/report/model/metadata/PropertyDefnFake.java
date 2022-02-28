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
	@Override
	public int getValueType() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#setTrimOption(int)
	 */
	@Override
	public void setTrimOption(int trimOption) {
		super.setTrimOption(trimOption);
	}

}
