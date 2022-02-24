/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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

package org.eclipse.birt.data.engine.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.NoRecalculateIVQuery;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.document.FilterDefnUtil;

/**
 * 
 */

public class NoRecalculateQueryUtil {

	public static IPreparedQuery getPreparedIVQuery(DataEngineImpl dataEngine, IBaseQueryDefinition previousQueryDefn,
			IQueryDefinition queryDefn, String queryResultID, Map appContext) throws DataException {
		return new PreparedNoRecalculateIVQuery(dataEngine,
				getOptimizedIVQuery(previousQueryDefn, queryDefn, queryResultID), appContext,
				QueryContextVisitorUtil.createQueryContextVisitor(queryDefn, appContext));
	}

	public static boolean hasNoRecalculateFilter(IQueryDefinition query) {
		if (hasGroupFilters(query.getGroups()) || hasQueryFilters(query.getFilters())) {
			return true;
		}
		return false;
	}

	private static boolean hasQueryFilters(List<IFilterDefinition> filters) {
		for (IFilterDefinition f : filters) {
			if (!f.updateAggregation())
				return true;
		}
		return false;
	}

	private static boolean hasGroupFilters(List<IGroupDefinition> groupDefns) {
		for (IGroupDefinition g : groupDefns) {
			if (hasQueryFilters(g.getFilters()))
				return true;
		}
		return false;
	}

	public static IQueryDefinition getOptimizedIVQuery(IBaseQueryDefinition oldq, IQueryDefinition newq,
			String queryResultID) throws DataException {
		if (oldq == null || newq == null)
			return null;

		if (!QueryCompUtil.isEqualBindings(oldq.getBindings(), newq.getBindings()))
			return null;
		if (!QueryCompUtil.isEqualGroups(oldq.getGroups(), newq.getGroups(), true))
			return null;
		if (!QueryCompUtil.isEqualSorts(oldq.getSorts(), newq.getSorts()))
			return null;

		List<IFilterDefinition> filters = getEffectiveFilters(oldq.getFilters(), newq.getFilters());
		if (filters == null)
			return null;

		// need pass group info due to need to prepare sub query etc.
		NoRecalculateIVQuery query = new NoRecalculateIVQuery(newq, oldq, new LinkedList<ISortDefinition>(), filters,
				newq.getGroups(), queryResultID);

		return query;
	}

	public static boolean isOptimizableIVQuery(IBaseQueryDefinition oldq, IQueryDefinition newq, String queryResultID)
			throws DataException {
		if (oldq == null || newq == null)
			return false;

		if (!QueryCompUtil.isEqualBindings(oldq.getBindings(), newq.getBindings()))
			return false;
		if (!QueryCompUtil.isEqualGroups(oldq.getGroups(), newq.getGroups(), true))
			return false;
		if (!QueryCompUtil.isEqualSorts(oldq.getSorts(), newq.getSorts()))
			return false;

		List<IFilterDefinition> filters = getEffectiveFilters(oldq.getFilters(), newq.getFilters());
		if (filters == null)
			return false;
		return true;
	}

	private static List<IFilterDefinition> getEffectiveFilters(List<IFilterDefinition> filters1,
			List<IFilterDefinition> filters2) throws DataException {
		if (filters1 == filters2) {
			return new LinkedList<IFilterDefinition>();
		}

		if (filters1.size() > filters2.size())
			return null;

		Iterator<IFilterDefinition> itr1 = filters1.iterator();
		Iterator<IFilterDefinition> itr2 = filters2.iterator();
		while (itr1.hasNext()) {
			IFilterDefinition fDefn1 = itr1.next();
			IFilterDefinition fDefn2 = itr2.next();
			if (!FilterDefnUtil.isEqualFilter(fDefn1, fDefn2)
					|| fDefn1.updateAggregation() != fDefn2.updateAggregation()) {
				return null;
			}
		}

		ArrayList<IFilterDefinition> effectiveFilters = new ArrayList<IFilterDefinition>();
		while (itr2.hasNext()) {
			IFilterDefinition f = itr2.next();
			if (!f.updateAggregation()) {
				effectiveFilters.add(f);
			} else {
				return null;
			}
		}
		return effectiveFilters.size() > 0 ? effectiveFilters : null;
	}
}
