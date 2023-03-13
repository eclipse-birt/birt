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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.expressions.ExpressionFilter;

/**
 * Expression filter for data set, filtering birt_objects and parameters when
 * invoking the expressiong builder.
 */

public class DataSetExpressionFilter extends ExpressionFilter {

	/**
	 * Creates a new instance.
	 */
	public DataSetExpressionFilter() {
		super();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.dialogs.ExpressionFilter#select(
	 * java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean select(Object parentElement, Object element) {
		if (ExpressionProvider.PARAMETERS.equals(element)) {
			return false;
		}
		return true;
	}
}
