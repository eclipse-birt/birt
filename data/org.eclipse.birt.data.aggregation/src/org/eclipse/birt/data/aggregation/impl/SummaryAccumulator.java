/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

import org.eclipse.birt.data.aggregation.calculator.ICalculator;
import org.eclipse.birt.data.engine.api.aggregation.Accumulator;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * Represents the built-in summary accumulator
 */
public abstract class SummaryAccumulator extends Accumulator {
	protected boolean isFinished = false;

	protected ICalculator calculator;

	/**
	 * Derived accumulator classes not using calculators will use the default
	 * constructor.
	 */
	public SummaryAccumulator() {
		calculator = null;
	}

	/**
	 * An explicit constructor. Derived accumulator classes should use it for
	 * constructing calculator based on the return aggregate function value type (or
	 * other business logic).
	 */
	public SummaryAccumulator(ICalculator calc) {
		calculator = calc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.aggregation.Accumulator#start()
	 */
	@Override
	public void start() {
		isFinished = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.aggregation.Accumulator#finish()
	 */
	@Override
	public void finish() throws DataException {
		isFinished = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.aggregation.Accumulator#getValue()
	 */
	@Override
	public Object getValue() throws DataException {
		if (!isFinished) {
			throw new RuntimeException("Error! Call summary total function before finished the dataset"); //$NON-NLS-1$
		}
		return getSummaryValue();
	}

	abstract public Object getSummaryValue() throws DataException;

}
