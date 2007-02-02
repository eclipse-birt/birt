/**
 * <copyright>
 * </copyright>
 *
 * $Id: LegendBehaviorType.java,v 1.1 2006/12/28 03:49:24 anonymous Exp $
 */

package org.eclipse.birt.chart.model.attribute;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Legend Behavior Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * 
 * 			This type defines the allowed values for Legend behaviors.
 * <!-- end-model-doc -->
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getLegendBehaviorType()
 * @model
 * @generated
 */
public final class LegendBehaviorType extends AbstractEnumerator
{

	/**
	 * The '<em><b>None</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>None</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #NONE_LITERAL
	 * @model name="None"
	 * @generated
	 * @ordered
	 */
	public static final int NONE = 0;

	/**
	 * The '<em><b>Toggle Serie Visibility</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Toggle Serie Visibility</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #TOGGLE_SERIE_VISIBILITY_LITERAL
	 * @model name="ToggleSerieVisibility"
	 * @generated
	 * @ordered
	 */
	public static final int TOGGLE_SERIE_VISIBILITY = 1;

	/**
	 * The '<em><b>Highlight Serie</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Highlight Serie</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #HIGHLIGHT_SERIE_LITERAL
	 * @model name="HighlightSerie"
	 * @generated
	 * @ordered
	 */
	public static final int HIGHLIGHT_SERIE = 2;

	/**
	 * The '<em><b>None</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #NONE
	 * @generated
	 * @ordered
	 */
	public static final LegendBehaviorType NONE_LITERAL = new LegendBehaviorType( NONE,
			"None", "None" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Toggle Serie Visibility</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #TOGGLE_SERIE_VISIBILITY
	 * @generated
	 * @ordered
	 */
	public static final LegendBehaviorType TOGGLE_SERIE_VISIBILITY_LITERAL = new LegendBehaviorType( TOGGLE_SERIE_VISIBILITY,
			"ToggleSerieVisibility", "ToggleSerieVisibility" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Highlight Serie</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #HIGHLIGHT_SERIE
	 * @generated
	 * @ordered
	 */
	public static final LegendBehaviorType HIGHLIGHT_SERIE_LITERAL = new LegendBehaviorType( HIGHLIGHT_SERIE,
			"HighlightSerie", "HighlightSerie" ); //$NON-NLS-1$

	/**
	 * An array of all the '<em><b>Legend Behavior Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final LegendBehaviorType[] VALUES_ARRAY = new LegendBehaviorType[]{
			NONE_LITERAL,
			TOGGLE_SERIE_VISIBILITY_LITERAL,
			HIGHLIGHT_SERIE_LITERAL,
	};

	/**
	 * A public read-only list of all the '<em><b>Legend Behavior Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList( Arrays.asList( VALUES_ARRAY ) );

	/**
	 * Returns the '<em><b>Legend Behavior Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static LegendBehaviorType get( String literal )
	{
		for ( int i = 0; i < VALUES_ARRAY.length; ++i )
		{
			LegendBehaviorType result = VALUES_ARRAY[i];
			if ( result.toString( ).equals( literal ) )
			{
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Legend Behavior Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static LegendBehaviorType getByName( String name )
	{
		for ( int i = 0; i < VALUES_ARRAY.length; ++i )
		{
			LegendBehaviorType result = VALUES_ARRAY[i];
			if ( result.getName( ).equals( name ) )
			{
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Legend Behavior Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static LegendBehaviorType get( int value )
	{
		switch ( value )
		{
			case NONE :
				return NONE_LITERAL;
			case TOGGLE_SERIE_VISIBILITY :
				return TOGGLE_SERIE_VISIBILITY_LITERAL;
			case HIGHLIGHT_SERIE :
				return HIGHLIGHT_SERIE_LITERAL;
		}
		return null;
	}

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private LegendBehaviorType( int value, String name, String literal )
	{
		super( value, name, literal );
	}

} //LegendBehaviorType
