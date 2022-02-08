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

package org.eclipse.birt.report.model.core;

import org.eclipse.birt.report.model.api.metadata.IObjectDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * Interface that provides a common generic getter/setter mechanism common to
 * elements & structures.
 * 
 */

public interface IPropertySet extends Cloneable {

	/**
	 * Gets the value of a property. An assertion occurs if the member name is not
	 * valid.
	 * 
	 * @param module the module
	 * 
	 * @param prop   definition of the property to get
	 * @return value of the item as an object, or null if the item is not set or is
	 *         not found.
	 */

	Object getProperty(Module module, PropertyDefn prop);

	/**
	 * Sets the value of a property. An assertion occurs if the member name is not
	 * valid.
	 * 
	 * @param prop  definition the property to set
	 * @param value the value to set
	 */

	void setProperty(PropertyDefn prop, Object value);

	/**
	 * Returns the definition of this object. The object definition provides access
	 * to the list of properties.
	 * 
	 * @return the object definition
	 */

	IObjectDefn getObjectDefn();
}
