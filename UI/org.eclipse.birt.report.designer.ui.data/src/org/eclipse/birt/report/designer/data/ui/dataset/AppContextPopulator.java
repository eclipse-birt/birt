
/*******************************************************************************
 * Copyright (c) 2004, 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
