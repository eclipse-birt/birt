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

package org.eclipse.birt.report.designer.ui.lib.explorer.dnd;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.dnd.DesignElementDragAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.ui.lib.explorer.resource.ReportResourceEntry;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.DragSourceEvent;

/**
 * Library tree viewer drag listener.
 */

public class LibraryDragListener extends DesignElementDragAdapter {

	public LibraryDragListener(StructuredViewer viewer) {
		super(viewer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.dnd.DesignElementDragAdapter#
	 * validateTransfer(java.lang.Object)
	 */
	protected boolean validateTransfer(Object transfer) {
		if (transfer instanceof ReportElementHandle || transfer instanceof EmbeddedImageHandle) {
			if (transfer instanceof ScalarParameterHandle
					&& ((ScalarParameterHandle) transfer).getContainer() instanceof CascadingParameterGroupHandle) {
				return false;
			} else if (transfer instanceof StyleHandle
					&& ((StyleHandle) transfer).getContainer() instanceof ThemeHandle) {
				return false;
			} else if (transfer instanceof DimensionHandle || transfer instanceof LevelHandle
					|| transfer instanceof MeasureHandle || transfer instanceof MeasureGroupHandle) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	public void dragStart(DragSourceEvent event) {
		boolean doit = !getViewer().getSelection().isEmpty();
		if (doit) {
			IStructuredSelection selection = (IStructuredSelection) getViewer().getSelection();
			List objectList = selection.toList();
			selectionList.clear();
			for (int i = 0; i < objectList.size(); i++) {
				if (objectList.get(i) instanceof ReportResourceEntry)
					selectionList.add(((ReportResourceEntry) objectList.get(i)).getReportElement());
				else
					selectionList.add(objectList.get(i));
			}
			Object[] objects = selectionList.toArray();
			if (validateType(objects)) {
				for (int i = 0; i < objects.length; i++)
					if (!validateTransfer(objects[i])) {
						doit = false;
						break;
					}
			} else
				doit = false;
			if (doit)
				TemplateTransfer.getInstance().setTemplate(objects);
		}
		event.doit = doit;
		if (Policy.TRACING_DND_DRAG && doit) {
			System.out.println("DND >> Drag starts."); //$NON-NLS-1$
		}
	}

}
