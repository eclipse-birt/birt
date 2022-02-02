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

import java.util.ArrayList;

import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.type.LineSeries;

/**
 * StackGroup
 */
public class StackGroup {

	ArrayList<Series> alSeries = new ArrayList<Series>();

	ArrayList<AxisSubUnit> alUnitPositions = null;

	final int iSharedUnitIndex;

	int iSharedUnitCount = 1;

	/** Return positive and negative values should be accumulated together or not */
	boolean bStackTogether = false;

	/**
	 * The constructor.
	 */
	StackGroup(int iSharedUnitIndex) {
		this.iSharedUnitIndex = iSharedUnitIndex;
	}

	/**
	 * 
	 * @param iSharedUnitCount
	 */
	final void updateCount(int iSharedUnitCount) {
		this.iSharedUnitCount = iSharedUnitCount;
	}

	/**
	 * 
	 * @param se
	 */
	final void addSeries(Series se) {
		alSeries.add(se);

		// If having one series stack together, that's it.
		if (!bStackTogether) {
			bStackTogether = isStackTogether(se);
		}
	}

	/**
	 * 
	 * @return
	 */
	public final ArrayList<Series> getSeries() {
		return alSeries;
	}

	public final int getSharedIndex() {
		return iSharedUnitIndex;
	}

	public final int getSharedCount() {
		return iSharedUnitCount;
	}

	/**
	 * Returns current series is stacked together
	 * 
	 * @TODO need to add api in Series to return the result
	 * @param series
	 * @return
	 */
	private boolean isStackTogether(Series series) {
		return series instanceof LineSeries;
	}
}
