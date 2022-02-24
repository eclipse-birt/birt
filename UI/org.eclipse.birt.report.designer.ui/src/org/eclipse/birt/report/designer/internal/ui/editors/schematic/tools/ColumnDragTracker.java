/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.schematic.ColumnHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.core.model.schematic.TableHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.rulers.EditorRulerComposite;
import org.eclipse.birt.report.designer.internal.ui.editors.rulers.EditorRulerComposite.DragGuideInfo;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.AbstractTableEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableUtil;
import org.eclipse.birt.report.designer.internal.ui.layout.ITableLayoutOwner;
import org.eclipse.birt.report.designer.internal.ui.layout.TableLayout.WorkingData;
import org.eclipse.birt.report.designer.internal.ui.layout.TableLayoutData.ColumnData;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.MetricUtility;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.swt.graphics.Cursor;

public class ColumnDragTracker extends TableDragGuideTracker {

	private static final String RESIZE_COLUMN_TRANS_LABEL = Messages.getString("ColumnDragTracker.ResizeColumn"); //$NON-NLS-1$
	private static final String PREFIX_LABEL = Messages.getString("ColumnDragTracker.Show.Label"); //$NON-NLS-1$

	/**
	 * Creates new ColumnDragtrcker, for resize the table column width
	 * 
	 * @param sourceEditPart
	 * @param start
	 * @param end
	 */
	public ColumnDragTracker(EditPart sourceEditPart, int start, int end) {
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

	protected void resize() {
		TableEditPart part = (TableEditPart) getSourceEditPart();
		int value = getMouseTrueValueX();
		part.getTableAdapter().transStar(RESIZE_COLUMN_TRANS_LABEL);
		if (isresizeMultipleColumn() && isCtrlDown()) {
			List list = filterEditPart(part.getViewer().getSelectedEditParts());
			boolean resizeTable = false;
			int width = 0;
			for (int i = 0; i < list.size(); i++) {
				int tempValue = value;
				Object model = ((EditPart) list.get(i)).getModel();
				ColumnHandleAdapter adapter = HandleAdapterFactory.getInstance().getColumnHandleAdapter(model);
				int start = adapter.getColumnNumber();
				int end = start + 1;

				int ori = TableUtil.caleVisualWidth(part, model);
				int adjustWidth = TableUtil.caleVisualWidth(part, part.getColumn(getStart())) + value;
				if (getStart() != start) {
					tempValue = adjustWidth - ori;
				}
				if (start == part.getColumnCount()) {
					end = start;
					resizeTable = true;

				} else {
					width = width + getTrueValue(tempValue, start, end);
				}
				resizeColumn(tempValue, start, end);

			}

			if (resizeTable) {
				Dimension size = part.getTableAdapter().getSize();
				try {
					part.getTableAdapter().setSize(new Dimension(size.width + width, -1));
				} catch (SemanticException e) {
					part.getTableAdapter().rollBack();
					ExceptionHandler.handle(e);
				}
			}
		} else {
			resizeColumn(value, getStart(), getEnd());
		}
		part.getTableAdapter().transEnd();
	}

	private void resizeColumn(int value, int start, int end) {
		TableEditPart part = (TableEditPart) getSourceEditPart();
		// int value = getLocation( ).x - getStartLocation( ).x;

		if (start != end) {
			value = getTrueValue(value, start, end);
			part.resizeColumn(start, end, value, !isCtrlDown());
		} else {
			/**
			 * This is the Last Column, resize the whole table.
			 */
			Dimension dimension = getDragWidth(start, end);

			if (value < dimension.width) {
				value = dimension.width;
			}

			TableHandleAdapter adp = HandleAdapterFactory.getInstance().getTableHandleAdapter(part.getModel());

			Dimension dm = adp.calculateSize();

			dm.width += value;
			dm.height = -1;
			try {
				adp.ajustSize(dm);
			} catch (SemanticException e) {
				ExceptionHandler.handle(e);
			}
		}
	}

	private void resizeFixColumn(int value, int start, int end) {
		TableEditPart part = (TableEditPart) getSourceEditPart();
		value = getTrueValue(value, start, end);
		// part.resizeColumn( start, end, value );

		Object startColumn = part.getColumn(start);
		if (!(startColumn instanceof ColumnHandle)) {
			return;
		}

		Object endColumn = part.getColumn(end);
		if (!(endColumn instanceof ColumnHandle)) {
			return;
		}

		int startWidth = 0;
		int endWidth = 0;

		startWidth = TableUtil.caleVisualWidth( part, startColumn );
		endWidth = TableUtil.caleVisualWidth( part, endColumn );
		try
		{
			MetricUtility.updateDimension(
					( (ColumnHandle) startColumn ).getWidth( ),
					startWidth + value );

			if ( !isCtrlDown( ) && start != end )
			{
				MetricUtility.updateDimension(
						( (ColumnHandle) endColumn ).getWidth( ),
						endWidth - value );
			}
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}
	}

	private boolean isresizeMultipleColumn() {
		TableEditPart part = (TableEditPart) getSourceEditPart();
		List list = filterEditPart(part.getViewer().getSelectedEditParts());
		if (list.size() < 2) {
			return false;
		}

		Object first = ((EditPart) list.get(0)).getModel();
		if (!(first instanceof org.eclipse.birt.report.model.api.ColumnHandle)
				|| !((org.eclipse.birt.report.model.api.ColumnHandle) first).getContainer().equals(part.getModel())) {
			return false;
		}
		for (int i = 0; i < list.size(); i++) {
			Object model = ((EditPart) list.get(i)).getModel();
			if (model.equals(part.getColumn(getStart()))) {
				return true;
			}
		}

		return false;
	}

	protected Rectangle getMarqueeSelectionRectangle() {
		IFigure figure = ((TableEditPart) getSourceEditPart()).getFigure();
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

	protected Dimension getDragWidth(int start, int end) {
		TableEditPart part = (TableEditPart) getSourceEditPart();
		if (isCtrlDown()) {
			Dimension retValue = new Dimension(part.getMinWidth(start) - getColumnWidth(start), Integer.MAX_VALUE);
			return retValue;
			// part.getFigure( ).translateToAbsolute( retValue );
		} else {
			if (getStart() == getEnd()) {
				return new Dimension(part.getMinWidth(getStart()) - getColumnWidth(getStart()), Integer.MAX_VALUE);
			}

			return new Dimension(part.getMinWidth(getStart()) - getColumnWidth(getStart()),
					getColumnWidth(getEnd()) - part.getMinWidth(getEnd()));
		}
		// return retValue;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.tools.AbstractTool#setCursor(org.eclipse.swt.graphics.Cursor)
	 */
	protected void setCursor(Cursor cursor) {
		super.setCursor(cursor);
	}

	/**
	 * Gets the column width
	 * 
	 * @param columnNumber
	 * @return
	 */
	protected int getColumnWidth(int columnNumber) {
		Object column = getTableEditPart().getColumn(columnNumber);
		if (column == null) {
			return HandleAdapterFactory.getInstance().getTableHandleAdapter(getTableEditPart().getModel())
					.getDefaultWidth(columnNumber);
		}

		return getColumnWidth(column);
	}

	/**
	 * Gets the column width
	 * 
	 * @param column
	 * @return
	 */
	protected int getColumnWidth(Object column) {
		return TableUtil.caleVisualWidth(getTableEditPart(), column);
	}

	/**
	 * Gets the TableEditPart
	 * 
	 * @return
	 */
	protected TableEditPart getTableEditPart() {
		return (TableEditPart) getSourceEditPart();
	}

	@Override
	protected String getInfomation() {
		TableEditPart part = (TableEditPart) getSourceEditPart();
		return getShowLabel(TableUtil.caleVisualWidth(part, part.getColumn(getStart())));
	}

	private String getShowLabel(int pix) {
		String unit = getDefaultUnits();

		double doubleValue = MetricUtility.pixelToPixelInch(pix);
		double showValue = DimensionUtil.convertTo(doubleValue, DesignChoiceConstants.UNITS_IN, unit).getMeasure();

		return PREFIX_LABEL + " " + getShowValue(showValue) + " " + getUnitDisplayName(unit) + " (" + pix + " " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				+ PIXELS_LABEL + ")"; //$NON-NLS-1$
	}

	private String getShowValue(double value) {
		return FORMAT.format(value);
	}

	@Override
	protected boolean handleDragInProgress() {
		TableEditPart part = (TableEditPart) getSourceEditPart();
		boolean bool = super.handleDragInProgress();
		// int value = getTrueValue( getLocation( ).x - getStartLocation( ).x);
		int value = getTrueValue(getMouseTrueValueX());

		int adjustWidth = TableUtil.caleVisualWidth(part, part.getColumn(getStart())) + value;
		updateInfomation(getShowLabel(adjustWidth));
		return bool;

	}

	@Override
	protected void fitResize() {
		List exclusion = new ArrayList();
		TableEditPart part = (TableEditPart) getSourceEditPart();

		int value = getMouseTrueValueX();

		part.getTableAdapter().transStar(RESIZE_COLUMN_TRANS_LABEL);
		int width = 0;

		if (isresizeMultipleColumn() && isCtrlDown()) {
			List list = filterEditPart(part.getViewer().getSelectedEditParts());

			for (int i = 0; i < list.size(); i++) {
				int tempValue = value;
				Object model = ((EditPart) list.get(i)).getModel();
				ColumnHandleAdapter adapter = HandleAdapterFactory.getInstance().getColumnHandleAdapter(model);
				int start = adapter.getColumnNumber();

				exclusion.add(Integer.valueOf(start));
				int end = start + 1;

				int ori = TableUtil.caleVisualWidth(part, model);
				int adjustWidth = TableUtil.caleVisualWidth(part, part.getColumn(getStart())) + value;
				if (getStart() != start) {
					tempValue = adjustWidth - ori;
				}
				if (start == part.getColumnCount()) {
					end = start;
				}

				width = width + getTrueValue(tempValue, start, end);

				resizeFixColumn(tempValue, start, end);
			}
		} else {
			exclusion.add(Integer.valueOf(getStart()));
			width = width + getTrueValue(value, getStart(), getEnd());
			resizeFixColumn(value, getStart(), getEnd());
		}
		if (!isCtrlDown()) {
			exclusion.add(Integer.valueOf(getEnd()));
			if (getStart() != getEnd()) {
				width = 0;
			}
		}
		// Resize the table
		Dimension tableSize = part.getFigure().getSize();
		try {
			ReportItemHandle handle = part.getTableAdapter().getReportItemHandle();
			// DimensionHandle dimension = handle.getWidth();
			// dimension.s
			// part.getTableAdapter( ).setSize( new Dimension(tableSize.width + width, -1)
			// );
			double tbWidth = converPixToDefaultUnit(tableSize.width + width);
			setDimensionValue(handle, tbWidth);
		} catch (SemanticException e) {
			part.getTableAdapter().rollBack();
			ExceptionHandler.handle(e);
		}
		adjustOthersColumn(exclusion);
		// check the % unit

		part.getTableAdapter().transEnd();
	}

	private void setDimensionValue(ReportItemHandle handle, double value) throws SemanticException {
		DimensionValue dimensionValue = new DimensionValue(value, getDefaultUnits());
		handle.getWidth().setValue(dimensionValue);
	}

	// If the column don't set the width or set the percentage unit, set the actual
	// value
	private void adjustOthersColumn(List exclusion) {
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
