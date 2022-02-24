/*******************************************************************************
 * Copyright (c)2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.util;

public class FlashFile {
	public static boolean isFlash(String mimeType, String uri, String extension) {
		boolean isFlash = ((mimeType != null) && mimeType.equalsIgnoreCase("application/x-shockwave-flash")) //$NON-NLS-1$
				|| ((uri != null) && uri.toLowerCase().endsWith(".swf")) //$NON-NLS-1$
				|| ((extension != null) && extension.toLowerCase().endsWith(".swf")); //$NON-NLS-1$
		return isFlash;
	}
}
