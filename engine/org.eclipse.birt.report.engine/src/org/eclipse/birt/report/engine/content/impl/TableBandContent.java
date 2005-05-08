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
import org.eclipse.birt.report.engine.content.ITableBandContent;

/**
 * 
 * table band content object There are three type: table header, table footer,
 * table body
 * 
 * @version $Revision: 1.2 $ $Date: 2005/05/08 06:08:27 $
 */
public class TableBandContent extends ReportElementContent
		implements
			ITableBandContent
{

	protected int type;

	public TableBandContent( int type )
	{
		super( null );
		this.type = type;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.ReportElementContent#accept(org.eclipse.birt.report.engine.content.ReportContentVisitor)
	 */
	public void accept( IReportContentVisitor visitor )
	{
		visitor.visitTableBandContent( this );

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

}