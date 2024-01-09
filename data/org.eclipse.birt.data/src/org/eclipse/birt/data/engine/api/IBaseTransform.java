/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
 *
 *************************************************************************
 */
package org.eclipse.birt.data.engine.api;

import java.util.Collection;
import java.util.List;

/**
 * Base class to define set of transforms that are common to queries, and groups
 * within queries.
 *
 */
public interface IBaseTransform {
	/**
	 * Returns the filters defined in this transform, as an ordered list of
	 * {@link org.eclipse.birt.data.engine.api.IFilterDefinition} objects.
	 *
	 * @return the filters. null if no filter is defined.
	 */
	List<IFilterDefinition> getFilters();

	/**
	 * Returns an unordered collection of subqueries that are alternative views of
	 * the result set for this transform. Objects are of type
	 * {@link org.eclipse.birt.data.engine.api.ISubqueryDefinition}.
	 *
	 * @return the subqueries for this transform
	 * @see ISubqueryDefinition
	 */

	Collection<ISubqueryDefinition> getSubqueries();

	/**
	 * Returns the sort criteria as an ordered list of
	 * {@link org.eclipse.birt.data.engine.api.ISortDefinition} objects.
	 *
	 * @return the sort criteria
	 */
	List<ISortDefinition> getSorts();

}
