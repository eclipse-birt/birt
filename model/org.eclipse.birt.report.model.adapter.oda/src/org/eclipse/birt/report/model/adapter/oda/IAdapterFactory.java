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

/**
 * Factory class to create an instance of DesignEngine
 */

public interface IAdapterFactory {

	/**
	 * The extension point used to create the factory object.
	 * 
	 * @see org.eclipse.birt.core.framework.Platform#createFactoryObject(String)
	 */

	public static final String EXTENSION_MODEL_ADAPTER_ODA_FACTORY = "org.eclipse.birt.report.model.adapter.oda.AdapterFactory"; //$NON-NLS-1$

	public IODADesignFactory getODADesignFactory();

	public IODAFactory getODAFactory();

	public IModelOdaAdapter createModelOdaAdapter();

	public IReportParameterAdapter createReportParameterAdapter();
}
