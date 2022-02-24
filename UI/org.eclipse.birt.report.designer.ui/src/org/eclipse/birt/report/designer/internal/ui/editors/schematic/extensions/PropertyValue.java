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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.extensions;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.ui.extensions.IPropertyValue;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;

/**
 */
public class PropertyValue implements IPropertyValue {
	private String stringValue;

	public PropertyValue(final String stringValue) {
		this.stringValue = stringValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.extensions.IPropertyValue#getStringValue(
	 * )
	 */
	public String getStringValue() {
		return stringValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IPropertyValue#getUnit()
	 */
	public String getUnit() {
		if (stringValue == null || stringValue.equals("")) //$NON-NLS-1$
			return stringValue;
		try {
			DimensionValue dimensionValue = DimensionValue.parse(stringValue);
			return dimensionValue.getUnits();
		} catch (PropertyValueException e) {
			ExceptionHandler.handle(e);
		}
		return ""; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.extensions.IPropertyValue#getMeasureValue
	 * ()
	 */
	public String getMeasureValue() {
		if (stringValue == null || stringValue.equals("")) //$NON-NLS-1$
			return stringValue;
		try {
			DimensionValue dimensionValue = DimensionValue.parse(stringValue);
			return Double.toString(dimensionValue.getMeasure());
		} catch (PropertyValueException e) {
			ExceptionHandler.handle(e);
		}
		return ""; //$NON-NLS-1$
	}

}
