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
package org.eclipse.birt.report.designer.ui.extensions;

/**
 * Holds a property value with helper functions to extract Unit and Measure
 * values
 */
public interface IPropertyValue {
	/**
	 * @return the value of the property
	 */
	public String getStringValue();

	/**
	 * This applies to properties with Units
	 * 
	 * @return the unit of the Property value if any
	 */
	public String getUnit();

	/**
	 * This applies to properties with Units
	 * 
	 * @return the measure value (without the Unit)
	 */
	public String getMeasureValue();
}
