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
	public boolean select(Object parentElement, Object element) {
		if (tableViewer != null) {
			IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
			Object obj = selection.getFirstElement();
			if (obj instanceof ComputedColumnHandle)
				handle = (ComputedColumnHandle) obj;
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
