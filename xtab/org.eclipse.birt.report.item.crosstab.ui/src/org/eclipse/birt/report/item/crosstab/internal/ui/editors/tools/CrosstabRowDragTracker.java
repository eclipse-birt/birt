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

import org.eclipse.birt.report.designer.internal.ui.editors.rulers.EditorRulerComposite;
import org.eclipse.birt.report.designer.internal.ui.editors.rulers.EditorRulerComposite.DragGuideInfo;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.AbstractTableEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableUtil;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.TableDragGuideTracker;
import org.eclipse.birt.report.designer.util.MetricUtility;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.CrosstabTableEditPart;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.CrosstabTableUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabHandleAdapter;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
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
 * Drag the cross cell bottom border to adjust the row height.
 */

public class CrosstabRowDragTracker extends TableDragGuideTracker {
	private static final String PREFIX_LABEL = Messages.getString("CrosstabRowDragTracker.Show.Label");

	/**
	 * Constructor
	 * 
	 * @param sourceEditPart
	 * @param start
	 * @param end
	 */
	public CrosstabRowDragTracker(EditPart sourceEditPart, int start, int end) {
		super(sourceEditPart, start, end);
		setDisabledCursor(Cursors.SIZENS);
	}

	@Override
	protected Cursor getDefaultCursor() {
		if (isCloneActive()) {
			return Cursors.SIZENS;
		}
		return super.getDefaultCursor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.
	 * TableDragGuideTracker#getDragWidth()
	 */
	protected Dimension getDragWidth(int start, int end) {
		return new Dimension(
				TableUtil.getMinHeight(getCrosstabTableEditPart(), getStart())
						- CrosstabTableUtil.caleVisualHeight(getCrosstabTableEditPart(), getStart()),
				Integer.MAX_VALUE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.
	 * TableDragGuideTracker#getMarqueeSelectionRectangle()
	 */
	protected Rectangle getMarqueeSelectionRectangle() {
		IFigure figure = getCrosstabTableEditPart().getFigure();
		Insets insets = figure.getInsets();

		int value = getLocation().y - getStartLocation().y;
		value = getTrueValueAbsolute(value);

		Point p = getStartLocation().getCopy();
		figure.translateToAbsolute(p);
		figure.translateToRelative(p);
		Rectangle bounds = figure.getBounds().getCopy();
		figure.translateToAbsolute(bounds);

		return new Rectangle(bounds.x + insets.left, value + p.y, bounds.width - (insets.left + insets.right), 2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.
	 * TableDragGuideTracker#resize()
	 */
	protected void resize() {
		CrosstabTableEditPart part = (CrosstabTableEditPart) getSourceEditPart().getParent();
		int value = getMouseTrueValueY();

		// if ( getStart( ) != getEnd( ) )
		{
			value = getTrueValue(value);

			CrosstabHandleAdapter adapter = part.getCrosstabHandleAdapter();
			int baseHeight = CrosstabTableUtil.caleVisualHeight(part, getStart());
			adapter.setRowHeight(getStart(), value + baseHeight);
		}
//		else
//		{
//			/**
//			 * This is the Last Row, resize the whole table.
//			 */
//			Dimension dimension = getDragWidth( );
//			if ( value < dimension.width )
//			{
//				value = dimension.width;
//			}
//
//			TableHandleAdapter adp = HandleAdapterFactory.getInstance( )
//					.getTableHandleAdapter( part.getModel( ) );
//
//			Dimension dm = adp.calculateSize( );
//			dm.height += value;
//			try
//			{
//				adp.ajustSize( dm );
//			}
//			catch ( SemanticException e )
//			{
//				ExceptionHandler.handle( e );
//			}
//
//		}

	}

	private CrosstabTableEditPart getCrosstabTableEditPart() {
		return (CrosstabTableEditPart) getSourceEditPart().getParent();
	}

	@Override
	protected String getInfomation() {
		CrosstabTableEditPart part = (CrosstabTableEditPart) getSourceEditPart().getParent();
		return getShowLabel(CrosstabTableUtil.caleVisualHeight(part, getStart()));
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
		// int value = getTrueValue( getLocation( ).y - getStartLocation( ).y);
		int value = getTrueValue(getMouseTrueValueY());

		int adjustWidth = CrosstabTableUtil.caleVisualHeight(part, getStart()) + value;
		updateInfomation(getShowLabel(adjustWidth));
		return bool;
	}

	@Override
	protected void fitResize() {
		CrosstabTableEditPart part = (CrosstabTableEditPart) getSourceEditPart().getParent();
		int value = getMouseTrueValueY();

		value = getTrueValue(value);

		CrosstabHandleAdapter adapter = part.getCrosstabHandleAdapter();
		int baseHeight = CrosstabTableUtil.caleVisualHeight(part, getStart());
		adapter.setRowHeight(getStart(), value + baseHeight);
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

	@Override
	protected DragGuideInfo createDragGuideInfo() {
		int value = getTrueValue(getMouseTrueValueY());
		Point p = getStartLocation().getCopy();

		getAbstractTableEditPart().getFigure().translateToRelative(p);
		value = value + p.y;
		EditorRulerComposite.DragGuideInfo info = new EditorRulerComposite.DragGuideInfo(false, value);
		return info;
	}
}
