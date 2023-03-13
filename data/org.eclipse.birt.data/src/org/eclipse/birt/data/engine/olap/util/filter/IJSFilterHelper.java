/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.data.engine.olap.util.filter;

import org.eclipse.birt.data.engine.olap.api.query.ICubeFilterDefinition;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;

/**
 *
 */
public interface IJSFilterHelper {

	/**
	 * get aggregation all levels.
	 *
	 * @return
	 */
	DimLevel[] getAggrLevels();

	/**
	 * get the dimension name.
	 *
	 * @return
	 */
	String getDimensionName();

	/**
	 *
	 * @return
	 */
	ICubeFilterDefinition getCubeFilterDefinition();

	/**
	 *
	 * @return
	 */
	boolean isAggregationFilter();

	/**
	 * This method should be called before we finish the usage of this class so that
	 * to deregister the "dimension" script object from the scope and deregister the
	 * current scope from its parent.
	 */
	void close();

}
