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
 * <em><b>Marker Type</b></em>', and utility methods for working with them. <!--
 * end-user-doc --> <!-- begin-model-doc -->
 * 
 * This type represents the possible values for markers supported for Line
 * Series.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getMarkerType()
 * @model
 * @generated
 */
public enum MarkerType implements Enumerator {
	/**
	 * The '<em><b>Crosshair</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Crosshair</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #CROSSHAIR
	 * @generated
	 * @ordered
	 */
	CROSSHAIR_LITERAL(0, "Crosshair", "Crosshair"),
	/**
	 * The '<em><b>Triangle</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Triangle</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #TRIANGLE
	 * @generated
	 * @ordered
	 */
	TRIANGLE_LITERAL(1, "Triangle", "Triangle"),
	/**
	 * The '<em><b>Box</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Box</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #BOX
	 * @generated
	 * @ordered
	 */
	BOX_LITERAL(2, "Box", "Box"),
	/**
	 * The '<em><b>Circle</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Circle</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #CIRCLE
	 * @generated
	 * @ordered
	 */
	CIRCLE_LITERAL(3, "Circle", "Circle"),
	/**
	 * The '<em><b>Icon</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #ICON
	 * @generated
	 * @ordered
	 */
	ICON_LITERAL(4, "Icon", "Icon"),
	/**
	 * The '<em><b>Nabla</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #NABLA
	 * @generated
	 * @ordered
	 */
	NABLA_LITERAL(5, "Nabla", "Nabla"),
	/**
	 * The '<em><b>Diamond</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #DIAMOND
	 * @generated
	 * @ordered
	 */
	DIAMOND_LITERAL(6, "Diamond", "Diamond"),
	/**
	 * The '<em><b>Four Diamonds</b></em>' literal object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #FOUR_DIAMONDS
	 * @generated
	 * @ordered
	 */
	FOUR_DIAMONDS_LITERAL(7, "FourDiamonds", "Four_Diamonds"),
	/**
	 * The '<em><b>Ellipse</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #ELLIPSE
	 * @generated
	 * @ordered
	 */
	ELLIPSE_LITERAL(8, "Ellipse", "Ellipse"),
	/**
	 * The '<em><b>Semi Circle</b></em>' literal object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #SEMI_CIRCLE
	 * @generated
	 * @ordered
	 */
	SEMI_CIRCLE_LITERAL(9, "SemiCircle", "Semi_Circle"),
	/**
	 * The '<em><b>Hexagon</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #HEXAGON
	 * @generated
	 * @ordered
	 */
	HEXAGON_LITERAL(10, "Hexagon", "Hexagon"),
	/**
	 * The '<em><b>Rectangle</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #RECTANGLE
	 * @generated
	 * @ordered
	 */
	RECTANGLE_LITERAL(11, "Rectangle", "Rectangle"),
	/**
	 * The '<em><b>Star</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #STAR
	 * @generated
	 * @ordered
	 */
	STAR_LITERAL(12, "Star", "Star"),
	/**
	 * The '<em><b>Column</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #COLUMN
	 * @generated
	 * @ordered
	 */
	COLUMN_LITERAL(13, "Column", "Column"),
	/**
	 * The '<em><b>Cross</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #CROSS
	 * @generated
	 * @ordered
	 */
	CROSS_LITERAL(14, "Cross", "Cross");

	/**
	 * The '<em><b>Crosshair</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #CROSSHAIR_LITERAL
	 * @model name="Crosshair"
	 * @generated
	 * @ordered
	 */
	public static final int CROSSHAIR = 0;

	/**
	 * The '<em><b>Triangle</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #TRIANGLE_LITERAL
	 * @model name="Triangle"
	 * @generated
	 * @ordered
	 */
	public static final int TRIANGLE = 1;

	/**
	 * The '<em><b>Box</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #BOX_LITERAL
	 * @model name="Box"
	 * @generated
	 * @ordered
	 */
	public static final int BOX = 2;

	/**
	 * The '<em><b>Circle</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #CIRCLE_LITERAL
	 * @model name="Circle"
	 * @generated
	 * @ordered
	 */
	public static final int CIRCLE = 3;

	/**
	 * The '<em><b>Icon</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Icon</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #ICON_LITERAL
	 * @model name="Icon"
	 * @generated
	 * @ordered
	 */
	public static final int ICON = 4;

	/**
	 * The '<em><b>Nabla</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Nabla</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #NABLA_LITERAL
	 * @model name="Nabla"
	 * @generated
	 * @ordered
	 */
	public static final int NABLA = 5;

	/**
	 * The '<em><b>Diamond</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Diamond</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #DIAMOND_LITERAL
	 * @model name="Diamond"
	 * @generated
	 * @ordered
	 */
	public static final int DIAMOND = 6;

	/**
	 * The '<em><b>Four Diamonds</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Four Diamonds</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #FOUR_DIAMONDS_LITERAL
	 * @model name="FourDiamonds" literal="Four_Diamonds"
	 * @generated
	 * @ordered
	 */
	public static final int FOUR_DIAMONDS = 7;

	/**
	 * The '<em><b>Ellipse</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Ellipse</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #ELLIPSE_LITERAL
	 * @model name="Ellipse"
	 * @generated
	 * @ordered
	 */
	public static final int ELLIPSE = 8;

	/**
	 * The '<em><b>Semi Circle</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Semi Circle</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #SEMI_CIRCLE_LITERAL
	 * @model name="SemiCircle" literal="Semi_Circle"
	 * @generated
	 * @ordered
	 */
	public static final int SEMI_CIRCLE = 9;

	/**
	 * The '<em><b>Hexagon</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Hexagon</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #HEXAGON_LITERAL
	 * @model name="Hexagon"
	 * @generated
	 * @ordered
	 */
	public static final int HEXAGON = 10;

	/**
	 * The '<em><b>Rectangle</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Rectangle</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #RECTANGLE_LITERAL
	 * @model name="Rectangle"
	 * @generated
	 * @ordered
	 */
	public static final int RECTANGLE = 11;

	/**
	 * The '<em><b>Star</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Star</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #STAR_LITERAL
	 * @model name="Star"
	 * @generated
	 * @ordered
	 */
	public static final int STAR = 12;

	/**
	 * The '<em><b>Column</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Column</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #COLUMN_LITERAL
	 * @model name="Column"
	 * @generated
	 * @ordered
	 */
	public static final int COLUMN = 13;

	/**
	 * The '<em><b>Cross</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Cross</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #CROSS_LITERAL
	 * @model name="Cross"
	 * @generated
	 * @ordered
	 */
	public static final int CROSS = 14;

	/**
	 * An array of all the '<em><b>Marker Type</b></em>' enumerators. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final MarkerType[] VALUES_ARRAY = new MarkerType[] { CROSSHAIR_LITERAL, TRIANGLE_LITERAL,
			BOX_LITERAL, CIRCLE_LITERAL, ICON_LITERAL, NABLA_LITERAL, DIAMOND_LITERAL, FOUR_DIAMONDS_LITERAL,
			ELLIPSE_LITERAL, SEMI_CIRCLE_LITERAL, HEXAGON_LITERAL, RECTANGLE_LITERAL, STAR_LITERAL, COLUMN_LITERAL,
			CROSS_LITERAL, };

	/**
	 * A public read-only list of all the '<em><b>Marker Type</b></em>' enumerators.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final List<MarkerType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Marker Type</b></em>' literal with the specified literal
	 * value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static MarkerType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			MarkerType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Marker Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static MarkerType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			MarkerType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Marker Type</b></em>' literal with the specified integer
	 * value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static MarkerType get(int value) {
		switch (value) {
		case CROSSHAIR:
			return CROSSHAIR_LITERAL;
		case TRIANGLE:
			return TRIANGLE_LITERAL;
		case BOX:
			return BOX_LITERAL;
		case CIRCLE:
			return CIRCLE_LITERAL;
		case ICON:
			return ICON_LITERAL;
		case NABLA:
			return NABLA_LITERAL;
		case DIAMOND:
			return DIAMOND_LITERAL;
		case FOUR_DIAMONDS:
			return FOUR_DIAMONDS_LITERAL;
		case ELLIPSE:
			return ELLIPSE_LITERAL;
		case SEMI_CIRCLE:
			return SEMI_CIRCLE_LITERAL;
		case HEXAGON:
			return HEXAGON_LITERAL;
		case RECTANGLE:
			return RECTANGLE_LITERAL;
		case STAR:
			return STAR_LITERAL;
		case COLUMN:
			return COLUMN_LITERAL;
		case CROSS:
			return CROSS_LITERAL;
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
	private MarkerType(int value, String name, String literal) {
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
