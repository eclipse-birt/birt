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

package org.eclipse.birt.report.engine.emitter.config;

import java.util.Locale;

/**
 * IEmitterConfigurationManager
 */
public interface IEmitterConfigurationManager {

	/**
	 * Returns an emitter descriptor with the specified emitter ID.
	 * 
	 * @param emitterID the emitter ID.
	 * @return an emitter descriptor with the specified emitter ID.
	 */
	IEmitterDescriptor getEmitterDescriptor(String emitterID);

	/**
	 * Returns an emitter descriptor with the specified emitter ID.
	 * 
	 * @param emitterID the emitter ID.
	 * @param locale    the descriptor locale
	 * @return an emitter descriptor with the specified emitter ID.
	 */
	IEmitterDescriptor getEmitterDescriptor(String emitterID, Locale locale);

	/**
	 * Register a custom emitter descriptor manually. It will overwrite the
	 * descriptor with same emitter id if exists.
	 * 
	 * @param descriptor
	 */
	void registerEmitterDescriptor(IEmitterDescriptor descriptor);

	/**
	 * Remove a custom emitter descriptor manually. If there is a descriptor
	 * registered through extension with same emitter id, then a descriptor will
	 * still be returned in following <code>getEmitterDescriptor()</code> call.
	 * 
	 * @param descriptor
	 */
	void deregisterEmitterDescriptor(IEmitterDescriptor descriptor);
}
