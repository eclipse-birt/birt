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

package org.eclipse.birt.chart.device.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.Map;

/**
 * This class provides a bridge between the java.awt.TextLayout class with the
 * SVG Renderer. The draw method is redirected to the SVG Graphic Context draw
 * string method.
 * 
 */
public class ChartTextLayout {

	protected String value;
	protected Map<? extends Attribute, ?> fontAttributes;
	protected FontRenderContext frc;
	protected java.awt.font.TextLayout helper;

	public ChartTextLayout(String value, Map<? extends Attribute, ?> fontAttributes, FontRenderContext frc) {
		this.value = value;
		this.fontAttributes = fontAttributes;
		this.frc = frc;
		this.helper = new java.awt.font.TextLayout(value, fontAttributes, frc);

	}

	/**
	 * Delegate method to the java.awt.TextLayout.getBounds method
	 */
	public Rectangle2D getBounds() {
		return helper.getBounds();
	}

	/**
	 * Delegate method to the Graphics2D drawString method.
	 * 
	 * @param g2d graphics context that is SVG graphic context.
	 * @param x   the x value to draw the string
	 * @param y   the y value to draw the string
	 */
	public void draw(Graphics2D g2d, float x, float y) {
		if (frc.isAntiAliased())
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		else
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		helper.draw(g2d, x, y);
	}
}
