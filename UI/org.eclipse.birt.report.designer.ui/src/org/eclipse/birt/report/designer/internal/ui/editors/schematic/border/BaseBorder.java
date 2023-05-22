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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.border;

import java.util.HashMap;

import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.swt.SWT;

/**
 * Base class for border
 */
public abstract class BaseBorder extends AbstractBorder {

	// Defines keys for line style.

	/** style property: no style */
	public static final String STYLE_NONO = DesignChoiceConstants.LINE_STYLE_NONE;
	/** style property: solid style */
	public static final String STYLE_SOLID = DesignChoiceConstants.LINE_STYLE_SOLID;
	/** style property: dotted style */
	public static final String STYLE_DOTTED = DesignChoiceConstants.LINE_STYLE_DOTTED;
	/** style property: dashed style */
	public static final String STYLE_DASHED = DesignChoiceConstants.LINE_STYLE_DASHED;
	/** style property: double style */
	public static final String STYLE_DOUBLE = DesignChoiceConstants.LINE_STYLE_DOUBLE;
	/** style property: ridge style */
	public static final String STYLE_RIDGE = DesignChoiceConstants.LINE_STYLE_RIDGE;
	/** style property: groove style */
	public static final String STYLE_GROOVE = DesignChoiceConstants.LINE_STYLE_GROOVE;
	/** style property: inset style */
	public static final String STYLE_INSET = DesignChoiceConstants.LINE_STYLE_INSET;
	/** style property: outset style */
	public static final String STYLE_OUTSET = DesignChoiceConstants.LINE_STYLE_OUTSET;


	// Defines line styles for painting.

	/** style property: line style solid */
	public static final int LINE_STYLE_SOLID = SWT.LINE_SOLID;
	/** style property: line style doted */
	public static final int LINE_STYLE_DOTTED = SWT.LINE_DOT;
	/** style property: line style dashed */
	public static final int LINE_STYLE_DASHED = SWT.LINE_DASH;

	/** style property: line style no */
	public static final int LINE_STYLE_NONE = 0;
	/** style property: line style double */
	public static final int LINE_STYLE_DOUBLE = -2;
	/** style property: line style ridge */
	public static final int LINE_STYLE_RIDGE = -3;
	/** style property: line style groove */
	public static final int LINE_STYLE_GROOVE = -4;
	/** style property: line style inset */
	public static final int LINE_STYLE_INSET = -5;
	/** style property: line style outset */
	public static final int LINE_STYLE_OUTSET = -6;

	/**
	 * Bottom border width.
	 */
	public String bottomWidth;
	/**
	 * Bottom border style.
	 */
	public String bottomStyle;
	/**
	 * Bottom border color.
	 */
	public int bottomColor;
	/**
	 * Top border width.
	 */
	public String topWidth;
	/**
	 * Top border style.
	 */
	public String topStyle;
	/**
	 * Top border color.
	 */
	public int topColor;
	/**
	 * Left border width.
	 */
	public String leftWidth;
	/**
	 * Left border style.
	 */
	public String leftStyle;
	/**
	 * Left border color.
	 */
	public int leftColor;
	/**
	 * Right border width.
	 */
	public String rightWidth;
	/**
	 * Right border style.
	 */
	public String rightStyle;
	/**
	 * Right border color.
	 */
	public int rightColor;
	/**
	 * Diagonal number.
	 */
	public int diagonalNumber;
	/**
	 * Diagonal width.
	 */
	public String diagonalWidth;
	/**
	 * Diagonal style.
	 */
	public String diagonalStyle;
	/**
	 * Diagonal color.
	 */
	public int diagonalColor;
	/**
	 * Antidiagonal number.
	 */
	public int antidiagonalNumber;
	/**
	 * Antidiagonal width.
	 */
	public String antidiagonalWidth;
	/**
	 * Antidiagonal style.
	 */
	public String antidiagonalStyle;
	/**
	 * Antidiagonal color.
	 */
	public int antidiagonalColor;

	protected int i_bottom_style, i_bottom_width = 1;
	protected int i_top_style, i_top_width = 1;
	protected int i_left_style, i_left_width = 1;
	protected int i_right_style, i_right_width = 1;
	protected int i_diagonal_style, i_diagonal_width = 1;
	protected int i_antidiagonal_style, i_antidiagonal_width = 1;

	private static final HashMap<String, Integer> styleMap = new HashMap<>();
	private static final HashMap<String, Integer> widthMap = new HashMap<>();

	private static final double EPS = 1.0E-10;

	protected int leftGap, rightGap, bottomGap, topGap;

	static {
		styleMap.put(STYLE_SOLID, Integer.valueOf(LINE_STYLE_SOLID));
		styleMap.put(STYLE_DOTTED, Integer.valueOf(LINE_STYLE_DOTTED));
		styleMap.put(STYLE_DASHED, Integer.valueOf(LINE_STYLE_DASHED));
		styleMap.put(STYLE_DOUBLE, Integer.valueOf(LINE_STYLE_DOUBLE));
		styleMap.put(STYLE_RIDGE, Integer.valueOf(LINE_STYLE_RIDGE));
		styleMap.put(STYLE_GROOVE, Integer.valueOf(LINE_STYLE_GROOVE));
		styleMap.put(STYLE_INSET, Integer.valueOf(LINE_STYLE_INSET));
		styleMap.put(STYLE_OUTSET, Integer.valueOf(LINE_STYLE_OUTSET));
		styleMap.put(STYLE_NONO, Integer.valueOf(LINE_STYLE_NONE));

		widthMap.put(DesignChoiceConstants.LINE_WIDTH_THIN, Integer.valueOf(1));
		widthMap.put(DesignChoiceConstants.LINE_WIDTH_MEDIUM, Integer.valueOf(2));
		widthMap.put(DesignChoiceConstants.LINE_WIDTH_THICK, Integer.valueOf(3));
	}

	private static final HashMap<String, Integer> commonCacheWidthMap = new HashMap<>();

	/**
	 * Clean up the width cache.
	 */
	public static void cleanWidthCache() {
		commonCacheWidthMap.clear();
	}

	/**
	 * Since the insets now include border and padding, use this to get the true and
	 * non-revised border insets.
	 *
	 * @return border insets.
	 */
	public abstract Insets getTrueBorderInsets();

	/**
	 * Since the insets now include border and padding, use this to get the border
	 * insets. This value may be revised according to specified element.
	 *
	 * @return border insets.
	 */
	public abstract Insets getBorderInsets();

	/**
	 * Sets the insets for padding.
	 *
	 * @param in
	 */
	public abstract void setPaddingInsets(Insets in);

	/**
	 * Returns the border style.
	 *
	 * @param obj
	 * @return Returns the border style
	 */
	protected int getBorderStyle(Object obj) {
		Integer retValue = styleMap.get(obj);
		if (retValue == null) {
			// fix bug 168627.the default style is solid.
			return SWT.LINE_SOLID;
			// return SWT.LINE_DASH;
		}

		return retValue.intValue();
	}

	/**
	 * Returns the border width as pixel.
	 *
	 * @param obj
	 * @return Return the border width as pixel
	 */
	protected int getBorderWidth(Object obj) {
		// handle predefined values.
		Integer retValue = widthMap.get(obj);

		if (retValue != null) {
			return retValue.intValue();
		}

		// handle cached values.
		retValue = commonCacheWidthMap.get(obj);

		if (retValue != null) {
			return retValue.intValue();
		}

		// handle non-predefined values.
		if (obj instanceof String) {
			String[] rt = DEUtil.splitString((String) obj);

			if (rt[0] != null && DEUtil.isValidNumber(rt[0])) {
				double w = DEUtil.convertoToPixel(new DimensionValue(Double.parseDouble(rt[0]), rt[1]));

				// if the width is too small,
				// think it's zero
				if (w <= EPS) {
					return 0;
				}

				// if the width is not too small;
				// think it's minimum size is 1
				int cw = Math.max(1, (int) w);

				// put to cache
				commonCacheWidthMap.put((String) obj, cw);

				return cw;
			}
		}

		return 1;
	}

	/**
	 * Convenient method to return the specified border style directly.
	 *
	 * @return Return the specified border style directly.
	 */
	public int getLeftBorderStyle() {
		return getBorderStyle(leftStyle);
	}

	/**
	 * Convenient method to return the specified border style directly.
	 *
	 * @return Return the specified border style directly.
	 */
	public int getRightBorderStyle() {
		return getBorderStyle(rightStyle);
	}

	/**
	 * Convenient method to return the specified border style directly.
	 *
	 * @return Return the specified border style directly.
	 */
	public int getTopBorderStyle() {
		return getBorderStyle(topStyle);
	}

	/**
	 * Convenient method to return the specified border style directly.
	 *
	 * @return Return the specified border style directly.
	 */
	public int getBottomBorderStyle() {
		return getBorderStyle(bottomStyle);
	}

	/**
	 * Convenient method to return the specified border width directly.
	 *
	 * @return Return the specified border width directly.
	 */
	public int getLeftBorderWidth() {
		return getBorderWidth(leftWidth);
	}

	/**
	 * Convenient method to return the specified border width directly.
	 *
	 * @return Return the specified border width directly.
	 */
	public int getRightBorderWidth() {
		return getBorderWidth(rightWidth);
	}

	/**
	 * Convenient method to return the specified border width directly.
	 *
	 * @return Return the specified border width directly.
	 */
	public int getTopBorderWidth() {
		return getBorderWidth(topWidth);
	}

	/**
	 * Convenient method to return the specified border width directly.
	 *
	 * @return Return the specified border width directly.
	 */
	public int getBottomBorderWidth() {
		return getBorderWidth(bottomWidth);
	}

	/**
	 * Convenient method to return the specified border color directly.
	 *
	 * @return Return the specified border color directly.
	 */
	public int getLeftBorderColor() {
		// return ColorUtil.parseColor( leftColor );
		return leftColor;
	}

	/**
	 * Convenient method to return the specified border color directly.
	 *
	 * @return Return the specified border color directly.
	 */
	public int getRightBorderColor() {
		// return ColorUtil.parseColor( rightColor );
		return rightColor;
	}

	/**
	 * Convenient method to return the specified border color directly.
	 *
	 * @return Return the specified border color directly.
	 */
	public int getTopBorderColor() {
		// return ColorUtil.parseColor( topColor );
		return topColor;
	}

	/**
	 * Convenient method to return the specified border color directly.
	 *
	 * @return Return the specified border color directly.
	 */
	public int getBottomBorderColor() {
		// return ColorUtil.parseColor( bottomColor );
		return bottomColor;
	}

	/**
	 * Convenient method to return the specified border style directly.
	 *
	 * @return Return the specified border style directly.
	 */
	public int getDiagonalNumber() {
		return diagonalNumber;
	}

	/**
	 * Convenient method to return the specified border style directly.
	 *
	 * @return Return the specified border style directly.
	 */
	public int getDiagonalStyle() {
		return getBorderStyle(diagonalStyle);
	}

	/**
	 * Convenient method to return the specified border width directly.
	 *
	 * @return Return the specified border width directly.
	 */
	public int getDiagonalWidth() {
		return getBorderWidth(diagonalWidth);
	}

	/**
	 * Convenient method to return the specified border color directly.
	 *
	 * @return Return the specified border color directly.
	 */
	public int getDiagonalColor() {
		return diagonalColor;
	}

	/**
	 * Convenient method to return the specified border style directly.
	 *
	 * @return Return the specified border style directly.
	 */
	public int getAntidiagonalNumber() {
		return antidiagonalNumber;
	}

	/**
	 * Convenient method to return the specified border style directly.
	 *
	 * @return Return the specified border style directly.
	 */
	public int getAntidiagonalStyle() {
		return getBorderStyle(antidiagonalStyle);
	}

	/**
	 * Convenient method to return the specified border width directly.
	 *
	 * @return Return the specified border width directly.
	 */
	public int getAntidiagonalWidth() {
		return getBorderWidth(antidiagonalWidth);
	}

	/**
	 * Convenient method to return the specified border color directly.
	 *
	 * @return Return the specified border color directly.
	 */
	public int getAntidiagonalColor() {
		return antidiagonalColor;
	}

}
