/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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

package org.eclipse.birt.chart.extension.datafeed;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.datafeed.AbstractDataPointDefinition;
import org.eclipse.birt.chart.engine.extension.i18n.Messages;

/**
 * 
 */

public class StockDataPointDefinition extends AbstractDataPointDefinition {

	public final static String TYPE_HIGH = "stock_high"; //$NON-NLS-1$

	public final static String TYPE_LOW = "stock_low"; //$NON-NLS-1$

	public final static String TYPE_OPEN = "stock_open"; //$NON-NLS-1$

	public final static String TYPE_CLOSE = "stock_close"; //$NON-NLS-1$

	private final String[] saTypeNames = { TYPE_HIGH, TYPE_LOW, TYPE_OPEN, TYPE_CLOSE };

	private final int[] iaTypeCompatibles = { IConstants.NUMERICAL, IConstants.NUMERICAL, IConstants.NUMERICAL,
			IConstants.NUMERICAL };

	public String[] getDataPointTypes() {
		return new String[] { TYPE_HIGH, TYPE_LOW, TYPE_OPEN, TYPE_CLOSE };
	}

	public String getDisplayText(String type) {
		if (TYPE_HIGH.equals(type)) {
			return Messages.getString("info.datapoint.High"); //$NON-NLS-1$
		} else if (TYPE_LOW.equals(type)) {
			return Messages.getString("info.datapoint.Low"); //$NON-NLS-1$
		} else if (TYPE_OPEN.equals(type)) {
			return Messages.getString("info.datapoint.Open"); //$NON-NLS-1$
		} else if (TYPE_CLOSE.equals(type)) {
			return Messages.getString("info.datapoint.Close"); //$NON-NLS-1$
		}
		return null;
	}

	public int getCompatibleDataType(String type) {
		for (int i = 0; i < saTypeNames.length; i++) {
			if (saTypeNames[i].equals(type)) {
				return this.iaTypeCompatibles[i];
			}
		}

		// no match, return the default value
		return 0;
	}
}
