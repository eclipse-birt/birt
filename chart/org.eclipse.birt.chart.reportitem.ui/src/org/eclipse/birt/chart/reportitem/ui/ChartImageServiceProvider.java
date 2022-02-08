/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.chart.reportitem.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.reportitem.ChartReportItemUtil;
import org.eclipse.birt.chart.ui.swt.interfaces.IImageServiceProvider;
import org.eclipse.birt.report.designer.internal.ui.util.graphics.BirtImageLoader;
import org.eclipse.birt.report.designer.util.ImageManager;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;

/**
 * ChartImageServiceProvider
 */

public class ChartImageServiceProvider implements IImageServiceProvider {

	private ExtendedItemHandle extendedHandle = null;

	public ChartImageServiceProvider(Object extendedHandle) {
		try {
			this.extendedHandle = (ExtendedItemHandle) extendedHandle;
		} catch (Exception e) {
			this.extendedHandle = null;
		}
	}

	public List<String> getEmbeddedImageName() {
		List<String> list = new ArrayList<String>();

		if (extendedHandle == null) {
			return list;
		}

		for (Iterator<?> itor = extendedHandle.getModuleHandle().getVisibleImages().iterator(); itor.hasNext();) {
			EmbeddedImageHandle handle = (EmbeddedImageHandle) itor.next();
			list.add(handle.getQualifiedName());
		}

		return list;
	}

	public String saveImage(String fullPath, String fileName) throws ChartException {
		BirtImageLoader imageLoader = new BirtImageLoader();
		try {
			EmbeddedImage image = imageLoader.save(extendedHandle.getModuleHandle(), fullPath, fileName);
			return image.getName();
		} catch (Exception e) {
			throw new ChartException(ChartReportItemUIActivator.ID, ChartException.IMAGE_LOADING, e);
		}
	}

	public org.eclipse.swt.graphics.Image getEmbeddedImage(String fileName) {
		if (extendedHandle == null) {
			return null;
		} else {
			return ImageManager.getInstance().getEmbeddedImage(extendedHandle.getModuleHandle(), fileName);
		}
	}

	public org.eclipse.swt.graphics.Image loadImage(String fileName) throws ChartException {
		try {
			return ImageManager.getInstance().loadImage(fileName);
		} catch (IOException e) {
			throw new ChartException(ChartReportItemUIActivator.ID, ChartException.IMAGE_LOADING, e);
		}
	}

	public String getImageAbsoluteURL(Image image) {
		if (extendedHandle == null) {
			return null;
		} else {
			return ChartReportItemUtil.getImageAbsoluteURL(image, extendedHandle);
		}
	}
}
