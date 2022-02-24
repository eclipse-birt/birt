/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.chart.device.svg;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D.Double;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.device.util.ChartTextRenderer;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.LineAttributes;

/**
 *
 */

public class SVGTextRenderer extends ChartTextRenderer {

	public SVGTextRenderer(IDisplayServer dispServer) {
		super(dispServer);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.util.ChartTextRenderer#fillWithoutDefer
	 * (java.awt.Graphics2D, java.awt.Shape)
	 */
	@Override
	protected void fillShadow(Graphics2D g2d, Shape shape) {
		((SVGGraphics2D) g2d).fill(shape, false);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.util.ChartTextRenderer#renderOutline(org
	 * .eclipse.birt.chart.device.IPrimitiveRenderer,
	 * org.eclipse.birt.chart.model.attribute.LineAttributes,
	 * java.awt.geom.Rectangle2D.Double)
	 */
	@Override
	protected void renderOutline(IPrimitiveRenderer renderer, LineAttributes lineAttribs, Double rect) {
		if (lineAttribs != null && lineAttribs.isVisible() && lineAttribs.getColor() != null) {
			SVGGraphics2D g2d = (SVGGraphics2D) ((IDeviceRenderer) renderer).getGraphicsContext();
			Stroke sPrevious = null;
			final ColorDefinition cd = lineAttribs.getColor();
			final Stroke sCurrent = ((SVGRendererImpl) renderer).getCachedStroke(lineAttribs);
			if (sCurrent != null) // SOME STROKE DEFINED?
			{
				sPrevious = g2d.getStroke();
				g2d.setStroke(sCurrent);
			}
			g2d.setColor((Color) _sxs.getColor(cd));
			g2d.draw(rect);
			g2d.setNoFillColor(g2d.getCurrentElement());
			if (sPrevious != null) // RESTORE PREVIOUS STROKE
			{
				g2d.setStroke(sPrevious);
			}
		}
	}

}
