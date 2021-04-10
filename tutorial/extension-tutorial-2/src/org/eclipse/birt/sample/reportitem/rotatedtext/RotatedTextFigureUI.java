/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.sample.reportitem.rotatedtext;

import org.eclipse.birt.report.designer.ui.extensions.ReportItemFigureProvider;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.draw2d.IFigure;

/**
 * RotatedTextFigureUI
 */
public class RotatedTextFigureUI extends ReportItemFigureProvider {

	public IFigure createFigure(ExtendedItemHandle handle) {
		try {
			IReportItem item = handle.getReportItem();

			if (item instanceof RotatedTextItem) {
				return new RotatedTextFigure((RotatedTextItem) item);
			}
		} catch (ExtendedElementException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void updateFigure(ExtendedItemHandle handle, IFigure figure) {
		try {
			IReportItem item = handle.getReportItem();

			if (item instanceof RotatedTextItem) {
				RotatedTextFigure fig = (RotatedTextFigure) figure;

				fig.setRotatedTextItem((RotatedTextItem) item);
			}
		} catch (ExtendedElementException e) {
			e.printStackTrace();
		}
	}

	public void disposeFigure(ExtendedItemHandle handle, IFigure figure) {
		((RotatedTextFigure) figure).dispose();
	}

}
