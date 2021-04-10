/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.model.attribute;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc --> A representation of the literals of the enumeration
 * '<em><b>Angle Type</b></em>', and utility methods for working with them. <!--
 * end-user-doc --> <!-- begin-model-doc --> This type defines the angle type of
 * an Angle3D <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getAngleType()
 * @model extendedMetaData="name='AngleType'"
 * @generated
 */
public enum AngleType implements Enumerator {
	/**
	 * The '<em><b>None</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #NONE
	 * @generated
	 * @ordered
	 */
	NONE_LITERAL(0, "None", "None"),
	/**
	 * The '<em><b>X</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #X
	 * @generated
	 * @ordered
	 */
	X_LITERAL(1, "X", "X"),
	/**
	 * The '<em><b>Y</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #Y
	 * @generated
	 * @ordered
	 */
	Y_LITERAL(2, "Y", "Y"),
	/**
	 * The '<em><b>Z</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #Z
	 * @generated
	 * @ordered
	 */
	Z_LITERAL(3, "Z", "Z");

	/**
	 * The '<em><b>None</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>None</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #NONE_LITERAL
	 * @model name="None"
	 * @generated
	 * @ordered
	 */
	public static final int NONE = 0;

	/**
	 * The '<em><b>X</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>X</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #X_LITERAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int X = 1;

	/**
	 * The '<em><b>Y</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Y</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #Y_LITERAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int Y = 2;

	/**
	 * The '<em><b>Z</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Z</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #Z_LITERAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int Z = 3;

	/**
	 * An array of all the '<em><b>Angle Type</b></em>' enumerators. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final AngleType[] VALUES_ARRAY = new AngleType[] { NONE_LITERAL, X_LITERAL, Y_LITERAL, Z_LITERAL, };

	/**
	 * A public read-only list of all the '<em><b>Angle Type</b></em>' enumerators.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final List<AngleType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Angle Type</b></em>' literal with the specified literal
	 * value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static AngleType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			AngleType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Angle Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static AngleType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			AngleType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Angle Type</b></em>' literal with the specified integer
	 * value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static AngleType get(int value) {
		switch (value) {
		case NONE:
			return NONE_LITERAL;
		case X:
			return X_LITERAL;
		case Y:
			return Y_LITERAL;
		case Z:
			return Z_LITERAL;
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
	private AngleType(int value, String name, String literal) {
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
