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

import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Marker Line</b></em>'. <!-- end-user-doc
 * -->
 * 
 * <!-- begin-model-doc -->
 * 
 * This type defines a single line element. It is intended for use as a marker associated with an Axis. e.g. To denote a
 * Target value
 * 
 * <!-- end-model-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.component.MarkerLine#getAttributes <em>Attributes</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.MarkerLine#getPosition <em>Position</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.MarkerLine#getAssociatedLabel <em>Associated Label</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.MarkerLine#getLabelPosition <em>Label Position</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getMarkerLine()
 * @model
 * @generated
 */
public interface MarkerLine extends EObject
{

    /**
     * Returns the value of the '<em><b>Attributes</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc --> <!-- begin-model-doc -->
     * 
     * Specify the line properties.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Attributes</em>' containment reference.
     * @see #setAttributes(LineAttributes)
     * @see org.eclipse.birt.chart.model.component.ComponentPackage#getMarkerLine_Attributes()
     * @model containment="true" resolveProxies="false" required="true"
     * @generated
     */
    LineAttributes getAttributes();

    /**
     * Sets the value of the '
     * {@link org.eclipse.birt.chart.model.component.MarkerLine#getAttributes <em>Attributes</em>}' containment
     * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Attributes</em>' containment reference.
     * @see #getAttributes()
     * @generated
     */
    void setAttributes(LineAttributes value);

    /**
     * Returns the value of the '<em><b>Position</b></em>' attribute. <!-- begin-user-doc --> Gets where this line
     * is to be positioned w.r.t the axis. <!-- end-user-doc --> <!-- begin-model-doc -->
     * 
     * Defines where this line is to be positioned w.r.t the axis. (Can be a value on the axis or a category name).
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Position</em>' attribute.
     * @see #setPosition(Object)
     * @see org.eclipse.birt.chart.model.component.ComponentPackage#getMarkerLine_Position()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType" required="true"
     * @generated
     */
    Object getPosition();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.component.MarkerLine#getPosition <em>Position</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Position</em>' attribute.
     * @see #getPosition()
     * @generated
     */
    void setPosition(Object value);

    /**
     * Returns the value of the '<em><b>Associated Label</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc --> <!-- begin-model-doc -->
     * 
     * Specifies the label associated with this line.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Associated Label</em>' containment reference.
     * @see #setAssociatedLabel(Label)
     * @see org.eclipse.birt.chart.model.component.ComponentPackage#getMarkerLine_AssociatedLabel()
     * @model containment="true" resolveProxies="false" required="true"
     * @generated
     */
    Label getAssociatedLabel();

    /**
     * Sets the value of the '
     * {@link org.eclipse.birt.chart.model.component.MarkerLine#getAssociatedLabel <em>Associated Label</em>}'
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
     * Specifies where the label associated with this line is to be positioned w.r.t the line itself.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Label Position</em>' attribute.
     * @see #setLabelPosition(String)
     * @see org.eclipse.birt.chart.model.component.ComponentPackage#getMarkerLine_LabelPosition()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     * @generated
     */
    String getLabelPosition();

    /**
     * Sets the value of the '
     * {@link org.eclipse.birt.chart.model.component.MarkerLine#getLabelPosition <em>Label Position</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Label Position</em>' attribute.
     * @see #getLabelPosition()
     * @generated
     */
    void setLabelPosition(String value);

} // MarkerLine
