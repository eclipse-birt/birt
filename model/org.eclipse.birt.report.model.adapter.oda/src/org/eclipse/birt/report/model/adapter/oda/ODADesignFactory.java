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

package org.eclipse.birt.report.model.adapter.oda;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.framework.Platform;

/**
 * The factory to create ODADesignFactory so that methods in ODA DesignFactory
 * can be used.
 *
 */

public class ODADesignFactory {

	/**
	 * The logger for errors.
	 */

	protected static final Logger errorLogger = Logger.getLogger(ODADesignFactory.class.getName());

	private static IODADesignFactory factory = null;

	/**
	 * @return the factory
	 */

	public synchronized static IODADesignFactory getFactory() {
		if (factory != null) {
			return factory;
		}

		Object adapterFactory = Platform.createFactoryObject(IAdapterFactory.EXTENSION_MODEL_ADAPTER_ODA_FACTORY);

		if (adapterFactory == null) {
			errorLogger.log(Level.SEVERE, "The platform has not yet been started. Must start it first..."); //$NON-NLS-1$
			return null;
		}

		if (adapterFactory instanceof IAdapterFactory) {
			factory = ((IAdapterFactory) adapterFactory).getODADesignFactory();
		}

		return factory;
	}
}
