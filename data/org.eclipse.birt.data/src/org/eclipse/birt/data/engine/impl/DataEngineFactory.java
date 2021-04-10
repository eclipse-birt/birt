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
