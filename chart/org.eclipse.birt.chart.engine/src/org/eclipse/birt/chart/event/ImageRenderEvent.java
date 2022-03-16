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

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Position;

/**
 * A rendering event type for rendering Image object.
 */
public class ImageRenderEvent extends PrimitiveRenderEvent {

	private static final long serialVersionUID = -5467310111862210812L;

	protected transient Image img;

	protected transient Location loc;

	protected Position pos;

	protected int width = -1;

	protected int height = -1;

	protected boolean stretch = false;

	/**
	 * The constructor.
	 */
	public ImageRenderEvent(Object oSource) {
		super(oSource);
	}

	/**
	 * Sets the location of the image.
	 */
	public void setLocation(Location loc) {
		this.loc = loc;
	}

	/**
	 * Sets the content of the image.
	 */
	public void setImage(Image img) {
		this.img = img;
	}

	/**
	 * Sets the position of the image.
	 */
	public void setPosition(Position pos) {
		this.pos = pos;
	}

	/**
	 * @return Returns the location of the image.
	 */
	public Location getLocation() {
		return loc;
	}

	/**
	 * @return Returns the content of the image.
	 */
	public Image getImage() {
		return img;
	}

	/**
	 * @return Returns the position of the image.
	 */
	public Position getPosition() {
		return pos;
	}

	/**
	 * Sets the width hint of the image.
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Sets the height hint of the image.
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return Returns the width hint of the image.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return Returns the height hint of the image.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets if stretch the image.
	 */
	public void setStretch(boolean val) {
		this.stretch = val;
	}

	/**
	 * @return Returns if stretch the image.
	 */
	public boolean isStretch() {
		return stretch;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#copy()
	 */
	@Override
	public PrimitiveRenderEvent copy() throws ChartException {
		ImageRenderEvent ire = new ImageRenderEvent(source);

		if (loc != null) {
			ire.setLocation(loc.copyInstance());
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
	 * @see
	 * org.eclipse.birt.chart.event.PrimitiveRenderEvent#fill(org.eclipse.birt.chart
	 * .device.IDeviceRenderer)
	 */
	@Override
	public void fill(IDeviceRenderer idr) throws ChartException {
		draw(idr);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.event.PrimitiveRenderEvent#draw(org.eclipse.birt.chart
	 * .device.IDeviceRenderer)
	 */
	@Override
	public void draw(IDeviceRenderer idr) throws ChartException {
		idr.drawImage(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.event.ChartEvent#reset()
	 */
	@Override
	public void reset() {
		this.loc = null;
		this.pos = null;
	}
}
