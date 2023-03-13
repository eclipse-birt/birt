/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.report.item.crosstab.core.script.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.item.crosstab.core.ILevelViewConstants;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.script.IMeasure;
import org.eclipse.birt.report.model.api.FilterConditionElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.api.simpleapi.IFilterConditionElement;
import org.eclipse.birt.report.model.api.simpleapi.ISimpleElementFactory;
import org.eclipse.birt.report.model.api.simpleapi.SimpleElementFactory;

/**
 * MeasureImpl
 */
public class MeasureImpl implements IMeasure {

	private MeasureHandle mh;
	private MeasureViewHandle mv;

	// TODO support computed measure view

	public MeasureImpl(MeasureViewHandle mv) {
		this.mv = mv;

		if (mv != null) {
			mh = mv.getCubeMeasure();
		}
	}

	@Override
	public String getFunctionName() {
		if (mh != null) {
			return mh.getFunction();
		}
		return null;
	}

	@Override
	public String getMeasureExpression() {
		if (mh != null) {
			return mh.getMeasureExpression();
		}
		return null;
	}

	@Override
	public String getName() {
		if (mh != null) {
			return mh.getName();
		}
		if (mv != null) {
			return mv.getCubeMeasureName();
		}
		return null;
	}

	@Override
	public void addFilterCondition(IFilterConditionElement filter) throws SemanticException {
		if (mh != null) {
			FilterConditionElementHandle fceh = mv.getModelHandle().getElementFactory().newFilterConditionElement();

			fceh.setExpr(filter.getExpr());
			fceh.setFilterTarget(filter.getFilterTarget());
			fceh.setOperator(filter.getOperator());
			fceh.setValue1(filter.getValue1List());
			fceh.setValue2(filter.getValue2());
			fceh.setOptional(filter.isOptional());

			mv.getModelHandle().add(ILevelViewConstants.FILTER_PROP, fceh);
		}
	}

	@Override
	public List<IFilterConditionElement> getFilterConditions() {
		if (mh != null) {
			List<IFilterConditionElement> filters = new ArrayList<>();
			ISimpleElementFactory factory = SimpleElementFactory.getInstance();

			for (Iterator itr = mv.filtersIterator(); itr.hasNext();) {
				FilterConditionElementHandle feh = (FilterConditionElementHandle) itr.next();

				filters.add((IFilterConditionElement) factory.getElement(feh));
			}

			if (filters.size() > 0) {
				return filters;
			}
		}

		return Collections.EMPTY_LIST;
	}

	@Override
	public void removeAllFilterConditions() throws SemanticException {
		if (mh != null) {
			mv.getModelHandle().setProperty(ILevelViewConstants.FILTER_PROP, null);
		}
	}

	@Override
	public void removeFilterCondition(IFilterConditionElement filter) throws SemanticException {
		if (mh == null || filter == null) {
			return;
		}

		FilterConditionElementHandle handle = null;

		for (Iterator itr = mv.filtersIterator(); itr.hasNext();) {
			FilterConditionElementHandle feh = (FilterConditionElementHandle) itr.next();

			if (equalFilter(feh, filter)) {
				handle = feh;
				break;
			}
		}

		if (handle != null) {
			mv.getModelHandle().drop(ILevelViewConstants.FILTER_PROP, handle);
		}
	}

	private boolean equalFilter(FilterConditionElementHandle fceh, IFilterConditionElement ifce) {
		List val1 = fceh.getValue1List();
		List val2 = ifce.getValue1List();

		if (val1 == null || val1.isEmpty()) {
			if (val2 != null && !val2.isEmpty()) {
				return false;
			}
		} else if (!val1.equals(val2)) {
			return false;
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

}
