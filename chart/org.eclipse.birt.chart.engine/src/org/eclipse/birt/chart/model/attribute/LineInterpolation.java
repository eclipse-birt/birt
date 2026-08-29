/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.chart.model.attribute;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '
 * <em><b>Line Interpolation</b></em>', and utility methods for working with
 * them. <!-- end-user-doc --> <!-- begin-model-doc -->
 *
 * This type represents how a Line Series joins two consecutive data points: a
 * straight segment (Linear), or a staircase that steps at the first point
 * (StepBefore), at the second (StepAfter) or half way between (StepCenter).
 *
 * <!-- end-model-doc -->
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getLineInterpolation()
 * @model
 * @generated
 * @since 4.25
 */
public enum LineInterpolation implements Enumerator {
	/**
	 * The '<em><b>Linear</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #LINEAR
	 * @generated
	 * @ordered
	 */
	LINEAR_LITERAL(0, "Linear", "Linear"),
	/**
	 * The '<em><b>Step Before</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #STEP_BEFORE
	 * @generated
	 * @ordered
	 */
	STEP_BEFORE_LITERAL(1, "StepBefore", "StepBefore"),
	/**
	 * The '<em><b>Step After</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #STEP_AFTER
	 * @generated
	 * @ordered
	 */
	STEP_AFTER_LITERAL(2, "StepAfter", "StepAfter"),
	/**
	 * The '<em><b>Step Center</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #STEP_CENTER
	 * @generated
	 * @ordered
	 */
	STEP_CENTER_LITERAL(3, "StepCenter", "StepCenter");

	/**
	 * The '<em><b>Linear</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #LINEAR_LITERAL
	 * @model name="Linear"
	 * @generated
	 * @ordered
	 */
	public static final int LINEAR = 0;

	/**
	 * The '<em><b>Step Before</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #STEP_BEFORE_LITERAL
	 * @model name="StepBefore"
	 * @generated
	 * @ordered
	 */
	public static final int STEP_BEFORE = 1;

	/**
	 * The '<em><b>Step After</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #STEP_AFTER_LITERAL
	 * @model name="StepAfter"
	 * @generated
	 * @ordered
	 */
	public static final int STEP_AFTER = 2;

	/**
	 * The '<em><b>Step Center</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #STEP_CENTER_LITERAL
	 * @model name="StepCenter"
	 * @generated
	 * @ordered
	 */
	public static final int STEP_CENTER = 3;

	/**
	 * An array of all the '<em><b>Line Interpolation</b></em>' enumerators. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private static final LineInterpolation[] VALUES_ARRAY = { LINEAR_LITERAL, STEP_BEFORE_LITERAL, STEP_AFTER_LITERAL,
			STEP_CENTER_LITERAL, };

	/**
	 * A public read-only list of all the '<em><b>Line Interpolation</b></em>'
	 * enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static final List<LineInterpolation> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Line Interpolation</b></em>' literal with the specified
	 * literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static LineInterpolation get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			LineInterpolation result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Line Interpolation</b></em>' literal with the specified
	 * name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static LineInterpolation getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			LineInterpolation result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Line Interpolation</b></em>' literal with the specified
	 * integer value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static LineInterpolation get(int value) {
		switch (value) {
		case LINEAR:
			return LINEAR_LITERAL;
		case STEP_BEFORE:
			return STEP_BEFORE_LITERAL;
		case STEP_AFTER:
			return STEP_AFTER_LITERAL;
		case STEP_CENTER:
			return STEP_CENTER_LITERAL;
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
	LineInterpolation(int value, String name, String literal) {
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
