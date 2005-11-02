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
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '<em><b>Styled Component</b></em>',
 * and utility methods for working with them. <!-- end-user-doc --> <!-- begin-model-doc -->
 * 
 * This type represents the possible values for the legend item type.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getStyledComponent()
 * @model
 * @generated
 */
public final class StyledComponent extends AbstractEnumerator
{

	/**
	 * The '<em><b>Chart All</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Chart All</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #CHART_ALL_LITERAL
	 * @model name="Chart_All"
	 * @generated
	 * @ordered
	 */
	public static final int CHART_ALL = 0;

	/**
	 * The '<em><b>Chart Title</b></em>' literal value.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #CHART_TITLE_LITERAL
	 * @model name="Chart_Title"
	 * @generated
	 * @ordered
	 */
	public static final int CHART_TITLE = 1;

	/**
	 * The '<em><b>Chart Background</b></em>' literal value.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #CHART_BACKGROUND_LITERAL
	 * @model name="Chart_Background"
	 * @generated
	 * @ordered
	 */
	public static final int CHART_BACKGROUND = 2;

	/**
	 * The '<em><b>Plot Background</b></em>' literal value.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #PLOT_BACKGROUND_LITERAL
	 * @model name="Plot_Background"
	 * @generated
	 * @ordered
	 */
	public static final int PLOT_BACKGROUND = 3;

	/**
	 * The '<em><b>Legend Background</b></em>' literal value.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #LEGEND_BACKGROUND_LITERAL
	 * @model name="Legend_Background"
	 * @generated
	 * @ordered
	 */
	public static final int LEGEND_BACKGROUND = 4;

	/**
	 * The '<em><b>Legend Label</b></em>' literal value.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #LEGEND_LABEL_LITERAL
	 * @model name="Legend_Label"
	 * @generated
	 * @ordered
	 */
	public static final int LEGEND_LABEL = 5;

	/**
	 * The '<em><b>Axis Title</b></em>' literal value.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #AXIS_TITLE_LITERAL
	 * @model name="Axis_Title"
	 * @generated
	 * @ordered
	 */
	public static final int AXIS_TITLE = 6;

	/**
	 * The '<em><b>Axis Label</b></em>' literal value.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #AXIS_LABEL_LITERAL
	 * @model name="Axis_Label"
	 * @generated
	 * @ordered
	 */
	public static final int AXIS_LABEL = 7;

	/**
	 * The '<em><b>Axis Line</b></em>' literal value.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #AXIS_LINE_LITERAL
	 * @model name="Axis_Line"
	 * @generated
	 * @ordered
	 */
	public static final int AXIS_LINE = 8;

	/**
	 * The '<em><b>Series Title</b></em>' literal value.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #SERIES_TITLE_LITERAL
	 * @model name="Series_Title"
	 * @generated
	 * @ordered
	 */
	public static final int SERIES_TITLE = 9;

	/**
	 * The '<em><b>Series Label</b></em>' literal value.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #SERIES_LABEL_LITERAL
	 * @model name="Series_Label"
	 * @generated
	 * @ordered
	 */
	public static final int SERIES_LABEL = 10;

	/**
	 * The '<em><b>Chart All</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CHART_ALL
	 * @generated
	 * @ordered
	 */
	public static final StyledComponent CHART_ALL_LITERAL = new StyledComponent( CHART_ALL,
			"Chart_All" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Chart Title</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Chart Title</b></em>' literal object isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #CHART_TITLE
	 * @generated
	 * @ordered
	 */
	public static final StyledComponent CHART_TITLE_LITERAL = new StyledComponent( CHART_TITLE,
			"Chart_Title" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Chart Background</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Chart Background</b></em>' literal object isn't clear, there really should be more
	 * of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #CHART_BACKGROUND
	 * @generated
	 * @ordered
	 */
	public static final StyledComponent CHART_BACKGROUND_LITERAL = new StyledComponent( CHART_BACKGROUND,
			"Chart_Background" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Plot Background</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Plot Background</b></em>' literal object isn't clear, there really should be more
	 * of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #PLOT_BACKGROUND
	 * @generated
	 * @ordered
	 */
	public static final StyledComponent PLOT_BACKGROUND_LITERAL = new StyledComponent( PLOT_BACKGROUND,
			"Plot_Background" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Legend Background</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Legend Background</b></em>' literal object isn't clear, there really should be
	 * more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #LEGEND_BACKGROUND
	 * @generated
	 * @ordered
	 */
	public static final StyledComponent LEGEND_BACKGROUND_LITERAL = new StyledComponent( LEGEND_BACKGROUND,
			"Legend_Background" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Legend Label</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Legend Label</b></em>' literal object isn't clear, there really should be more of
	 * a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #LEGEND_LABEL
	 * @generated
	 * @ordered
	 */
	public static final StyledComponent LEGEND_LABEL_LITERAL = new StyledComponent( LEGEND_LABEL,
			"Legend_Label" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Axis Title</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Axis Title</b></em>' literal object isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #AXIS_TITLE
	 * @generated
	 * @ordered
	 */
	public static final StyledComponent AXIS_TITLE_LITERAL = new StyledComponent( AXIS_TITLE,
			"Axis_Title" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Axis Label</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Axis Label</b></em>' literal object isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #AXIS_LABEL
	 * @generated
	 * @ordered
	 */
	public static final StyledComponent AXIS_LABEL_LITERAL = new StyledComponent( AXIS_LABEL,
			"Axis_Label" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Axis Line</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Axis Line</b></em>' literal object isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #AXIS_LINE
	 * @generated
	 * @ordered
	 */
	public static final StyledComponent AXIS_LINE_LITERAL = new StyledComponent( AXIS_LINE,
			"Axis_Line" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Series Title</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Series Title</b></em>' literal object isn't clear, there really should be more of
	 * a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #SERIES_TITLE
	 * @generated
	 * @ordered
	 */
	public static final StyledComponent SERIES_TITLE_LITERAL = new StyledComponent( SERIES_TITLE,
			"Series_Title" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Series Label</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Series Label</b></em>' literal object isn't clear, there really should be more of
	 * a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #SERIES_LABEL
	 * @generated
	 * @ordered
	 */
	public static final StyledComponent SERIES_LABEL_LITERAL = new StyledComponent( SERIES_LABEL,
			"Series_Label" ); //$NON-NLS-1$

	/**
	 * An array of all the '<em><b>Styled Component</b></em>' enumerators.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 */
	private static final StyledComponent[] VALUES_ARRAY = new StyledComponent[]{
			CHART_ALL_LITERAL,
			CHART_TITLE_LITERAL,
			CHART_BACKGROUND_LITERAL,
			PLOT_BACKGROUND_LITERAL,
			LEGEND_BACKGROUND_LITERAL,
			LEGEND_LABEL_LITERAL,
			AXIS_TITLE_LITERAL,
			AXIS_LABEL_LITERAL,
			AXIS_LINE_LITERAL,
			SERIES_TITLE_LITERAL,
			SERIES_LABEL_LITERAL,
	};

	/**
	 * A public read-only list of all the '<em><b>Styled Component</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList( Arrays.asList( VALUES_ARRAY ) );

	/**
	 * Returns the '<em><b>Styled Component</b></em>' literal with the specified name.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 */
	public static StyledComponent get( String name )
	{
		for ( int i = 0; i < VALUES_ARRAY.length; ++i )
		{
			StyledComponent result = VALUES_ARRAY[i];
			if ( result.toString( ).equals( name ) )
			{
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Styled Component</b></em>' literal with the specified value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static StyledComponent get( int value )
	{
		switch ( value )
		{
			case CHART_ALL :
				return CHART_ALL_LITERAL;
			case CHART_TITLE :
				return CHART_TITLE_LITERAL;
			case CHART_BACKGROUND :
				return CHART_BACKGROUND_LITERAL;
			case PLOT_BACKGROUND :
				return PLOT_BACKGROUND_LITERAL;
			case LEGEND_BACKGROUND :
				return LEGEND_BACKGROUND_LITERAL;
			case LEGEND_LABEL :
				return LEGEND_LABEL_LITERAL;
			case AXIS_TITLE :
				return AXIS_TITLE_LITERAL;
			case AXIS_LABEL :
				return AXIS_LABEL_LITERAL;
			case AXIS_LINE :
				return AXIS_LINE_LITERAL;
			case SERIES_TITLE :
				return SERIES_TITLE_LITERAL;
			case SERIES_LABEL :
				return SERIES_LABEL_LITERAL;
		}
		return null;
	}

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private StyledComponent( int value, String name )
	{
		super( value, name );
	}

} //StyledComponent
