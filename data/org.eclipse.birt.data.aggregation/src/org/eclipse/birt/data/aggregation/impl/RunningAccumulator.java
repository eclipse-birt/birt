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
 * 
 * Represents the built-in summary accumulator
 */
public abstract class RunningAccumulator extends Accumulator
{

	protected ICalculator calculator;
	
	/**
	 * Derived accumulator classes not using calculators will use the default
	 * constructor.  
	 */
	public RunningAccumulator( )
	{
		calculator = null;
	}
	/**
	 * An explicit constructor. Derived accumulator classes should use it
	 * for constructing calculator based on the return aggregate function
	 * value type (or other business logic).
	 */
	public RunningAccumulator( ICalculator calc )
	{
		calculator = calc;
	}

}
