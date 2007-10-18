/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures;

import java.util.List;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
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

	private Dimension recommendSize;

	/**
	 * Creates a new LabelFigure with a default MarginBorder size 3 and a
	 * FlowPage containing a TextFlow with the style WORD_WRAP_SOFT.
	 */
	public LabelFigure() {
		this(1);
	}

	/**
	 * Creates a new LabelFigure with a MarginBorder that is the given size and
	 * a FlowPage containing a TextFlow with the style WORD_WRAP_HARD.
	 * 
	 * @param borderSize
	 *            the size of the MarginBorder
	 */
	public LabelFigure(int borderSize) {
		setBorder(new MarginBorder(borderSize));

		label = new TextFlow() {

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
						top = Math
								.min(top, box.getBaseline() - box.getAscent());
						bottom = Math.max(bottom, box.getBaseline()
								+ box.getDescent());
					}

					setBounds(new Rectangle(left, top, LabelFigure.this
							.getClientArea().width, Math.max(LabelFigure.this
							.getClientArea().height, bottom - top)));

					list = getChildren();
					for (int i = 0; i < list.size(); i++) {
						((FlowFigure) list.get(i)).postValidate();
					}
				} else {
					super.postValidate();
				}
			}
		};

		label.setLayoutManager(new ParagraphTextLayout(label,
				ParagraphTextLayout.WORD_WRAP_SOFT));

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
	public Dimension getPreferredSize(int wHint, int hHint) {
		//Dimension dim = super.getPreferredSize(wHint, hHint);
		
		int rx = recommendSize != null ? recommendSize.width : 0;
		int ry = recommendSize != null ? recommendSize.height : 0;
		Dimension dim = super.getPreferredSize(rx == 0? wHint:rx, hHint);
		
//		if (DesignChoiceConstants.DISPLAY_BLOCK.equals(display)) {
//			return new Dimension(dim.width, Math.max(dim.height, ry));
//		}

		//if (DesignChoiceConstants.DISPLAY_INLINE.equals(display)) 
		//{
		return new Dimension(Math.max(dim.width, rx), Math.max(dim.height,
					ry));
		//}

		//return dim;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#getMinimumSize(int, int)
	 */
	public Dimension getMinimumSize(int wHint, int hHint) {
		if (DesignChoiceConstants.DISPLAY_NONE.equals(display)) {
			return ZERO_DIMENSION;
		}

		int rx = recommendSize != null ? recommendSize.width : 0;
		int ry = recommendSize != null ? recommendSize.height : 0;

		if (wHint == -1 && hHint == -1) {
			// return ZERO_DIMENSION;

			int maxWidth = calcMaxSegment();

			// FlowUtilities
			return new Dimension(Math.max(maxWidth, rx), Math.max(getInsets()
					.getHeight(), ry));
		}

		// return the true minimum size with minimum width;
		Dimension dim = super.getMinimumSize(-1, hHint);

		if (dim.width < wHint) {
			return new Dimension(Math.max(dim.width, rx), Math.max(dim.height,
					ry));
		}

		dim = super.getMinimumSize(wHint, hHint);

		return new Dimension(Math.max(dim.width, rx), Math.max(dim.height, ry));

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
		return maxWidth;
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

	private static int getMinimumFontSize( Font ft )
	{
		if ( ft != null && ft.getFontData( ).length > 0 )
		{
			return ft.getFontData( )[0].getHeight( );
		}

		return 0;
	}

	/**
	 * Since Eclipse TextFlow figure ignore the trailing /r/n for calculating
	 * the client size, we must append the extra size ourselves.
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
					count++;
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
	 * Sets the display property of the Label.
	 * 
	 * @param display
	 *            the display property. this should be one of the following:
	 *            DesignChoiceConstants.DISPLAY_BLOCK |
	 *            DesignChoiceConstants.DISPLAY_INLINE |
	 *            DesignChoiceConstants.DISPLAY_NONE
	 */
	public void setDisplay(String display) {
		this.display = display;
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
	 * @param newText
	 *            the new text value.
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
	 * @param textOverline
	 *            The textOverline to set.
	 */
	public void setTextOverline(String textOverline) {
		label.setTextOverline(textOverline);
	}

	/**
	 * Sets the line-through style of the text.
	 * 
	 * @param textLineThrough
	 *            The textLineThrough to set.
	 */
	public void setTextLineThrough(String textLineThrough) {
		label.setTextLineThrough(textLineThrough);
	}

	/**
	 * Sets the underline style of the text.
	 * 
	 * @param textUnderline
	 *            The textUnderline to set.
	 */
	public void setTextUnderline(String textUnderline) {
		label.setTextUnderline(textUnderline);
	}

	/**
	 * Sets the horizontal text alignment style.
	 * 
	 * @param textAlign
	 *            The textAlign to set.
	 */
	public void setTextAlign(String textAlign) {
		label.setTextAlign(textAlign);
	}

	/**
	 * Sets the vertical text alignment style.
	 * 
	 * @param verticalAlign
	 *            The verticalAlign to set.
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
			LabelFigure tooltip = new LabelFigure();
			tooltip.setText(toolTip);
			tooltip.setBorder(new MarginBorder(0, 2, 0, 2));

			setToolTip(tooltip);
		} else {
			setToolTip(null);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#setFont(org.eclipse.swt.graphics.Font)
	 */
	public void setFont(Font f) {
		super.setFont(f);
		label.setFont(f);
	}
	
	/**
	 * @param specialPREFIX
	 */
	public void setSpecialPREFIX( String specialPREFIX )
	{
		label.setSpecialPREFIX( specialPREFIX );
	}
}