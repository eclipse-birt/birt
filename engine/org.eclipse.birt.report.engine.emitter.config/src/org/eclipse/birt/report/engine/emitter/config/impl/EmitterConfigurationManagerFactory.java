/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.config.impl;

import org.eclipse.birt.report.engine.emitter.config.IEmitterConfigurationManager;
import org.eclipse.birt.report.engine.emitter.config.IEmitterConfigurationManagerFactory;

/**
 * EmitterConfigurationManagerFactory
 */
public class EmitterConfigurationManagerFactory implements IEmitterConfigurationManagerFactory {

	private static EmitterConfigurationManager instance = new EmitterConfigurationManager();

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.emitter.config.
	 * IEmitterConfigurationManagerFactory#createManager()
	 */
	public IEmitterConfigurationManager createManager() {
		return instance;
	}
}
