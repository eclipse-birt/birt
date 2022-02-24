/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.query.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.ICollectionConditionalExpression;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeOperation;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.api.IBindingValueFetcher;
import org.eclipse.birt.data.engine.olap.data.api.cube.ICube;
import org.eclipse.birt.data.engine.olap.data.impl.CachedAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.impl.Cube;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.AggrMeasureFilterHelper;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.AggregationFilterHelper;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;
import org.eclipse.birt.data.engine.olap.data.util.BufferedStructureArray;
import org.eclipse.birt.data.engine.olap.data.util.CompareUtil;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;
import org.eclipse.birt.data.engine.olap.impl.query.AddingNestAggregations;
import org.eclipse.birt.data.engine.olap.impl.query.CubeQueryExecutor;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionCompiler;
import org.eclipse.birt.data.engine.olap.util.filter.AggrMeasureFilterEvalHelper;
import org.eclipse.birt.data.engine.olap.util.filter.BaseDimensionFilterEvalHelper;
import org.eclipse.birt.data.engine.olap.util.filter.IFacttableRow;
import org.eclipse.birt.data.engine.olap.util.filter.IJSFacttableFilterEvalHelper;
import org.eclipse.birt.data.engine.olap.util.filter.IJSFilterHelper;
import org.eclipse.birt.data.engine.olap.util.filter.JSFacttableFilterEvalHelper;
import org.eclipse.birt.data.engine.script.ScriptConstants;

public class NoUpdateAggregateFilterHelper {

	IAggregationResultSet[] applyNoAggrUpdateFilters(List finalFilters, CubeQueryExecutor executor,
			IAggregationResultSet[] rs, ICube cube, IBindingValueFetcher fetcher, boolean fromCubeOperation)
			throws DataException, IOException {
		finalFilters = this.getNoAggrUpdateFilters(finalFilters);
		if (!finalFilters.isEmpty()) {
			List aggrEvalList = new ArrayList<AggrMeasureFilterEvalHelper>();
			List dimEvalList = new ArrayList<IJSFilterHelper>();
			List<IFilterDefinition> drillFilterList = new ArrayList<>();
			for (int i = 0; i < finalFilters.size(); i++) {
				IFilterDefinition filter = (IFilterDefinition) finalFilters.get(i);
				boolean find = false;
				String bindingName = OlapExpressionCompiler.getReferencedScriptObject(filter.getExpression(),
						ScriptConstants.DATA_BINDING_SCRIPTABLE);
				if (executor.getCubeQueryDefinition().getCubeOperations().length > 0) {
					ICubeOperation[] operations = executor.getCubeQueryDefinition().getCubeOperations();
					for (int j = 0; j < operations.length; j++) {
						if (operations[j] instanceof AddingNestAggregations) {
							AddingNestAggregations aggr = (AddingNestAggregations) operations[j];
							IBinding[] bindings = aggr.getNewBindings();
							for (int k = 0; k < bindings.length; k++) {
								if (bindings[k].getBindingName().equals(bindingName)) {
									find = true;
									break;
								}
							}
						}
					}

				}
				if (find != fromCubeOperation) {
					continue;
				}
				int type = executor.getFilterType(filter, executor.getDimLevelsDefinedInCubeQuery());

				if (type == executor.DIMENSION_FILTER) {
					dimEvalList.add(BaseDimensionFilterEvalHelper.createFilterHelper(executor.getOuterResults(),
							executor.getScope(), executor.getCubeQueryDefinition(), filter,
							executor.getSession().getEngineContext().getScriptContext()));
				} else if (type == executor.AGGR_MEASURE_FILTER) {
					aggrEvalList.add(new AggrMeasureFilterEvalHelper(executor.getOuterResults(), executor.getScope(),
							executor.getCubeQueryDefinition(), filter,
							executor.getSession().getEngineContext().getScriptContext()));
				} else if (type == executor.FACTTABLE_FILTER) {
					drillFilterList.add(filter);
				}
			}
			List<Integer> affectedAggrResultSetIndex = new ArrayList<>();
			if (aggrEvalList.size() > 0) {
				AggrMeasureFilterHelper aggrFilterHelper = new AggrMeasureFilterHelper(cube, rs);
				aggrFilterHelper.setQueryExecutor(executor);
				aggrFilterHelper.setBindingValueFetcher(fetcher);
				rs = aggrFilterHelper.removeInvalidAggrRows(aggrEvalList, affectedAggrResultSetIndex);

			}
			if (dimEvalList.size() > 0) {
				AggregationFilterHelper helper = new AggregationFilterHelper((Cube) cube, dimEvalList, fetcher);
				rs = helper.generateFilteredAggregationResultSet(rs, affectedAggrResultSetIndex);
			}

			Map<DimLevel, IJSFacttableFilterEvalHelper> edgeDrillFilterMap = populateEdgeDrillFilterMap(executor,
					drillFilterList);

			List<IAggregationResultSet> edgeResultSet = populateAndFilterEdgeResultSet(rs, edgeDrillFilterMap);

			for (int i = 0; i < edgeResultSet.size(); i++) {
				for (int j = 0; j < affectedAggrResultSetIndex.size(); j++) {
					this.applyJoin(edgeResultSet.get(i), rs[affectedAggrResultSetIndex.get(j).intValue()]);
				}
			}

			if (edgeResultSet.size() > 1) {
				combineEdgeResultSetsInfo(edgeResultSet);
			}
		}

		return rs;
	}

	private void combineEdgeResultSetsInfo(List<IAggregationResultSet> edgeResultSet) {
		int index = -1;
		for (int i = 0; i < edgeResultSet.size(); i++) {
			IAggregationResultSet rs = edgeResultSet.get(i);
			if (rs.length() == 0) {
				index = i;
			}
		}
		if (index >= 0) {
			for (int i = 0; i < edgeResultSet.size(); i++) {
				if (i != index) {
					IAggregationResultSet rs = edgeResultSet.get(i);
					IDiskArray newRsRows = new BufferedStructureArray(AggregationResultRow.getCreator(), rs.length());
					if (rs instanceof AggregationResultSet) {
						((AggregationResultSet) rs).setAggregationResultRows(newRsRows);
					} else if (rs instanceof CachedAggregationResultSet) {
						((CachedAggregationResultSet) rs).setAggregationResultRows(newRsRows);
					}
				}
			}
		}
	}

	List<IAggregationResultSet> populateAndFilterEdgeResultSet(IAggregationResultSet[] rs,
			Map<DimLevel, IJSFacttableFilterEvalHelper> edgeDrillFilterMap) throws IOException, DataException {
		List<IAggregationResultSet> edgeResultSet = new ArrayList<>();

		for (int i = 0; i < rs.length; i++) {
			if (rs[i].getAggregationDefinition().getAggregationFunctions() == null) {
				edgeResultSet.add(rs[i]);

				if (edgeDrillFilterMap == null || edgeDrillFilterMap.isEmpty()) {
					continue;
				}

				filterEdgeAggrSet(edgeDrillFilterMap, rs[i]);
			}
		}

		return edgeResultSet;
	}

	private void filterEdgeAggrSet(Map<DimLevel, IJSFacttableFilterEvalHelper> edgeDrillFilterMap,
			IAggregationResultSet edgeAggrSet) throws IOException, DataException {
		IJSFacttableFilterEvalHelper drillFilterHelper = null;
		for (DimLevel dimLevel : edgeAggrSet.getAllLevels()) {
			drillFilterHelper = edgeDrillFilterMap.get(dimLevel);
			if (drillFilterHelper != null) {
				AggregateRowWrapper aggrRowWrapper = new AggregateRowWrapper(edgeAggrSet);
				IDiskArray newRs = new BufferedStructureArray(AggregationResultRow.getCreator(), 2000);
				for (int j = 0; j < edgeAggrSet.length(); j++) {
					edgeAggrSet.seek(j);
					if (drillFilterHelper.evaluateFilter(aggrRowWrapper)) {
						newRs.add(edgeAggrSet.getCurrentRow());
					}
				}
				reSetAggregationResultSetDiskArray(edgeAggrSet, newRs);
			}
		}
	}

	private void reSetAggregationResultSetDiskArray(IAggregationResultSet edgeAggrSet, IDiskArray newRs) {
		if (edgeAggrSet instanceof AggregationResultSet) {
			((AggregationResultSet) edgeAggrSet).setAggregationResultRows(newRs);
		} else if (edgeAggrSet instanceof CachedAggregationResultSet) {
			((CachedAggregationResultSet) edgeAggrSet).setAggregationResultRows(newRs);
			((CachedAggregationResultSet) edgeAggrSet).setLength(newRs.size());
		}
	}

	/**
	 * Populate Edge Drill Filter Map. There will be one random level picked from
	 * edge to map to a drill filter.
	 *
	 * @param executor
	 * @param drillFilterList
	 * @param edgeDrillFilterMap
	 * @throws DataException
	 */
	private Map<DimLevel, IJSFacttableFilterEvalHelper> populateEdgeDrillFilterMap(CubeQueryExecutor executor,
			List<IFilterDefinition> drillFilterList) throws DataException {
		Map<DimLevel, IJSFacttableFilterEvalHelper> result = new HashMap<>();

		for (IFilterDefinition filterDefn : drillFilterList) {
			assert filterDefn instanceof ICollectionConditionalExpression;
			Collection<IScriptExpression> exprs = ((ICollectionConditionalExpression) (filterDefn.getExpression()))
					.getExpr();
			Iterator<IScriptExpression> exprsIterator = exprs.iterator();
			DimLevel containedDimLevel = null;
			while (exprsIterator.hasNext()) {
				Iterator dimLevels = OlapExpressionCompiler.getReferencedDimLevel(exprsIterator.next(), new ArrayList())
						.iterator();
				if (dimLevels.hasNext()) {
					containedDimLevel = (DimLevel) dimLevels.next();
				}
				if (containedDimLevel != null) {
					break;
				}
			}

			if (containedDimLevel == null) {
				continue;
			}

			result.put(containedDimLevel, new JSFacttableFilterEvalHelper(executor.getScope(),
					executor.getSession().getEngineContext().getScriptContext(), filterDefn, null, null));
		}
		return result;
	}

	private int getPos(String[][] joinLevelKeys, String[][] detailLevelKeys) {
		for (int i = 0; i < detailLevelKeys.length; i++) {
			if (CompareUtil.compare(joinLevelKeys[0], detailLevelKeys[i]) == 0) {
				return i;
			}
		}
		return -1;
	}

	void applyJoin(IAggregationResultSet joinRS, IAggregationResultSet detailRS) throws IOException {
		String[][] detailLevelKeys = detailRS.getLevelKeys();
		List<Members> detailMember = new ArrayList<>();
		String[][] joinLevelKeys;
		Member[] members = null;
		IDiskArray aggregationResultRows = null;

		joinLevelKeys = joinRS.getLevelKeys();

		if (detailLevelKeys == null) {
			return;
		}

		int pos = getPos(joinLevelKeys, detailLevelKeys);
		if (pos < 0) {
			int detailLevelKeyslen = detailLevelKeys.length;
			if (detailLevelKeyslen == 0 && detailRS.length() == 0) {
				IDiskArray emptyRows = new BufferedStructureArray(AggregationResultRow.getCreator(), 0);
				reSetAggregationResultSetDiskArray(joinRS, emptyRows);
			}
			return;
		}

		for (int index = 0; index < detailRS.length(); index++) {
			detailRS.seek(index);
			members = detailRS.getCurrentRow().getLevelMembers();
			if (members == null) {
				continue;
			}
			List<Member> tmpMembers = new ArrayList<>();
			for (int j = pos; j < pos + joinLevelKeys.length; j++) {
				if (j > members.length - 1) {
					break;
				}
				if (CompareUtil.compare(joinLevelKeys[j - pos], detailLevelKeys[j]) == 0) {
					tmpMembers.add(members[j]);
				}

			}
			detailMember.add(new Members(tmpMembers.toArray(new Member[] {})));
		}
		Collections.sort(detailMember);
		if (joinRS instanceof AggregationResultSet) {
			aggregationResultRows = ((AggregationResultSet) joinRS).getAggregationResultRows();
		} else if (joinRS instanceof CachedAggregationResultSet) {
			aggregationResultRows = ((CachedAggregationResultSet) joinRS).getAggregationResultRows();
		}
		IDiskArray newRsRows = new BufferedStructureArray(AggregationResultRow.getCreator(),
				aggregationResultRows.size());
		int result;
		for (int index = 0; index < joinRS.length(); index++) {
			joinRS.seek(index);
			result = Collections.binarySearch(detailMember, new Members(joinRS.getCurrentRow().getLevelMembers()));

			if (result >= 0) {
				newRsRows.add(aggregationResultRows.get(index));
			}
		}
		reSetAggregationResultSetDiskArray(joinRS, newRsRows);
		detailMember.clear();

	}

	private class Members implements Comparable<Members> {

		public Member[] members;

		public Members(Member[] members) {
			this.members = members;
		}

		@Override
		public int compareTo(Members other) {
			for (int i = 0; i < members.length; i++) {
				int result = members[i].compareTo(other.members[i]);
				if (result != 0) {
					return result;
				}
			}
			return 0;
		}

	}

	List getNoAggrUpdateFilters(List filters) {
		List NoAggrUpdateFilters = new ArrayList();

		for (int i = 0; i < filters.size(); i++) {
			if (!((IFilterDefinition) filters.get(i)).updateAggregation()) {
				NoAggrUpdateFilters.add(filters.get(i));
			}
		}
		return NoAggrUpdateFilters;
	}

	/**
	 * The class that wrap an IAggregationResultSet instance into IFacttableRow. The
	 * actual IAggregationResultRow instance returned is controlled by internal
	 * cursor in IAggregationResultSet instance.
	 *
	 * @author lzhu
	 *
	 */
	private class AggregateRowWrapper implements IFacttableRow {
		private IAggregationResultSet aggrResultSet;

		public AggregateRowWrapper(IAggregationResultSet aggrResultSet) {
			this.aggrResultSet = aggrResultSet;
		}

		/**
		 * Not implemented. We only expect this being used in drill filter in which only
		 * level member is used.
		 */
		@Override
		public Object getMeasureValue(String measureName) throws DataException {
			throw new UnsupportedOperationException();
		}

		@Override
		public Object[] getLevelKeyValue(String dimensionName, String levelName) throws DataException, IOException {
			return this.aggrResultSet
					.getLevelKeyValue(this.aggrResultSet.getLevelIndex(new DimLevel(dimensionName, levelName)));
		}

		/**
		 * Not implemented. We only expect this being used in drill filter in which only
		 * level member is used.
		 */
		@Override
		public Object getLevelAttributeValue(String dimensionName, String levelName, String attributeName)
				throws DataException, IOException {
			throw new UnsupportedOperationException();
		}

	}
}
