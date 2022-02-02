/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.chart.device;

import org.eclipse.birt.chart.computation.GObjectFactory;
import org.eclipse.birt.chart.computation.IGObjectFactory;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.component.Label;

/**
 * 
 */

public class TextRendererAdapter implements ITextRenderer {

	protected IDisplayServer _sxs = null;

	protected static final IGObjectFactory goFactory = GObjectFactory.instance();

	protected TextRendererAdapter(IDisplayServer sxs) {
		this._sxs = sxs;
	}

	public void renderShadowAtLocation(IPrimitiveRenderer idr, int labelPosition, Location lo, Label la)
			throws ChartException {
		// TODO Auto-generated method stub

	}

	public void renderTextAtLocation(IPrimitiveRenderer ipr, int labelPosition, Location lo, Label la)
			throws ChartException {
		// TODO Auto-generated method stub

	}

	public void renderTextInBlock(IDeviceRenderer idr, Bounds boBlock, TextAlignment taBlock, Label la)
			throws ChartException {
		// TODO Auto-generated method stub

	}

	/**
	 * Adjusts the text by one half of width or height, according to the direction
	 * in position state. Returns new location if position changed, or returns the
	 * original location instance without position change.
	 * 
	 * @param iLabelPosition  position state
	 * @param lo              location
	 * @param itm
	 * @param dAngleInDegrees the rotated degree of font
	 * @return new location if position changed, or the original location instance.
	 */
	final protected Location adjustTextPosition(int iLabelPosition, final Location lo, final ITextMetrics itm,
			double dAngleInDegrees) {
		if (iLabelPosition > POSITION_MASK) {
			final double dAngleInRadians = ((-dAngleInDegrees * Math.PI) / 180.0);
			final double dSineTheta = Math.abs(Math.sin(dAngleInRadians));
			final double dCosTheta = Math.abs(Math.cos(dAngleInRadians));
			Location newLo = lo.copyInstance();
			if ((iLabelPosition & POSITION_MOVE_ABOVE) == POSITION_MOVE_ABOVE) {
				newLo.setY(lo.getY() - (itm.getFullHeight() * dCosTheta + itm.getFullWidth() * dSineTheta) / 2);
			} else if ((iLabelPosition & POSITION_MOVE_BELOW) == POSITION_MOVE_BELOW) {
				newLo.setY(lo.getY() + (itm.getFullHeight() * dCosTheta + itm.getFullWidth() * dSineTheta) / 2);
			} else if ((iLabelPosition & POSITION_MOVE_LEFT) == POSITION_MOVE_LEFT) {
				newLo.setX(lo.getX() - (itm.getFullWidth() * dCosTheta + itm.getFullHeight() * dSineTheta) / 2);
			} else if ((iLabelPosition & POSITION_MOVE_RIGHT) == POSITION_MOVE_RIGHT) {
				newLo.setX(lo.getX() + (itm.getFullWidth() * dCosTheta + itm.getFullHeight() * dSineTheta) / 2);
			}
			return newLo;
		}
		return lo;
	}

}
