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

import java.util.ArrayList;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.aggregation.api.IBuildInAggregation;
import org.eclipse.birt.data.aggregation.calculator.CalculatorFactory;
import org.eclipse.birt.data.aggregation.calculator.ICalculator;
import org.eclipse.birt.data.aggregation.i18n.Messages;
import org.eclipse.birt.data.aggregation.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.api.aggregation.Accumulator;
import org.eclipse.birt.data.engine.api.aggregation.IParameterDefn;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * 
 * Implements the built-in Total.irr aggregation
 */
public class TotalIrr extends AggrFunction {
//	private static Logger logger = Logger.getLogger( TotalIrr.class.getName( ) );

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#getName()
	 */
	public String getName() {
		return IBuildInAggregation.TOTAL_IRR_FUNC;
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
		return DataType.DOUBLE_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#getParameterDefn()
	 */
	public IParameterDefn[] getParameterDefn() {
		return new IParameterDefn[] {
				new ParameterDefn(Constants.EXPRESSION_NAME, Constants.EXPRESSION_DISPLAY_NAME, false, true,
						SupportedDataTypes.CALCULATABLE, ""), //$NON-NLS-1$
				new ParameterDefn("intrate", Messages.getString("TotalIrr.param.intrate"), false, false, //$NON-NLS-1$ //$NON-NLS-2$
						SupportedDataTypes.CALCULATABLE, "") //$NON-NLS-1$
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#newAccumulator()
	 */
	public Accumulator newAccumulator() {
		return new MyAccumulator(CalculatorFactory.getCalculator(getDataType()));
	}

	private static class MyAccumulator extends SummaryAccumulator {

		private ArrayList list;

		private double intrate = 0D;

		private Number ret = null;

		MyAccumulator(ICalculator calc) {
			super(calc);
		}

		public void start() {
			super.start();
			intrate = 0D;
			list = new ArrayList<Number>();
			ret = null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.data.engine.aggregation.Accumulator#onRow(java.lang.Object[]
		 * )
		 */
		public void onRow(Object[] args) throws DataException {
			assert (args.length > 1);
			if (args[0] != null && args[1] != null) {
				try {
					if (list.size() == 0) {
						intrate = DataTypeUtil.toDouble(args[1]).doubleValue();
					}
					list.add(calculator.getTypedObject(args[0]));
				} catch (BirtException e) {
					throw DataException.wrap(new AggrException(ResourceConstants.DATATYPEUTIL_ERROR, e));
				}
			}
		}

		public void finish() throws DataException {
			if (list.size() > 0) {
				Number[] values = new Number[list.size()];
				list.toArray(values);
				try {
					ret = new Double(Finance.irr(values, intrate));
				} catch (BirtException e) {
					throw DataException.wrap(e);
					// Failed to calculate MIRR value, you may consider returning null
					// instead of throwing exception directly
//					logger.log( Level.WARNING, "Failed to calcualte IRR value", e ); //$NON-NLS-1$
//					ret = null;

				}
			}
			super.finish();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.data.engine.aggregation.SummaryAccumulator#getSummaryValue()
		 */
		public Object getSummaryValue() {
			return ret;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDescription()
	 */
	public String getDescription() {
		return Messages.getString("TotalIrr.description"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDisplayName()
	 */
	public String getDisplayName() {
		return Messages.getString("TotalIrr.displayName"); //$NON-NLS-1$
	}

}
