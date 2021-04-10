/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api.simpleapi;

import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FilterConditionElementHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.HideRuleHandle;
import org.eclipse.birt.report.model.api.HighlightRuleHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.SortElementHandle;
import org.eclipse.birt.report.model.api.SortKeyHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;

/**
 * The factory class to create scriptable objects.
 */

public interface ISimpleElementFactory {

	final int MULTI_ROW_ITEM = 0;

	final int SIMPLE_ROW_ITEM = 1;

	/**
	 * Creates the scriptable object for the corresponding element handles.
	 * 
	 * @param handle the element handle
	 * @param type   the expected data row type. Can be <code>MULTI_ROW_ITEM</code>
	 *               or <code>EMPTY_ROW_ITEM</code>
	 * @return the scriptable object
	 */

	public IReportItem wrapExtensionElement(ExtendedItemHandle handle, int type);

	/**
	 * Create <code>IHideRule</code> instance
	 * 
	 * @return IHideRule
	 */

	public IHideRule createHideRule();

	/**
	 * Create <code>IHideRule</code> instance
	 * 
	 * @param rule the structure to create corresponding IHideRule instance.
	 * @return IHideRule
	 */

	public IHideRule createHideRule(HideRule rule);

	/**
	 * Create <code>IHideRule</code> instance
	 * 
	 * @param handle the element handle to create corresponding IHideRule instance.
	 * @return IHideRule
	 */

	public IHideRule createHideRule(HideRuleHandle handle);

	/**
	 * Creates the filter structure.
	 * 
	 * @return the filter
	 */

	public IFilterCondition createFilterCondition();

	/**
	 * Creates the IFilterCondition instance.
	 * 
	 * @param condition the structure to create corresponding IFilterCondition
	 *                  instance.
	 * @return the IFilterCondition instance
	 */

	public IFilterCondition createFilterCondition(FilterCondition condition);

	/**
	 * Creates the IFilterCondition instance.
	 * 
	 * @param handle the element handle to create corresponding IFilterCondition
	 *               instance.
	 * @return the IFilterCondition instance
	 */

	public IFilterCondition createFilterCondition(FilterConditionHandle handle);

	/**
	 * Creates the data biinding structure.
	 * 
	 * @return the data binding
	 */

	public IDataBinding createDataBinding();

	/**
	 * Creates the data biinding structure.
	 * 
	 * @param columnHandle the computed column handle
	 * @return the data binding
	 */

	public IDataBinding createDataBinding(ComputedColumnHandle columnHandle);

	/**
	 * Creates the data biinding structure.
	 * 
	 * @param column the computed column
	 * @return the data binding
	 */

	public IDataBinding createDataBinding(ComputedColumn column);

	/**
	 * Creates the sort structure.
	 * 
	 * @return the sort
	 */

	public ISortCondition createSortCondition();

	/**
	 * Creates the sort structure.
	 * 
	 * @param sort the structure to create corresponding ISortCondition instance.
	 * @return the sort
	 */

	public ISortCondition createSortCondition(SortKey sort);

	/**
	 * Creates the sort structure.
	 * 
	 * @param handle the element handle that holds the SortCondition structure
	 * @return the sort
	 */

	public ISortCondition createSortCondition(SortKeyHandle sortHandle);

	/**
	 * Creates the action structure.
	 * 
	 * @param action the structure handle
	 * @param handle the element handle that holds the action structure
	 * @return the action
	 */

	public IAction createAction(ActionHandle action, ReportItemHandle handle);

	/**
	 * Create the action structure.
	 * 
	 * @return the action.
	 */
	public IAction createAction();

	/**
	 * Creates the IDesignElement instance.
	 * 
	 * @param handle the element handle to create corresponding IDesignElement
	 *               instance.
	 * @return the IDesignElement instance
	 */

	public IDesignElement getElement(DesignElementHandle handle);

	/**
	 * Creates the IDataSet instance.
	 * 
	 * @param handle the element handle to create corresponding IDataSet instance.
	 * @return the IDataSet instance
	 */

	public IDataSet createDataSet(DataSetHandle handle);

	/**
	 * Creates the IResultSetColumn instance.
	 * 
	 * @param handle the element handle to create corresponding IResultSetColumn
	 *               instance.
	 * @return the IResultSetColumn instance
	 */

	public IResultSetColumn createResultSetColumn(ResultSetColumnHandle columnHandle);

	/**
	 * Creates the IResultSetColumn instance.
	 * 
	 * @return the IResultSetColumn instance
	 */

	public IResultSetColumn createResultSetColumn();

	/**
	 * Creates the IDataSource instance.
	 * 
	 * @param handle the element handle to create corresponding IDataSource
	 *               instance.
	 * @return the IDataSource instance
	 */

	public IDataSource createDataSource(DataSourceHandle handle);

	/**
	 * Create <code>IHighlightRule</code> instance
	 * 
	 * @return IHighlightRule
	 */

	public IHighlightRule createHighlightRule();

	/**
	 * Create <code>IHighlightRule</code> instance
	 * 
	 * @param highlightRule the structure to create corresponding IHighlightRule
	 *                      instance.
	 * @return IHighlightRule
	 */

	public IHighlightRule createHighlightRule(HighlightRule highlightRule);

	/**
	 * Create <code>IHighlightRule</code> instance
	 * 
	 * @param handle the element handle to create corresponding IHighlightRule
	 *               instance.
	 * @return IHighlightRule
	 */

	public IHighlightRule createHighlightRule(HighlightRuleHandle handle);

	/**
	 * Create <code>IStyle</code> instance
	 * 
	 * @param handle the element handle to create corresponding IStyle instance.
	 * @return IStyle
	 */

	public IStyle createStyle(StyleHandle style);

	/**
	 * Creates <code>IFilterConditionElement</code> instance.
	 * 
	 * @param handle the filterConditionElement handle to create corresponding
	 *               IFilterConditionElement instance.
	 * @return IFilterConditionElement
	 */
	public IFilterConditionElement createFilterConditionElement(FilterConditionElementHandle handle);

	/**
	 * Create <code>ISortElement</code> instance.
	 * 
	 * @param handle the handle to create corresponding ISortElement instance.
	 * @return ISortElement
	 */
	public ISortElement createSortElement(SortElementHandle handle);

	/**
	 * Creates <code>IExpression</code> instance.
	 * 
	 * @return IExpression
	 */

	public IExpression createExpression();
}
