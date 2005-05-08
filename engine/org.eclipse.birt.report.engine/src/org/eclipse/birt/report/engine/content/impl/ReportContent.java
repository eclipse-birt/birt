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

import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IReportContentVisitor;
import org.eclipse.birt.report.engine.content.IReportElementContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.ir.Report;

/**
 * Report is the root element of the design.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/21 01:57:06 $
 */
public class ReportContent extends ReportElementContent implements IReportContent
{

	/**
	 * default constructor.
	 */
	public ReportContent( Report report, IReportElementContent parent )
	{
		super( report, parent );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.content.IReportContent#getStyleCount()
	 */
	public int getStyleCount( )
	{
		return ( ( Report ) designReference ).getStyleCount( );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.content.IReportContent#getStyle(int)
	 */
	public IStyle getStyle( int index )
	{
		return ( ( Report ) designReference ).getStyle( index );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.content.IReportContent#getBasePath()
	 */
	public String getBasePath( )
	{
		return ( ( Report ) designReference ).getBasePath( );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.content.IReportContent#getBodyStyle()
	 */
	public IStyle getBodyStyle( )
	{
		return ( ( Report ) designReference ).getBodyStyle( );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.content.impl.ReportElementContent#accept(org.eclipse.birt.report.engine.content.IReportContentVisitor)
	 */
	public void accept( IReportContentVisitor visitor )
	{
	}

}