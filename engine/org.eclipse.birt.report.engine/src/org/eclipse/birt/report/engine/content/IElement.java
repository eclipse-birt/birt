/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.content;

import java.util.Collection;

/**
 * Provides the interface of the element of the report.
 *
 * All elements in the report must have the implementations of IElement or its
 * subinterface.
 */
public interface IElement {
	/**
	 * Get the parent of the element object, or return <code>null</code> if the
	 * element is in top level.
	 *
	 * @return the parent of the element.
	 */
	IElement getParent();

	/**
	 * Set the parent of the element.
	 *
	 * @param parent the parent of the element
	 */
	void setParent(IElement parent);

	/**
	 * Get the children of the element, or return <code>null</code> if the element
	 * is in leaf level.
	 *
	 * @return the children of the element.
	 */
	Collection getChildren();

}
