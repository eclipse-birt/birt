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

package org.eclipse.birt.report.engine.content;

import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.ir.Report;

/**
 * Creates the content objects.
 * <p>
 * In any case, the user gets the two different content object for any two
 * calls.
 * 
 * @version $Revision: 1.12 $ $Date: 2005/11/17 16:50:48 $
 */
public class ContentFactory
{

	/**
	 * Creates the Report content object
	 * 
	 * @param design
	 *            the Report
	 * @param parent
	 *            the parent content object
	 * @return the instance
	 */
	public static IReportContent createReportContent( Report design )
	{
		return new ReportContent( design );
	}

	public static IReportContent createReportContent( )
	{
		return new ReportContent( );
	}
}