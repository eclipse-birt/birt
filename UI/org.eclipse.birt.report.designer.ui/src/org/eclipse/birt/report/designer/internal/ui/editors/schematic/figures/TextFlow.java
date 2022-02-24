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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures;

import java.lang.reflect.Field;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.bidi.BidiUIUtils;
import org.eclipse.birt.report.engine.util.BidiAlignmentResolver;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.text.BidiProcessor;
import org.eclipse.draw2d.text.ContentBox;
import org.eclipse.draw2d.text.LineRoot;
import org.eclipse.draw2d.text.TextFragmentBox;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;

/**
 * An enhanced TextFlow inherited from org.eclipse.draw2d.text.TextFlow. Adds
 * supports for horizontal-alignment, line-through, underline styles.
 * 
 */
public class TextFlow extends org.eclipse.draw2d.text.TextFlow {

	private String specialPREFIX = "";//$NON-NLS-1$
	/**
	 * The multiple of this is the actual drawing line width.
	 */
	static int LINE_FACTOR = 24;

	/**
	 * Since the <b>ELLIPSIS </b> field in org.eclipse.draw2d.text.TextFlow is not
	 * accessible outside its package, we use this to substitute it.
	 */
	static String ELLIPSIS = "..."; //$NON-NLS-1$

	/**
	 * Since the <b>truncated </b> field in org.eclipse.draw2d.text.TextFragmentBox
	 * is not accessible outside its package, we use this Field to reflect the value
	 * of relevant object.
	 */
	static Field TRUNCATED;

	/**
	 * Text underline style.
	 */
	private String textUnderline = DesignChoiceConstants.TEXT_UNDERLINE_NONE;

	/**
	 * Text line-through style.
	 */
	private String textLineThrough = DesignChoiceConstants.TEXT_LINE_THROUGH_NONE;

	/**
	 * Text over-line style.
	 */
	private String textOverline = DesignChoiceConstants.TEXT_OVERLINE_NONE;

	/**
	 * Text horizontal alignment style.
	 */
	private String textAlign = DesignChoiceConstants.TEXT_ALIGN_LEFT;

	/**
	 * Text vertical alignment style.
	 */
	private String verticalAlign = DesignChoiceConstants.VERTICAL_ALIGN_TOP;

	/**
	 * bidi_hcg: Text direction style.
	 */
	private String direction = null;

	/**
	 * bidi_hcg: Color we use as a transparent color.
	 */
	private static final Color TRANSPARENT_COLOR = ColorConstants.lightGray;

	/**
	 * bidi_hcg: <b>lineRoot</b> field in org.eclipse.draw2d.text.TextFragmentBox.
	 */
	static Field LINE_ROOT;

	static {
		try {
			/**
			 * Here we try to retrieve the original value of <b>ELLIPSIS </b> in
			 * org.eclipse.draw2d.text.TextFlow, so we can adapt the future change of that
			 * class.
			 */

			Field ellipsis = org.eclipse.draw2d.text.TextFlow.class.getDeclaredField("ELLIPSIS"); //$NON-NLS-1$
			ellipsis.setAccessible(true);

			ELLIPSIS = (String) ellipsis.get(new org.eclipse.draw2d.text.TextFlow());
		} catch (SecurityException e) {
			ExceptionHandler.handle(e);
		} catch (NoSuchFieldException e) {
			ExceptionHandler.handle(e);
		} catch (IllegalArgumentException e) {
			ExceptionHandler.handle(e);
		} catch (IllegalAccessException e) {
			ExceptionHandler.handle(e);
		}

		try {
			/**
			 * Creates the <b>TRUNCATED </b> field object in advance for efficiency
			 * consideration.
			 */
			TRUNCATED = TextFragmentBox.class.getDeclaredField("truncated"); //$NON-NLS-1$
			TRUNCATED.setAccessible(true);
		} catch (SecurityException e) {
			ExceptionHandler.handle(e);
		} catch (NoSuchFieldException e) {
			ExceptionHandler.handle(e);
		}

		// bidi_hcg start
		try {
			/**
			 * Creates the <b>LINE_ROOT</b> field object in advance for efficiency
			 * consideration.
			 */
			LINE_ROOT = ContentBox.class.getDeclaredField("lineRoot"); //$NON-NLS-1$
			LINE_ROOT.setAccessible(true);
		} catch (SecurityException e) {
			ExceptionHandler.handle(e);
		} catch (NoSuchFieldException e) {
			ExceptionHandler.handle(e);
		}
		// bidi_hcg end
	}

	/**
	 * Paints self to specified graphics with given translating point.
	 * 
	 * @param g
	 * @param translationPoint
	 */
	public void paintTo(Graphics g, Point translationPoint) {
		paintTo(g, translationPoint.x, translationPoint.y);
	}

	/**
	 * Paints self to specified graphics with given X,Y offsets.
	 * 
	 * @param g
	 * @param xoff
	 * @param yoff
	 */
	public void paintTo(Graphics g, int xoff, int yoff) {
		List fragments = this.getFragments();
		assert this.getFont().getFontData().length > 0;

		/**
		 * Calculates the actual drawing line width according to the Font size.
		 */
		int lineWidth = this.getFont().getFontData()[0].getHeight() / LINE_FACTOR + 1;

		if (fragments.size() > 0) {
			int index = 0;

			boolean isMirrored = isMirrored();

			do {
				index = paintLineTo(g, xoff, yoff, lineWidth, index, isMirrored); // bidi_hcg
			} while (index != -1);
		}
		g.restoreState();
	}

	// bidi_hcg: rename and leave an old method for now.
	public void paintTo_old(Graphics g, int xoff, int yoff) {
		TextFragmentBox frag;
		List fragments = this.getFragments();
		assert this.getFont().getFontData().length > 0;

		/**
		 * Calculates the actual drawing line width according to the Font size.
		 */
		int lineWidth = this.getFont().getFontData()[0].getHeight() / LINE_FACTOR + 1;
		/**
		 * Get the total fragments height first
		 */
		int totalHeight = 0;
		for (int i = 0; i < fragments.size(); i++) {
			// FlowBoxWrapper wrapper = new FlowBoxWrapper( (FlowBox) fragments
			// .get( i ) );
			totalHeight += ((TextFragmentBox) fragments.get(i)).getAscent()
					+ ((TextFragmentBox) fragments.get(i)).getDescent();
		}

		for (int i = 0; i < fragments.size(); i++) {
			frag = (TextFragmentBox) fragments.get(i);

			// FlowBoxWrapper wrapper = new FlowBoxWrapper( frag );
			String draw = null;

			try {
				/**
				 * Uses stored <b>TRUNCATED </b> Field object to reflect the relevant value.
				 */
				if (TRUNCATED != null && TRUNCATED.getBoolean(frag)) {
					draw = getText().substring(frag.offset, frag.offset + frag.length) + ELLIPSIS;
				} else {
					draw = getText().substring(frag.offset, frag.offset + frag.length);
				}
			} catch (IllegalArgumentException e) {
				ExceptionHandler.handle(e);
			} catch (IllegalAccessException e) {
				ExceptionHandler.handle(e);
			}

			/**
			 * Calculates the adjusted left coordinate according to the horizontal alignment
			 * style.
			 */

			// Here we need re-calculate the line width of fragments,
			// since maybe the font style is changed
			// See bugzilla item
			int linew = FigureUtilities.getTextWidth(draw, g.getFont());
			frag.setWidth(linew);

			int left = calculateLeft(this.getSize().width, frag.getWidth());

			int top = calculateTop(this.getSize().height, totalHeight);

			int realX = frag.getX() + left + xoff;
			int realY = frag.getBaseline() - frag.getAscent() + top + yoff;

			if (!isEnabled()) {
				// g.setForegroundColor( ColorConstants.buttonLightest );
				// g.drawString( draw, realX + 1, realY + 1 );
				g.setForegroundColor(ColorConstants.buttonDarker);
				// g.drawString( draw, realX, realY );
				paintSpecial_old(g, draw, realX, realY, i == 0);
			} else {
				// g.drawString( draw, realX, realY );
				paintSpecial_old(g, draw, realX, realY, i == 0);
			}

			/**
			 * Only draws the line when width is greater than 1 to avoid unnecessary lines
			 * under/in blank text.
			 */
			if (frag.getWidth() > 1) {
				g.setLineWidth(lineWidth);

				/**
				 * Processes the underline style.
				 */
				if (DesignChoiceConstants.TEXT_UNDERLINE_UNDERLINE.equals(textUnderline)) {
					g.drawLine(realX, frag.getBaseline() + top + frag.getDescent() - lineWidth, realX + frag.getWidth(),
							frag.getBaseline() + top + frag.getDescent() - lineWidth);
				}

				/**
				 * Processes the line-through style.
				 */
				if (DesignChoiceConstants.TEXT_LINE_THROUGH_LINE_THROUGH.equals(textLineThrough)) {
					g.drawLine(realX, frag.getBaseline() + top - frag.getAscent() / 2 + lineWidth,
							realX + frag.getWidth(), frag.getBaseline() + top - frag.getAscent() / 2 + lineWidth);
				}
				/**
				 * Processes the over-line style.
				 */
				if (DesignChoiceConstants.TEXT_OVERLINE_OVERLINE.equals(textOverline)) {
					g.drawLine(realX, realY + 1, realX + frag.getWidth(), realY + 1);
				}

			}

			g.restoreState();
		}
	}

	// bidi_hcg: rename and leave an old method for now.
	private void paintSpecial_old(Graphics g, String text, int x, int y, boolean firstBox) {
		if (firstBox && specialPREFIX.length() != 0 && text.indexOf(specialPREFIX) == 0) {
			int with = FigureUtilities.getTextWidth(specialPREFIX, g.getFont());
			Color c = g.getForegroundColor();

			g.setForegroundColor(ReportColorConstants.textFillColor);

			g.drawString(specialPREFIX, x, y);

			g.setForegroundColor(c);
			g.drawString(text.substring(specialPREFIX.length()), x + with, y);
		} else {
			g.drawString(text, x, y);
		}
	}

	/**
	 * Paints self to specified graphics with given X,Y offsets.
	 * 
	 * @param g
	 * @param xoff
	 * @param yoff
	 * 
	 * @return Next fragment index or -1 if there is no more fragments to process
	 */
	private int paintLineTo(Graphics g, int xoff, int yoff, int lineWidth, int fragIndex, boolean isMirrored) {
		List fragments = this.getFragments();
		if (fragments == null)
			return -1;

		int nFragments = fragments.size();
		if (nFragments < 1 || fragIndex >= nFragments)
			return -1;

		/**
		 * Get the total fragments height first
		 */
		// bidi_hcg start
		TextFragmentBox frag = (TextFragmentBox) fragments.get(fragIndex);
		int totalHeight = frag.getAscent() + frag.getDescent();
		int totalWidth = 0;
		String[] draws = new String[nFragments - fragIndex];
		LineRoot lineRoot = null, prevLineRoot;

		try {
			lineRoot = prevLineRoot = (LineRoot) LINE_ROOT.get(frag);
		} catch (IllegalArgumentException e) {
			ExceptionHandler.handle(e);
		} catch (IllegalAccessException e) {
			ExceptionHandler.handle(e);
		}

		int i = fragIndex;
		// bidi_hcg end

		do {
			String draw = null;

			try {
				/**
				 * Uses stored <b>TRUNCATED </b> Field object to reflect the relevant value.
				 */
				if (TRUNCATED != null && TRUNCATED.getBoolean(frag)) {
					draw = getText().substring(frag.offset, frag.offset + frag.length) + ELLIPSIS;
				} else {
					draw = getText().substring(frag.offset, frag.offset + frag.length);
				}
			} catch (IllegalArgumentException e) {
				ExceptionHandler.handle(e);
			} catch (IllegalAccessException e) {
				ExceptionHandler.handle(e);
			}

			/**
			 * Calculates the adjusted left coordinate according to the horizontal alignment
			 * style.
			 */

			// Here we need re-calculate the line width of fragments,
			// since maybe the font style is changed
			// See bugzilla item
			int linew = FigureUtilities.getTextWidth(draw, g.getFont());
			frag.setWidth(linew);

			// bidi_hcg start
			totalWidth += linew;
			draws[i - fragIndex] = draw;

			if (++i >= nFragments)
				break;

			frag = (TextFragmentBox) fragments.get(i);

			prevLineRoot = lineRoot;

			try {
				lineRoot = (LineRoot) LINE_ROOT.get(frag);
			} catch (IllegalArgumentException iare) {
				ExceptionHandler.handle(iare);
			} catch (IllegalAccessException iace) {
				ExceptionHandler.handle(iace);
			}
			// bidi_hcg end
		} while (fragIndex < nFragments && prevLineRoot == lineRoot);

		// bidi_hcg start
		int retIndex = i;
		int spacing = calculateSpacing(this.getSize().width, totalWidth, isMirrored);
		// bidi_hcg end

		for (i = fragIndex; i < retIndex; i++) {
			frag = (TextFragmentBox) fragments.get(i);

			int fragAscent = frag.getAscent();

			/**
			 * Calculates the adjusted left coordinate according to the horizontal alignment
			 * style.
			 */

			int top = calculateTop(this.getSize().height, totalHeight);

			int realX = frag.getX() + spacing // bidi_hcg
					+ xoff;
			int realY = frag.getBaseline() - fragAscent + top + yoff;

			if (!isEnabled()) {
				g.setForegroundColor(ColorConstants.buttonDarker);
				paintSpecial(g, draws[i - fragIndex], realX, realY, i == 0, frag.isRightToLeft(), isMirrored); // bidi_hcg
			} else {
				paintSpecial(g, draws[i - fragIndex], realX, realY, i == 0, frag.isRightToLeft(), isMirrored); // bidi_hcg
			}

			/**
			 * Only draws the line when width is greater than 1 to avoid unnecessary lines
			 * under/in blank text.
			 */
			if (frag.getWidth() > 1) {
				g.setLineWidth(lineWidth);

				/**
				 * Processes the underline style.
				 */
				if (DesignChoiceConstants.TEXT_UNDERLINE_UNDERLINE.equals(textUnderline)) {
					int fragDescent = frag.getDescent();

					g.drawLine(realX, frag.getBaseline() + top + fragDescent - lineWidth, realX + frag.getWidth(),
							frag.getBaseline() + top + fragDescent - lineWidth);
				}

				/**
				 * Processes the line-through style.
				 */
				if (DesignChoiceConstants.TEXT_LINE_THROUGH_LINE_THROUGH.equals(textLineThrough)) {
					g.drawLine(realX, frag.getBaseline() + top - fragAscent / 2 + lineWidth, realX + frag.getWidth(),
							frag.getBaseline() + top - fragAscent / 2 + lineWidth);
				}
				/**
				 * Processes the over-line style.
				 */
				if (DesignChoiceConstants.TEXT_OVERLINE_OVERLINE.equals(textOverline)) {
					g.drawLine(realX, realY + 1, realX + frag.getWidth(), realY + 1);
				}

			}
		}
		return retIndex;
	}

	private void paintSpecial(Graphics g, String text, int x, int y, boolean firstBox, boolean rtl,
			boolean isMirrored) {
		// ReportColorConstants.textFillColor
		// bidi_hcg start
		Image image = null;
		GC gc = null;

		// To maintain the proper text order, draw the text run into graphics
		// with orientation consistent with the run direction.
		// When run is RTL or this figure is mirrored, paint to an offscreen
		// image with the appropriate orientation, and then copy the image to
		// the main painting graphics.
		if (rtl || isMirrored) {
			TextLayout textLayout = BidiUIUtils.INSTANCE.getTextLayout(SWT.LEFT_TO_RIGHT);
			textLayout.setFont(g.getFont());

			if (firstBox && specialPREFIX.length() != 0 && text.indexOf(specialPREFIX) == 0)
				textLayout.setText(text.substring(specialPREFIX.length()));
			else
				textLayout.setText(text);

			textLayout.setStyle(new TextStyle(g.getFont(), g.getForegroundColor(), TRANSPARENT_COLOR), 0,
					text.length());
			RGB rgbData = g.getForegroundColor().getRGB();
			if (ColorConstants.black.getRGB().equals(rgbData)) {
				rgbData = ColorConstants.buttonDarker.getRGB();
			}
			PaletteData paletteData = new PaletteData(new RGB[] { TRANSPARENT_COLOR.getRGB(), rgbData });
			ImageData imageData = new ImageData(
					// RTL graphics has a 1-pixel-origin-shift problem, so add
					// 1 extra pixel to the image width.
					textLayout.getBounds().width + 1, textLayout.getBounds().height, 4, paletteData);
			imageData.transparentPixel = paletteData.getPixel(TRANSPARENT_COLOR.getRGB());
			image = new Image(Display.getCurrent(), imageData);

			gc = new GC(image, rtl ? SWT.RIGHT_TO_LEFT : SWT.LEFT_TO_RIGHT);
			textLayout.draw(gc, 0, 0);
		}
		// bidi_hcg end

		if (firstBox && specialPREFIX.length() != 0 && text.indexOf(specialPREFIX) == 0) {
			int with = FigureUtilities.getTextWidth(specialPREFIX, g.getFont());
			Color c = g.getForegroundColor();

			g.setForegroundColor(ReportColorConstants.greyFillColor);
			g.drawString(specialPREFIX, x, y);

			g.setForegroundColor(c);

			// bidi_hcg start
			if (image != null)
				g.drawImage(image, x + with, y);
			else
				// bidi_hcg end

				g.drawString(text.substring(specialPREFIX.length()), x + with, y);
		} else {
			// bidi_hcg start
			if (image != null)
				g.drawImage(image, x, y);
			else
				// bidi_hcg end

				g.drawString(text, x, y);
		}
		// bidi_hcg start
		if (gc != null)
			gc.dispose();
		if (image != null)
			image.dispose();
		// bidi_hcg end
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.draw2d.text.TextFlow#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics g) {
		paintTo(g, 0, 0);
	}

	/**
	 * Calculates the left coordinate by given container width, text width and
	 * horizontal alignment style.
	 * 
	 * @param compWidth Container width.
	 * @param textWidth Text width.
	 * @return
	 */
	protected int calculateLeft(int compWidth, int textWidth) {
		int rlt = 0;

		if (DesignChoiceConstants.TEXT_ALIGN_LEFT.equals(textAlign)
				|| DesignChoiceConstants.TEXT_ALIGN_JUSTIFY.equals(textAlign)) {
			rlt = 0;
		} else if (DesignChoiceConstants.TEXT_ALIGN_CENTER.equals(textAlign)) {
			rlt = (compWidth - textWidth) / 2;
		} else if (DesignChoiceConstants.TEXT_ALIGN_RIGHT.equals(textAlign)) {
			rlt = (compWidth - textWidth);
		}

		return rlt;
	}

	/**
	 * Calculates the spacing by given container width, text width and horizontal
	 * alignment style.
	 * 
	 * @param compWidth Container width.
	 * @param textWidth Text width.
	 * @return
	 * 
	 * @author bidi_hcg
	 */
	private int calculateSpacing(int compWidth, int textWidth, boolean isMirrored) {
		int rlt = 0;

		String align = BidiAlignmentResolver.resolveAlignmentForDesigner(textAlign,
				resolveDirection(direction, isMirrored), isMirrored);

		if (DesignChoiceConstants.TEXT_ALIGN_LEFT.equals(align)) {
			rlt = 0;
		} else if (DesignChoiceConstants.TEXT_ALIGN_CENTER.equals(align)) {
			rlt = (compWidth - textWidth) / 2;
		} else if (DesignChoiceConstants.TEXT_ALIGN_RIGHT.equals(align)) {
			rlt = (compWidth - textWidth);
		}

		return rlt;
	}

	/**
	 * Calculates the top coordinate by given container height, text height and
	 * vertical alignment style.
	 * 
	 * @param compHeight Container height.
	 * @param textHeight text height.
	 * @return
	 */
	protected int calculateTop(int compHeight, int textHeight) {
		int rlt = 0;

		if (DesignChoiceConstants.VERTICAL_ALIGN_TOP.equals(verticalAlign)
				|| DesignChoiceConstants.VERTICAL_ALIGN_BASELINE.equals(verticalAlign)) {
			rlt = 0;
		} else if (DesignChoiceConstants.VERTICAL_ALIGN_MIDDLE.equals(verticalAlign)) {
			rlt = (compHeight - textHeight) / 2;
		} else if (DesignChoiceConstants.VERTICAL_ALIGN_BOTTOM.equals(verticalAlign)) {
			rlt = (compHeight - textHeight);
		}

		return rlt;
	}

	/**
	 * Sets the horizontal text alignment style.
	 * 
	 * @param textAlign The textAlign to set.
	 */
	public void setTextAlign(String textAlign) {
		this.textAlign = textAlign;
	}

	/**
	 * Gets the horizontal text alignment style.
	 * 
	 * @return
	 */
	public String getTextAlign() {
		return textAlign;
	}

	/**
	 * Sets the over-line style of the text.
	 * 
	 * @param textOverline The textOverLine to set.
	 */
	public void setTextOverline(String textOverline) {
		this.textOverline = textOverline;
	}

	/**
	 * Sets the line-through style of the text.
	 * 
	 * @param textLineThrough The textLineThrough to set.
	 */
	public void setTextLineThrough(String textLineThrough) {
		this.textLineThrough = textLineThrough;
	}

	/**
	 * Sets the underline style of the text.
	 * 
	 * @param textUnderline The textUnderline to set.
	 */
	public void setTextUnderline(String textUnderline) {
		this.textUnderline = textUnderline;
	}

	/**
	 * Sets the vertical text alignment style.
	 * 
	 * @param verticalAlign The verticalAlign to set.
	 */
	public void setVerticalAlign(String verticalAlign) {
		this.verticalAlign = verticalAlign;
	}

	/**
	 * @param specialPREFIX
	 */
	public void setSpecialPREFIX(String specialPREFIX) {
		if (specialPREFIX == null) {
			return;
		}
		this.specialPREFIX = specialPREFIX;
		repaint();
	}

	/**
	 * Sets the text direction style.
	 * 
	 * @param direction The direction to set.
	 * 
	 * @author bidi_hcg
	 */
	public void setDirection(String direction) {
		direction = resolveDirection(direction);

		if ((direction == null && this.direction != null) || (direction != null && !direction.equals(this.direction))) {
			this.direction = direction;
			revalidateBidi(this);
		}
	}

	/**
	 * Gets the text direction style.
	 * 
	 * @return The direction style.
	 * 
	 * @author bidi_hcg
	 */
	public String getDirection() {
		return resolveDirection(direction);
	}

	/**
	 * Determines whether the text is Right-To-Left.
	 * 
	 * @return Boolean indicating whether the text is Right-To-Left or not.
	 * 
	 * @author bidi_hcg
	 */
	private boolean isRtl() {
		return DesignChoiceConstants.BIDI_DIRECTION_RTL.equals(getDirection());
	}

	/**
	 * Resolves the text direction style.
	 * 
	 * @return The direction style.
	 * 
	 * @author bidi_hcg
	 */
	private String resolveDirection(String direction) {
		if (direction == null) {
			return isMirrored() ? DesignChoiceConstants.BIDI_DIRECTION_RTL : DesignChoiceConstants.BIDI_DIRECTION_LTR;
		}
		return direction;
	}

	private String resolveDirection(String direction, boolean isMirrored) {
		if (direction == null) {
			return isMirrored ? DesignChoiceConstants.BIDI_DIRECTION_RTL : DesignChoiceConstants.BIDI_DIRECTION_LTR;
		}
		return direction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.text.TextFlow#contributeBidi(org.eclipse.draw2d.text
	 * .BidiProcessor)
	 * 
	 * @bidi_hcg
	 */
	protected void contributeBidi(BidiProcessor proc) {
		proc.setOrientation(isRtl() ? SWT.RIGHT_TO_LEFT : SWT.LEFT_TO_RIGHT);
		super.contributeBidi(proc);
	}

}
