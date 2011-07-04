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

package org.eclipse.birt.report.engine.emitter.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * AbstractEmitterDescriptor
 */
public abstract class AbstractEmitterDescriptor implements IEmitterDescriptor
{

	protected Map initParams = null;
	protected Locale locale;
	protected Properties defaultValues = null;	
	protected IConfigurableOption[] options;
	
	private static final String OPTIONS_CONFIG_FILE = "RenderDefaults.cfg";
	
	public void setInitParameters( Map params )
	{
		this.initParams = params;
	}

	public void setLocale( Locale locale )
	{
		if ( this.locale != locale )
		{
			this.locale = locale;
			initOptions( );
		}
	}

	public IConfigurableOptionObserver createOptionObserver( )
	{
		return null;
	}

	public String getDescription( )
	{
		return null;
	}

	public String getDisplayName( )
	{
		return null;
	}

	public String getID( )
	{
		return null;
	}
	
	protected abstract void initOptions( );

	protected void applyDefaultValues( )
	{
		// parse the default value from the config file first.
		for ( IConfigurableOption option : options )
		{
			applyDefaultValue( option );
		}
	}
	
	protected boolean loadDefaultValues( String bundleName )
	{
		try
		{
			URL url = getConfigURL( bundleName );
			if ( url != null )
			{
				InputStream in = url.openStream( );
				defaultValues = new Properties( );
				defaultValues.load( in );
				in.close( );
				return true;
			}
		}
		catch ( IOException e )
		{
			defaultValues = null;
			return false;
		}
		return false;
	}
	
	private void applyDefaultValue( IConfigurableOption option )
	{
		if ( defaultValues == null || defaultValues.isEmpty( ) )
		{
			return;
		}
		String value = (String) defaultValues.get( option.getName( ) );
		if ( value != null )
		{
			ConfigurableOption optionImpl = (ConfigurableOption) option;
			switch ( option.getDataType( ) )
			{
				case STRING :
					optionImpl.setDefaultValue( value );
					break;
				case BOOLEAN :
					optionImpl.setDefaultValue(Boolean.valueOf( value ));
					break;
				case INTEGER :
					Integer intValue = null;
					try
					{
						intValue = Integer.decode( value );
					}
					catch ( NumberFormatException e )
					{
						break;
					}
					optionImpl.setDefaultValue( intValue);
					break;
				case FLOAT :
					Float floatValue = null;
					try
					{
						floatValue = Float.valueOf( value );
					}
					catch ( NumberFormatException e )
					{
						break;
					}
					optionImpl.setDefaultValue( floatValue );
					break;
				default :
					break;
			}
		}
	}
	
	private URL getConfigURL( String bundleName )
	{
		Bundle bundle = Platform.getBundle( bundleName ); //$NON-NLS-1$
		if ( bundle != null )
		{
			return bundle.getEntry( OPTIONS_CONFIG_FILE );
		}
		return null;
	}
	

}
