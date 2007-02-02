/**
 * <copyright>
 * </copyright>
 *
 * $Id: DataType.java,v 1.1 2006/12/28 03:49:24 anonymous Exp $
 */

package org.eclipse.birt.chart.model.attribute;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '
 * <em><b>Data Type</b></em>', and utility methods for working with them.
 * <!-- end-user-doc --> <!-- begin-model-doc -->
 * 
 * This type represents the possible data type values associated with chart
 * components.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getDataType()
 * @model
 * @generated
 */
public final class DataType extends AbstractEnumerator
{

	/**
	 * The '<em><b>Numeric</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #NUMERIC_LITERAL
	 * @model name="Numeric"
	 * @generated
	 * @ordered
	 */
	public static final int NUMERIC = 0;

	/**
	 * The '<em><b>Date Time</b></em>' literal value.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @see #DATE_TIME_LITERAL
	 * @model name="DateTime"
	 * @generated
	 * @ordered
	 */
	public static final int DATE_TIME = 1;

	/**
	 * The '<em><b>Text</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #TEXT_LITERAL
	 * @model name="Text"
	 * @generated
	 * @ordered
	 */
	public static final int TEXT = 2;

	/**
	 * The '<em><b>Numeric</b></em>' literal object.
	 * <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of '<em><b>Numeric</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #NUMERIC
	 * @generated
	 * @ordered
	 */
	public static final DataType NUMERIC_LITERAL = new DataType( NUMERIC,
			"Numeric", "Numeric" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Date Time</b></em>' literal object.
	 * <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of '<em><b>Date Time</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DATE_TIME
	 * @generated
	 * @ordered
	 */
	public static final DataType DATE_TIME_LITERAL = new DataType( DATE_TIME,
			"DateTime", "DateTime" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Text</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Text</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #TEXT
	 * @generated
	 * @ordered
	 */
	public static final DataType TEXT_LITERAL = new DataType( TEXT,
			"Text", "Text" ); //$NON-NLS-1$

	/**
	 * An array of all the '<em><b>Data Type</b></em>' enumerators. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final DataType[] VALUES_ARRAY = new DataType[]{
			NUMERIC_LITERAL, DATE_TIME_LITERAL, TEXT_LITERAL,
	};

	/**
	 * A public read-only list of all the '<em><b>Data Type</b></em>' enumerators.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList( Arrays.asList( VALUES_ARRAY ) );

	/**
	 * Returns the '<em><b>Data Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static DataType get( String literal )
	{
		for ( int i = 0; i < VALUES_ARRAY.length; ++i )
		{
			DataType result = VALUES_ARRAY[i];
			if ( result.toString( ).equals( literal ) )
			{
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Data Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static DataType getByName( String name )
	{
		for ( int i = 0; i < VALUES_ARRAY.length; ++i )
		{
			DataType result = VALUES_ARRAY[i];
			if ( result.getName( ).equals( name ) )
			{
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Data Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static DataType get( int value )
	{
		switch ( value )
		{
			case NUMERIC :
				return NUMERIC_LITERAL;
			case DATE_TIME :
				return DATE_TIME_LITERAL;
			case TEXT :
				return TEXT_LITERAL;
		}
		return null;
	}

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private DataType( int value, String name, String literal )
	{
		super( value, name, literal );
	}

} //DataType
