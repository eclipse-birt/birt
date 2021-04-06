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
 * Resource key property type. The resource key is a string that should match a
 * message in the customer's message catalog. The value can be null, which means
 * that the message is not localized. It may be that the message ID does not
 * match a valid message in the message file. This is handled during semantic
 * checks; the property type validation does not check this case. This allows
 * the developer to enter a message ID first, then add the message to the
 * catalog later. It also allows the developer to open a report even if the
 * message catalog is not available. If the message ID can't be found, the
 * application will use the corresponding non-localized property, if set.
 *
 */

public class ResourceKeyPropertyType extends TextualPropertyType {
	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.messageId"; //$NON-NLS-1$

	/**
	 * Constructor.
	 */

	public ResourceKeyPropertyType() {
		super(DISPLAY_NAME_KEY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getTypeCode()
	 */

	public int getTypeCode() {
		return RESOURCE_KEY_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getXmlName()
	 */

	public String getName() {
		return RESOURCE_KEY_TYPE_NAME;
	}

}
