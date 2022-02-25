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
 * <em><b>Rule Type</b></em>', and utility methods for working with them. <!--
 * end-user-doc --> <!-- begin-model-doc -->
 *
 * This type represents the possible rule types for use in Query objects.
 *
 * <!-- end-model-doc -->
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getRuleType()
 * @model
 * @deprecated only reserved for compatibility
 */
public enum RuleType implements Enumerator {
	/**
	 * The '<em><b>Filter</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Filter</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #FILTER
	 * @generated
	 * @ordered
	 */
	FILTER_LITERAL(0, "Filter", "Filter"),
	/**
	 * The '<em><b>Suppress</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Suppress</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #SUPPRESS
	 * @generated
	 * @ordered
	 */
	SUPPRESS_LITERAL(1, "Suppress", "Suppress"),
	/**
	 * The '<em><b>Link</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Link</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #LINK
	 * @generated
	 * @ordered
	 */
	LINK_LITERAL(2, "Link", "Link");

	/**
	 * The '<em><b>Filter</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #FILTER_LITERAL
	 * @model name="Filter"
	 * @generated
	 * @ordered
	 */
	public static final int FILTER = 0;

	/**
	 * The '<em><b>Suppress</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #SUPPRESS_LITERAL
	 * @model name="Suppress"
	 * @generated
	 * @ordered
	 */
	public static final int SUPPRESS = 1;

	/**
	 * The '<em><b>Link</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #LINK_LITERAL
	 * @model name="Link"
	 * @generated
	 * @ordered
	 */
	public static final int LINK = 2;

	/**
	 * An array of all the '<em><b>Rule Type</b></em>' enumerators. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private static final RuleType[] VALUES_ARRAY = { FILTER_LITERAL, SUPPRESS_LITERAL, LINK_LITERAL, };

	/**
	 * A public read-only list of all the '<em><b>Rule Type</b></em>' enumerators.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static final List<RuleType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Rule Type</b></em>' literal with the specified literal
	 * value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static RuleType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			RuleType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Rule Type</b></em>' literal with the specified name. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static RuleType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			RuleType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Rule Type</b></em>' literal with the specified integer
	 * value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static RuleType get(int value) {
		switch (value) {
		case FILTER:
			return FILTER_LITERAL;
		case SUPPRESS:
			return SUPPRESS_LITERAL;
		case LINK:
			return LINK_LITERAL;
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
	RuleType(int value, String name, String literal) {
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
