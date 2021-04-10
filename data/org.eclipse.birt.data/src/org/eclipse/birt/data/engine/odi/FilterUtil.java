/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.odi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;

/**
 * 
 */
public class FilterUtil {
	/**
	 * No instance
	 */
	private FilterUtil() {
	}

	/**
	 * @param filters
	 */
	public static List sortFilters(List filters) {
		if (filters == null)
			return null;

		int size = filters.size();
		Object[] filterArray = new Object[size];
		for (int i = 0; i < size; i++)
			filterArray[i] = filters.get(i);

		Arrays.sort(filterArray, new Comparator() {

			/*
			 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
			 */
			public int compare(Object o1, Object o2) {
				if (o1 instanceof FilterDefinition && isTopOrBottomFilter((FilterDefinition) o1)) {
					return -1;
				} else if (o2 instanceof FilterDefinition && isTopOrBottomFilter((FilterDefinition) o2)) {
					return 1;
				}
				return 0;
			}

		});

		List sortedFilters = new ArrayList();
		for (int i = 0; i < size; i++)
			sortedFilters.add(filterArray[i]);

		return sortedFilters;
	}

	/**
	 * @param fd
	 * @return
	 */
	private static boolean isTopOrBottomFilter(FilterDefinition fd) {
		IBaseExpression be = fd.getExpression();
		if (be instanceof ConditionalExpression) {
			ConditionalExpression ce = (ConditionalExpression) be;
			if (ce.getOperator() == IConditionalExpression.OP_TOP_N
					|| ce.getOperator() == IConditionalExpression.OP_TOP_PERCENT
					|| ce.getOperator() == IConditionalExpression.OP_BOTTOM_N
					|| ce.getOperator() == IConditionalExpression.OP_BOTTOM_PERCENT) {
				return true;
			}
		}
		return false;
	}
}
