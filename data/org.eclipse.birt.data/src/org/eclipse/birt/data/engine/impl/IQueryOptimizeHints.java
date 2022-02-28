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

public interface IQueryOptimizeHints {
	String QUERY_OPTIMIZE_HINT = "org.eclipse.birt.data.internal.optimize.hints";

	boolean enablePushDownForTransientQuery();

	Map<String, List<String>> getOptimizedFilterExpr();

	Map<String, QuerySpecification> getOptimizedCombinedQuerySpec();

	Map<String, List<IColumnDefinition>> getTrimmedColumns();

	Map<String, List<String>> getPushedDownComputedColumns();

	List<IColumnDefinition> getResultSetsForCombinedQuery();

	Map<String, List<IFilterDefinition>> getFiltersInAdvance();

	Map<String, Set<Integer>> getPositionsInCombinedQuery();

	List<IComputedColumn> getUnpushedDownComputedColumnInCombinedQuery();

	Map<String, List<String>> getCombinedDataSets();

	Map<String, Set<String>> getInvalidAliasDataSetNames();

	Map<String, List<Integer>> getPushedDownDataSetFilters();

	List<IFilterDefinition> getFilterNeededMerge();

	String getDataSetForAtomicQuery(AtomicQuery query);
}
