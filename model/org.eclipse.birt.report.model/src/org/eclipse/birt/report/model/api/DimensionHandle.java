/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.metadata.Choice;
import org.eclipse.birt.report.model.metadata.ChoiceSet;
import org.eclipse.birt.report.model.metadata.DimensionPropertyType;
import org.eclipse.birt.report.model.metadata.DimensionValue;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

/**
 * Simplifies working with dimension properties. A dimension property consists
 * of a measure and a dimension. This handle helps assemble and disassemble
 * dimension property values. The dimension property itself can be either a
 * top-level element property, or the member of a property structure.
 * <p>
 * Note that this handle cannot translate a dimension property into a physical
 * dimension. BIRT uses the CSS dimension system and requires a CSS User Agent
 * (UA) to compute the physical layout of a report given a report design. These
 * calculations often require context (to compute relative dimensions) and
 * knowledge of item contents to compute the sizes of items that expand to fit
 * their content.
 * <p>
 * This handle works with individual dimensions, the application-provided UA
 * uses these properties (and information about the overall report design) to
 * produce physical, absolute dimensions.
 * <p>
 * The application generally does not create dimension handles directly. It uses
 * the method in <code>DesignElementHandle</code> to get a dimension handle.
 * For example:
 * 
 * <pre>
 * 
 * DesignElementHandle elementHandle = element.handle( );DimensionHandle dimensionHandle = elementHandle.getDimensionProperty( Style.FONT_SIZE_PROP );
 * 
 *   
 *  
 * </pre>
 * 
 * <p>
 * The value of the dimension can be a standard format such as 1pt, 100% etc.
 * This kind of value represents a standard dimension, or it can be a CSS
 * (predefined) value such as XX-SMALL, X-SMALL. The CSS values are defined in
 * {@link org.eclipse.birt.report.model.elements.DesignChoiceConstants}.
 * 
 * @see org.eclipse.birt.report.model.elements.DesignChoiceConstants
 */

public class DimensionHandle extends ComplexValueHandle
{

	/**
	 * Constructs a dimension handle for a member of a structure. This member
	 * must be a dimension type.
	 * 
	 * @param element
	 *            the design element handle
	 * @param memberRef
	 *            the memberRef for the member property
	 */

	public DimensionHandle( DesignElementHandle element, MemberRef memberRef )
	{
		super( element, memberRef );
		assert memberRef.getMemberDefn( ).getType( ) instanceof DimensionPropertyType;
	}

	/**
	 * Constructs a dimension handle for a element property. This property must
	 * be a dimension type.
	 * 
	 * @param element
	 *            handle to the element that defined the property.
	 * @param thePropDefn
	 *            definition of the dimension property.
	 */

	public DimensionHandle( DesignElementHandle element,
			ElementPropertyDefn thePropDefn )
	{
		super( element, thePropDefn );
		assert thePropDefn.getType( ) instanceof DimensionPropertyType;
	}

	/**
	 * Determines if the dimension is given by a standard format or by a
	 * pre-defined constant.
	 * 
	 * @return <code>true</code> if the dimension is given by an pre-defined
	 *         constant <code>false</code> if the dimension is given by a
	 *         standard dimension.
	 */

	public boolean isKeyword( )
	{
		ChoiceSet choiceSet = propDefn.getChoices( );
		if ( choiceSet != null && choiceSet.contains( getStringValue( ) ) )
			return true;

		return false;
	}

	/**
	 * Returns the numeric measure part of the dimension. For example, if the
	 * dimension value is "2.3cm", the measure is 2.3.
	 * 
	 * @return the numeric measure of the dimension, return <code>0.0</code>
	 *         if the dimension from an choice.
	 */

	public double getMeasure( )
	{
		if ( isKeyword( ) )
		{
			// Map a pre-defined choice to a zero value.
			return 0.0;
		}

		DimensionValue value = (DimensionValue) getValue( );
		if ( value != null )
			return value.getMeasure( );

		return 0.0;
	}

	/**
	 * Returns an array of allowed units. The set of allowed units depends on
	 * context, not all properties allow all units.
	 * 
	 * @return an array of allowed unit suffixes. Each suffix is a string.
	 */

	public Choice[] getAllowedUnits( )
	{
		if ( memberRef == null )
			return propDefn.getAllowedChoices( ).getChoices( );

		return memberRef.getMemberDefn( ).getAllowedChoices( ).getChoices( );
	}

	/**
	 * Returns the code for the units portion of the dimension. For example, if
	 * the dimension value is "2.3cm", then the unit is "cm".
	 * 
	 * @return the units portion of the dimension. Return
	 *         <code>DimensionValue.DEFAULT_UNIT</code> if the dimension is a
	 *         predefined constant.
	 */

	public String getUnits( )
	{
		if ( isKeyword( ) )
		{
			return DimensionValue.DEFAULT_UNIT;
		}

		DimensionValue value = (DimensionValue) getValue( );

		if ( value != null )
			return value.getUnits( );
		return null;
	}

	/**
	 * Sets the value of a dimension in application units. Use this to set a
	 * value through direct manipulation, such as drag & drop.
	 * 
	 * @param value
	 *            the new value in application units.
	 * @deprecated
	 */

	public void setAbsolute( double value )
	{
		try
		{
			setValue( new Double( value ) );
		}
		catch ( SemanticException e )
		{
			// double value does not fail

			assert false;
		}
	}

	/**
	 * Returns the font size for computing relative value of style property. The
	 * font size value is retrieved from the element which actually has the
	 * value of this dimension property. At one of the following conditions, the
	 * font size value is returned from the current element with the normal
	 * property search algorithm:
	 * <ul>
	 * <li>If this property is not a style property
	 * <li>If this element is style
	 * <li>if the unit of this property is not either of
	 * <code>DesignChoiceConstants.UNITS_EM</code>,
	 * <code>DesignChoiceConstants.UNITS_EX</code> and
	 * <code>DesignChoiceConstants.UNITS_PERCENTAGE</code>
	 * </ul>
	 * <p>
	 * CSS 2.1 specification has the following statements:
	 * <p>
	 * Child elements do not inherit the relative values specified for their
	 * parent; they inherit the computed values.
	 * <p>
	 * Example(s):
	 * <p>
	 * In the following rules, the computed 'text-indent' value of "h1" elements
	 * will be 36px, not 45px, if "h1" is a child of the "body" element.
	 * <p>
	 * body { font-size: 12px; text-indent: 3em; /* i.e., 36px}}
	 * <p>
	 * h1 { font-size: 15px }
	 * <p>
	 * 
	 * So when computing the value of text-indent, with this method, the value
	 * of font-size is retrieved from body, instead of h1.
	 * 
	 * @return the actual font size for computing relative value.
	 */

	public Object getFactualFontSize( )
	{
		DesignElement element = getElement( );
		ReportDesign design = elementHandle.getDesign( );

		if ( !propDefn.isStyleProperty( ) || element.isStyle( ) )
			return element.getProperty( design, Style.FONT_SIZE_PROP );

		if ( !DesignChoiceConstants.UNITS_EM.equalsIgnoreCase( getUnits( ) )
				&& !DesignChoiceConstants.UNITS_EX
						.equalsIgnoreCase( getUnits( ) )
				&& !DesignChoiceConstants.UNITS_PERCENTAGE
						.equalsIgnoreCase( getUnits( ) ) )
			return element.getProperty( design, Style.FONT_SIZE_PROP );

		Object propValue = null;
		Object fontSizeValue = null;

		DesignElement e = element;
		ElementPropertyDefn fontSizeDefn = e
				.getPropertyDefn( Style.FONT_SIZE_PROP );

		// Located the element which has the property this dimension represents.

		while ( e != null )
		{
			propValue = e.getPropertyFromElement( design, propDefn );
			if ( propValue != null )
				break;

			// If the property this dimension represents can not be inherited.

			if ( !propDefn.canInherit( ) )
				break;

			e = e.getContainer( );
		}

		if ( propValue != null )
			fontSizeValue = e.getProperty( design, fontSizeDefn );

		if ( fontSizeValue == null )
			fontSizeValue = element.getDefaultValue( design, fontSizeDefn );

		return fontSizeValue;
	}
}