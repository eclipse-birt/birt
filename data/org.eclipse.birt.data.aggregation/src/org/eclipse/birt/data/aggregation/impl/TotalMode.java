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

import java.util.Iterator;
import java.util.LinkedHashMap;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.aggregation.api.IBuildInAggregation;
import org.eclipse.birt.data.aggregation.i18n.Messages;
import org.eclipse.birt.data.engine.api.aggregation.Accumulator;
import org.eclipse.birt.data.engine.api.aggregation.IParameterDefn;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * 
 * Implements the built-in Total.mode aggregation
 */
public class TotalMode extends AggrFunction {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#getName()
	 */
	public String getName() {
		return IBuildInAggregation.TOTAL_MODE_FUNC;
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
				false, true, SupportedDataTypes.CALCULATABLE, "")//$NON-NLS-1$
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#newAccumulator()
	 */
	public Accumulator newAccumulator() {
		return new MyAccumulator();
	}

	private static class MyAccumulator extends SummaryAccumulator {
		// Maps a value (Double) to its count (Integer)
		private LinkedHashMap cacheMap;// used by for muti-mode storage, return the first appeared mode
		private Object mode;
		private int maxCount;
		private boolean multiMaxValue;

		public void start() {
			super.start();
			maxCount = 0;
			mode = null;
			cacheMap = new LinkedHashMap();
			multiMaxValue = false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.data.engine.aggregation.Accumulator#onRow(java.lang.Object[]
		 * )
		 */
		public void onRow(Object[] args) throws DataException {
			assert (args.length > 0);
			if (args[0] != null) {
				Object value = args[0];
				Object obj = cacheMap.get(value);
				int count = 1;
				if (obj != null) {
					count = ((Integer) obj).intValue();
					count++;
				}
				cacheMap.put(value, count);

				if (count > maxCount) {
					mode = value;
					maxCount = count;
					multiMaxValue = false;
				} else if (count == maxCount) {
					multiMaxValue = true;
				}
			}
		}

		public void finish() throws DataException {
			super.finish();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.data.engine.aggregation.SummaryAccumulator#getSummaryValue()
		 */
		public Object getSummaryValue() {
			if (maxCount == 1)// all of the objects are unique values
			{
				// no modes; ROM scripting spec says we should return null
				return null;
			}
			if (multiMaxValue && cacheMap != null && !cacheMap.isEmpty()) {
				// find the mode with the minimum index in all searched modes
				for (Iterator i = cacheMap.keySet().iterator(); i.hasNext();) {
					Object key = (Object) i.next();
					int count = (Integer) cacheMap.get(key);
					if (count == maxCount) {
						mode = key;
						break;
					}
				}
			}
			cacheMap = null;
			return mode;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDescription()
	 */
	public String getDescription() {
		return Messages.getString("TotalMode.description"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDisplayName()
	 */
	public String getDisplayName() {
		return Messages.getString("TotalMode.displayName"); //$NON-NLS-1$
	}
}