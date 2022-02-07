/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.data.engine.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.data.engine.api.IColumnDefinition;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.datatools.connectivity.oda.spec.QuerySpecification;
import org.eclipse.datatools.connectivity.oda.spec.basequery.AtomicQuery;

public class QueryOptimizeHints implements IQueryOptimizeHints {
	public boolean enablePushDownForTransientQuery() {
		return true;
	}

	public void setEnablePushDownForTransientQuery(boolean enablePushDownForTransientQuery) {

	}

	public Map<String, QuerySpecification> getOptimizedCombinedQuerySpec() {
		return null;
	}

	public Map<String, List<IColumnDefinition>> getTrimmedColumns() {
		return null;
	}

	public Map<String, List<String>> getPushedDownComputedColumns() {
		return null;
	}

	public List<IColumnDefinition> getResultSetsForCombinedQuery() {
		return null;
	}

	public Map<String, List<IFilterDefinition>> getFiltersInAdvance() {
		return null;
	}

	public Map<String, List<String>> getOptimizedFilterExpr() {
		return null;
	}

	public Map<String, Set<Integer>> getPositionsInCombinedQuery() {
		return null;
	}

	public List<IComputedColumn> getUnpushedDownComputedColumnInCombinedQuery() {
		return null;
	}

	public Map<String, List<String>> getCombinedDataSets() {
		return null;
	}

	public Map<String, Set<String>> getInvalidAliasDataSetNames() {
		return null;
	}

	public Map<String, List<Integer>> getPushedDownDataSetFilters() {
		return null;
	}

	public List<IFilterDefinition> getFilterNeededMerge() {
		return null;
	}

	@Override
	public String getDataSetForAtomicQuery(AtomicQuery query) {
		return null;
	}
}
