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
 * <em><b>Anchor</b></em>', and utility methods for working with them. <!--
 * end-user-doc --> <!-- begin-model-doc -->
 *
 * This type represents the options available for anchoring blocks.
 *
 * <!-- end-model-doc -->
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getAnchor()
 * @model
 * @generated
 */
public enum Anchor implements Enumerator {
	/**
	 * The '<em><b>North</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>North</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #NORTH
	 * @generated
	 * @ordered
	 */
	NORTH_LITERAL(0, "North", "North"),
	/**
	 * The '<em><b>North East</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>North East</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #NORTH_EAST
	 * @generated
	 * @ordered
	 */
	NORTH_EAST_LITERAL(1, "NorthEast", "North_East"),
	/**
	 * The '<em><b>East</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>East</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #EAST
	 * @generated
	 * @ordered
	 */
	EAST_LITERAL(2, "East", "East"),
	/**
	 * The '<em><b>South East</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>South East</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #SOUTH_EAST
	 * @generated
	 * @ordered
	 */
	SOUTH_EAST_LITERAL(3, "SouthEast", "South_East"),
	/**
	 * The '<em><b>South</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>South</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #SOUTH
	 * @generated
	 * @ordered
	 */
	SOUTH_LITERAL(4, "South", "South"),
	/**
	 * The '<em><b>South West</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>South West</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #SOUTH_WEST
	 * @generated
	 * @ordered
	 */
	SOUTH_WEST_LITERAL(5, "SouthWest", "South_West"),
	/**
	 * The '<em><b>West</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>West</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #WEST
	 * @generated
	 * @ordered
	 */
	WEST_LITERAL(6, "West", "West"),
	/**
	 * The '<em><b>North West</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>North West</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #NORTH_WEST
	 * @generated
	 * @ordered
	 */
	NORTH_WEST_LITERAL(7, "NorthWest", "North_West");

	/**
	 * The '<em><b>North</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #NORTH_LITERAL
	 * @model name="North"
	 * @generated
	 * @ordered
	 */
	public static final int NORTH = 0;

	/**
	 * The '<em><b>North East</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #NORTH_EAST_LITERAL
	 * @model name="NorthEast" literal="North_East"
	 * @generated
	 * @ordered
	 */
	public static final int NORTH_EAST = 1;

	/**
	 * The '<em><b>East</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #EAST_LITERAL
	 * @model name="East"
	 * @generated
	 * @ordered
	 */
	public static final int EAST = 2;

	/**
	 * The '<em><b>South East</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #SOUTH_EAST_LITERAL
	 * @model name="SouthEast" literal="South_East"
	 * @generated
	 * @ordered
	 */
	public static final int SOUTH_EAST = 3;

	/**
	 * The '<em><b>South</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #SOUTH_LITERAL
	 * @model name="South"
	 * @generated
	 * @ordered
	 */
	public static final int SOUTH = 4;

	/**
	 * The '<em><b>South West</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #SOUTH_WEST_LITERAL
	 * @model name="SouthWest" literal="South_West"
	 * @generated
	 * @ordered
	 */
	public static final int SOUTH_WEST = 5;

	/**
	 * The '<em><b>West</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #WEST_LITERAL
	 * @model name="West"
	 * @generated
	 * @ordered
	 */
	public static final int WEST = 6;

	/**
	 * The '<em><b>North West</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #NORTH_WEST_LITERAL
	 * @model name="NorthWest" literal="North_West"
	 * @generated
	 * @ordered
	 */
	public static final int NORTH_WEST = 7;

	/**
	 * An array of all the '<em><b>Anchor</b></em>' enumerators. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private static final Anchor[] VALUES_ARRAY = { NORTH_LITERAL, NORTH_EAST_LITERAL, EAST_LITERAL, SOUTH_EAST_LITERAL,
			SOUTH_LITERAL, SOUTH_WEST_LITERAL, WEST_LITERAL, NORTH_WEST_LITERAL, };

	/**
	 * A public read-only list of all the '<em><b>Anchor</b></em>' enumerators. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static final List<Anchor> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Anchor</b></em>' literal with the specified literal
	 * value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static Anchor get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			Anchor result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Anchor</b></em>' literal with the specified name. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static Anchor getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			Anchor result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Anchor</b></em>' literal with the specified integer
	 * value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static Anchor get(int value) {
		switch (value) {
		case NORTH:
			return NORTH_LITERAL;
		case NORTH_EAST:
			return NORTH_EAST_LITERAL;
		case EAST:
			return EAST_LITERAL;
		case SOUTH_EAST:
			return SOUTH_EAST_LITERAL;
		case SOUTH:
			return SOUTH_LITERAL;
		case SOUTH_WEST:
			return SOUTH_WEST_LITERAL;
		case WEST:
			return WEST_LITERAL;
		case NORTH_WEST:
			return NORTH_WEST_LITERAL;
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
	Anchor(int value, String name, String literal) {
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
