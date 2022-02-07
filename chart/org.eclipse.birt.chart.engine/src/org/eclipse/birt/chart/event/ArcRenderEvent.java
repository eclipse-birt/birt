/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.event;

import org.eclipse.birt.chart.computation.GObjectFactory;
import org.eclipse.birt.chart.computation.IGObjectFactory;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;

/**
 * A rendering event type for rendering Arc object.
 */
public class ArcRenderEvent extends PrimitiveRenderEvent {

	private static final long serialVersionUID = -8516218845415390970L;

	protected static final IGObjectFactory goFactory = GObjectFactory.instance();

	protected transient Location loTopLeft = null;

	protected double dWidth;

	protected double dHeight;

	protected double dStartInDegrees;

	protected double dExtentInDegrees;

	protected double dInnerRadius;

	protected double dOuterRadius;

	protected transient LineAttributes outline;

	protected transient Fill ifBackground = null;

	protected int iStyle = SECTOR;

	/**
	 * The closure type for an open arc with no path segments connecting the two
	 * ends of the arc segment.
	 */
	public static final int OPEN = 1;

	/**
	 * The closure type for an arc closed by drawing a straight line segment from
	 * the start of the arc segment to the end of the arc segment.
	 */
	public static final int CLOSED = 2;

	/**
	 * The closure type for an arc closed by drawing straight line segments from the
	 * start of the arc segment to the center of the full ellipse and from that
	 * point to the end of the arc segment.
	 */
	public static final int SECTOR = 3;

	/**
	 * The constructor.
	 */
	public ArcRenderEvent(Object oSource) {
		super(oSource);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#reset()
	 */
	public void reset() {
		loTopLeft = null;
		dWidth = 0;
		dHeight = 0;
		dStartInDegrees = 0;
		dExtentInDegrees = 0;
		dInnerRadius = 0;
		dOuterRadius = 0;
		outline = null;
		ifBackground = null;
		iStyle = SECTOR;
	}

	/**
	 * @return Returns the arc style. The value must be one of these defined in this
	 *         class:
	 *         <ul>
	 *         <li>{@link #OPEN}
	 *         <li>{@link #CLOSED}
	 *         <li>{@link #SECTOR}
	 *         </ul>
	 */
	public final int getStyle() {
		return iStyle;
	}

	/**
	 * @param style The arc style to set. The value must be one of these defined in
	 *              this class:
	 *              <ul>
	 *              <li>{@link #OPEN}
	 *              <li>{@link #CLOSED}
	 *              <li>{@link #SECTOR}
	 *              </ul>
	 */
	public final void setStyle(int style) {
		iStyle = style;
	}

	/**
	 * @return Returns the top left co-ordinates of the bounding elliptical box for
	 *         the arc
	 */
	public final Location getTopLeft() {
		return loTopLeft;
	}

	/**
	 * Sets the top-left location of the containing bounds.
	 * 
	 * @param loTopLeft The top left co-ordinates of the bounding elliptical box for
	 *                  the arc
	 */
	public final void setTopLeft(Location loTopLeft) {
		this.loTopLeft = loTopLeft;
	}

	/**
	 * @return Returns the angle extent of this arc.
	 */
	public final double getAngleExtent() {
		return dExtentInDegrees;
	}

	/**
	 * Sets the angle extent for this arc.
	 * 
	 * @param angleExtent The angle extent
	 * @since 2.1
	 */

	public final void setAngleExtent(double angleExtent) {
		this.dExtentInDegrees = angleExtent;
	}

	/**
	 * Sets the angle extent for this arc.
	 * 
	 * @param endAngle The angle extent
	 * @deprecated Use {@link #setAngleExtent()} instead.
	 */
	public final void setEndAngle(double endAngle) {
		this.dExtentInDegrees = endAngle;
	}

	/**
	 * @return Returns the background.
	 */
	public final Fill getBackground() {
		return ifBackground;
	}

	/**
	 * Sets the backgound for this arc.
	 * 
	 * @param ifBackground The background to set.
	 */
	public final void setBackground(Fill ifBackground) {
		this.ifBackground = ifBackground;
	}

	/**
	 * @return Returns the width of the containing bounds.
	 */
	public double getWidth() {
		return dWidth;
	}

	/**
	 * Sets the width for the containing bounds.
	 * 
	 * @param radius The width to set.
	 */
	public void setWidth(double width) {
		this.dWidth = width;
	}

	/**
	 * @return Returns the height of the containing bounds.
	 */
	public double getHeight() {
		return dHeight;
	}

	/**
	 * Sets the height for the containing bounds.
	 * 
	 * @param radius The height to set.
	 */
	public void setHeight(double height) {
		this.dHeight = height;
	}

	/**
	 * @return Returns the startAngle.
	 */
	public final double getStartAngle() {
		return dStartInDegrees;
	}

	/**
	 * Sets the start angle for this arc.
	 * 
	 * @param startAngle The startAngle to set.
	 */
	public final void setStartAngle(double startAngle) {
		this.dStartInDegrees = startAngle;
	}

	/**
	 * Sets the containing bounds of this arc.
	 * 
	 * @param bo
	 */
	public final void setBounds(Bounds bo) {
		setTopLeft(goFactory.createLocation(bo.getLeft(), bo.getTop()));
		setWidth(bo.getWidth());
		setHeight(bo.getHeight());
	}

	/**
	 * Returns the full containing bounds of the complete ellipse.
	 * 
	 * @return
	 */
	public Bounds getEllipseBounds() {
		return goFactory.createBounds(loTopLeft.getX(), loTopLeft.getY(), dWidth, dHeight);
	}

	private double normalizeDegrees(double angle) {
		if (angle > 180.0) {
			if (angle <= (180.0 + 360.0)) {
				angle = angle - 360.0;
			} else {
				angle = Math.IEEEremainder(angle, 360.0);
				// IEEEremainder can return -180 here for some input values...
				if (angle == -180.0) {
					angle = 180.0;
				}
			}
		} else if (angle <= -180.0) {
			if (angle > (-180.0 - 360.0)) {
				angle = angle + 360.0;
			} else {
				angle = Math.IEEEremainder(angle, 360.0);
				// IEEEremainder can return -180 here for some input values...
				if (angle == -180.0) {
					angle = 180.0;
				}
			}
		}
		return angle;
	}

	private boolean containsAngle(double angle) {
		double angExt = getAngleExtent();
		boolean backwards = (angExt < 0.0);
		if (backwards) {
			angExt = -angExt;
		}
		if (angExt >= 360) {
			return true;
		}
		angle = normalizeDegrees(angle) - normalizeDegrees(getStartAngle());
		if (backwards) {
			angle = -angle;
		}
		if (angle < 0.0) {
			angle += 360.0;
		}

		return (angle >= 0.0) && (angle < angExt);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#getBounds()
	 */
	public Bounds getBounds() {
		// calculate the actual bounds.
		double x1, y1, x2, y2;
		if (getStyle() == SECTOR) {
			x1 = y1 = x2 = y2 = 0.0;
		} else {
			x1 = y1 = 1.0;
			x2 = y2 = -1.0;
		}
		double angle = 0.0;
		for (int i = 0; i < 6; i++) {
			if (i < 4) {
				// 0-3 are the four quadrants
				angle += 90.0;
				if (!containsAngle(angle)) {
					continue;
				}
			} else if (i == 4) {
				// 4 is start angle
				angle = getStartAngle();
			} else {
				// 5 is end angle
				angle += getAngleExtent();
			}
			double rads = Math.toRadians(-angle);
			double xe = Math.cos(rads);
			double ye = Math.sin(rads);
			x1 = Math.min(x1, xe);
			y1 = Math.min(y1, ye);
			x2 = Math.max(x2, xe);
			y2 = Math.max(y2, ye);
		}
		double w = getWidth();
		double h = getHeight();
		x2 = (x2 - x1) * 0.5 * w;
		y2 = (y2 - y1) * 0.5 * h;
		x1 = getTopLeft().getX() + (x1 * 0.5 + 0.5) * w;
		y1 = getTopLeft().getY() + (y1 * 0.5 + 0.5) * h;

		return goFactory.createBounds(x1, y1, x2, y2);
	}

	/**
	 * @return Returns the outline.
	 */
	public final LineAttributes getOutline() {
		return outline;
	}

	/**
	 * Sets the outline for this arc.
	 * 
	 * @param outline The outline to set.
	 */
	public final void setOutline(LineAttributes outline) {
		this.outline = outline;
	}

	/**
	 * @return Returns the inner radius for this arc.
	 */
	public double getInnerRadius() {
		return dInnerRadius;
	}

	/**
	 * Sets the inner radius for this arc.
	 * 
	 * @param innerRadius
	 */
	public void setInnerRadius(double innerRadius) {
		dInnerRadius = innerRadius;
	}

	/**
	 * @return Returns the outer radius for this arc.
	 */
	public double getOuterRadius() {
		return dOuterRadius;
	}

	/**
	 * Sets the outer radius for this arc.
	 * 
	 * @param outerRadius
	 */
	public void setOuterRadius(double outerRadius) {
		dOuterRadius = outerRadius;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#copy()
	 */
	public PrimitiveRenderEvent copy() throws ChartException {
		ArcRenderEvent are = new ArcRenderEvent(source);
		if (outline != null) {
			are.setOutline(goFactory.copyOf(outline));
		}

		if (ifBackground != null) {
			are.setBackground(goFactory.copyOf(ifBackground));
		}

		if (loTopLeft != null) {
			are.setTopLeft(loTopLeft.copyInstance());
		}

		are.setStyle(iStyle);
		are.setWidth(dWidth);
		are.setHeight(dHeight);
		are.setStartAngle(dStartInDegrees);
		are.setEndAngle(dExtentInDegrees);
		are.setInnerRadius(dInnerRadius);
		are.setOuterRadius(dOuterRadius);

		return are;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.event.PrimitiveRenderEvent#draw(org.eclipse.birt.chart
	 * .device.IDeviceRenderer)
	 */
	public void draw(IDeviceRenderer idr) throws ChartException {
		idr.drawArc(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.event.PrimitiveRenderEvent#fill(org.eclipse.birt.chart
	 * .device.IDeviceRenderer)
	 */
	public void fill(IDeviceRenderer idr) throws ChartException {
		idr.fillArc(this);
	}

	public LineAttributes getLineAttributes() {
		return getOutline();
	}
}
