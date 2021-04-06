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
 * A rendering event type for rendering 3D Image object.
 */
public final class Image3DRenderEvent extends ImageRenderEvent implements I3DRenderEvent {

	private static final long serialVersionUID = -5027476689319210090L;

	private transient Object3D object3D;

	/**
	 * The constructor.
	 */
	public Image3DRenderEvent(Object oSource) {
		super(oSource);
	}

	/**
	 * Sets the 3D location of the image.
	 */
	public void setLocation3D(Location3D lo) {
		object3D = new Object3D(lo);
	}

	/**
	 * @return Returns the 3D location of the image.
	 */
	public Location3D getLocation3D() {
		return object3D.getLocation3D()[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#copy()
	 */
	public PrimitiveRenderEvent copy() {
		Image3DRenderEvent ire = new Image3DRenderEvent(source);

		if (object3D != null) {
			ire.object3D = new Object3D(object3D);
		}

		if (img != null) {
			ire.setImage(goFactory.copyOf(img));
		}

		ire.setPosition(pos);
		ire.setWidth(width);
		ire.setHeight(height);
		ire.setStretch(stretch);

		return ire;
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
		setLocation(points[0]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.ImageRenderEvent#reset()
	 */
	public void reset() {
		this.object3D = null;
		super.reset();
	}
}
