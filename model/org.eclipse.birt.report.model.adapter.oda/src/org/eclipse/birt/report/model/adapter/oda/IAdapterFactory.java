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

/**
 * Factory class to create an instance of DesignEngine
 */

public interface IAdapterFactory {

	/**
	 * The extension point used to create the factory object.
	 *
	 * @see org.eclipse.birt.core.framework.Platform#createFactoryObject(String)
	 */

	String EXTENSION_MODEL_ADAPTER_ODA_FACTORY = "org.eclipse.birt.report.model.adapter.oda.AdapterFactory"; //$NON-NLS-1$

	IODADesignFactory getODADesignFactory();

	IODAFactory getODAFactory();

	IModelOdaAdapter createModelOdaAdapter();

	IReportParameterAdapter createReportParameterAdapter();
}
