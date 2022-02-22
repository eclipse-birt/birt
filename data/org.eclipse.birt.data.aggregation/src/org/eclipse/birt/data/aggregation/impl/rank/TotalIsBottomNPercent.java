/*******************************************************************************
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
 *******************************************************************************/

package org.eclipse.birt.data.aggregation.impl.rank;

import org.eclipse.birt.data.aggregation.api.IBuildInAggregation;
import org.eclipse.birt.data.aggregation.i18n.Messages;
import org.eclipse.birt.data.engine.api.aggregation.Accumulator;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * Implements the built-in Total.isBottomPercent aggregation.
 */
public class TotalIsBottomNPercent extends BaseTopBottomAggregation {
	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.aggregation.IAggregation#getName()
	 */
	@Override
	public String getName() {
		return IBuildInAggregation.TOTAL_BOTTOM_PERCENT_FUNC;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggregation#newAccumulator()
	 */
	@Override
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
		@Override
		protected int getNextIndex() throws DataException {
			return RankAggregationUtil.getNextBottomIndex(cachedValues);
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
		return Messages.getString("TotalIsBottomNPercent.description"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return Messages.getString("TotalIsBottomNPercent.displayName"); //$NON-NLS-1$
	}
}
