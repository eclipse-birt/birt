/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts;

import org.eclipse.birt.report.designer.core.model.DesignElementHandleAdapter;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;

/**
 * Dummy edit part to avoid null pointer error This will be the default editpart
 * the if there're no editpart is created by the BraphicalPartFactory.
 * 
 */
public class DummyEditpart extends ReportElementEditPart {

	/**
	 * Constructor
	 * 
	 * @param model
	 */
	public DummyEditpart(Object model) {
		super(model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		Figure figure = new Figure();
		figure.setMaximumSize(new Dimension(0, 0));
		figure.setSize(0, 0);
		return figure;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.
	 * ReportElementEditPart#elementChanged(org.eclipse.birt.model.api.
	 * DesignElementHandle, org.eclipse.birt.model.activity.NotificationEvent)
	 */
	public void elementChanged(DesignElementHandle arg0, NotificationEvent arg1) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.
	 * ReportElementEditPart#refreshFigure()
	 */
	public void refreshFigure() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.
	 * ReportElementEditPart#creatDesignElementHandleAdapter()
	 */
	public DesignElementHandleAdapter creatDesignElementHandleAdapter() {
		return null;
		// do nothing
	}

}
