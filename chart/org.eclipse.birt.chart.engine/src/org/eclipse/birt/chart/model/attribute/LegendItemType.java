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
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '<em><b>Legend Item Type</b></em>',
 * and utility methods for working with them. <!-- end-user-doc --> <!-- begin-model-doc -->
 * 
 * This type represents the possible values for the legend item type.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getLegendItemType()
 * @model
 * @generated
 */
public final class LegendItemType extends AbstractEnumerator
{

    /**
     * The '<em><b>Series</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #SERIES_LITERAL
     * @model name="Series"
     * @generated
     * @ordered
     */
    public static final int SERIES = 0;

    /**
     * The '<em><b>Categories</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #CATEGORIES_LITERAL
     * @model name="Categories"
     * @generated
     * @ordered
     */
    public static final int CATEGORIES = 1;

    /**
     * The '<em><b>Series</b></em>' literal object. <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Series</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #SERIES
     * @generated
     * @ordered
     */
    public static final LegendItemType SERIES_LITERAL = new LegendItemType(SERIES, "Series");

    /**
     * The '<em><b>Categories</b></em>' literal object. <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Categories</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #CATEGORIES
     * @generated
     * @ordered
     */
    public static final LegendItemType CATEGORIES_LITERAL = new LegendItemType(CATEGORIES, "Categories");

    /**
     * An array of all the '<em><b>Legend Item Type</b></em>' enumerators. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    private static final LegendItemType[] VALUES_ARRAY = new LegendItemType[]
    {
        SERIES_LITERAL, CATEGORIES_LITERAL,
    };

    /**
     * A public read-only list of all the '<em><b>Legend Item Type</b></em>' enumerators. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Legend Item Type</b></em>' literal with the specified name. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    public static LegendItemType get(String name)
    {
        for (int i = 0; i < VALUES_ARRAY.length; ++i)
        {
            LegendItemType result = VALUES_ARRAY[i];
            if (result.toString().equals(name))
            {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Legend Item Type</b></em>' literal with the specified value. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public static LegendItemType get(int value)
    {
        switch (value)
        {
            case SERIES:
                return SERIES_LITERAL;
            case CATEGORIES:
                return CATEGORIES_LITERAL;
        }
        return null;
    }

    /**
     * Only this class can construct instances. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private LegendItemType(int value, String name)
    {
        super(value, name);
    }

} //LegendItemType
