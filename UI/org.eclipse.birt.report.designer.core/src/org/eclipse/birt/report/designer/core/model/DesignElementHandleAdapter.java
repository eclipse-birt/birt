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

package org.eclipse.birt.report.designer.core.model;

import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

/**
 * Adapter class to adapt model handle. This adapter provides convenience
 * methods to GUI requirement DesignElementHandleAdapter responds to model
 * DesignElmentHandle
 * 
 * 
 */
public abstract class DesignElementHandleAdapter {

	private DesignElementHandle elementHandle;

	private IModelAdapterHelper helper;

	/**
	 * constructor
	 * 
	 * @param element Design element handle
	 * @param mark    Helper mark
	 */
	public DesignElementHandleAdapter(DesignElementHandle element, IModelAdapterHelper mark) {
		this.elementHandle = element;
		this.helper = mark;
	}

	/**
	 * Gets the Children iterator. This children relationship is determined by GUI
	 * requirement. This is not the model children relationship.
	 * 
	 * @return Children iterator
	 */
	public List getChildren() {
		return Collections.EMPTY_LIST;

	}

	/**
	 * Gets display name of report element
	 * 
	 * @return Display name
	 */

	public String getDisplayName() {
		return getHandle().getDisplayLabel();
	}

	/**
	 * @return Returns the handle.
	 */
	public DesignElementHandle getHandle() {
		return elementHandle;
	}

	/**
	 * Gets the handle of the report design
	 * 
	 * @return Returns the handle of the report design
	 */
	public ReportDesignHandle getReportDesignHandle() {
		return (ReportDesignHandle) elementHandle.getModuleHandle();
	}

	/**
	 * Gets the handle of the moudule handle
	 * 
	 * @return The module hanle
	 */
	public ModuleHandle getModuleHandle() {
		if (elementHandle != null) {
			return elementHandle.getModuleHandle();
		}
		return null;
	}

	/**
	 * Reloads all properties from model
	 */
	public void reload() {

	}

	/**
	 * @return true if the cached data is dirty
	 */
	public boolean checkDirty() {
		if (helper != null) {
			return helper.isDirty();
		}
		return true;
	}

	/**
	 * @return
	 */
	public IModelAdapterHelper getModelAdaptHelper() {
		return helper;
	}

	/**
	 * @return
	 */
	protected Dimension getDefaultSize() {
		return helper.getPreferredSize().shrink(helper.getInsets().getWidth(), helper.getInsets().getHeight());
	}

	/**
	 * Sets the handle this adapter
	 * 
	 * @param handle
	 */
	public void setElementHandle(DesignElementHandle handle) {
		this.elementHandle = handle;
	}

	/**
	 * Starts a transaction on the current activity stack.
	 * 
	 * @param name
	 */
	public void transStar(String name) {
		CommandStack stack = getModuleHandle().getCommandStack();
		// start trans
		stack.startTrans(name);
	}

	/**
	 * Ends a transaction on the current activity stack
	 */
	public void transEnd() {
		CommandStack stack = getModuleHandle().getCommandStack();
		stack.commit();
	}

	/**
	 * rollback the transaction;
	 */
	public void rollBack() {
		CommandStack stack = getModuleHandle().getCommandStack();
		stack.rollbackAll();
	}

	/**
	 * Get the padding of the current element.
	 * 
	 * @param retValue The padding value of the current element.
	 * @return The padding's new value of the current element.
	 */
	public Insets getPadding(Insets retValue) {
		return DEUtil.getPadding(getHandle(), retValue);
	}

	public Insets getMargin(Insets retValue) {
		return getMargin(retValue, new Dimension(-1, -1));
	}

	/**
	 * Get the margin of the current element.
	 * 
	 * @param retValue The margin value of the current element.
	 * @return The maring's new value of the current element.
	 */
	public Insets getMargin(Insets retValue, Dimension size) {
		if (retValue == null) {
			retValue = new Insets();
		} else {
			retValue = new Insets(retValue);
		}

		int fontSize = DEUtil.getFontSizeIntValue(getHandle());

		double px = 0;
		Object prop = getHandle().getProperty(StyleHandle.MARGIN_TOP_PROP);
		if (!DesignChoiceConstants.MARGIN_AUTO.equals(prop)) {
			px = DEUtil.convertToPixel(prop, fontSize);
		}

		double py = 0;
		prop = getHandle().getProperty(StyleHandle.MARGIN_BOTTOM_PROP);
		if (!DesignChoiceConstants.MARGIN_AUTO.equals(prop)) {
			py = DEUtil.convertToPixel(prop, fontSize);
		}

		retValue.top = (int) px;
		retValue.bottom = (int) py;

		px = py = 0;
		prop = getHandle().getProperty(StyleHandle.MARGIN_LEFT_PROP);
		if (!DesignChoiceConstants.MARGIN_AUTO.equals(prop)) {
			if (isPercentageValue(prop) && size.width > 0) {
				px = getMeasure(prop) * size.width / 100;
			} else {
				px = DEUtil.convertToPixel(prop, fontSize);
			}
		}

		prop = getHandle().getProperty(StyleHandle.MARGIN_RIGHT_PROP);
		if (!DesignChoiceConstants.MARGIN_AUTO.equals(prop)) {
			if (isPercentageValue(prop) && size.width > 0) {
				py = getMeasure(prop) * size.width / 100;
			} else {
				py = DEUtil.convertToPixel(prop, fontSize);
			}
		}

		retValue.left = (int) px;
		retValue.right = (int) py;

		return retValue;
	}

	private boolean isPercentageValue(Object object) {
		if (object instanceof DimensionValue) {
			DimensionValue dimension = (DimensionValue) object;
			String units = dimension.getUnits();
			if (DesignChoiceConstants.UNITS_PERCENTAGE.equals(units)) {
				return true;
			}
		}
		return false;
	}

	private double getMeasure(Object object) {
		if (object instanceof DimensionValue) {
			DimensionValue dimension = (DimensionValue) object;
			double measure = dimension.getMeasure();
			return measure;
		}
		return 0;
	}

	/**
	 * @param handle
	 * @return
	 */
	public int getBackgroundImageWidth(DesignElementHandle handle, Dimension size, Image image) {

		DimensionHandle obj = handle.getDimensionProperty(StyleHandle.BACKGROUND_SIZE_WIDTH);
		if (obj == null || obj.getUnits() == null || obj.getUnits().length() == 0) {
			if (image == null) {
				return 0;
			}
			String str = handle.getStringProperty(StyleHandle.BACKGROUND_SIZE_WIDTH);
			if (DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN.equals(str)) {
				Dimension imageSize = new Dimension(image);
				if (((double) imageSize.width / ((double) imageSize.height)) > ((double) size.width
						/ ((double) size.height))) {
					return size.width;
				} else {
					double value = ((double) imageSize.width * ((double) size.height) / ((double) imageSize.height));
					return (int) value;
				}
			} else if (DesignChoiceConstants.BACKGROUND_SIZE_COVER.equals(str)) {
				Dimension imageSize = new Dimension(image);
				if (((double) imageSize.width / ((double) imageSize.height)) > ((double) size.width
						/ ((double) size.height))) {
					double value = ((double) imageSize.width * ((double) size.height) / ((double) imageSize.height));
					return (int) value;

				} else {
					return size.width;
				}
				// return size.width;
			}

			return 0;
		}
		int fontSize = DEUtil.getFontSizeIntValue(getHandle());

		double px = 0;

		px = DEUtil.convertToPixel(obj, fontSize);

		return (int) px;
	}

	/**
	 * @param handle
	 * @return
	 */
	public int getBackgroundImageHeight(DesignElementHandle handle, Dimension size, Image image) {
		DimensionHandle obj = handle.getDimensionProperty(StyleHandle.BACKGROUND_SIZE_HEIGHT);
		if (obj == null || obj.getUnits() == null || obj.getUnits().length() == 0) {
			if (image == null) {
				return 0;
			}
			String str = handle.getStringProperty(StyleHandle.BACKGROUND_SIZE_WIDTH);
			if (DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN.equals(str)) {
				Dimension imageSize = new Dimension(image);
				if (((double) imageSize.width / ((double) imageSize.height)) > ((double) size.width
						/ ((double) size.height))) {
					double value = ((double) imageSize.height * ((double) size.width) / ((double) imageSize.width));
					return (int) value;
				} else {
					return size.height;
				}
			} else if (DesignChoiceConstants.BACKGROUND_SIZE_COVER.equals(str)) {
				Dimension imageSize = new Dimension(image);
				if (((double) imageSize.width / ((double) imageSize.height)) > ((double) size.width
						/ ((double) size.height))) {
					return size.height;

				} else {
					double value = ((double) imageSize.height * ((double) size.width) / ((double) imageSize.width));
					return (int) value;
				}
				// return size.height;
			}
			return 0;
		}
		int fontSize = DEUtil.getFontSizeIntValue(getHandle());

		double py = 0;

		py = DEUtil.convertToPixel(obj, fontSize);

		return (int) py;
	}

	/**
	 * Get the foreground color.
	 * 
	 * @param handle The handle of design element.
	 * @return fore ground color
	 */
	public int getForegroundColor(DesignElementHandle handle) {
		Object obj = handle.getProperty(StyleHandle.COLOR_PROP);

		if (obj == null) {
			// return 0x0;
			return SWT.COLOR_LIST_FOREGROUND;
		}

		// TODO optimize to not get value twice
		int color = handle.getPropertyHandle(StyleHandle.COLOR_PROP).getIntValue();

//		if ( obj instanceof String )
//		{
//			return ColorUtil.parseColor( (String) obj );
//		}
//
//		return ( (Integer) obj ).intValue( );
		return color;
	}

	/**
	 * Get the background color.
	 * 
	 * @param handle The handle of design element.
	 * @return back ground color
	 */
	public int getBackgroundColor(DesignElementHandle handle) {
		Object obj = handle.getProperty(StyleHandle.BACKGROUND_COLOR_PROP);

		if (obj == null) {
			// return 0xFFFFFF;
			return SWT.COLOR_LIST_BACKGROUND;
		}

		// TODO optimize to not get value twice
		int color = handle.getPropertyHandle(StyleHandle.BACKGROUND_COLOR_PROP).getIntValue();

//		if ( obj instanceof String )
//		{
//			return ColorUtil.parseColor( (String) obj );
//		}
//
//		return ( (Integer) obj ).intValue( );
		return color;
	}

	/**
	 * Get background image.
	 * 
	 * @param handle The handle of design element.
	 * @return background image
	 */
	public String getBackgroundImage(DesignElementHandle handle) {
		return handle.getStringProperty(StyleHandle.BACKGROUND_IMAGE_PROP);
	}

	/**
	 * Get background position.
	 * 
	 * @param handle The handle of design element.
	 * @return background position
	 */
	public Object[] getBackgroundPosition(DesignElementHandle handle) {
		Object x = null;
		Object y = null;

		if (handle != null) {
			Object px = handle.getProperty(StyleHandle.BACKGROUND_POSITION_X_PROP);
			Object py = handle.getProperty(StyleHandle.BACKGROUND_POSITION_Y_PROP);

			if (px instanceof String) {
				x = px;
			} else if (px instanceof DimensionValue) {
				// {0%,0%}
				if (DesignChoiceConstants.UNITS_PERCENTAGE.equals(((DimensionValue) px).getUnits())) {
					x = px;
				} else {
					// {1cm,1cm}
					x = Integer.valueOf((int) DEUtil.convertoToPixel(px));
				}
			}

			if (py instanceof String) {
				y = py;
			} else if (py instanceof DimensionValue) {
				// {0%,0%}
				if (DesignChoiceConstants.UNITS_PERCENTAGE.equals(((DimensionValue) py).getUnits())) {
					y = py;
				} else {
					// {1cm,1cm}
					y = Integer.valueOf((int) DEUtil.convertoToPixel(py));
				}
			}
		}
		return new Object[] { x, y };
	}

	/**
	 * Get background repeat property.
	 * 
	 * @param handle The handle of design element.
	 * @return background repeat property
	 */
	public int getBackgroundRepeat(DesignElementHandle handle) {
		return getRepeat(handle.getStringProperty(StyleHandle.BACKGROUND_REPEAT_PROP));
	}

	/**
	 * Get the position from string
	 * 
	 * @param position The given string
	 * @return The position
	 */
	public static int getPosition(String position) {
		if (DesignChoiceConstants.BACKGROUND_POSITION_LEFT.equals(position)) {
			return PositionConstants.WEST;
		}
		if (DesignChoiceConstants.BACKGROUND_POSITION_RIGHT.equals(position)) {
			return PositionConstants.EAST;
		}
		if (DesignChoiceConstants.BACKGROUND_POSITION_TOP.equals(position)) {
			return PositionConstants.NORTH;
		}
		if (DesignChoiceConstants.BACKGROUND_POSITION_BOTTOM.equals(position)) {
			return PositionConstants.SOUTH;
		}
		return PositionConstants.CENTER;
	}

	/**
	 * Get reppeat value
	 * 
	 * @param repeat Given string
	 * @return The repeat value
	 */
	private int getRepeat(String repeat) {
		if (DesignChoiceConstants.BACKGROUND_REPEAT_REPEAT_X.equals(repeat)) {
			return 1;
		} else if (DesignChoiceConstants.BACKGROUND_REPEAT_REPEAT_Y.equals(repeat)) {
			return 2;
		} else if (DesignChoiceConstants.BACKGROUND_REPEAT_REPEAT.equals(repeat)) {
			return 3;
		}
		return 0;
	}

	/**
	 * @param handle
	 * @return
	 */
	public boolean isChildren(DesignElementHandle handle) {
		while (handle != null) {
			if (handle.equals(elementHandle)) {
				return true;
			} else {
				handle = handle.getContainer();
			}
		}
		return false;

	}
}
