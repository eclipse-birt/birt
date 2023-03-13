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

package org.eclipse.birt.report.designer.internal.ui.extension;

import org.eclipse.birt.report.designer.ui.extensions.IReportItemFigureProvider;
import org.eclipse.birt.report.designer.ui.extensions.IReportItemImageProvider;
import org.eclipse.birt.report.designer.ui.extensions.IReportItemLabelProvider;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.swt.graphics.Image;

/**
 */
public class ExtendedUIAdapter implements IReportItemFigureProvider {

	private IReportItemFigureProvider figureExtension;
	private IReportItemImageProvider imageExtension;
	private IReportItemLabelProvider labelExtension;
	private int extensionType;
	private final static int FIGURE_EXTENSION_TYPE = 0;
	private final static int IMAGE_EXTENSION_TYPE = 1;
	private final static int LABEL_EXTENSION_TYPE = 2;

	public ExtendedUIAdapter(Object extension) {
		if (extension instanceof IReportItemFigureProvider) {
			figureExtension = (IReportItemFigureProvider) extension;
			extensionType = FIGURE_EXTENSION_TYPE;
		} else if (extension instanceof IReportItemImageProvider) {
			imageExtension = (IReportItemImageProvider) extension;
			extensionType = IMAGE_EXTENSION_TYPE;
		} else if (extension instanceof IReportItemLabelProvider) {
			labelExtension = (IReportItemLabelProvider) extension;
			extensionType = LABEL_EXTENSION_TYPE;
		} else {
			throw new IllegalArgumentException();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.extensions.IReportItemFigureProvider#
	 * createFigure(org.eclipse.birt.report.model.api.ExtendedItemHandle)
	 */
	@Override
	public IFigure createFigure(ExtendedItemHandle handle) {
		switch (extensionType) {
		case FIGURE_EXTENSION_TYPE:
			return figureExtension.createFigure(handle);
		case IMAGE_EXTENSION_TYPE:
			return new ImageFigure(imageExtension.getImage(handle));
		case LABEL_EXTENSION_TYPE:
			return new Label(labelExtension.getLabel(handle));
		default:
			return null;
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.extensions.IReportItemFigureProvider#
	 * updateFigure(org.eclipse.birt.report.model.api.ExtendedItemHandle,
	 * org.eclipse.draw2d.IFigure)
	 */
	@Override
	public void updateFigure(ExtendedItemHandle handle, IFigure figure) {
		switch (extensionType) {
		case FIGURE_EXTENSION_TYPE:
			figureExtension.updateFigure(handle, figure);
			break;
		case IMAGE_EXTENSION_TYPE:
			ImageFigure imageFigure = (ImageFigure) figure;
			Image newImage = imageExtension.getImage(handle);
			Image oldImage = imageFigure.getImage();
			if (newImage != oldImage) {
				imageFigure.setImage(newImage);
				imageExtension.disposeImage(handle, oldImage);
			}
			break;
		case LABEL_EXTENSION_TYPE:
			((Label) figure).setText(labelExtension.getLabel(handle));
			break;
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.extensions.IReportItemFigureProvider#
	 * disposeFigure(org.eclipse.birt.report.model.api.ExtendedItemHandle,
	 * org.eclipse.draw2d.IFigure)
	 */
	@Override
	public void disposeFigure(ExtendedItemHandle handle, IFigure figure) {
		switch (extensionType) {
		case FIGURE_EXTENSION_TYPE:
			figureExtension.disposeFigure(handle, figure);
			break;
		case IMAGE_EXTENSION_TYPE:
			imageExtension.disposeImage(handle, ((ImageFigure) figure).getImage());
			break;
		case LABEL_EXTENSION_TYPE:
			// no action needed
			break;
		}

	}

}
