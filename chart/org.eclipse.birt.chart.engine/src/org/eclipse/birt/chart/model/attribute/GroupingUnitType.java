/**
 * <copyright>
 * </copyright>
 *
 * $Id: GroupingUnitType.java,v 1.1 2006/12/28 03:49:25 anonymous Exp $
 */

package org.eclipse.birt.chart.model.attribute;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '
 * <em><b>Grouping Unit Type</b></em>', and utility methods for working with
 * them. <!-- end-user-doc --> <!-- begin-model-doc -->
 * 
 * This type represents the possible units for grouping data.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getGroupingUnitType()
 * @model
 * @generated
 */
public final class GroupingUnitType extends AbstractEnumerator
{

	/**
	 * The '<em><b>Seconds</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #SECONDS_LITERAL
	 * @model name="Seconds"
	 * @generated
	 * @ordered
	 */
	public static final int SECONDS = 0;

	/**
	 * The '<em><b>Minutes</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MINUTES_LITERAL
	 * @model name="Minutes"
	 * @generated
	 * @ordered
	 */
	public static final int MINUTES = 1;

	/**
	 * The '<em><b>Hours</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #HOURS_LITERAL
	 * @model name="Hours"
	 * @generated
	 * @ordered
	 */
	public static final int HOURS = 2;

	/**
	 * The '<em><b>Days</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DAYS_LITERAL
	 * @model name="Days"
	 * @generated
	 * @ordered
	 */
	public static final int DAYS = 3;

	/**
	 * The '<em><b>Weeks</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #WEEKS_LITERAL
	 * @model name="Weeks"
	 * @generated
	 * @ordered
	 */
	public static final int WEEKS = 4;

	/**
	 * The '<em><b>Months</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MONTHS_LITERAL
	 * @model name="Months"
	 * @generated
	 * @ordered
	 */
	public static final int MONTHS = 5;

	/**
	 * The '<em><b>Years</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #YEARS_LITERAL
	 * @model name="Years"
	 * @generated
	 * @ordered
	 */
	public static final int YEARS = 6;

	/**
	 * The '<em><b>Seconds</b></em>' literal object.
	 * <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of '<em><b>Seconds</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #SECONDS
	 * @generated
	 * @ordered
	 */
	public static final GroupingUnitType SECONDS_LITERAL = new GroupingUnitType( SECONDS,
			"Seconds", "Seconds" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Minutes</b></em>' literal object.
	 * <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of '<em><b>Minutes</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MINUTES
	 * @generated
	 * @ordered
	 */
	public static final GroupingUnitType MINUTES_LITERAL = new GroupingUnitType( MINUTES,
			"Minutes", "Minutes" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Hours</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Hours</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #HOURS
	 * @generated
	 * @ordered
	 */
	public static final GroupingUnitType HOURS_LITERAL = new GroupingUnitType( HOURS,
			"Hours", "Hours" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Days</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Days</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DAYS
	 * @generated
	 * @ordered
	 */
	public static final GroupingUnitType DAYS_LITERAL = new GroupingUnitType( DAYS,
			"Days", "Days" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Weeks</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Weeks</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #WEEKS
	 * @generated
	 * @ordered
	 */
	public static final GroupingUnitType WEEKS_LITERAL = new GroupingUnitType( WEEKS,
			"Weeks", "Weeks" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Months</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Months</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MONTHS
	 * @generated
	 * @ordered
	 */
	public static final GroupingUnitType MONTHS_LITERAL = new GroupingUnitType( MONTHS,
			"Months", "Months" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Years</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Years</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #YEARS
	 * @generated
	 * @ordered
	 */
	public static final GroupingUnitType YEARS_LITERAL = new GroupingUnitType( YEARS,
			"Years", "Years" ); //$NON-NLS-1$

	/**
	 * An array of all the '<em><b>Grouping Unit Type</b></em>' enumerators.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private static final GroupingUnitType[] VALUES_ARRAY = new GroupingUnitType[]{
			SECONDS_LITERAL,
			MINUTES_LITERAL,
			HOURS_LITERAL,
			DAYS_LITERAL,
			WEEKS_LITERAL,
			MONTHS_LITERAL,
			YEARS_LITERAL,
	};

	/**
	 * A public read-only list of all the '<em><b>Grouping Unit Type</b></em>' enumerators.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList( Arrays.asList( VALUES_ARRAY ) );

	/**
	 * Returns the '<em><b>Grouping Unit Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static GroupingUnitType get( String literal )
	{
		for ( int i = 0; i < VALUES_ARRAY.length; ++i )
		{
			GroupingUnitType result = VALUES_ARRAY[i];
			if ( result.toString( ).equals( literal ) )
			{
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Grouping Unit Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static GroupingUnitType getByName( String name )
	{
		for ( int i = 0; i < VALUES_ARRAY.length; ++i )
		{
			GroupingUnitType result = VALUES_ARRAY[i];
			if ( result.getName( ).equals( name ) )
			{
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Grouping Unit Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static GroupingUnitType get( int value )
	{
		switch ( value )
		{
			case SECONDS :
				return SECONDS_LITERAL;
			case MINUTES :
				return MINUTES_LITERAL;
			case HOURS :
				return HOURS_LITERAL;
			case DAYS :
				return DAYS_LITERAL;
			case WEEKS :
				return WEEKS_LITERAL;
			case MONTHS :
				return MONTHS_LITERAL;
			case YEARS :
				return YEARS_LITERAL;
		}
		return null;
	}

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private GroupingUnitType( int value, String name, String literal )
	{
		super( value, name, literal );
	}

} //GroupingUnitType
