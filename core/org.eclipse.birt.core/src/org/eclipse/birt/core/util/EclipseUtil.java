/*******************************************************************************
 * Copyright (c) 2022 Remain Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Wim Jongman - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.core.util;

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
