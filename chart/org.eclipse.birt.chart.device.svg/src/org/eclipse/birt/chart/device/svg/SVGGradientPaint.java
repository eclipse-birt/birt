/***********************************************************************
 * Copyright (c) 2005 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.device.svg;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;

/**
 * This class represents gradient paint elements for the SVG renderer/
 */
public class SVGGradientPaint extends GradientPaint {

	private GradientPaint gradientPaint;
	protected String id;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param arg5
	 * @param gradientPaint
	 */
	public SVGGradientPaint(GradientPaint gradientPaint) {
		super(gradientPaint.getPoint1(), gradientPaint.getColor1(), gradientPaint.getPoint2(),
				gradientPaint.getColor2(), gradientPaint.isCyclic());
		this.gradientPaint = gradientPaint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		if (object == null)
			return false;
		if (object instanceof GradientPaint) {
			GradientPaint gp = (GradientPaint) object;
			return (gp.getColor1().equals(getColor1()) && gp.getColor2().equals(getColor2())
					&& (gp.isCyclic() == gp.isCyclic()) && gp.getPoint1().equals(getPoint1())
					&& gp.getPoint2().equals(getPoint2()));
		} else {
			return false;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int result = 17;
		if (getColor1() != null)
			result = 37 * result + getColor1().hashCode();
		if (getColor2() != null)
			result = 37 * result + getColor2().hashCode();
		result = 37 * result + ((isCyclic()) ? 0 : 1);
		if (getPoint1() != null)
			result = 37 * result + getPoint1().hashCode();
		if (getPoint2() != null)
			result = 37 * result + getPoint2().hashCode();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.GradientPaint#createContext(java.awt.image.ColorModel,
	 * java.awt.Rectangle, java.awt.geom.Rectangle2D, java.awt.geom.AffineTransform,
	 * java.awt.RenderingHints)
	 */
	public PaintContext createContext(ColorModel arg0, Rectangle arg1, Rectangle2D arg2, AffineTransform arg3,
			RenderingHints arg4) {
		return gradientPaint.createContext(arg0, arg1, arg2, arg3, arg4);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.GradientPaint#getColor1()
	 */
	public Color getColor1() {
		return gradientPaint.getColor1();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.GradientPaint#getColor2()
	 */
	public Color getColor2() {
		return gradientPaint.getColor2();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.GradientPaint#getPoint1()
	 */
	public Point2D getPoint1() {
		return gradientPaint.getPoint1();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.GradientPaint#getPoint2()
	 */
	public Point2D getPoint2() {
		return gradientPaint.getPoint2();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.GradientPaint#getTransparency()
	 */
	public int getTransparency() {
		return gradientPaint.getTransparency();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.GradientPaint#isCyclic()
	 */
	public boolean isCyclic() {
		return gradientPaint.isCyclic();
	}

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return "gp" + hashCode(); //$NON-NLS-1$
	}
}
