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
