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

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * The common parent Accumulator used by Top/Bottom N aggregations.
 */
public abstract class NAccumulator extends BaseTopBottomAccumulator {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.aggregation.rank.BaseTopBottomAccumulator#
	 * populateNValue(java.lang.Object)
	 */
	protected double populateNValue(Object N) throws DataException {
		double result = 0;
		try {
			result = DataTypeUtil.toInteger(N).intValue();
		} catch (BirtException e) {
			// conversion error
			throw new DataException(ResourceConstants.INVALID_TOP_BOTTOM_ARGUMENT, e);
		}
		if (result < 0)
			throw new DataException(ResourceConstants.INVALID_TOP_BOTTOM_N_ARGUMENT);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.aggregation.rank.BaseTopBottomAccumulator#
	 * adjustNValue(double)
	 */
	protected int adjustNValue(double N) {
		return (int) (N < 0 ? 0 : N);
	}
}
