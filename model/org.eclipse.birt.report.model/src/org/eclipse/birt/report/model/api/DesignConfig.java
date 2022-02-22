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

import java.util.HashMap;

import org.eclipse.birt.core.framework.PlatformConfig;

/**
 * Wraps around configuration settings for design engine. Allows engine to
 * provide customized implementations for image handling, hyperlink handling and
 * font handling, etc.
 */

public class DesignConfig extends PlatformConfig implements IDesignConfig {

	/**
	 * constructor
	 */

	public DesignConfig() {
		// set default configruation
	}

	/**
	 * sets a configuration variable that is available through scripting in engine
	 *
	 * @param name  configuration variable name
	 * @param value configuration variable value
	 */

	public void setConfigurationVariable(String name, String value) {
		setProperty(name, value);
	}

	/**
	 * returns a hash map that contains all the configuration objects
	 *
	 * @return the configuration object map
	 */

	public HashMap getConfigMap() {
		return properties;
	}

	/**
	 * @return the resourceLocator
	 */

	public IResourceLocator getResourceLocator() {
		Object locator = getProperty(RESOURCE_LOCATOR);
		if (locator instanceof IResourceLocator) {
			return (IResourceLocator) locator;
		}
		return null;
	}

	/**
	 * @param resourceLocator the resourceLocator to set
	 */

	public void setResourceLocator(IResourceLocator resourceLocator) {
		setProperty(RESOURCE_LOCATOR, resourceLocator);
	}

}
