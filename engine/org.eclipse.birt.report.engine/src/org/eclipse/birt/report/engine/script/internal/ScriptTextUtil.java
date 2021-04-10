/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.script.internal;

final class ScriptTextUtil {

	/**
	 * Whether the script string is null or comments
	 * 
	 */
	static boolean isNullOrComments(String script) {
		if (script == null)
			return true;
		try {
			String scriptWithoutComments = script.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "");
			if (scriptWithoutComments.trim().length() == 0)
				return true;
			else
				return false;
		} catch (Throwable e) {
			return false;
		}
	}
}
