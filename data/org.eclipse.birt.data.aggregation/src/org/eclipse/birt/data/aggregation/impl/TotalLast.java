/*
 *************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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
 *
 *************************************************************************
 */
package org.eclipse.birt.data.aggregation.impl;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.aggregation.api.IBuildInAggregation;
import org.eclipse.birt.data.aggregation.i18n.Messages;
import org.eclipse.birt.data.engine.api.aggregation.Accumulator;
import org.eclipse.birt.data.engine.api.aggregation.IParameterDefn;

/**
 *
 * Implements the built-in Total.last aggregation
 */
public class TotalLast extends AggrFunction {

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#getName()
	 */
	@Override
	public String getName() {
		return IBuildInAggregation.TOTAL_LAST_FUNC;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#getType()
	 */
	@Override
	public int getType() {
		return SUMMARY_AGGR;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.aggregation.IAggregation#getDateType()
	 */
	@Override
	public int getDataType() {
		return DataType.ANY_TYPE;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#getParameterDefn()
	 */
	@Override
	public IParameterDefn[] getParameterDefn() {
		return new IParameterDefn[] { new ParameterDefn(Constants.EXPRESSION_NAME, Constants.EXPRESSION_DISPLAY_NAME,
				false, true, SupportedDataTypes.ANY, "")//$NON-NLS-1$
		};
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#
	 * isDataOrderSensitive()
	 */
	@Override
	public boolean isDataOrderSensitive() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#newAccumulator()
	 */
	@Override
	public Accumulator newAccumulator() {
		return new MyAccumulator();
	}

	private static class MyAccumulator extends SummaryAccumulator {
		private Object last = null;

		private boolean isRowAvailable = false;

		@Override
		public void start() {
			super.start();
			last = null;
			isRowAvailable = false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.birt.data.engine.aggregation.Accumulator#onRow(java.lang.Object[]
		 * )
		 */
		@Override
		public void onRow(Object[] args) {
			assert (args.length > 0);
			if (args[0] != null) {
				if (!isRowAvailable) {
					isRowAvailable = true;
				}
				last = args[0];
			}
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.birt.data.engine.aggregation.SummaryAccumulator#getSummaryValue()
		 */
		@Override
		public Object getSummaryValue() {
			return last;
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDescription()
	 */
	@Override
	public String getDescription() {
		return Messages.getString("TotalLast.description"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return Messages.getString("TotalLast.displayName"); //$NON-NLS-1$
	}
}
