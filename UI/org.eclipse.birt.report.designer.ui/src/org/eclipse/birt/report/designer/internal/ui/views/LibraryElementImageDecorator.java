/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.views;

import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * 
 */

public class LibraryElementImageDecorator implements IReportImageDecorator {
	private static int Normal_Element = 1;
	private static int Library_Element = 2;
	private static int Local_Library_Element = 4;
	private String Library_Key = "LibraryKey"; //$NON-NLS-1$
	private String Local_Library_Key = "LocalLibraryKey"; //$NON-NLS-1$

	public Image decorateImage(Image image, Object element) {
		int flag = getElementFlag(element);
		if ((flag & Normal_Element) != 0) {
			return image;
		}
		String key = ""; //$NON-NLS-1$
		if (element instanceof DesignElementHandle) {
			DesignElementHandle handle = (DesignElementHandle) element;
			key = handle.getElement().getDefn().getName();
		} else if (element instanceof EmbeddedImageHandle) {
			EmbeddedImageHandle imageHandle = (EmbeddedImageHandle) element;
			key = imageHandle.getQualifiedName();
		}

		ImageDescriptor descriptor = null;
		if ((flag & Library_Element) != 0) {
			key = key + Library_Key;
			descriptor = new LibraryImageDescriptor(image,
					ReportPlatformUIImages.getImageDescriptor(IReportGraphicConstants.ICON_REPORT_LIBRARY_OVER));
		} else if ((flag & Local_Library_Element) != 0) {
			key = key + Local_Library_Key;
			descriptor = new LibraryImageDescriptor(image,
					ReportPlatformUIImages.getImageDescriptor(IReportGraphicConstants.ICON_REPORT_LOCAL_LIBRARY_OVER));
		}
		ImageRegistry regiest = ReportPlugin.getDefault().getImageRegistry();
		ImageDescriptor temp = regiest.getDescriptor(key);
		if (temp != null) {
			return temp.createImage();
		} else if (descriptor != null) {
			regiest.put(key, descriptor);
			return descriptor.createImage();
		}
		// ReportPlugin.getDefault( ).getImageRegistry( ).get
		return image;
	}

	private int getElementFlag(Object element) {
		if (!(element instanceof DesignElementHandle) && !(element instanceof EmbeddedImageHandle)) {
			return Normal_Element;
		}

		if (element instanceof EmbeddedImageHandle) {
			EmbeddedImageHandle image = (EmbeddedImageHandle) element;
			if (image.isLibReference()) {
				return Library_Element;
			}
		} else if (element instanceof DesignElementHandle) {
			DesignElementHandle handle = (DesignElementHandle) element;
			if (DEUtil.isLinkedElement(handle)) {
				if (handle.hasLocalProperties()) {
					return Local_Library_Element;
				}
				return Library_Element;
			}
		}
		return Normal_Element;
	}
}
