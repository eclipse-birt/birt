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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.tools;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.rulers.EditorRulerComposite;
import org.eclipse.birt.report.designer.internal.ui.editors.rulers.EditorRulerComposite.DragGuideInfo;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.AbstractTableEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableUtil;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.TableDragGuideTracker;
import org.eclipse.birt.report.designer.internal.ui.layout.ITableLayoutOwner;
import org.eclipse.birt.report.designer.internal.ui.layout.TableLayout.WorkingData;
import org.eclipse.birt.report.designer.internal.ui.layout.TableLayoutData.ColumnData;
import org.eclipse.birt.report.designer.util.MetricUtility;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.CrosstabTableEditPart;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.CrosstabTableUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabHandleAdapter;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.swt.graphics.Cursor;

/**
 * Drag the cross cell right border to adjust the coumn width
 */
public class CrosstabColumnDragTracker extends TableDragGuideTracker {
	private static final String RESIZE_COLUMN_TRANS_LABEL = Messages
			.getString("CrosstabColumnDragTracker.ResizeColumn");
	private static final String PREFIX_LABEL = Messages.getString("CrosstabColumnDragTracker.Show.Label");

	/**
	 * Constructor
	 *
	 * @param sourceEditPart
	 * @param start
	 * @param end
	 */
	public CrosstabColumnDragTracker(EditPart sourceEditPart, int start, int end) {
		super(sourceEditPart, start, end);
		setDisabledCursor(Cursors.SIZEWE);
	}

	@Override
	protected Cursor getDefaultCursor() {
		if (isCloneActive()) {
			return Cursors.SIZEWE;
		}
		return super.getDefaultCursor();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.
	 * TableDragGuideTracker#getDragWidth()
	 */
	@Override
	protected Dimension getDragWidth(int start, int end) {
//		return new Dimension( TableUtil.getMinWidth( getCrosstabTableEditPart( ),
//				getStart( ) )
//				- CrosstabTableUtil.caleVisualWidth( getCrosstabTableEditPart( ),
//						getStart( ) ),
//				Integer.MAX_VALUE );
		if (isCtrlDown()) {
			return new Dimension(
					TableUtil.getMinWidth(getCrosstabTableEditPart(), getStart())
							- CrosstabTableUtil.caleVisualWidth(getCrosstabTableEditPart(), getStart()),
					Integer.MAX_VALUE);
		} else {
			if (getStart() == getEnd()) {
				return new Dimension(
						TableUtil.getMinWidth(getCrosstabTableEditPart(), getStart())
								- CrosstabTableUtil.caleVisualWidth(getCrosstabTableEditPart(), getStart()),
						Integer.MAX_VALUE);
			}

			return new Dimension(
					TableUtil.getMinWidth(getCrosstabTableEditPart(), getStart())
							- CrosstabTableUtil.caleVisualWidth(getCrosstabTableEditPart(), getStart()),
					CrosstabTableUtil.caleVisualWidth(getCrosstabTableEditPart(), getEnd())
							- TableUtil.getMinWidth(getCrosstabTableEditPart(), getEnd()));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.
	 * TableDragGuideTracker#getMarqueeSelectionRectangle()
	 */
	@Override
	protected Rectangle getMarqueeSelectionRectangle() {
		IFigure figure = getCrosstabTableEditPart().getFigure();
		Insets insets = figure.getInsets();

		int value = getLocation().x - getStartLocation().x;
		value = getTrueValueAbsolute(value);

		Point p = getStartLocation().getCopy();
		figure.translateToAbsolute(p);
		figure.translateToRelative(p);
		Rectangle bounds = figure.getBounds().getCopy();
		figure.translateToAbsolute(bounds);

		return new Rectangle(value + p.x, bounds.y + insets.top, 2, bounds.height - (insets.top + insets.bottom));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.
	 * TableDragGuideTracker#resize()
	 */
	@Override
	protected void resize() {
		CrosstabTableEditPart part = (CrosstabTableEditPart) getSourceEditPart().getParent();
		int value = getMouseTrueValueX();
		part.getCrosstabHandleAdapter().getCrosstabItemHandle().getModuleHandle().getCommandStack()
				.startTrans(RESIZE_COLUMN_TRANS_LABEL);
		if (getStart() != getEnd()) {
			value = getTrueValue(value);
			resizeColumn(getStart(), getEnd(), value);
		} else {
			/**
			 * This is the Last Column, resize the whole table.
			 */
			Dimension dimension = getDragWidth();

			if (value < dimension.width) {
				value = dimension.width;
			}

			int with = calculateWidth() + value;

			int startWidth;

			startWidth = CrosstabTableUtil.caleVisualWidth(part, getStart());

			part.getCrosstabHandleAdapter().setWidth(with);

			part.getCrosstabHandleAdapter().setColumnWidth(getStart(), startWidth + value);

		}

		part.getCrosstabHandleAdapter().getCrosstabItemHandle().getModuleHandle().getCommandStack().commit();
	}

	/**
	 * Calculates table layout size. For table supports auto layout, the layout size
	 * need to be calculated when drawing.
	 *
	 * @return
	 */
	private int calculateWidth() {
		CrosstabTableEditPart part = (CrosstabTableEditPart) getSourceEditPart().getParent();

		int columnCount = part.getColumnCount();
		int samColumnWidth = 0;
		for (int i = 0; i < columnCount; i++) {
			samColumnWidth = samColumnWidth + CrosstabTableUtil.caleVisualWidth(part, i + 1);
		}

		return samColumnWidth;
	}

	private void resizeFixColumn(int value, int start, int end) {
		CrosstabTableEditPart part = (CrosstabTableEditPart) getSourceEditPart().getParent();
		CrosstabHandleAdapter crosstabAdapter = part.getCrosstabHandleAdapter();

		int startWidth;
		int endWidth;
		startWidth = CrosstabTableUtil.caleVisualWidth(part, start);
		endWidth = CrosstabTableUtil.caleVisualWidth(part, end);

		crosstabAdapter.setColumnWidth(start, startWidth + value);
		if (!isCtrlDown() && start != end) {
			crosstabAdapter.setColumnWidth(end, endWidth - value);
		}
	}

	/**
	 * Resets size of column.
	 *
	 * @param start
	 * @param end
	 * @param value
	 */
	public void resizeColumn(int start, int end, int value) {
		CrosstabTableEditPart part = (CrosstabTableEditPart) getSourceEditPart().getParent();
		CrosstabHandleAdapter crosstabAdapter = part.getCrosstabHandleAdapter();

		int startWidth;
		int endWidth;

		startWidth = CrosstabTableUtil.caleVisualWidth(part, start);
		endWidth = CrosstabTableUtil.caleVisualWidth(part, end);

		crosstabAdapter.setColumnWidth(start, startWidth + value);
		if (!isCtrlDown() && start != end) {
			crosstabAdapter.setColumnWidth(end, endWidth - value);
		}

	}

	private CrosstabTableEditPart getCrosstabTableEditPart() {
		return (CrosstabTableEditPart) getSourceEditPart().getParent();
	}

	@Override
	protected String getInfomation() {
		CrosstabTableEditPart part = (CrosstabTableEditPart) getSourceEditPart().getParent();
		return getShowLabel(CrosstabTableUtil.caleVisualWidth(part, getStart()));
	}

	private String getShowLabel(int pix) {
		String unit = getDefaultUnits();

		double doubleValue = MetricUtility.pixelToPixelInch(pix);
		double showValue = DimensionUtil.convertTo(doubleValue, DesignChoiceConstants.UNITS_IN, unit).getMeasure();

		return PREFIX_LABEL + " " + getShowValue(showValue) + " " + getUnitDisplayName(unit) + " (" + pix + " "
				+ PIXELS_LABEL + ")";
	}

	private String getShowValue(double value) {
		return FORMAT.format(value);
	}

	@Override
	protected boolean handleDragInProgress() {
		CrosstabTableEditPart part = (CrosstabTableEditPart) getSourceEditPart().getParent();
		boolean bool = super.handleDragInProgress();
		// int value = getTrueValue( getLocation( ).x - getStartLocation( ).x );

		int value = getTrueValue(getMouseTrueValueX());

		int adjustWidth = CrosstabTableUtil.caleVisualWidth(part, getStart()) + value;
		updateInfomation(getShowLabel(adjustWidth));
		return bool;

	}

	@Override
	protected void fitResize() {
		List exclusion = new ArrayList();
		CrosstabTableEditPart part = (CrosstabTableEditPart) getSourceEditPart().getParent();

		int value = getMouseTrueValueX();

		CommandStack stack = part.getCrosstabHandleAdapter().getCrosstabItemHandle().getModuleHandle()
				.getCommandStack();

		stack.startTrans(RESIZE_COLUMN_TRANS_LABEL);
		int width = 0;

		exclusion.add(Integer.valueOf(getStart()));
		width = width + getTrueValue(value, getStart(), getEnd());
		resizeFixColumn(getTrueValue(value), getStart(), getEnd());

		if (!isCtrlDown()) {
			exclusion.add(Integer.valueOf(getEnd()));
			if (getStart() != getEnd()) {
				width = 0;
			}
		}
		// Resize the table
		Dimension tableSize = part.getFigure().getSize();

		part.getCrosstabHandleAdapter().setWidth(converPixToDefaultUnit(tableSize.width + width), getDefaultUnits());

		adjustOthersColumn(exclusion);
		// check the % unit

		stack.commit();
	}

	@Override
	protected AbstractTableEditPart getAbstractTableEditPart() {
		return (AbstractTableEditPart) getSourceEditPart().getParent();
	}

	@Override
	protected String getDefaultUnits() {
		CrosstabTableEditPart part = (CrosstabTableEditPart) getSourceEditPart().getParent();
		CrosstabHandleAdapter crosstabAdapter = part.getCrosstabHandleAdapter();
		return crosstabAdapter.getDesignElementHandle().getModuleHandle().getDefaultUnits();
	}

	// If the column don't set the width or set the percentage unit, set the actual
	// value
	protected void adjustOthersColumn(List exclusion) {
		AbstractTableEditPart part = getAbstractTableEditPart();
		WorkingData data = getTableWorkingData();
		ColumnData[] datas = data.columnWidths;
		if (datas == null) {
			return;
		}
		for (int i = 0; i < datas.length; i++) {
			if (exclusion.contains(Integer.valueOf(datas[i].columnNumber))) {
				continue;
			}

			ITableLayoutOwner.DimensionInfomation dim = part.getColumnWidth(datas[i].columnNumber);
			if (DesignChoiceConstants.UNITS_PERCENTAGE.equals(dim.getUnits())) {
				resizeFixColumn(0, datas[i].columnNumber, 1);
			} else if (dim.getUnits() == null || dim.getUnits().length() == 0) {
				resizeFixColumn(0, datas[i].columnNumber, datas[i].columnNumber);
			}
		}
	}

	@Override
	protected DragGuideInfo createDragGuideInfo() {
		int value = getTrueValue(getMouseTrueValueX());
		Point p = getStartLocation().getCopy();

		getAbstractTableEditPart().getFigure().translateToRelative(p);
		value = value + p.x;
		EditorRulerComposite.DragGuideInfo info = new EditorRulerComposite.DragGuideInfo(true, value);
		return info;
	}
}
