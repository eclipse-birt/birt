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

package org.eclipse.birt.report.item.crosstab.internal.ui.dialogs;

import org.eclipse.birt.report.designer.ui.expressions.ExpressionFilter;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;
import org.eclipse.core.runtime.IAdaptable;

/**
 * 
 */

public class CrosstabAggregationExpressionProvider extends CrosstabExpressionProvider {

	protected void addFilterToProvider() {
		this.addFilter(new ExpressionFilter() {

			public boolean select(Object parentElement, Object element) {
				// Crosstab Aggregation expression can only use measeure of
				// cube.
				if (parentElement instanceof String) {
					String parent = (String) parentElement;
					if (ExpressionFilter.CATEGORY.equals(parent)) {
						if (element instanceof String) {
							String elementString = (String) element;
							if (COLUMN_BINDINGS.equals(elementString)) {
								return false;
							}
						}
					}

					if (CURRENT_CUBE.equals(parent)) {
						PropertyHandle handle = null;
						if (element instanceof PropertyHandle)
							handle = (PropertyHandle) element;
						else if (element instanceof IAdaptable
								&& ((IAdaptable) element).getAdapter(PropertyHandle.class) instanceof PropertyHandle)
							handle = (PropertyHandle) ((IAdaptable) element).getAdapter(PropertyHandle.class);

						if (handle != null
								&& handle.getPropertyDefn().getName().equals(ICubeModel.MEASURE_GROUPS_PROP)) {
							return true;
						}
						return false;
					}
				}
				return true;
			}
		});
	}

	public CrosstabAggregationExpressionProvider(DesignElementHandle handle,
			ComputedColumnHandle computedColumnHandle) {
		super(handle, computedColumnHandle);
	}

}
