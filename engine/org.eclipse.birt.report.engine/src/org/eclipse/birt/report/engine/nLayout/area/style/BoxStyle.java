/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.nLayout.area.style;

import java.awt.Color;

/**
 * Define class of box style
 *
 * @since 3.3
 *
 */
public class BoxStyle extends AreaConstants {

	/**
	 * represents a default box style , it is immutable.
	 *
	 * <pre>
	 * BoxStyle style = BoxStyle.DEFAULT;
	 * // this is wrong.
	 * style.setBakgroundColor(color);
	 * // do like this when you want to use setter().
	 * // create a new instance.this is OK.
	 * style = new BoxStyle();
	 * style.setBakgroundColor(color);
	 * </pre>
	 *
	 * use this code be careful !
	 */
	public final static BoxStyle DEFAULT = new ImmutableBoxStyle();

	protected Color backgroundColor = null;

	protected BackgroundImageInfo backgroundImage = null;

	protected BorderInfo topBorder = null;

	protected BorderInfo leftBorder = null;

	protected BorderInfo rightBorder = null;

	protected BorderInfo bottomBorder = null;

	/**
	 * Get Background color
	 *
	 * @return Return the background color
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Constructor
	 */
	public BoxStyle() {

	}

	private static class ImmutableBoxStyle extends BoxStyle {
		@Override
		public void setBackgroundColor(Color backgroundColor) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBottomBorder(BorderInfo bottomBorder) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBackgroundImage(BackgroundImageInfo backgroundImage) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLeftBorder(BorderInfo leftBorder) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRightBorder(BorderInfo rightBorder) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTopBorder(BorderInfo topBorder) {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * Constructor with box style (border info)
	 *
	 * @param bs
	 */
	public BoxStyle(BoxStyle bs) {
		this.backgroundColor = bs.backgroundColor;
		if (bs.topBorder != null) {
			this.topBorder = new BorderInfo(bs.topBorder);
		}
		if (bs.leftBorder != null) {
			this.leftBorder = new BorderInfo(bs.leftBorder);
		}
		if (bs.rightBorder != null) {
			this.rightBorder = new BorderInfo(bs.rightBorder);
		}
		if (bs.bottomBorder != null) {
			this.bottomBorder = new BorderInfo(bs.bottomBorder);
		}
		if (bs.backgroundImage != null) {
			this.backgroundImage = new BackgroundImageInfo(bs.backgroundImage);
		}
	}

	/**
	 * CLear the border
	 */
	public void clearBorder() {
		topBorder = null;
		leftBorder = null;
		rightBorder = null;
		bottomBorder = null;
	}

	/**
	 * Get the background image info
	 *
	 * @return Return the background image info
	 */
	public BackgroundImageInfo getBackgroundImage() {
		return backgroundImage;
	}

	/**
	 * Get the top border info
	 *
	 * @return Return the top border info
	 */
	public BorderInfo getTopBorder() {
		return topBorder;
	}

	/**
	 * Get the left border info
	 *
	 * @return Return the left border info
	 */
	public BorderInfo getLeftBorder() {
		return leftBorder;
	}

	/**
	 * Get the right border info
	 *
	 * @return Return the right border info
	 */
	public BorderInfo getRightBorder() {
		return rightBorder;
	}

	/**
	 * Get the bottom border info
	 *
	 * @return Return the bottm border info
	 */
	public BorderInfo getBottomBorder() {
		return bottomBorder;
	}

	/**
	 * Get the left border width
	 *
	 * @return Return the left border width
	 */
	public int getLeftBorderWidth() {
		if (leftBorder != null) {
			return leftBorder.getWidth();
		}
		return 0;
	}

	/**
	 * Get the right border width
	 *
	 * @return Return the right border width
	 */
	public int getRightBorderWidth() {
		if (rightBorder != null) {
			return rightBorder.getWidth();
		}
		return 0;
	}

	/**
	 * Get the top border width
	 *
	 * @return Return the top border width
	 */
	public int getTopBorderWidth() {
		if (topBorder != null) {
			return topBorder.getWidth();
		}
		return 0;
	}

	/**
	 * Get the bottom border width
	 *
	 * @return Return the bottom border width
	 */
	public int getBottomBorderWidth() {
		if (bottomBorder != null) {
			return bottomBorder.getWidth();
		}
		return 0;
	}

	/**
	 * Get the left border color
	 *
	 * @return Return the left border color
	 */
	public Color getLeftBorderColor() {
		if (leftBorder != null) {
			return leftBorder.getColor();
		}
		return null;
	}

	/**
	 * Get the right border color
	 *
	 * @return Return the right border color
	 */
	public Color getRightBorderColor() {
		if (rightBorder != null) {
			return rightBorder.getColor();
		}
		return null;
	}

	/**
	 * Get the top border color
	 *
	 * @return Return the top border color
	 */
	public Color getTopBorderColor() {
		if (topBorder != null) {
			return topBorder.getColor();
		}
		return null;
	}

	/**
	 * Get the bottom border color
	 *
	 * @return Return the bottom border color
	 */
	public Color getBottomBorderColor() {
		if (bottomBorder != null) {
			return bottomBorder.getColor();
		}
		return null;
	}

	/**
	 * Get the left border style
	 *
	 * @return Return the left border style
	 */
	public int getLeftBorderStyle() {
		if (leftBorder != null) {
			return leftBorder.getStyle();
		}
		return 0;
	}

	/**
	 * Get the right border style
	 *
	 * @return Return the right border style
	 */
	public int getRightBorderStyle() {
		if (rightBorder != null) {
			return rightBorder.getStyle();
		}
		return 0;
	}

	/**
	 * Get the top border style
	 *
	 * @return Return the top border style
	 */
	public int getTopBorderStyle() {
		if (topBorder != null) {
			return topBorder.getStyle();
		}
		return 0;
	}

	/**
	 * Get the bottom border style
	 *
	 * @return Return the bottom border style
	 */
	public int getBottomBorderStyle() {
		if (bottomBorder != null) {
			return bottomBorder.getStyle();
		}
		return 0;
	}

	/**
	 * Set the background color
	 *
	 * @param backgroundColor
	 */
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Set the background image
	 *
	 * @param backgroundImage
	 */
	public void setBackgroundImage(BackgroundImageInfo backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	/**
	 * Set the top border
	 *
	 * @param topBorder
	 */
	public void setTopBorder(BorderInfo topBorder) {
		this.topBorder = topBorder;
	}

	/**
	 * Set the left border
	 *
	 * @param leftBorder
	 */
	public void setLeftBorder(BorderInfo leftBorder) {
		this.leftBorder = leftBorder;
	}

	/**
	 * Set the right border
	 *
	 * @param rightBorder
	 */
	public void setRightBorder(BorderInfo rightBorder) {
		this.rightBorder = rightBorder;
	}

	/**
	 * Set the bottom border
	 *
	 * @param bottomBorder
	 */
	public void setBottomBorder(BorderInfo bottomBorder) {
		this.bottomBorder = bottomBorder;
	}

}
