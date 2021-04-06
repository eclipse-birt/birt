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

import java.util.ArrayList;

import org.eclipse.birt.chart.computation.Object3D;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;

/**
 * A rendering event type for rendering 3D Oval object.
 */
public class Oval3DRenderEvent extends OvalRenderEvent implements I3DRenderEvent {

	private static final long serialVersionUID = 3249838045689532033L;

	private transient Object3D object3D;

	/**
	 * The constructor.
	 */
	public Oval3DRenderEvent(Object oSource) {
		super(oSource);
	}

	/**
	 * Set 3D location of the event.
	 */
	public void setLocation3D(Location3D[] loa) {
		object3D = new Object3D(loa);
	}

	/**
	 * @return Returns the 3D location of the event.
	 */
	public Location3D[] getLocation3D() {
		return object3D.getLocation3D();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#copy()
	 */
	public PrimitiveRenderEvent copy() {
		final Oval3DRenderEvent ore = new Oval3DRenderEvent(source);
		if (object3D != null) {
			ore.object3D = new Object3D(object3D);
		}

		ore.setOutline(goFactory.copyOf(_lia));

		ore.setBackground(goFactory.copyOf(_ifBackground));
		return ore;
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

		ArrayList<Location> lst = new ArrayList<Location>();
		for (int i = 0; i < points.length; i++) {
			if (!lst.contains(points[i])) {
				lst.add(points[i]);
			}
		}
		points = lst.toArray(new Location[lst.size()]);

		setBounds(goFactory.createBounds(points[0].getX(), points[0].getY(), points[2].getX() - points[0].getX(),
				points[2].getY() - points[0].getY()));
	}
}
