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
	@Override
	public boolean enablePushDownForTransientQuery() {
		return true;
	}

	public void setEnablePushDownForTransientQuery(boolean enablePushDownForTransientQuery) {

	}

	@Override
	public Map<String, QuerySpecification> getOptimizedCombinedQuerySpec() {
		return null;
	}

	@Override
	public Map<String, List<IColumnDefinition>> getTrimmedColumns() {
		return null;
	}

	@Override
	public Map<String, List<String>> getPushedDownComputedColumns() {
		return null;
	}

	@Override
	public List<IColumnDefinition> getResultSetsForCombinedQuery() {
		return null;
	}

	@Override
	public Map<String, List<IFilterDefinition>> getFiltersInAdvance() {
		return null;
	}

	@Override
	public Map<String, List<String>> getOptimizedFilterExpr() {
		return null;
	}

	@Override
	public Map<String, Set<Integer>> getPositionsInCombinedQuery() {
		return null;
	}

	@Override
	public List<IComputedColumn> getUnpushedDownComputedColumnInCombinedQuery() {
		return null;
	}

	@Override
	public Map<String, List<String>> getCombinedDataSets() {
		return null;
	}

	@Override
	public Map<String, Set<String>> getInvalidAliasDataSetNames() {
		return null;
	}

	@Override
	public Map<String, List<Integer>> getPushedDownDataSetFilters() {
		return null;
	}

	@Override
	public List<IFilterDefinition> getFilterNeededMerge() {
		return null;
	}

	@Override
	public String getDataSetForAtomicQuery(AtomicQuery query) {
		return null;
	}
}
