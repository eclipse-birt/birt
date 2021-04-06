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

package org.eclipse.birt.report.designer.internal.ui.editors.parts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.AbstractCellEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.AbstractTableEditPart;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;

/**
 * A convenient class for Table Cell selection calculation.
 */

public class TableCellSelectionHelper {

	/**
	 * Gets the minimal rectangle includes all given selection.
	 * 
	 * @param first
	 * @param selection
	 * @return
	 */
	public static Rectangle getSelectionRectangle(AbstractCellEditPart first, List selection) {
		Rectangle rect = new Rectangle();

		rect.x = first.getColumnNumber();
		rect.y = first.getRowNumber();

		int xdir = 0;
		int ydir = 0;

		int dx = rect.x + first.getColSpan() - 1;
		int dy = rect.y + first.getRowSpan() - 1;

		EditPart oparent = first.getParent();

		for (Iterator itr = selection.iterator(); itr.hasNext();) {
			EditPart part = (EditPart) itr.next();

			if (!(part instanceof AbstractCellEditPart) || part.getParent() != oparent) {
				continue;
			}

			AbstractCellEditPart sel = (AbstractCellEditPart) part;

			if (sel == first) {
				continue;
			}

			if (xdir == 0) {
				if (sel.getColumnNumber() > rect.x) {
					xdir = 1;
					dx = sel.getColumnNumber();
				} else if (sel.getColumnNumber() < rect.x) {
					xdir = -1;
					dx = sel.getColumnNumber();
				}
			} else {
				if (xdir > 0) {
					if (sel.getColumnNumber() > dx) {
						dx = sel.getColumnNumber();
					} else if (sel.getColumnNumber() < rect.x) {
						rect.x = sel.getColumnNumber();
					}
				} else if (xdir < 0) {
					if (sel.getColumnNumber() < dx) {
						dx = sel.getColumnNumber();
					} else if (sel.getColumnNumber() > rect.x) {
						rect.x = sel.getColumnNumber();
					}
				}
			}

			if (ydir == 0) {
				if (sel.getRowNumber() > rect.y) {
					ydir = 1;
					dy = sel.getRowNumber();
				} else if (sel.getRowNumber() < rect.y) {
					ydir = -1;
					dy = sel.getRowNumber();
				}
			} else {
				if (ydir > 0) {
					if (sel.getRowNumber() > dy) {
						dy = sel.getRowNumber();
					} else if (sel.getRowNumber() < rect.y) {
						rect.y = sel.getRowNumber();
					}
				} else if (ydir < 0) {
					if (sel.getRowNumber() < dy) {
						dy = sel.getRowNumber();
					} else if (sel.getRowNumber() > rect.y) {
						rect.y = sel.getRowNumber();
					}
				}
			}

		}

		rect.width = dx - rect.x;
		rect.height = dy - rect.y;

		return rect;
	}

	/**
	 * Gets all cells within the Rectangle.
	 * 
	 * @param rect
	 * @param table
	 * @return
	 */
	public static List getRectangleSelection(Rectangle rect, AbstractTableEditPart table) {
		ArrayList al = new ArrayList();

		int xstart = Math.min(rect.x, rect.x + rect.width);
		int xend = Math.max(rect.x, rect.x + rect.width);

		xstart = Math.max(1, xstart);
		xend = Math.min(table.getColumnCount(), xend);

		int ystart = Math.min(rect.y, rect.y + rect.height);
		int yend = Math.max(rect.y, rect.y + rect.height);

		ystart = Math.max(1, ystart);
		yend = Math.min(table.getRowCount(), yend);

		for (int i = xstart; i <= xend; i++) {
			for (int j = ystart; j <= yend; j++) {
				AbstractCellEditPart cell = table.getCell(j, i);
				if (!al.contains(cell)) {
					al.add(cell);
				}
			}
		}

		return al;
	}

	/**
	 * Checks every cell in the Rectangle and increases the selection.
	 * 
	 * @param rect
	 * @param table
	 * @return
	 */
	public static boolean increaseSelectionRectangle(Rectangle rect, AbstractTableEditPart table) {
		Rectangle nRect = rect.getCopy();

		boolean refined = false;

		if (rect.width < 0) {
			for (int i = rect.x + rect.width; i <= rect.x; i++) {
				if (rect.height < 0) {
					for (int j = rect.y + rect.height; j <= rect.y; j++) {
						boolean rlt = checkAndIncreaseCellRectangle(table, j, i, nRect);

						if (!refined) {
							refined = rlt;
						}
					}
				} else {
					for (int j = rect.y; j <= rect.y + rect.height; j++) {
						boolean rlt = checkAndIncreaseCellRectangle(table, j, i, nRect);

						if (!refined) {
							refined = rlt;
						}
					}
				}
			}
		} else {
			for (int i = rect.x; i <= rect.x + rect.width; i++) {
				if (rect.height < 0) {
					for (int j = rect.y + rect.height; j <= rect.y; j++) {
						boolean rlt = checkAndIncreaseCellRectangle(table, j, i, nRect);

						if (!refined) {
							refined = rlt;
						}
					}
				} else {
					for (int j = rect.y; j <= rect.y + rect.height; j++) {
						boolean rlt = checkAndIncreaseCellRectangle(table, j, i, nRect);

						if (!refined) {
							refined = rlt;
						}
					}
				}
			}
		}

		rect.x = nRect.x;
		rect.y = nRect.y;
		rect.width = nRect.width;
		rect.height = nRect.height;

		return refined;
	}

	/**
	 * Checks and increases the rectangle for specified Cell.
	 * 
	 * @param table
	 * @param rowNumber
	 * @param columnNumber
	 * @param constraint
	 * @return
	 */
	private static boolean checkAndIncreaseCellRectangle(AbstractTableEditPart table, int rowNumber, int columnNumber,
			Rectangle constraint) {
		boolean refined = false;
		Point pt;

		/**
		 * Checks the row, column number.
		 */
		if (rowNumber < 1 || rowNumber > table.getRowCount() || columnNumber < 1
				|| columnNumber > table.getColumnCount()) {
			return false;
		}

		AbstractCellEditPart cell = table.getCell(rowNumber, columnNumber);

		/**
		 * Extracts one diagonal point.
		 */
		pt = new Point(cell.getColumnNumber() + cell.getColSpan() - 1, cell.getRowNumber());

		if (pt.x < 1) {
			pt.x = 1;
		}

		if (pt.x > table.getColumnCount()) {
			pt.x = table.getColumnCount();
		}

		/**
		 * Checks the point, resize the rectangle to include it.
		 */
		refined = incResizeRectangle(constraint, pt);

		/**
		 * Extracts another diagonal point.
		 */
		pt = new Point(cell.getColumnNumber(), cell.getRowNumber() + cell.getRowSpan() - 1);

		if (pt.y < 1) {
			pt.y = 1;
		}

		if (pt.y > table.getRowCount()) {
			pt.y = table.getRowCount();
		}

		/**
		 * Checks the second point, resize the rectangle to include it.
		 */
		boolean rlt = incResizeRectangle(constraint, pt);

		if (!refined) {
			refined = rlt;
		}

		return refined;
	}

	/**
	 * Resizes the rectangle to include the given point.
	 * 
	 * @param rect
	 * @param pt
	 * @return
	 */
	private static boolean incResizeRectangle(Rectangle rect, Point pt) {
		boolean resized = false;

		if (!isRectContains(rect, pt)) {
			if (rect.width < 0) {
				if (pt.x < rect.x + rect.width) {
					rect.width = pt.x - rect.x;

					resized = true;
				} else if (pt.x > rect.x) {
					rect.width -= pt.x - rect.x;
					rect.x = pt.x;

					resized = true;
				}
			} else {
				if (pt.x < rect.x) {
					rect.width += rect.x - pt.x;
					rect.x = pt.x;

					resized = true;
				} else if (pt.x > rect.x + rect.width) {
					rect.width = pt.x - rect.x;

					resized = true;
				}
			}

			if (rect.height < 0) {
				if (pt.y < rect.y + rect.height) {
					rect.height = pt.y - rect.y;

					resized = true;
				} else if (pt.y > rect.y) {
					rect.height -= pt.y - rect.y;
					rect.y = pt.y;

					resized = true;
				}
			} else {
				if (pt.y < rect.y) {
					rect.height += rect.y - pt.y;
					rect.y = pt.y;

					resized = true;
				} else if (pt.y > rect.y + rect.height) {
					rect.height = pt.y - rect.y;

					resized = true;
				}
			}
		}

		return resized;
	}

	/**
	 * Checks every cell in the Rectangle and decreases the selection.
	 * 
	 * @param rect
	 * @param table
	 * @param direction
	 * @return
	 */
	public static boolean decreaseSelectionRectangle(Rectangle rect, AbstractTableEditPart table, int direction) {
		Rectangle nRect = rect.getCopy();

		boolean decreased = false;

		if (rect.width < 0) {
			for (int i = rect.x + rect.width; i <= rect.x; i++) {
				if (rect.height < 0) {
					for (int j = rect.y + rect.height; j <= rect.y; j++) {
						boolean rlt = checkAndDecreaseCellRectangle(table, j, i, nRect, direction);

						if (!decreased) {
							decreased = rlt;
						}
					}
				} else {
					for (int j = rect.y; j <= rect.y + rect.height; j++) {
						boolean rlt = checkAndDecreaseCellRectangle(table, j, i, nRect, direction);

						if (!decreased) {
							decreased = rlt;
						}
					}
				}
			}
		} else {
			for (int i = rect.x; i <= rect.x + rect.width; i++) {
				if (rect.height < 0) {
					for (int j = rect.y + rect.height; j <= rect.y; j++) {
						boolean rlt = checkAndDecreaseCellRectangle(table, j, i, nRect, direction);

						if (!decreased) {
							decreased = rlt;
						}
					}
				} else {
					for (int j = rect.y; j <= rect.y + rect.height; j++) {
						boolean rlt = checkAndDecreaseCellRectangle(table, j, i, nRect, direction);

						if (!decreased) {
							decreased = rlt;
						}
					}
				}
			}
		}

		rect.x = nRect.x;
		rect.y = nRect.y;
		rect.width = nRect.width;
		rect.height = nRect.height;

		return decreased;
	}

	/**
	 * Checks and decreases the rectangle for specified Cell.
	 * 
	 * @param table
	 * @param rowNumber
	 * @param columnNumber
	 * @param constraint
	 * @param direction
	 * @return
	 */
	private static boolean checkAndDecreaseCellRectangle(AbstractTableEditPart table, int rowNumber, int columnNumber,
			Rectangle constraint, int direction) {
		Point pt1, pt2;

		/**
		 * Checks the row, column number.
		 */
		if (rowNumber < 1 || rowNumber > table.getRowCount() || columnNumber < 1
				|| columnNumber > table.getColumnCount()) {
			return false;
		}

		AbstractCellEditPart cell = table.getCell(rowNumber, columnNumber);

		/**
		 * Extracts the two diagonal point of the Cell.
		 */

		pt1 = new Point(cell.getColumnNumber() + cell.getColSpan() - 1, cell.getRowNumber());

		if (pt1.x < 1) {
			pt1.x = 1;
		}

		if (pt1.x > table.getColumnCount()) {
			pt1.x = table.getColumnCount();
		}

		pt2 = new Point(cell.getColumnNumber(), cell.getRowNumber() + cell.getRowSpan() - 1);

		if (pt2.y < 1) {
			pt2.y = 1;
		}

		if (pt2.y > table.getRowCount()) {
			pt2.y = table.getRowCount();
		}

		/**
		 * Checks the two points, excludes the area they occupy from the rectangle.
		 */
		return decResizeRectangle(constraint, pt1, pt2, direction);
	}

	/**
	 * Resizes the rectangle to exclude the given two points.
	 * 
	 * @param rect
	 * @param pt1
	 * @param pt2
	 * @param direction
	 * @return
	 */
	private static boolean decResizeRectangle(Rectangle rect, Point pt1, Point pt2, int direction) {
		boolean resized = false;

		boolean con1 = isRectContains(rect, pt1);
		boolean con2 = isRectContains(rect, pt2);

		/**
		 * Checks the two points, they must satisfy that one is inside the rectangle,
		 * and the other is outside the rectangle.
		 */
		if (con1 ^ con2) {
			/**
			 * Exchanges the point data if necessary, make pt1 always being within the
			 * rectangle.
			 */
			if (con2) {
				Point tmp = pt1.getCopy();

				pt1 = pt2;
				pt2 = tmp;
			}

			/**
			 * If it is a horizontal movement, we adjust the width first, then height, else
			 * we adjust the height first, then width.
			 */
			if (direction == PositionConstants.WEST || direction == PositionConstants.EAST) {
				boolean rlt = decAdjustWidth(rect, pt1, pt2);

				if (rlt) {
					resized = true;
				}

				con1 = isRectContains(rect, pt1);
				con2 = isRectContains(rect, pt2);

				/**
				 * Rechecks the relationship between the two points and the adjusted rectangle.
				 */
				if (con1 ^ con2) {
					rlt = decAdjustHeight(rect, pt1, pt2);

					if (rlt) {
						resized = true;
					}
				}
			} else {
				boolean rlt = decAdjustHeight(rect, pt1, pt2);

				if (rlt) {
					resized = true;
				}

				con1 = isRectContains(rect, pt1);
				con2 = isRectContains(rect, pt2);

				if (con1 ^ con2) {
					rlt = decAdjustWidth(rect, pt1, pt2);

					if (rlt) {
						resized = true;
					}
				}
			}

		}

		return resized;
	}

	/**
	 * Adjusts the rectangle's width to exclude the two points.
	 * 
	 * @param rect
	 * @param pt1
	 * @param pt2
	 * @return
	 */
	private static boolean decAdjustWidth(Rectangle rect, Point pt1, Point pt2) {
		boolean resized = false;

		if (rect.width < 0) {
			if (pt1.x < pt2.x) {
				rect.width += rect.x - pt1.x + 1;
				rect.x = pt1.x - 1;

				resized = true;
			} else if (pt1.x > pt2.x) {
				rect.width = pt1.x - rect.x + 1;

				resized = true;
			}
		} else if (rect.width > 0) {
			if (pt1.x < pt2.x) {
				rect.width = pt1.x - rect.x - 1;

				resized = true;
			} else if (pt1.x > pt2.x) {
				rect.width -= pt1.x - rect.x + 1;

				rect.x = pt1.x + 1;

				resized = true;
			}
		}

		return resized;
	}

	/**
	 * Adjusts the rectangle's height to exclude the two points.
	 * 
	 * @param rect
	 * @param pt1
	 * @param pt2
	 * @return
	 */
	private static boolean decAdjustHeight(Rectangle rect, Point pt1, Point pt2) {
		boolean resized = false;

		if (rect.height < 0) {
			if (pt1.y < pt2.y) {
				rect.height += rect.y - pt1.y + 1;
				rect.y = pt1.y - 1;

				resized = true;
			} else if (pt1.y > pt2.y) {
				rect.height = pt1.y - rect.y + 1;

				resized = true;
			}
		} else if (rect.height > 0) {
			if (pt1.y < pt2.y) {
				rect.height = pt1.y - rect.y - 1;

				resized = true;
			} else if (pt1.y > pt2.y) {
				rect.height -= pt1.y - rect.y + 1;

				rect.y = pt1.y + 1;

				resized = true;
			}
		}

		return resized;
	}

	/**
	 * Checks if the given point is in the given rectangle.
	 * 
	 * @param rect
	 * @param pt
	 * @return
	 */
	private static boolean isRectContains(Rectangle rect, Point pt) {
		if (rect.width < 0) {
			if (pt.x < rect.x + rect.width || pt.x > rect.x) {
				return false;
			}
		} else {
			if (pt.x < rect.x || pt.x > rect.x + rect.width) {
				return false;
			}
		}

		if (rect.height < 0) {
			if (pt.y < rect.y + rect.height || pt.y > rect.y) {
				return false;
			}
		} else {
			if (pt.y < rect.y || pt.y > rect.y + rect.height) {
				return false;
			}
		}

		return true;
	}

}