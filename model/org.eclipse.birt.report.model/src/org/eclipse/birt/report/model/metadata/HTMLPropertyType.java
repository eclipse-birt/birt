/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/

package org.eclipse.birt.report.model.metadata;

/**
 * HTML property type.
 *
 */

public class HTMLPropertyType extends TextualPropertyType {
	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.html"; //$NON-NLS-1$

	/**
	 * Constructor.
	 */

	public HTMLPropertyType() {
		super(DISPLAY_NAME_KEY);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getTypeCode()
	 */

	@Override
	public int getTypeCode() {
		return HTML_TYPE;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getXmlName()
	 */

	@Override
	public String getName() {
		return HTML_TYPE_NAME;
	}

}
