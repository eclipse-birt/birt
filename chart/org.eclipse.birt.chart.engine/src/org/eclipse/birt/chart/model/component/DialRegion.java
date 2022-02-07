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

package org.eclipse.birt.chart.model.component;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Dial
 * Region</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.component.DialRegion#getInnerRadius
 * <em>Inner Radius</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.DialRegion#getOuterRadius
 * <em>Outer Radius</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getDialRegion()
 * @model extendedMetaData="name='DialRegion' kind='elementOnly'"
 * @generated
 */
public interface DialRegion extends MarkerRange {

	/**
	 * Returns the value of the '<em><b>Inner Radius</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Specifies
	 * the percentage value of the inner radius of the dial region. <!--
	 * end-model-doc -->
	 * 
	 * @return the value of the '<em>Inner Radius</em>' attribute.
	 * @see #isSetInnerRadius()
	 * @see #unsetInnerRadius()
	 * @see #setInnerRadius(double)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getDialRegion_InnerRadius()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double"
	 *        extendedMetaData="kind='element' name='InnerRadius'"
	 * @generated
	 */
	double getInnerRadius();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.DialRegion#getInnerRadius
	 * <em>Inner Radius</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value the new value of the '<em>Inner Radius</em>' attribute.
	 * @see #isSetInnerRadius()
	 * @see #unsetInnerRadius()
	 * @see #getInnerRadius()
	 * @generated
	 */
	void setInnerRadius(double value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.DialRegion#getInnerRadius
	 * <em>Inner Radius</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #isSetInnerRadius()
	 * @see #getInnerRadius()
	 * @see #setInnerRadius(double)
	 * @generated
	 */
	void unsetInnerRadius();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.component.DialRegion#getInnerRadius
	 * <em>Inner Radius</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Inner Radius</em>' attribute is set.
	 * @see #unsetInnerRadius()
	 * @see #getInnerRadius()
	 * @see #setInnerRadius(double)
	 * @generated
	 */
	boolean isSetInnerRadius();

	/**
	 * Returns the value of the '<em><b>Outer Radius</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Specifies
	 * the percentage value of the outer radius of the dial region. <!--
	 * end-model-doc -->
	 * 
	 * @return the value of the '<em>Outer Radius</em>' attribute.
	 * @see #isSetOuterRadius()
	 * @see #unsetOuterRadius()
	 * @see #setOuterRadius(double)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getDialRegion_OuterRadius()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double"
	 *        extendedMetaData="kind='element' name='OuterRadius'"
	 * @generated
	 */
	double getOuterRadius();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.DialRegion#getOuterRadius
	 * <em>Outer Radius</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value the new value of the '<em>Outer Radius</em>' attribute.
	 * @see #isSetOuterRadius()
	 * @see #unsetOuterRadius()
	 * @see #getOuterRadius()
	 * @generated
	 */
	void setOuterRadius(double value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.DialRegion#getOuterRadius
	 * <em>Outer Radius</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #isSetOuterRadius()
	 * @see #getOuterRadius()
	 * @see #setOuterRadius(double)
	 * @generated
	 */
	void unsetOuterRadius();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.component.DialRegion#getOuterRadius
	 * <em>Outer Radius</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Outer Radius</em>' attribute is set.
	 * @see #unsetOuterRadius()
	 * @see #getOuterRadius()
	 * @see #setOuterRadius(double)
	 * @generated
	 */
	boolean isSetOuterRadius();

	/**
	 * @generated
	 */
	DialRegion copyInstance();

} // DialRegion
