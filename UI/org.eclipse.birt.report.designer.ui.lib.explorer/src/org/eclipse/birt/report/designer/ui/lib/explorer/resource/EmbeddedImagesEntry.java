/*******************************************************************************
 * Copyright (c) 2007, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.lib.explorer.resource;

import org.eclipse.birt.report.designer.core.model.views.outline.EmbeddedImageNode;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.util.DEUtil;

/**
 * This class is a representation of resource entry for embedded image.
 */
public class EmbeddedImagesEntry extends ReportElementEntry {

	/**
	 * Constructs a resource entry for the specified embedded image.
	 * 
	 * @param image  the specified embedded image.
	 * @param parent the parent entry.
	 */
	public EmbeddedImagesEntry(EmbeddedImageNode image, ResourceEntry parent) {
		super(image, parent);
	}

	@Override
	public boolean equals(Object object) {
		if (object == null || !object.getClass().equals(getClass())) {
			return false;
		}

		if (object == this) {
			return true;
		} else {
			EmbeddedImagesEntry temp = (EmbeddedImagesEntry) object;
			EmbeddedImageNode tempImage = temp.getReportElement();
			EmbeddedImageNode thisImage = getReportElement();

			if (tempImage == thisImage) {
				return true;
			}

			if (tempImage != null && thisImage != null
					&& tempImage.getReportDesignHandle().getID() == thisImage.getReportDesignHandle().getID()
					&& DEUtil.isSameString(tempImage.getReportDesignHandle().getModule().getFileName(),
							thisImage.getReportDesignHandle().getModule().getFileName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		EmbeddedImageNode image = getReportElement();

		if (image == null) {
			return super.hashCode();
		}

		String fileName = image.getReportDesignHandle().getModule().getFileName();

		return (int) image.getReportDesignHandle().getID() * 7 + (fileName == null ? 0 : fileName.hashCode());
	}

	@Override
	public EmbeddedImageNode getReportElement() {
		Object image = super.getReportElement();

		return image instanceof EmbeddedImageNode ? (EmbeddedImageNode) image : null;
	}
}
