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

package org.eclipse.birt.report.engine.internal.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class BundleVersionUtil
{
	/**
	 * The Log object that <code>BundleVersionUtil</code> uses to log the error, debug,
	 * information messages.
	 */
	protected static Logger logger = Logger.getLogger( BundleVersionUtil.class.getName() );
	
	private static String MANIFEST_PATH = "/META-INF/MANIFEST.MF";
	private static String UNKNOWN_VERSION = "version unknown";
	
	public static String getBundleVersion( String bundleName ) 
	{
		Bundle bundle = Platform.getBundle( bundleName );
		if ( null == bundle )
			return UNKNOWN_VERSION;
		Path path = new Path( MANIFEST_PATH );
		URL bundleURL = FileLocator.find( bundle, path, null );
		if ( null == bundleURL )
			return UNKNOWN_VERSION;
		String mainVersion = null;
		try 
		{
			Manifest mf = new Manifest(new BufferedInputStream(bundleURL
					.openStream()));
			if ( null == mf )
				mainVersion = UNKNOWN_VERSION;
			String version = mf.getMainAttributes().getValue("Bundle-Version");
			// ignore the qualifier version.
			if ( null != version )
				mainVersion = version.substring(0, version.lastIndexOf("."));
			
		} catch (IOException ioe) 
		{
			logger.log( Level.WARNING, ioe.getMessage(), ioe );
			return UNKNOWN_VERSION;
		}
		return mainVersion;
	}
	
}
