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

package org.eclipse.birt.report.designer.testutil;

import org.eclipse.core.runtime.Platform;

/**
 * The utility class for platform related tests
 */

public class PlatformUtil {

	static public boolean isWindows() {
		return Platform.getOS().equals("win32"); //$NON-NLS-1$
	}
}