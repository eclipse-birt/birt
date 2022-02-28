/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.Map;

import org.eclipse.birt.chart.device.util.ChartTextLayout;

/**
 * This class provides a bridge between the java.awt.TextLayout class with the
 * SVG Renderer. The draw method is redirected to the SVG Graphic Context draw
 * string method.
 *
 */

public class SVGTextLayout extends ChartTextLayout {

	public SVGTextLayout(String value, Map<? extends Attribute, ?> fontAttributes, FontRenderContext frc) {
		super(value, fontAttributes, frc);
	}

	@Override
	public void draw(Graphics2D g2d, float x, float y) {
		if (frc.isAntiAliased()) {
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		} else {
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		}
		g2d.drawString(value, x, y);
	}

}
