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
import org.eclipse.birt.report.engine.api.script.element.IDataBinding;
import org.eclipse.birt.report.engine.api.script.element.IDesignElement;
import org.eclipse.birt.report.engine.api.script.element.IHideRule;
import org.eclipse.birt.report.engine.api.script.element.IHighlightRule;
import org.eclipse.birt.report.engine.api.script.element.IReportItem;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.simpleapi.SimpleElementFactory;

/**
 * Implements of ReportItem
 */

public class ReportItem extends ReportElement implements IReportItem {

	public ReportItem(ReportItemHandle handle) {
		super(handle);
	}

	public ReportItem(org.eclipse.birt.report.model.api.simpleapi.IReportItem reportElementImpl) {
		super(reportElementImpl);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#getX()
	 */

	@Override
	public String getX() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IReportItem) designElementImpl).getX();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#getY()
	 */

	@Override
	public String getY() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IReportItem) designElementImpl).getY();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IReportItem#setX(java.lang.
	 * String)
	 */

	@Override
	public void setX(String dimension) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IReportItem) designElementImpl).setX(dimension);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IReportItem#setX(double)
	 */

	@Override
	public void setX(double dimension) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IReportItem) designElementImpl).setX(dimension);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IReportItem#setY(java.lang.
	 * String)
	 */

	@Override
	public void setY(String dimension) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IReportItem) designElementImpl).setY(dimension);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IReportItem#setY(double)
	 */

	@Override
	public void setY(double dimension) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IReportItem) designElementImpl).setY(dimension);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IReportItem#setHeight(java.
	 * lang.String)
	 */

	@Override
	public void setHeight(String dimension) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IReportItem) designElementImpl).setHeight(dimension);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#setHeight(
	 * double)
	 */

	@Override
	public void setHeight(double dimension) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IReportItem) designElementImpl).setHeight(dimension);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IReportItem#setWidth(java.
	 * lang.String)
	 */

	@Override
	public void setWidth(String dimension) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IReportItem) designElementImpl).setWidth(dimension);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#setWidth(
	 * double)
	 */

	@Override
	public void setWidth(double dimension) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IReportItem) designElementImpl).setWidth(dimension);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#getWidth()
	 */

	@Override
	public String getWidth() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IReportItem) designElementImpl).getWidth();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IReportItem#getHeight()
	 */
	@Override
	public String getHeight() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IReportItem) designElementImpl).getHeight();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IReportItem#getBookmark()
	 */

	@Override
	public String getBookmark() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IReportItem) designElementImpl).getBookmark();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IReportItem#setBookmark(
	 * java.lang.String)
	 */

	@Override
	public void setBookmark(String value) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IReportItem) designElementImpl).setBookmark(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#
	 * setTocExpression(java.lang.String)
	 */

	@Override
	public void setTocExpression(String expression) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IReportItem) designElementImpl).setTocExpression(expression);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#
	 * getTocExpression()
	 */

	@Override
	public String getTocExpression() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IReportItem) designElementImpl).getTocExpression();
	}

	@Override
	public String getDataBinding(String bindingName) {
		return ((org.eclipse.birt.report.model.api.simpleapi.IReportItem) designElementImpl)
				.getDataBinding(bindingName);
	}

	@Override
	public IDataBinding[] getDataBindings() {
		org.eclipse.birt.report.model.api.simpleapi.IDataBinding[] values = ((org.eclipse.birt.report.model.api.simpleapi.IReportItem) designElementImpl)
				.getDataBindings();
		IDataBinding[] dataBindings = new IDataBinding[values.length];

		for (int i = 0; i < values.length; i++) {
			dataBindings[i] = new DataBindingImpl(values[i]);
		}
		return dataBindings;
	}

	@Override
	public void removeDataBinding(String bindingName) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IReportItem) designElementImpl)
					.removeDataBinding(bindingName);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/**
	 * Removes all data bindings.
	 *
	 * @throws ScriptException
	 */

	@Override
	public void removeDataBindings() throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IReportItem) designElementImpl).removeDataBindings();
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/**
	 * Adds ComputedColumn.
	 *
	 * @param binding
	 * @throws ScriptException
	 */

	@Override
	public void addDataBinding(IDataBinding binding) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IReportItem) designElementImpl).addDataBinding(
					SimpleElementFactory.getInstance().createDataBinding((ComputedColumn) binding.getStructure()));
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/**
	 * Gets all hide rules.
	 */

	@Override
	public IHideRule[] getHideRules() {
		org.eclipse.birt.report.model.api.simpleapi.IHideRule[] values = ((org.eclipse.birt.report.model.api.simpleapi.IReportItem) designElementImpl)
				.getHideRules();
		IHideRule[] hideRules = new IHideRule[values.length];

		for (int i = 0; i < values.length; i++) {
			hideRules[i] = new HideRuleImpl(values[i]);
		}
		return hideRules;
	}

	/**
	 * Removes Hide Rule through format type.
	 */

	@Override
	public void removeHideRule(IHideRule rule) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IReportItem) designElementImpl)
					.removeHideRule(SimpleElementFactory.getInstance().createHideRule((HideRule) rule.getStructure()));
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/**
	 * Add HideRule.
	 *
	 * @param rule
	 * @throws ScriptException
	 */

	@Override
	public void addHideRule(IHideRule rule) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IReportItem) designElementImpl)
					.addHideRule(SimpleElementFactory.getInstance().createHideRule((HideRule) rule.getStructure()));
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/**
	 * Removes Hide Rules.
	 */

	@Override
	public void removeHideRules() throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IReportItem) designElementImpl).removeHideRules();
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	@Override
	public void addHighlightRule(IHighlightRule rule) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IReportItem) designElementImpl).addHighlightRule(
					SimpleElementFactory.getInstance().createHighlightRule((HighlightRule) rule.getStructure()));
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	@Override
	public IHighlightRule[] getHighlightRules() {
		org.eclipse.birt.report.model.api.simpleapi.IHighlightRule[] values = ((org.eclipse.birt.report.model.api.simpleapi.IReportItem) designElementImpl)
				.getHighlightRules();
		IHighlightRule[] highlightRules = new IHighlightRule[values.length];

		for (int i = 0; i < values.length; i++) {
			highlightRules[i] = new HighlightRuleImpl(values[i]);
		}
		return highlightRules;
	}

	@Override
	public void removeHighlightRule(IHighlightRule rule) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IReportItem) designElementImpl).removeHighlightRule(
					SimpleElementFactory.getInstance().createHighlightRule((HighlightRule) rule.getStructure()));
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	@Override
	public void removeHighlightRules() throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IReportItem) designElementImpl).removeHighlightRules();
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IReportItem#setCurrentView(
	 * org.eclipse.birt.report.engine.api.script.element.IDesignElement)
	 */

	@Override
	public void setCurrentView(IDesignElement viewElement) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IReportItem) designElementImpl)
					.setCurrentView(((DesignElement) viewElement).designElementImpl);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

}
