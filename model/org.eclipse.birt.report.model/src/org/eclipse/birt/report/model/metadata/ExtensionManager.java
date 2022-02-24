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

package org.eclipse.birt.report.model.metadata;

/**
 * Represents the extension manager which is responsible to load all extensions
 * that Model supports. This class can not be instantiated and derived.
 */

public final class ExtensionManager extends ExtensionManagerImpl {

	/**
	 * the singleton instance
	 */

	private static ExtensionManager instance;

	protected ExtensionManager() {
		super();
	}

	/**
	 * create the static instance. It is a separate function so that getInstance do
	 * not need to be synchronized
	 */
	private synchronized static void createInstance() {
		if (instance == null)
			instance = new ExtensionManager();
	}

	/**
	 * @return the single instance for the extension manager
	 */
	static public ExtensionManager getInstance() {
		if (instance == null)
			createInstance();

		return instance;
	}

	/**
	 * Release all the resources in this class.
	 */

	static void releaseInstance() {
		instance = null;
	}
}
