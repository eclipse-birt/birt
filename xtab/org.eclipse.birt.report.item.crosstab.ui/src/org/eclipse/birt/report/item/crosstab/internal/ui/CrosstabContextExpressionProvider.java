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

package org.eclipse.birt.report.item.crosstab.internal.ui;

import org.eclipse.birt.report.designer.ui.expressions.AbstractContextExpressionProvider;
import org.eclipse.birt.report.designer.ui.expressions.ExpressionFilter;

/**
 * CrosstabContextExpressionProvider
 */
public class CrosstabContextExpressionProvider extends AbstractContextExpressionProvider {

	private ExpressionFilter filter = new ExpressionFilter() {

		@Override
		public boolean select(Object parentElement, Object element) {
			if (ExpressionFilter.CATEGORY.equals(parentElement) && (ExpressionFilter.CATEGORY_PARAMETERS.equals(element)
					|| ExpressionFilter.CATEGORY_BIRT_OBJECTS.equals(element))) {
				return false;
			}
			return true;
		}
	};

	@Override
	public ExpressionFilter getExpressionFilter(String contextName) {
		return filter;
	}
}
