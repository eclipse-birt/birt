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

import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '<em><b>Units Of Measurement</b></em>',
 * and utility methods for working with them. <!-- end-user-doc --> <!-- begin-model-doc -->
 * 
 * This type represents the possible values of Units of Measurement.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getUnitsOfMeasurement()
 * @model
 * @generated
 */
public final class UnitsOfMeasurement extends AbstractEnumerator
{
    /**
     * The '<em><b>Pixels</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #PIXELS_LITERAL
     * @model name="Pixels"
     * @generated
     * @ordered
     */
    public static final int PIXELS = 0;

    /**
     * The '<em><b>Points</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #POINTS_LITERAL
     * @model name="Points"
     * @generated
     * @ordered
     */
    public static final int POINTS = 1;

    /**
     * The '<em><b>Inches</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #INCHES_LITERAL
     * @model name="Inches"
     * @generated
     * @ordered
     */
    public static final int INCHES = 2;

    /**
     * The '<em><b>Centimeters</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #CENTIMETERS_LITERAL
     * @model name="Centimeters"
     * @generated
     * @ordered
     */
    public static final int CENTIMETERS = 3;

    /**
     * The '<em><b>Pixels</b></em>' literal object. <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Pixels</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #PIXELS
     * @generated
     * @ordered
     */
    public static final UnitsOfMeasurement PIXELS_LITERAL = new UnitsOfMeasurement(PIXELS, "Pixels");

    /**
     * The '<em><b>Points</b></em>' literal object. <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Points</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #POINTS
     * @generated
     * @ordered
     */
    public static final UnitsOfMeasurement POINTS_LITERAL = new UnitsOfMeasurement(POINTS, "Points");

    /**
     * The '<em><b>Inches</b></em>' literal object. <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Inches</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #INCHES
     * @generated
     * @ordered
     */
    public static final UnitsOfMeasurement INCHES_LITERAL = new UnitsOfMeasurement(INCHES, "Inches");

    /**
     * The '<em><b>Centimeters</b></em>' literal object. <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Centimeters</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #CENTIMETERS
     * @generated
     * @ordered
     */
    public static final UnitsOfMeasurement CENTIMETERS_LITERAL = new UnitsOfMeasurement(CENTIMETERS, "Centimeters");

    /**
     * An array of all the '<em><b>Units Of Measurement</b></em>' enumerators. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    private static final UnitsOfMeasurement[] VALUES_ARRAY = new UnitsOfMeasurement[]
    {
        PIXELS_LITERAL, POINTS_LITERAL, INCHES_LITERAL, CENTIMETERS_LITERAL,
    };

    /**
     * A public read-only list of all the '<em><b>Units Of Measurement</b></em>' enumerators. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Units Of Measurement</b></em>' literal with the specified name. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public static UnitsOfMeasurement get(String name)
    {
        for (int i = 0; i < VALUES_ARRAY.length; ++i)
        {
            UnitsOfMeasurement result = VALUES_ARRAY[i];
            if (result.toString().equals(name))
            {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Units Of Measurement</b></em>' literal with the specified value. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public static UnitsOfMeasurement get(int value)
    {
        switch (value)
        {
            case PIXELS:
                return PIXELS_LITERAL;
            case POINTS:
                return POINTS_LITERAL;
            case INCHES:
                return INCHES_LITERAL;
            case CENTIMETERS:
                return CENTIMETERS_LITERAL;
        }
        return null;
    }

    /**
     * Only this class can construct instances. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private UnitsOfMeasurement(int value, String name)
    {
        super(value, name);
    }

} //UnitsOfMeasurement
