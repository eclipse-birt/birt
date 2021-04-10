/*
 *************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
 * Implements the built-in Total.first aggregation
 */
public class TotalFirst extends AggrFunction {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#getName()
	 */
	public String getName() {
		return IBuildInAggregation.TOTAL_FIRST_FUNC;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#getType()
	 */
	public int getType() {
		return SUMMARY_AGGR;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.aggregation.IAggregation#getDateType()
	 */
	public int getDataType() {
		return DataType.ANY_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#getParameterDefn()
	 */
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
	public boolean isDataOrderSensitive() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#newAccumulator()
	 */
	public Accumulator newAccumulator() {
		return new MyAccumulator();
	}

	private class MyAccumulator extends SummaryAccumulator {
		private Object first = null;

		private boolean isRowAvailable = false;

		public void start() {
			super.start();
			first = null;
			isRowAvailable = false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.data.engine.aggregation.Accumulator#onRow(java.lang.Object[]
		 * )
		 */
		public void onRow(Object[] args) {
			assert (args.length > 0);
			if (args[0] != null) {
				if (!isRowAvailable) {
					first = args[0];
					isRowAvailable = true;
				}
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.data.engine.aggregation.SummaryAccumulator#getSummaryValue()
		 */
		public Object getSummaryValue() {
			return first;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDescription()
	 */
	public String getDescription() {
		return Messages.getString("TotalFirst.description"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDisplayName()
	 */
	public String getDisplayName() {
		return Messages.getString("TotalFirst.displayName"); //$NON-NLS-1$
	}
}