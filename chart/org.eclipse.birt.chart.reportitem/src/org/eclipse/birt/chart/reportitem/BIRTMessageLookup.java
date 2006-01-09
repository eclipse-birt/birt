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

package org.eclipse.birt.chart.reportitem;

import java.util.Locale;

import org.eclipse.birt.chart.factory.IMessageLookup;
import org.eclipse.birt.report.engine.api.script.IReportContext;


public class BIRTMessageLookup implements IMessageLookup
{

	private IReportContext context;
	public BIRTMessageLookup( IReportContext context )
	{
		this.context = context;
		if ( context == null )
		{
			throw new IllegalArgumentException();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.chart.factory.IMessageLookup#getMessageValue(java.lang.String, java.util.Locale)
	 */
	public String getMessageValue( String sKey, Locale lcl )
	{
		return context.getMessage(sKey, lcl );
	}

}
