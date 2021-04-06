
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
