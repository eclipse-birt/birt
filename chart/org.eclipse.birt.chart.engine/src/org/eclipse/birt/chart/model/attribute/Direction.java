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
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '<em><b>Direction</b></em>', and
 * utility methods for working with them. <!-- end-user-doc --> <!-- begin-model-doc -->
 * 
 * This type represents the options available for directions.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getDirection()
 * @model @generated
 */
public final class Direction extends AbstractEnumerator
{

    /**
     * The '<em><b>Left Right</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #LEFT_RIGHT_LITERAL
     * @model name="Left_Right"
     * @generated @ordered
     */
    public static final int LEFT_RIGHT = 0;

    /**
     * The '<em><b>Top Bottom</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #TOP_BOTTOM_LITERAL
     * @model name="Top_Bottom"
     * @generated @ordered
     */
    public static final int TOP_BOTTOM = 1;

    /**
     * The '<em><b>Left Right</b></em>' literal object. <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Left Right</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #LEFT_RIGHT
     * @generated @ordered
     */
    public static final Direction LEFT_RIGHT_LITERAL = new Direction(LEFT_RIGHT, "Left_Right");

    /**
     * The '<em><b>Top Bottom</b></em>' literal object. <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Top Bottom</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #TOP_BOTTOM
     * @generated @ordered
     */
    public static final Direction TOP_BOTTOM_LITERAL = new Direction(TOP_BOTTOM, "Top_Bottom");

    /**
     * An array of all the '<em><b>Direction</b></em>' enumerators.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private static final Direction[] VALUES_ARRAY =
        new Direction[]
        {
            LEFT_RIGHT_LITERAL,
            TOP_BOTTOM_LITERAL,
        };

    /**
     * A public read-only list of all the '<em><b>Direction</b></em>' enumerators.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     */
    public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Direction</b></em>' literal with the specified name.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     */
    public static Direction get(String name)
    {
        for (int i = 0; i < VALUES_ARRAY.length; ++i)
        {
            Direction result = VALUES_ARRAY[i];
            if (result.toString().equals(name))
            {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Direction</b></em>' literal with the specified value.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     */
    public static Direction get(int value)
    {
        switch (value)
        {
            case LEFT_RIGHT: return LEFT_RIGHT_LITERAL;
            case TOP_BOTTOM: return TOP_BOTTOM_LITERAL;
        }
        return null;	
    }

    /**
     * Only this class can construct instances.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private Direction(int value, String name)
    {
        super(value, name);
    }

} //Direction
