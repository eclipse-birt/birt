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

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GraphicMasterPageHandle;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.MultiElementSlot;
import org.eclipse.birt.report.model.validators.MasterPageMultiColumnValidator;
import org.eclipse.birt.report.model.validators.MasterPageTypeValidator;

/**
 * This class represents a Graphic Master Page element in the report design. A
 * graphic master page describes a physical page free-form page ¡°decoration¡±.
 * The name of the master page is required and must be unique within the design.
 * The decoration can include simple headers and footers, but can also include
 * content within the left and right margins, as well as watermarks under the
 * content area. The page can contain multiple columns. In a multi-column
 * report, the content area is the area inside the margins defined by each
 * column. Note that each page has only one content area, though that content
 * area can be divided into multiple columns. That is, a page has one content
 * area. If a page has multiple columns, the column layout is ¡°overlayed¡± on top
 * of the content area. Use the
 * {@link org.eclipse.birt.report.model.api.GraphicMasterPageHandle}class to
 * access the content slot of the graphic master page.
 *  
 */
public class GraphicMasterPage extends MasterPage
{

	/**
	 * Name of the property that gives the number of columns to appear on the
	 * page.
	 */

	public static final String COLUMNS_PROP = "columns"; //$NON-NLS-1$

	/**
	 * Name of the dimension property that gives the spacing between columns of
	 * a multi-column page.
	 */

	public static final String COLUMN_SPACING_PROP = "columnSpacing"; //$NON-NLS-1$

	/**
	 * Identifier of the slot that holds the page decoration.
	 */

	public static final int CONTENT_SLOT = 0;

	/**
	 * Holds the report items that reside directly on the page.
	 */

	protected MultiElementSlot contents = new MultiElementSlot( );

	/**
	 * Default Constructor.
	 */

	public GraphicMasterPage( )
	{
		super( );
	}

	/**
	 * Constructs the graphic master page with a required and unique name.
	 * 
	 * @param theName
	 *            the required name
	 */

	public GraphicMasterPage( String theName )
	{
		super( theName );
	}

	/**
	 * Makes a clone of this graphic master element. The cloned master page
	 * contains list of contents, which are copied from the original element.
	 * 
	 * @return the cloned graphic master page object.
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone( ) throws CloneNotSupportedException
	{
		GraphicMasterPage page = (GraphicMasterPage) super.clone( );
		page.contents = (MultiElementSlot) contents.copy( page, CONTENT_SLOT );
		return page;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getSlot(int)
	 */

	public ContainerSlot getSlot( int slot )
	{
		assert slot == CONTENT_SLOT;
		return contents;
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param design
	 *            the report design
	 * @return an API handle for this element
	 */

	public GraphicMasterPageHandle handle( ReportDesign design )
	{
		if ( handle == null )
		{
			handle = new GraphicMasterPageHandle( design, this );
		}
		return (GraphicMasterPageHandle) handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.MasterPage#apply(org.eclipse.birt.report.model.elements.ElementVisitor)
	 */

	public void apply( ElementVisitor visitor )
	{
		visitor.visitGraphicMasterPage( this );
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
		if ( pageSizeErrors.isEmpty( ) )
		{
			list.addAll( MasterPageMultiColumnValidator.getInstance( )
					.validate( design, this ) );
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName( )
	{
		return ReportDesignConstants.GRAPHIC_MASTER_PAGE_ELEMENT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getHandle(org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	public DesignElementHandle getHandle( ReportDesign design )
	{
		return handle( design );
	}
}