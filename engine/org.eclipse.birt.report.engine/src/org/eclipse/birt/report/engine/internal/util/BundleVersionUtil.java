/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.internal.util;

import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.logging.Logger;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class BundleVersionUtil {

	/**
	 * The Log object that <code>BundleVersionUtil</code> uses to log the error,
	 * debug, information messages.
	 */
	protected static Logger logger = Logger.getLogger(BundleVersionUtil.class.getName());

	private static String UNKNOWN_VERSION = "UNKNOWN";

	public static String getBundleVersion(String bundleName) {
		Bundle bundle = Platform.getBundle(bundleName);
		if (bundle != null) {
			return bundle.getVersion().toString();
		}
		// the engine.jar are in the class path
		ProtectionDomain domain = BundleVersionUtil.class.getProtectionDomain();
		if (domain != null) {
			CodeSource codeSource = domain.getCodeSource();
			if (codeSource != null) {
				URL jarUrl = codeSource.getLocation();
				if (jarUrl != null) {
					return jarUrl.getFile();
				}
			}
		}
		return UNKNOWN_VERSION;
	}
}
