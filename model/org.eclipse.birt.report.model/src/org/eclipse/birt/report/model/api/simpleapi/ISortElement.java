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

package org.eclipse.birt.report.model.api.simpleapi;

import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * The simple API class for the SortElementHandle.
 */

public interface ISortElement extends IDesignElement {

	/**
	 * Returns the sort direction. The possible values are define in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are:
	 * <ul>
	 * <li>SORT_DIRECTION_ASC
	 * <li>SORT_DIRECTION_DESC
	 * </ul>
	 *
	 * @return the direction to sort
	 */

	String getDirection();

	/**
	 * Sets the sort direction. The allowed values are define in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are:
	 * <ul>
	 * <li>SORT_DIRECTION_ASC
	 * <li>SORT_DIRECTION_DESC
	 * </ul>
	 *
	 * @param direction the direction to set
	 * @throws SemanticException if the direction is not in choice list.
	 */

	void setDirection(String direction) throws SemanticException;

	/**
	 * Returns an expression that gives the sort key on which to sort. The simplest
	 * case is the name of a column. The expression can also be an expression that
	 * includes columns. When used for a group, the expression can contain an
	 * aggregate computed over the group.
	 *
	 * @return the key to sort
	 *
	 * @see #setKey(String)
	 */

	String getKey();

	/**
	 * Sets an expression that gives the sort key on which to sort.
	 *
	 * @param key the key to sort
	 * @throws SemanticException value required exception
	 * @see #getKey()
	 */

	void setKey(String key) throws SemanticException;
}
