/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.chart.extension.aggregate;

import org.eclipse.birt.chart.aggregate.AggregateFunctionAdapter;
import org.eclipse.birt.chart.engine.extension.i18n.Messages;
import org.eclipse.birt.core.data.DataType;

/**
 * 
 *
 */
public class Top extends AggregateFunctionAdapter {
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.aggregate.IAggregateFunction#getDisplayParameters()
	 */
	public String[] getDisplayParameters() {
		return new String[] { Messages.getString("Top.AggregateFunction.Parameters.Label.N") }; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.aggregate.IAggregateFunction#getParametersCount()
	 */
	public int getParametersCount() {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.aggregate.AggregateFunctionAdapter#getType()
	 */
	public int getType() {
		return RUNNING_AGGR;
	}

	@Override
	public int getBIRTDataType() {
		return DataType.BOOLEAN_TYPE;
	}
}
