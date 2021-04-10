/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.script.api.data;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Number
 * Data Element</b></em>'. <!-- end-user-doc -->
 * 
 * <!-- begin-model-doc -->
 * 
 * This type defines a single element of numerical data.
 * 
 * <!-- end-model-doc -->
 */

public interface INumberDataElement extends IDataElement {

	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Holds a single numeric value as a double.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(double)
	 */
	double getValue();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.NumberDataElement#getValue
	 * <em>Value</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 */
	void setValue(double value);

}
