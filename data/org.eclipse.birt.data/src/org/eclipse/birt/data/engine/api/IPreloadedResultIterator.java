/*******************************************************************************
 * Copyright (c) 2018 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.api;

/**
 * This iterator only loads predefined number of rows from starting row index.
 * Max row numbers and starting row index must be set immediately after new
 * instance or before accessing data. This extends
 * <class>IResultIterator</class> and provides better performance.
 * 
 * @since 4.8
 */

public interface IPreloadedResultIterator extends IResultIterator {

	/**
	 * Sets max number of rows.
	 * 
	 * @param rowNum max number of rows.
	 * @since 4.8
	 */
	void setMaxRows(int rowNum);

	/**
	 * Sets starting row index.
	 * 
	 * @param startIndex starting row index
	 * @since 4.8
	 */
	void setStartingRow(int startIndex);
}
