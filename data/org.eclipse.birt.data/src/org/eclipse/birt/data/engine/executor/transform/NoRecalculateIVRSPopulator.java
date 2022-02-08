
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
package org.eclipse.birt.data.engine.executor.transform;

import java.util.List;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.BaseQuery;
import org.eclipse.birt.data.engine.executor.transform.pass.NoRecalculatePassManager;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.odi.IEventHandler;
import org.eclipse.birt.data.engine.odi.IResultClass;

/**
 * 
 */

public class NoRecalculateIVRSPopulator extends ResultSetPopulator {

	public NoRecalculateIVRSPopulator(BaseQuery query, IResultClass rsMeta, CachedResultSet ri,
			DataEngineSession session, IEventHandler eventHandler, List[] groups) throws DataException {
		super(query, rsMeta, ri, session, eventHandler);
		this.getGroupProcessorManager().getGroupCalculationUtil().getGroupInformationUtil().setGroups(groups);
	}

	public void populateResultSet(OdiResultSetWrapper odaResultSet) throws DataException {
		NoRecalculatePassManager.populateResultSet(this, odaResultSet, this.session);
	}
}
