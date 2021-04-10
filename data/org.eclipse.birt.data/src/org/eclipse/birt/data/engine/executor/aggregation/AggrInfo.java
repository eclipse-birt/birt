
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.executor.aggregation;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IAggrInfo;

/**
 * 
 */

public class AggrInfo implements IAggrInfo {
	private IAggrFunction aggr;
	private IBaseExpression[] argument;
	private IBaseExpression filter;
	private int groupLevel;
	private int calcualteLevel;
	private String name;
	private int round;

	/**
	 * 
	 * @param name
	 * @param groupLevel
	 * @param aggr
	 * @param argument
	 * @param filter
	 * @throws DataException
	 */
	public AggrInfo(String name, int groupLevel, IAggrFunction aggr, IBaseExpression[] argument, IBaseExpression filter)
			throws DataException {
		this.name = name;
		this.aggr = aggr;
		this.groupLevel = groupLevel;
		this.filter = filter;
		this.argument = argument;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.odi.IAggrDefinition#getAggregation()
	 */
	public IAggrFunction getAggregation() {
		return this.aggr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.odi.IAggrDefinition#getArgument()
	 */
	public IBaseExpression[] getArgument() {
		return this.argument;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.odi.IAggrDefinition#getCalcualteLevel()
	 */
	public int getCalcualteLevel() {
		return this.calcualteLevel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.odi.IAggrDefinition#getFilter()
	 */
	public IBaseExpression getFilter() {
		return this.filter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.odi.IAggrDefinition#getGroupLevel()
	 */
	public int getGroupLevel() {
		return this.groupLevel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.odi.IAggrDefinition#getName()
	 */
	public String getName() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.odi.IAggrDefinition#setCalculateLevel(int)
	 */
	public void setCalculateLevel(int calculateLevel) {
		this.calcualteLevel = calculateLevel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.odi.IAggrDefinition#setRound(int)
	 */
	public void setRound(int round) {
		this.round = round;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.odi.IAggrDefinition#getRound()
	 */
	public int getRound() {
		return this.round;
	}

}
