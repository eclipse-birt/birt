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

package org.eclipse.birt.report.designer.data.ui.aggregation;

/**
 * Represents the definition of argument. The argument definition includes the
 * data type, internal name, and display name.
 */

public class ArgumentInfo extends org.eclipse.birt.report.model.api.metadata.ArgumentInfo {

	/**
	 * Sets the internal name of the property.
	 * 
	 * @param theName the internal property name
	 */

	protected void setName(String theName) {
		super.setName(theName);
	}

	/**
	 * Sets the message ID for the display name.
	 * 
	 * @param id message ID for the display name
	 */

	protected void setDisplayNameKey(String id) {
		super.setDisplayNameKey(id);
	}

	/**
	 * Sets the script type of this argument.
	 * 
	 * @param type the script type to set
	 */

	protected void setType(String type) {
		super.setType(type);
	}

}