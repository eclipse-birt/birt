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

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Java Number Format Specifier</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <!-- begin-model-doc -->
 * 
 * This type holds the java pattern for NumberFormat.
 * 
 * <!-- end-model-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier#getPattern <em>Pattern</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier#getMultiplier <em>Multiplier</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getJavaNumberFormatSpecifier()
 * @model
 * @generated
 */
public interface JavaNumberFormatSpecifier extends FormatSpecifier
{

    /**
     * Returns the value of the '<em><b>Pattern</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * Specifies the format pattern.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Pattern</em>' attribute.
     * @see #setPattern(String)
     * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getJavaNumberFormatSpecifier_Pattern()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     * @generated
     */
    String getPattern();

    /**
     * Sets the value of the '
     * {@link org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier#getPattern <em>Pattern</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Pattern</em>' attribute.
     * @see #getPattern()
     * @generated
     */
    void setPattern(String value);

    /**
     * Returns the value of the '<em><b>Multiplier</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * --> <!-- begin-model-doc -->
     * 
     * Specifies the multiplier.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Multiplier</em>' attribute.
     * @see #isSetMultiplier()
     * @see #unsetMultiplier()
     * @see #setMultiplier(double)
     * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getJavaNumberFormatSpecifier_Multiplier()
     * @model unique="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double" required="true"
     * @generated
     */
    double getMultiplier();

    /**
     * Sets the value of the '
     * {@link org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier#getMultiplier <em>Multiplier</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Multiplier</em>' attribute.
     * @see #isSetMultiplier()
     * @see #unsetMultiplier()
     * @see #getMultiplier()
     * @generated
     */
    void setMultiplier(double value);

    /**
     * Unsets the value of the '
     * {@link org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier#getMultiplier <em>Multiplier</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #isSetMultiplier()
     * @see #getMultiplier()
     * @see #setMultiplier(double)
     * @generated
     */
    void unsetMultiplier();

    /**
     * Returns whether the value of the '
     * {@link org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier#getMultiplier <em>Multiplier</em>}'
     * attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return whether the value of the '<em>Multiplier</em>' attribute is set.
     * @see #unsetMultiplier()
     * @see #getMultiplier()
     * @see #setMultiplier(double)
     * @generated
     */
    boolean isSetMultiplier();

    /**
     * 
     * @param dValue
     * @return
     */
    String format(double dValue);

} // JavaNumberFormatSpecifier
