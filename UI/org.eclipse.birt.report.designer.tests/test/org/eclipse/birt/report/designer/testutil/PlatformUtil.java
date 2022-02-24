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
