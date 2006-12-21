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

package org.eclipse.birt.report.engine.emitter.postscript;

import org.eclipse.birt.report.engine.api.RenderOptionBase;


public class PostscriptRenderOption extends RenderOptionBase
{
	public static final String PS_LEVEL = "psLevel";

	/**
	 * Sets postscript level.
	 * 
	 * @param level
	 */
	public void setPostscriptLevel( int level )
	{
		options.put( PS_LEVEL, new Integer( level ) );
	}

	/**
	 * Gets postscript level.
	 */
	public int getPostscriptLevel( )
	{
		Object value = options.get( PS_LEVEL );
		if ( value instanceof Integer )
		{
			return ( ( Integer )value ).intValue( );
		}
		return 1;
	}

}
