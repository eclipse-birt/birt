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

package org.eclipse.birt.report.designer.util;

/**
 * This class implemented the table border collision algorithm according to the
 * CSS2.0 specification.
 */

public class TableBorderCollisionArbiter {

	/**
	 * @param data
	 * @param style
	 * @param width
	 * @param color
	 * @param rowIndex
	 * @param colIndex
	 */
	public static void refreshBorderData(int[] data, int style, int width, int color, int rowIndex, int colIndex) {
		refreshBorderData(data, style, width, color, rowIndex, colIndex, 0);
	}

	/**
	 * Refresh the border data as per the current border setting and existing border
	 * setting, using the CSS2.0 border collision algorithm: if the style is none,
	 * always lose; then compare the width, greater win; if width is equal, compare
	 * the style, less win; or keep the original. This is the part 1 of the
	 * algorithm.
	 */
	public static void refreshBorderData(int[] data, int style, int width, int color, int rowIndex, int colIndex,
			int type) {
		assert (data.length > 5);

		if (style != 0) {
			if (data[0] == 0) {
				data[0] = style;
				data[1] = width;
				data[2] = color;
				data[3] = rowIndex;
				data[4] = colIndex;
				data[5] = type;
				return;
			}
			if (type > data[5]) {
				data[0] = style;
				data[1] = width;
				data[2] = color;
				data[3] = rowIndex;
				data[4] = colIndex;
				data[5] = type;
			} else if (type < data[5]) {
				return;
			}
			if (width > data[1] || data[0] == 0) {
				data[0] = style;
				data[1] = width;
				data[2] = color;
				data[3] = rowIndex;
				data[4] = colIndex;
				data[5] = type;
			} else if (width == data[1] && style < data[0]) {
				data[0] = style;
				data[2] = color;
				data[3] = rowIndex;
				data[4] = colIndex;
				data[5] = type;
			}
		} else if (data[0] == 0) {
			// this is the default line width. set to distinguish from those
			// never set.
			data[1] = 1;
		}
	}

	/**
	 * This method implemented the border collision algorithm in the cross point
	 * during actual drawing according to the CSS2.0 specification. This is the part
	 * 2 of the algorithm.
	 */
	public static boolean canExtend(int[] data, int extNBWidth, int extNBStyle, int leftHandWidth, int leftHandStyle,
			int leftHandX, int leftHandY, int rightHandWidth, int rightHandStyle, int rightHandX, int rightHandY,
			boolean isHead, boolean isVertical) {
		int maxHandWidth = Math.max(leftHandWidth, rightHandWidth);
		int maxNBWidth = Math.max(maxHandWidth, extNBWidth);

		boolean isLeftBrother = data[3] == leftHandX && data[4] == leftHandY;
		boolean isRightBrother = data[3] == rightHandX && data[4] == rightHandY;

		/**
		 * In this case, current width is greater than any of other three edges, should
		 * extend.
		 */
		if (data[1] > maxNBWidth) {
			return true;
		}

		/**
		 * In this case, current width is equal to one neighbour edge and greater than
		 * two neighbour edge.
		 */
		if (data[1] == extNBWidth && data[1] > maxHandWidth) {
			// if the equal edge style is NONE, if current style is not NONE or
			// is header, win.
			if (extNBStyle == 0) {
				return (data[0] != 0) || isHead;
			} else if (data[0] == 0) {
				return false;
			}

			// if the style is less than equal edge style, win.
			if (data[0] < extNBStyle) {
				return true;
			}

			// if style is equal, but is header, win.
			return (data[0] == extNBStyle) && isHead;
		}

		/**
		 * In this case, current width is equal to one neighbour edge and greater than
		 * two neighbour edge.
		 */
		if (data[1] > extNBWidth && data[1] > leftHandWidth && data[1] == rightHandWidth) {
			// If the equal edge is a brother edge, happy extend.
			if (isRightBrother) {
				return true;
			}

			// if the equal edge style is NONE, if current style is not NONE
			// or is header, win.
			if (rightHandStyle == 0) {
				return (data[0] != 0) || isHead;
			} else if (data[0] == 0) {
				return false;
			}

			// if the style is less than equal edge style, win.
			if (data[0] < rightHandStyle) {
				return true;
			}

			// if style is equal, but is header, win.
			return (data[0] == rightHandStyle) && isHead;
		}

		/**
		 * In this case, current width is equal to one neighbour edge and greater than
		 * two neighbour edge.
		 */
		if (data[1] > extNBWidth && data[1] > rightHandWidth && data[1] == leftHandWidth) {
			// If the equal edge is a brother edge, happy extend.
			if (isLeftBrother) {
				return true;
			}

			// if the equal edge style is NONE, if current style is not NONE or
			// is header and is vertical, win.
			if (leftHandStyle == 0) {
				return (data[0] != 0) || (isHead && isVertical);
			} else if (data[0] == 0) {
				return false;
			}

			// if the style is less than equal edge style, win.
			if (data[0] < leftHandStyle) {
				return true;
			}

			// if style is equal, but is header and is vertical, win.
			return (data[0] == leftHandStyle) && isHead && isVertical;
		}

		/**
		 * In this case, current width is equal to two neighbour edge and greater than
		 * one neighbour edge.
		 */
		if (data[1] > extNBWidth && data[1] == maxHandWidth) {
			// if the equal edges' styles are NONE, if current style is not NONE
			// or is header and is vertical, win.
			if (leftHandStyle == 0 && rightHandStyle == 0) {
				return (data[0] != 0) || (isHead && isVertical);
			} else if (leftHandStyle != 0 && rightHandStyle == 0) {
				// if one equal edge style is not NONE, and current styule is
				// NONE or greater than it, lose.
				if (data[0] == 0 || data[0] > leftHandStyle) {
					return false;
				}

				// if current style is less than the equal edge style, or equal
				// to it and self is header and is vertical or it's a brother,
				// win.
				return (data[0] < leftHandStyle)
						|| (data[0] == leftHandStyle && ((isHead && isVertical) || isLeftBrother));
			} else if (leftHandStyle == 0 && rightHandStyle != 0) {
				// if one equal edge style is not NONE, and current styule is
				// NONE or greater than it, lose.
				if (data[0] == 0 || data[0] > rightHandStyle) {
					return false;
				}

				// if current style is less than the equal edge style, or equal
				// to it and self is header or it's a brother, win.
				return (data[0] < rightHandStyle) || (data[0] == rightHandStyle && (isHead || isRightBrother));
			} else if (data[0] == 0) {
				return false;
			}

			if (data[0] < leftHandStyle && data[0] < rightHandStyle) {
				return true;
			}

			if (data[0] == leftHandStyle && data[0] < rightHandStyle) {
				return (isHead && isVertical) || isLeftBrother;
			}

			if (data[0] < leftHandStyle && data[0] == rightHandStyle) {
				return isHead || isRightBrother;
			}

			return data[0] == leftHandStyle && data[0] == rightHandStyle && isHead && isVertical;
		}

		/**
		 * In this case, current width is equal to two neighbour edge and greater than
		 * one neighbour edge.
		 */
		if (data[1] > leftHandWidth && data[1] == rightHandWidth && data[1] == extNBWidth) {
			// if the equal edges' styles are NONE, if current style is not NONE
			// or is header, win.
			if (extNBStyle == 0 && rightHandStyle == 0) {
				return (data[0] != 0) || isHead;
			} else if (extNBStyle != 0 && rightHandStyle == 0) {
				if (data[0] == 0 || data[0] > extNBStyle) {
					return false;
				}

				return (data[0] < extNBStyle) || (data[0] == extNBStyle && isHead);
			} else if (extNBStyle == 0 && rightHandStyle != 0) {
				if (data[0] == 0 || data[0] > rightHandStyle) {
					return false;
				}

				return (data[0] < rightHandStyle) || (data[0] == rightHandStyle && (isHead || isRightBrother));
			} else if (data[0] == 0) {
				return false;
			}

			if (data[0] < extNBStyle && data[0] < rightHandStyle) {
				return true;
			}

			if (data[0] == extNBStyle && data[0] < rightHandStyle) {
				return isHead;
			}

			if (data[0] < extNBStyle && data[0] == rightHandStyle) {
				return isHead || isRightBrother;
			}

			return data[0] == extNBStyle && data[0] == rightHandStyle && isHead;
		}

		/**
		 * In this case, current width is equal to two neighbour edge and greater than
		 * one neighbour edge.
		 */
		if (data[1] > rightHandWidth && data[1] == leftHandWidth && data[1] == extNBWidth) {
			// if the equal edges' styles are NONE, if current style is not NONE
			// or is header, win.
			if (extNBStyle == 0 && leftHandStyle == 0) {
				return (data[0] != 0) || (isHead && isVertical);
			} else if (extNBStyle != 0 && leftHandStyle == 0) {
				if (data[0] == 0 || data[0] > extNBStyle) {
					return false;
				}

				return (data[0] < extNBStyle) || (data[0] == extNBStyle && isHead);
			} else if (extNBStyle == 0 && leftHandStyle != 0) {
				if (data[0] == 0 || data[0] > leftHandStyle) {
					return false;
				}

				return (data[0] < leftHandStyle)
						|| (data[0] == leftHandStyle && ((isHead && isVertical) || isLeftBrother));
			} else if (data[0] == 0) {
				return false;
			}

			if (data[0] < extNBStyle && data[0] < leftHandStyle) {
				return true;
			}

			if (data[0] == extNBStyle && data[0] < leftHandStyle) {
				return isHead;
			}

			if (data[0] < extNBStyle && data[0] == leftHandStyle) {
				return (isHead && isVertical) || isLeftBrother;
			}

			return data[0] == extNBStyle && data[0] == leftHandStyle && isHead && isVertical;
		}

		/**
		 * In this case, current width is equal to all three neighbour edges.
		 */
		if (data[1] == extNBWidth && data[1] == maxHandWidth) {
			// if the equal edges' styles are NONE, if current style is not NONE
			// or is header and is vertical, win.
			if (extNBStyle == 0 && leftHandStyle == 0 && rightHandStyle == 0) {
				return (data[0] != 0) || (isHead && isVertical);
			} else if (extNBStyle != 0 && leftHandStyle == 0 && rightHandStyle == 0) {
				if (data[0] == 0 || data[0] > extNBStyle) {
					return false;
				}

				return (data[0] < extNBStyle) || (data[0] == extNBStyle && isHead);
			} else if (extNBStyle == 0 && leftHandStyle != 0 && rightHandStyle == 0) {
				if (data[0] == 0 || data[0] > leftHandStyle) {
					return false;
				}

				return (data[0] < leftHandStyle)
						|| (data[0] == leftHandStyle && ((isHead && isVertical) || isLeftBrother));
			} else if (extNBStyle == 0 && leftHandStyle == 0 && rightHandStyle != 0) {
				if (data[0] == 0 || data[0] > rightHandStyle) {
					return false;
				}

				return (data[0] < rightHandStyle) || (data[0] == rightHandStyle && (isHead || isRightBrother));
			} else if (extNBStyle != 0 && leftHandStyle != 0 && rightHandStyle == 0) {
				if (data[0] == 0 || data[0] > extNBStyle || data[0] > leftHandStyle) {
					return false;
				}

				if (data[0] == extNBStyle && data[0] == leftHandStyle) {
					return isHead && isVertical;
				}

				if (data[0] < extNBStyle && data[0] == leftHandStyle) {
					return (isHead && isVertical) || isLeftBrother;
				}

				if (data[0] == extNBStyle && data[0] < leftHandStyle) {
					return isHead;
				}

				return (data[0] < extNBStyle) && (data[0] < leftHandStyle);
			} else if (extNBStyle == 0 && leftHandStyle != 0 && rightHandStyle != 0) {
				if (data[0] == 0 || data[0] > leftHandStyle || data[0] > rightHandStyle) {
					return false;
				}

				if (data[0] == leftHandStyle && data[0] == rightHandStyle) {
					return isHead && isVertical;
				}

				if (data[0] < leftHandStyle && data[0] == rightHandStyle) {
					return isHead || isRightBrother;
				}

				if (data[0] == leftHandStyle && data[0] < rightHandStyle) {
					return (isHead && isVertical) || isLeftBrother;
				}

				return (data[0] < leftHandStyle) && (data[0] < rightHandStyle);

			} else if (extNBStyle != 0 && leftHandStyle == 0 && rightHandStyle != 0) {
				if (data[0] == 0 || data[0] > extNBStyle || data[0] > rightHandStyle) {
					return false;
				}

				if (data[0] == extNBStyle && data[0] == rightHandStyle) {
					return isHead;
				}

				if (data[0] < extNBStyle && data[0] == rightHandStyle) {
					return isHead || isRightBrother;
				}

				if (data[0] == extNBStyle && data[0] < rightHandStyle) {
					return isHead;
				}

				return (data[0] < extNBStyle) && (data[0] < rightHandStyle);

			} else if (data[0] == 0) {
				return false;
			}

			// if the current style is less than all three other neighbour
			// style, win.
			if (data[0] < extNBStyle && data[0] < leftHandStyle && data[0] < rightHandStyle) {
				return true;
			}

			if (data[0] == extNBStyle && data[0] < leftHandStyle && data[0] < rightHandStyle) {
				return isHead;
			}

			if (data[0] < extNBStyle && data[0] == leftHandStyle && data[0] < rightHandStyle) {
				return (isHead && isVertical) || isLeftBrother;
			}

			if (data[0] < extNBStyle && data[0] < leftHandStyle && data[0] == rightHandStyle) {
				return isHead || isRightBrother;
			}

			if (data[0] < extNBStyle && data[0] == leftHandStyle && data[0] == rightHandStyle) {
				return isHead && isVertical;
			}

			if (data[0] == extNBStyle && data[0] == leftHandStyle && data[0] < rightHandStyle) {
				return isHead && isVertical;
			}

			if (data[0] == extNBStyle && data[0] < leftHandStyle && data[0] == rightHandStyle) {
				return isHead;
			}

			return data[0] == extNBStyle && data[0] == leftHandStyle && data[0] == rightHandStyle && isHead
					&& isVertical;
		}

		/**
		 * In other case, always lose.
		 */
		return false;
	}

	/**
	 * This is the part 3 of the border collision algorithm according to the CSS2.0
	 * specification. This part is used to fix some side-situation.
	 */
	public static boolean isBrotherWin(int[] data, int extNBWidth, int extNBStyle, int extNBX, int extNBY,
			int leftHandWidth, int leftHandStyle, int leftHandX, int leftHandY, int rightHandWidth, int rightHandStyle,
			int rightHandX, int rightHandY, boolean isHead, boolean isVertical) {
		// if left hand is brother.
		if (data[3] == leftHandX && data[4] == leftHandY) {
			return canExtend(new int[] { leftHandStyle, leftHandWidth, 0, 0, 0 }, rightHandWidth, rightHandStyle,
					extNBWidth, extNBStyle, extNBX, extNBY, data[1], data[0], data[3], data[4], isVertical ^ isHead,
					!isVertical);
		}

		// if right hand is brother.
		if (data[3] == rightHandX && data[4] == rightHandY) {
			return canExtend(new int[] { rightHandStyle, rightHandWidth, 0, 0, 0 }, leftHandWidth, leftHandStyle,
					data[1], data[0], data[3], data[4], extNBWidth, extNBStyle, extNBX, extNBY, !(isVertical ^ isHead),
					!isVertical);
		}

		// if no brother.
		return false;
	}

}
