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
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;

/**
 * 
 * the table content object which contains columns object and row objects
 * 
 * @version $Revision: 1.2 $ $Date: 2005/03/18 19:35:53 $
 */
public class TableContent extends ReportItemContent implements ITableContent
{

	/**
	 * table type(table or grid)
	 */
	protected int type;

	protected String caption = null;
	/**
	 * column count
	 */
	protected int columnCount = 0;

	public boolean getRepeatHeader( )
	{
		if (designReference instanceof TableItemDesign)
		{
			return ( (TableItemDesign) designReference ).getRepeatHeader( );
		}
		return false;
	}

	/**
	 * get column count
	 * 
	 * @return column count
	 */
	public int getColumnCount( )
	{
		return columnCount;
	}

	/**
	 * constructor
	 * 
	 * @param item
	 *            the grid design
	 */
	public TableContent( GridItemDesign item, IReportElementContent parent )
	{
		super( item, parent );
		this.columnCount = item.getColumnCount( );
	}

	/**
	 * constructor
	 * 
	 * @param item
	 *            the table deign
	 */
	public TableContent( TableItemDesign item, IReportElementContent parent )
	{
		super( item, parent );
	}

	public void accept( IReportContentVisitor visitor )
	{
		visitor.visitTableContent( this );
	}

	/**
	 * @return Returns the caption.
	 */
	public String getCaption( )
	{
		return caption;
	}

	/**
	 * @param caption
	 *            The caption to set.
	 */
	public void setCaption( String caption )
	{
		this.caption = caption;
	}
}