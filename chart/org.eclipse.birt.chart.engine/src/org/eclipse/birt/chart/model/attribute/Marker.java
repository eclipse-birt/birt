/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.model.attribute;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Marker</b></em>'. <!-- end-user-doc -->
 * 
 * <!-- begin-model-doc -->
 * 
 * This type represents the value marker for a line or scatter series.
 * 
 * <!-- end-model-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Marker#getType <em>Type</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Marker#getSize <em>Size</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Marker#isVisible <em>Visible</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getMarker()
 * @model
 * @generated
 */
public interface Marker extends EObject
{

    /**
     * Returns the value of the '<em><b>Type</b></em>' attribute. The default value is <code>"Crosshair"</code>.
     * The literals are from the enumeration {@link org.eclipse.birt.chart.model.attribute.MarkerType}. <!--
     * begin-user-doc --> Gets the type of marker. <!-- end-user-doc --> <!-- begin-model-doc -->
     * 
     * Specifies the type of marker.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Type</em>' attribute.
     * @see org.eclipse.birt.chart.model.attribute.MarkerType
     * @see #isSetType()
     * @see #unsetType()
     * @see #setType(MarkerType)
     * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getMarker_Type()
     * @model default="Crosshair" unique="false" unsettable="true" required="true"
     * @generated
     */
    MarkerType getType();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.attribute.Marker#getType <em>Type</em>}' attribute.
     * <!-- begin-user-doc --> Sets the type of marker. <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Type</em>' attribute.
     * @see org.eclipse.birt.chart.model.attribute.MarkerType
     * @see #isSetType()
     * @see #unsetType()
     * @see #getType()
     * @generated
     */
    void setType(MarkerType value);

    /**
     * Unsets the value of the '{@link org.eclipse.birt.chart.model.attribute.Marker#getType <em>Type</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #isSetType()
     * @see #getType()
     * @see #setType(MarkerType)
     * @generated
     */
    void unsetType();

    /**
     * Returns whether the value of the '{@link org.eclipse.birt.chart.model.attribute.Marker#getType <em>Type</em>}'
     * attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return whether the value of the '<em>Type</em>' attribute is set.
     * @see #unsetType()
     * @see #getType()
     * @see #setType(MarkerType)
     * @generated
     */
    boolean isSetType();

    /**
     * Returns the value of the '<em><b>Size</b></em>' attribute. <!-- begin-user-doc --> Gets the size of the
     * marker in the chart (as a percentage). <!-- end-user-doc --> <!-- begin-model-doc -->
     * 
     * Specifies the size of the marker in the chart (as a percentage).
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Size</em>' attribute.
     * @see #isSetSize()
     * @see #unsetSize()
     * @see #setSize(int)
     * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getMarker_Size()
     * @model unique="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Int" required="true"
     * @generated
     */
    int getSize();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.attribute.Marker#getSize <em>Size</em>}' attribute.
     * <!-- begin-user-doc --> Sets the size of the marker in the chart (as a percentage). <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Size</em>' attribute.
     * @see #isSetSize()
     * @see #unsetSize()
     * @see #getSize()
     * @generated
     */
    void setSize(int value);

    /**
     * Unsets the value of the '{@link org.eclipse.birt.chart.model.attribute.Marker#getSize <em>Size</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #isSetSize()
     * @see #getSize()
     * @see #setSize(int)
     * @generated
     */
    void unsetSize();

    /**
     * Returns whether the value of the '{@link org.eclipse.birt.chart.model.attribute.Marker#getSize <em>Size</em>}'
     * attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return whether the value of the '<em>Size</em>' attribute is set.
     * @see #unsetSize()
     * @see #getSize()
     * @see #setSize(int)
     * @generated
     */
    boolean isSetSize();

    /**
     * Returns the value of the '<em><b>Visible</b></em>' attribute. <!-- begin-user-doc --> Gets whether the
     * marker will be rendered. <!-- end-user-doc --> <!-- begin-model-doc -->
     * 
     * Specifies whether the marker is to be rendered.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Visible</em>' attribute.
     * @see #isSetVisible()
     * @see #unsetVisible()
     * @see #setVisible(boolean)
     * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getMarker_Visible()
     * @model unique="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean" required="true"
     * @generated
     */
    boolean isVisible();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.attribute.Marker#isVisible <em>Visible</em>}'
     * attribute. <!-- begin-user-doc --> Specifies whether the marker is to be rendered. <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Visible</em>' attribute.
     * @see #isSetVisible()
     * @see #unsetVisible()
     * @see #isVisible()
     * @generated
     */
    void setVisible(boolean value);

    /**
     * Unsets the value of the '{@link org.eclipse.birt.chart.model.attribute.Marker#isVisible <em>Visible</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #isSetVisible()
     * @see #isVisible()
     * @see #setVisible(boolean)
     * @generated
     */
    void unsetVisible();

    /**
     * Returns whether the value of the '
     * {@link org.eclipse.birt.chart.model.attribute.Marker#isVisible <em>Visible</em>}' attribute is set. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return whether the value of the '<em>Visible</em>' attribute is set.
     * @see #unsetVisible()
     * @see #isVisible()
     * @see #setVisible(boolean)
     * @generated
     */
    boolean isSetVisible();

} // Marker
