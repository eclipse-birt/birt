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

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '
 * <em><b>Legend Item Type</b></em>', and utility methods for working with them.
 * <!-- end-user-doc --> <!-- begin-model-doc -->
 * 
 * This type represents the possible values for the legend item type.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getLegendItemType()
 * @model
 * @generated
 */
public enum LegendItemType implements Enumerator {
	/**
	 * The '<em><b>Series</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Series</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #SERIES
	 * @generated
	 * @ordered
	 */
	SERIES_LITERAL(0, "Series", "Series"),
	/**
	 * The '<em><b>Categories</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Categories</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #CATEGORIES
	 * @generated
	 * @ordered
	 */
	CATEGORIES_LITERAL(1, "Categories", "Categories");

	/**
	 * The '<em><b>Series</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #SERIES_LITERAL
	 * @model name="Series"
	 * @generated
	 * @ordered
	 */
	public static final int SERIES = 0;

	/**
	 * The '<em><b>Categories</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #CATEGORIES_LITERAL
	 * @model name="Categories"
	 * @generated
	 * @ordered
	 */
	public static final int CATEGORIES = 1;

	/**
	 * An array of all the '<em><b>Legend Item Type</b></em>' enumerators. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final LegendItemType[] VALUES_ARRAY = new LegendItemType[] { SERIES_LITERAL, CATEGORIES_LITERAL, };

	/**
	 * A public read-only list of all the '<em><b>Legend Item Type</b></em>'
	 * enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final List<LegendItemType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Legend Item Type</b></em>' literal with the specified
	 * literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static LegendItemType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			LegendItemType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Legend Item Type</b></em>' literal with the specified
	 * name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static LegendItemType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			LegendItemType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Legend Item Type</b></em>' literal with the specified
	 * integer value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static LegendItemType get(int value) {
		switch (value) {
		case SERIES:
			return SERIES_LITERAL;
		case CATEGORIES:
			return CATEGORIES_LITERAL;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private final int value;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	private LegendItemType(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public int getValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getLiteral() {
		return literal;
	}

	/**
	 * Returns the literal value of the enumerator, which is its string
	 * representation. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		return literal;
	}
}
