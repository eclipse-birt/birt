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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '<em><b>Date Format Type</b></em>',
 * and utility methods for working with them. <!-- end-user-doc --> <!-- begin-model-doc -->
 * 
 * This type defines predefined date format types.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getDateFormatType()
 * @model
 * @generated
 */
public final class DateFormatType extends AbstractEnumerator
{

    /**
     * The '<em><b>Long</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #LONG_LITERAL
     * @model name="Long"
     * @generated
     * @ordered
     */
    public static final int LONG = 0;

    /**
     * The '<em><b>Short</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #SHORT_LITERAL
     * @model name="Short"
     * @generated
     * @ordered
     */
    public static final int SHORT = 1;

    /**
     * The '<em><b>Medium</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #MEDIUM_LITERAL
     * @model name="Medium"
     * @generated
     * @ordered
     */
    public static final int MEDIUM = 2;

    /**
     * The '<em><b>Full</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #FULL_LITERAL
     * @model name="Full"
     * @generated
     * @ordered
     */
    public static final int FULL = 3;

    /**
     * The '<em><b>Long</b></em>' literal object. <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Long</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #LONG
     * @generated
     * @ordered
     */
    public static final DateFormatType LONG_LITERAL = new DateFormatType(LONG, "Long");

    /**
     * The '<em><b>Short</b></em>' literal object. <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Short</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #SHORT
     * @generated
     * @ordered
     */
    public static final DateFormatType SHORT_LITERAL = new DateFormatType(SHORT, "Short");

    /**
     * The '<em><b>Medium</b></em>' literal object. <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Medium</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #MEDIUM
     * @generated
     * @ordered
     */
    public static final DateFormatType MEDIUM_LITERAL = new DateFormatType(MEDIUM, "Medium");

    /**
     * The '<em><b>Full</b></em>' literal object. <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Full</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #FULL
     * @generated
     * @ordered
     */
    public static final DateFormatType FULL_LITERAL = new DateFormatType(FULL, "Full");

    /**
     * An array of all the '<em><b>Date Format Type</b></em>' enumerators. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    private static final DateFormatType[] VALUES_ARRAY = new DateFormatType[]
    {
        LONG_LITERAL, SHORT_LITERAL, MEDIUM_LITERAL, FULL_LITERAL,
    };

    /**
     * A public read-only list of all the '<em><b>Date Format Type</b></em>' enumerators. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Date Format Type</b></em>' literal with the specified name. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    public static DateFormatType get(String name)
    {
        for (int i = 0; i < VALUES_ARRAY.length; ++i)
        {
            DateFormatType result = VALUES_ARRAY[i];
            if (result.toString().equals(name))
            {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Date Format Type</b></em>' literal with the specified value. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public static DateFormatType get(int value)
    {
        switch (value)
        {
            case LONG:
                return LONG_LITERAL;
            case SHORT:
                return SHORT_LITERAL;
            case MEDIUM:
                return MEDIUM_LITERAL;
            case FULL:
                return FULL_LITERAL;
        }
        return null;
    }

    /**
     * Only this class can construct instances. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private DateFormatType(int value, String name)
    {
        super(value, name);
    }

} //DateFormatType
