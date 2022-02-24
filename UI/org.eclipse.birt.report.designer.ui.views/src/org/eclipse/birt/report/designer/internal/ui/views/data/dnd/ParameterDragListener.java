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

package org.eclipse.birt.report.designer.internal.ui.views.data.dnd;

import org.eclipse.birt.report.designer.core.model.views.data.DataSetItemModel;
import org.eclipse.birt.report.designer.internal.ui.dnd.DesignElementDragAdapter;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;

/**
 * Supports dragging elements from data viewer.
 */
public class ParameterDragListener extends DesignElementDragAdapter {

	/**
	 * Constructor
	 *
	 * @param viewer
	 */
	public ParameterDragListener(StructuredViewer viewer) {
		super(viewer);
	}

	/**
	 * @see DragSourceAdapter#dragSetData(DragSourceEvent)
	 */
	@Override
	public void dragSetData(DragSourceEvent event) {
		IStructuredSelection selection = (IStructuredSelection) getViewer().getSelection();
		Object[] objects = selection.toList().toArray();

		if (TemplateTransfer.getInstance().isSupportedType(event.dataType)) {
			event.data = objects;
		}
	}

	/**
	 * @see DesignElementDragAdapter#validateTransfer(Object)
	 */
	@Override
	protected boolean validateTransfer(Object transfer) {
		return (transfer instanceof ScalarParameterHandle
				&& !(((ScalarParameterHandle) transfer).getContainer() instanceof CascadingParameterGroupHandle))
				|| (transfer instanceof ParameterGroupHandle && !(transfer instanceof CascadingParameterGroupHandle))
				|| transfer instanceof DataSetItemModel || transfer instanceof ResultSetColumnHandle
				|| transfer instanceof DataSetHandle;
	}
}
