/*******************************************************************************
 * Copyright (c)2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
