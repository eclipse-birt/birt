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

import org.eclipse.birt.report.designer.ui.expressions.ExpressionFilter;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

/**
 *
 */

public class ComputedColumnExpressionFilter extends ExpressionFilter {

	protected ComputedColumnHandle handle;

	protected TableViewer tableViewer;

	public ComputedColumnExpressionFilter(TableViewer tableViewer) {
		super();
		this.tableViewer = tableViewer;
	}

	public ComputedColumnExpressionFilter(ComputedColumnHandle input) {
		super();
		handle = input;
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
		if (tableViewer != null) {
			IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
			Object obj = selection.getFirstElement();
			if (obj instanceof ComputedColumnHandle) {
				handle = (ComputedColumnHandle) obj;
			}
		}
		if (handle != null && element instanceof ComputedColumnHandle) {
			ComputedColumnHandle elementHandle = (ComputedColumnHandle) element;
			if (handle.getStructure() == elementHandle.getStructure()) {
				return false;
			}
		}
		return true;
	}

}
