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
package org.eclipse.birt.data.engine.impl;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IDataEngineFactory;

/**
 * Factory pattern to create an instance of Data Engine
 */
public class DataEngineFactory implements IDataEngineFactory {

	/*
	 * @see
	 * org.eclipse.birt.data.engine.api.IDataEngineFactory#createDataEngine(org.
	 * eclipse.birt.data.engine.api.DataEngineContext)
	 */
	public DataEngine createDataEngine(DataEngineContext context) throws BirtException {
		if (context == null) {
			try {
				context = DataEngineContext.newInstance(DataEngineContext.DIRECT_PRESENTATION, new ScriptContext(),
						null, null, null);
			} catch (BirtException e) {
				// impossible get here
			}
		}

		return new DataEngineImpl(context);
	}

}
