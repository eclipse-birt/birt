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

package org.eclipse.birt.chart.model.component;

import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Marker Range</b></em>'. <!-- end-user-doc
 * -->
 * 
 * <!-- begin-model-doc -->
 * 
 * This type defines a marker area. It is intended for use as a range marker associated with an Axis.
 * 
 * <!-- end-model-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.component.MarkerRange#getOutline <em>Outline</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.MarkerRange#getFill <em>Fill</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.MarkerRange#getStartPosition <em>Start Position</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.MarkerRange#getEndPosition <em>End Position</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.MarkerRange#getAssociatedLabel <em>Associated Label</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.MarkerRange#getLabelPosition <em>Label Position</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getMarkerRange()
 * @model
 * @generated
 */
public interface MarkerRange extends EObject
{

    /**
     * Returns the value of the '<em><b>Outline</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc --> <!-- begin-model-doc -->
     * 
     * Specify the outline properties for the marker range.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Outline</em>' containment reference.
     * @see #setOutline(LineAttributes)
     * @see org.eclipse.birt.chart.model.component.ComponentPackage#getMarkerRange_Outline()
     * @model containment="true" resolveProxies="false" required="true"
     * @generated
     */
    LineAttributes getOutline();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.component.MarkerRange#getOutline <em>Outline</em>}'
     * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Outline</em>' containment reference.
     * @see #getOutline()
     * @generated
     */
    void setOutline(LineAttributes value);

    /**
     * Returns the value of the '<em><b>Fill</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc --> <!-- begin-model-doc -->
     * 
     * Specify the background for the marker range.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Fill</em>' containment reference.
     * @see #setFill(Fill)
     * @see org.eclipse.birt.chart.model.component.ComponentPackage#getMarkerRange_Fill()
     * @model containment="true" resolveProxies="false" required="true"
     * @generated
     */
    Fill getFill();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.component.MarkerRange#getFill <em>Fill</em>}'
     * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Fill</em>' containment reference.
     * @see #getFill()
     * @generated
     */
    void setFill(Fill value);

    /**
     * Returns the value of the '<em><b>Start Position</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc --> <!-- begin-model-doc -->
     * 
     * Defines where this area starts relative to the axis scale.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Start Position</em>' attribute.
     * @see #setStartPosition(Object)
     * @see org.eclipse.birt.chart.model.component.ComponentPackage#getMarkerRange_StartPosition()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType" required="true"
     * @generated
     */
    Object getStartPosition();

    /**
     * Sets the value of the '
     * {@link org.eclipse.birt.chart.model.component.MarkerRange#getStartPosition <em>Start Position</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Start Position</em>' attribute.
     * @see #getStartPosition()
     * @generated
     */
    void setStartPosition(Object value);

    /**
     * Returns the value of the '<em><b>End Position</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * --> <!-- begin-model-doc -->
     * 
     * Defines where this area is ends relative to the axis scale.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>End Position</em>' attribute.
     * @see #setEndPosition(Object)
     * @see org.eclipse.birt.chart.model.component.ComponentPackage#getMarkerRange_EndPosition()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType" required="true"
     * @generated
     */
    Object getEndPosition();

    /**
     * Sets the value of the '
     * {@link org.eclipse.birt.chart.model.component.MarkerRange#getEndPosition <em>End Position</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>End Position</em>' attribute.
     * @see #getEndPosition()
     * @generated
     */
    void setEndPosition(Object value);

    /**
     * Returns the value of the '<em><b>Associated Label</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc --> <!-- begin-model-doc -->
     * 
     * Specifies the label associated with this area.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Associated Label</em>' containment reference.
     * @see #setAssociatedLabel(Label)
     * @see org.eclipse.birt.chart.model.component.ComponentPackage#getMarkerRange_AssociatedLabel()
     * @model containment="true" resolveProxies="false" required="true"
     * @generated
     */
    Label getAssociatedLabel();

    /**
     * Sets the value of the '
     * {@link org.eclipse.birt.chart.model.component.MarkerRange#getAssociatedLabel <em>Associated Label</em>}'
     * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Associated Label</em>' containment reference.
     * @see #getAssociatedLabel()
     * @generated
     */
    void setAssociatedLabel(Label value);

    /**
     * Returns the value of the '<em><b>Label Position</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc --> <!-- begin-model-doc -->
     * 
     * Specifies where the label associated with this range is to be positioned relative to the area itself.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Label Position</em>' attribute.
     * @see #setLabelPosition(String)
     * @see org.eclipse.birt.chart.model.component.ComponentPackage#getMarkerRange_LabelPosition()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     * @generated
     */
    String getLabelPosition();

    /**
     * Sets the value of the '
     * {@link org.eclipse.birt.chart.model.component.MarkerRange#getLabelPosition <em>Label Position</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Label Position</em>' attribute.
     * @see #getLabelPosition()
     * @generated
     */
    void setLabelPosition(String value);

} // MarkerRange
