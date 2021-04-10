
/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.executor.transform;

import java.util.List;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.BaseQuery;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.odi.IDataSetPopulator;
import org.eclipse.birt.data.engine.odi.IEventHandler;
import org.eclipse.birt.data.engine.odi.IResultClass;

/**
 * 
 */

public class NoRecalculateIVResultSet extends CachedResultSet {
	public NoRecalculateIVResultSet(BaseQuery query, IResultClass meta, IDataSetPopulator odaResultSet,
			IEventHandler eventHandler, DataEngineSession session, List[] groups) throws DataException {
		super();

		this.handler = eventHandler;
		this.resultSetPopulator = new NoRecalculateIVRSPopulator(query, meta, this, session, eventHandler, groups);
		resultSetPopulator.populateResultSet(new OdiResultSetWrapper(odaResultSet));
	}
}
