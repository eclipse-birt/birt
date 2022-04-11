/*******************************************************************************
 * Copyright (c) 2022 Henning von Bargen
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Henning von Bargen - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.extension.qrcode;

import org.eclipse.birt.report.designer.ui.extensions.ReportItemFigureProvider;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.draw2d.IFigure;

/**
 * QRCodeFigureUI
 */
public class QRCodeFigureUI extends ReportItemFigureProvider {

	@Override
	public IFigure createFigure(ExtendedItemHandle handle) {
		try {
			IReportItem item = handle.getReportItem();

			if (item instanceof QRCodeItem) {
				return new QRCodeFigure((QRCodeItem) item);
			}
		} catch (ExtendedElementException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void updateFigure(ExtendedItemHandle handle, IFigure figure) {
		try {
			IReportItem item = handle.getReportItem();

			if (item instanceof QRCodeItem) {
				QRCodeFigure fig = (QRCodeFigure) figure;
				fig.setQRCodeItem((QRCodeItem) item);
			}
		} catch (ExtendedElementException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void disposeFigure(ExtendedItemHandle handle, IFigure figure) {
		((QRCodeFigure) figure).dispose();
	}

}
