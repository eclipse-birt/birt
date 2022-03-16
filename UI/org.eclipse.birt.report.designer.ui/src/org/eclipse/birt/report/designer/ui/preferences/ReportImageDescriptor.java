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

package org.eclipse.birt.report.designer.ui.preferences;

import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;

/**
 * A {@link ReportImageDescriptor} consists of a base image and several
 * adornments. The adornments are computed according to the flags either passed
 * during creation or set via the method {@link #setAdornments(int)}.
 *
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 *
 * @since 2.0
 */
public class ReportImageDescriptor extends CompositeImageDescriptor {

	private ImageDescriptor fBaseImage;

	public ReportImageDescriptor(ImageDescriptor baseImage) {
		fBaseImage = baseImage;
	}

	@Override
	public boolean equals(Object object) {
		if (object == null || !ReportImageDescriptor.class.equals(object.getClass())) {
			return false;
		}
		ReportImageDescriptor other = (ReportImageDescriptor) object;
		return (fBaseImage.equals(other.fBaseImage));
	}

	/*
	 * (non-Javadoc) Method declared on Object.
	 */
	@Override
	public int hashCode() {
		return fBaseImage.hashCode();
	}

	/*
	 * (non-Javadoc) Method declared in CompositeImageDescriptor
	 */
	@Override
	protected void drawCompositeImage(int width, int height) {
		ImageData bg = fBaseImage.getImageData();
		ImageData data = getImageData(
				ReportPlatformUIImages.getImageDescriptor(IReportGraphicConstants.ICON_REPORT_PROJECT_OVER));
		drawImage(bg, 0, 0);
		Point pos = new Point(getSize().x, 0);
		addTopRightImage(data, pos);
	}

	private ImageData getImageData(ImageDescriptor descriptor) {
		ImageData data = descriptor.getImageData(); // null
		if (data == null) {
			data = DEFAULT_IMAGE_DATA;
		}
		return data;
	}

	@Override
	protected Point getSize() {
		return new Point(fBaseImage.getImageData().width, fBaseImage.getImageData().height);
	}

	private void addTopRightImage(ImageData data, Point pos) {
		int x = pos.x - data.width;
		if (x >= 0) {
			drawImage(data, x, pos.y);
			pos.x = x;
		}
	}
}
