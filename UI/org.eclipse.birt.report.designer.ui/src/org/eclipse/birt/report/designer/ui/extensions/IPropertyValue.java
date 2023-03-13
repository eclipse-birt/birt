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
package org.eclipse.birt.report.designer.ui.extensions;

/**
 * Holds a property value with helper functions to extract Unit and Measure
 * values
 */
public interface IPropertyValue {
	/**
	 * @return the value of the property
	 */
	String getStringValue();

	/**
	 * This applies to properties with Units
	 *
	 * @return the unit of the Property value if any
	 */
	String getUnit();

	/**
	 * This applies to properties with Units
	 *
	 * @return the measure value (without the Unit)
	 */
	String getMeasureValue();
}
