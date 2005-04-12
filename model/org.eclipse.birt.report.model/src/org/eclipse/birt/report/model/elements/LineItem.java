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
import org.eclipse.birt.report.model.api.LineHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;

/**
 * This class represents a line item. Uses lines to add graphical interest to a
 * report. Most reports are intended for viewing on the web. Web browsers
 * generally support only horizontal and vertical, but not diagonal lines.
 * Diagonal lines require an image which imposes extra cost and is difficult to
 * correctly format. So, implementation limits support to horizontal and
 * vertical lines. Use the {@link org.eclipse.birt.report.model.api.LineHandle}class
 * to change the properties, such as the line size, line color and line pattern
 * using the item's style.
 * 
 */

public class LineItem extends ReportItem
{

	/**
	 * Name of the orientation property.The orientation of the line: Horizontal
	 * (default) or Vertical.
	 */

	public static final String ORIENTATION_PROP = "orientation"; //$NON-NLS-1$ 

	/**
	 * Default Constructor.
	 */

	public LineItem( )
	{
		super( );
	}

	/**
	 * Constructs the line item with an optional name.
	 * 
	 * @param theName
	 *            the optional name of the line item
	 */

	public LineItem( String theName )
	{
		super( theName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.report.model.elements.ElementVisitor)
	 */

	public void apply( ElementVisitor visitor )
	{
		visitor.visitLine( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */
	
	public String getElementName( )
	{
		return ReportDesignConstants.LINE_ITEM;
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

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param design
	 *            the report design
	 * @return an API handle for this element
	 */

	public LineHandle handle( ReportDesign design )
	{
		if ( handle == null )
		{
			handle = new LineHandle( design, this );
		}
		return (LineHandle) handle;
	}

}
