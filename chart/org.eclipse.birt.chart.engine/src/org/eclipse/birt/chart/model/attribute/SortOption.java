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
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '<em><b>Sort Option</b></em>', and
 * utility methods for working with them. <!-- end-user-doc --> <!-- begin-model-doc -->
 * 
 * This type represents the options available for sorting.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getSortOption()
 * @model @generated
 */
public final class SortOption extends AbstractEnumerator
{

    /**
     * The '<em><b>Ascending</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #ASCENDING_LITERAL
     * @model name="Ascending"
     * @generated @ordered
     */
    public static final int ASCENDING = 0;

    /**
     * The '<em><b>Descending</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #DESCENDING_LITERAL
     * @model name="Descending"
     * @generated @ordered
     */
    public static final int DESCENDING = 1;

    /**
     * The '<em><b>Ascending</b></em>' literal object. <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Ascending</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #ASCENDING
     * @generated @ordered
     */
    public static final SortOption ASCENDING_LITERAL = new SortOption(ASCENDING, "Ascending");

    /**
     * The '<em><b>Descending</b></em>' literal object. <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Descending</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #DESCENDING
     * @generated @ordered
     */
    public static final SortOption DESCENDING_LITERAL = new SortOption(DESCENDING, "Descending");

    /**
     * An array of all the '<em><b>Sort Option</b></em>' enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private static final SortOption[] VALUES_ARRAY = new SortOption[]
    {
        ASCENDING_LITERAL, DESCENDING_LITERAL,
    };

    /**
     * A public read-only list of all the '<em><b>Sort Option</b></em>' enumerators. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Sort Option</b></em>' literal with the specified name. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    public static SortOption get(String name)
    {
        for (int i = 0; i < VALUES_ARRAY.length; ++i)
        {
            SortOption result = VALUES_ARRAY[i];
            if (result.toString().equals(name))
            {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Sort Option</b></em>' literal with the specified value. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    public static SortOption get(int value)
    {
        switch (value)
        {
            case ASCENDING:
                return ASCENDING_LITERAL;
            case DESCENDING:
                return DESCENDING_LITERAL;
        }
        return null;
    }

    /**
     * Only this class can construct instances. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private SortOption(int value, String name)
    {
        super(value, name);
    }

} //SortOption
