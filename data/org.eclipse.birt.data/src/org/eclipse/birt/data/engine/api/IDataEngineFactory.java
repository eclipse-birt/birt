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
package org.eclipse.birt.data.engine.api;

import org.eclipse.birt.core.exception.BirtException;

/**
 * Factory class to create an instance of DataEngine
 */
public interface IDataEngineFactory {

	/**
	 * the extension point used to create the factory object.
	 * 
	 * @see org.eclipse.birt.core.framework.Platform#createFactoryObject(String)
	 */
	static final String EXTENSION_DATA_ENGINE_FACTORY = "org.eclipse.birt.data.DataEngineFactory";

	/**
	 * create a new report engine object.
	 * 
	 * @param DataEngineContext context used to create the data engine.
	 * @return the data engine object
	 */
	DataEngine createDataEngine(DataEngineContext context) throws BirtException;

}
