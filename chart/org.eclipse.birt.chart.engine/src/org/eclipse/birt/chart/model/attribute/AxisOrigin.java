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
import org.eclipse.birt.chart.model.data.DataElement;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Axis
 * Origin</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 *
 * This type represents the intersection point for an axis.
 *
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.AxisOrigin#getType
 * <em>Type</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.AxisOrigin#getValue
 * <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getAxisOrigin()
 * @model
 * @generated
 */
public interface AxisOrigin extends IChartObject {

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute. The default value
	 * is <code>"Min"</code>. The literals are from the enumeration
	 * {@link org.eclipse.birt.chart.model.attribute.IntersectionType}. <!--
	 * begin-user-doc --> Gets the type of the origin value. If it is 'Min' or
	 * 'Max', its value is determined at runtime. <!-- end-user-doc --> <!--
	 * begin-model-doc -->
	 *
	 * Defines the type of origin. This determines whether and how the origin value
	 * is to be used.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.IntersectionType
	 * @see #isSetType()
	 * @see #unsetType()
	 * @see #setType(IntersectionType)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getAxisOrigin_Type()
	 * @model default="Min" unique="false" unsettable="true" required="true"
	 * @generated
	 */
	IntersectionType getType();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.AxisOrigin#getType
	 * <em>Type</em>}' attribute. <!-- begin-user-doc --> Gets the type of the
	 * origin value. If it is 'Min' or 'Max', its value is determined at runtime.
	 * <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.IntersectionType
	 * @see #isSetType()
	 * @see #unsetType()
	 * @see #getType()
	 * @generated
	 */
	void setType(IntersectionType value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.AxisOrigin#getType
	 * <em>Type</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isSetType()
	 * @see #getType()
	 * @see #setType(IntersectionType)
	 * @generated
	 */
	void unsetType();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.model.attribute.AxisOrigin#getType
	 * <em>Type</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @return whether the value of the '<em>Type</em>' attribute is set.
	 * @see #unsetType()
	 * @see #getType()
	 * @see #setType(IntersectionType)
	 * @generated
	 */
	boolean isSetType();

	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute. <!--
	 * begin-user-doc --> Gets the value of the intersection point on the primary
	 * axis perpendicular to this one. Getting its contents makes sence only if the
	 * type for origin is set to 'Value'. <!-- end-user-doc --> <!-- begin-model-doc
	 * -->
	 *
	 * Specifies the actual value of the axis origin.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(Object)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getAxisOrigin_Value()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType"
	 *        required="true"
	 * @generated
	 */
	DataElement getValue();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.AxisOrigin#getValue
	 * <em>Value</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @param value the new value of the '<em>Value</em>' containment reference.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(DataElement value);

	/**
	 * @generated
	 */
	@Override
	AxisOrigin copyInstance();

} // AxisOrigin
