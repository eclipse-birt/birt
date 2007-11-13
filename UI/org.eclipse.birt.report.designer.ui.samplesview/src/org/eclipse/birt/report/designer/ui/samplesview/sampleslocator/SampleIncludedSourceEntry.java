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

package org.eclipse.birt.report.designer.ui.samplesview.sampleslocator;

import java.net.URL;
import java.util.Enumeration;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * Represents the resource entry of sample screenshot image
 */
public class SampleIncludedSourceEntry
{

	private static final String SAMPLE_REPORTS_HOST = "org.eclipse.birt.report.designer.samplereports";

	private static Bundle samplesBundle = Platform.getBundle( SAMPLE_REPORTS_HOST );

	private static final String imageFragmentPath = "/screenshots";

	private static final String librariesFragmentPath = "/samplereports/Reporting Feature Examples/Libraries";

	private static final String scriptedDataSourceFragmentPath = "/samplereports.ide/Scripting/Scripted Data Source";

	private static final String extendingFragmentPath = "/samplereports.ide/Extending BIRT";

	private static final String pngFragmentPath = "/samplereports/Reporting Feature Examples/XML Data Source";

	private static final String drillThroughFragmentPath = "/samplereports/Reporting Feature Examples/Drill to Details";

	public static URL getImagePath( String name )
	{
		Enumeration enumeration = samplesBundle.findEntries( imageFragmentPath,
				name + ".PNG",
				false );
		if ( enumeration != null && enumeration.hasMoreElements( ) )
		{
			return (URL) enumeration.nextElement( );
		}
		return null;
	}

	public static Enumeration getDrillDetailsReports( )
	{
		return samplesBundle.findEntries( drillThroughFragmentPath,
				"*.rptdesign",
				false );
	}

	public static Enumeration getIncludedLibraries( )
	{
		return samplesBundle.findEntries( librariesFragmentPath,
				"*.rptlibrary",
				false );
	}

	public static Enumeration getJavaObjects( )
	{
		return samplesBundle.findEntries( scriptedDataSourceFragmentPath,
				"*.java",
				false );
	}

	public static Enumeration getExtendedPlugin( String categoryName )
	{
		// The plug-in should be packaged in zip
		return samplesBundle.findEntries( extendingFragmentPath
				+ "/"
				+ categoryName, "*.zip", false );
	}

	public static Enumeration getEntries( String path )
	{
		// The plug-in should be packaged in zip
		return samplesBundle.findEntries( path, "*.*", false );
	}

	public static Enumeration getIncludedPng( )
	{
		return samplesBundle.findEntries( pngFragmentPath, "*.png", false );
	}
}
