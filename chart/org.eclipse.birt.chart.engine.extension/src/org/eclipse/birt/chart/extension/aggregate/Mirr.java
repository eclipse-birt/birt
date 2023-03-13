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
 * @since BIRT 2.3
 *
 */
public class Mirr extends AggregateFunctionAdapter {
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.aggregate.IAggregateFunction#getDisplayParameters()
	 */
	@Override
	public String[] getDisplayParameters() {
		return new String[] { Messages.getString("Mirr.AggregateFunction.Parameters.Label.FinanceRate"), //$NON-NLS-1$
				Messages.getString("Mirr.AggregateFunction.Parameters.Label.ReinvestmentRate") }; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.aggregate.IAggregateFunction#getParametersCount()
	 */
	@Override
	public int getParametersCount() {
		return 2;
	}

	@Override
	public int getBIRTDataType() {
		return DataType.DOUBLE_TYPE;
	}
}
