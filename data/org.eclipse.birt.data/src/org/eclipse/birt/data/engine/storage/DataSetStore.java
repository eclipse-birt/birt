/*******************************************************************************
 * Copyright (c) 2004, 2012 Actuate Corporation.
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

package org.eclipse.birt.data.engine.storage;

import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.impl.document.stream.StreamManager;
import org.eclipse.birt.data.engine.impl.index.IAuxiliaryIndexCreator;
import org.eclipse.birt.data.engine.odi.IResultClass;

/**
 * 
 */

public class DataSetStore {

	public static IDataSetReader createReader(StreamManager manager, IResultClass resultClass, boolean includeInnerId,
			Map<?, ?> appContext) throws DataException {
		return null;
	}

	public static IDataSetWriter createWriter(StreamManager manager, IResultClass resultClass, Map<?, ?> appContext,
			DataEngineSession session, List<IAuxiliaryIndexCreator> auxiliaryIndexs) throws DataException {
		return null;
	}

	public static IDataSetUpdater createUpdater(StreamManager manager, IResultClass resultClass, Map<?, ?> appContext,
			DataEngineSession session, List<IAuxiliaryIndexCreator> auxiliaryIndexs) throws DataException {
		return null;
	}

	public static boolean isDataMartStore(Map<?, ?> appContext, DataEngineSession session) throws DataException {
		return false;
	}
}
