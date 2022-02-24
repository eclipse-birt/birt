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
 * Factory class to create an instance of DesignEngine
 */

public interface IDesignEngineFactory {

	/**
	 * The extension point used to create the factory object.
	 * 
	 * @see org.eclipse.birt.core.framework.Platform#createFactoryObject(String)
	 */

	public static final String EXTENSION_DESIGN_ENGINE_FACTORY = "org.eclipse.birt.report.model.DesignEngineFactory"; //$NON-NLS-1$

	/**
	 * Creates a new design engine object.
	 * 
	 * @param config the given design config
	 * @return the design engine object
	 */

	IDesignEngine createDesignEngine(DesignConfig config);

}
