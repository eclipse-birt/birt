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

package org.eclipse.birt.data.engine.api.aggregation;

import java.util.List;

/**
 *
 */

public interface IAggregationFactory {

	/**
	 * returns a list of <code>IAggrFunction</code> instances that current factory
	 * produces.
	 *
	 * @return list of aggregate functions
	 */
	List<IAggrFunction> getAggregations();

	/**
	 * get an IAggrFunction instance whos's name is <code>name</code>.
	 *
	 * @param name
	 * @return aggregate function
	 */
	IAggrFunction getAggregation(String name);

}
