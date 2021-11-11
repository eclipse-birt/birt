/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.util.graphics;

import java.awt.geom.AffineTransform;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ScrollBar;

/**
 * Special Canvas class used to display the image.
 *
 */
public class ImageCanvas extends Canvas {

	private static final float ZOOMIN_RATE = 1.1f;

	private static final float ZOOMOUT_RATE = 0.9f;

	private Image sourceImage;

	private Image screenImage;

	private AffineTransform transform = new AffineTransform();

	/**
	 * The constructor.
	 *
	 * @param parent
	 */
	public ImageCanvas(final Composite parent) {
		this(parent, 0);
		initAccessible();
	}

	/**
	 * The constructor.
	 *
	 * @param parent
	 * @param style
	 *
	 */
	public ImageCanvas(final Composite parent, int style) {
		super(parent, style);
		addControlListener(new ControlAdapter() {

			public void controlResized(ControlEvent event) {
				syncScrollBars();
			}
		});
		addPaintListener(new PaintListener() {

			public void paintControl(final PaintEvent event) {
				paint(event.gc);
			}
		});
		initScrollBars();
		initAccessible();
	}

	void initAccessible() {
		getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {

			public void getChildAtPoint(AccessibleControlEvent e) {
				Point testPoint = toControl(e.x, e.y);
				if (getBounds().contains(testPoint)) {
					e.childID = ACC.CHILDID_SELF;
				}
			}

			public void getLocation(AccessibleControlEvent e) {
				Rectangle location = getBounds();
				Point pt = toDisplay(location.x, location.y);
				e.x = pt.x;
				e.y = pt.y;
				e.width = location.width;
				e.height = location.height;
			}

			public void getChildCount(AccessibleControlEvent e) {
				e.detail = 0;
			}

			public void getRole(AccessibleControlEvent e) {
				e.detail = ACC.ROLE_LABEL;
			}

			public void getState(AccessibleControlEvent e) {
				e.detail = ACC.STATE_NORMAL;
			}

			public void getValue(AccessibleControlEvent e) {
				e.result = "Preview Image"; //$NON-NLS-1$
			}

		});

		AccessibleAdapter accessibleAdapter = new AccessibleAdapter() {

			public void getHelp(AccessibleEvent e) {
				e.result = "Preview Image"; //$NON-NLS-1$
			}

			public void getName(AccessibleEvent e) {
				e.result = "Preview Image"; //$NON-NLS-1$
			}
		};
		getAccessible().addAccessibleListener(accessibleAdapter);
	}

	public void dispose() {
		if (sourceImage != null && !sourceImage.isDisposed()) {
			sourceImage.dispose();
		}

		if (screenImage != null && !screenImage.isDisposed()) {
			screenImage.dispose();
		}
		super.dispose();
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

			if (screenImage != null)
				screenImage.dispose();
			screenImage = new Image(getDisplay(), clientRect.width, clientRect.height);
			addDisposeListener(e -> UIUtil.dispose(screenImage));
			GC newGC = new GC(screenImage);
			newGC.setClipping(clientRect);
			newGC.drawImage(sourceImage, imageRect.x, imageRect.y, imageRect.width, imageRect.height, destRect.x,
					destRect.y, destRect.width, destRect.height);
			newGC.dispose();

			gc.drawImage(screenImage, 0, 0);
		} else {
			gc.setClipping(clientRect);
			gc.fillRectangle(clientRect);
			initScrollBars();
		}
	}

	private void initScrollBars() {

		ScrollBar horizontal = getHorizontalBar();
		if (horizontal != null) {
			horizontal.setEnabled(false);
			horizontal.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent event) {
					scrollHorizontally((ScrollBar) event.widget);
				}
			});
		}
		ScrollBar vertical = getVerticalBar();
		if (vertical != null) {
			vertical.setEnabled(false);
			vertical.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent event) {
					scrollVertically((ScrollBar) event.widget);
				}
			});
		}

	}

	private void scrollHorizontally(ScrollBar scrollBar) {
		if (sourceImage == null)
			return;

		AffineTransform af = transform;
		double tx = af.getTranslateX();
		double select = -scrollBar.getSelection();
		af.preConcatenate(AffineTransform.getTranslateInstance(select - tx, 0));
		transform = af;
		syncScrollBars();
	}

	private void scrollVertically(ScrollBar scrollBar) {
		if (sourceImage == null)
			return;

		AffineTransform af = transform;
		double ty = af.getTranslateY();
		double select = -scrollBar.getSelection();
		af.preConcatenate(AffineTransform.getTranslateInstance(0, select - ty));
		transform = af;
		syncScrollBars();
	}

	/**
	 * Returns the Source image.
	 *
	 * @return sourceImage.
	 */
	public Image getSourceImage() {
		return sourceImage;
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
		if (tx > 0)
			tx = 0;
		if (ty > 0)
			ty = 0;

		Rectangle imageBound = sourceImage.getBounds();
		int cw = getClientArea().width, ch = getClientArea().height;

		ScrollBar horizontal = getHorizontalBar();

		if (horizontal != null) {
			horizontal.setIncrement((int) (getClientArea().width / 100));
			horizontal.setPageIncrement(getClientArea().width);

			if (imageBound.width * sx > cw) {
				horizontal.setMaximum((int) (imageBound.width * sx));
				horizontal.setEnabled(true);
				if (((int) -tx) > horizontal.getMaximum() - cw)
					tx = -horizontal.getMaximum() + cw;
			} else {
				horizontal.setEnabled(false);
				tx = (cw - imageBound.width * sx) / 2;
			}
			horizontal.setSelection((int) (-tx));
			horizontal.setThumb((int) (getClientArea().width));
		}
		ScrollBar vertical = getVerticalBar();
		if (vertical != null) {
			vertical.setIncrement((int) (getClientArea().height / 100));
			vertical.setPageIncrement((int) (getClientArea().height));
			if (imageBound.height * sy > ch) {
				vertical.setMaximum((int) (imageBound.height * sy));
				vertical.setEnabled(true);
				if (((int) -ty) > vertical.getMaximum() - ch)
					ty = -vertical.getMaximum() + ch;
			} else {
				vertical.setEnabled(false);
				ty = (ch - imageBound.height * sy) / 2;
			}
			vertical.setSelection((int) (-ty));
			vertical.setThumb((int) (getClientArea().height));
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
	 * @return
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
	 * Load image from a byte array.
	 *
	 * @param filename
	 * @return
	 */
	public Image loadImage(byte[] data) {
		return loadImage(new ByteArrayInputStream(data));
	}

	/**
	 * Load the image to canvas.
	 *
	 * @param img
	 * @return the new image
	 */
	public Image loadImage(Image img) {
		if (sourceImage != null && !sourceImage.isDisposed()) {
			sourceImage.dispose();
			sourceImage = null;
		}

		if (img == null) {
			sourceImage = null;
			redraw();
			return null;
		}
		sourceImage = new Image(getDisplay(), img.getImageData());
		addDisposeListener(e -> UIUtil.dispose(sourceImage));

		if (sourceImage.getBounds().width > this.getBounds().width
				|| sourceImage.getBounds().height > this.getBounds().height) {
			fitCanvas();
		} else {
			showOriginal();
		}
		return sourceImage;

	}

	/**
	 * @param sourceImage2
	 */
	private void doDispose(Image sourceImage2) {
		// TODO Auto-generated method stub

	}

	/**
	 * Load image from a input-stream.
	 *
	 * @param is
	 * @return
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
	 * Load the image from a URL.
	 *
	 * @param url
	 * @return
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
	 * Reset the image data and update the image.
	 *
	 * @param data
	 */
	public void setImageData(ImageData data) {
		if (sourceImage != null) {
			sourceImage.dispose();
			sourceImage = null;
		}
		if (data != null)
			sourceImage = new Image(getDisplay(), data);
		syncScrollBars();
	}

	/**
	 * Adjust the image onto the canvas
	 */
	public void fitCanvas() {
		if (sourceImage == null)
			return;
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
		if (sourceImage == null)
			return;
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
		}
		GC clearGC = new GC(this);
		paint(clearGC);
		clearGC.dispose();
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

	/**
	 * Zoom in the image.
	 */
	public void zoomIn() {
		if (sourceImage == null)
			return;
		Rectangle rect = getClientArea();
		int w = rect.width, h = rect.height;
		double dx = ((double) w) / 2;
		double dy = ((double) h) / 2;
		centerZoom(dx, dy, ZOOMIN_RATE, transform);
	}

	/**
	 * Zoom out the image.
	 */
	public void zoomOut() {
		if (sourceImage == null)
			return;
		Rectangle rect = getClientArea();
		int w = rect.width, h = rect.height;
		double dx = ((double) w) / 2;
		double dy = ((double) h) / 2;
		centerZoom(dx, dy, ZOOMOUT_RATE, transform);
	}
}