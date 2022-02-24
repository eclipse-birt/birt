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

package org.eclipse.birt.report.model.adapter.oda.impl;

import org.eclipse.birt.report.model.adapter.oda.IAdapterFactory;
import org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter;
import org.eclipse.birt.report.model.adapter.oda.IODADesignFactory;
import org.eclipse.birt.report.model.adapter.oda.IODAFactory;
import org.eclipse.birt.report.model.adapter.oda.IReportParameterAdapter;

/**
 * Factory pattern to create an instance of Design Engine
 */

public class AdapterFactory implements IAdapterFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.adapter.oda.IAdapterFactory#getModelOdaAdapter(
	 * )
	 */

	public IModelOdaAdapter createModelOdaAdapter() {
		return new ModelOdaAdapter();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.adapter.oda.IAdapterFactory#getODADesignFactory
	 * ()
	 */

	public IODADesignFactory getODADesignFactory() {
		return new ODADesignFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.adapter.oda.IAdapterFactory#getODAFactory()
	 */

	public IODAFactory getODAFactory() {
		return new ODAFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.IAdapterFactory#
	 * getReportParameterAdapter()
	 */

	public IReportParameterAdapter createReportParameterAdapter() {
		return new ReportParameterAdapter();
	}

}
