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
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '<em><b>Tick Style</b></em>', and
 * utility methods for working with them. <!-- end-user-doc --> <!-- begin-model-doc -->
 * 
 * This type represents the possible values for tick positions.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getTickStyle()
 * @model @generated
 */
public final class TickStyle extends AbstractEnumerator
{

    /**
     * The '<em><b>Left</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #LEFT_LITERAL
     * @model name="Left"
     * @generated @ordered
     */
    public static final int LEFT = 0;

    /**
     * The '<em><b>Right</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #RIGHT_LITERAL
     * @model name="Right"
     * @generated @ordered
     */
    public static final int RIGHT = 1;

    /**
     * The '<em><b>Above</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #ABOVE_LITERAL
     * @model name="Above"
     * @generated @ordered
     */
    public static final int ABOVE = 2;

    /**
     * The '<em><b>Below</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #BELOW_LITERAL
     * @model name="Below"
     * @generated @ordered
     */
    public static final int BELOW = 3;

    /**
     * The '<em><b>Across</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #ACROSS_LITERAL
     * @model name="Across"
     * @generated @ordered
     */
    public static final int ACROSS = 4;

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
    public static final TickStyle LEFT_LITERAL = new TickStyle(LEFT, "Left");

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
    public static final TickStyle RIGHT_LITERAL = new TickStyle(RIGHT, "Right");

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
    public static final TickStyle ABOVE_LITERAL = new TickStyle(ABOVE, "Above");

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
    public static final TickStyle BELOW_LITERAL = new TickStyle(BELOW, "Below");

    /**
     * The '<em><b>Across</b></em>' literal object. <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Across</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #ACROSS
     * @generated @ordered
     */
    public static final TickStyle ACROSS_LITERAL = new TickStyle(ACROSS, "Across");

    /**
     * An array of all the '<em><b>Tick Style</b></em>' enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private static final TickStyle[] VALUES_ARRAY = new TickStyle[]
    {
        LEFT_LITERAL, RIGHT_LITERAL, ABOVE_LITERAL, BELOW_LITERAL, ACROSS_LITERAL,
    };

    /**
     * A public read-only list of all the '<em><b>Tick Style</b></em>' enumerators. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Tick Style</b></em>' literal with the specified name. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    public static TickStyle get(String name)
    {
        for (int i = 0; i < VALUES_ARRAY.length; ++i)
        {
            TickStyle result = VALUES_ARRAY[i];
            if (result.toString().equals(name))
            {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Tick Style</b></em>' literal with the specified value. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    public static TickStyle get(int value)
    {
        switch (value)
        {
            case LEFT:
                return LEFT_LITERAL;
            case RIGHT:
                return RIGHT_LITERAL;
            case ABOVE:
                return ABOVE_LITERAL;
            case BELOW:
                return BELOW_LITERAL;
            case ACROSS:
                return ACROSS_LITERAL;
        }
        return null;
    }

    /**
     * Only this class can construct instances. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private TickStyle(int value, String name)
    {
        super(value, name);
    }

} //TickStyle
