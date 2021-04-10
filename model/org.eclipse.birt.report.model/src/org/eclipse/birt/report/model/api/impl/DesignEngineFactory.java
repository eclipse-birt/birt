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

package org.eclipse.birt.report.model.api.impl;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.IDesignEngine;
import org.eclipse.birt.report.model.api.IDesignEngineFactory;

/**
 * Factory pattern to create an instance of Design Engine
 */

public class DesignEngineFactory implements IDesignEngineFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.IDesignEngineFactory#createDesignEngine()
	 */

	public IDesignEngine createDesignEngine(DesignConfig config) {
		return new DesignEngineImpl(config);
	}

}
