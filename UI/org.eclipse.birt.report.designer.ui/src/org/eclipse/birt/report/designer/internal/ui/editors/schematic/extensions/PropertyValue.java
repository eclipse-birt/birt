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
