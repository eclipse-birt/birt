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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.elements.structures.SearchKey;

/**
 * Represents the handle of key used for search. The search key is the search
 * criteria which is used when we defined drill-through action in some report
 * item, such as label item, image item and so on. It's provided for
 * drill-through contents. Each search key has the following properties:
 * 
 * <p>
 * <dl>
 * <dt><strong>Expression </strong></dt>
 * <dd>an expression of the search key for the drill-through.</dd>
 * </dl>
 * <p>
 * 
 * @see ActionHandle
 */

public class SearchKeyHandle extends StructureHandle {

	/**
	 * Constructs the handle of search key.
	 * 
	 * @param valueHandle the value handle for search key list of one property
	 * @param index       the position of this search key in the list
	 */

	public SearchKeyHandle(SimpleValueHandle valueHandle, int index) {
		super(valueHandle, index);
	}

	/**
	 * Returns the expression of this search key.
	 * 
	 * @return the expression of this search key.
	 */

	public String getExpression() {
		return getStringProperty(SearchKey.EXPRESSION_MEMBER);
	}

	/**
	 * Sets the expression of this search key.
	 * 
	 * @param expression the expression to set
	 */

	public void setExpression(String expression) {
		setPropertySilently(SearchKey.EXPRESSION_MEMBER, expression);
	}
}
