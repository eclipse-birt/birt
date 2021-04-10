/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.event;

import org.eclipse.birt.chart.computation.Object3D;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;

/**
 * A rendering event type for rendering 3D Arc object.
 */
public class Arc3DRenderEvent extends ArcRenderEvent implements I3DRenderEvent {

	private static final long serialVersionUID = 4105315690869364270L;

	private transient Object3D object3D;

	/**
	 * The constructor.
	 */
	public Arc3DRenderEvent(Object oSource) {
		super(oSource);
	}

	/**
	 * Sets the 3D top-left location of the arc bounds.
	 * 
	 * @param loc
	 */
	public void setTopLeft3D(Location3D loc) {
		object3D = new Object3D(loc);
	}

	/**
	 * @return Returns the 3D top-left location of the arc bounds.
	 */
	public Location3D getTopLeft3D() {
		return object3D.getLocation3D()[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#copy()
	 */
	public PrimitiveRenderEvent copy() {
		Arc3DRenderEvent are = new Arc3DRenderEvent(source);

		if (object3D != null) {
			are.object3D = new Object3D(object3D);
		}

		if (outline != null) {
			are.setOutline(goFactory.copyOf(outline));
		}

		if (ifBackground != null) {
			are.setBackground(goFactory.copyOf(ifBackground));
		}

		are.setStyle(iStyle);
		are.setWidth(dWidth);
		are.setHeight(dHeight);
		are.setStartAngle(dStartInDegrees);
		are.setAngleExtent(dExtentInDegrees);
		are.setInnerRadius(dInnerRadius);
		are.setOuterRadius(dOuterRadius);

		return are;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.I3DRenderEvent#getObject3D()
	 */
	public Object3D getObject3D() {
		return object3D;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.I3DRenderEvent#prepare2D(double, double)
	 */
	public void prepare2D(double xOffset, double yOffset) {
		Location[] points = object3D.getPoints2D(xOffset, yOffset);
		setTopLeft(points[0]);
	}

}
