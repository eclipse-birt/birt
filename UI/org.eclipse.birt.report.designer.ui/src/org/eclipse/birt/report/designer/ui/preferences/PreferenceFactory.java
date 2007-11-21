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

package org.eclipse.birt.report.designer.ui.preferences;

import java.util.HashMap;

import org.eclipse.birt.core.preference.IPreferences;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class PreferenceFactory
{

	private static PreferenceFactory instance = null;

	private PreferenceFactory( )
	{
	}

	public static synchronized PreferenceFactory getInstance( )
	{
		if ( instance == null )
			instance = new PreferenceFactory( );
		return instance;
	}

	private HashMap preferenceMap = new HashMap( );

	public IPreferences getPreferences( AbstractUIPlugin plugin )
	{
		return getPreferences( plugin, null );
	}

	public IPreferences getPreferences( AbstractUIPlugin plugin,
			IProject project )
	{
		String pluginId = plugin.getBundle( ).getSymbolicName( );

		IReportPreferenceFactory preference = (IReportPreferenceFactory) ElementAdapterManager.getAdapter( plugin,
				IReportPreferenceFactory.class );
		
		PreferenceWrapper wrapper = null;
		if ( preference == null || project == null )
		{
			if ( preferenceMap.containsKey( pluginId ) )
				return (PreferenceWrapper) preferenceMap.get( pluginId );
			wrapper = new PreferenceWrapper( plugin.getPreferenceStore( ) );
			preferenceMap.put( pluginId, wrapper );
		}
		else
		{
			String id = pluginId.concat( "/" ).concat( project.getName( ) ); //$NON-NLS-1$
			if ( preferenceMap.containsKey( id ) )
			{
				return (PreferenceWrapper) preferenceMap.get( id );
			}
			wrapper = new PreferenceWrapper( preference,
					project,
					plugin.getPreferenceStore( ) );
			preferenceMap.put( id, wrapper );
		}
		return wrapper;
	}
}
