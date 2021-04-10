/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.birt.report.designer.internal.ui.editors.parts.DeferredGraphicalViewer;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.GraphicalPartFactory;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportRootEditPart;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

/**
 * ReportGraphicsViewPainter
 */
public class ReportGraphicsViewPainter {

	private Object model;
	private GraphicalViewer viewer;
	private Shell hostShell;

	public ReportGraphicsViewPainter(Object model) {
		this.model = model;

		configGraphicalViewer();
	}

	public void paint(Drawable drawable, Device device, Rectangle region) {
		ReportPrintGraphicalViewerOperation op = new ReportPrintGraphicalViewerOperation(viewer, drawable, device,
				new org.eclipse.draw2d.geometry.Rectangle(region));
		if (model instanceof ReportDesignHandle) {
			op.setOrientation(((ReportDesignHandle) model).getBidiOrientation());
		}

		op.run("paint"); //$NON-NLS-1$

	}

	public void dispose() {
		if (hostShell != null && !hostShell.isDisposed()) {
			hostShell.dispose();
			hostShell = null;
		}
	}

	protected void configGraphicalViewer() {
		viewer = new DeferredGraphicalViewer() {

			protected void fireSelectionChanged() {
				// do nothing
			}
		};

		DeferredGraphicalViewer reportViewer = (DeferredGraphicalViewer) viewer;
		// reportViewer.hookRefreshListener( getRefreshManager( ) );
		if (model instanceof ReportDesignHandle) {
			String orientation = ((ReportDesignHandle) model).getBidiOrientation();
			if (DesignChoiceConstants.BIDI_DIRECTION_RTL.equals(orientation)) {
				hostShell = new Shell(SWT.RIGHT_TO_LEFT);
			} else {
				hostShell = new Shell(SWT.LEFT_TO_RIGHT);
			}
		} else {
			hostShell = new Shell();
		}
		reportViewer.createControl(hostShell);
		reportViewer.setEditDomain(new DefaultEditDomain(null));
		reportViewer.setRootEditPart(new ReportRootEditPart());
		reportViewer.setEditPartFactory(new GraphicalPartFactory());
		reportViewer.setContents(model);
		reportViewer.flush();
	}

}
