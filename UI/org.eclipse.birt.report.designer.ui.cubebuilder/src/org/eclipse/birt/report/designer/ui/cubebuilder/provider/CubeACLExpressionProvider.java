/*******************************************************************************
 * Copyright (c) 2012 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.cubebuilder.provider;

import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.expressions.ExpressionFilter;
import org.eclipse.birt.report.model.api.DesignElementHandle;

/**
 * The expression provider for the Access Control List Expressions. Basically
 * this is the default CubeExpressionProvider except that it filters the data
 * set row category for the data set rows are not available while evaluating the
 * ACL expression for a level.
 */
public class CubeACLExpressionProvider extends CubeExpressionProvider {
	public CubeACLExpressionProvider(DesignElementHandle handle) {
		super(handle);
	}

	protected void addFilterToProvider() {
		this.addFilter(new ExpressionFilter() {

			public boolean select(Object parentElement, Object element) {
				if (ExpressionFilter.CATEGORY.equals(parentElement)
						&& ExpressionProvider.CURRENT_CUBE.equals(element)) {
					return false;
				}
				if (ExpressionFilter.CATEGORY.equals(parentElement) && ExpressionProvider.MEASURE.equals(element)) {
					return false;
				}
				if (ExpressionFilter.CATEGORY.equals(parentElement) && ExpressionProvider.DATASETS.equals(element)) {
					return false;
				}
				return true;
			}
		});
	}
}
