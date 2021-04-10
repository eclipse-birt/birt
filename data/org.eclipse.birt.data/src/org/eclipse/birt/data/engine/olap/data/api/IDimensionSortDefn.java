
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	public static final int SORT_UNDEFINED = -1;

	/**
	 * Sorts in ascending order of sort key values
	 */
	public static final int SORT_ASC = 0;

	/**
	 * Sorts in descending order of sort key values
	 */
	public static final int SORT_DESC = 1;

	/**
	 * refactor to getSortLevel
	 * 
	 * @return
	 */
	public ILevel getLevel();

	/**
	 * 
	 * @return
	 */
	public int getDirection();
}
