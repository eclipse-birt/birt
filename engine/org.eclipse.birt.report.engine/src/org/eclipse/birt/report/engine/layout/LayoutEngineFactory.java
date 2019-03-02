/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.layout;

import org.eclipse.birt.report.engine.layout.html.HTMLReportLayoutEngine;

public class LayoutEngineFactory
{
	public static IReportLayoutEngine createLayoutEngine( String paginationType )
	{
		/*if ( ExtensionManager.PAPER_SIZE_PAGINATION.equals( paginationType ) ) 
		{
			try
			{
				Class clazz = Class
						.forName( "org.eclipse.birt.report.engine.layout.pdf.PDFReportLayoutEngine" ); //$NON-NLS-1$
				Object engine = clazz.newInstance( );
				return (IReportLayoutEngine) engine;
			}
			catch ( Exception ex )
			{
			}
			return null;
		}*/
		return new HTMLReportLayoutEngine( );
	}
}
