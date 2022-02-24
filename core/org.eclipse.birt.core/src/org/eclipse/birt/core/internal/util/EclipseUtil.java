/*******************************************************************************
 * Copyright (c) 2022 Remain Software
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Wim Jongman - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.core.internal.util;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * Eclipse and OSGi utilities
 *
 * @since 4.9
 *
 */
public class EclipseUtil {

	/**
	 * Safely gets a bundle from Eclipse
	 *
	 * @param bundleId
	 * @return the required bundle or null if the bundle could not be found or the
	 *         platform is not running.
	 */
	public static Bundle getBundle(String bundleId) {
		if (Platform.isRunning()) {
			return Platform.getBundle(bundleId);
		}
		return null;
	}
}
