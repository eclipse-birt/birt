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

package org.eclipse.birt.report.model.elements;

import java.util.List;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
import org.eclipse.birt.report.model.api.util.Point;
import org.eclipse.birt.report.model.api.util.Rectangle;
import org.eclipse.birt.report.model.api.validators.MasterPageSizeValidator;
import org.eclipse.birt.report.model.api.validators.MasterPageTypeValidator;
import org.eclipse.birt.report.model.core.StyledElement;

/**
 * This class represents a Master Page element in the report design. This class
 * provides methods to access the most common properties. Use the
 * {@link org.eclipse.birt.report.model.api.MasterPageHandle}class to change
 * the properties.
 * 
 */

public abstract class MasterPage extends StyledElement
{

	/**
	 * Name of the page type property. This gives a name to the page size such
	 * as A4 or US Letter.
	 */

	public static final String TYPE_PROP = "type"; //$NON-NLS-1$

	/**
	 * Name of the property that gives the orientation of a standard-sized page.
	 * Ignored for custom-sized pages.
	 */

	public static final String ORIENTATION_PROP = "orientation"; //$NON-NLS-1$
	/**
	 * The name of the custom height property set when using a custom-sized
	 * page. Ignored for standard-sized pages.
	 */

	public static final String HEIGHT_PROP = "height"; //$NON-NLS-1$

	/**
	 * The name of the custom width property set when using a custom-sized page.
	 * Ignored for standard-sized pages.
	 */

	public static final String WIDTH_PROP = "width"; //$NON-NLS-1$

	/**
	 * Name of the dimension property that gives the amount of space between the
	 * bottom of the page and the page content.
	 */

	public static final String BOTTOM_MARGIN_PROP = "bottomMargin"; //$NON-NLS-1$

	/**
	 * Name of the dimension property that gives the amount of space between the
	 * right of the page and the page content.
	 */

	public static final String RIGHT_MARGIN_PROP = "rightMargin"; //$NON-NLS-1$

	/**
	 * Name of the dimension property that gives the amount of space between the
	 * top of the page and the page content.
	 */

	public static final String TOP_MARGIN_PROP = "topMargin"; //$NON-NLS-1$

	/**
	 * Name of the dimension property that gives the amount of space between the
	 * left of the page and the page content.
	 */

	public static final String LEFT_MARGIN_PROP = "leftMargin"; //$NON-NLS-1$

	/**
	 * Name of the dimension property that gives the height of the header.
	 */

	public static final String HEADER_HEIGHT_PROP = "headerHeight"; //$NON-NLS-1$

	/**
	 * Name of the dimension property that gives the height of the footer.
	 */

	public static final String FOOTER_HEIGHT_PROP = "footerHeight"; //$NON-NLS-1$

	/**
	 * Height of the US Letter page.
	 */

	public static final String US_LETTER_HEIGHT = "11in"; //$NON-NLS-1$

	/**
	 * Width of the US Letter page.
	 */

	public static final String US_LETTER_WIDTH = "8.5in"; //$NON-NLS-1$

	/**
	 * Height of the US Legal page.
	 */

	public static final String US_LEGAL_HEIGHT = "14in"; //$NON-NLS-1$

	/**
	 * Width of the US Legal page.
	 */

	public static final String US_LEGAL_WIDTH = "8.5in"; //$NON-NLS-1$

	/**
	 * Height of the A4 page.
	 */

	public static final String A4_HEIGHT = "297mm"; //$NON-NLS-1$

	/**
	 * Width of the A4 page.
	 */

	public static final String A4_WIDTH = "210mm"; //$NON-NLS-1$

	/**
	 * Default constructor.
	 */

	public MasterPage( )
	{
	}

	/**
	 * Constructs the master page with a required name.
	 * 
	 * @param theName
	 *            the required name
	 */

	public MasterPage( String theName )
	{
		super( theName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.report.model.elements.ElementVisitor)
	 */

	abstract public void apply( ElementVisitor visitor );

	/**
	 * Returns the size of the page in application units. Considers the page
	 * type and orientation. If the page type is set to one of the standard
	 * sizes, then the height and width properties are ignored. Orientation
	 * affects standard sizes, but is ignored for custom sizes.
	 * 
	 * @param design
	 *            the report design
	 * @return the page size in application units
	 */

	public Point getSize( ReportDesign design )
	{
		// Determine height and width dimensions.

		Point size = new Point( );
		String type = getStringProperty( design, TYPE_PROP );
		String height = null;
		String width = null;

		if ( type.equalsIgnoreCase( DesignChoiceConstants.PAGE_SIZE_CUSTOM ) )
		{
			height = getStringProperty( design, HEIGHT_PROP );
			width = getStringProperty( design, WIDTH_PROP );
		}
		else if ( type
				.equalsIgnoreCase( DesignChoiceConstants.PAGE_SIZE_US_LETTER ) )
		{
			height = US_LETTER_HEIGHT;
			width = US_LETTER_WIDTH;
		}
		else if ( type
				.equalsIgnoreCase( DesignChoiceConstants.PAGE_SIZE_US_LEGAL ) )
		{
			height = US_LEGAL_HEIGHT;
			width = US_LEGAL_WIDTH;
		}
		else if ( type.equalsIgnoreCase( DesignChoiceConstants.PAGE_SIZE_A4 ) )
		{
			height = A4_HEIGHT;
			width = A4_WIDTH;
		}
		else
		{
			// Choice should have been validated.

			assert false;
			return size;
		}

		// Consider orientation for standard pages sizes, but not custom size.

		if ( type.equalsIgnoreCase( DesignChoiceConstants.PAGE_SIZE_CUSTOM )
				&& ( getStringProperty( design, ORIENTATION_PROP )
						.equalsIgnoreCase( DesignChoiceConstants.PAGE_ORIENTATION_LANDSCAPE ) ) )
		{
			String temp = height;
			height = width;
			width = temp;
		}

		// Convert to application units.
		try
		{
			size.y = DimensionUtil.convertTo( height, design.getSession( ).getUnits( ),
					design.getSession( ).getUnits( ) ).getMeasure( );
			size.x = DimensionUtil.convertTo( width, design.getSession( ).getUnits( ),
					design.getSession( ).getUnits( ) ).getMeasure( );

		}
		catch ( PropertyValueException e )
		{
			// dimension value should have be validated.

			assert false;
		}

		return size;
	}

	/**
	 * Returns the content area rectangle in application units. The content area
	 * is the portion of the page after subtracting the four margins.
	 * 
	 * @param design
	 *            the report design
	 * @return the content area rectangle in application units
	 */

	public Rectangle getContentArea( ReportDesign design )
	{
		Point size = getSize( design );
		Rectangle margins = new Rectangle( );
		margins.y = getFloatProperty( design, TOP_MARGIN_PROP );
		margins.x = getFloatProperty( design, LEFT_MARGIN_PROP );
		margins.height = size.y - margins.y
				- getFloatProperty( design, BOTTOM_MARGIN_PROP );
		margins.width = size.x - margins.x
				- getFloatProperty( design, RIGHT_MARGIN_PROP );
		return margins;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	public List validate( ReportDesign design )
	{
		List list = super.validate( design );

		List pageSizeErrors = MasterPageTypeValidator.getInstance( ).validate(
				design, this );
		if ( !pageSizeErrors.isEmpty( ) )
		{
			list.addAll( pageSizeErrors );
			return list;
		}

		list.addAll( MasterPageSizeValidator.getInstance( ).validate( design,
				this ) );

		return list;
	}

}
