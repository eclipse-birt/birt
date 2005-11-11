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

import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.ITableBandContent;

/**
 * 
 * table band content object There are three type: table header, table footer,
 * table body
 * 
 * @version $Revision: 1.5 $ $Date: 2005/10/27 02:13:34 $
 */
public class TableBandContent extends AbstractContent
		implements
			ITableBandContent
{

	protected int type;
	
	public TableBandContent(ReportContent report)
	{
		super(report);
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.ReportElementContent#accept(org.eclipse.birt.report.engine.content.ReportContentVisitor)
	 */
	public void accept( IContentVisitor visitor, Object value )
	{
		visitor.visitTableBand( this , value);

	}

	/**
	 * get type
	 * 
	 * @return the type
	 */
	public int getType( )
	{
		return this.type;
	}
	
	public void setType(int type)
	{
		this.type = type;
	}
}