/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
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
 * <em><b>Sort Option</b></em>', and utility methods for working with them. <!--
 * end-user-doc --> <!-- begin-model-doc -->
 *
 * This type represents the options available for sorting.
 *
 * <!-- end-model-doc -->
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getSortOption()
 * @model
 * @generated
 */
public enum SortOption implements Enumerator {
	/**
	 * The '<em><b>Ascending</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Ascending</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #ASCENDING
	 * @generated
	 * @ordered
	 */
	ASCENDING_LITERAL(0, "Ascending", "Ascending"),
	/**
	 * The '<em><b>Descending</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Descending</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #DESCENDING
	 * @generated
	 * @ordered
	 */
	DESCENDING_LITERAL(1, "Descending", "Descending");

	/**
	 * The '<em><b>Ascending</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #ASCENDING_LITERAL
	 * @model name="Ascending"
	 * @generated
	 * @ordered
	 */
	public static final int ASCENDING = 0;

	/**
	 * The '<em><b>Descending</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #DESCENDING_LITERAL
	 * @model name="Descending"
	 * @generated
	 * @ordered
	 */
	public static final int DESCENDING = 1;

	/**
	 * An array of all the '<em><b>Sort Option</b></em>' enumerators. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private static final SortOption[] VALUES_ARRAY = { ASCENDING_LITERAL, DESCENDING_LITERAL, };

	/**
	 * A public read-only list of all the '<em><b>Sort Option</b></em>' enumerators.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static final List<SortOption> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Sort Option</b></em>' literal with the specified literal
	 * value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static SortOption get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			SortOption result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Sort Option</b></em>' literal with the specified name.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static SortOption getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			SortOption result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Sort Option</b></em>' literal with the specified integer
	 * value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static SortOption get(int value) {
		switch (value) {
		case ASCENDING:
			return ASCENDING_LITERAL;
		case DESCENDING:
			return DESCENDING_LITERAL;
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
	SortOption(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public int getValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
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
