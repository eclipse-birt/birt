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

import org.eclipse.birt.chart.computation.Object3D;
import org.eclipse.birt.chart.computation.Vector;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;

/**
 * A rendering event type for rendering 3D Line object.
 */
public final class Line3DRenderEvent extends LineRenderEvent implements I3DRenderEvent {

	private static final long serialVersionUID = 33812052466380930L;

	private transient Object3D object3D;

	private transient Object3D object3DParent;

	/**
	 * The constructor.
	 */
	public Line3DRenderEvent(Object oSource) {
		super(oSource);
		object3D = new Object3D(2);
	}

	/**
	 * Sets the 3D start location of the line.
	 */
	public void setStart3D(Location3D start) {
		object3D.getVectors()[0] = new Vector(start);
	}

	/**
	 * Sets the 3D start location of the line.
	 */
	public void setStart3D(double x, double y, double z) {
		object3D.getVectors()[0] = new Vector(x, y, z, true);
	}

	/**
	 * Returns the 3D start location of this line. Not a live object
	 * 
	 * @return
	 */
	public Location3D getStart3D() {
		return object3D.getLocation3D()[0];
	}

	/**
	 * Sets the 3D end location of the line.
	 */
	public void setEnd3D(Location3D end) {
		object3D.getVectors()[1] = new Vector(end);
	}

	/**
	 * Sets the 3D end location of the line.
	 */
	public void setEnd3D(double x, double y, double z) {
		object3D.getVectors()[1] = new Vector(x, y, z, true);
	}

	/**
	 * Returns the 3D end location of this line. Not a live object
	 * 
	 * @return
	 */
	public Location3D getEnd3D() {
		return object3D.getLocation3D()[1];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#copy()
	 */
	public PrimitiveRenderEvent copy() {
		Line3DRenderEvent lre = new Line3DRenderEvent(source);
		lre.setLineAttributes(goFactory.copyOf(lia));
		if (object3D != null) {
			lre.object3D = new Object3D(object3D);
		}
		if (object3DParent != null) {
			lre.object3DParent = object3DParent;
		}
		return lre;
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
		setStart(points[0]);
		setEnd(points[1]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#reset()
	 */
	public void reset() {
		object3D = new Object3D(2);
		super.reset();
	}

	public Object3D getObject3DParent() {
		return object3DParent;
	}

	public void setObject3DParent(Object3D object3DParent) {
		this.object3DParent = object3DParent;
	}

}
