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

import java.util.Calendar;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Java Date Format Specifier</b></em>'. <!--
 * end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 			This type holds the java pattern for DateFormat.
 * 			
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.JavaDateFormatSpecifier#getPattern <em>Pattern</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getJavaDateFormatSpecifier()
 * @model 
 * @generated
 */
public interface JavaDateFormatSpecifier extends FormatSpecifier{

    /**
     * Returns the value of the '<em><b>Pattern</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * 							Specifies the format pattern.
     * 							
     * <!-- end-model-doc -->
     * @return the value of the '<em>Pattern</em>' attribute.
     * @see #setPattern(String)
     * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getJavaDateFormatSpecifier_Pattern()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     * @generated
     */
    String getPattern();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.attribute.JavaDateFormatSpecifier#getPattern <em>Pattern</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Pattern</em>' attribute.
     * @see #getPattern()
     * @generated
     */
    void setPattern(String value);

    /**
     * Formats a calendar value using the Java date format pattern
     * 
     * NOTE: Manually written
     * 
     * @param c
     * @return
     */
    String format(Calendar c);
} // JavaDateFormatSpecifier
