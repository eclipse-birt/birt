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
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '<em><b>Orientation</b></em>', and
 * utility methods for working with them. <!-- end-user-doc --> <!-- begin-model-doc -->
 * 
 * This type represents the options available for orientation.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getOrientation()
 * @model
 * @generated
 */
public final class Orientation extends AbstractEnumerator
{

    /**
     * The '<em><b>Horizontal</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #HORIZONTAL_LITERAL
     * @model name="Horizontal"
     * @generated
     * @ordered
     */
    public static final int HORIZONTAL = 0;

    /**
     * The '<em><b>Vertical</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #VERTICAL_LITERAL
     * @model name="Vertical"
     * @generated
     * @ordered
     */
    public static final int VERTICAL = 1;

    /**
     * The '<em><b>Horizontal</b></em>' literal object. <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Horizontal</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #HORIZONTAL
     * @generated
     * @ordered
     */
    public static final Orientation HORIZONTAL_LITERAL = new Orientation(HORIZONTAL, "Horizontal");

    /**
     * The '<em><b>Vertical</b></em>' literal object. <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Vertical</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #VERTICAL
     * @generated
     * @ordered
     */
    public static final Orientation VERTICAL_LITERAL = new Orientation(VERTICAL, "Vertical");

    /**
     * An array of all the '<em><b>Orientation</b></em>' enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private static final Orientation[] VALUES_ARRAY = new Orientation[]
    {
        HORIZONTAL_LITERAL, VERTICAL_LITERAL,
    };

    /**
     * A public read-only list of all the '<em><b>Orientation</b></em>' enumerators. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Orientation</b></em>' literal with the specified name. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    public static Orientation get(String name)
    {
        for (int i = 0; i < VALUES_ARRAY.length; ++i)
        {
            Orientation result = VALUES_ARRAY[i];
            if (result.toString().equals(name))
            {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Orientation</b></em>' literal with the specified value. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    public static Orientation get(int value)
    {
        switch (value)
        {
            case HORIZONTAL:
                return HORIZONTAL_LITERAL;
            case VERTICAL:
                return VERTICAL_LITERAL;
        }
        return null;
    }

    /**
     * Only this class can construct instances. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private Orientation(int value, String name)
    {
        super(value, name);
    }

} //Orientation
