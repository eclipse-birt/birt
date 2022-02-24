
/*******************************************************************************
 * Copyright (c) 2004, 2010 Actuate Corporation.
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
package org.eclipse.birt.report.designer.data.ui.dataset;

import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;

/**
 * 
 */

public class AppContextPopulator {
	public static void populateApplicationContext(DataSourceHandle handle, Map input) throws BirtException {
	}

	public static void populateApplicationContext(CubeHandle handle, Map input) throws BirtException {
	}

	public static void populateApplicationContext(DataSetHandle handle, Map input) throws BirtException {

	}

	public static void populateApplicationContext(DataSetHandle handle, DataRequestSession session)
			throws BirtException {
	}
}
