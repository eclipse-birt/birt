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
package org.eclipse.birt.data.engine.executor.transform.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.cache.BasicCachedList;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.transform.ResultSetPopulator;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.odi.IAggrInfo;
import org.eclipse.birt.data.engine.odi.IAggrValueHolder;

public class AggrValuesUpdator implements IAggrValueHolder {

	private IAggrInfo[] aggrInfos;
	private HashMap<String, Integer> aggrIndexMap;
	private ResultSetPopulator populator;
	private String tempDir;

	private List<Object>[] aggrValues;
	private List<Object>[] oldAggrValues;
	private HashMap<Integer, List<Integer>> summaryAggrsMap;
	private HashMap<String, Integer> runningAggrsMap;

	@SuppressWarnings("unchecked")
	public AggrValuesUpdator(IAggrValueHolder aggrValuesHolder, ResultSetPopulator populator) throws DataException {
		Set<String> aggrNames = aggrValuesHolder.getAggrNames();
		this.aggrIndexMap = new HashMap<String, Integer>();
		this.aggrInfos = new IAggrInfo[aggrNames.size()];
		this.aggrValues = new List[aggrNames.size()];
		this.oldAggrValues = new List[aggrNames.size()];
		this.summaryAggrsMap = new HashMap<Integer, List<Integer>>();
		this.runningAggrsMap = new HashMap<String, Integer>();
		this.populator = populator;
		this.tempDir = populator.getSession().getTempDir();

		Iterator<String> itr = aggrNames.iterator();
		for (int i = 0; itr.hasNext(); i++) {
			String aggrName = itr.next();
			this.aggrIndexMap.put(aggrName, i);
			this.aggrInfos[i] = aggrValuesHolder.getAggrInfo(aggrName);
			this.aggrValues[i] = new BasicCachedList(tempDir, DataEngineSession.getCurrentClassLoader());
			this.oldAggrValues[i] = aggrValuesHolder.getAggrValues(aggrName);
			if (this.aggrInfos[i].getAggregation().getType() == IAggrFunction.SUMMARY_AGGR) {
				int level = this.aggrInfos[i].getGroupLevel();
				List<Integer> aggrs = summaryAggrsMap.get(level);
				if (aggrs == null) {
					aggrs = new ArrayList<Integer>();
					summaryAggrsMap.put(level, aggrs);
				}
				aggrs.add(i);
			} else {
				runningAggrsMap.put(aggrName, i);
			}
		}
	}

	public IAggrInfo getAggrInfo(String aggrName) throws DataException {
		Integer i = aggrIndexMap.get(aggrName);
		return i == null ? null : aggrInfos[i];
	}

	public boolean hasAggr(String aggrName) throws DataException {
		return aggrIndexMap.get(aggrName) != null;
	}

	public Object getAggrValue(String name) throws DataException {
		assert this.populator != null;

		IAggrInfo aggrInfo = this.getAggrInfo(name);

		if (this.populator.getCache().getCount() == 0) {
			return aggrInfo.getAggregation().getDefaultValue();
		}

		try {
			int groupIndex;

			if (aggrInfo.getAggregation().getType() == IAggrFunction.SUMMARY_AGGR) {
				// Aggregate on the whole list: there is only one group
				if (aggrInfo.getGroupLevel() == 0)
					groupIndex = 0;
				else
					groupIndex = this.getCurrentGroupIndex(aggrInfo.getGroupLevel());
			} else {
				groupIndex = this.getCurrentResultIndex();
			}

			return this.aggrValues[aggrIndexMap.get(name)].get(groupIndex);

		} catch (DataException e) {
			throw e;
		}
	}

	private int getCurrentGroupIndex(int groupLevel) throws DataException {
		assert this.populator != null;

		return this.populator.getResultIterator().getCurrentGroupIndex(groupLevel);
	}

	private int getCurrentResultIndex() throws DataException {
		return this.populator.getResultIterator().getCurrentResultIndex();
	}

	public List getAggrValues(String name) throws DataException {
		return this.aggrValues[aggrIndexMap.get(name)];
	}

	public Set<String> getAggrNames() throws DataException {
		return this.aggrIndexMap.keySet();
	}

	public void onRow(int rowId) {
		// Handle aggregation on each row.
		for (Map.Entry<String, Integer> a : runningAggrsMap.entrySet()) {
			int idx = a.getValue();
			this.aggrValues[idx].add(this.oldAggrValues[idx].get(rowId));
		}

		// Handle aggregation on group level 0.
		List<Integer> lv0Idxs = summaryAggrsMap.get(0);
		for (int i = 0; lv0Idxs != null && i < lv0Idxs.size(); i++) {
			int id = lv0Idxs.get(i);
			if (this.aggrValues[id].size() <= 0)
				this.aggrValues[id].add(this.oldAggrValues[id].get(0));
		}
	}

	public void onGroup(int level, int groupIndex) {
		// Handle aggregation on group level > 0.
		List<Integer> idxs = this.summaryAggrsMap.get(level);
		for (int i = 0; idxs != null && i < idxs.size(); i++) {
			int id = idxs.get(i);
			Object aggr = this.oldAggrValues[id].get(groupIndex);
			this.aggrValues[id].add(aggr);
		}
	}

}
