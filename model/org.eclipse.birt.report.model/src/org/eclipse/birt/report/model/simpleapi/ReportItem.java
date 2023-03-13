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

package org.eclipse.birt.report.model.simpleapi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.extension.MultiRowItem;
import org.eclipse.birt.report.model.api.simpleapi.IDataBinding;
import org.eclipse.birt.report.model.api.simpleapi.IDesignElement;
import org.eclipse.birt.report.model.api.simpleapi.IHideRule;
import org.eclipse.birt.report.model.api.simpleapi.IHighlightRule;
import org.eclipse.birt.report.model.api.simpleapi.IReportItem;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;

/**
 * Implements of ReportItem
 *
 */

public class ReportItem extends ReportElement implements IReportItem {

	public ReportItem(ReportItemHandle handle) {
		super(handle);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#getDataSet
	 * ()
	 */

	public DataSetHandle getDataSet() {
		return ((ReportItemHandle) handle).getDataSet();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#setDataSet
	 * (org.eclipse.birt.report.model.api.DataSetHandle)
	 */

	public void setDataSet(DataSetHandle dataSet) throws SemanticException {
		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			((ReportItemHandle) handle).setDataSet(dataSet);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#getX()
	 */

	@Override
	public String getX() {
		DimensionHandle x = ((ReportItemHandle) handle).getX();
		return (x == null ? null : x.getStringValue());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#getY()
	 */

	@Override
	public String getY() {
		DimensionHandle y = ((ReportItemHandle) handle).getY();
		return (y == null ? null : y.getStringValue());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#setX(java
	 * .lang.String)
	 */

	@Override
	public void setX(String dimension) throws SemanticException {
		setProperty(IReportItemModel.X_PROP, dimension);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IReportItem#setX(double )
	 */

	@Override
	public void setX(double dimension) throws SemanticException {

		setProperty(IReportItemModel.X_PROP, Double.valueOf(dimension));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#setY(java
	 * .lang.String)
	 */

	@Override
	public void setY(String dimension) throws SemanticException {
		setProperty(IReportItemModel.Y_PROP, dimension);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IReportItem#setY(double )
	 */

	@Override
	public void setY(double dimension) throws SemanticException {

		setProperty(IReportItemModel.Y_PROP, Double.valueOf(dimension));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#setHeight
	 * (java.lang.String)
	 */

	@Override
	public void setHeight(String dimension) throws SemanticException {

		setProperty(IReportItemModel.HEIGHT_PROP, dimension);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#setHeight
	 * (double)
	 */

	@Override
	public void setHeight(double dimension) throws SemanticException {
		setProperty(IReportItemModel.HEIGHT_PROP, Double.valueOf(dimension));

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#setWidth
	 * (java.lang.String)
	 */

	@Override
	public void setWidth(String dimension) throws SemanticException {

		setProperty(IReportItemModel.WIDTH_PROP, dimension);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#setWidth
	 * (double)
	 */

	@Override
	public void setWidth(double dimension) throws SemanticException {

		setProperty(IReportItemModel.WIDTH_PROP, Double.valueOf(dimension));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#getWidth()
	 */

	@Override
	public String getWidth() {
		return ((ReportItemHandle) handle).getWidth().getDisplayValue();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IReportItem#getHeight()
	 */
	@Override
	public String getHeight() {
		return ((ReportItemHandle) handle).getHeight().getDisplayValue();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IReportItem#getBookmark ()
	 */

	@Override
	public String getBookmark() {
		return ((ReportItemHandle) handle).getBookmark();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IReportItem#setBookmark
	 * (java.lang.String)
	 */

	@Override
	public void setBookmark(String value) throws SemanticException {
		setProperty(IReportItemModel.BOOKMARK_PROP, value);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.engine.api.script.element.IReportItem#
	 * setTocExpression(java.lang.String)
	 */

	@Override
	public void setTocExpression(String expression) throws SemanticException {
		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			((ReportItemHandle) handle).setTocExpression(expression);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.engine.api.script.element.IReportItem#
	 * getTocExpression()
	 */

	@Override
	public String getTocExpression() {
		return ((ReportItemHandle) handle).getTocExpression();
	}

	@Override
	public String getDataBinding(String bindingName) {
		if (bindingName == null || bindingName.length() == 0) {
			return null;
		}

		Iterator iterator = ((ReportItemHandle) handle).columnBindingsIterator();
		while (iterator.hasNext()) {
			ComputedColumnHandle columnHandle = (ComputedColumnHandle) iterator.next();
			if (columnHandle.getName().equals(bindingName)) {
				return columnHandle.getExpression();
			}
		}

		return null;
	}

	@Override
	public IDataBinding[] getDataBindings() {
		Iterator iterator = ((ReportItemHandle) handle).columnBindingsIterator();
		List rList = new ArrayList();
		int count = 0;
		while (iterator.hasNext()) {
			ComputedColumnHandle columnHandle = (ComputedColumnHandle) iterator.next();
			DataBindingImpl d = new DataBindingImpl(columnHandle);
			rList.add(d);
			++count;
		}

		return (IDataBinding[]) rList.toArray(new IDataBinding[count]);
	}

	@Override
	public void removeDataBinding(String bindingName) throws SemanticException {
		if (bindingName == null || bindingName.length() == 0) {
			return;
		}

		PropertyHandle propHandle = ((ReportItemHandle) handle)
				.getPropertyHandle(IReportItemModel.BOUND_DATA_COLUMNS_PROP);
		List structureList = new ArrayList();
		Iterator iterator = propHandle.iterator();

		while (iterator.hasNext()) {
			ComputedColumnHandle columnHandle = (ComputedColumnHandle) iterator.next();
			if (bindingName.equals(columnHandle.getName())) {
				structureList.add(columnHandle);
			}
		}

		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			propHandle.removeItems(structureList);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/**
	 * Removes all data bindings.
	 *
	 * @throws SemanticException
	 */

	@Override
	public void removeDataBindings() throws SemanticException {
		PropertyHandle propHandle = handle.getPropertyHandle(IReportItemModel.BOUND_DATA_COLUMNS_PROP);

		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			propHandle.clearValue();
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/**
	 * Adds ComputedColumn.
	 *
	 * @param binding
	 * @throws SemanticException
	 */

	@Override
	public void addDataBinding(IDataBinding binding) throws SemanticException {
		if (binding == null) {
			return;
		}

		PropertyHandle propHandle = handle.getPropertyHandle(IReportItemModel.BOUND_DATA_COLUMNS_PROP);

		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			propHandle.addItem(binding.getStructure());
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/**
	 * Gets all hide rules.
	 */

	@Override
	public IHideRule[] getHideRules() {
		return HideRuleMethodUtil.getHideRules(handle);
	}

	/**
	 * Removes Hide Rule through format type.
	 */

	@Override
	public void removeHideRule(IHideRule rule) throws SemanticException {
		if (rule == null) {
			return;
		}

		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			HideRuleMethodUtil.removeHideRule(handle, rule);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/**
	 * Add HideRule.
	 *
	 * @param rule
	 * @throws SemanticException
	 */

	@Override
	public void addHideRule(IHideRule rule) throws SemanticException {
		if (rule == null) {
			return;
		}
		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			HideRuleMethodUtil.addHideRule(handle, rule);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/**
	 * Removes Hide Rules.
	 */

	@Override
	public void removeHideRules() throws SemanticException {
		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			HideRuleMethodUtil.removeHideRules(handle);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	@Override
	public void addHighlightRule(IHighlightRule rule) throws SemanticException {
		if (rule == null) {
			return;
		}
		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			HighlightRuleMethodUtil.addHighlightRule(handle, rule);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	@Override
	public IHighlightRule[] getHighlightRules() {
		return HighlightRuleMethodUtil.getHighlightRules(handle);
	}

	@Override
	public void removeHighlightRule(IHighlightRule rule) throws SemanticException {
		if (rule == null) {
			return;
		}
		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			HighlightRuleMethodUtil.removeHighlightRule(handle, rule);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	@Override
	public void removeHighlightRules() throws SemanticException {
		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			HighlightRuleMethodUtil.removeHighlightRules(handle);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportItem#setCurrentView
	 * (org.eclipse.birt.report.model.api.simpleapi.IDesignElement)
	 */

	@Override
	public void setCurrentView(IDesignElement viewElement) throws SemanticException {
		DesignElementHandle tmpElement = null;
		if (viewElement instanceof MultiRowItem) {
			DesignElement reportItem = (DesignElement) ((MultiRowItem) viewElement).getExtensionElement();
			tmpElement = reportItem.handle;
		} else if (viewElement != null) {
			tmpElement = ((DesignElement) viewElement).handle;
		}

		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			((ReportItemHandle) handle).setCurrentView(tmpElement);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}
}
