
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
package org.eclipse.birt.data.engine.olap.util.filter;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;

/**
 * 
 */

public interface IJSTopBottomFilterHelper extends IJSFilterHelper {
	public static final int TOP_N = 1;
	public static final int BOTTOM_N = 2;
	public static final int TOP_PERCENT = 3;
	public static final int BOTTOM_PERCENT = 4;

	/**
	 * Return the filter type.
	 * 
	 * @return
	 */
	public int getFilterType();

	/**
	 * Evaluate the filter expression.
	 * 
	 * @param resultRow
	 * @return
	 * @throws DataException
	 */
	public Object evaluateFilterExpr(IResultRow resultRow) throws DataException;

	/**
	 * 
	 * @param resultRow
	 * @return
	 * @throws DataException
	 */
	public boolean isQualifiedRow(IResultRow resultRow) throws DataException;

	/**
	 * 
	 * @return
	 */
	public double getN();

	/**
	 * 
	 * @return
	 * @throws DataException
	 */
	public DimLevel getTargetLevel() throws DataException;

	/**
	 * return true if filterType is TOP_N or TOP_PERCENT; otherwise return false.
	 * 
	 * @return
	 */
	public boolean isTop();

	/**
	 * return true if filterType is TOP_PERCENT or BOTTOM_PERCENT; otherwise return
	 * false
	 * 
	 * @return
	 */
	public boolean isPercent();

}
