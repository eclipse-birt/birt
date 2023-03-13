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

import org.eclipse.birt.chart.model.IChartObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Extended
 * Property</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> ExtendedProperty is a name-value pair which is
 * specialized for representing an extended property entry that is created to
 * hold data for minor extensions to a chart.
 *
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.ExtendedProperty#getName
 * <em>Name</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.ExtendedProperty#getValue
 * <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getExtendedProperty()
 * @model extendedMetaData="name='ExtendedProperty' kind='elementOnly'"
 * @extends IChartObject
 * @generated
 */
public interface ExtendedProperty extends IChartObject {

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Attribute
	 * "Name" specifies the unique name for the property entry.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getExtendedProperty_Name()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='element' name='Name'"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.ExtendedProperty#getName
	 * <em>Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Attribute
	 * "Value" specifies the value for the property entry.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(String)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getExtendedProperty_Value()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='element' name='Value'"
	 * @generated
	 */
	String getValue();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.ExtendedProperty#getValue
	 * <em>Value</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(String value);

	/**
	 * @generated
	 */
	@Override
	ExtendedProperty copyInstance();

} // ExtendedProperty
