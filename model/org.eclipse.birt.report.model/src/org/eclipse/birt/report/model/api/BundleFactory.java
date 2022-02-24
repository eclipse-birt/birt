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

package org.eclipse.birt.report.model.api;

/**
 * The internal factory to find resources in the bundle.
 * 
 */

public class BundleFactory {

	private static volatile IBundleFactory bundleFactory = null;

	/**
	 * Sets the bundle factory.
	 * 
	 * @param factory the bundle factory
	 */

	public synchronized static void setBundleFactory(IBundleFactory factory) {
		bundleFactory = factory;
	}

	/**
	 * Returns the bundle factory.
	 * 
	 * @return the bundle factory.
	 */

	public static IBundleFactory getBundleFactory() {
		if (bundleFactory != null) {
			return bundleFactory;
		}
		synchronized (BundleFactory.class) {
			if (bundleFactory == null) {
				try {
					Class clazz = Class.forName(" org.eclipse.birt.report.model.plugin.PlatformBundleFactory");
					bundleFactory = (IBundleFactory) clazz.newInstance();
				} catch (Exception ex) {
				}
			}
		}
		return bundleFactory;
	}

	/**
	 * Releases bundle factory.
	 */
	public static void releaseInstance() {
		bundleFactory = null;
	}
}
