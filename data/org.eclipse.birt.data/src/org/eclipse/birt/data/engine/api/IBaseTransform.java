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
	public List getFilters();

	/**
	 * Returns an unordered collection of subqueries that are alternative views of
	 * the result set for this transform. Objects are of type
	 * {@link org.eclipse.birt.data.engine.api.ISubqueryDefinition}.
	 * 
	 * @return the subqueries for this transform
	 * @see ISubqueryDefinition
	 */

	public Collection getSubqueries();

	/**
	 * Returns the sort criteria as an ordered list of
	 * {@link org.eclipse.birt.data.engine.api.ISortDefinition} objects.
	 * 
	 * @return the sort criteria
	 */
	public List getSorts();

}
