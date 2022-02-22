
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.executor.aggregation;

import java.util.HashMap;
import java.util.List;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IAggrDefnManager;
import org.eclipse.birt.data.engine.odi.IAggrInfo;

/**
 *
 */

public class AggrDefnManager implements IAggrDefnManager {
	private IAggrInfo[] aggrDefns;
	private HashMap index;

	public AggrDefnManager(List aggrDefns) {
		this.aggrDefns = new IAggrInfo[aggrDefns.size()];
		this.index = new HashMap();
		for (int i = 0; i < aggrDefns.size(); i++) {
			this.aggrDefns[i] = (IAggrInfo) aggrDefns.get(i);
			this.index.put(this.aggrDefns[i].getName(), Integer.valueOf(i));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.executor.aggregation.IAggrDefnManager#
	 * getAggrDefn(java.lang.String)
	 */
	@Override
	public IAggrInfo getAggrDefn(String name) throws DataException {
		return this.aggrDefns[this.getAggrDefnIndex(name)];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.executor.aggregation.IAggrDefnManager#
	 * getAggrDefn(int)
	 */
	@Override
	public IAggrInfo getAggrDefn(int index) throws DataException {
		return this.aggrDefns[index];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.executor.aggregation.IAggrDefnManager#
	 * getAggrDefnIndex(java.lang.String)
	 */
	@Override
	public int getAggrDefnIndex(String name) throws DataException {
		if (this.index.get(name) == null) {
			return -1;
		}
		return ((Integer) (this.index.get(name))).intValue();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.executor.aggregation.IAggrDefnManager#
	 * getAggrCount()
	 */
	@Override
	public int getAggrCount() {
		return this.aggrDefns.length;
	}
}
