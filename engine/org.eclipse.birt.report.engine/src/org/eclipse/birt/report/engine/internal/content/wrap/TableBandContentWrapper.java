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
import org.eclipse.birt.report.engine.content.ITableBandContent;

/**
 * 
 * table band content object There are three type: table header, table footer,
 * table body
 * 
 * @version $Revision: 1.8 $ $Date: 2006/01/20 14:55:38 $
 */
public class TableBandContentWrapper extends AbstractContentWrapper
		implements
			ITableBandContent
{
	ITableBandContent bandContent;
	public TableBandContentWrapper( ITableBandContent content )
	{
		super( content );
		bandContent = content;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.ReportElementContent#accept(org.eclipse.birt.report.engine.content.ReportContentVisitor)
	 */
	public void accept( IContentVisitor visitor, Object value )
	{
		visitor.visitTableBand( this, value );

	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.content.ITableBandContent#getType()
	 */
	public int getType( )
	{
		return bandContent.getType( );
	}
}