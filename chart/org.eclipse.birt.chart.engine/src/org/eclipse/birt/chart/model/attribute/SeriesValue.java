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

package org.eclipse.birt.chart.model.attribute;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Series
 * Value</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * SeriesValue extends type ActionValue to devote itself to 'Toggle_Visibility' actions.
 * 			
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.SeriesValue#getName <em>Name</em>}</li>
 * </ul>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getSeriesValue()
 * @model extendedMetaData="name='SeriesValue' kind='elementOnly'"
 * @generated
 */
public interface SeriesValue extends ActionValue {

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Attribute
	 * "Name" specifies the name for the series definition.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getSeriesValue_Name()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='element' name='Name'"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.attribute.SeriesValue#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * @generated
	 */
	@Override
	SeriesValue copyInstance();

} // SeriesValue
