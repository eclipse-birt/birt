/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.expressions;

import java.util.ArrayList;
import java.util.List;

/**
 * A filter that used by expression provider to extract a subset of elements
 * 
 * @since 2.5
 */
public abstract class ExpressionFilter {

	/**
	 * The constant parent element for all categories.
	 */
	public static final String CATEGORY = "Category"; //$NON-NLS-1$

	// A set of bulilt-in categories
	public static final String CATEGORY_OPERATORS = "Operators"; //$NON-NLS-1$
	public static final String CATEGORY_NATIVE_OBJECTS = "Native Objects"; //$NON-NLS-1$
	public static final String CATEGORY_BIRT_OBJECTS = "Birt Objects"; //$NON-NLS-1$
	public static final String CATEGORY_PARAMETERS = "Parameters"; //$NON-NLS-1$
	public static final String CATEGORY_VARIABLES = "Variables"; //$NON-NLS-1$
	public static final String CATEGORY_CONTEXT = "Context"; //$NON-NLS-1$

	/**
	 * Filters the given elements for the given viewer. The input array is not
	 * modified.
	 * <p>
	 * The default implementation of this method calls <code>select</code> on each
	 * element in the array, and returns only those elements for which
	 * <code>select</code> returns <code>true</code>.
	 * </p>
	 * 
	 * @param parent   the parent element
	 * @param elements the elements to filter
	 * @return the filtered elements
	 */
	public Object[] filter(Object parent, Object[] elements) {
		if (elements == null) {
			return null;
		}

		int size = elements.length;
		List<Object> out = new ArrayList<Object>(size);
		for (int i = 0; i < size; ++i) {
			Object element = elements[i];
			if (select(parent, element)) {
				out.add(element);
			}
		}
		return out.toArray(new Object[out.size()]);
	}

	/**
	 * Returns whether the given element makes it through this filter.
	 * 
	 * @param parentElement the parent element,or CATEGORY if want to filter the
	 *                      categroy list
	 * @param element       the element
	 * @return <code>true</code> if element is included in the filtered set, and
	 *         <code>false</code> if excluded
	 */
	public abstract boolean select(Object parentElement, Object element);
}
