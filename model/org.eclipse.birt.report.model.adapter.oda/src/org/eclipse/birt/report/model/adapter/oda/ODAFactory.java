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

package org.eclipse.birt.report.model.adapter.oda;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;


/**
 * 
 *
 */

public class ODAFactory
{

	private static IODAFactory factory = null;



	static
	{
		Logger errorLogger = Logger
		.getLogger( ODAFactory.class.getName( ) );
		
		try
		{
			Platform.startup( null );
		}
		catch ( BirtException e )
		{
			errorLogger.log( Level.INFO,
					"Error occurs while start the platform", e ); //$NON-NLS-1$
		}

		Object adapterFactory = Platform
				.createFactoryObject( IAdapterFactory.EXTENSION_MODEL_ADAPTER_ODA_FACTORY );
		if ( adapterFactory instanceof IAdapterFactory )
		{
			factory = ( (IAdapterFactory) adapterFactory ).getODAFactory( );
		}
		if ( factory == null )
		{
			errorLogger.log( Level.INFO,
					"Can not start the model adapter oda factory." ); //$NON-NLS-1$
		}
	}
	
	/**
	 * @param inFactory
	 */
	
	public static synchronized void setODAFactory( IODAFactory inFactory )
	{
		factory = inFactory;
	}

	/**
	 * @return the factory
	 */
	
	public static IODAFactory getFactory( )
	{
		return factory;
	}
}
