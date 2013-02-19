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

import java.util.Locale;
import java.util.Map;

import org.eclipse.birt.report.engine.api.EmitterInfo;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.RenderOptionDefn;

/**
 * EmitterConfigUtil
 */
public class EmitterConfigUtil
{

	private EmitterConfigUtil( )
	{

	}

	/**
	 * Returns the default emitter descriptor for given format. If the default
	 * emitter is already specified in <code>EngineConfig</code> for this
	 * format, it will be returned; otherwise, the first matched emitter will be
	 * returned.
	 * 
	 * @param format
	 *            The render format. e.g. "html", "pdf".
	 * @param manager
	 *            The emitter configuration manager instance.
	 * @param engine
	 *            The report engine instance.
	 * @return The descriptor. Could be <code>null</code> if no matching found.
	 */
	public static IEmitterDescriptor getDefaultDescriptor( String format,
			IEmitterConfigurationManager manager, IReportEngine engine )
	{
		return getDefaultDescriptor( format, manager, engine, Locale.getDefault( ) );
	}
	
	/**
	 * Returns the default emitter descriptor for given format. If the default
	 * emitter is already specified in <code>EngineConfig</code> for this
	 * format, it will be returned; otherwise, the first matched emitter will be
	 * returned.
	 * 
	 * @param format
	 *            The render format. e.g. "html", "pdf".
	 * @param manager
	 *            The emitter configuration manager instance.
	 * @param engine
	 *            The report engine instance.
	 * @param locale
	 * 			  The locale setting, passing null to this value is equal to Locale.getDefault().
	 * @return The descriptor. Could be <code>null</code> if no matching found.
	 */
	public static IEmitterDescriptor getDefaultDescriptor( String format,
			IEmitterConfigurationManager manager, IReportEngine engine, Locale locale )
	{
		if ( locale == null )
		{
			locale = Locale.getDefault( );
		}
		String defaultID = engine.getConfig( ).getDefaultEmitter( format );

		if ( defaultID == null )
		{
			EmitterInfo[] eis = engine.getEmitterInfo( );

			if ( eis != null )
			{
				for ( int i = 0; i < eis.length; i++ )
				{
					if ( format.equals( eis[i].getFormat( ) ) )
					{
						defaultID = eis[i].getID( );
						break;
					}
				}
			}
		}

		if ( defaultID != null )
		{
			Map<String, RenderOptionDefn> options = engine.getDefaultEmitterRenderOption( format );
			IEmitterDescriptor desc = manager.getEmitterDescriptor( defaultID );
			if ( desc != null )
			{
				desc.setLocale( locale );
				desc.setDefaultRenderOptions( options );
			}
			return desc;
		}

		return null;
	}
}
