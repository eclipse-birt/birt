/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.executor.transform.pass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.dscache.DataSetFromCache;
import org.eclipse.birt.data.engine.executor.transform.OdiResultSetWrapper;
import org.eclipse.birt.data.engine.executor.transform.ResultSetPopulator;
import org.eclipse.birt.data.engine.executor.transform.TransformationConstants;
import org.eclipse.birt.data.engine.impl.ComputedColumnHelper;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.impl.FilterByRow;
import org.eclipse.birt.data.engine.odi.IEventHandler;
import org.eclipse.birt.data.engine.script.OnFetchScriptHelper;

/**
 * The entry point of this package.
 */
public class PassManager {
	//
	protected ResultSetPopulator populator;

	protected ComputedColumnHelper computedColumnHelper;

	protected FilterByRow filterByRow;

	protected ComputedColumnsState iccState;
	protected PassStatusController psController;

	/**
	 * Constructor.
	 * 
	 * @param populator
	 */
	protected PassManager(ResultSetPopulator populator) {
		this.populator = populator;
	}

	/**
	 * 
	 * @param populator
	 * @param odaResultSet
	 * @param stopSign
	 * @throws DataException
	 */
	public static void populateResultSet(ResultSetPopulator populator, OdiResultSetWrapper odaResultSet,
			DataEngineSession session) throws DataException {
		new PassManager(populator).pass(odaResultSet);
	}

	public static void populateDataSetResultSet(ResultSetPopulator populator, OdiResultSetWrapper odaResultSetWrapper)
			throws DataException {
		new PassManager(populator).prepareDataSetResultSet(odaResultSetWrapper);
	}

	private void prepareDataSetResultSet(OdiResultSetWrapper odaResultSet) throws DataException {
		this.populator.getExpressionProcessor().setDataSetMode(true);
		prepareFetchEventList();
		psController = new PassStatusController(populator, filterByRow, computedColumnHelper);
		boolean needMultiPass = psController.needMultipassProcessing();
		if (!needMultiPass) {
			doSinglePass(odaResultSet);
		} else {
			populateDataSet(odaResultSet);
		}

	}

	protected void prepareQueryResultSet() throws DataException {
		if (psController.needMultipassProcessing()) {
			this.populator.getExpressionProcessor().setDataSetMode(false);
			ResultSetProcessUtil.doPopulate(this.populator, iccState, computedColumnHelper, filterByRow, psController,
					Arrays.asList(this.populator.getQuery().getOrdering()));
		} else {
			ResultSetProcessUtil.doPopulateAggregation(this.populator, iccState, computedColumnHelper, filterByRow,
					psController, Arrays.asList(this.populator.getQuery().getOrdering()));
		}
	}

	/**
	 * Pass the oda result set.
	 * 
	 * @param odaResultSet
	 * @param stopSign
	 * @throws DataException
	 */
	protected void pass(OdiResultSetWrapper odaResultSet) throws DataException {
		prepareDataSetResultSet(odaResultSet);
		prepareQueryResultSet();
	}

	/**
	 * 
	 *
	 */
	private void prepareFetchEventList() {
		Object[] fetchEventsList = getFetchEventListFromQuery(this.populator);

		for (int i = 0; i < fetchEventsList.length; i++) {
			if (fetchEventsList[i] instanceof ComputedColumnHelper) {
				computedColumnHelper = (ComputedColumnHelper) fetchEventsList[i];
			} else if (fetchEventsList[i] instanceof FilterByRow) {
				filterByRow = (FilterByRow) fetchEventsList[i];
			}
		}
	}

	/**
	 * 
	 * @param odaResultSet
	 * @param stopSign
	 * @throws DataException
	 */
	private void doSinglePass(OdiResultSetWrapper odaResultSet) throws DataException {
		if (computedColumnHelper != null)
			computedColumnHelper.setModel(TransformationConstants.DATA_SET_MODEL);
		PassUtil.pass(this.populator, odaResultSet, false);
		this.populator.getExpressionProcessor().setDataSetMode(false);

		removeOnFetchScriptHelper();
		handleEndOfDataSetProcess();
	}

	/**
	 * The OnFetchScript should only be calcualted one time.
	 */
	private void removeOnFetchScriptHelper() {
		if (this.populator.getQuery().getFetchEvents() == null)
			return;
		for (Iterator it = this.populator.getQuery().getFetchEvents().iterator(); it.hasNext();) {
			Object o = it.next();
			if (o instanceof OnFetchScriptHelper) {
				it.remove();
			}
		}
	}

	/**
	 * Return the fetch event list from the given query.
	 * 
	 * @param rsp
	 * @return
	 */
	private static Object[] getFetchEventListFromQuery(ResultSetPopulator rsp) {
		// Temp variable which is used to store the FetchEvents of a query.
		// If a query does not hava a FetchEvent then return an empty array.
		Object[] fetchEventsList = null;

		if (rsp.getQuery().getFetchEvents() == null) {
			fetchEventsList = new Object[] {};
		} else {
			fetchEventsList = rsp.getQuery().getFetchEvents().toArray();
		}
		return fetchEventsList;
	}

	private void populateDataSet(OdiResultSetWrapper odaResultSet) throws DataException {
		// The data member which record the state of each computed column.
		// The order that computed columns are cached in iccState is significant
		// and only after all
		// the computed columns in higherindex is marked as "avaliable" that the
		// computed columns in lower
		// index can be marked as available

		if (computedColumnHelper != null) {
			iccState = new ComputedColumnsState(computedColumnHelper);
		}

		List cachedSorting = Arrays.asList(this.populator.getQuery().getOrdering() == null ? new Object[0]
				: this.populator.getQuery().getOrdering());
		this.populator.getQuery().setOrdering(new ArrayList());
		this.populator.getExpressionProcessor().setDataSetMode(true);

		populateResultSetCacheInResultSetPopulator(odaResultSet);

		if (!(odaResultSet.getWrappedOdiResultSet() instanceof DataSetFromCache)) {
			DataSetProcessUtil.doPopulate(this.populator, iccState, computedColumnHelper, filterByRow, psController);
		}
		handleEndOfDataSetProcess();
		this.populator.getQuery().setOrdering(cachedSorting);
	}

	/**
	 * 
	 * @param odaResultSet
	 * @param stopSign
	 * @throws DataException
	 */
	private void populateResultSetCacheInResultSetPopulator(OdiResultSetWrapper odaResultSet) throws DataException {
		int max = 0;

		if (computedColumnHelper != null)
			computedColumnHelper.setModel(TransformationConstants.PRE_CALCULATE_MODEL);

		if (filterByRow != null) {
			filterByRow.setWorkingFilterSet(FilterByRow.NO_FILTER);
		}

		max = this.populator.getQuery().getMaxRows();

		if (filterByRow != null)
			this.populator.getQuery().setMaxRows(0);

		PassUtil.pass(this.populator, odaResultSet, false);
		this.removeOnFetchScriptHelper();
		this.populator.getQuery().setMaxRows(max);
	}

	/**
	 * @throws DataException
	 * 
	 *
	 */
	private void handleEndOfDataSetProcess() throws DataException {
		IEventHandler eventHandler = this.populator.getEventHandler();

		if (eventHandler != null)
			eventHandler.handleEndOfDataSetProcess(this.populator.getResultIterator());
	}

}
