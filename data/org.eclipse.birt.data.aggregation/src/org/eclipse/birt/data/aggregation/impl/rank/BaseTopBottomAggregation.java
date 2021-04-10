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

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.aggregation.i18n.Messages;
import org.eclipse.birt.data.aggregation.impl.AggrFunction;
import org.eclipse.birt.data.aggregation.impl.Constants;
import org.eclipse.birt.data.aggregation.impl.ParameterDefn;
import org.eclipse.birt.data.aggregation.impl.SupportedDataTypes;
import org.eclipse.birt.data.engine.api.aggregation.IParameterDefn;

/**
 * Base definition of all Top/Bottom aggregations
 */
abstract class BaseTopBottomAggregation extends AggrFunction {

	private static final int NUMBER_OF_PASSES = 2;

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
		return DataType.BOOLEAN_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggregation#getParameterDefn()
	 */
	public IParameterDefn[] getParameterDefn() {
		return new IParameterDefn[] {
				new ParameterDefn(Constants.EXPRESSION_NAME, Constants.EXPRESSION_DISPLAY_NAME, false, true,
						SupportedDataTypes.CALCULATABLE, ""), //$NON-NLS-1$
				new ParameterDefn("n", Messages.getString("BaseTopBottomAggregation.param.n"), false, false, //$NON-NLS-1$ //$NON-NLS-2$
						SupportedDataTypes.CALCULATABLE, "") //$NON-NLS-1$
		};
	}

	public int getNumberOfPasses() {
		return NUMBER_OF_PASSES;
	}
}
