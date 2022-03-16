/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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
package org.eclipse.birt.data.engine.executor.transform.group;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.transform.ResultSetPopulator;
import org.eclipse.birt.data.engine.odi.IAggrValueHolder;

public class IncrementalUpdateCaculator {

	protected ResultSetPopulator populator;
	protected List<GroupInfo>[] originGroups;
	protected int[] groupSize;
	protected String tempDir;
	protected GroupInformationUtil groupInfoUtil;
	protected boolean doFiltering;
	protected GroupInfoUpdator[] groupUpdators;
	protected AggrValuesUpdator[] aggrValuesUpdators;

	public IncrementalUpdateCaculator(ResultSetPopulator populator) throws DataException {
		this.populator = populator;
		this.tempDir = populator.getSession().getTempDir();

		List<IAggrValueHolder> aggrValues = populator.getResultIterator().getAggrValueHolders();
		this.aggrValuesUpdators = new AggrValuesUpdator[aggrValues.size()];
		for (int i = 0; i < aggrValuesUpdators.length; i++) {
			aggrValuesUpdators[i] = new AggrValuesUpdator(aggrValues.get(i), populator);
		}

		this.groupInfoUtil = populator.getGroupProcessorManager().getGroupCalculationUtil().getGroupInformationUtil();
		this.originGroups = groupInfoUtil.getGroups();
		this.groupSize = new int[originGroups.length];
		this.groupUpdators = new GroupInfoUpdator[originGroups.length];
		for (int i = 0; i < originGroups.length; i++) {
			groupUpdators[i] = new GroupInfoUpdator(i, tempDir, originGroups[i], getLastGroupIndex(i),
					aggrValuesUpdators);
			groupSize[i] = originGroups[i].size();
		}

	}

	protected int getLastGroupIndex(int level) throws DataException {
		int last;
		if (level < originGroups.length - 1) {
			last = originGroups[level + 1].size() - 1;
		} else {
			last = populator.getCache().getCount() - 1;
		}
		return last;
	}

	protected int getCurrentGroupIndex(int level) throws DataException {
		return groupInfoUtil.getCurrentGroupIndex(level);
	}

	@SuppressWarnings("unchecked")
	public List<GroupInfo>[] getGroups() {
		ArrayList<List<GroupInfo>> groups = new ArrayList<>();
		for (int i = 0; i < groupUpdators.length; i++) {
			groupUpdators[i].close();
			groups.add(groupUpdators[i].getGroups());
		}
		return groups.toArray(new List[0]);
	}

	protected void acceptAggrValues(int rowIndex) {
		for (int i = 0; i < aggrValuesUpdators.length; i++) {
			aggrValuesUpdators[i].onRow(rowIndex);
		}
	}

}
