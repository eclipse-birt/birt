/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.IOutsideBorder;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.MetricUtility;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.ResizeTracker;

/**
 * 
 */

public class ReportResizeTracker extends ResizeTracker {
	private ShowDragInfomationProcessor processor;

	public ReportResizeTracker(GraphicalEditPart owner, int direction) {
		super(owner, direction);
		if (PositionConstants.EAST == direction || PositionConstants.SOUTH == direction) {
			processor = new ShowDragInfomationProcessor(owner);
		}
	}

	protected void showSourceFeedback() {
		super.showSourceFeedback();
		if (processor != null) {
			int pix = 0;
			if (PositionConstants.EAST == getResizeDirection()) {
				pix = getFigureSize().width;
			} else if (PositionConstants.SOUTH == getResizeDirection()) {
				pix = getFigureSize().height;
			}
			processor.getInfomationLabel(getInfomation(pix), getStartLocation()).setText(getInfomation(pix));
		}
	}

	private Dimension getFigureSize() {
		IFigure figure = getOwner().getFigure();
		Dimension dim = figure.getSize();
		if (figure instanceof IOutsideBorder) {
			Border border = ((IOutsideBorder) figure).getOutsideBorder();
			Insets insets = border.getInsets(figure);
			if (PositionConstants.EAST == getResizeDirection()) {
				dim.width = dim.width - insets.right - insets.left;
			} else if (PositionConstants.SOUTH == getResizeDirection()) {
				dim.height = dim.height - insets.bottom - insets.top;
			}
		}
		return dim;
	}

	protected boolean handleButtonDown(int button) {
		boolean bool = super.handleButtonDown(button);
		if (button == 1) {
			showSourceFeedback();
		}
		return bool;
	}

	@Override
	protected void eraseSourceFeedback() {
		super.eraseSourceFeedback();
		if (processor != null) {
			processor.removeLabelFigue();
		}
	}

	@Override
	protected boolean handleDragInProgress() {
		boolean bool = super.handleDragInProgress();
		if (processor != null) {
			int width = 0;
			if (PositionConstants.EAST == getResizeDirection()) {
				width = getFigureSize().width + getMouseTrueValueX();
			} else if (PositionConstants.SOUTH == getResizeDirection()) {
				width = getFigureSize().height + getMouseTrueValueY();
			}
			if (width < 1) {
				width = 1;
			}
			processor.updateInfomation(getInfomation(width), getStartLocation());
		}
		return bool;
	}

	protected int getMouseTrueValueX() {
		int value = getLocation().x - getStartLocation().x;

		Dimension temp = new Dimension(value, 0);
		getOwner().getFigure().translateToRelative(temp);
		value = temp.width;

		return value;
	}

	protected int getMouseTrueValueY() {
		int value = getLocation().y - getStartLocation().y;

		Dimension temp = new Dimension(value, 0);
		getOwner().getFigure().translateToRelative(temp);
		value = temp.width;

		return value;
	}

	protected String getInfomation(int pix) {
		return getShowLabel(pix);
	}

	protected String getDefaultUnits() {
		Object model = getOwner().getModel();
		if (!(model instanceof DesignElementHandle)) {
			return DesignChoiceConstants.UNITS_IN;
		}
		ModuleHandle handle = ((DesignElementHandle) model).getModuleHandle();
		return handle.getDefaultUnits();
	}

	private String getShowLabel(int pix) {
		String unit = getDefaultUnits();

		double doubleValue = MetricUtility.pixelToPixelInch(pix);
		double showValue = DimensionUtil.convertTo(doubleValue, DesignChoiceConstants.UNITS_IN, unit).getMeasure();
		String prefix = "";
		if (PositionConstants.EAST == getResizeDirection()) {
			prefix = Messages.getString("ColumnDragTracker.Show.Label");
			;
		} else if (PositionConstants.SOUTH == getResizeDirection()) {
			prefix = Messages.getString("RowDragTracker.Show.Label");
		}
		return prefix + " " + TableDragGuideTracker.FORMAT.format(showValue) + " " //$NON-NLS-1$ //$NON-NLS-2$
				+ TableDragGuideTracker.getUnitDisplayName(unit) + " (" + pix + " " + TableDragGuideTracker.PIXELS_LABEL //$NON-NLS-1$ //$NON-NLS-2$
				+ ")"; //$NON-NLS-1$
	}

}
