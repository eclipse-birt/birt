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
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;

/**
 * A rendering event type for rendering 3D text object.
 */
public final class Text3DRenderEvent extends TextRenderEvent implements I3DRenderEvent {

	private static final long serialVersionUID = 3083777028665416663L;

	private transient Object3D object3D;

	/**
	 * The constructor.
	 */
	public Text3DRenderEvent(Object oSource) {
		super(oSource);
	}

	/**
	 * Sets the 3D location of the text.
	 */
	public void setLocation3D(Location3D loc) {
		this.object3D = new Object3D(loc);
	}

	/**
	 * @return Returns the 3D location of the text.
	 */
	public Location3D getLocation3D() {
		return object3D.getLocation3D()[0];
	}

	/**
	 * Sets the 3D block bounds of the text.
	 */
	public void setBlockBounds3D(Location3D[] loa) {
		this.object3D = new Object3D(loa);
	}

	/**
	 * @return Returns the 3D block bounds of the text.
	 */
	public Location3D[] getBlockBounds3D() {
		return object3D.getLocation3D();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#copy()
	 */
	public PrimitiveRenderEvent copy() {
		Text3DRenderEvent tre = new Text3DRenderEvent(source);
		tre.setAction(_iAction);
		tre.setTextPosition(_iTextPosition);
		if (_la != null) {
			tre.setLabel(goFactory.copyOf(_la));
		}
		if (object3D != null) {
			tre.object3D = new Object3D(object3D);
		}
		if (_taBlock != null) {
			tre.setBlockAlignment(goFactory.copyOf(_taBlock));
		}
		return tre;
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

		if (_iAction == TextRenderEvent.RENDER_TEXT_IN_BLOCK) {
			_iAction = TextRenderEvent.RENDER_TEXT_AT_LOCATION;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.TextRenderEvent#reset()
	 */
	public void reset() {
		object3D.reset();
	}

}
