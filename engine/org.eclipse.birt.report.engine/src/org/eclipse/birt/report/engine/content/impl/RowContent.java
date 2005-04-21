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

package org.eclipse.birt.report.engine.content.impl;

import org.eclipse.birt.report.engine.content.IReportContentVisitor;
import org.eclipse.birt.report.engine.content.IReportElementContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.RowDesign;

/**
 * 
 * the row content object which contains cell content objects
 * 
 * @version $Revision: 1.1 $ $Date: 2005/02/25 06:02:24 $
 */
public class RowContent extends StyledElementContent implements IRowContent
{

	/**
	 * the evaluated bookmark value
	 */
	protected String bookmarkValue = null;

	/**
	 * @return Returns the height.
	 */
	public DimensionType getHeight( )
	{
		return ( (RowDesign) this.designReference ).getHeight( );
	}

	/**
	 * constructor
	 * 
	 * @param row
	 *            the row deign
	 */
	public RowContent( RowDesign row, IReportElementContent parent )
	{
		super( row, parent );
	}

	public void accept( IReportContentVisitor visitor )
	{
		visitor.visitRowContent( this );
	}

	/**
	 * @return the Bookmark value
	 */
	public String getBookmarkValue( )
	{
		return bookmarkValue;
	}

	/**
	 * Set the bookmark value which is calculated in the Executor
	 * 
	 * @param newValue
	 */
	public void setBookmarkValue( String newValue )
	{
		bookmarkValue = newValue;
	}
}