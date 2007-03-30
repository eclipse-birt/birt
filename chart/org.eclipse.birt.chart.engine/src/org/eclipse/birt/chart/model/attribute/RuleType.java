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
 * <em><b>Rule Type</b></em>', and utility methods for working with them.
 * <!-- end-user-doc --> <!-- begin-model-doc -->
 * 
 * This type represents the possible rule types for use in Query objects.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getRuleType()
 * @model
 * @deprecated only reserved for compatibility
 */
public final class RuleType extends AbstractEnumerator
{

	/**
	 * The '<em><b>Filter</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #FILTER_LITERAL
	 * @model name="Filter"
	 * @generated
	 * @ordered
	 */
	public static final int FILTER = 0;

	/**
	 * The '<em><b>Suppress</b></em>' literal value.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @see #SUPPRESS_LITERAL
	 * @model name="Suppress"
	 * @generated
	 * @ordered
	 */
	public static final int SUPPRESS = 1;

	/**
	 * The '<em><b>Link</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #LINK_LITERAL
	 * @model name="Link"
	 * @generated
	 * @ordered
	 */
	public static final int LINK = 2;

	/**
	 * The '<em><b>Filter</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Filter</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #FILTER
	 * @generated
	 * @ordered
	 */
	public static final RuleType FILTER_LITERAL = new RuleType( FILTER,
			"Filter", "Filter" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Suppress</b></em>' literal object.
	 * <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of '<em><b>Suppress</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #SUPPRESS
	 * @generated
	 * @ordered
	 */
	public static final RuleType SUPPRESS_LITERAL = new RuleType( SUPPRESS,
			"Suppress", "Suppress" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Link</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Link</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #LINK
	 * @generated
	 * @ordered
	 */
	public static final RuleType LINK_LITERAL = new RuleType( LINK,
			"Link", "Link" ); //$NON-NLS-1$

	/**
	 * An array of all the '<em><b>Rule Type</b></em>' enumerators. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final RuleType[] VALUES_ARRAY = new RuleType[]{
			FILTER_LITERAL, SUPPRESS_LITERAL, LINK_LITERAL,
	};

	/**
	 * A public read-only list of all the '<em><b>Rule Type</b></em>' enumerators.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList( Arrays.asList( VALUES_ARRAY ) );

	/**
	 * Returns the '<em><b>Rule Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static RuleType get( String literal )
	{
		for ( int i = 0; i < VALUES_ARRAY.length; ++i )
		{
			RuleType result = VALUES_ARRAY[i];
			if ( result.toString( ).equals( literal ) )
			{
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Rule Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static RuleType getByName( String name )
	{
		for ( int i = 0; i < VALUES_ARRAY.length; ++i )
		{
			RuleType result = VALUES_ARRAY[i];
			if ( result.getName( ).equals( name ) )
			{
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Rule Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static RuleType get( int value )
	{
		switch ( value )
		{
			case FILTER :
				return FILTER_LITERAL;
			case SUPPRESS :
				return SUPPRESS_LITERAL;
			case LINK :
				return LINK_LITERAL;
		}
		return null;
	}

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private RuleType( int value, String name, String literal )
	{
		super( value, name, literal );
	}

} //RuleType
