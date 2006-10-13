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

import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '
 * <em><b>Riser Type</b></em>', and utility methods for working with them.
 * <!-- end-user-doc --> <!-- begin-model-doc -->
 * 
 * This type represents the possible values for risers supported for Bar Series.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getRiserType()
 * @model
 * @generated
 */
public final class RiserType extends AbstractEnumerator
{

	/**
	 * The '<em><b>Rectangle</b></em>' literal value.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @see #RECTANGLE_LITERAL
	 * @model name="Rectangle"
	 * @generated
	 * @ordered
	 */
	public static final int RECTANGLE = 0;

	/**
	 * The '<em><b>Triangle</b></em>' literal value.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @see #TRIANGLE_LITERAL
	 * @model name="Triangle"
	 * @generated
	 * @ordered
	 */
	public static final int TRIANGLE = 1;

	/**
	 * The '<em><b>Tube</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Tube</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #TUBE_LITERAL
	 * @model name="Tube"
	 * @generated
	 * @ordered
	 */
	public static final int TUBE = 2;

	/**
	 * The '<em><b>Rectangle</b></em>' literal object.
	 * <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of '<em><b>Rectangle</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #RECTANGLE
	 * @generated
	 * @ordered
	 */
	public static final RiserType RECTANGLE_LITERAL = new RiserType( RECTANGLE,
			"Rectangle", "Rectangle" ); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Triangle</b></em>' literal object.
	 * <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of '<em><b>Triangle</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #TRIANGLE
	 * @generated
	 * @ordered
	 */
	public static final RiserType TRIANGLE_LITERAL = new RiserType( TRIANGLE,
			"Triangle", "Triangle" ); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Tube</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #TUBE
	 * @generated
	 * @ordered
	 */
	public static final RiserType TUBE_LITERAL = new RiserType( TUBE,
			"Tube", "Tube" ); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * An array of all the '<em><b>Riser Type</b></em>' enumerators. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final RiserType[] VALUES_ARRAY = new RiserType[]{
			RECTANGLE_LITERAL, TRIANGLE_LITERAL, TUBE_LITERAL,
	};

	/**
	 * A public read-only list of all the '<em><b>Riser Type</b></em>' enumerators.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList( Arrays.asList( VALUES_ARRAY ) );

	/**
	 * Returns the '<em><b>Riser Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static RiserType get( String literal )
	{
		for ( int i = 0; i < VALUES_ARRAY.length; ++i )
		{
			RiserType result = VALUES_ARRAY[i];
			if ( result.toString( ).equals( literal ) )
			{
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Riser Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static RiserType getByName( String name )
	{
		for ( int i = 0; i < VALUES_ARRAY.length; ++i )
		{
			RiserType result = VALUES_ARRAY[i];
			if ( result.getName( ).equals( name ) )
			{
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Riser Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static RiserType get( int value )
	{
		switch ( value )
		{
			case RECTANGLE :
				return RECTANGLE_LITERAL;
			case TRIANGLE :
				return TRIANGLE_LITERAL;
			case TUBE :
				return TUBE_LITERAL;
		}
		return null;
	}

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private RiserType( int value, String name, String literal )
	{
		super( value, name, literal );
	}

} //RiserType
