/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.core.script.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.item.crosstab.core.ILevelViewConstants;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.script.ILevel;
import org.eclipse.birt.report.model.api.FilterConditionElementHandle;
import org.eclipse.birt.report.model.api.SortElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.simpleapi.IFilterConditionElement;
import org.eclipse.birt.report.model.api.simpleapi.ISimpleElementFactory;
import org.eclipse.birt.report.model.api.simpleapi.ISortElement;
import org.eclipse.birt.report.model.api.simpleapi.SimpleElementFactory;

/**
 * LevelImpl
 */
public class LevelImpl implements ILevel {

	private LevelViewHandle lv;
	private LevelHandle lh;

	public LevelImpl(LevelViewHandle lv) {
		this.lv = lv;
		if (lv != null) {
			lh = lv.getCubeLevel();
		}
	}

	public String getDimensionName() {
		if (lh != null && lh.getContainer() != null) {
			DimensionHandle dh = (DimensionHandle) lh.getContainer().getContainer();
			if (dh != null) {
				return dh.getName();
			}
		}
		return null;
	}

	public String getName() {
		if (lh != null) {
			return lh.getName();
		}
		return null;
	}

	public void addFilterCondition(IFilterConditionElement filter) throws SemanticException {
		FilterConditionElementHandle fceh = lv.getModelHandle().getElementFactory().newFilterConditionElement();

		fceh.setExpr(filter.getExpr());
		fceh.setFilterTarget(filter.getFilterTarget());
		fceh.setOperator(filter.getOperator());
		fceh.setValue1(filter.getValue1List());
		fceh.setValue2(filter.getValue2());
		fceh.setOptional(filter.isOptional());

		lv.getModelHandle().add(ILevelViewConstants.FILTER_PROP, fceh);
	}

	public List<IFilterConditionElement> getFilterConditions() {
		List<IFilterConditionElement> filters = new ArrayList<IFilterConditionElement>();
		ISimpleElementFactory factory = SimpleElementFactory.getInstance();

		for (Iterator itr = lv.filtersIterator(); itr.hasNext();) {
			FilterConditionElementHandle feh = (FilterConditionElementHandle) itr.next();

			filters.add((IFilterConditionElement) factory.getElement(feh));
		}

		if (filters.size() > 0) {
			return filters;
		}

		return Collections.EMPTY_LIST;
	}

	public void removeAllFilterConditions() throws SemanticException {
		lv.getModelHandle().setProperty(ILevelViewConstants.FILTER_PROP, null);
	}

	public void removeFilterCondition(IFilterConditionElement filter) throws SemanticException {
		if (filter == null) {
			return;
		}

		FilterConditionElementHandle handle = null;

		for (Iterator itr = lv.filtersIterator(); itr.hasNext();) {
			FilterConditionElementHandle feh = (FilterConditionElementHandle) itr.next();

			if (equalFilter(feh, filter)) {
				handle = feh;
				break;
			}
		}

		if (handle != null) {
			lv.getModelHandle().drop(ILevelViewConstants.FILTER_PROP, handle);
		}
	}

	public void addSortCondition(ISortElement sort) throws SemanticException {
		SortElementHandle seh = lv.getModelHandle().getElementFactory().newSortElement();

		seh.setDirection(sort.getDirection());
		seh.setKey(sort.getKey());

		lv.getModelHandle().add(ILevelViewConstants.SORT_PROP, seh);
	}

	public List<ISortElement> getSortConditions() {
		List<ISortElement> sorts = new ArrayList<ISortElement>();
		ISimpleElementFactory factory = SimpleElementFactory.getInstance();

		for (Iterator itr = lv.sortsIterator(); itr.hasNext();) {
			SortElementHandle seh = (SortElementHandle) itr.next();

			sorts.add((ISortElement) factory.getElement(seh));
		}

		if (sorts.size() > 0) {
			return sorts;
		}

		return Collections.EMPTY_LIST;
	}

	public void removeAllSortConditions() throws SemanticException {
		lv.getModelHandle().setProperty(ILevelViewConstants.SORT_PROP, null);
	}

	public void removeSortCondition(ISortElement sort) throws SemanticException {
		if (sort == null) {
			return;
		}

		SortElementHandle handle = null;
		for (Iterator itr = lv.sortsIterator(); itr.hasNext();) {
			SortElementHandle seh = (SortElementHandle) itr.next();

			if (equalSort(seh, sort)) {
				handle = seh;
				break;
			}
		}

		if (handle != null) {
			lv.getModelHandle().drop(ILevelViewConstants.SORT_PROP, handle);
		}
	}

	private boolean equalSort(SortElementHandle seh, ISortElement ise) {
		return equalString(seh.getDirection(), ise.getDirection()) && equalString(seh.getKey(), ise.getKey());
	}

	private boolean equalFilter(FilterConditionElementHandle fceh, IFilterConditionElement ifce) {
		List val1 = fceh.getValue1List();
		List val2 = ifce.getValue1List();

		if (val1 == null || val1.isEmpty()) {
			if (val2 != null && !val2.isEmpty()) {
				return false;
			}
		} else {
			if (!val1.equals(val2)) {
				return false;
			}
		}

		return (fceh.isOptional() == ifce.isOptional()) && equalString(fceh.getExpr(), ifce.getExpr())
				&& equalString(fceh.getFilterTarget(), ifce.getFilterTarget())
				&& equalString(fceh.getOperator(), ifce.getOperator())
				&& equalString(fceh.getValue2(), ifce.getValue2());
	}

	private boolean equalString(String s1, String s2) {
		if (s1 == null) {
			return s2 == null;
		}
		return s1.equals(s2);
	}

	public String getPageBreakAfter() {
		return lv.getPageBreakAfter();
	}

	public String getPageBreakBefore() {
		return lv.getPageBreakBefore();
	}

	public String getPageBreakInside() {
		return lv.getPageBreakInside();
	}

	public int getPageBreakInterval() {
		return lv.getPageBreakInterval();
	}

	public void setPageBreakAfter(String value) throws SemanticException {
		lv.setPageBreakAfter(value);
	}

	public void setPageBreakBefore(String value) throws SemanticException {
		lv.setPageBreakBefore(value);
	}

	public void setPageBreakInside(String value) throws SemanticException {
		lv.setPageBreakInside(value);
	}

	public void setPageBreakInterval(int value) throws SemanticException {
		lv.setPageBreakInterval(value);
	}
}
