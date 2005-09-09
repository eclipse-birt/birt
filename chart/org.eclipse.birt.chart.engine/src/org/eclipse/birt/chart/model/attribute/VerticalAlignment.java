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
 * <em><b>Vertical Alignment</b></em>', and utility methods for working with
 * them. <!-- end-user-doc --> <!-- begin-model-doc -->
 * 
 * This type defines the allowed values for Vertical Text alignment.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getVerticalAlignment()
 * @model
 * @generated
 */
public final class VerticalAlignment extends AbstractEnumerator
{

	/**
	 * The '<em><b>Top</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #TOP_LITERAL
	 * @model name="Top"
	 * @generated
	 * @ordered
	 */
	public static final int TOP = 0;

	/**
	 * The '<em><b>Center</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CENTER_LITERAL
	 * @model name="Center"
	 * @generated
	 * @ordered
	 */
	public static final int CENTER = 1;

	/**
	 * The '<em><b>Bottom</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #BOTTOM_LITERAL
	 * @model name="Bottom"
	 * @generated
	 * @ordered
	 */
	public static final int BOTTOM = 2;

	/**
	 * The '<em><b>Top</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Top</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #TOP
	 * @generated
	 * @ordered
	 */
	public static final VerticalAlignment TOP_LITERAL = new VerticalAlignment( TOP,
			"Top" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Center</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Center</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #CENTER
	 * @generated
	 * @ordered
	 */
	public static final VerticalAlignment CENTER_LITERAL = new VerticalAlignment( CENTER,
			"Center" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Bottom</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Bottom</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #BOTTOM
	 * @generated
	 * @ordered
	 */
	public static final VerticalAlignment BOTTOM_LITERAL = new VerticalAlignment( BOTTOM,
			"Bottom" ); //$NON-NLS-1$

	/**
	 * An array of all the '<em><b>Vertical Alignment</b></em>' enumerators.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private static final VerticalAlignment[] VALUES_ARRAY = new VerticalAlignment[]{
			TOP_LITERAL, CENTER_LITERAL, BOTTOM_LITERAL,
	};

	/**
	 * A public read-only list of all the '<em><b>Vertical Alignment</b></em>' enumerators.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList( Arrays.asList( VALUES_ARRAY ) );

	/**
	 * Returns the '<em><b>Vertical Alignment</b></em>' literal with the specified name.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static VerticalAlignment get( String name )
	{
		for ( int i = 0; i < VALUES_ARRAY.length; ++i )
		{
			VerticalAlignment result = VALUES_ARRAY[i];
			if ( result.toString( ).equals( name ) )
			{
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Vertical Alignment</b></em>' literal with the specified value.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static VerticalAlignment get( int value )
	{
		switch ( value )
		{
			case TOP :
				return TOP_LITERAL;
			case CENTER :
				return CENTER_LITERAL;
			case BOTTOM :
				return BOTTOM_LITERAL;
		}
		return null;
	}

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 */
	private VerticalAlignment( int value, String name )
	{
		super( value, name );
	}

} //VerticalAlignment
