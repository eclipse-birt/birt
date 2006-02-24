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



public class BirtContext
{
	/**
	 * Thread local.
	 */
	private static ThreadLocal contextTraker = new ThreadLocal( );

    /**
     * Reference to the viewer attribute bean.
     */
    private ViewerAttributeBean bean = null;
    
    /**
     * Reference to the current session.
     */
    private HttpServletRequest request = null;
    
	/**
	 * Static accessor.
	 * 
	 * @return
	 */
    public static BirtContext get( )
    {
        return ( ( BirtContext ) contextTraker.get( ) );
    }

	/**
	 * Constructor.
	 * 
	 * @param request
	 * @param response
	 */
	public BirtContext( HttpServletRequest request )
	{
		this.bean = ( ViewerAttributeBean ) request.getAttribute( "attributeBean" ); //$NON-NLS-1$
		if ( bean == null )
		{
			bean = new ViewerAttributeBean( request );
		}
		request.setAttribute( "attributeBean", bean ); //$NON-NLS-1$
		
		this.request = request;
		
		contextTraker.set( this );
	}

	/**
	 * Finalize the instance.
	 */
	public void finalize( )
	{
		if ( bean != null )
		{
			bean.finalize( );
		}
	}

	/**
	 * @return Returns the bean.
	 */
	public ViewerAttributeBean getBean( )
	{
		return bean;
	}
	
	/**
	 * @return Returns the request.
	 */
	public HttpServletRequest getRequest( )
	{
		return request;
	}
}