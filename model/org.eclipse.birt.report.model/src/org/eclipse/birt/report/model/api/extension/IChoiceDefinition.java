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

package org.eclipse.birt.report.model.api.extension;

/**
 * Defines a choice within a list of property choices defined by a peer
 * extension.
 */

public interface IChoiceDefinition {

	/**
	 * Returns the resource key for localized display name of this choice.
	 * 
	 * @return the resource key for localized display name of this choice.
	 */

	String getDisplayNameID();

	/**
	 * Returns the non-localized, internal version of the choice.
	 * 
	 * @return the non-localized internal choice value
	 */

	String getName();

	/**
	 * Returns the value to which this choice corresponds. For example, a color may
	 * have a name and a value that is an RGB value. The value is optional.
	 * 
	 * @return the value that this choice represents
	 */

	Object getValue();
}