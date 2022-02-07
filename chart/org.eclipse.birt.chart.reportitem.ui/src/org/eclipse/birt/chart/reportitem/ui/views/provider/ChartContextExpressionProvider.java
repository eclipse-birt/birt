/*******************************************************************************
* Copyright (c) 2007 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/
package org.eclipse.birt.chart.reportitem.ui.views.provider;

import org.eclipse.birt.report.designer.ui.expressions.AbstractContextExpressionProvider;
import org.eclipse.birt.report.designer.ui.expressions.ExpressionFilter;

/**
 * 
 */

public class ChartContextExpressionProvider extends AbstractContextExpressionProvider {

	private ExpressionFilter filter = new ExpressionFilter() {

		@Override
		public boolean select(Object parentElement, Object element) {
			if (ExpressionFilter.CATEGORY.equals(parentElement)
					&& (ExpressionFilter.CATEGORY_PARAMETERS.equals(element))) {
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
