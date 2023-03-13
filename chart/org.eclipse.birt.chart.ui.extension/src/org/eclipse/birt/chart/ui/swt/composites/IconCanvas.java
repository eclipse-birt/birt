/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.chart.ui.swt.composites;

import java.awt.geom.AffineTransform;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.birt.chart.ui.util.TransformUtil;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ScrollBar;

/**
 * Special Canvas class used to display the image.
 *
 */
public class IconCanvas extends Canvas {

	private Image sourceImage;

	private Image screenImage;

	private AffineTransform transform = new AffineTransform();

	/**
	 * The constructor.
	 *
	 * @param parent
	 */
	public IconCanvas(final Composite parent) {
		this(parent, 0);
	}

	/**
	 * The constructor.
	 *
	 * @param parent
	 * @param style
	 *
	 */
	public IconCanvas(final Composite parent, int style) {
		super(parent, style);

		addPaintListener(new PaintListener() {

			@Override
			public void paintControl(final PaintEvent event) {
				paint(event.gc);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	@Override
	public void dispose() {
		if (sourceImage != null && !sourceImage.isDisposed()) {
			sourceImage.dispose();
		}

		if (screenImage != null && !screenImage.isDisposed()) {
			screenImage.dispose();
		}
	}

	private void paint(GC gc) {
		Rectangle clientRect = getClientArea();

		if (sourceImage != null) {
			Rectangle imageRect = TransformUtil.inverseTransformRect(transform, clientRect);
			int gap = 2;
			imageRect.x -= gap;
			imageRect.y -= gap;
			imageRect.width += 2 * gap;
			imageRect.height += 2 * gap;

			Rectangle imageBound = sourceImage.getBounds();
			imageRect = imageRect.intersection(imageBound);
			Rectangle destRect = TransformUtil.transformRect(transform, imageRect);

			if (screenImage != null) {
				screenImage.dispose();
			}
			screenImage = new Image(getDisplay(), clientRect.width, clientRect.height);
			GC newGC = new GC(screenImage);
			newGC.setClipping(clientRect);
			newGC.drawImage(sourceImage, imageRect.x, imageRect.y, imageRect.width, imageRect.height, destRect.x,
					destRect.y, destRect.width, destRect.height);
			newGC.dispose();

			gc.drawImage(screenImage, 0, 0);
		} else {
			gc.setClipping(clientRect);
			gc.fillRectangle(clientRect);
		}
	}

	/**
	 * SYNC the scroll-bars with the image.
	 */
	public void syncScrollBars() {
		if (sourceImage == null) {
			redraw();
			return;
		}

		AffineTransform af = transform;
		double sx = af.getScaleX(), sy = af.getScaleY();
		double tx = af.getTranslateX(), ty = af.getTranslateY();
		if (tx > 0) {
			tx = 0;
		}
		if (ty > 0) {
			ty = 0;
		}

		Rectangle imageBound = sourceImage.getBounds();
		int cw = getClientArea().width, ch = getClientArea().height;

		ScrollBar horizontal = getHorizontalBar();

		if (horizontal != null) {
			horizontal.setIncrement((getClientArea().width / 100));
			horizontal.setPageIncrement(getClientArea().width);

			if (imageBound.width * sx > cw) {
				horizontal.setMaximum((int) (imageBound.width * sx));
				horizontal.setEnabled(true);
				if (((int) -tx) > horizontal.getMaximum() - cw) {
					tx = -horizontal.getMaximum() + cw;
				}
			} else {
				horizontal.setEnabled(false);
				tx = (cw - imageBound.width * sx) / 2;
			}
			horizontal.setSelection((int) (-tx));
			horizontal.setThumb((getClientArea().width));
		}
		ScrollBar vertical = getVerticalBar();
		if (vertical != null) {
			vertical.setIncrement((getClientArea().height / 100));
			vertical.setPageIncrement((getClientArea().height));
			if (imageBound.height * sy > ch) {
				vertical.setMaximum((int) (imageBound.height * sy));
				vertical.setEnabled(true);
				if (((int) -ty) > vertical.getMaximum() - ch) {
					ty = -vertical.getMaximum() + ch;
				}
			} else {
				vertical.setEnabled(false);
				ty = (ch - imageBound.height * sy) / 2;
			}
			vertical.setSelection((int) (-ty));
			vertical.setThumb((getClientArea().height));
		}

		af = AffineTransform.getScaleInstance(sx, sy);
		af.preConcatenate(AffineTransform.getTranslateInstance(tx, ty));
		transform = af;

		redraw();
	}

	/**
	 * Load the image from a file
	 *
	 * @param filename
	 *
	 */
	public Image loadImage(String filename) {
		if (sourceImage != null && !sourceImage.isDisposed()) {
			sourceImage.dispose();
			sourceImage = null;
		}
		sourceImage = new Image(getDisplay(), filename);

		if (sourceImage.getBounds().width > this.getBounds().width
				|| sourceImage.getBounds().height > this.getBounds().height) {
			fitCanvas();
		} else {
			showOriginal();
		}
		return sourceImage;
	}

	/**
	 * Load the image from a URL.
	 *
	 * @param url
	 */
	public Image loadImage(URL url) {
		if (sourceImage != null && !sourceImage.isDisposed()) {
			sourceImage.dispose();
			sourceImage = null;
		}

		sourceImage = ImageDescriptor.createFromURL(url).createImage();

		if (sourceImage.getBounds().width > this.getBounds().width
				|| sourceImage.getBounds().height > this.getBounds().height) {
			fitCanvas();
		} else {
			showOriginal();
		}
		return sourceImage;
	}

	/**
	 * Load the image from a file
	 *
	 * @param filename
	 *
	 */
	public Image loadImage(InputStream is) {
		if (sourceImage != null && !sourceImage.isDisposed()) {
			sourceImage.dispose();
			sourceImage = null;
		}
		sourceImage = new Image(getDisplay(), is);

		if (sourceImage.getBounds().width > this.getBounds().width
				|| sourceImage.getBounds().height > this.getBounds().height) {
			fitCanvas();
		} else {
			showOriginal();
		}
		return sourceImage;
	}

	/**
	 * Adjust the image onto the canvas
	 */
	public void fitCanvas() {
		if (sourceImage == null) {
			return;
		}
		Rectangle imageBound = sourceImage.getBounds();
		Rectangle destRect = getClientArea();
		double sx = (double) destRect.width / (double) imageBound.width;
		double sy = (double) destRect.height / (double) imageBound.height;
		double s = Math.min(sx, sy);
		double dx = 0.5 * destRect.width;
		double dy = 0.5 * destRect.height;
		centerZoom(dx, dy, s, new AffineTransform());
	}

	/**
	 * Show the image with the original size
	 */
	public void showOriginal() {
		if (sourceImage == null) {
			return;
		}
		transform = new AffineTransform();
		syncScrollBars();
	}

	/**
	 * Clear the canvas
	 */
	public void clear() {
		if (sourceImage != null) {
			sourceImage.dispose();
			sourceImage = null;
			GC clearGC = new GC(this);
			paint(clearGC);
			clearGC.dispose();
		}
	}

	/**
	 * Perform a zooming operation.
	 *
	 * @param dx
	 * @param dy
	 * @param scale
	 * @param af
	 */
	public void centerZoom(double dx, double dy, double scale, AffineTransform af) {
		af.preConcatenate(AffineTransform.getTranslateInstance(-dx, -dy));
		af.preConcatenate(AffineTransform.getScaleInstance(scale, scale));
		af.preConcatenate(AffineTransform.getTranslateInstance(dx, dy));
		transform = af;
		syncScrollBars();
	}
}
