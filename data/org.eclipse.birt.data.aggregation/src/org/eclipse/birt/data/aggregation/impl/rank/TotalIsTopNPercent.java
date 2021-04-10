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

package org.eclipse.birt.data.aggregation.impl.rank;

import org.eclipse.birt.data.aggregation.api.IBuildInAggregation;
import org.eclipse.birt.data.aggregation.i18n.Messages;
import org.eclipse.birt.data.engine.api.aggregation.Accumulator;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * Implements the built-in Total.isTopPercent aggregation.
 */
public class TotalIsTopNPercent extends BaseTopBottomAggregation {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.aggregation.IAggregation#getName()
	 */
	public String getName() {
		return IBuildInAggregation.TOTAL_TOP_PERCENT_FUNC;
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

	private static class MyAccumulator extends PercentAccumulator {
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.data.engine.aggregation.rank.PercentAccumulator#getNextIndex
		 * ()
		 */
		protected int getNextIndex() throws DataException {
			return RankAggregationUtil.getNextTopIndex(cachedValues);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDescription()
	 */
	public String getDescription() {
		return Messages.getString("TotalIsTopNPercent.description"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDisplayName()
	 */
	public String getDisplayName() {
		return Messages.getString("TotalIsTopNPercent.displayName"); //$NON-NLS-1$
	}
}
