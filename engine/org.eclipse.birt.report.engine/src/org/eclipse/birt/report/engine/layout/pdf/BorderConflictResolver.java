/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf;

import java.util.HashMap;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.value.Value;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.w3c.dom.css.CSSValue;

/**
 * This class implments border conflict algorithm.
 * <p>
 * In the collapsing border model, borders at every edge of every cell may be
 * specified by border properties on a variety of elements that meet at that
 * edge (cells, rows, row groups, columns, column groups, and the table itself),
 * and these borders may vary in width, style, and color. The rule of thumb is
 * that at each edge the most "eye catching" border style is chosen, except that
 * any occurrence of the style <code>hidden</code> unconditionally turns the
 * border off.
 * <p>
 * The following rules determine which border style "wins" in case of a
 * conflict:
 * <ul>
 * <li>Borders with the 'border-style' of 'hidden' take precedence over all
 * other conflicting borders. Any border with this value suppresses all borders
 * at this location.
 * <li>Borders with a style of 'none' have the lowest priority. Only if the
 * border properties of all the elements meeting at this edge are 'none' will
 * the border be omitted (but note that <code>none</code> is the default value
 * for the border style.)
 * <li>If none of the styles are 'hidden' and at least one of them is not
 * 'none', then narrow borders are discarded in favor of wider ones. If several
 * have the same 'border-width' then styles are preferred in this order:
 * <code>double</code>, <code>solid</code>, <code>dashed</code>,
 * <code>dotted</code>, <code>ridge</code>, <code>outset</code>,
 * <code>groove</code>, and the lowest: <code>inset</code>.
 * <li>If border styles differ only in color, then a style set on a cell wins
 * over one on a row, which wins over a row group, column, column group and,
 * lastly, table. It is undefined which color is used when two elements of the
 * same type disagree.
 * </ul>
 * 
 * 
 */
///TODO: change the border style's resolve.
public class BorderConflictResolver {

	final static int POSITION_LEFT = 0;

	final static int POSITION_TOP = 1;

	final static int POSITION_RIGHT = 2;

	final static int POSITION_BOTTOM = 3;

	private int POSITION_LEAD = POSITION_LEFT;

	private int POSITION_TRAIL = POSITION_RIGHT;

	static HashMap<Value, Integer> styleMap = null;
	static {
		styleMap = new HashMap<Value, Integer>();
		styleMap.put(IStyle.NONE_VALUE, 0);
		styleMap.put(IStyle.INSET_VALUE, 1);
		styleMap.put(IStyle.GROOVE_VALUE, 2);
		styleMap.put(IStyle.OUTSET_VALUE, 3);
		styleMap.put(IStyle.RIDGE_VALUE, 4);
		styleMap.put(IStyle.DOTTED_VALUE, 5);
		styleMap.put(IStyle.DASHED_VALUE, 6);
		styleMap.put(IStyle.SOLID_VALUE, 7);
		styleMap.put(IStyle.DOUBLE_VALUE, 8);
	}

	final static int[] BORDER_COLOR_POPERTIES = new int[] { IStyle.STYLE_BORDER_LEFT_COLOR,
			IStyle.STYLE_BORDER_TOP_COLOR, IStyle.STYLE_BORDER_RIGHT_COLOR, IStyle.STYLE_BORDER_BOTTOM_COLOR };

	final static int[] BORDER_WIDTH_POPERTIES = new int[] { IStyle.STYLE_BORDER_LEFT_WIDTH,
			IStyle.STYLE_BORDER_TOP_WIDTH, IStyle.STYLE_BORDER_RIGHT_WIDTH, IStyle.STYLE_BORDER_BOTTOM_WIDTH };

	final static int[] BORDER_STYLE_POPERTIES = new int[] { IStyle.STYLE_BORDER_LEFT_STYLE,
			IStyle.STYLE_BORDER_TOP_STYLE, IStyle.STYLE_BORDER_RIGHT_STYLE, IStyle.STYLE_BORDER_BOTTOM_STYLE };

	protected BorderCache tableLeftBorderCache = new BorderCache(4);

	/**
	 * The used style should be style of area which is writable, and the others are
	 * styles of content which is read-only.
	 * 
	 * @param tableLeft
	 * @param columnLeft
	 * @param cellLeft
	 * @param usedStyle
	 */
	public void resolveTableLeftBorder(IStyle tableLeft, IStyle rowLeft, IStyle columnLeft, IStyle cellLeft,
			IStyle usedStyle) {
		resolveBorder(tableLeftBorderCache,
				new BorderStyleInfo[] { new BorderStyleInfo(cellLeft, POSITION_LEAD),
						new BorderStyleInfo(columnLeft, POSITION_LEAD), new BorderStyleInfo(rowLeft, POSITION_LEAD),
						new BorderStyleInfo(tableLeft, POSITION_LEAD) },
				new BorderStyleInfo(usedStyle, POSITION_LEAD));
	}

	protected BorderCache tableTopBorderCache = new BorderCache(4);

	/**
	 * The used style should be style of area which is writable, and the others are
	 * styles of content which is read-only.
	 * 
	 * @param tableTop
	 * @param rowTop
	 * @param cellTop
	 * @param usedStyle
	 */
	public void resolveTableTopBorder(IStyle tableTop, IStyle rowTop, IStyle columnTop, IStyle cellTop,
			IStyle usedStyle) {
		resolveBorder(tableTopBorderCache,
				new BorderStyleInfo[] { new BorderStyleInfo(cellTop, POSITION_TOP),
						new BorderStyleInfo(rowTop, POSITION_TOP), new BorderStyleInfo(columnTop, POSITION_TOP),
						new BorderStyleInfo(tableTop, POSITION_TOP) },
				new BorderStyleInfo(usedStyle, POSITION_TOP));
	}

	protected BorderCache tableBottomBorderCache = new BorderCache(4);

	/**
	 * The used style should be style of area which is writable, and the others are
	 * styles of content which is read-only.
	 * 
	 * @param tableBottom
	 * @param rowBottom
	 * @param cellBottom
	 * @param usedStyle
	 */
	public void resolveTableBottomBorder(IStyle tableBottom, IStyle rowBottom, IStyle columnBottom, IStyle cellBottom,
			IStyle usedStyle) {
		resolveBorder(tableBottomBorderCache, new BorderStyleInfo[] { new BorderStyleInfo(cellBottom, POSITION_BOTTOM),
				new BorderStyleInfo(rowBottom, POSITION_BOTTOM), new BorderStyleInfo(columnBottom, POSITION_BOTTOM),
				new BorderStyleInfo(tableBottom, POSITION_BOTTOM) }, new BorderStyleInfo(usedStyle, POSITION_BOTTOM));
	}

	protected BorderCache pagenatedTableTopBorderCache = new BorderCache(4);

	public void resolvePagenatedTableTopBorder(IStyle rowTop, IStyle cellTop, IStyle usedStyle) {
		resolveBorder(pagenatedTableTopBorderCache, new BorderStyleInfo[] { new BorderStyleInfo(cellTop, POSITION_TOP),
				new BorderStyleInfo(rowTop, POSITION_TOP) }, new BorderStyleInfo(usedStyle, POSITION_TOP));
	}

	protected BorderCache pagenatedTableBottomBorderCache = new BorderCache(4);

	public void resolvePagenatedTableBottomBorder(IStyle rowBottom, IStyle cellBottom, IStyle usedStyle) {
		resolveBorder(pagenatedTableBottomBorderCache,
				new BorderStyleInfo[] { new BorderStyleInfo(cellBottom, POSITION_BOTTOM),
						new BorderStyleInfo(rowBottom, POSITION_BOTTOM) },
				new BorderStyleInfo(usedStyle, POSITION_BOTTOM));
	}

	protected BorderCache tableRightBorderCache = new BorderCache(4);

	/**
	 * The used style should be style of area which is writable, and the others are
	 * styles of content which is read-only.
	 * 
	 * @param tableRight
	 * @param columnRight
	 * @param cellRight
	 * @param usedStyle
	 */
	public void resolveTableRightBorder(IStyle tableRight, IStyle rowRight, IStyle columnRight, IStyle cellRight,
			IStyle usedStyle) {
		resolveBorder(tableRightBorderCache,
				new BorderStyleInfo[] { new BorderStyleInfo(cellRight, POSITION_TRAIL),
						new BorderStyleInfo(columnRight, POSITION_TRAIL), new BorderStyleInfo(rowRight, POSITION_TRAIL),
						new BorderStyleInfo(tableRight, POSITION_TRAIL) },
				new BorderStyleInfo(usedStyle, POSITION_TRAIL));
	}

	protected BorderCache cellLeftBorderCache = new BorderCache(4);

	/**
	 * The used style should be style of area which is writable, and the others are
	 * styles of content which is read-only.
	 * 
	 * @param preColumnRight
	 * @param columnLeft
	 * @param preCellRight
	 * @param cellLeft
	 * @param usedStyle
	 */
	public void resolveCellLeftBorder(IStyle preColumnRight, IStyle columnLeft, IStyle preCellRight, IStyle cellLeft,
			IStyle usedStyle) {
		resolveBorder(cellLeftBorderCache, new BorderStyleInfo[] { new BorderStyleInfo(preCellRight, POSITION_TRAIL),
				new BorderStyleInfo(cellLeft, POSITION_LEAD), new BorderStyleInfo(preColumnRight, POSITION_TRAIL),
				new BorderStyleInfo(columnLeft, POSITION_LEAD) }, new BorderStyleInfo(usedStyle, POSITION_LEAD));
	}

	protected BorderCache cellTopBorderCache = new BorderCache(4);

	/**
	 * The used style should be style of area which is writable, and the others are
	 * styles of content which is read-only.
	 * 
	 * @param preRowBottom
	 * @param rowTop
	 * @param preCellBottom
	 * @param cellTop
	 * @param usedStyle
	 */
	public void resolveCellTopBorder(IStyle preRowBottom, IStyle rowTop, IStyle preCellBottom, IStyle cellTop,
			IStyle usedStyle) {
		resolveBorder(cellTopBorderCache,
				new BorderStyleInfo[] { new BorderStyleInfo(preCellBottom, POSITION_BOTTOM),
						new BorderStyleInfo(cellTop, POSITION_TOP), new BorderStyleInfo(preRowBottom, POSITION_BOTTOM),
						new BorderStyleInfo(rowTop, POSITION_TOP) },
				new BorderStyleInfo(usedStyle, POSITION_TOP));
	}

	private void resolveBorder(BorderCache cache, BorderStyleInfo[] styles, BorderStyleInfo usedStyle) {
		if (cache.isSame(styles)) {
			usedStyle.setBorder(cache.borderStyle, cache.borderWidth, cache.borderColor);
			return;
		}

		CSSValue[] borderStyles = new CSSValue[styles.length];
		for (int i = 0; i < styles.length; i++) {
			borderStyles[i] = styles[i].getBorderStyle();
			if (IStyle.HIDDEN_VALUE.equals(borderStyles[i])) {
				usedStyle.setBorderStyle(IStyle.HIDDEN_VALUE);
				cache.setValues(styles, IStyle.HIDDEN_VALUE, IStyle.NUMBER_0, IStyle.BLACK_VALUE);
				return;
			}
		}

		// resolve border width
		int maxWidth = 0;
		int maxCount = 1;
		int maxFirstIndex = 0;
		int[] ws = new int[styles.length];
		CSSValue[] borderWidths = new CSSValue[styles.length];
		for (int i = 0; i < styles.length; i++) {
			borderWidths[i] = styles[i].getBorderWidth();
			ws[i] = PropertyUtil.getDimensionValue(styles[i].getBorderWidth());
			if (ws[i] > maxWidth) {
				maxWidth = ws[i];
				maxCount = 1;
				maxFirstIndex = i;
			} else if (ws[i] == maxWidth) {
				maxCount++;
			}
		}

		if (maxWidth == 0 || maxCount == 1) {
			CSSValue color = styles[maxFirstIndex].getBorderColor();
			usedStyle.setBorder(borderStyles[maxFirstIndex], borderWidths[maxFirstIndex], color);
			cache.setValues(styles, borderStyles[maxFirstIndex], borderWidths[maxFirstIndex], color);
			return;
		} else {
			// resolve border style
			int max = 0;
			int maxStyleIndex = 0;
			int[] ss = new int[styles.length];
			for (int i = 0; i < styles.length; i++) {
				if (ws[i] == maxWidth) {
					ss[i] = ((Integer) styleMap.get(styles[i].getBorderStyle())).intValue();
					if (ss[i] > max) {
						max = ss[i];
						maxStyleIndex = i;
					}
				}
			}
			CSSValue color = styles[maxStyleIndex].getBorderColor();
			usedStyle.setBorder(borderStyles[maxStyleIndex], borderWidths[maxStyleIndex], color);
			cache.setValues(styles, borderStyles[maxStyleIndex], borderWidths[maxStyleIndex], color);
		}
	}

	public void setRTL(boolean rtl) {
		if (rtl) {
			POSITION_LEAD = POSITION_RIGHT;
			POSITION_TRAIL = POSITION_LEFT;
		} else {
			// XXX currently useless
			POSITION_LEAD = POSITION_LEFT;
			POSITION_TRAIL = POSITION_RIGHT;
		}
	}

	protected static class BorderStyleInfo {

		protected int position;

		protected IStyle style;

		public BorderStyleInfo(IStyle style, int position) {
			this.style = style;
			this.position = position;
		}

		public void setBorderColor(CSSValue value) {
			assert (style != null);
			style.setProperty(BORDER_COLOR_POPERTIES[position], value);
		}

		public CSSValue getBorderColor() {
			if (style != null) {
				return style.getProperty(BORDER_COLOR_POPERTIES[position]);
			}
			return IStyle.BLACK_RGB_VALUE;
		}

		public CSSValue getBorderStyle() {
			if (style != null) {
				return style.getProperty(BORDER_STYLE_POPERTIES[position]);
			}
			return IStyle.NONE_VALUE;
		}

		public CSSValue getBorderWidth() {
			if (style != null) {
				return style.getProperty(BORDER_WIDTH_POPERTIES[position]);
			}
			return null;
		}

		private void setBorderStyle(CSSValue value) {
			style.setProperty(BORDER_STYLE_POPERTIES[position], value);
		}

		private void setBorderWidth(CSSValue value) {
			style.setProperty(BORDER_WIDTH_POPERTIES[position], value);
		}

		public void setBorder(CSSValue style, CSSValue width, CSSValue color) {
			setBorderStyle(style);
			setBorderWidth(width);
			setBorderColor(color);
		}
	}

	private static class BorderCache {

		IStyle[] styles;
		CSSValue borderStyle;
		CSSValue borderWidth;
		CSSValue borderColor;

		BorderCache(int styleCount) {
			styles = new IStyle[styleCount];
		}

		public void setValues(BorderStyleInfo[] stylesInfo, CSSValue borderStyle, CSSValue borderWidth,
				CSSValue borderColor) {
			this.borderColor = borderColor;
			this.borderStyle = borderStyle;
			this.borderWidth = borderWidth;
			for (int i = 0; i < stylesInfo.length; i++) {
				styles[i] = stylesInfo[i].style;
			}
		}

		public boolean isSame(BorderStyleInfo[] stylesInfo) {
			if (styles.length == stylesInfo.length) {
				for (int i = 0; i < styles.length; i++) {
					if (styles[i] != stylesInfo[i].style) {
						return false;
					}
				}
				return true;
			}
			return false;
		}
	}
}
