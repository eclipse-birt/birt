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
