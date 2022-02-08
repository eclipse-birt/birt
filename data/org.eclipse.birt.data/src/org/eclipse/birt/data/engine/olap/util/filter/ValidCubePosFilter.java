
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

/**
 * 
 */

public class ValidCubePosFilter extends CubePosFilter {
	/**
	 * 
	 * @param dimensionNames
	 */
	public ValidCubePosFilter(String[] dimensionNames) {
		super(dimensionNames);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.util.filter.CubePosFilter#getFilterResult(
	 * int[])
	 */
	public boolean getFilterResult(int[] dimPositions) {
		for (int i = 0; i < cubePosRangeFilter.size(); i++) {
			if (((CubePositionRangeFilter) cubePosRangeFilter.get(i)).match(dimPositions)) {
				return true;
			}
		}
		return false;
	}

}
