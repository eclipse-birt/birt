/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.extension.aggregate;

import org.eclipse.birt.chart.aggregate.AggregateFunctionAdapter;
import org.eclipse.birt.chart.engine.extension.i18n.Messages;
import org.eclipse.birt.core.data.DataType;

/**
 * @since BIRT 2.3
 */
public class Rank extends AggregateFunctionAdapter {
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.aggregate.IAggregateFunction#getDisplayParameters()
	 */
	public String[] getDisplayParameters() {
		return new String[] { Messages.getString("Rank.AggregateFunction.Parameters.Label.Ascending") }; //$NON-NLS-1$
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
		return DataType.INTEGER_TYPE;
	}
}
