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

package org.eclipse.birt.data.engine.executor.transform.pass;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.transform.ResultSetPopulator;
import org.eclipse.birt.data.engine.impl.IFilterByRow;
import org.eclipse.birt.data.engine.impl.NoUpdateFilterByRow;
import org.eclipse.birt.data.engine.odi.IResultObjectEvent;
import org.eclipse.birt.data.engine.script.FilterPassController;

/**
 * MultiPass filter processor.Used to apply filters to result data.
 */
class NoUpdateFilterCalculator extends FilterCalculator {

	private NoUpdateFilterCalculator(ResultSetPopulator populator, NoUpdateFilterByRow filterByRow) {
		super(populator, filterByRow);
		this.populator = populator;
		this.filterByRow = filterByRow;
	}

	/**
	 * @param populator
	 * @param filterByRow
	 * @throws DataException
	 */
	@SuppressWarnings("unchecked")
	static void applyFilters(ResultSetPopulator populator, IFilterByRow filterByRow) throws DataException {
		NoUpdateFilterByRow noUpdateRowFilter = new NoUpdateFilterByRow(filterByRow, populator);
		int max = populator.getQuery().getMaxRows();
		populator.getQuery().setMaxRows(0);
		List<IResultObjectEvent> onFetchEvents = populator.getQuery().getFetchEvents();
		List<IResultObjectEvent> runEvents = new ArrayList<>();
		List<IResultObjectEvent> tempSavedEvents = new ArrayList<>();
		for (int i = 0; i < onFetchEvents.size(); i++) {
			if (!(onFetchEvents.get(i) instanceof IFilterByRow)) {
				runEvents.add(onFetchEvents.get(i));
			}
			tempSavedEvents.add(onFetchEvents.get(i));
		}
		onFetchEvents.clear();
		onFetchEvents.addAll(runEvents);
		onFetchEvents.add(noUpdateRowFilter);

		new NoUpdateFilterCalculator(populator, noUpdateRowFilter).applyFilters();

		runEvents.clear();
		onFetchEvents.clear();
		onFetchEvents.addAll(tempSavedEvents);
		populator.getQuery().setMaxRows(max);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.executor.transform.pass.FilterCalculator
	 * #doFiltering(org.eclipse.birt.data.engine.script.FilterPassController)
	 */
	@Override
	protected void doFiltering(FilterPassController filterPass) throws DataException {
		((NoUpdateFilterByRow) filterByRow).setUpdateGroupInfo(true);

		super.doFiltering(filterPass);

		((NoUpdateFilterByRow) filterByRow).close();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.executor.transform.pass.FilterCalculator
	 * #makeFirstPassToMultiPassFilter
	 * (org.eclipse.birt.data.engine.script.FilterPassController)
	 */
	@Override
	protected void makeFirstPassToMultiPassFilter(FilterPassController filterPass) throws DataException {
		((NoUpdateFilterByRow) filterByRow).setUpdateGroupInfo(false);

		super.makeFirstPassToMultiPassFilter(filterPass);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.executor.transform.pass.FilterCalculator#
	 * makePreparationPassToMultiPassFilter(org.eclipse.birt.data.engine.script.
	 * FilterPassController)
	 */
	@Override
	protected void makePreparationPassToMultiPassFilter(FilterPassController filterPass) throws DataException {
		((NoUpdateFilterByRow) filterByRow).setUpdateGroupInfo(false);

		super.makePreparationPassToMultiPassFilter(filterPass);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.executor.transform.pass.FilterCalculator
	 * #makeSecondPassToMultiPassFilter
	 * (org.eclipse.birt.data.engine.script.FilterPassController)
	 */
	@Override
	protected void makeSecondPassToMultiPassFilter(FilterPassController filterPass) throws DataException {
		((NoUpdateFilterByRow) filterByRow).setUpdateGroupInfo(true);

		super.makeSecondPassToMultiPassFilter(filterPass);
	}
}
