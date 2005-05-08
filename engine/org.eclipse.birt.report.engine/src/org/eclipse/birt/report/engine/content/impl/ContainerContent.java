/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.content.impl;

import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IReportContentVisitor;
import org.eclipse.birt.report.engine.content.IReportElementContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;

/**
 * container content object
 * 
 * @version $Revision: 1.4 $ $Date: 2005/04/21 01:57:06 $
 */
public class ContainerContent extends ReportItemContent
		implements
			IContainerContent
{

	public final static int SECTION_CONTAINER = 0;
	public final static int REPORTITEM_CONTAINER = 1;

	/**
	 * the type of the container
	 */
	private int type;

	/**
	 * the style defined for the report item
	 */
	private IStyle style = null;

	/**
	 * constructor for REPORTITEM_CONTAINER
	 * 
	 * @param item
	 *            reference to the design object in engine IR
	 */
	public ContainerContent( ReportItemDesign item, IReportElementContent parent )
	{
		super( item, parent );

		style = item.getStyle( );
		type = REPORTITEM_CONTAINER;
	}

	/**
	 * constructor for SECTION_CONTAINER
	 */
	public ContainerContent( )
	{
		super( );
		type = SECTION_CONTAINER;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.ReportElementContent#accept(org.eclipse.birt.report.engine.content.ReportContentVisitor)
	 */
	public void accept( IReportContentVisitor visitor )
	{
		visitor.visitContainerContent( this );
	}

	/**
	 * @return Returns the style.
	 */
	public IStyle getStyle( )
	{
		return style;
	}

	/**
	 * @return the container type
	 */
	public int getType( )
	{
		return type;
	}

	/**
	 * @return the height of the container.
	 */
	public DimensionType getHeight( )
	{
		if ( type == REPORTITEM_CONTAINER )
			return super.getHeight( );
		return null;
	}

	/**
	 * @return the width of the container.
	 */
	public DimensionType getWidth( )
	{
		if ( type == REPORTITEM_CONTAINER )
			return super.getWidth( );
		return null;
	}

	/**
	 * @return the x position of the report item
	 */
	public DimensionType getX( )
	{
		if ( type == REPORTITEM_CONTAINER )
			return super.getX( );
		return null;
	}

	/**
	 * @return the y position of the report item
	 */
	public DimensionType getY( )
	{
		if ( type == REPORTITEM_CONTAINER )
			return super.getY( );
		return null;
	}
}