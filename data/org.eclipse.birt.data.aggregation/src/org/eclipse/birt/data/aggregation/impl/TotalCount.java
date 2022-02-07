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
 * Implements the built-in Total.count aggregation
 */
public class TotalCount extends AggrFunction {

	public String getName() {
		return IBuildInAggregation.TOTAL_COUNT_FUNC;
	}

	public int getType() {
		return SUMMARY_AGGR;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.aggregation.IAggregation#getDateType()
	 */
	public int getDataType() {
		return DataType.INTEGER_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getParameterDefn()
	 */
	public IParameterDefn[] getParameterDefn() {
		// one parameter definition
		return new IParameterDefn[] { new ParameterDefn(Constants.EXPRESSION_NAME, Constants.EXPRESSION_DISPLAY_NAME,
				true, true, SupportedDataTypes.ANY, "") };
	}

	public Accumulator newAccumulator() {
		return new MyAccumulator();
	}

	private static class MyAccumulator extends SummaryAccumulator {

		private int count = 0;
		boolean countByColumn = true;

		public void start() {
			super.start();
			count = 0;
		}

		public void onRow(Object[] args) {
			if (!countByColumn || args == null || args.length == 0) {
				if (countByColumn) {
					countByColumn = false;
				}
				++count;
			} else if (args.length > 0 && args[0] != null) {
				++count;
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.data.engine.aggregation.SummaryAccumulator#getSummaryValue()
		 */
		public Object getSummaryValue() {
			return Integer.valueOf(count);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDescription()
	 */
	public String getDescription() {
		return Messages.getString("TotalCount.description"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDisplayName()
	 */
	public String getDisplayName() {
		return Messages.getString("TotalCount.displayName"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.AggrFunction#getDefaultValue()
	 */
	public Object getDefaultValue() {
		return Integer.valueOf(0);
	}
}
