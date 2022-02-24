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

package org.eclipse.birt.report.model.api.extension;

import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;

/**
 * Interface to traverse all the style property values.
 */
public interface IStyleDeclaration extends IStyleModel {

	/**
	 * Gets the value of the specified style property.
	 * 
	 * @param name name of the style property, it should be one that defined in
	 *             <code>IStyleModel</code>
	 * @return the property value.
	 */
	public Object getProperty(String name);

	/**
	 * Gets the name which this style is defined for. The default stype is defined
	 * for a certain extension element. For instance, if the style is defined for
	 * MyExtensionElement, then the name returned is "MyExtensionElement".
	 * 
	 * @return the name of this default style.
	 */
	public String getName();
}
