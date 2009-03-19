/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.text.Collator;
import java.util.TreeMap;

import org.eclipse.birt.report.designer.nls.Messages;

import com.ibm.icu.util.ULocale;

/**
 * 
 */

public abstract class FormatDescriptorProvider extends
		AbstractDescriptorProvider
{

	protected static final String NONE = Messages.getString("FormatDescriptorProvider.DisplayName.None"); //$NON-NLS-1$

	public final static TreeMap<String, ULocale> LOCALE_TABLE = new TreeMap<String, ULocale>( Collator.getInstance( ) );

	static
	{
		// Initialize the locale mapping table
		ULocale[] locales = ULocale.getAvailableLocales( );
		if ( locales != null )
		{
			for ( int i = 0; i < locales.length; i++ )
			{
				ULocale locale = locales[i];
				if ( locale != null )
				{
					LOCALE_TABLE.put( locale.getDisplayName( ), locale ); //$NON-NLS-1$
				}
			}
		}
	}

	public String[] getLocaleDisplayNames( )
	{
		String[] oldNames = (String[]) LOCALE_TABLE.keySet( )
				.toArray( new String[0] );
		String[] newNames = new String[oldNames.length + 1];
		newNames[0] = NONE;
		System.arraycopy( oldNames, 0, newNames, 1, oldNames.length );
		return newNames;
	}

	public ULocale getLocaleByDisplayName( String localeDisplayName )
	{
		if ( NONE.equals( localeDisplayName ) || localeDisplayName == null )
			return null;
		return LOCALE_TABLE.get( localeDisplayName );
	}

	public String getLocaleDisplayName( ULocale locale )
	{
		if ( locale == null )
			return NONE;
		else
			return locale.getDisplayName( );
	}
}
