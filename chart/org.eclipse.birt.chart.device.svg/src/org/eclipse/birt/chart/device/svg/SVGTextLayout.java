package org.eclipse.birt.chart.device.svg;
/***********************************************************************
 * Copyright (c) 2006 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 ***********************************************************************/
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.Map;

/**
 * This class provides a bridge between the java.awt.TextLayout class with 
 * the SVG Renderer.  The draw method is redirected to the SVG Graphic Context
 * draw string method.
 *
 */
public class SVGTextLayout {
	
	protected String value;
	protected Map fontAttributes;
	protected FontRenderContext frc;
	protected java.awt.font.TextLayout helper;
	
	public SVGTextLayout(String value, Map fontAttributes, FontRenderContext frc){
		this.value = value;
		this.fontAttributes = fontAttributes;
		this.frc = frc;
		this.helper = new java.awt.font.TextLayout(value, fontAttributes, frc);
		
	}
	
	/**
	 * Delegate method to the java.awt.TextLayout.getBounds method
	 */
	public Rectangle2D getBounds(){
		return helper.getBounds();
	}
	
	/**
	 * Delegate method to the Graphics2D drawString method.
	 * @param g2d graphics context that is SVG graphic context.
	 * @param x the x value to draw the string
	 * @param y the y value to draw the string
	 */
	public void draw(Graphics2D g2d, float x, float y){
		SVGGraphics2D SVGg2d = (SVGGraphics2D)g2d;
		if ( frc.isAntiAliased( ) )
			SVGg2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
		else
			SVGg2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_OFF );
		SVGg2d.drawString(value, x, y);
	}
}
