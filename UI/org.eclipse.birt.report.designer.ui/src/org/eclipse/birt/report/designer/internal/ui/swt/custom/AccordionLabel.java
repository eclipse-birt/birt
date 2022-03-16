/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.swt.custom;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.Accessible;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * A Label which supports aligned text and/or an image and different border
 * styles.
 * <p>
 * If there is not enough space a CLabel uses the following strategy to fit the
 * information into the available space:
 *
 * <pre>
 * 		ignores the indent in left align mode
 * 		ignores the image and the gap
 * 		shortens the text by replacing the center portion of the label with an ellipsis
 * 		shortens the text by removing the center portion of the label
 * </pre>
 * <p>
 * <dl>
 * <dt><b>Styles:</b>
 * <dd>LEFT, RIGHT, CENTER, SHADOW_IN, SHADOW_OUT, SHADOW_NONE</dd>
 * <dt><b>Events:</b>
 * <dd></dd>
 * </dl>
 *
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      CustomControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class AccordionLabel extends Canvas {

	/** Gap between icon and text */
	private static final int GAP = 5;
	/** Left and right margins */
	private static final int DEFAULT_MARGIN = 3;
	/** a string inserted in the middle of text that has been shortened */
	private static final String ELLIPSIS = "..."; //$NON-NLS-1$ // could use the ellipsis glyph on some platforms
													// "\u2026"
	/** the alignment. Either CENTER, RIGHT, LEFT. Default is LEFT */
	private int align = SWT.LEFT;
	private int leftMargin = DEFAULT_MARGIN;
	private int topMargin = DEFAULT_MARGIN;
	private int rightMargin = DEFAULT_MARGIN + 2;
	private int bottomMargin = DEFAULT_MARGIN;
	private int actionImageWidth = 16;
	private int actionImageHeight = 16;
	/** the current text */
	private String text;
	/** the current icon */
	private Image image;
	// The tooltip is used for two purposes - the application can set
	// a tooltip or the tooltip can be used to display the full text when the
	// the text has been truncated due to the label being too short.
	// The appToolTip stores the tooltip set by the application.
	// Control.tooltiptext
	// contains whatever tooltip is currently being displayed.
	private String appToolTipText;
	private boolean ignoreDispose;

	private Image backgroundImage;
	private Color[] gradientColors;
	private int[] gradientPercents;
	private boolean gradientVertical;
	private Color background;

	private static int DRAW_FLAGS = SWT.DRAW_MNEMONIC | SWT.DRAW_TAB | SWT.DRAW_TRANSPARENT | SWT.DRAW_DELIMITER;

	/**
	 * Constructs a new instance of this class given its parent and a style value
	 * describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must be
	 * built by <em>bitwise OR</em>'ing together (that is, using the
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
	public AccordionLabel(Composite parent, int style) {
		super(parent, checkStyle(style));
		if ((style & (SWT.CENTER | SWT.RIGHT)) == 0) {
			style |= SWT.LEFT;
		}
		if ((style & SWT.CENTER) != 0) {
			align = SWT.CENTER;
		}
		if ((style & SWT.RIGHT) != 0) {
			align = SWT.RIGHT;
		}
		if ((style & SWT.LEFT) != 0) {
			align = SWT.LEFT;
		}

		addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent event) {
				onPaint(event);
			}
		});

		addTraverseListener(new TraverseListener() {

			@Override
			public void keyTraversed(TraverseEvent event) {
				if (event.detail == SWT.TRAVERSE_MNEMONIC) {
					onMnemonic(event);
				}
			}
		});

		addListener(SWT.Dispose, new Listener() {

			@Override
			public void handleEvent(Event event) {
				onDispose(event);
			}
		});

		initAccessible();

	}

	/**
	 * Check the style bits to ensure that no invalid styles are applied.
	 */
	private static int checkStyle(int style) {
		if ((style & SWT.BORDER) != 0) {
			style |= SWT.SHADOW_IN;
		}
		int mask = SWT.SHADOW_IN | SWT.SHADOW_OUT | SWT.SHADOW_NONE | SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		style = style & mask;
		return style |= SWT.NO_FOCUS | SWT.DOUBLE_BUFFERED;
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();
		Point e = getTotalSize(image, text);
		if (wHint == SWT.DEFAULT) {
			e.x += leftMargin + rightMargin;
		} else {
			e.x = wHint;
		}
		if (hHint == SWT.DEFAULT) {
			e.y += topMargin + bottomMargin;
		} else {
			e.y = hHint;
		}
		return e;
	}

	/*
	 * Return the lowercase of the first non-'&' character following an '&'
	 * character in the given string. If there are no '&' characters in the given
	 * string, return '\0'.
	 */
	char _findMnemonic(String string) {
		if (string == null) {
			return '\0';
		}
		int index = 0;
		int length = string.length();
		do {
			while (index < length && string.charAt(index) != '&') {
				index++;
			}
			if (++index >= length) {
				return '\0';
			}
			if (string.charAt(index) != '&') {
				return Character.toLowerCase(string.charAt(index));
			}
			index++;
		} while (index < length);
		return '\0';
	}

	/**
	 * Returns the horizontal alignment. The alignment style (LEFT, CENTER or RIGHT)
	 * is returned.
	 *
	 * @return SWT.LEFT, SWT.RIGHT or SWT.CENTER
	 */
	public int getAlignment() {
		// checkWidget();
		return align;
	}

	/**
	 * Return the CLabel's bottom margin.
	 *
	 * @return the bottom margin of the label
	 *
	 * @since 3.6
	 */
	public int getBottomMargin() {
		// checkWidget();
		return bottomMargin;
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
	 * Return the CLabel's left margin.
	 *
	 * @return the left margin of the label
	 *
	 * @since 3.6
	 */
	public int getLeftMargin() {
		// checkWidget();
		return leftMargin;
	}

	/**
	 * Return the CLabel's right margin.
	 *
	 * @return the right margin of the label
	 *
	 * @since 3.6
	 */
	public int getRightMargin() {
		// checkWidget();
		return rightMargin;
	}

	/**
	 * Compute the minimum size.
	 */
	private Point getTotalSize(Image image, String text) {
		Point size = new Point(0, 0);

		if (image != null) {
			Rectangle r = image.getBounds();
			size.x += r.width;
			size.y += r.height;
		}

		GC gc = new GC(this);
		if (text != null && text.length() > 0) {
			Point e = gc.textExtent(text, DRAW_FLAGS);
			size.x += e.x;
			size.y = Math.max(size.y, e.y);
			if (image != null) {
				size.x += GAP;
			}
		} else {
			size.y = Math.max(size.y, gc.getFontMetrics().getHeight());
		}
		gc.dispose();

		return size;
	}

	@Override
	public int getStyle() {
		int style = super.getStyle();
		switch (align) {
		case SWT.RIGHT:
			style |= SWT.RIGHT;
			break;
		case SWT.CENTER:
			style |= SWT.CENTER;
			break;
		case SWT.LEFT:
			style |= SWT.LEFT;
			break;
		}
		return style;
	}

	/**
	 * Return the Label's text.
	 *
	 * @return the text of the label or null
	 */
	public String getText() {
		// checkWidget();
		return text;
	}

	@Override
	public String getToolTipText() {
		checkWidget();
		return appToolTipText;
	}

	/**
	 * Return the CLabel's top margin.
	 *
	 * @return the top margin of the label
	 *
	 * @since 3.6
	 */
	public int getTopMargin() {
		// checkWidget();
		return topMargin;
	}

	private void initAccessible() {
		Accessible accessible = getAccessible();
		accessible.addAccessibleListener(new AccessibleAdapter() {

			@Override
			public void getName(AccessibleEvent e) {
				e.result = getText();
			}

			@Override
			public void getHelp(AccessibleEvent e) {
				e.result = getToolTipText();
			}

			@Override
			public void getKeyboardShortcut(AccessibleEvent e) {
				char mnemonic = _findMnemonic(AccordionLabel.this.text);
				if (mnemonic != '\0') {
					e.result = "Alt+" + mnemonic; //$NON-NLS-1$
				}
			}
		});

		accessible.addAccessibleControlListener(new AccessibleControlAdapter() {

			@Override
			public void getChildAtPoint(AccessibleControlEvent e) {
				e.childID = ACC.CHILDID_SELF;
			}

			@Override
			public void getLocation(AccessibleControlEvent e) {
				Rectangle rect = getDisplay().map(getParent(), null, getBounds());
				e.x = rect.x;
				e.y = rect.y;
				e.width = rect.width;
				e.height = rect.height;
			}

			@Override
			public void getChildCount(AccessibleControlEvent e) {
				e.detail = 0;
			}

			@Override
			public void getRole(AccessibleControlEvent e) {
				e.detail = ACC.ROLE_LABEL;
			}

			@Override
			public void getState(AccessibleControlEvent e) {
				e.detail = ACC.STATE_READONLY;
			}
		});
	}

	void onDispose(Event event) {
		/* make this handler run after other dispose listeners */
		if (ignoreDispose) {
			ignoreDispose = false;
			return;
		}
		ignoreDispose = true;
		notifyListeners(event.type, event);
		event.type = SWT.NONE;

		gradientColors = null;
		gradientPercents = null;
		backgroundImage = null;
		text = null;
		image = null;
		appToolTipText = null;
	}

	void onMnemonic(TraverseEvent event) {
		char mnemonic = _findMnemonic(text);
		if ((mnemonic == '\0') || (Character.toLowerCase(event.character) != mnemonic)) {
			return;
		}
		Composite control = this.getParent();
		while (control != null) {
			Control[] children = control.getChildren();
			int index = 0;
			while (index < children.length) {
				if (children[index] == this) {
					break;
				}
				index++;
			}
			index++;
			if (index < children.length) {
				if (children[index].setFocus()) {
					event.doit = true;
					event.detail = SWT.TRAVERSE_NONE;
				}
			}
			control = control.getParent();
		}
	}

	void onPaint(PaintEvent event) {
		Rectangle rect = getClientArea();
		if (rect.width == 0 || rect.height == 0) {
			return;
		}

		boolean shortenText = false;
		String t = text;
		Image img = image;
		int availableWidth = Math.max(0, rect.width - (leftMargin + rightMargin));
		Point extent = getTotalSize(img, t);
		if (extent.x > availableWidth) {
			shortenText = true;
		}

		GC gc = event.gc;
		String[] lines = text == null ? null : splitString(text);

		// shorten the text
		if (shortenText) {
			extent.x = 0;
			for (int i = 0; i < lines.length; i++) {
				Point e = gc.textExtent(lines[i], DRAW_FLAGS);
				int width = availableWidth;
				if (img != null) {
					width -= (image.getBounds().width + GAP);
				}
				if (e.x > width) {
					lines[i] = shortenText(gc, lines[i], width);
					extent.x = Math.max(extent.x, getTotalSize(img, lines[i]).x);
				} else {
					extent.x = Math.max(extent.x, e.x);
				}
			}
			if (appToolTipText == null) {
				super.setToolTipText(text);
			}
		} else {
			super.setToolTipText(appToolTipText);
		}

		// determine horizontal position
		int x = rect.x + leftMargin;
		if (align == SWT.CENTER) {
			x = (rect.width - extent.x) / 2;
		}
		if (align == SWT.RIGHT) {
			x = rect.width - rightMargin - extent.x;
		}

		// draw a background image behind the text
		try {
			if (backgroundImage != null) {
				// draw a background image behind the text
				Rectangle imageRect = backgroundImage.getBounds();
				// tile image to fill space
				gc.setBackground(getBackground());
				gc.fillRectangle(rect);
				int xPos = 0;
				while (xPos < rect.width) {
					int yPos = 0;
					while (yPos < rect.height) {
						gc.drawImage(backgroundImage, xPos, yPos);
						yPos += imageRect.height;
					}
					xPos += imageRect.width;
				}
			} else if (gradientColors != null) {
				// draw a gradient behind the text
				final Color oldBackground = gc.getBackground();
				if (gradientColors.length == 1) {
					if (gradientColors[0] != null) {
						gc.setBackground(gradientColors[0]);
					}
					gc.fillRectangle(0, 0, rect.width, rect.height);
				} else {
					final Color oldForeground = gc.getForeground();
					Color lastColor = gradientColors[0];
					if (lastColor == null) {
						lastColor = oldBackground;
					}
					int pos = 0;
					for (int i = 0; i < gradientPercents.length; ++i) {
						gc.setForeground(lastColor);
						lastColor = gradientColors[i + 1];
						if (lastColor == null) {
							lastColor = oldBackground;
						}
						gc.setBackground(lastColor);
						if (gradientVertical) {
							final int gradientHeight = (gradientPercents[i] * rect.height / 100) - pos;
							gc.fillGradientRectangle(0, pos, rect.width, gradientHeight, true);
							pos += gradientHeight;
						} else {
							final int gradientWidth = (gradientPercents[i] * rect.width / 100) - pos;
							gc.fillGradientRectangle(pos, 0, gradientWidth, rect.height, false);
							pos += gradientWidth;
						}
					}
					if (gradientVertical && pos < rect.height) {
						gc.setBackground(getBackground());
						gc.fillRectangle(0, pos, rect.width, rect.height - pos);
					}
					if (!gradientVertical && pos < rect.width) {
						gc.setBackground(getBackground());
						gc.fillRectangle(pos, 0, rect.width - pos, rect.height);
					}
					gc.setForeground(oldForeground);
				}
				gc.setBackground(oldBackground);
			} else if (background != null || (getStyle() & SWT.DOUBLE_BUFFERED) == 0) {
				gc.setBackground(getBackground());
				gc.fillRectangle(rect);
			}
		} catch (SWTException e) {
			if ((getStyle() & SWT.DOUBLE_BUFFERED) == 0) {
				gc.setBackground(getBackground());
				gc.fillRectangle(rect);
			}
		}

		/*
		 * Compute text height and image height. If image height is more than the text
		 * height, draw image starting from top margin. Else draw text starting from top
		 * margin.
		 */
		int lineHeight = 0, textHeight = 0, imageHeight = 0;

		if (img != null) {
			imageHeight = actionImageHeight;
		}
		if (lines != null) {
			lineHeight = gc.getFontMetrics().getHeight();
			textHeight = lines.length * lineHeight;
		}

		int imageY = 0, midPoint = 0, lineY = 0;
		if (imageHeight > textHeight) {
			if (topMargin == DEFAULT_MARGIN && bottomMargin == DEFAULT_MARGIN) {
				imageY = rect.y + (rect.height - imageHeight) / 2;
			} else {
				imageY = topMargin;
			}
			midPoint = imageY + imageHeight / 2;
			lineY = midPoint - textHeight / 2;
		} else {
			if (topMargin == DEFAULT_MARGIN && bottomMargin == DEFAULT_MARGIN) {
				lineY = rect.y + (rect.height - textHeight) / 2;
			} else {
				lineY = topMargin;
			}
			midPoint = lineY + textHeight / 2;
			imageY = midPoint - imageHeight / 2;
		}

		// draw the image
		if (img != null) {
			gc.drawImage(img, 0, 0, actionImageWidth, imageHeight, x, imageY, actionImageWidth, imageHeight);
			x += actionImageWidth + GAP;
			extent.x -= actionImageWidth + GAP;
		}

		// draw the text
		if (lines != null) {
			gc.setForeground(getForeground());
			for (int i = 0; i < lines.length; i++) {
				int lineX = x;
				if (lines.length > 1) {
					if (align == SWT.CENTER) {
						int lineWidth = gc.textExtent(lines[i], DRAW_FLAGS).x;
						lineX = x + Math.max(0, (extent.x - lineWidth) / 2);
					}
					if (align == SWT.RIGHT) {
						int lineWidth = gc.textExtent(lines[i], DRAW_FLAGS).x;
						lineX = Math.max(x, rect.x + rect.width - rightMargin - lineWidth);
					}
				}
				gc.drawText(lines[i], lineX, lineY, DRAW_FLAGS);
				lineY += lineHeight;
			}
		}

		// draw the image
		if (actionImage != null) {
			gc.drawImage(actionImage, 0, 0, actionImageWidth, actionImageHeight,
					rect.x + rect.width - rightMargin - actionImageWidth, imageY, actionImageWidth, actionImageHeight);

		}

		gc.setForeground(FormWidgetFactory.getInstance().getColors().getBorderColor());

//		gc.drawLine( rect.x
//				+ rect.width
//				- rightMargin
//				- actionImageWidth
//				- rightMargin, rect.y, rect.x
//				+ rect.width
//				- rightMargin
//				- actionImageWidth
//				- rightMargin, rect.y + rect.height );
	}

	/**
	 * Set the horizontal alignment of the CLabel. Use the values LEFT, CENTER and
	 * RIGHT to align image and text within the available space.
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

	@Override
	public void setBackground(Color color) {
		super.setBackground(color);
		// Are these settings the same as before?
		if (backgroundImage == null && gradientColors == null && gradientPercents == null) {
			if (color == null) {
				if (background == null) {
					return;
				}
			} else if (color.equals(background)) {
				return;
			}
		}
		background = color;
		backgroundImage = null;
		gradientColors = null;
		gradientPercents = null;
		redraw();
	}

	/**
	 * Specify a gradient of colours to be drawn in the background of the CLabel.
	 * <p>
	 * For example, to draw a gradient that varies from dark blue to blue and then
	 * to white and stays white for the right half of the label, use the following
	 * call to setBackground:
	 * </p>
	 *
	 * <pre>
	 * clabel.setBackground(
	 * 		new Color[] { display.getSystemColor(SWT.COLOR_DARK_BLUE), display.getSystemColor(SWT.COLOR_BLUE),
	 * 				display.getSystemColor(SWT.COLOR_WHITE), display.getSystemColor(SWT.COLOR_WHITE) },
	 * 		new int[] { 25, 50, 100 });
	 * </pre>
	 *
	 * @param colors   an array of Color that specifies the colors to appear in the
	 *                 gradient in order of appearance from left to right; The value
	 *                 <code>null</code> clears the background gradient; the value
	 *                 <code>null</code> can be used inside the array of Color to
	 *                 specify the background color.
	 * @param percents an array of integers between 0 and 100 specifying the percent
	 *                 of the width of the widget at which the color should change;
	 *                 the size of the percents array must be one less than the size
	 *                 of the colors array.
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         <li>ERROR_INVALID_ARGUMENT - if the values of colors
	 *                         and percents are not consistent</li>
	 *                         </ul>
	 */
	public void setBackground(Color[] colors, int[] percents) {
		setBackground(colors, percents, false);
	}

	/**
	 * Specify a gradient of colours to be drawn in the background of the CLabel.
	 * <p>
	 * For example, to draw a gradient that varies from dark blue to white in the
	 * vertical, direction use the following call to setBackground:
	 * </p>
	 *
	 * <pre>
	 * clabel.setBackground(
	 * 		new Color[] { display.getSystemColor(SWT.COLOR_DARK_BLUE), display.getSystemColor(SWT.COLOR_WHITE) },
	 * 		new int[] { 100 }, true);
	 * </pre>
	 *
	 * @param colors   an array of Color that specifies the colors to appear in the
	 *                 gradient in order of appearance from left/top to
	 *                 right/bottom; The value <code>null</code> clears the
	 *                 background gradient; the value <code>null</code> can be used
	 *                 inside the array of Color to specify the background color.
	 * @param percents an array of integers between 0 and 100 specifying the percent
	 *                 of the width/height of the widget at which the color should
	 *                 change; the size of the percents array must be one less than
	 *                 the size of the colors array.
	 * @param vertical indicate the direction of the gradient. True is vertical and
	 *                 false is horizontal.
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         <li>ERROR_INVALID_ARGUMENT - if the values of colors
	 *                         and percents are not consistent</li>
	 *                         </ul>
	 *
	 * @since 3.0
	 */
	public void setBackground(Color[] colors, int[] percents, boolean vertical) {
		checkWidget();
		if (colors != null) {
			if (percents == null || percents.length != colors.length - 1) {
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
			if (getDisplay().getDepth() < 15) {
				// Don't use gradients on low color displays
				colors = new Color[] { colors[colors.length - 1] };
				percents = new int[] {};
			}
			for (int i = 0; i < percents.length; i++) {
				if (percents[i] < 0 || percents[i] > 100) {
					SWT.error(SWT.ERROR_INVALID_ARGUMENT);
				}
				if (i > 0 && percents[i] < percents[i - 1]) {
					SWT.error(SWT.ERROR_INVALID_ARGUMENT);
				}
			}
		}

		// Are these settings the same as before?
		final Color background = getBackground();
		if (backgroundImage == null) {
			if ((gradientColors != null) && (colors != null) && (gradientColors.length == colors.length)) {
				boolean same = false;
				for (int i = 0; i < gradientColors.length; i++) {
					same = (gradientColors[i] == colors[i])
							|| ((gradientColors[i] == null) && (colors[i] == background))
							|| ((gradientColors[i] == background) && (colors[i] == null));
					if (!same) {
						break;
					}
				}
				if (same) {
					for (int i = 0; i < gradientPercents.length; i++) {
						same = gradientPercents[i] == percents[i];
						if (!same) {
							break;
						}
					}
				}
				if (same && this.gradientVertical == vertical) {
					return;
				}
			}
		} else {
			backgroundImage = null;
		}
		// Store the new settings
		if (colors == null) {
			gradientColors = null;
			gradientPercents = null;
			gradientVertical = false;
		} else {
			gradientColors = new Color[colors.length];
			for (int i = 0; i < colors.length; ++i) {
				gradientColors[i] = (colors[i] != null) ? colors[i] : background;
			}
			gradientPercents = new int[percents.length];
			for (int i = 0; i < percents.length; ++i) {
				gradientPercents[i] = percents[i];
			}
			gradientVertical = vertical;
		}
		// Refresh with the new settings
		redraw();
	}

	/**
	 * Set the image to be drawn in the background of the label.
	 *
	 * @param image the image to be drawn in the background
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setBackground(Image image) {
		checkWidget();
		if (image == backgroundImage) {
			return;
		}
		if (image != null) {
			gradientColors = null;
			gradientPercents = null;
		}
		backgroundImage = image;
		redraw();

	}

	/**
	 * Set the label's bottom margin, in pixels.
	 *
	 * @param bottomMargin the bottom margin of the label, which must be equal to or
	 *                     greater than zero
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.6
	 */
	public void setBottomMargin(int bottomMargin) {
		checkWidget();
		if (this.bottomMargin == bottomMargin || bottomMargin < 0) {
			return;
		}
		this.bottomMargin = bottomMargin;
		redraw();
	}

	@Override
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

	/**
	 * Set the label's horizontal left margin, in pixels.
	 *
	 * @param leftMargin the left margin of the label, which must be equal to or
	 *                   greater than zero
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.6
	 */
	public void setLeftMargin(int leftMargin) {
		checkWidget();
		if (this.leftMargin == leftMargin || leftMargin < 0) {
			return;
		}
		this.leftMargin = leftMargin;
		redraw();
	}

	/**
	 * Set the label's margins, in pixels.
	 *
	 * @param leftMargin   the left margin.
	 * @param topMargin    the top margin.
	 * @param rightMargin  the right margin.
	 * @param bottomMargin the bottom margin.
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.6
	 */
	public void setMargins(int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
		checkWidget();
		this.leftMargin = Math.max(0, leftMargin);
		this.topMargin = Math.max(0, topMargin);
		this.rightMargin = Math.max(0, rightMargin);
		this.bottomMargin = Math.max(0, bottomMargin);
		redraw();
	}

	/**
	 * Set the label's right margin, in pixels.
	 *
	 * @param rightMargin the right margin of the label, which must be equal to or
	 *                    greater than zero
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.6
	 */
	public void setRightMargin(int rightMargin) {
		checkWidget();
		if (this.rightMargin == rightMargin || rightMargin < 0) {
			return;
		}
		this.rightMargin = rightMargin;
		redraw();
	}

	/**
	 * Set the label's text. The value <code>null</code> clears it.
	 * <p>
	 * Mnemonics are indicated by an '&amp;' that causes the next character to be
	 * the mnemonic. When the user presses a key sequence that matches the mnemonic,
	 * focus is assigned to the control that follows the label. On most platforms,
	 * the mnemonic appears underlined but may be emphasised in a platform specific
	 * manner. The mnemonic indicator character '&amp;' can be escaped by doubling
	 * it in the string, causing a single '&amp;' to be displayed.
	 * </p>
	 *
	 * @param text the text to be displayed in the label or null
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setText(String text) {
		checkWidget();
		if (text == null) {
			text = ""; //$NON-NLS-1$
		}
		if (!text.equals(this.text)) {
			this.text = text;
			redraw();
		}
	}

	@Override
	public void setToolTipText(String string) {
		super.setToolTipText(string);
		appToolTipText = super.getToolTipText();
	}

	/**
	 * Set the label's top margin, in pixels.
	 *
	 * @param topMargin the top margin of the label, which must be equal to or
	 *                  greater than zero
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.6
	 */
	public void setTopMargin(int topMargin) {
		checkWidget();
		if (this.topMargin == topMargin || topMargin < 0) {
			return;
		}
		this.topMargin = topMargin;
		redraw();
	}

	/**
	 * Shorten the given text <code>t</code> so that its length doesn't exceed the
	 * given width. The default implementation replaces characters in the center of
	 * the original string with an ellipsis ("..."). Override if you need a
	 * different strategy.
	 *
	 * @param gc    the gc to use for text measurement
	 * @param t     the text to shorten
	 * @param width the width to shorten the text to, in pixels
	 * @return the shortened text
	 */
	protected String shortenText(GC gc, String t, int width) {
		if (t == null) {
			return null;
		}
		int w = gc.textExtent(ELLIPSIS, DRAW_FLAGS).x;
		if (width <= w) {
			return t;
		}
		int l = t.length();
		int max = l / 2;
		int min = 0;
		int mid = (max + min) / 2 - 1;
		if (mid <= 0) {
			return t;
		}
		TextLayout layout = new TextLayout(getDisplay());
		layout.setText(t);
		mid = validateOffset(layout, mid);
		while (min < mid && mid < max) {
			String s1 = t.substring(0, mid);
			String s2 = t.substring(validateOffset(layout, l - mid), l);
			int l1 = gc.textExtent(s1, DRAW_FLAGS).x;
			int l2 = gc.textExtent(s2, DRAW_FLAGS).x;
			if (l1 + w + l2 > width) {
				max = mid;
				mid = validateOffset(layout, (max + min) / 2);
			} else if (l1 + w + l2 < width) {
				min = mid;
				mid = validateOffset(layout, (max + min) / 2);
			} else {
				min = max;
			}
		}
		String result = mid == 0 ? t : t.substring(0, mid) + ELLIPSIS + t.substring(validateOffset(layout, l - mid), l);
		layout.dispose();
		return result;
	}

	int validateOffset(TextLayout layout, int offset) {
		int nextOffset = layout.getNextOffset(offset, SWT.MOVEMENT_CLUSTER);
		if (nextOffset != offset) {
			return layout.getPreviousOffset(nextOffset, SWT.MOVEMENT_CLUSTER);
		}
		return offset;
	}

	private String[] splitString(String text) {
		String[] lines = new String[1];
		int start = 0, pos;
		do {
			pos = text.indexOf('\n', start);
			if (pos == -1) {
				lines[lines.length - 1] = text.substring(start);
			} else {
				boolean crlf = (pos > 0) && (text.charAt(pos - 1) == '\r');
				lines[lines.length - 1] = text.substring(start, pos - (crlf ? 1 : 0));
				start = pos + 1;
				String[] newLines = new String[lines.length + 1];
				System.arraycopy(lines, 0, newLines, 0, lines.length);
				lines = newLines;
			}
		} while (pos != -1);
		return lines;
	}

	private IAction action;
	private Image actionImage;
	ResourceManager parentResourceManager = JFaceResources.getResources();
	LocalResourceManager localManager = new LocalResourceManager(parentResourceManager);

	public void setAction(IAction action) {
		this.action = action;
		if (action != null && action.getImageDescriptor() != null) {
			actionImage = localManager.createImage(action.getImageDescriptor());
		}
	}

	public IAction getAction() {
		return action;
	}

	public boolean isActionArea(int x, int y) {
		Rectangle rect = getClientArea();
		Rectangle actionArea = new Rectangle(rect.x + rect.width - rightMargin - actionImageWidth - rightMargin, rect.y,
				rect.x + rect.width - rightMargin - actionImageWidth - rightMargin, rect.y + rect.height);
		return actionArea.contains(x, y);
	}
}
