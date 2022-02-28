/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.layout;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.IReportElementFigure;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.LabelFigure;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.ReportRootFigure;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.draw2d.AbstractHintLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Lays out children in rows or columns, wrapping when the current row/column is
 * filled. The alignment and spacing of rows in the parent can be configured.
 * The alignment and spacing of children within a row can be configured.
 */
/**
 * @author David Michonneau
 *
 */
public class ReportFlowLayout extends AbstractHintLayout {

	/** Constant to specify components to be aligned in the center */
	public static final int ALIGN_CENTER = 0;

	/** Constant to specify components to be aligned on the left/top */
	public static final int ALIGN_LEFTTOP = 1;

	/** Constant to specify components to be aligned on the right/bottom */
	public static final int ALIGN_RIGHTBOTTOM = 2;

	/**
	 * An insets singleton.
	 */
	private static final Insets INSETS_SINGLETON = new Insets();

	/**
	 * The property that determines whether leftover space at the end of a
	 * row/column should be filled by the last item in that row/column.
	 */
	protected boolean fill = false;

	/** The alignment along the major axis. */
	protected int majorAlignment = ALIGN_LEFTTOP;

	/** The alignment along the minor axis. */
	protected int minorAlignment = ALIGN_LEFTTOP;

	/** The spacing along the minor axis. */
	protected int minorSpacing = 5;

	/** The spacing along the major axis. */
	protected int majorSpacing = 5;

	private WorkingData data = null;

	private Hashtable constraints = new Hashtable();

	private String layoutPreference = DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_AUTO_LAYOUT;

	public String getLayoutPreference() {
		return layoutPreference;
	}

	private IFlowLayoutStrategy layoutStrategy = null;

	/**
	 * Holds the necessary information for layout calculations.
	 */
	public static class WorkingData {

		int rowHeight, rowWidth, rowCount, rowX, rowY, maxWidth, rowPos;

		Rectangle bounds[], area;

		Insets margin[];

		IFigure row[];
	}

	/**
	 * Constructs a ReportFlowLayout with horizontal orientation.
	 *
	 * @since 2.0
	 */
	public ReportFlowLayout() {
	}

	/**
	 * Returns the alignment used for an entire row/column.
	 * <P>
	 * Possible values are :
	 * <ul>
	 * <li>{@link #ALIGN_CENTER}
	 * <li>{@link #ALIGN_LEFTTOP}
	 * <li>{@link #ALIGN_RIGHTBOTTOM}
	 * </ul>
	 *
	 * @return the major alignment
	 * @since 2.0
	 */
	public int getMajorAlignment() {
		return majorAlignment;
	}

	/**
	 * Returns the spacing in pixels to be used between children in the direction
	 * parallel to the layout's orientation.
	 *
	 * @return the major spacing
	 */
	public int getMajorSpacing() {
		return majorSpacing;
	}

	/**
	 * Returns the alignment used for children within a row/column.
	 * <P>
	 * Possible values are :
	 * <ul>
	 * <li>{@link #ALIGN_CENTER}
	 * <li>{@link #ALIGN_LEFTTOP}
	 * <li>{@link #ALIGN_RIGHTBOTTOM}
	 * </ul>
	 *
	 * @return the minor alignment
	 * @since 2.0
	 */
	public int getMinorAlignment() {
		return minorAlignment;
	}

	/**
	 * Returns the spacing to be used between children within a row/column.
	 *
	 * @return the minor spacing
	 */
	public int getMinorSpacing() {
		return minorSpacing;
	}

	protected Insets getFigureMargin(IFigure f) {
		ReportItemConstraint constraint = (ReportItemConstraint) getConstraint(f);
		Insets margin = INSETS_SINGLETON;
		if (constraint != null) {
			margin = constraint.getMargin();
		}

		if ((margin == null || margin == INSETS_SINGLETON) && f instanceof IReportElementFigure) {
			margin = ((IReportElementFigure) f).getMargin();
		}

		return margin;
	}

	/**
	 * Initializes the state of row data, which is internal to the layout process.
	 */
	private void initRow() {
		data.rowX = 0;
		data.rowHeight = 0;
		data.rowWidth = 0;
		// data.rowCount = 0;
	}

	/**
	 * Initializes state data for laying out children, based on the Figure given as
	 * input.
	 *
	 * @param parent the parent figure
	 * @since 2.0
	 */
	private void initVariables(IFigure parent) {
		data.row = new IFigure[parent.getChildren().size()];
		data.bounds = new Rectangle[data.row.length];
		data.margin = new Insets[data.row.length];
		data.maxWidth = data.area.width;
	}

	/**
	 * @see org.eclipse.draw2d.LayoutManager#layout(IFigure)
	 */
	@Override
	public void layout(IFigure parent) {
		data = new WorkingData();
		Rectangle relativeArea = parent.getClientArea().getCopy();
		data.area = relativeArea;

		initVariables(parent);
		initRow();

		getLayoutStrategy().layout(parent, data);
		data = null;
	}

	private void layoutVertical(IFigure parent) {
		if (minorAlignment == ALIGN_CENTER || minorAlignment == ALIGN_RIGHTBOTTOM) {
			int minTop = 0;
			int maxBottom = 0;

			for (int i = 0; i < data.bounds.length; i++) {
				if (data.bounds[i].y < minTop) {
					minTop = data.bounds[i].y;
				}
				if (data.bounds[i].y + data.bounds[i].height > maxBottom) {
					maxBottom = data.bounds[i].y + data.bounds[i].height;
				}
			}

			if (maxBottom - minTop < data.area.height) {
				int adjustment = data.area.height - maxBottom + minTop;

				if (minorAlignment == ALIGN_CENTER) {
					adjustment /= 2;
				}

				for (int i = 0; i < data.bounds.length; i++) {
					Rectangle fbounds = data.bounds[i].getCopy().crop(data.margin[i]);
					fbounds.y += adjustment;
					setBoundsOfChild(parent, data.row[i], fbounds);
				}
			} else {
				for (int i = 0; i < data.bounds.length; i++) {
					Rectangle fbounds = data.bounds[i].getCopy().crop(data.margin[i]);
					setBoundsOfChild(parent, data.row[i], fbounds);
				}
			}
		}
	}

	/**
	 * Layouts one row of components. This is done based on the layout's
	 * orientation, minor alignment and major alignment.
	 *
	 * @param parent the parent figure
	 * @since 2.0
	 */
	protected void layoutRow(IFigure parent) {
		int majorAdjustment;
		int minorAdjustment = 0;
		int correctMajorAlignment = majorAlignment;
		// int correctMinorAlignment = minorAlignment;

		majorAdjustment = data.area.width - data.rowWidth + getMinorSpacing();
		if (majorAdjustment < 0) {
			if (parent instanceof ReportRootFigure || !parent.isMirrored()) {
				majorAdjustment = 0;
			}
		} else {
			switch (correctMajorAlignment) {
			case ALIGN_LEFTTOP:
				majorAdjustment = 0;
				break;
			case ALIGN_CENTER:
				majorAdjustment /= 2;
				break;
			case ALIGN_RIGHTBOTTOM:
				break;
			}
		}

		boolean needVerticalAlign = minorAlignment == ALIGN_CENTER || minorAlignment == ALIGN_RIGHTBOTTOM;

		for (int j = data.rowPos; j < data.rowCount; j++) {
			int correctMinorAlignment = getChildVerticalAlign(data.row[j]);

			if (fill) {
				data.bounds[j].height = data.rowHeight;
			} else {
				minorAdjustment = data.rowHeight - data.bounds[j].height;
				switch (correctMinorAlignment) {
				case ALIGN_LEFTTOP:
					minorAdjustment = 0;
					break;
				case ALIGN_CENTER:
					minorAdjustment /= 2;
					break;
				case ALIGN_RIGHTBOTTOM:
					break;
				}

				if (minorAdjustment < 0) {
					minorAdjustment = 0;
				}
				data.bounds[j].y += minorAdjustment;

			}

			data.bounds[j].x += majorAdjustment;

			Rectangle parentArea = parent.getClientArea().getCopy();
//			Rectangle rect =  data.bounds[j].getCopy( );
//			rect.translate( parentArea.x, parentArea.y );
			if (data.rowY < parentArea.y + parentArea.height
					&& data.rowY + data.rowHeight > parentArea.y + parentArea.height) {
				Rectangle rect = data.bounds[j].getCopy();
				rect.translate(parentArea.x, parentArea.y);
				int distanceHeight = rect.y + rect.height - parentArea.y - parentArea.height;
				if (distanceHeight > 0) {
					int topDistnceHeight = rect.y - data.rowY - parentArea.y;
					if (topDistnceHeight > 0) {
						if (distanceHeight > topDistnceHeight) {
							data.bounds[j].y = data.bounds[j].y - topDistnceHeight;
						} else {
							if (correctMinorAlignment == ALIGN_CENTER) {
								data.bounds[j].y = data.bounds[j].y - minorAdjustment
										+ (topDistnceHeight - distanceHeight) / 2;
							} else {
								data.bounds[j].y = (data.bounds[j].y - minorAdjustment + topDistnceHeight
										- distanceHeight);
							}
						}
					}
				} else if (correctMinorAlignment == ALIGN_CENTER) {
					data.bounds[j].y = data.bounds[j].y - minorAdjustment
							+ (parentArea.height - data.rowY - data.bounds[j].height) / 2;
				}

			}

			if (!needVerticalAlign) {
				setBoundsOfChild(parent, data.row[j], data.bounds[j].getCopy().crop(data.margin[j]));
			}
		}

		data.rowPos = data.rowCount;
		data.rowY += getMajorSpacing() + data.rowHeight;
		postLayoutRow(data);
		initRow();
	}

	void postLayoutRow(WorkingData data) {

	}

	/**
	 * Sets the given bounds for the child figure input.
	 *
	 * @param parent the parent figure
	 * @param child  the child figure
	 * @param bounds the size of the child to be set
	 * @since 2.0
	 */
	protected void setBoundsOfChild(IFigure parent, IFigure child, Rectangle bounds) {
		parent.getClientArea(Rectangle.SINGLETON);
		bounds.translate(Rectangle.SINGLETON.x, Rectangle.SINGLETON.y);
		child.setBounds(bounds);
	}

	/**
	 * Sets flag based on layout orientation. If in horizontal orientation, all
	 * figures will have the same height. If in vertical orientation, all figures
	 * will have the same width.
	 *
	 * @param value fill state desired
	 * @since 2.0
	 */
	public void setStretchMinorAxis(boolean value) {
		fill = value;
	}

	/**
	 * Sets the alignment for an entire row/column within the parent figure.
	 * <P>
	 * Possible values are :
	 * <ul>
	 * <li>{@link #ALIGN_CENTER}
	 * <li>{@link #ALIGN_LEFTTOP}
	 * <li>{@link #ALIGN_RIGHTBOTTOM}
	 * </ul>
	 *
	 * @param align the major alignment
	 * @since 2.0
	 */
	public void setMajorAlignment(int align) {
		majorAlignment = align;
	}

	/**
	 * Sets the spacing in pixels to be used between children in the direction
	 * parallel to the layout's orientation.
	 *
	 * @param n the major spacing
	 * @since 2.0
	 */
	public void setMajorSpacing(int n) {
		majorSpacing = n;
	}

	/**
	 * Sets the alignment to be used within a row/column.
	 * <P>
	 * Possible values are :
	 * <ul>
	 * <li>{@link #ALIGN_CENTER}
	 * <li>{@link #ALIGN_LEFTTOP}
	 * <li>{@link #ALIGN_RIGHTBOTTOM}
	 * </ul>
	 *
	 * @param align the minor alignment
	 * @since 2.0
	 */
	public void setMinorAlignment(int align) {
		minorAlignment = align;
	}

	/**
	 * Sets the spacing to be used between children within a row/column.
	 *
	 * @param n the minor spacing
	 * @since 2.0
	 */
	public void setMinorSpacing(int n) {
		minorSpacing = n;
	}

	private int getDisplay(IFigure element) {

		ReportItemConstraint constraint = (ReportItemConstraint) getConstraint(element);
		if (constraint != null) {
			return constraint.getDisplay();
		} else {
			return ReportItemConstraint.BLOCK;
		}
	}

	@Override
	public Object getConstraint(IFigure child) {
		return constraints.get(child);
	}

	/**
	 * Sets the layout constraint of the given figure. The constraints can only be
	 * of type {@link ReportItemConstraint}.
	 *
	 * @see LayoutManager#setConstraint(IFigure, Object)
	 */
	@Override
	public void setConstraint(IFigure figure, Object newConstraint) {
		super.setConstraint(figure, newConstraint);
		if (newConstraint != null) {
			// store the constraint in a HashTable
			constraints.put(figure, newConstraint);

		}
	}

	private void updateChild(IFigure child, int wHint) {
		ReportItemConstraint constraint = (ReportItemConstraint) getConstraint(child);

		if (DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_FIXED_LAYOUT.equals(layoutPreference)
				&& child instanceof IFixLayoutHelper) {
			if (constraint != null) {
				Dimension dimension = constraint.getSize();

				if (dimension.width <= 0) {
					if (constraint.getMeasure() != 0
							&& DesignChoiceConstants.UNITS_PERCENTAGE.equals(constraint.getUnits())) {
						int trueWidth;
						// if (trueWidth <= 0)
						{
							trueWidth = getParentClientArea(child);
						}
						int width = (int) constraint.getMeasure() * trueWidth / 100;
						if (child instanceof LabelFigure) {
							LabelFigure label = (LabelFigure) child;
							Dimension dim = label.getRecommendSize();
							label.setRecommendSize(new Dimension(width, dim.height));
						}
					}
				}
			}
		}
	}

	private int getChildVerticalAlign(IFigure child) {
		ReportItemConstraint constraint = (ReportItemConstraint) getConstraint(child);
		if (constraint == null) {
			return ALIGN_RIGHTBOTTOM;
		}
		return constraint.getAlign();
	}

	private int getParentClientArea(IFigure child) {
		int parentWidth = child.getParent().getClientArea().width;

		Insets fmargin = getFigureMargin(child);

		return Math.max(0, parentWidth - fmargin.getWidth());

	}

	protected Dimension getChildSize(IFigure child, int wHint, int hHint) {
		updateChild(child, wHint);
		ReportItemConstraint constraint = (ReportItemConstraint) getConstraint(child);
		Dimension preferredDimension;
		if (DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_FIXED_LAYOUT.equals(layoutPreference)
				&& child instanceof IFixLayoutHelper) {
			preferredDimension = ((IFixLayoutHelper) child).getFixPreferredSize(wHint, hHint);
			// process UNITS_PERCENTAGE
			if (constraint != null) {
				Dimension dimension = constraint.getSize();

				if (dimension.width <= 0) {
					if (constraint.getMeasure() != 0
							&& DesignChoiceConstants.UNITS_PERCENTAGE.equals(constraint.getUnits())) {
						int trueWidth;
						// if (trueWidth <= 0)
						{
							trueWidth = getParentClientArea(child);
						}
						preferredDimension.width = (int) constraint.getMeasure() * trueWidth / 100;
					}

				}
			}
			constraint = null;
		} else {
			preferredDimension = child.getPreferredSize(wHint, hHint);
		}

		// now support the persent value
		if (constraint != null && !constraint.isFitContiner()) {
			if (constraint.isNone()) {
				// DISPLAY = none, do not display
				return new Dimension(0, 0);
			}

			Dimension dimension = constraint.getSize();

			if (dimension.height <= 0) {
				dimension.height = preferredDimension.height;
			}
			if (dimension.width <= 0) {
				if (constraint.getMeasure() != 0
						&& DesignChoiceConstants.UNITS_PERCENTAGE.equals(constraint.getUnits())) {
					dimension.width = (int) constraint.getMeasure() * wHint / 100;
				} else {
					dimension.width = preferredDimension.width;
				}
			}
			return dimension;
		} else {
			return preferredDimension;
		}
	}

	/**
	 * @see org.eclipse.draw2d.AbstractLayout#calculatePreferredSize(IFigure, int,
	 *      int)
	 */
	@Override
	protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
		// Subtract out the insets from the hints
		if (wHint > -1) {
			wHint = Math.max(0, wHint - container.getInsets().getWidth());
		}
		if (hHint > -1) {
			hHint = Math.max(0, hHint - container.getInsets().getHeight());
		}

		// Figure out the new hint that we are interested in based on the
		// orientation
		// Ignore the other hint (by setting it to -1). NOTE: The children of
		// the
		// parent figure will then be asked to ignore that hint as well.
		int maxWidth;

		maxWidth = wHint;
		hHint = -1;

		if (maxWidth < 0) {
			maxWidth = Integer.MAX_VALUE;
		}

		// The preferred dimension that is to be calculated and returned
		Dimension prefSize = new Dimension();

		List children = container.getChildren();
		int width = 0;
		int height = 0;
		IFigure child;
		IFigure lastChild = null;
		Dimension childSize;
		int caleHint;
		// Build the sizes for each row, and update prefSize accordingly
		for (int i = 0; i < children.size(); i++) {
			child = (IFigure) children.get(i);

			Insets fmargin = getFigureMargin(child);

			if (wHint != -1) {
				wHint = Math.max(0, wHint - fmargin.getWidth());
			}

			// added by gao, if figure is in-line, wHint is -1
			if (getDisplay(child) != ReportItemConstraint.BLOCK) {
				caleHint = -1;
			} else {
				caleHint = wHint;
			}

			childSize = getChildSize(child, caleHint, hHint);

			if (i == 0) {
				width = childSize.width + fmargin.getWidth();
				height = childSize.height + fmargin.getHeight();
			} else if ((getDisplay(child) == ReportItemConstraint.NONE)) {
				// don't display the child
			} else if ((width + childSize.width + fmargin.getWidth() + getMinorSpacing() <= maxWidth)
					&& ((getDisplay(child) == ReportItemConstraint.INLINE)
							&& (getDisplay(lastChild) == ReportItemConstraint.INLINE))) {
				// The current row can fit another child.
				width += childSize.width + fmargin.getWidth() + getMinorSpacing();
				height = Math.max(height, childSize.height + fmargin.getHeight());
			} else {
				// The current row is full or the element is not in-line, start
				// a
				// new row.
				prefSize.height += height + getMajorSpacing();
				prefSize.width = Math.max(prefSize.width, width);
				width = childSize.width + fmargin.getWidth();
				height = childSize.height + fmargin.getHeight();
			}

			lastChild = child;
		}

		// Flush out the last row's data
		prefSize.height += height;
		prefSize.width = Math.max(prefSize.width, width);

		// compensate for the border.
		prefSize.width += container.getInsets().getWidth();
		prefSize.height += container.getInsets().getHeight();
		prefSize.union(getBorderPreferredSize(container));

		return prefSize;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.draw2d.AbstractHintLayout#calculateMinimumSize(org.eclipse.draw2d
	 * .IFigure, int, int)
	 */
	@Override
	protected Dimension calculateMinimumSize(IFigure container, int wHint, int hHint) {
		if (wHint > -1) {
			wHint = Math.max(0, wHint - container.getInsets().getWidth());
		}
		if (hHint > -1) {
			hHint = Math.max(0, hHint - container.getInsets().getHeight());
		}

		// Figure out the new hint that we are interested in based on the
		// orientation
		// Ignore the other hint (by setting it to -1). NOTE: The children of
		// the
		// parent figure will then be asked to ignore that hint as well.
		int maxWidth;

		maxWidth = wHint;
		hHint = -1;

		if (maxWidth < 0) {
			maxWidth = Integer.MAX_VALUE;
		}

		// The preferred dimension that is to be calculated and returned
		Dimension prefSize = new Dimension();

		List children = container.getChildren();
		int width = 0;
		int height = 0;
		IFigure child;
		IFigure lastChild = null;
		Dimension childSize;

		// Build the sizes for each row, and update prefSize accordingly
		for (int i = 0; i < children.size(); i++) {
			child = (IFigure) children.get(i);

			Insets fmargin = getFigureMargin(child);

			if (wHint != -1) {
				wHint = Math.max(0, wHint - fmargin.getWidth());
			}
			if (DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_FIXED_LAYOUT.equals(layoutPreference)
					&& child instanceof IFixLayoutHelper) {
				updateChild(child, wHint);
				int display;
				display = getDisplay(child);
				if (display == ReportItemConstraint.INLINE && child instanceof LabelFigure
						&& (lastChild != null && getDisplay(lastChild) == ReportItemConstraint.INLINE)) {
					childSize = ((IFixLayoutHelper) child).getFixMinimumSize(
							wHint - width - getMinorSpacing() <= 0 ? -1 : wHint - width - getMinorSpacing(), hHint);
					if (childSize.width == wHint - width - getMinorSpacing()) {
						childSize = ((IFixLayoutHelper) child).getFixMinimumSize(-1, hHint);
					}
				} else if (display == ReportItemConstraint.INLINE) {
					childSize = ((IFixLayoutHelper) child).getFixMinimumSize(-1, hHint);
				} else {
					childSize = ((IFixLayoutHelper) child).getFixMinimumSize(wHint, hHint);
				}
			} else {
				childSize = child.getMinimumSize(wHint, hHint);
			}

			if (i == 0) {
				width = childSize.width + fmargin.getWidth();
				height = childSize.height + fmargin.getHeight();
			} else if ((getDisplay(child) == ReportItemConstraint.NONE)) {
				// don't display the child
			} else if ((width + childSize.width + fmargin.getWidth() + getMinorSpacing() <= maxWidth)
					&& ((getDisplay(child) == ReportItemConstraint.INLINE)
							&& (getDisplay(lastChild) == ReportItemConstraint.INLINE))) {
				// The current row can fit another child.
				width += childSize.width + fmargin.getWidth() + getMinorSpacing();
				height = Math.max(height, childSize.height + fmargin.getHeight());
			} else {
				// The current row is full or the element is not in-line, start
				// a
				// new row.
				if (DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_FIXED_LAYOUT.equals(layoutPreference)
						&& child instanceof IFixLayoutHelper) {
					int display;
					display = getDisplay(child);

					if (display == ReportItemConstraint.INLINE) {
						childSize = ((IFixLayoutHelper) child).getFixMinimumSize(-1, hHint);
					} else {
						childSize = ((IFixLayoutHelper) child).getFixMinimumSize(wHint, hHint);
					}

					// height = childSize.height+ fmargin.getHeight( );
				}
				prefSize.height += height + getMajorSpacing();
				prefSize.width = Math.max(prefSize.width, width);
				width = childSize.width + fmargin.getWidth();
				height = childSize.height + fmargin.getHeight();
			}

			lastChild = child;
		}

		// Flush out the last row's data
		prefSize.height += height;
		prefSize.width = Math.max(prefSize.width, width);

		// compensate for the border.
		prefSize.width += container.getInsets().getWidth();
		prefSize.height += container.getInsets().getHeight();
		prefSize.union(getBorderPreferredSize(container));

		return prefSize;
	}

	public void setLayoutPreference(String layoutPreference) {
		this.layoutPreference = layoutPreference;
		layoutStrategy = null;
	}

	protected IFlowLayoutStrategy createFlowLayoutStrategy() {
		if (DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_AUTO_LAYOUT.equals(layoutPreference)) {
			return new AutoLayoutStrategy();
		} else if (DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_FIXED_LAYOUT.equals(layoutPreference)) {
			return new FixLayoutStrategy();
		}
		throw new RuntimeException("Don't support this flow layout style"); //$NON-NLS-1$
	}

	public IFlowLayoutStrategy getLayoutStrategy() {
		if (layoutStrategy == null) {
			layoutStrategy = createFlowLayoutStrategy();
		}
		return layoutStrategy;
	}

	public interface IFlowLayoutStrategy {
		void layout(IFigure figure, WorkingData data);
	}

	private class AutoLayoutStrategy implements IFlowLayoutStrategy {

		@Override
		public void layout(IFigure parent, WorkingData data) {
			Iterator iterator = parent.getChildren().iterator();
			int dx;
			int i = 0;
			int display = ReportItemConstraint.BLOCK;
			int lastDisplay = ReportItemConstraint.BLOCK;

			int wHint = parent.getClientArea().width;
			int hHint = -1;

			while (iterator.hasNext()) {
				IFigure f = (IFigure) iterator.next();

				Insets fmargin = getFigureMargin(f);

				// fix bugzilla 156157
				// always pass hint here, let child process inline/block cases. this
				// is to resovle percentage custom size
				wHint = parent.getClientArea().width;

				if (wHint != -1) {
					wHint = Math.max(0, wHint - fmargin.getWidth());
				}

				Dimension pref = getChildSize(f, wHint, hHint);

				// Hack to allow in-line label wrap.
				if (f instanceof LabelFigure && (pref.width + fmargin.getWidth()) > parent.getClientArea().width) {
					pref = getChildSize(f, Math.max(0, parent.getClientArea().width - fmargin.getWidth()), hHint);
				}

				Rectangle r = new Rectangle(0, 0, pref.width + fmargin.getWidth(), pref.height + fmargin.getHeight());

				display = getDisplay(f);

				if (data.rowCount > data.rowPos) {
					if ((data.rowWidth + r.width > data.maxWidth) || display == ReportItemConstraint.BLOCK
							|| lastDisplay == ReportItemConstraint.BLOCK) {
						layoutRow(parent);
					}
				}
				lastDisplay = display;

				r.x = data.rowX;
				r.y = data.rowY;
				dx = r.width + getMinorSpacing();
				data.rowX += dx;
				data.rowWidth += dx;
				data.rowHeight = Math.max(data.rowHeight, r.height);
				data.row[data.rowCount] = f;
				data.margin[data.rowCount] = fmargin;
				data.bounds[data.rowCount] = r;
				data.rowCount++;
				i++;
			}
			if (data.rowCount > data.rowPos) {
				layoutRow(parent);
			}
			layoutVertical(parent);
		}
	}

	private class FixLayoutStrategy implements IFlowLayoutStrategy {
		@Override
		public void layout(IFigure parent, WorkingData data) {
			Iterator iterator = parent.getChildren().iterator();
			int dx;
			int i = 0;

			int display = ReportItemConstraint.BLOCK;
			int lastDisplay = ReportItemConstraint.BLOCK;
			int hHint = -1;

			while (iterator.hasNext()) {
				int wHint = parent.getClientArea().width;
				IFigure f = (IFigure) iterator.next();

				Insets fmargin = getFigureMargin(f);

				if (wHint != -1) {
					wHint = Math.max(0, wHint - fmargin.getWidth());
				}

				Dimension pref = getChildSize(f, wHint, hHint);
				display = getDisplay(f);
				if (display == ReportItemConstraint.INLINE && f instanceof LabelFigure
						&& lastDisplay == ReportItemConstraint.INLINE) {
					pref = getChildSize(f, wHint - data.rowWidth <= 0 ? -1 : wHint - data.rowWidth, hHint);
					if (pref.width == wHint - data.rowWidth) {
						pref = getChildSize(f, -1, hHint);
					}
				} else if (display == ReportItemConstraint.INLINE)

				{
					pref = getChildSize(f, -1, hHint);
				}

				Rectangle r = new Rectangle(0, 0, pref.width + fmargin.getWidth(), pref.height + fmargin.getHeight());

				if (data.rowCount > data.rowPos) {
					if ((data.rowWidth + r.width > data.maxWidth) || display == ReportItemConstraint.BLOCK
							|| lastDisplay == ReportItemConstraint.BLOCK) {
						layoutRow(parent);
					}
				}
				lastDisplay = display;
				r.x = data.rowX;
				r.y = data.rowY;
				dx = r.width + getMinorSpacing();
				data.rowX += dx;
				data.rowWidth += dx;
				data.rowHeight = Math.max(data.rowHeight, r.height);
				data.row[data.rowCount] = f;
				data.margin[data.rowCount] = fmargin;
				data.bounds[data.rowCount] = r;
				data.rowCount++;
				i++;
			}
			if (data.rowCount > data.rowPos) {
				layoutRow(parent);
			}
			layoutVertical(parent);
		}

	}
}
