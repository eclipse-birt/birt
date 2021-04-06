/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.swt.custom;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.Accessible;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * A Label which supports aligned an image and different border styles.
 * <p>
 * <dl>
 * <dt><b>Styles: </b>
 * <dd>LEFT, RIGHT, CENTER, SHADOW_IN, SHADOW_OUT, SHADOW_NONE</dd>
 * <dt><b>Events: </b>
 * <dd></dd>
 * </dl>
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class ImageLabel extends Canvas {

	/** Left and right margins */
	private static final int INDENT = 3;

	/** the alignment. Either CENTER, RIGHT, LEFT. Default is LEFT */
	private int align = SWT.LEFT;

	private int hIndent = INDENT;

	private int vIndent = INDENT;

	/** the current icon */
	private Image image;

	// Disposes a description for the image
	private String appToolTipText;

	private Color backgroundColor;

	/**
	 * Constructs a new instance of this class given its parent and a style value
	 * describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must be
	 * built by <em>bitwise OR</em> 'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code> style
	 * constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 * 
	 * @param parent a widget which will be the parent of the new instance (cannot
	 *               be null)
	 * @param style  the style of widget to construct
	 * 
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the parent
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     parent</li>
	 *                                     </ul>
	 * 
	 * @see SWT#LEFT
	 * @see SWT#RIGHT
	 * @see SWT#CENTER
	 * @see SWT#SHADOW_IN
	 * @see SWT#SHADOW_OUT
	 * @see SWT#SHADOW_NONE
	 * @see #getStyle()
	 */
	public ImageLabel(Composite parent, int style) {
		super(parent, checkStyle(style));

		if ((style & SWT.CENTER) != 0)
			align = SWT.CENTER;
		if ((style & SWT.RIGHT) != 0)
			align = SWT.RIGHT;
		if ((style & SWT.LEFT) != 0)
			align = SWT.LEFT;

		addPaintListener(new PaintListener() {

			public void paintControl(PaintEvent event) {
				onPaint(event);
			}
		});

		addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {
				redraw();
			}

			public void focusLost(FocusEvent e) {
				redraw();
			}
		});

		addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent event) {
				onDispose(event);
			}
		});

		addTraverseListener(new TraverseListener() {

			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
					e.doit = true;
				}
			};
		});

		initAccessible();
	}

	void initAccessible() {
		getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {

			public void getChildAtPoint(AccessibleControlEvent e) {
				Point pt = toControl(new Point(e.x, e.y));
				e.childID = (getBounds().contains(pt)) ? ACC.CHILDID_SELF : ACC.CHILDID_NONE;
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
		});

		Accessible accessible = getAccessible();
		accessible.addAccessibleListener(new AccessibleAdapter() {

			public void getName(AccessibleEvent e) {
				getHelp(e);
			}

			public void getHelp(AccessibleEvent e) {
				e.result = getToolTipText();
			}
		});
	}

	/**
	 * Check the style bits to ensure that no invalid styles are applied.
	 */
	private static int checkStyle(int style) {
		if ((style & SWT.BORDER) != 0)
			style |= SWT.SHADOW_IN;
		int mask = SWT.SHADOW_IN | SWT.SHADOW_OUT | SWT.SHADOW_NONE | SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		style = style & mask;
		style |= SWT.NO_FOCUS;
		if ((style & (SWT.CENTER | SWT.RIGHT)) == 0)
			style |= SWT.LEFT;
		// TEMPORARY CODE
		/*
		 * The default background on carbon and some GTK themes is not a solid color but
		 * a texture. To show the correct default background, we must allow the
		 * operating system to draw it and therefore, we can not use the NO_BACKGROUND
		 * style. The NO_BACKGROUND style is not required on platforms that use double
		 * buffering which is true in both of these cases.
		 */
		String platform = SWT.getPlatform();
		if ("carbon".equals(platform) || "gtk".equals(platform)) //$NON-NLS-1$ //$NON-NLS-2$
			return style;
		return style | SWT.NO_BACKGROUND;
	}

	// protected void checkSubclass () {
	// String name = getClass().getName ();
	// String validName = CLabel.class.getName();
	// if (!validName.equals(name)) {
	// SWT.error (SWT.ERROR_INVALID_SUBCLASS);
	// }
	// }

	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();
		Point e = getTotalSize(image);
		if (wHint == SWT.DEFAULT) {
			e.x += 2 * hIndent;
		} else {
			e.x = wHint;
		}
		if (hHint == SWT.DEFAULT) {
			e.y += 2 * vIndent;
		} else {
			e.y = hHint;
		}
		return e;
	}

	/**
	 * Draw a rectangle in the given colors.
	 */
	private void drawBevelRect(GC gc, int x, int y, int w, int h, Color topleft, Color bottomright) {
		gc.setForeground(bottomright);
		gc.drawLine(x + w, y, x + w, y + h);
		gc.drawLine(x, y + h, x + w, y + h);

		gc.setForeground(topleft);
		gc.drawLine(x, y, x + w - 1, y);
		gc.drawLine(x, y, x, y + h - 1);
	}

	/**
	 * Returns the alignment. The alignment style (LEFT, CENTER or RIGHT) is
	 * returned.
	 * 
	 * @return SWT.LEFT, SWT.RIGHT or SWT.CENTER
	 */
	public int getAlignment() {
		// checkWidget();
		return align;
	}

	/**
	 * Return the CLabel's image or <code>null</code>.
	 * 
	 * @return the image of the label or null
	 */
	public Image getImage() {
		// checkWidget();
		return image;
	}

	/**
	 * Compute the minimum size.
	 */
	private Point getTotalSize(Image image) {
		Point size = new Point(0, 0);

		if (image != null) {
			Rectangle r = image.getBounds();
			size.x += r.width;
			size.y += r.height;
		}
		return size;
	}

	public void setToolTipText(String string) {
		super.setToolTipText(string);
		appToolTipText = super.getToolTipText();
	}

	public String getToolTipText() {
		checkWidget();
		return appToolTipText;
	}

	/**
	 * Paint the Label's border.
	 */
	private void paintBorder(GC gc, Rectangle r) {
		Display disp = getDisplay();

		Color c1 = null;
		Color c2 = null;

		int style = getStyle();
		if ((style & SWT.SHADOW_IN) != 0) {
			c1 = disp.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
			c2 = disp.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW);
		}
		if ((style & SWT.SHADOW_OUT) != 0) {
			c1 = disp.getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);
			c2 = disp.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
		}

		if (c1 != null && c2 != null) {
			gc.setLineWidth(1);
			drawBevelRect(gc, r.x, r.y, r.width - 1, r.height - 1, c1, c2);
		}
	}

	void onDispose(DisposeEvent event) {
		image = null;
		appToolTipText = null;
	}

	/*
	 * Process the paint event
	 */
	void onPaint(PaintEvent event) {
		Rectangle rect = getClientArea();
		if (rect.width == 0 || rect.height == 0)
			return;

		Image img = image;
		Point extent = getTotalSize(img);

		GC gc = event.gc;

		// determine horizontal position
		int x = rect.x + hIndent;
		if (align == SWT.CENTER) {
			x = (rect.width - extent.x) / 2;
		}
		if (align == SWT.RIGHT) {
			x = rect.width - extent.x - hIndent;
		}

		if (this.backgroundColor != null) {
			Color oldBackground = gc.getBackground();
			gc.setBackground(backgroundColor);
			gc.fillRectangle(0, 0, rect.width, rect.height);
			gc.setBackground(oldBackground);
		} else {
			if ((getStyle() & SWT.NO_BACKGROUND) != 0) {
				gc.setBackground(getBackground());
				gc.fillRectangle(rect);
			}
		}

		// draw border
		int style = getStyle();
		if ((style & SWT.SHADOW_IN) != 0 || (style & SWT.SHADOW_OUT) != 0) {
			paintBorder(gc, rect);
		}
		// draw the image
		if (img != null) {
			Rectangle imageRect = img.getBounds();
			if (this.isFocusControl()) {

				ImageData data = img.getImageData();
				PaletteData palette = new PaletteData(
						new RGB[] { this.getDisplay().getSystemColor(SWT.COLOR_WHITE).getRGB(),
								this.getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION).getRGB(), });
				ImageData sourceData = new ImageData(data.width, data.height, 1, palette);
				for (int i = 0; i < data.width; i++) {
					for (int j = 0; j < data.height; j++) {
						if (data.getPixel(i, j) != data.transparentPixel)
							sourceData.setPixel(i, j, 1);
					}
				}

				Image highlightImage = new Image(this.getDisplay(), sourceData);

				gc.drawImage(highlightImage, 0, 0, imageRect.width, imageRect.height, x,
						(rect.height - imageRect.height) / 2, rect.width - 10, imageRect.height);
				highlightImage.dispose();
			} else
				gc.drawImage(img, 0, 0, imageRect.width, imageRect.height, x, (rect.height - imageRect.height) / 2,
						rect.width - 10, imageRect.height);
			x += imageRect.width;
		}
	}

	/**
	 * Set the alignment of the CLabel. Use the values LEFT, CENTER and RIGHT to
	 * align image and text within the available space.
	 * 
	 * @param align the alignment style of LEFT, RIGHT or CENTER
	 * 
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         <li>ERROR_INVALID_ARGUMENT - if the value of align is
	 *                         not one of SWT.LEFT, SWT.RIGHT or SWT.CENTER</li>
	 *                         </ul>
	 */
	public void setAlignment(int align) {
		checkWidget();
		if (align != SWT.LEFT && align != SWT.RIGHT && align != SWT.CENTER) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (this.align != align) {
			this.align = align;
			redraw();
		}
	}

	public void setBackground(Color color) {
		super.setBackground(color);
		// Are these settings the same as before?
		if (color != null) {
			Color background = getBackground();
			if (color.equals(background)) {
				return;
			}
		}
		backgroundColor = color;
		redraw();
	}

	public void setFont(Font font) {
		super.setFont(font);
		redraw();
	}

	/**
	 * Set the label's Image. The value <code>null</code> clears it.
	 * 
	 * @param image the image to be displayed in the label or null
	 * 
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setImage(Image image) {
		checkWidget();
		if (image != this.image) {
			this.image = image;
			redraw();
		}
	}

}
