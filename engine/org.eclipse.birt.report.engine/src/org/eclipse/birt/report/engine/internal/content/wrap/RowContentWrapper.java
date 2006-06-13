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
import org.eclipse.birt.report.engine.content.ITableContent;

/**
 * 
 * the row content object which contains cell content objects
 * 
 * @version $Revision: 1.2 $ $Date: 2006/05/18 09:10:25 $
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

	public Object accept( IContentVisitor visitor, Object value )
	{
		return visitor.visitRow( this, value );
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
	 * @see org.eclipse.birt.report.engine.content.IRowContent#setRowID(int)
	 */
	public void setRowID( int rowID )
	{
		rowContent.setRowID( rowID );
	}

	public ITableContent getTable( )
	{
		return null;
	}
}