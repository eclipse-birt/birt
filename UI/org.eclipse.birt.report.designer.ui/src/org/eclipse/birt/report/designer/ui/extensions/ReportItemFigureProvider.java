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

package org.eclipse.birt.report.designer.ui.extensions;

import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;

/**
 *  
 */
public class ReportItemFigureProvider implements IReportItemFigureProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.extensions.IReportItemFigureProvider#
	 * getFigure(org.eclipse.birt.report.model.api.ExtendedItemHandle)
	 */
	public IFigure createFigure(ExtendedItemHandle handle) {
		return new Figure();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.extensions.IReportItemFigureProvider#
	 * updateFigure(org.eclipse.birt.report.model.api.ExtendedItemHandle,
	 * org.eclipse.draw2d.IFigure)
	 */
	public void updateFigure(ExtendedItemHandle handle, IFigure figure) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.extensions.IReportItemFigureProvider#
	 * disposeFigure(org.eclipse.birt.report.model.api.ExtendedItemHandle,
	 * org.eclipse.draw2d.IFigure)
	 */
	public void disposeFigure(ExtendedItemHandle handle, IFigure figure) {
	}

}
