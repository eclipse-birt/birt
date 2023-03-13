/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.engine.script.internal.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.IAction;
import org.eclipse.birt.report.engine.api.script.element.IDataBinding;
import org.eclipse.birt.report.engine.api.script.element.IDataItem;
import org.eclipse.birt.report.engine.api.script.element.IDataSet;
import org.eclipse.birt.report.engine.api.script.element.IDataSource;
import org.eclipse.birt.report.engine.api.script.element.IDesignElement;
import org.eclipse.birt.report.engine.api.script.element.IDynamicText;
import org.eclipse.birt.report.engine.api.script.element.IFilterCondition;
import org.eclipse.birt.report.engine.api.script.element.IGrid;
import org.eclipse.birt.report.engine.api.script.element.IHideRule;
import org.eclipse.birt.report.engine.api.script.element.IHighlightRule;
import org.eclipse.birt.report.engine.api.script.element.IImage;
import org.eclipse.birt.report.engine.api.script.element.ILabel;
import org.eclipse.birt.report.engine.api.script.element.IList;
import org.eclipse.birt.report.engine.api.script.element.IMasterPage;
import org.eclipse.birt.report.engine.api.script.element.IReportDesign;
import org.eclipse.birt.report.engine.api.script.element.IReportElement;
import org.eclipse.birt.report.engine.api.script.element.ISortCondition;
import org.eclipse.birt.report.engine.api.script.element.ITable;
import org.eclipse.birt.report.engine.api.script.element.ITextItem;
import org.eclipse.birt.report.engine.script.internal.ElementUtil;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;

/**
 * ReportDesign
 */
public class ReportDesign extends DesignElement implements IReportDesign {

	public ReportDesign(org.eclipse.birt.report.model.api.simpleapi.IReportDesign reportElementImpl) {
		super(reportElementImpl);
	}

	public ReportDesign(ReportDesignHandle report) {
		super(report);
	}

	@Override
	public IDataSet getDataSet(String name) {
		return new DataSet(
				((org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl).getDataSet(name));
	}

	@Override
	public IDataSource getDataSource(String name) {
		return new DataSource(
				((org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl).getDataSource(name));
	}

	@Override
	public IReportElement getReportElement(String name) {
		org.eclipse.birt.report.model.api.simpleapi.IReportElement tmpElement = ((org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl)
				.getReportElement(name);

		IDesignElement retElement = ElementUtil.getElement(tmpElement);
		if (retElement instanceof IReportElement) {
			return (IReportElement) retElement;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.engine.api.script.element.IReportDesign#
	 * getReportElementByID(long)
	 */
	@Override
	public IReportElement getReportElementByID(long id) {
		org.eclipse.birt.report.model.api.simpleapi.IReportElement tmpElement = ((org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl)
				.getReportElementByID(id);

		IDesignElement retElement = ElementUtil.getElement(tmpElement);
		if (retElement instanceof IReportElement) {
			return (IReportElement) retElement;
		}

		return null;
	}

	public IDataItem getDataItem(String name) {
		return new DataItem(
				((org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl).getDataItem(name));
	}

	@Override
	public IGrid getGrid(String name) {
		return new Grid(((org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl).getGrid(name));
	}

	@Override
	public IImage getImage(String name) {
		return new Image(
				((org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl).getImage(name));
	}

	@Override
	public ILabel getLabel(String name) {
		return new Label(
				((org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl).getLabel(name));
	}

	@Override
	public IList getList(String name) {
		return new List(((org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl).getList(name));
	}

	@Override
	public ITable getTable(String name) {
		return new Table(
				((org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl).getTable(name));
	}

	@Override
	public IDynamicText getDynamicText(String name) {
		return new DynamicText(
				((org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl).getDynamicText(name));
	}

	public ITextItem getTextItem(String name) {
		return new TextItem(
				((org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl).getTextItem(name));
	}

	@Override
	public void setDisplayNameKey(String displayNameKey) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl)
					.setDisplayNameKey(displayNameKey);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	@Override
	public String getDisplayNameKey() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl).getDisplayNameKey();
	}

	@Override
	public void setDisplayName(String displayName) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl).setDisplayName(displayName);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	@Override
	public String getDisplayName() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl).getDisplayName();
	}

	@Override
	public IMasterPage getMasterPage(String name) {
		return new MasterPage(
				((org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl).getMasterPage(name));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportDesign#getTheme
	 * ()
	 */
	@Override
	public String getTheme() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl).getTheme();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportDesign#setTheme
	 * (java.lang.String)
	 */
	@Override
	public void setTheme(String theme) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl).setTheme(theme);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.engine.api.script.element.IReportDesign#
	 * createHideRule()
	 */
	@Override
	public IHideRule createHideRule() {
		HideRule r = new HideRule();
		IHideRule rule = new HideRuleImpl(r);
		return rule;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.engine.api.script.element.IReportDesign#
	 * createFilterCondition()
	 */
	@Override
	public IFilterCondition createFilterCondition() {
		FilterCondition c = new FilterCondition();
		IFilterCondition condition = new FilterConditionImpl(c);
		return condition;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.engine.api.script.element.IReportDesign#
	 * createDataBinding()
	 */
	@Override
	public IDataBinding createDataBinding() {
		ComputedColumn c = new ComputedColumn();
		IDataBinding binding = new DataBindingImpl(c);
		return binding;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.engine.api.script.element.IReportDesign#
	 * createHighLightRule()
	 */
	@Override
	public IHighlightRule createHighLightRule() {
		HighlightRule h = new HighlightRule();
		IHighlightRule rule = new HighlightRuleImpl(h);
		return rule;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.engine.api.script.element.IReportDesign#
	 * createSortCondition()
	 */
	@Override
	public ISortCondition createSortCondition() {
		SortKey s = new SortKey();
		ISortCondition sort = new SortConditionImpl(s);
		return sort;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IReportDesign#createAction(
	 * )
	 */
	@Override
	public IAction createAction() {
		IAction action = new ActionImpl();
		return action;
	}
}
