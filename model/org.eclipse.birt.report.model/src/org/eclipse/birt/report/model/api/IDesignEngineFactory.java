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
