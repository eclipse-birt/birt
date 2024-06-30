/*************************************************************************************
 * Copyright (c) 2011, 2012, 2013 James Talbut.
 *  jim-emitters@spudsoft.co.uk
 *
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     James Talbut - Initial implementation.
 ************************************************************************************/

package uk.co.spudsoft.birt.emitters.excel;

import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.w3c.dom.css.CSSValue;

/**
 * Area borders class
 *
 * @since 3.3
 *
 */
public class AreaBorders {

	/** property: area is merged cell */
	public boolean isMergedCells;
	/** property: border bottom */
	public int bottom;
	/** property: border left */
	public int left;
	/** property: border right */
	public int right;
	/** property: border top */
	public int top;
	/** property: border diagonal */
	public int diagonal;
	/** property: border antidiagonal */
	public int antidiagonal;

	/** property: css style array */
	public CSSValue[] cssStyle = new CSSValue[6];
	/** property: css width array */
	public CSSValue[] cssWidth = new CSSValue[6];
	/** property: css color array */
	public CSSValue[] cssColour = new CSSValue[6];

	private AreaBorders(boolean isMergedCells, int bottom, int left, int right, int top, int diagonal, int antidiagonal,
			CSSValue[] cssStyle,
			CSSValue[] cssWidth, CSSValue[] cssColour) {
		this.isMergedCells = isMergedCells;
		this.bottom = bottom;
		this.left = left;
		this.right = right;
		this.top = top;
		this.diagonal = diagonal;
		this.antidiagonal = antidiagonal;
		this.cssStyle = cssStyle;
		this.cssWidth = cssWidth;
		this.cssColour = cssColour;
	}

	/**
	 * Create area borders and area is normal area/cell
	 *
	 * @param bottom       bottom border
	 * @param left         left border
	 * @param right        right border
	 * @param top          top border
	 * @param diagonal     diagonal border
	 * @param antidiagonal antidiagonal border
	 * @param borderStyle  border style
	 *
	 * @return Return the created area borders
	 */
	public static AreaBorders create(int bottom, int left, int right, int top, int diagonal, int antidiagonal,
			BirtStyle borderStyle) {
		return create(false, bottom, left, right, top, diagonal, antidiagonal, borderStyle);
	}

	/**
	 * Create area borders and area is a merged cell
	 *
	 * @param bottom       bottom border
	 * @param left         left border
	 * @param right        right border
	 * @param top          top border
	 * @param diagonal     diagonal border
	 * @param antidiagonal antidiagonal border
	 * @param borderStyle  border style
	 *
	 * @return Return the created area borders
	 */
	public static AreaBorders createForMergedCells(int bottom, int left, int right, int top, int diagonal,
			int antidiagonal, BirtStyle borderStyle) {
		return create(true, bottom, left, right, top, diagonal, antidiagonal, borderStyle);
	}

	/**
	 * Create area borders in dependency of the merged cell flag
	 *
	 * @param isMergedCells cell merged flag
	 * @param bottom        bottom border
	 * @param left          left border
	 * @param right         right border
	 * @param top           top border
	 * @param diagonal      diagonal border
	 * @param antidiagonal  antidiagonal border
	 * @param borderStyle   border style
	 *
	 * @return Return the created area borders
	 */
	public static AreaBorders create(boolean isMergedCells, int bottom, int left, int right, int top, int diagonal,
			int antidiagonal,
			BirtStyle borderStyle) {

		CSSValue borderStyleBottom = borderStyle.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_STYLE);
		CSSValue borderWidthBottom = borderStyle.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_WIDTH);
		CSSValue borderColourBottom = borderStyle.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_COLOR);
		CSSValue borderStyleLeft = borderStyle.getProperty(StyleConstants.STYLE_BORDER_LEFT_STYLE);
		CSSValue borderWidthLeft = borderStyle.getProperty(StyleConstants.STYLE_BORDER_LEFT_WIDTH);
		CSSValue borderColourLeft = borderStyle.getProperty(StyleConstants.STYLE_BORDER_LEFT_COLOR);
		CSSValue borderStyleRight = borderStyle.getProperty(StyleConstants.STYLE_BORDER_RIGHT_STYLE);
		CSSValue borderWidthRight = borderStyle.getProperty(StyleConstants.STYLE_BORDER_RIGHT_WIDTH);
		CSSValue borderColourRight = borderStyle.getProperty(StyleConstants.STYLE_BORDER_RIGHT_COLOR);
		CSSValue borderStyleTop = borderStyle.getProperty(StyleConstants.STYLE_BORDER_TOP_STYLE);
		CSSValue borderWidthTop = borderStyle.getProperty(StyleConstants.STYLE_BORDER_TOP_WIDTH);
		CSSValue borderColourTop = borderStyle.getProperty(StyleConstants.STYLE_BORDER_TOP_COLOR);

		CSSValue borderStyleDiagonal = borderStyle.getProperty(StyleConstants.STYLE_BORDER_DIAGONAL_STYLE);
		CSSValue borderWidthDiagonal = borderStyle.getProperty(StyleConstants.STYLE_BORDER_DIAGONAL_WIDTH);
		CSSValue borderColourDiagonal = borderStyle.getProperty(StyleConstants.STYLE_BORDER_DIAGONAL_COLOR);
		CSSValue borderStyleAntidiagonal = borderStyle.getProperty(StyleConstants.STYLE_BORDER_ANTIDIAGONAL_STYLE);
		CSSValue borderWidthAntidiagonal = borderStyle.getProperty(StyleConstants.STYLE_BORDER_ANTIDIAGONAL_WIDTH);
		CSSValue borderColourAntidiagonal = borderStyle.getProperty(StyleConstants.STYLE_BORDER_ANTIDIAGONAL_COLOR);

		if ((borderStyleBottom == null) || (CSSConstants.CSS_NONE_VALUE.equals(borderStyleBottom.getCssText()))
				|| (borderWidthBottom == null) || ("0".equals(borderWidthBottom.getCssText()))
				|| (borderColourBottom == null)
				|| (CSSConstants.CSS_TRANSPARENT_VALUE.equals(borderColourBottom.getCssText()))) {
			borderStyleBottom = null;
			borderWidthBottom = null;
			borderColourBottom = null;
		}

		if ((borderStyleLeft == null) || (CSSConstants.CSS_NONE_VALUE.equals(borderStyleLeft.getCssText()))
				|| (borderWidthLeft == null) || ("0".equals(borderWidthLeft.getCssText())) || (borderColourLeft == null)
				|| (CSSConstants.CSS_TRANSPARENT_VALUE.equals(borderColourLeft.getCssText()))) {
			borderStyleLeft = null;
			borderWidthLeft = null;
			borderColourLeft = null;
		}

		if ((borderStyleRight == null) || (CSSConstants.CSS_NONE_VALUE.equals(borderStyleRight.getCssText()))
				|| (borderWidthRight == null) || ("0".equals(borderWidthRight.getCssText()))
				|| (borderColourRight == null)
				|| (CSSConstants.CSS_TRANSPARENT_VALUE.equals(borderColourRight.getCssText()))) {
			borderStyleRight = null;
			borderWidthRight = null;
			borderColourRight = null;
		}

		if ((borderStyleTop == null) || (CSSConstants.CSS_NONE_VALUE.equals(borderStyleTop.getCssText()))
				|| (borderWidthTop == null) || ("0".equals(borderWidthTop.getCssText())) || (borderColourTop == null)
				|| (CSSConstants.CSS_TRANSPARENT_VALUE.equals(borderColourTop.getCssText()))) {
			borderStyleTop = null;
			borderWidthTop = null;
			borderColourTop = null;
		}
		if ((borderStyleDiagonal == null) || (CSSConstants.CSS_NONE_VALUE.equals(borderStyleDiagonal.getCssText()))
				|| (borderWidthDiagonal == null) || ("0".equals(borderWidthDiagonal.getCssText()))
				|| (borderColourDiagonal == null)
				|| (CSSConstants.CSS_TRANSPARENT_VALUE.equals(borderColourDiagonal.getCssText()))) {
			borderStyleDiagonal = null;
			borderWidthDiagonal = null;
			borderColourDiagonal = null;
		}
		if ((borderStyleAntidiagonal == null)
				|| (CSSConstants.CSS_NONE_VALUE.equals(borderStyleAntidiagonal.getCssText()))
				|| (borderWidthAntidiagonal == null) || ("0".equals(borderWidthAntidiagonal.getCssText()))
				|| (borderColourAntidiagonal == null)
				|| (CSSConstants.CSS_TRANSPARENT_VALUE.equals(borderColourAntidiagonal.getCssText()))) {
			borderStyleAntidiagonal = null;
			borderWidthAntidiagonal = null;
			borderColourAntidiagonal = null;
		}

		if (((bottom >= 0)
				&& ((borderStyleBottom != null) || (borderWidthBottom != null) || (borderColourBottom != null)))
				|| ((left >= 0)
						&& ((borderStyleLeft != null) || (borderWidthLeft != null) || (borderColourLeft != null)))
				|| ((right >= 0)
						&& ((borderStyleRight != null) || (borderWidthRight != null) || (borderColourRight != null)))
				|| ((top >= 0)
						&& ((borderStyleTop != null) || (borderWidthTop != null) || (borderColourTop != null)))
				|| ((diagonal >= 0) && ((borderStyleDiagonal != null) || (borderWidthDiagonal != null)
						|| (borderColourDiagonal != null)))
				|| ((antidiagonal >= 0) && ((borderStyleAntidiagonal != null) || (borderWidthAntidiagonal != null)
						|| (borderColourAntidiagonal != null)))

		) {
			CSSValue[] cssStyle = { borderStyleBottom, borderStyleLeft, borderStyleRight, borderStyleTop,
					borderStyleDiagonal, borderStyleAntidiagonal };
			CSSValue[] cssWidth = { borderWidthBottom, borderWidthLeft, borderWidthRight, borderWidthTop,
					borderWidthDiagonal, borderWidthAntidiagonal };
			CSSValue[] cssColour = { borderColourBottom, borderColourLeft, borderColourRight, borderColourTop,
					borderColourDiagonal, borderColourAntidiagonal };
			return new AreaBorders(isMergedCells, bottom, left, right, top, diagonal, antidiagonal, cssStyle, cssWidth,
					cssColour);
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("[").append(top).append(",").append(left).append("]");
		result.append("-");
		result.append("[").append(bottom).append(",").append(right).append("]");
		result.append("=");
		for (int i = 0; i < 6; ++i) {
			result.append("[");
			result.append(cssStyle[i]);
			result.append(";");
			result.append(cssWidth[i]);
			result.append(";");
			result.append(cssColour[i]);
			result.append("]");
		}
		return result.toString();
	}

}
