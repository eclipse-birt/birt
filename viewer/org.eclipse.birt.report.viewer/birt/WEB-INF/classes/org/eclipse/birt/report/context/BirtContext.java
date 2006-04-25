/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BirtContext extends BaseContext
{
	/**
	 * Constructor.
	 * 
	 * @param request
	 * @param response
	 */
	public BirtContext( HttpServletRequest request, HttpServletResponse response )
	{
		super( request, response );
	}
	
	/**
	 * Local init.
	 * 
	 * @return
	 */
	protected void __init( )
	{
		this.bean = ( ViewerAttributeBean ) request.getAttribute( "attributeBean" ); //$NON-NLS-1$
		if ( bean == null )
		{
			bean = new ViewerAttributeBean( request );
		}
		request.setAttribute( "attributeBean", bean ); //$NON-NLS-1$
	}
}