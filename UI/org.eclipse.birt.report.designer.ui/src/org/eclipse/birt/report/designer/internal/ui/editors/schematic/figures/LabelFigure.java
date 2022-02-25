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

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.ReportFigureUtilities;
import org.eclipse.birt.report.designer.internal.ui.layout.ReportItemConstraint;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.FlowBox;
import org.eclipse.draw2d.text.FlowFigure;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.swt.graphics.Font;

import com.ibm.icu.text.BreakIterator;

/**
 * A Figure with an embedded TextFlow within a FlowPage that contains text.
 *
 *
 */
public class LabelFigure extends ReportElementFigure {

	private static final Dimension ZERO_DIMENSION = new Dimension();

	private TextFlow label;

	private FlowPage flowPage;

	private String display;

	private Dimension recommendSize = new Dimension();

	private boolean isFixLayout;

	/**
	 * Creates a new LabelFigure with a default MarginBorder size 1 and a FlowPage
	 * containing a TextFlow with the style WORD_WRAP_SOFT.
	 */
	public LabelFigure() {
		this(1);
	}

	/**
	 * @return
	 */
	public String getDisplay() {
		return display;
	}

	/**
	 * Creates a new LabelFigure with a MarginBorder that is the given size and a
	 * FlowPage containing a TextFlow with the style WORD_WRAP_HARD.
	 *
	 * @param borderSize the size of the MarginBorder
	 */
	public LabelFigure(int borderSize) {
		setBorder(new MarginBorder(borderSize));

		label = new TextFlow() {

			@Override
			public void postValidate() {
				if (DesignChoiceConstants.DISPLAY_BLOCK.equals(display)
						|| DesignChoiceConstants.DISPLAY_INLINE.equals(display)) {
					List list = getFragments();
					FlowBox box;

					int left = Integer.MAX_VALUE, top = left;
					int bottom = Integer.MIN_VALUE;

					for (int i = 0; i < list.size(); i++) {
						box = (FlowBox) list.get(i);

						left = Math.min(left, box.getX());
						top = Math.min(top, box.getBaseline() - box.getAscent());
						bottom = Math.max(bottom, box.getBaseline() + box.getDescent());
					}
					int width = LabelFigure.this.getClientArea().width;
					if (isFixLayout) {
						int maxWidth = calcMaxSegment() - getInsets().getWidth();
						width = Math.max(width, maxWidth);
					}

					setBounds(new Rectangle(left, top, width,
							Math.max(LabelFigure.this.getClientArea().height, bottom - top)));

					if (isFixLayout()) {
						Figure child = (Figure) getParent();
						Rectangle rect = child.getBounds();
						child.setBounds(new Rectangle(rect.x, rect.y, width, rect.height));
					}

					list = getChildren();
					for (int i = 0; i < list.size(); i++) {
						((FlowFigure) list.get(i)).postValidate();
					}
				} else {
					super.postValidate();
				}
			}
		};

		label.setLayoutManager(new ParagraphTextLayout(label, ParagraphTextLayout.WORD_WRAP_SOFT));

		flowPage = new FlowPage();

		flowPage.add(label);

		setLayoutManager(new StackLayout());

		add(flowPage);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.draw2d.IFigure#getPreferredSize(int, int)
	 */
	private Dimension getPreferredSize(int wHint, int hHint, boolean isFix, boolean forceWidth, boolean forceHeight) {
		int rx = recommendSize != null ? recommendSize.width : 0;
		int ry = recommendSize != null ? recommendSize.height : 0;

		rx = getRealRecommendSizeX(rx, wHint);

		Dimension dim = null;

		if (isFix) {
			int tempHint = wHint;
			int maxWidth = calcMaxSegment();
			if (wHint < maxWidth && !forceWidth) {
				tempHint = maxWidth;
			}
			dim = super.getPreferredSize(tempHint <= 0 ? -1 : tempHint, hHint);
		}
		// only when display is block, use passed in wHint
		else if (DesignChoiceConstants.DISPLAY_BLOCK.equals(display)) {
			dim = super.getPreferredSize(rx == 0 ? wHint : rx, hHint);
		} else {
			dim = super.getPreferredSize(rx == 0 ? -1 : rx, hHint);
			// fix bug 271116.
			if (rx == 0 && wHint > 0 && dim.width > wHint) {
				dim = super.getPreferredSize(wHint, hHint);
			}
		}

		return new Dimension(Math.max(dim.width, rx), Math.max(dim.height, ry));
	}

	@Override
	public Dimension getPreferredSize(int wHint, int hHint) {
		return getPreferredSize(wHint, hHint, false, false, false);
	}

	@Override
	public Dimension getMinimumSize(int wHint, int hHint) {
		return getMinimumSize(wHint, hHint, false, false, false);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.draw2d.Figure#getMinimumSize(int, int)
	 */
	private Dimension getMinimumSize(int wHint, int hHint, boolean isFix, boolean forceWidth, boolean forceHeight) {
		if (DesignChoiceConstants.DISPLAY_NONE.equals(display)) {
			return ZERO_DIMENSION;
		}

		int rx = recommendSize != null ? recommendSize.width : 0;
		int ry = recommendSize != null ? recommendSize.height : 0;

		rx = getRealRecommendSizeX(rx, wHint);

		if (wHint == -1 && hHint == -1) {
			int maxWidth = calcMaxSegment();

			// use recommend size if specified, otherwise use max segment size
			Dimension dim = super.getMinimumSize(rx == 0 ? maxWidth : rx, -1);

			dim.height = Math.max(dim.height, Math.max(getInsets().getHeight(), ry));

			return dim;
		}
		Dimension dim;
		// return the true minimum size with minimum width;
		if (isFix) {
			int tempHint = wHint;
			int maxWidth = calcMaxSegment();
			if (wHint < maxWidth && !forceWidth) {
				tempHint = maxWidth;
			}
			dim = super.getMinimumSize(tempHint <= 0 ? -1 : tempHint, hHint);

			return new Dimension(Math.max(dim.width, rx), Math.max(dim.height, ry));
		} else {
			dim = super.getMinimumSize(rx == 0 ? -1 : rx, hHint);
		}

		if (dim.width < wHint) {
			return new Dimension(Math.max(dim.width, rx), Math.max(dim.height, ry));
		}

		dim = super.getMinimumSize(wHint, hHint);

		return new Dimension(Math.max(dim.width, rx), Math.max(dim.height, ry));

	}

	private int getRealRecommendSizeX(int rx, int wHint) {
		if (rx > 0 || wHint == -1) {
			return rx;
		}

		if (getParent() != null && getParent().getLayoutManager() != null) {
			ReportItemConstraint constraint = (ReportItemConstraint) getParent().getLayoutManager().getConstraint(this);

			if (constraint != null && constraint.getMeasure() != 0
					&& DesignChoiceConstants.UNITS_PERCENTAGE.equals(constraint.getUnits())) {
				// compute real percentag recommend size
				rx = (int) constraint.getMeasure() * wHint / 100;

			}
		}

		return rx;
	}

	private int calcMaxSegment() {
		String text = label.getText();
		char[] chars = text.toCharArray();
		int position = 0;
		int maxWidth = 0;

		for (int i = 0; i < chars.length; i++) {
			if (canBreakAfter(chars[i])) {
				int tempMaxWidth;
				String st = text.substring(position, i + 1);
				tempMaxWidth = FigureUtilities.getStringExtents(st, getFont()).width;

				if (tempMaxWidth > maxWidth) {
					maxWidth = tempMaxWidth;
				}
				position = i;
			}
		}
		String st = text.substring(position, chars.length);
		int tempMaxWidth = FigureUtilities.getStringExtents(st, getFont()).width;

		if (tempMaxWidth > maxWidth) {
			maxWidth = tempMaxWidth;
		}
		return maxWidth + getInsets().getWidth();
	}

	static final BreakIterator LINE_BREAK = BreakIterator.getLineInstance();

	static boolean canBreakAfter(char c) {
		boolean result = Character.isWhitespace(c) || c == '-';
		if (!result && (c < 'a' || c > 'z')) {
			// chinese characters and such would be caught in here
			// LINE_BREAK is used here because INTERNAL_LINE_BREAK might be in
			// use
			LINE_BREAK.setText(c + "a"); //$NON-NLS-1$
			result = LINE_BREAK.isBoundary(1);
		}
		return result;
	}

	private static int getMinimumFontSize(Font ft) {
		if (ft != null && ft.getFontData().length > 0) {
			return ft.getFontData()[0].getHeight();
		}

		return 0;
	}

	/**
	 * Since Eclipse TextFlow figure ignore the trailing /r/n for calculating the
	 * client size, we must append the extra size ourselves.
	 *
	 * @return dimension for the client area used by the editor.
	 */
	public Rectangle getEditorArea() {
		Rectangle rect = getClientArea().getCopy();

		String s = getText();

		int count = 0;

		if (s != null && s.length() > 1) {
			for (int i = s.length() - 2; i >= 0; i -= 2) {
				if ("\r\n".equals(s.substring(i, i + 2))) //$NON-NLS-1$
				{
					// count++;
				} else {
					break;
				}
			}
		}

		int hh = getMinimumFontSize(getFont());
		rect.height += count * hh + ((count == 0) ? 0 : (hh / 2));

		return rect;
	}

	/**
	 * Sets the recommended size.
	 *
	 * @param recommendSize
	 */
	public void setRecommendSize(Dimension recommendSize) {
		this.recommendSize = recommendSize;
	}

	/**
	 * Gets the recommended size.
	 *
	 * @return
	 */
	public Dimension getRecommendSize() {
		return recommendSize;
	}

	/**
	 * Sets the display property of the Label.
	 *
	 * @param display the display property. this should be one of the following:
	 *                DesignChoiceConstants.DISPLAY_BLOCK |
	 *                DesignChoiceConstants.DISPLAY_INLINE |
	 *                DesignChoiceConstants.DISPLAY_NONE
	 */
	public void setDisplay(String display) {
		// if the display equals none, as the block
		if (DesignChoiceConstants.DISPLAY_NONE.equals(display)) {
			this.display = DesignChoiceConstants.DISPLAY_BLOCK;
		} else {
			this.display = display;
		}
	}

	/**
	 * Returns the text inside the TextFlow.
	 *
	 * @return the text flow inside the text.
	 */
	public String getText() {
		return label.getText();
	}

	/**
	 * Sets the text of the TextFlow to the given value.
	 *
	 * @param newText the new text value.
	 */
	public void setText(String newText) {
		if (newText == null) {
			newText = "";//$NON-NLS-1$
		}

		label.setText(newText);
	}

	/**
	 * Sets the over-line style of the text.
	 *
	 * @param textOverline The textOverline to set.
	 */
	public void setTextOverline(String textOverline) {
		label.setTextOverline(textOverline);
	}

	/**
	 * Sets the line-through style of the text.
	 *
	 * @param textLineThrough The textLineThrough to set.
	 */
	public void setTextLineThrough(String textLineThrough) {
		label.setTextLineThrough(textLineThrough);
	}

	/**
	 * Sets the underline style of the text.
	 *
	 * @param textUnderline The textUnderline to set.
	 */
	public void setTextUnderline(String textUnderline) {
		label.setTextUnderline(textUnderline);
	}

	/**
	 * Sets the horizontal text alignment style.
	 *
	 * @param textAlign The textAlign to set.
	 */
	public void setTextAlign(String textAlign) {
		label.setTextAlign(textAlign);
	}

	/**
	 * Gets the horizontal text alignment style.
	 *
	 * @return The textAlign.
	 */
	public String getTextAlign() {
		return label.getTextAlign();
	}

	/**
	 * Sets the vertical text alignment style.
	 *
	 * @param verticalAlign The verticalAlign to set.
	 */
	public void setVerticalAlign(String verticalAlign) {
		label.setVerticalAlign(verticalAlign);
	}

	/**
	 * Sets the toolTip text for this figure.
	 *
	 * @param toolTip
	 */
	public void setToolTipText(String toolTip) {
		if (toolTip != null) {
			setToolTip(ReportFigureUtilities.createToolTipFigure(toolTip, this.getDirection(), this.getTextAlign()));
		} else {
			setToolTip(null);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.draw2d.Figure#setFont(org.eclipse.swt.graphics.Font)
	 */
	@Override
	public void setFont(Font f) {
		super.setFont(f);
		label.setFont(f);
	}

	/**
	 * @param specialPREFIX
	 */
	public void setSpecialPREFIX(String specialPREFIX) {
		label.setSpecialPREFIX(specialPREFIX);
	}

	/**
	 * Gets the direction property of the Label.
	 *
	 * @return the Label direction.
	 *
	 * @author bidi_hcg
	 */
	public String getDirection() {
		return label.getDirection();
	}

	/**
	 * Sets the direction property of the Label.
	 *
	 * @param direction the direction property. this should be one of the following:
	 *                  DesignChoiceConstants.BIDI_DIRECTION_LTR |
	 *                  DesignChoiceConstants.BIDI_DIRECTION_RTL
	 *
	 * @author bidi_hcg
	 */
	public void setDirection(String direction) {
		label.setDirection(direction);
	}

	@Override
	public Dimension getFixPreferredSize(int w, int h) {
		int width = 0;
		int height = 0;
		if (recommendSize.width > 0) {
			width = recommendSize.width;
		} else if (recommendSize.height > 0) {
			width = getPreferredSize(w, recommendSize.height, true, false, true).width;
		} else {
			width = getPreferredSize(w, h, true, false, false).width;
		}

		if (recommendSize.height > 0) {
			height = recommendSize.height;
		} else if (recommendSize.width > 0) {
			int maxWidth = calcMaxSegment();
			height = getPreferredSize(Math.max(maxWidth, recommendSize.width), h, true, true, false).height;
		} else {
			height = getPreferredSize(w, h, true, false, false).height;
		}

		return new Dimension(width, height);
	}

	@Override
	public Dimension getFixMinimumSize(int w, int h) {
		int width = 0;
		int height = 0;
		if (recommendSize.width > 0) {
			width = recommendSize.width;
		} else if (recommendSize.height > 0) {
			width = getMinimumSize(w, recommendSize.height, true, false, true).width;
		} else {
			width = getMinimumSize(w, h, true, false, false).width;
		}

		if (recommendSize.height > 0) {
			height = recommendSize.height;
		} else if (recommendSize.width > 0) {
			int maxWidth = calcMaxSegment();
			height = getMinimumSize(Math.max(maxWidth, recommendSize.width), h, true, true, false).height;
		} else {
			height = getMinimumSize(w, h, true, false, false).height;
		}

		return new Dimension(width, height);
	}

	public boolean isFixLayout() {
		return isFixLayout;
	}

	public void setFixLayout(boolean isFixLayout) {
		this.isFixLayout = isFixLayout;
	}
}
