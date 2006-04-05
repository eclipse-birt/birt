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

package org.eclipse.birt.report.engine.internal.content.wrap;

import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IRowContent;

/**
 * 
 * the row content object which contains cell content objects
 * 
 * @version $Revision: 1.12 $ $Date: 2006/01/26 04:34:38 $
 */
public class RowContentWrapper extends AbstractContentWrapper
		implements
			IRowContent
{

	IRowContent rowContent;

	/**
	 * constructor
	 * 
	 * @param row
	 *            the row deign
	 */
	public RowContentWrapper( IRowContent content )
	{
		super( content );
		rowContent = content;
	}

	public void accept( IContentVisitor visitor, Object value )
	{
		visitor.visitRow( this, value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IRowContent#getGroupLevel()
	 */
	public int getGroupLevel( )
	{
		return rowContent.getGroupLevel( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IRowContent#getRowID()
	 */
	public int getRowID( )
	{
		return rowContent.getRowID( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IRowContent#getRowType()
	 */
	public int getRowType( )
	{
		return rowContent.getRowType( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IRowContent#setRowID(int)
	 */
	public void setRowID( int rowID )
	{
		rowContent.setRowID( rowID );
	}

}