/*
 *************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.eclipse.birt.core.i18n.ResourceHandle;

/**
 * Implementation of ResourceHandle in DtE project
 */

public class DataResourceHandle extends ResourceHandle
{
	private static DataResourceHandle sm_resourceHandle;
	private static Map sm_localeResourceHandleMap;
	
	public static DataResourceHandle getInstance()
	{
		if( sm_resourceHandle == null )
			sm_resourceHandle = new DataResourceHandle( Locale.getDefault() );
		
		return sm_resourceHandle;
	}
	
	public static DataResourceHandle getInstance( Locale locale )
	{
		DataResourceHandle ret = 
			(DataResourceHandle) getLocaleResourceHandleMap().get( locale );
		
		if( ret == null )
		{
			ret = new DataResourceHandle( locale );
			getLocaleResourceHandleMap().put( locale, ret );
		}
		
		return ret;
	}
	
	private static Map getLocaleResourceHandleMap()
	{
		if( sm_localeResourceHandleMap == null )
			sm_localeResourceHandleMap = new HashMap();
		
		return sm_localeResourceHandleMap;
	}

	/**
	 * @param locale
	 */
	public DataResourceHandle( Locale locale )
	{
		super( locale );
	}

}
