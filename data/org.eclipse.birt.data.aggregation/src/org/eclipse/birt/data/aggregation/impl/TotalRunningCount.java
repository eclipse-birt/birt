/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.aggregation.impl;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.aggregation.api.IBuildInAggregation;
import org.eclipse.birt.data.aggregation.i18n.Messages;
import org.eclipse.birt.data.engine.api.aggregation.Accumulator;
import org.eclipse.birt.data.engine.api.aggregation.IParameterDefn;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * Implements build-in running count function.
 */
public class TotalRunningCount extends AggrFunction {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.aggregation.IAggregation#getName()
	 */
	public String getName() {
		return IBuildInAggregation.TOTAL_RUNNINGCOUNT_FUNC;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.aggregation.IAggregation#getType()
	 */
	public int getType() {
		return RUNNING_AGGR;
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
	 * org.eclipse.birt.data.engine.api.aggregation.IAggregation#getParameterDefn()
	 */
	public IParameterDefn[] getParameterDefn() {
		// one parameter definition
		return new IParameterDefn[] { new ParameterDefn(Constants.EXPRESSION_NAME, Constants.EXPRESSION_DISPLAY_NAME,
				true, true, SupportedDataTypes.CALCULATABLE, "") };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggregation#newAccumulator()
	 */
	public Accumulator newAccumulator() {
		return new MyAccumulator();
	}

	private static class MyAccumulator extends RunningAccumulator {

		private int count;
		boolean countByColumn = true;

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.api.aggregation.Accumulator#start()
		 */
		public void start() throws DataException {
			count = 0;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.data.engine.api.aggregation.Accumulator#onRow(java.lang.
		 * Object[])
		 */
		public void onRow(Object[] args) throws DataException {
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
		 * @see org.eclipse.birt.data.engine.api.aggregation.Accumulator#getValue()
		 */
		public Object getValue() throws DataException {
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
		return Messages.getString("TotalRunningCount.description"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDisplayName()
	 */
	public String getDisplayName() {
		return Messages.getString("TotalRunningCount.displayName"); //$NON-NLS-1$
	}

}
