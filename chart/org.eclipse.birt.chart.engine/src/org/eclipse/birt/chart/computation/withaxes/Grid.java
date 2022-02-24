/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.computation.withaxes;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.model.attribute.LineAttributes;

/**
 * Grid
 */
public final class Grid {

	int iMajorTickStyle = 0;

	int iMinorTickStyle = 0;

	int iMinorUnitsPerMajorUnit = 0;

	LineAttributes laMajorGrid;

	LineAttributes laMajorTicks;

	LineAttributes laMinorGrid;

	LineAttributes laMinorTicks;

	public int getTickStyle(int iMajorOrMinor) {
		if (iMajorOrMinor == IConstants.MAJOR) {
			return iMajorTickStyle;
		} else if (iMajorOrMinor == IConstants.MINOR) {
			return iMinorTickStyle;
		}
		return IConstants.TICK_NONE;
	}

	public LineAttributes getLineAttributes(int iMajorOrMinor) {
		if (iMajorOrMinor == IConstants.MAJOR) {
			return laMajorGrid;
		} else if (iMajorOrMinor == IConstants.MINOR) {
			return laMinorGrid;
		}
		return null;
	}

	public int getMinorCountPerMajor() {
		return iMinorUnitsPerMajorUnit;
	}

	/**
	 * @param major
	 * @return
	 */
	public LineAttributes getTickAttributes(int iMajorOrMinor) {
		if (iMajorOrMinor == IConstants.MAJOR) {
			return laMajorTicks;
		} else if (iMajorOrMinor == IConstants.MINOR) {
			return laMinorTicks;
		}
		return null;
	}
}
