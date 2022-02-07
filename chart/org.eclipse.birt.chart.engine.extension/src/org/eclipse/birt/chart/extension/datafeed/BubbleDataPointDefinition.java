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

public class BubbleDataPointDefinition extends AbstractDataPointDefinition {

	public final static String TYPE_VALUE = "bubble_value"; //$NON-NLS-1$

	public final static String TYPE_SIZE = "bubble_size"; //$NON-NLS-1$

	private final String[] saTypeNames = { TYPE_VALUE, TYPE_SIZE };

	private final int[] iaTypeCompatibles = { IConstants.NUMERICAL, IConstants.NUMERICAL };

	public String[] getDataPointTypes() {
		return saTypeNames;
	}

	public String getDisplayText(String type) {
		if (TYPE_VALUE.equals(type)) {
			return Messages.getString("info.datapoint.BubbleValue"); //$NON-NLS-1$
		} else if (TYPE_SIZE.equals(type)) {
			return Messages.getString("info.datapoint.BubbleSize"); //$NON-NLS-1$
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
