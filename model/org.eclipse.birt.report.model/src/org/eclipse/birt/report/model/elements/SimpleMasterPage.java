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

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.SingleElementSlot;

/**
 * This class represents a Simple Master Page element in the report design. Use
 * the {@link org.eclipse.birt.report.model.api.SimpleMasterPageHandle}class to access
 * the page-header and page-footer slot of the simple master page. The simple
 * master page provides a header and footer that appear on every page. The page
 * margins define the position of the content area on the page. The page header
 * and footer reside within the content area. The page header at the top on each
 * page, and the page footer at the bottom.
 * 
 *  
 */

public class SimpleMasterPage extends MasterPage
{

	/**
	 * The page header slot ID
	 */

	public static final int PAGE_HEADER_SLOT = 0;

	/**
	 * The page footer slot ID
	 */

	public static final int PAGE_FOOTER_SLOT = 1;

	/**
	 * The slot count of simple master page. There are only 2 slots defined in
	 * simple master page, the page header and footer slot.
	 */

	public static final int SLOT_COUNT = 2;

	/**
	 * Slots to hold items defined either in page header or page footer.
	 */

	protected ContainerSlot slots[] = null;

	/**
	 * Name of the property 'show-header-on-first' that indicates whether show
	 * the header on the first page or not.
	 */

	public static final String SHOW_HEADER_ON_FIRST_PROP = "showHeaderOnFirst"; //$NON-NLS-1$

	/**
	 * Name of the property 'show-footer-on-last' that indicates whether show
	 * the footer on the last page.
	 */

	public static final String SHOW_FOOTER_ON_LAST_PROP = "showFooterOnLast"; //$NON-NLS-1$

	/**
	 * Name of the property 'floating-footer'
	 */

	public static final String FLOATING_FOOTER = "floatingFooter"; //$NON-NLS-1$

	/**
	 * Default Constructor
	 */

	public SimpleMasterPage( )
	{
		super( );
		initSlot( );
	}

	/**
	 * Constructs the simple master page with a required name.
	 * 
	 * @param theName
	 *            the required name of this master page.
	 */

	public SimpleMasterPage( String theName )
	{
		super( theName );
		initSlot( );
	}

	/**
	 * Initializes the slot of this simple master page.
	 *  
	 */

	private void initSlot( )
	{
		slots = new ContainerSlot[SLOT_COUNT];
		for ( int i = 0; i < SLOT_COUNT; i++ )
			slots[i] = new SingleElementSlot( );
	}

	/**
	 * Makes a clone of this simple master page.
	 * 
	 * @return the cloned simple master page element.
	 * 
	 * @see java.lang.Object#clone()
	 */

	public Object clone( ) throws CloneNotSupportedException
	{
		SimpleMasterPage page = (SimpleMasterPage) super.clone( );
		page.initSlot( );
		for ( int i = 0; i < slots.length; i++ )
		{
			page.slots[i] = slots[i].copy( page, i );
		}
		return page;
	}

	/**
	 * Return the handle of this element.
	 * 
	 * @param design
	 *            the report design
	 * @return the handle of this element
	 */

	public SimpleMasterPageHandle handle( ReportDesign design )
	{
		if ( handle == null )
		{
			handle = new SimpleMasterPageHandle( design, this );
		}
		return (SimpleMasterPageHandle) handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getSlot(int)
	 */

	public ContainerSlot getSlot( int slot )
	{
		assert slot == PAGE_HEADER_SLOT || slot == PAGE_FOOTER_SLOT;
		return slots[slot];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.MasterPage#apply(org.eclipse.birt.report.model.elements.ElementVisitor)
	 */

	public void apply( ElementVisitor visitor )
	{
		visitor.visitSimpleMasterPage( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName( )
	{
		return ReportDesignConstants.SIMPLE_MASTER_PAGE_ELEMENT;
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