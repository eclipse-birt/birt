/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
	public static final int AGGR_TABULAR = 0;
	public static final int AGGR_XTAB = 1;
	public static final int AGGR_MEASURE = 2;
	
	/**
	 * returns a list of <code>IAggregationInfo</code> instances which
	 * contains the information of aggregations.
	 * 
	 * @param type
	 * @return
	 */
	public abstract List getAggrInfoList( int type );

	/**
	 * Return an IAggregation instance according to the given aggregation name
	 * @param name the name of the aggregation
	 * @return an IAggregation instance specified by the give named
	 */
	public abstract IAggregation getAggregation( String name );

}