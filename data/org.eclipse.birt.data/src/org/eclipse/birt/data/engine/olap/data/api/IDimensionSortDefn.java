
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.data.api;

/**
 *
 */

public interface IDimensionSortDefn {
	/**
	 * Sorts in ascending order of sort key values
	 */
	int SORT_UNDEFINED = -1;

	/**
	 * Sorts in ascending order of sort key values
	 */
	int SORT_ASC = 0;

	/**
	 * Sorts in descending order of sort key values
	 */
	int SORT_DESC = 1;

	/**
	 * refactor to getSortLevel
	 *
	 * @return
	 */
	ILevel getLevel();

	/**
	 *
	 * @return
	 */
	int getDirection();
}
