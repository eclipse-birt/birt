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

package org.eclipse.birt.report.item.crosstab.core.re.executor;

import javax.olap.OLAPException;

/**
 * IColumnWalker
 */
interface IColumnWalker {

	boolean IGNORE_TOTAL_COLUMN_WITHOUT_AGGREGATION = true;

	boolean IGNORE_TOTAL_COLUMN_WITHOUT_MEASURE = true;

	/**
	 * Returns if has next column for this walker
	 */
	boolean hasNext() throws OLAPException;

	/**
	 * Steps to next column and returns the corresponding column event
	 */
	ColumnEvent next() throws OLAPException;

	/**
	 * Puts the walker to initial state
	 */
	void reload();
}
