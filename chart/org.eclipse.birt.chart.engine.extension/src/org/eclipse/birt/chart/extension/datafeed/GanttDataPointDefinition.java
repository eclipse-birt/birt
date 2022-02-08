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

public class GanttDataPointDefinition extends AbstractDataPointDefinition {

	public final static String TYPE_START_DATE = "gantt_start_date"; //$NON-NLS-1$

	public final static String TYPE_END_DATE = "gantt_end_date"; //$NON-NLS-1$

	public final static String TYPE_DECORATION_LABEL = "gantt_decoration_label"; //$NON-NLS-1$

	private final String[] saTypeNames = { TYPE_START_DATE, TYPE_END_DATE, TYPE_DECORATION_LABEL };

	private final int[] iaTypeCompatibles = { IConstants.DATE_TIME, IConstants.DATE_TIME, IConstants.TEXT };

	public String[] getDataPointTypes() {
		return saTypeNames;
	}

	public String getDisplayText(String type) {
		if (TYPE_START_DATE.equals(type)) {
			return Messages.getString("info.datapoint.GanttStartDate"); //$NON-NLS-1$
		} else if (TYPE_END_DATE.equals(type)) {
			return Messages.getString("info.datapoint.GanttEndDate"); //$NON-NLS-1$
		} else if (TYPE_DECORATION_LABEL.equals(type)) {
			return Messages.getString("info.datapoint.GanttDecorationLabel"); //$NON-NLS-1$
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.datafeed.AbstractDataPointDefinition#
	 * isUnknownComponentType(java.lang.String)
	 */
	// public boolean isAnyDataType( String type )
	// {
	// if ( TYPE_DECORATION_LABEL.equals( type ) )
	// {
	// // decoration label should be as series value type for any available types.
	// return true;
	// }
	//
	// // Default value type.
	// return false;
	// }
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
