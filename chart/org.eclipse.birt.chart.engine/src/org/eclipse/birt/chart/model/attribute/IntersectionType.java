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
 * <em><b>Intersection Type</b></em>', and utility methods for working with
 * them. <!-- end-user-doc --> <!-- begin-model-doc -->
 * 
 * This type represents the possible values for axis intersection locations.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getIntersectionType()
 * @model
 * @generated
 */
public final class IntersectionType extends AbstractEnumerator
{

	/**
	 * The '<em><b>Min</b></em>' literal value. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #MIN_LITERAL
	 * @model name="Min"
	 * @generated
	 * @ordered
	 */
	public static final int MIN = 0;

	/**
	 * The '<em><b>Max</b></em>' literal value. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #MAX_LITERAL
	 * @model name="Max"
	 * @generated
	 * @ordered
	 */
	public static final int MAX = 1;

	/**
	 * The '<em><b>Value</b></em>' literal value. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #VALUE_LITERAL
	 * @model name="Value"
	 * @generated
	 * @ordered
	 */
	public static final int VALUE = 2;

	/**
	 * The '<em><b>Min</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Min</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #MIN
	 * @generated
	 * @ordered
	 */
	public static final IntersectionType MIN_LITERAL = new IntersectionType( MIN,
			"Min" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Max</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Max</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #MAX
	 * @generated
	 * @ordered
	 */
	public static final IntersectionType MAX_LITERAL = new IntersectionType( MAX,
			"Max" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Value</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Value</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #VALUE
	 * @generated
	 * @ordered
	 */
	public static final IntersectionType VALUE_LITERAL = new IntersectionType( VALUE,
			"Value" ); //$NON-NLS-1$

	/**
	 * An array of all the '<em><b>Intersection Type</b></em>' enumerators.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final IntersectionType[] VALUES_ARRAY = new IntersectionType[]{
			MIN_LITERAL, MAX_LITERAL, VALUE_LITERAL,
	};

	/**
	 * A public read-only list of all the '<em><b>Intersection Type</b></em>'
	 * enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList( Arrays.asList( VALUES_ARRAY ) );

	/**
	 * Returns the '<em><b>Intersection Type</b></em>' literal with the
	 * specified name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static IntersectionType get( String name )
	{
		for ( int i = 0; i < VALUES_ARRAY.length; ++i )
		{
			IntersectionType result = VALUES_ARRAY[i];
			if ( result.toString( ).equals( name ) )
			{
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Intersection Type</b></em>' literal with the
	 * specified value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static IntersectionType get( int value )
	{
		switch ( value )
		{
			case MIN :
				return MIN_LITERAL;
			case MAX :
				return MAX_LITERAL;
			case VALUE :
				return VALUE_LITERAL;
		}
		return null;
	}

	/**
	 * Only this class can construct instances. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	private IntersectionType( int value, String name )
	{
		super( value, name );
	}

} //IntersectionType
