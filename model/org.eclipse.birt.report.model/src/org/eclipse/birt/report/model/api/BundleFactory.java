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
