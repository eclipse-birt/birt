/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
