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
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '<em><b>Label Position</b></em>',
 * and utility methods for working with them. <!-- end-user-doc --> <!-- begin-model-doc -->
 * 
 * This type represents the possible values for label positions.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getPosition()
 * @model @generated
 */
public final class Position extends AbstractEnumerator
{

    /**
     * The '<em><b>Above</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #ABOVE_LITERAL
     * @model name="Above"
     * @generated @ordered
     */
    public static final int ABOVE = 0;

    /**
     * The '<em><b>Below</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #BELOW_LITERAL
     * @model name="Below"
     * @generated @ordered
     */
    public static final int BELOW = 1;

    /**
     * The '<em><b>Left</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #LEFT_LITERAL
     * @model name="Left"
     * @generated @ordered
     */
    public static final int LEFT = 2;

    /**
     * The '<em><b>Right</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #RIGHT_LITERAL
     * @model name="Right"
     * @generated @ordered
     */
    public static final int RIGHT = 3;

    /**
     * The '<em><b>Inside</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #INSIDE_LITERAL
     * @model name="Inside"
     * @generated @ordered
     */
    public static final int INSIDE = 4;

    /**
     * The '<em><b>Outside</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #OUTSIDE_LITERAL
     * @model name="Outside"
     * @generated @ordered
     */
    public static final int OUTSIDE = 5;

    /**
     * The '<em><b>Above</b></em>' literal object. <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Above</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #ABOVE
     * @generated @ordered
     */
    public static final Position ABOVE_LITERAL = new Position(ABOVE, "Above");

    /**
     * The '<em><b>Below</b></em>' literal object. <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Below</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #BELOW
     * @generated @ordered
     */
    public static final Position BELOW_LITERAL = new Position(BELOW, "Below");

    /**
     * The '<em><b>Left</b></em>' literal object. <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Left</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #LEFT
     * @generated @ordered
     */
    public static final Position LEFT_LITERAL = new Position(LEFT, "Left");

    /**
     * The '<em><b>Right</b></em>' literal object. <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Right</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #RIGHT
     * @generated @ordered
     */
    public static final Position RIGHT_LITERAL = new Position(RIGHT, "Right");

    /**
     * The '<em><b>Inside</b></em>' literal object. <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Inside</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #INSIDE
     * @generated @ordered
     */
    public static final Position INSIDE_LITERAL = new Position(INSIDE, "Inside");

    /**
     * The '<em><b>Outside</b></em>' literal object. <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Outside</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #OUTSIDE
     * @generated @ordered
     */
    public static final Position OUTSIDE_LITERAL = new Position(OUTSIDE, "Outside");

    /**
     * An array of all the '<em><b>Position</b></em>' enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private static final Position[] VALUES_ARRAY = new Position[]
    {
        ABOVE_LITERAL, BELOW_LITERAL, LEFT_LITERAL, RIGHT_LITERAL, INSIDE_LITERAL, OUTSIDE_LITERAL,
    };

    /**
     * A public read-only list of all the '<em><b>Position</b></em>' enumerators. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Position</b></em>' literal with the specified name. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    public static Position get(String name)
    {
        for (int i = 0; i < VALUES_ARRAY.length; ++i)
        {
            Position result = VALUES_ARRAY[i];
            if (result.toString().equals(name))
            {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Position</b></em>' literal with the specified value. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    public static Position get(int value)
    {
        switch (value)
        {
            case ABOVE:
                return ABOVE_LITERAL;
            case BELOW:
                return BELOW_LITERAL;
            case LEFT:
                return LEFT_LITERAL;
            case RIGHT:
                return RIGHT_LITERAL;
            case INSIDE:
                return INSIDE_LITERAL;
            case OUTSIDE:
                return OUTSIDE_LITERAL;
        }
        return null;
    }

    /**
     * Only this class can construct instances. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private Position(int value, String name)
    {
        super(value, name);
    }

} //Position
