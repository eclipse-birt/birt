/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
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
public class SampleIncludedSourceEntry {

	private static final String SAMPLE_REPORTS_HOST = "org.eclipse.birt.report.designer.samplereports"; //$NON-NLS-1$

	private static Bundle samplesBundle = Platform.getBundle(SAMPLE_REPORTS_HOST);

	private static final String imageFragmentPath = "/screenshots"; //$NON-NLS-1$

	private static final String librariesFragmentPath = "/samplereports/Reporting Feature Examples/Libraries"; //$NON-NLS-1$

	private static final String scriptedDataSourceFragmentPath = "/samplereports.ide/Scripting/Scripted Data Source"; //$NON-NLS-1$

	private static final String extendingFragmentPath = "/samplereports.ide/Extending BIRT"; //$NON-NLS-1$

	private static final String pngFragmentPath = "/samplereports/Reporting Feature Examples/XML Data Source"; //$NON-NLS-1$

	private static final String drillThroughFragmentPath = "/samplereports/Reporting Feature Examples/Drill to Details"; //$NON-NLS-1$

	public static URL getImagePath(String name) {
		Enumeration enumeration = samplesBundle.findEntries(imageFragmentPath, name + ".PNG", //$NON-NLS-1$
				false);
		if (enumeration != null && enumeration.hasMoreElements()) {
			return (URL) enumeration.nextElement();
		}
		return null;
	}

	public static Enumeration getDrillDetailsReports() {
		return samplesBundle.findEntries(drillThroughFragmentPath, "*.rptdesign", //$NON-NLS-1$
				false);
	}

	public static Enumeration getIncludedLibraries() {
		return samplesBundle.findEntries(librariesFragmentPath, "*.rptlibrary", //$NON-NLS-1$
				false);
	}

	public static Enumeration getJavaObjects() {
		return samplesBundle.findEntries(scriptedDataSourceFragmentPath, "*.java", //$NON-NLS-1$
				false);
	}

	public static Enumeration getExtendedPlugin(String categoryName) {
		// The plug-in should be packaged in zip
		return samplesBundle.findEntries(extendingFragmentPath + "/" //$NON-NLS-1$
				+ categoryName, "*.zip", false); //$NON-NLS-1$
	}

	public static Enumeration getEntries(String path) {
		// The plug-in should be packaged in zip
		return samplesBundle.findEntries(path, "*.*", false); //$NON-NLS-1$
	}

	public static Enumeration getIncludedPng() {
		return samplesBundle.findEntries(pngFragmentPath, "*.png", false); //$NON-NLS-1$
	}
}
