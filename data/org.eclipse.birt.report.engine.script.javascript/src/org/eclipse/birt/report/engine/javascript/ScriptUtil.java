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

package org.eclipse.birt.report.engine.javascript;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;

import org.mozilla.javascript.PolicySecurityController;
import org.mozilla.javascript.SecurityController;

public class ScriptUtil {

	public static SecurityController createSecurityController() {
		return new PolicySecurityController();
	}

	public static Object getSecurityDomain(final String file) {
		if (file == null) {
			return null;
		}
		if (System.getSecurityManager() == null) {
			return null;
		}
		try {
			return new CodeSource(new URL(file), (java.security.cert.Certificate[]) null);
		} catch (MalformedURLException ex) {
			try {
				return new CodeSource(new File(file).toURI().toURL(), (java.security.cert.Certificate[]) null);
			} catch (MalformedURLException e) {
				return null;
			}
		}
	}
}
