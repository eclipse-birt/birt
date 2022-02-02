
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.report.data.adapter.impl;

import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.model.api.olap.CubeHandle;

public class TransientDataMartUtil {
	public static void prepareDataSet(Map appContext, DataEngineContext oriContext, IBaseDataSourceDesign dsource,
			IBaseDataSetDesign dset, DataRequestSession session) throws BirtException {
	}

	public static void prepareCube(Map appContext, DataEngineContext oriContext, CubeHandle handle)
			throws BirtException {
	}
}
