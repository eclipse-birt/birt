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

package org.eclipse.birt.chart.event;

import java.util.Iterator;

import org.eclipse.birt.chart.computation.Object3D;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Gradient;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.model.attribute.MultipleFill;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;

/**
 * A rendering event type for rendering 3D Polygon object.
 */
public final class Polygon3DRenderEvent extends PolygonRenderEvent implements I3DRenderEvent {

	private static final long serialVersionUID = -6572679563207168795L;

	private boolean bDoubleSided = false;

	private double dBrightness = 1d;

	private boolean bBehind = false;

	private transient Object3D object3D;

	private transient Fill runtimeBackground;

	/**
	 * The constructor.
	 */
	public Polygon3DRenderEvent(Object oSource) {
		super(oSource);
	}

	/**
	 * Returns true if double sided polygons (not enclosing a volume)
	 * 
	 * @return
	 */
	public boolean isDoubleSided() {
		return bDoubleSided;
	}

	/**
	 * Sets if this polygon is double sided.
	 * 
	 * @param value
	 */
	public void setDoubleSided(boolean value) {
		this.bDoubleSided = value;
	}

	/**
	 * @return Returns if this polygon is facing behind(along user's viewing
	 *         direction).
	 */
	public boolean isBehind() {
		return bBehind;
	}

	/**
	 * Sets if this polygon is facing behind(along user's viewing direction).
	 */
	public void setBehind(boolean value) {
		this.bBehind = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.event.PolygonRenderEvent#setBackground(org.eclipse.
	 * birt.chart.model.attribute.Fill)
	 */
	public void setBackground(Fill ifBackground) {
		super.setBackground(ifBackground);

		runtimeBackground = ifBackground;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PolygonRenderEvent#getBackground()
	 */
	public Fill getBackground() {
		return runtimeBackground;
	}

	/**
	 * @return Returns the brightness of this polyogn(0.0 - 1.0).
	 */
	public double getBrightness() {
		return dBrightness;
	}

	protected void applyBrightness(ColorDefinition cdf, double brightness) {
		cdf.set((int) (cdf.getRed() * dBrightness), (int) (cdf.getGreen() * dBrightness),
				(int) (cdf.getBlue() * dBrightness), cdf.getTransparency());
	}

	protected void applyBrightnessToFill(Fill fill, double brightness) {
		if (fill instanceof ColorDefinition) {
			ColorDefinition cdf = (ColorDefinition) fill;
			applyBrightness(cdf, dBrightness);
		} else if (fill instanceof Gradient) {
			Gradient gradient = (Gradient) fill;
			applyBrightness(gradient.getStartColor(), dBrightness);
			applyBrightness(gradient.getEndColor(), dBrightness);

		} else if (fill instanceof MultipleFill) {
			for (Iterator<?> iter = ((MultipleFill) fill).getFills().iterator(); iter.hasNext();) {
				applyBrightnessToFill((Fill) iter.next(), brightness);
			}
		}
	}

	/**
	 * Sets the brightness of this polygon, the value ranges 0.0 - 1.0.
	 */
	public void setBrightness(double value) {
		dBrightness = value;
		if (_ifBackground != null) {
			Fill fill = goFactory.copyOf(_ifBackground);

			applyBrightnessToFill(fill, dBrightness);
			runtimeBackground = fill;
		}
	}

	/**
	 * Note that setPoints3D must be called with the points in the right order: that
	 * is needed for the right orientation of the polygon. Points must be given in
	 * anti-clockwise order if looking at the face from outside the enclosed volume,
	 * and so that two adjacent points define a line of the polygon. A minimum of
	 * three points is required, less will throw an IllegalArgumentException, three
	 * consecutive points cannot be aligned.
	 * 
	 * @param la Sets the co-ordinates for each point that defines the polygon
	 */
	public final void setPoints3D(Location3D[] loa) throws ChartException {
		setPoints3D(loa, false);
	}

	/**
	 * Note that setPoints3D must be called with the points in the right order: that
	 * is needed for the right orientation of the polygon. Points must be given in
	 * anti-clockwise order if looking at the face from outside the enclosed volume,
	 * and so that two adjacent points define a line of the polygon. A minimum of
	 * three points is required, less will throw an IllegalArgumentException, three
	 * consecutive points cannot be aligned.
	 * 
	 * @param la       Sets the co-ordinates for each point that defines the polygon
	 * @param inverted Inverts the orientation of the surface if true
	 */
	public final void setPoints3D(Location3D[] loa, boolean inverted) throws ChartException {

		if (loa.length < 3) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.RENDERING,
					"exception.3D.points.length.less.than.3", //$NON-NLS-1$
					Messages.getResourceBundle());
		}
		object3D = new Object3D(loa, inverted);
	}

	/**
	 * @return Returns the co-ordinates for each point in the polygon
	 */
	public Location3D[] getPoints3D() {
		return object3D.getLocation3D();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.I3DRenderEvent#prepare2D(double, double)
	 */
	public void prepare2D(double xOffset, double yOffset) {
		Location[] points = object3D.getPoints2D(xOffset, yOffset);
		setPoints(points);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#copy()
	 */
	public PrimitiveRenderEvent copy() {
		final Polygon3DRenderEvent pre = new Polygon3DRenderEvent(source);
		if (object3D != null) {
			pre.object3D = new Object3D(object3D);
		}

		if (_lia != null) {
			pre.setOutline(goFactory.copyOf(_lia));
		}

		if (_ifBackground != null) {
			pre.setBackground(goFactory.copyOf(_ifBackground));
		}

		pre.bDoubleSided = bDoubleSided;
		pre.dBrightness = dBrightness;
		pre.bBehind = bBehind;
		pre.setEnable(this.bEnabled);
		return pre;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PolygonRenderEvent#reset()
	 */
	public void reset() {
		if (object3D != null) {
			object3D.reset();
		}
		this.bDoubleSided = false;
		this.dBrightness = 1;
		this.bBehind = false;
		this.runtimeBackground = null;
		this.setEnable(true);
		super.reset();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.I3DRenderEvent#getObject3D()
	 */
	public Object3D getObject3D() {
		return object3D;
	}

}
