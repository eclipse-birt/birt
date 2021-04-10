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
 * Implements the built-in Total.mirr aggregation
 */
public class TotalMirr extends AggrFunction {
//	private static Logger logger = Logger.getLogger( TotalMirr.class.getName( ) );
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#getName()
	 */
	public String getName() {
		return IBuildInAggregation.TOTAL_MIRR_FUNC;
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
				new ParameterDefn("finance rate", Messages.getString("TotalMirr.param.finance_rate"), false, false, //$NON-NLS-1$ //$NON-NLS-2$
						SupportedDataTypes.CALCULATABLE, ""), //$NON-NLS-1$
				new ParameterDefn("reinvestment rate", Messages.getString("TotalMirr.param.reinvestment_rate"), false, //$NON-NLS-1$ //$NON-NLS-2$
						false, SupportedDataTypes.CALCULATABLE, ""), //$NON-NLS-1$
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

		private ArrayList<Number> list;

		private double frate = 0D;

		private double rrate = 0D;

		private Number ret = null;

		MyAccumulator(ICalculator calc) {
			super(calc);
		}

		public void start() {
			super.start();
			frate = 0D;
			rrate = 0D;
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
			assert (args.length > 2);
			if (args[0] != null) {
				try {
					if (list.size() == 0) {
						if (args[1] != null) {
							// if args[1] is null, frate remains 0
							frate = DataTypeUtil.toDouble(args[1]).doubleValue();
						}
						if (args[2] != null) {
							// if args[2] is null, rrate remains 0
							rrate = DataTypeUtil.toDouble(args[2]).doubleValue();
						}
					}
					list.add(calculator.add(calculator.getTypedObject(0), calculator.getTypedObject(args[0])));
				} catch (BirtException e) {
					throw DataException.wrap(new AggrException(ResourceConstants.DATATYPEUTIL_ERROR, e));
				}
			}
		}

		public void finish() throws DataException {
			// user input parameters are invalid, throw exception to warn user
			if (frate < 0 || rrate < 0) {
				throw DataException.wrap(new AggrException(ResourceConstants.ILLEGAL_PARAMETER_FUN, "mirr")); //$NON-NLS-1$
			}
			if (list.size() > 0) {
				Number[] values = new Number[list.size()];
				list.toArray(values);
				try {
					ret = new Double(Finance.mirr(values, frate, rrate));
				} catch (BirtException e) {

					throw DataException.wrap(e);
					// Failed to calculate MIRR value, you may consider returning null
					// instead of throwing exception directly
//					logger.log( Level.WARNING, "Failed to calcualte MIRR value", e ); //$NON-NLS-1$
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
		return Messages.getString("TotalMirr.description"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDisplayName()
	 */
	public String getDisplayName() {
		return Messages.getString("TotalMirr.displayName"); //$NON-NLS-1$
	}

}