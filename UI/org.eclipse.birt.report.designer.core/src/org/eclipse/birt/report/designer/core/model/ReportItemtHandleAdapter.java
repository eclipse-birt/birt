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

package org.eclipse.birt.report.designer.core.model;

import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.MetricUtility;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Adapter class to adapt model handle. This adapter provides convenience
 * methods to GUI requirement ReportItemtHandleAdapter responds to model
 * ReportItemtHandle.
 */

public abstract class ReportItemtHandleAdapter extends DesignElementHandleAdapter {

	/**
	 * Constructor
	 * 
	 * @param handle
	 */
	public ReportItemtHandleAdapter(ReportItemHandle handle, IModelAdapterHelper mark) {
		super(handle, mark);
	}

	/**
	 * get the location Converts the value from other units to px
	 * 
	 * @return location point
	 */
	public Point getLocation() {
		DimensionHandle handle = getReportItemHandle().getX();
		int x = 0;
		if (DesignChoiceConstants.UNITS_PX.equals(handle.getUnits())) {
			x = (int) handle.getMeasure();
		}

		double px = DEUtil.convertToValue(handle, DesignChoiceConstants.UNITS_IN);

		handle = getReportItemHandle().getY();

		int y = 0;
		if (DesignChoiceConstants.UNITS_PX.equals(handle.getUnits())) {
			y = (int) handle.getMeasure();
		}
		double py = DEUtil.convertToValue(handle, DesignChoiceConstants.UNITS_IN);
		Point p = MetricUtility.inchToPixel(px, py);
		if (x != 0) {
			p.x = x;
		}
		if (y != 0) {
			p.y = y;
		}
		return p;
	}

	/**
	 * Set the location.
	 * 
	 * @param location
	 * @throws SemanticException
	 */
	public void setLocation(Point location) throws SemanticException {
		getReportItemHandle().getX()
				.setStringValue(Integer.valueOf(location.x).toString() + DesignChoiceConstants.UNITS_PX);
		getReportItemHandle().getY()
				.setStringValue(Integer.valueOf(location.y).toString() + DesignChoiceConstants.UNITS_PX);
	}

	/**
	 * get the size Converts the value from other units to px
	 * 
	 * @return size dimension
	 */
	public Dimension getSize() {
		DimensionHandle handle = getReportItemHandle().getWidth();

		int px = (int) DEUtil.convertoToPixel(handle);

		handle = getReportItemHandle().getHeight();

		int py = (int) DEUtil.convertoToPixel(handle);

		return new Dimension(px, py);
	}

	/**
	 * Sets the size.
	 * 
	 * @param size the dimension size
	 * @throws SemanticException the semantic exception
	 */
	public void setSize(Dimension size) throws SemanticException {
		DimensionValue dimensionValue;

		if (size.width >= 0) {
			double width = MetricUtility.pixelToPixelInch(size.width);

			dimensionValue = new DimensionValue(width, DesignChoiceConstants.UNITS_IN);

			getReportItemHandle().getWidth().setValue(dimensionValue);
		}

		if (size.height >= 0) {
			double height = MetricUtility.pixelToPixelInch(size.height);

			dimensionValue = new DimensionValue(height, DesignChoiceConstants.UNITS_IN);

			getReportItemHandle().getHeight().setValue(dimensionValue);
		}
	}

	/**
	 * Gets the bounds
	 * 
	 * @return the bounds
	 */
	public Rectangle getbounds() {
		return new Rectangle(getLocation().x, getLocation().y, getSize().width, getSize().height);
	}

	/**
	 * Sets bounds.
	 * 
	 * @param bounds the bounds
	 * @throws SemanticException the semantic exception
	 */
	public void setBounds(Rectangle bounds) throws SemanticException {
		setSize(bounds.getSize());
		setLocation(new Point(bounds.getLocation().x, bounds.getLocation().y));
	}

	/**
	 * Gets the reportItemHandle.
	 * 
	 * @return the report item handle.
	 */
	public ReportItemHandle getReportItemHandle() {
		return (ReportItemHandle) getHandle();
	}

}