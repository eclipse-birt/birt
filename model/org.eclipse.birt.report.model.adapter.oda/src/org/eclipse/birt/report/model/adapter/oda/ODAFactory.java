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

package org.eclipse.birt.report.model.adapter.oda;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.framework.Platform;

/**
 * 
 * 
 */

public class ODAFactory {

	/**
	 * The logger for errors.
	 */

	protected static final Logger errorLogger = Logger.getLogger(ODAFactory.class.getName());

	private static IODAFactory factory = null;

	/**
	 * @return the factory
	 */

	public static IODAFactory getFactory() {
		if (factory != null)
			return factory;

		Object adapterFactory = Platform.createFactoryObject(IAdapterFactory.EXTENSION_MODEL_ADAPTER_ODA_FACTORY);

		if (adapterFactory == null) {
			errorLogger.log(Level.SEVERE, "The platform has not yet been started. Must start it first..."); //$NON-NLS-1$
			return null;
		}

		if (adapterFactory instanceof IAdapterFactory) {
			factory = ((IAdapterFactory) adapterFactory).getODAFactory();
		}

		return factory;
	}
}
