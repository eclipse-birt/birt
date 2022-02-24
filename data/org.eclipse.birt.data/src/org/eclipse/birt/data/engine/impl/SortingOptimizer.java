/*
 *************************************************************************
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
 *
 *************************************************************************
 */
package org.eclipse.birt.data.engine.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IColumnDefinition;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IQueryExecutionHints;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.core.DataException;

public class SortingOptimizer {
	private List<ISortDefinition> sortHints;
	private List<ISortDefinition> sortings;
	private List<IGroupDefinition> groups;
	private IQueryExecutionHints queryExeHint;
	private SortMatcher hintsMatcher;
	private boolean optimizeGroupSorting = false;
	private boolean optimizeQuerySorting = false;
	private IBaseQueryDefinition baseQuery = null;
	private IBaseDataSetDesign dataSet = null;

	public SortingOptimizer(IBaseDataSetDesign dataSet, IBaseQueryDefinition query) {
		if (dataSet == null || query == null)
			return;
		this.sortHints = dataSet.getSortHints();
		this.dataSet = dataSet;
		this.baseQuery = query;
		this.groups = query.getGroups();
		this.sortings = query.getSorts();
		this.queryExeHint = query.getQueryExecutionHints();
		hintsMatcher = new SortMatcher(sortHints);
	}

	public boolean acceptGroupSorting() {
		// No sort hint and no group, no optimize
		if (sortHints == null || groups == null)
			return false;

		// No group sorting, no optimize
		if (queryExeHint != null && !queryExeHint.doSortBeforeGrouping())
			return false;

		// Contains group interval, no optimize
		for (Object o : groups) {
			IGroupDefinition g = (IGroupDefinition) o;
			if (g.getInterval() != IGroupDefinition.NO_INTERVAL)
				return false;
		}

		if (sortings != null) {
			// Merge group key sorting and query sorting into one sorting sequence
			GroupSortingCaculator calc = new GroupSortingCaculator(groups);
			List<?> sorts = calc.getSortingSequence(sortings, new SortDefnMatchInfo());
			if (hintsMatcher.match(sorts, new GroupDefnSortDefnMatchInfo())) {
				optimizeGroupSorting = true;
				optimizeQuerySorting = true;
			}
		} else {
			if (hintsMatcher.match(groups, new GroupDefnMatchInfo()))
				optimizeGroupSorting = true;
		}

		return optimizeGroupSorting;
	}

	public boolean acceptQuerySorting() throws DataException {
		if (sortHints == null || sortings == null) {
			return false;
		}

		if (optimizeQuerySorting)
			return true;

		if (hintsMatcher.match(this.sortings, new SortDefnMatchInfo()))
			return true;

		return false;
	}

	class SortMatcher {
		private List<?> hints = null;
		private MatchInfo hInfo = null;

		public SortMatcher(List<?> sortHints, MatchInfo info) {
			this.hints = sortHints;
			hInfo = info;
		}

		public SortMatcher(List<?> sortHints) {
			this(sortHints, new SortHintMatchInfo());
		}

		public boolean match(List<?> sorts, MatchInfo util) {
			if (hints == null || hints.size() == 0 || hints.size() < sorts.size())
				return false;

			int pos = 0;
			for (; pos < hints.size() && pos < sorts.size();) {
				String hKey = hInfo.getKey(hints.get(pos));
				int hDirection = hInfo.getDirection(hints.get(pos));
				String mKey = util.getKey(sorts.get(pos));
				int mDirection = util.getDirection(sorts.get(pos));
				if (hKey != null && mKey != null && hKey.equals(mKey) && hDirection == mDirection) {
					pos++;
				} else {
					break;
				}
			}

			// For sortHints and sorting definitions:
			// 1. SortHints contains all sorts conditions.
			// 2. Sorts sequence match sortHints start from the first sort hint.
			if (pos == sorts.size())
				return true;

			return false;
		}
	}

	class GroupSortingCaculator {
		private List<?> base = null;
		private MatchInfo hInfo = null;
		private List<?> compareSorts = null;

		public GroupSortingCaculator(List<?> sortHints, MatchInfo info) {
			this.base = sortHints;
			hInfo = info;
		}

		public GroupSortingCaculator(List<?> sortHints) {
			this(sortHints, new GroupDefnMatchInfo());
		}

		private void caculate(List<?> sorts, MatchInfo util) {
			if (base == null || base.size() == 0)
				return;

			int pos = 0;
			int j = 0;
			for (; pos < base.size() && j < sorts.size();) {
				String hKey = hInfo.getKey(base.get(pos));
				int hDirection = hInfo.getDirection(base.get(pos));
				String mKey = util.getKey(sorts.get(j));
				int mDirection = util.getDirection(sorts.get(j));

				if (hKey != null && mKey != null && hKey.equals(mKey) && hDirection == mDirection) {
					pos++;
					j++;
				} else {
					pos++;
				}
			}

			compareSorts = sorts.subList(j, sorts.size());
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public List getSortingSequence(List<?> querySorts, MatchInfo util) {
			caculate(querySorts, util);

			ArrayList sorts = new ArrayList();
			Iterator it = base.iterator();
			while (it.hasNext()) {
				sorts.add(it.next());
			}

			if (compareSorts != null) {
				it = compareSorts.iterator();
				while (it.hasNext()) {
					sorts.add(it.next());
				}
			}
			return sorts;
		}
	}

	interface MatchInfo {
		public String getKey(Object o);

		public int getDirection(Object o);
	}

	class SortHintMatchInfo implements MatchInfo {
		public String getKey(Object o) {
			ISortDefinition sort = (ISortDefinition) o;
			String key = sort.getColumn();
			if (key == null)
				key = sort.getExpression().getText();
			return key;
		}

		public int getDirection(Object o) {
			ISortDefinition sort = (ISortDefinition) o;
			return sort.getSortDirection();
		}
	}

	class SortDefnMatchInfo implements MatchInfo {
		public String getKey(Object o) {
			ISortDefinition sort = (ISortDefinition) o;

			// No matching while sorting with local and strength.
			if (sort.getSortLocale() != null || sort.getSortStrength() != ISortDefinition.ASCII_SORT_STRENGTH)
				return null;

			String sortKey = sort.getColumn();
			if (sortKey == null)
				sortKey = sort.getExpression().getText();
			else
				sortKey = getColumnRefExpression(sortKey);

			return getResolvedExpression(sortKey);
		}

		public int getDirection(Object o) {
			ISortDefinition sort = (ISortDefinition) o;
			return sort.getSortDirection();
		}
	}

	class GroupDefnMatchInfo implements MatchInfo {
		public String getKey(Object o) {
			IGroupDefinition grp = (IGroupDefinition) o;
			String rowExpr = getGroupKeyExpression(grp);
			return getResolvedExpression(rowExpr);
		}

		public int getDirection(Object o) {
			IGroupDefinition grp = (IGroupDefinition) o;
			return grp.getSortDirection();
		}
	}

	class GroupDefnSortDefnMatchInfo implements MatchInfo {
		private MatchInfo grpInfo = new GroupDefnMatchInfo();
		private MatchInfo sortInfo = new SortDefnMatchInfo();

		public String getKey(Object o) {
			if (o instanceof IGroupDefinition)
				return grpInfo.getKey(o);
			if (o instanceof ISortDefinition)
				return sortInfo.getKey(o);
			return null;
		}

		public int getDirection(Object o) {
			if (o instanceof IGroupDefinition)
				return grpInfo.getDirection(o);
			if (o instanceof ISortDefinition)
				return sortInfo.getDirection(o);
			return IGroupDefinition.NO_SORT;
		}
	}

	private String resolveDataSetExpr(String rowExpr) throws DataException {
		if (rowExpr == null)
			return null;

		String dataSetExpr = null;
		try {
			String bindingName = ExpressionUtil.getColumnBindingName(rowExpr);
			Object binding = this.baseQuery.getBindings().get(bindingName);
			if (binding != null) {
				IBaseExpression expr = ((IBinding) binding).getExpression();
				if (expr != null && expr instanceof IScriptExpression) {
					dataSetExpr = ((IScriptExpression) expr).getText();
					if (dataSetExpr != null) {
						return resolveDataSetExpr(dataSetExpr);
					}
				}
				return dataSetExpr;
			} else
				return rowExpr; // Already resolved.
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}
	}

	@SuppressWarnings("rawtypes")
	private String resolveColumnAlias(String columnAlias) {
		List rsHints = this.dataSet.getResultSetHints();
		if (rsHints == null)
			return null;

		String resolved = null;
		IColumnDefinition col = null;
		Iterator itr = rsHints.iterator();
		while (itr.hasNext()) {
			col = (IColumnDefinition) itr.next();
			if (col.getAlias() != null && col.getAlias().equals(columnAlias)) {
				resolved = col.getColumnName();
				break;
			}
		}
		return resolved;
	}

	private String getResolvedExpression(String rowExpr) {
		String expr = null;
		try {
			expr = resolveDataSetExpr(rowExpr);
			if (expr != null) {
				String bindingName = ExpressionUtil.getColumnName(expr);
				String column = resolveColumnAlias(bindingName);
				if (column != null) // Binding name is a column alias
					expr = ExpressionUtil.createDataSetRowExpression(column);
			}
		} catch (BirtException ignore) {
			expr = null;
		}
		return expr;
	}

	private String getGroupKeyExpression(IGroupDefinition src) {
		String expr = src.getKeyColumn();
		if (expr == null) {
			expr = src.getKeyExpression();
		} else {
			expr = getColumnRefExpression(expr);
		}
		return expr;
	}

	/**
	 *
	 * @param expr
	 * @return
	 */
	private String getColumnRefExpression(String expr) {
		return ExpressionUtil.createJSRowExpression(expr);
	}
}
