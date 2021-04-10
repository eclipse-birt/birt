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