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
 * '<em><b>Trigger Flow</b></em>', and utility methods for working with them.
 * <!-- end-user-doc --> <!-- begin-model-doc --> The TriggerFlow defines
 * whether it reacts to the capturing or bubbling sequence Capture: the
 * trigger's action will be triggered in the capturing phase Bubble (default):
 * the trigger's action will be triggered in the bubbling phase BubbleAndStop:
 * the trigger's action will be triggered in the bubbling phase, and will stop
 * the bubbling flow.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getTriggerFlow()
 * @model extendedMetaData="name='TriggerFlow'"
 * @generated
 */
public enum TriggerFlow implements Enumerator {
	/**
	 * The '<em><b>Capture</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #CAPTURE
	 * @generated
	 * @ordered
	 */
	CAPTURE_LITERAL(0, "Capture", "Capture"),
	/**
	 * The '<em><b>Bubble</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #BUBBLE
	 * @generated
	 * @ordered
	 */
	BUBBLE_LITERAL(1, "Bubble", "Bubble"),
	/**
	 * The '<em><b>Bubble And Stop</b></em>' literal object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #BUBBLE_AND_STOP
	 * @generated
	 * @ordered
	 */
	BUBBLE_AND_STOP_LITERAL(2, "BubbleAndStop", "BubbleAndStop");

	/**
	 * The '<em><b>Capture</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Capture</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #CAPTURE_LITERAL
	 * @model name="Capture"
	 * @generated
	 * @ordered
	 */
	public static final int CAPTURE = 0;

	/**
	 * The '<em><b>Bubble</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Bubble</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #BUBBLE_LITERAL
	 * @model name="Bubble"
	 * @generated
	 * @ordered
	 */
	public static final int BUBBLE = 1;

	/**
	 * The '<em><b>Bubble And Stop</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Bubble And Stop</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #BUBBLE_AND_STOP_LITERAL
	 * @model name="BubbleAndStop"
	 * @generated
	 * @ordered
	 */
	public static final int BUBBLE_AND_STOP = 2;

	/**
	 * An array of all the '<em><b>Trigger Flow</b></em>' enumerators. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final TriggerFlow[] VALUES_ARRAY = new TriggerFlow[] { CAPTURE_LITERAL, BUBBLE_LITERAL,
			BUBBLE_AND_STOP_LITERAL, };

	/**
	 * A public read-only list of all the '<em><b>Trigger Flow</b></em>'
	 * enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final List<TriggerFlow> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Trigger Flow</b></em>' literal with the specified literal
	 * value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static TriggerFlow get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			TriggerFlow result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Trigger Flow</b></em>' literal with the specified name.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static TriggerFlow getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			TriggerFlow result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Trigger Flow</b></em>' literal with the specified integer
	 * value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static TriggerFlow get(int value) {
		switch (value) {
		case CAPTURE:
			return CAPTURE_LITERAL;
		case BUBBLE:
			return BUBBLE_LITERAL;
		case BUBBLE_AND_STOP:
			return BUBBLE_AND_STOP_LITERAL;
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
	private TriggerFlow(int value, String name, String literal) {
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
