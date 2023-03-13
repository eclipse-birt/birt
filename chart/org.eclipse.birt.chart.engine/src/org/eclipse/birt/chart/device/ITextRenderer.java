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

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.component.Label;

/**
 * Provides convenience methods for rendering rotated text with configurable
 * attributes on a graphics context.
 */

public interface ITextRenderer extends IConstants {

	/**
	 * This method renders the 'shadow' at an offset from the text 'rotated
	 * rectangle' subsequently rendered.
	 *
	 * @param ipr
	 * @param iLabelPosition The position of the label w.r.t. the location specified
	 *                       by 'lo'
	 * @param lo             The location (specified as a 2d point) where the text
	 *                       is to be rendered
	 * @param la             The chart model structure containing the encapsulated
	 *                       text (and attributes) to be rendered
	 */
	void renderShadowAtLocation(IPrimitiveRenderer idr, int iLabelPosition, Location lo, Label la)
			throws ChartException;

	/**
	 *
	 * @param ipr
	 * @param iLabelPosition IConstants. LEFT, RIGHT, ABOVE or BELOW
	 * @param lo             POINT WHERE THE CORNER OF THE ROTATED RECTANGLE (OR
	 *                       EDGE CENTERED) IS RENDERED
	 * @param la
	 * @throws ChartException
	 */
	void renderTextAtLocation(IPrimitiveRenderer ipr, int iLabelPosition, Location lo, Label la) throws ChartException;

	/**
	 *
	 * @param idr
	 * @param boBlock
	 * @param taBlock
	 * @param la
	 */
	void renderTextInBlock(IDeviceRenderer idr, Bounds boBlock, TextAlignment taBlock, Label la) throws ChartException;
}
