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

package org.eclipse.birt.report.engine.extension;

import org.eclipse.birt.report.engine.ir.EngineIRConstants;

/**
 * Defines a helper class for passing size information from the extension
 */
public class Size {
	public final static String UNITS_CM = EngineIRConstants.UNITS_CM;
	public final static String UNITS_IN = EngineIRConstants.UNITS_IN;
	public final static String UNITS_MM = EngineIRConstants.UNITS_MM;
	public final static String UNITS_PT = EngineIRConstants.UNITS_PT;
	public final static String UNITS_PX = EngineIRConstants.UNITS_PX;

	protected float width;
	protected float height;
	protected String unit;

	/**
	 * @return Returns the unit.
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit The unit to set.
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * @return Returns the height.
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * @param height The height to set.
	 */
	public void setHeight(float height) {
		this.height = height;
	}

	/**
	 * @return Returns the width.
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * @param width The width to set.
	 */
	public void setWidth(float width) {
		this.width = width;
	}
}
