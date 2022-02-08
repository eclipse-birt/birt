/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.chart.reportitem.ui.views.attributes.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.expressions.ExpressionFilter;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;

/**
 * Since if chart is using cube set, the filter expressions only allow that
 * expressions used by chart, so the class extends
 * {@link org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider} and
 * provides available expressions for setting filter condition.
 * 
 * @since 2.3
 */
public class ChartCubeFilterExpressionProvider extends ExpressionProvider {
	/** Available expressions used by chart. */
	private String[] fAvailableExpressions;

	/** The list stores all report element handles related to chart. */
	private List<DesignElementHandle> fChartElementHandles = new ArrayList();

	/**
	 * Constructor.
	 * 
	 * @param handle report element handle which chart relies on.
	 */
	public ChartCubeFilterExpressionProvider(DesignElementHandle handle, final String[] availableExpressions) {
		super(handle);
		fAvailableExpressions = availableExpressions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider#getChildren(
	 * java.lang.Object)
	 */
	public Object[] getChildren(Object parent) {
		Object[] children = null;

		if (parent instanceof ReportItemHandle && fChartElementHandles.contains(parent)) {
			// Only returns available expressions used by chart..
			children = getAvailableExpressions((ReportItemHandle) parent);
		} else if (parent instanceof String && COLUMN_BINDINGS.equals(parent)) {
			// Returns available chart element handles.
			fChartElementHandles = getAllBindingElementHandles();
			children = fChartElementHandles.toArray();
		} else {
			children = getChildrenList(parent).toArray();
		}

		if (filterList != null && !filterList.isEmpty()) {
			for (Iterator iter = filterList.iterator(); iter.hasNext();) {
				Object obj = iter.next();
				if (obj instanceof ExpressionFilter) {
					children = ((ExpressionFilter) obj).filter(parent, children);
				}
			}
		}
		return children;
	}

	/**
	 * Returns available expressions used by chart.
	 * 
	 * @param reportItemHandle the report item handle which chart relies on.
	 * @return
	 */
	private Object[] getAvailableExpressions(ReportItemHandle reportItemHandle) {
		List childrenList = new ArrayList();
		Iterator iter = reportItemHandle.columnBindingsIterator();
		if (iter == null) {
			return childrenList.toArray();
		}

		while (iter.hasNext()) {
			ComputedColumnHandle cch = (ComputedColumnHandle) iter.next();
			if (isAvailableExpression(ExpressionUtil.createJSDataExpression(cch.getName()))) {
				childrenList.add(cch);
			}
		}
		return childrenList.toArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider#
	 * getAllBindingElementHandles()
	 */
	protected List<DesignElementHandle> getAllBindingElementHandles() {
		ArrayList<DesignElementHandle> childrenList = new ArrayList();
		List bindingList = getAllColumnBindingList();
		// The list is from top to bottom,reverse it
		Collections.reverse(bindingList);
		for (Iterator iter = bindingList.iterator(); iter.hasNext();) {
			ComputedColumnHandle handle = (ComputedColumnHandle) iter.next();
			if (!isAvailableExpression(ExpressionUtil.createJSDataExpression(handle.getName()))) {
				continue;
			}

			if (!childrenList.contains(handle.getElementHandle())) {

				childrenList.add(handle.getElementHandle());
			}
		}
		return childrenList;
	}

	/**
	 * Check if specified expression is used by chart.
	 * 
	 * @param expression
	 * @return
	 */
	private boolean isAvailableExpression(String expression) {
		String regex = ".*\\Q" + expression + "\\E.*"; //$NON-NLS-1$ //$NON-NLS-2$
		for (String exp : fAvailableExpressions) {
			if (exp.matches(regex)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Overwrite the method to disable Cube Data selection, because chart with cube
	 * set only allow to set filter condition on used expressions.
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider#getCategoryList()
	 */
	protected List getCategoryList() {
		List list = super.getCategoryList();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Object o = iter.next();
			if (o instanceof String && CURRENT_CUBE.equals(o)) {
				iter.remove();
				break;
			}
		}

		return list;
	}
}
