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
 * <em><b>Trigger Condition</b></em>', and utility methods for working with
 * them. <!-- end-user-doc --> <!-- begin-model-doc -->
 * 
 * This type defines the allowed values for Trigger conditions (for
 * interactivity with chart). Any new condition needs to be added here to be
 * supported.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getTriggerCondition()
 * @model
 * @generated
 */
public final class TriggerCondition extends AbstractEnumerator
{

	/**
	 * The '<em><b>Mouse Hover</b></em>' literal value. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #MOUSE_HOVER_LITERAL
	 * @model name="Mouse_Hover"
	 * @generated
	 * @ordered
	 */
	public static final int MOUSE_HOVER = 0;

	/**
	 * The '<em><b>Mouse Click</b></em>' literal value. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #MOUSE_CLICK_LITERAL
	 * @model name="Mouse_Click"
	 * @generated
	 * @ordered
	 */
	public static final int MOUSE_CLICK = 1;

	/**
	 * The '<em><b>Mouse Hover</b></em>' literal object. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Mouse Hover</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #MOUSE_HOVER
	 * @generated
	 * @ordered
	 */
	public static final TriggerCondition MOUSE_HOVER_LITERAL = new TriggerCondition( MOUSE_HOVER,
			"Mouse_Hover" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Mouse Click</b></em>' literal object. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Mouse Click</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #MOUSE_CLICK
	 * @generated
	 * @ordered
	 */
	public static final TriggerCondition MOUSE_CLICK_LITERAL = new TriggerCondition( MOUSE_CLICK,
			"Mouse_Click" ); //$NON-NLS-1$

	/**
	 * An array of all the '<em><b>Trigger Condition</b></em>' enumerators.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final TriggerCondition[] VALUES_ARRAY = new TriggerCondition[]{
			MOUSE_HOVER_LITERAL, MOUSE_CLICK_LITERAL,
	};

	/**
	 * A public read-only list of all the '<em><b>Trigger Condition</b></em>'
	 * enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList( Arrays.asList( VALUES_ARRAY ) );

	/**
	 * Returns the '<em><b>Trigger Condition</b></em>' literal with the
	 * specified name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static TriggerCondition get( String name )
	{
		for ( int i = 0; i < VALUES_ARRAY.length; ++i )
		{
			TriggerCondition result = VALUES_ARRAY[i];
			if ( result.toString( ).equals( name ) )
			{
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Trigger Condition</b></em>' literal with the
	 * specified value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static TriggerCondition get( int value )
	{
		switch ( value )
		{
			case MOUSE_HOVER :
				return MOUSE_HOVER_LITERAL;
			case MOUSE_CLICK :
				return MOUSE_CLICK_LITERAL;
		}
		return null;
	}

	/**
	 * Only this class can construct instances. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	private TriggerCondition( int value, String name )
	{
		super( value, name );
	}

} //TriggerCondition
