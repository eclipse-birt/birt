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

package org.eclipse.birt.data.engine.api.aggregation;

import java.util.List;

/**
 * 
 */

public interface IAggregationFactory
{

	/**
	 * returns a list of <code>IAggrFunction</code> instances that current
	 * factory produces.
	 * 
	 * @return list of aggregate functions
	 */
	public List<IAggrFunction> getAggregations( );

	/**
	 * get an IAggrFunction instance whos's name is <code>name</code>.
	 * 
	 * @param name
	 * @return aggregate function
	 */
	public IAggrFunction getAggregation( String name );

}