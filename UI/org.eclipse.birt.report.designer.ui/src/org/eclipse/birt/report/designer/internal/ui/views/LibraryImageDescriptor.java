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

package org.eclipse.birt.report.designer.internal.ui.views;

import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;

/**
 * A {@link LibraryImageDescriptor} consists of a base image and several
 * adornments. The adornments are computed according to the flags either passed
 * during creation or set via the method {@link #setAdornments(int)}.
 *
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 *
 * @since 2.0
 */
public class LibraryImageDescriptor extends CompositeImageDescriptor {

	private ImageDescriptor fBaseImage;
	private ImageDescriptor fDecoratorImage;
	// private

	public LibraryImageDescriptor(Image baseImage, ImageDescriptor decoratorImage) {
		fBaseImage = new ImageImageDescriptor(baseImage);
		fDecoratorImage = decoratorImage;
	}

	@Override
	public boolean equals(Object object) {
		if (object == null || !LibraryImageDescriptor.class.equals(object.getClass())) {
			return false;
		}
		LibraryImageDescriptor other = (LibraryImageDescriptor) object;
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

		drawImage(bg, 0, 0);
		Point pos = new Point(getSize().y, getSize().y);
		ImageData data = fDecoratorImage.getImageData();
		addLeftBottomImage(data, pos);
	}

	@Override
	protected Point getSize() {
		return new Point(fBaseImage.getImageData().width, fBaseImage.getImageData().height);
	}

	private void addLeftBottomImage(ImageData data, Point pos) {
		int y = pos.y - data.height;
		int x = pos.x - data.width;
		if (y >= 0 && x >= 0) {
			drawImage(data, x, y);
		}
	}

	private static class ImageImageDescriptor extends ImageDescriptor {
		private Image fImage;

		/**
		 * Constructor for ImagImageDescriptor.
		 */
		public ImageImageDescriptor(Image image) {
			super();
			fImage = image;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see ImageDescriptor#getImageData()
		 */
		@Override
		public ImageData getImageData() {
			return fImage.getImageData();
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see Object#equals(Object)
		 */
		@Override
		public boolean equals(Object obj) {
			return (obj != null) && getClass().equals(obj.getClass())
					&& fImage.equals(((ImageImageDescriptor) obj).fImage);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return fImage.hashCode();
		}
	}
}
