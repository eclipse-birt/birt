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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Places a cross cell drag handle relative to a figure's bounds.
 */
public class CellDragoicator implements Locator {

	private double relativeX;
	private double relativeY;
	private IFigure reference;

	/**
	 * Constructor
	 *
	 * @param reference
	 * @param location
	 */
	public CellDragoicator(IFigure reference, int location) {
		setReferenceFigure(reference);
		switch (location & PositionConstants.NORTH_SOUTH) {
		case PositionConstants.SOUTH:
			relativeY = 1.0;
			break;
		default:
			relativeY = 0;
		}

		switch (location & PositionConstants.EAST_WEST) {
		case PositionConstants.EAST:
			relativeX = 1.0;
			break;
		default:
			relativeX = 0;
		}
	}

	/**
	 * @return
	 */
	protected Rectangle getReferenceBox() {
		return getReferenceFigure().getBounds();
	}

	/**
	 * @return
	 */
	protected IFigure getReferenceFigure() {
		return reference;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.draw2d.Locator#relocate(org.eclipse.draw2d.IFigure)
	 */
	@Override
	public void relocate(IFigure target) {
		IFigure reference = getReferenceFigure();
		Rectangle targetBounds = new PrecisionRectangle(getReferenceBox().getResized(-1, -1));
		reference.translateToAbsolute(targetBounds);
		target.translateToRelative(targetBounds);
		targetBounds.resize(1, 1);

		Dimension targetSize = getTargetSize(targetBounds.getSize());

		targetBounds.x += (int) (targetBounds.width * relativeX) - 1;
		targetBounds.y += (int) (targetBounds.height * relativeY);
		if (targetBounds.x < 0) {
			targetBounds.x = 0;
		}
		if (targetBounds.y < 0) {
			targetBounds.y = 0;
		}
		targetBounds.setSize(targetSize);
		target.setBounds(targetBounds);
	}

	private Dimension getTargetSize(Dimension size) {
		Dimension retValue = new Dimension((int) (size.width * relativeY), (int) (size.height * relativeX));
		retValue.width = Math.max(2, retValue.width);
		retValue.height = Math.max(2, retValue.height);

		return retValue;
	}

	/**
	 * @param reference
	 */
	public void setReferenceFigure(IFigure reference) {
		this.reference = reference;
	}

}
