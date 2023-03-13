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
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.ResultFieldMetadata;
import org.eclipse.birt.data.engine.executor.transform.OdiResultSetWrapper;
import org.eclipse.birt.data.engine.executor.transform.ResultSetPopulator;
import org.eclipse.birt.data.engine.impl.ComputedColumnHelper;
import org.eclipse.birt.data.engine.odi.ICustomDataSet;
import org.eclipse.birt.data.engine.odi.IResultClass;

/**
 * This class is used to populate computed column values in multipass row
 * processing.
 */
class ComputedColumnCalculator {
	/**
	 *
	 */
	private ResultSetPopulator populator;
	private ComputedColumnsState iccState;
	private ComputedColumnHelper computedColumnHelper;

	/**
	 *
	 * @param populator
	 * @param singlePassRowProcessor
	 */
	private ComputedColumnCalculator(ResultSetPopulator populator, ComputedColumnsState iccState,
			ComputedColumnHelper computedColumnHelper) {
		this.populator = populator;
		this.iccState = iccState;
		this.computedColumnHelper = computedColumnHelper;
	}

	/**
	 * This method is used to populate computed columns.
	 *
	 * @param odaResultSet
	 * @param iccState
	 * @param computedColumnHelper
	 * @param stopSign
	 * @throws DataException
	 */
	static void populateComputedColumns(ResultSetPopulator populator, OdiResultSetWrapper odaResultSet,
			ComputedColumnsState iccState, ComputedColumnHelper computedColumnHelper) throws DataException {
		new ComputedColumnCalculator(populator, iccState, computedColumnHelper)
				.doPopulate(odaResultSet.getWrappedOdiResultSet() instanceof ICustomDataSet);

	}

	/**
	 *
	 * @param isCustomDataSet
	 * @param stopSign
	 * @throws DataException
	 */
	private void doPopulate(boolean isCustomDataSet) throws DataException {
		while (needMoreExpressionProcessOnComputedColumns()) {
			makeAPassToComputedColumn(isCustomDataSet);
		}
	}

	/**
	 * Detect whether should one more pass being carried out.If there are still one
	 * or more than one computed columns in iccState are marked as "unavailable"
	 * then the method return false else return true.
	 *
	 * @param iccState
	 * @return
	 */
	private boolean needMoreExpressionProcessOnComputedColumns() {
		if (iccState == null) {
			return false;
		}
		for (int i = 0; i < iccState.getCount(); i++) {
			if (!iccState.isValueAvailable(i)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Make a Pass. Populate all achievable computed column values.
	 *
	 * @param iccState
	 * @param computedColumnHelper
	 * @param backupFetchEvents
	 * @throws DataException
	 */
	private void makeAPassToComputedColumn(boolean isCustomDataSet) throws DataException {
		// ICustomDataSet need special treatment.
		if (isCustomDataSet) {
			populator.setResultSetMetadata(rebuildCustomedResultClass(populator.getResultSetMetadata(), false));
		}

		populateComputedColumns();

		// ICustomDataSet need special treatment.
		if (isCustomDataSet) {
			populator.setResultSetMetadata(rebuildCustomedResultClass(populator.getResultSetMetadata(), true));
		}

		PassUtil.pass(populator, new OdiResultSetWrapper(populator.getResultIterator()), true);
	}

	/**
	 * This method is used to rebuild the customized result set metadata so that it
	 * can be used by ExpressionProcessor.The current implementation of
	 * ExpressionProcessor distinct computed columns and ordinary columns using
	 * IResultClass meta data. The metadata of ICustomedResult, however, mark all
	 * columns as computed columns. This method is used to treat the metadata so
	 * that it can properly servers the ExpressionProcessor implemention. This
	 * method, however, makes this class coupling with certain IExpressionProcessor
	 * instance, that is ,ExpressionProcessor. And should be refactor in future.
	 *
	 * @param meta
	 * @param returnToOriginalValue if true, then reset the metaData to the state
	 *                              when it is passed to this CachedResultSet, if
	 *                              false, then set the metaData so that it can be
	 *                              used by ExpressionProcessor
	 * @return
	 * @throws DataException
	 */
	private static IResultClass rebuildCustomedResultClass(IResultClass meta, boolean returnToOriginalValue)
			throws DataException {
		List projectedColumns = new ArrayList();
		for (int i = 1; i <= meta.getFieldCount(); i++) {
			projectedColumns.add(new ResultFieldMetadata(0, meta.getFieldName(i), meta.getFieldLabel(i),
					meta.getFieldValueClass(i), meta.getFieldNativeTypeName(i),
					returnToOriginalValue ? true
							: (PassUtil.isTemporaryResultSetComputedColumn(meta.getFieldName(i)) ? true : false),
					meta.getAnalysisType(i), meta.getAnalysisColumn(i), meta.isIndexColumn(i),
					meta.isCompressedColumn(i)));
		}
		return new ResultClass(projectedColumns);
	}

	/**
	 * Populate the computed columns to be used in current pass.
	 *
	 * @param iccState             The object which indicate the status of computed
	 *                             columns.
	 * @param computedColumnHelper
	 * @throws DataException
	 */
	private void populateComputedColumns() throws DataException {

		calculateAggregation();

		// The following code block add available computed column to
		// ComputedColumnHelper.The value returned by first calling of
		// getLastAccessedComputedColumnIndex()
		// should be -1.
		int startValue = iccState.getLastAccessedComputedColumnIndex() + 1;

		computedColumnHelper.getComputedColumnList().clear();
		for (int i = startValue; i < iccState.getCount(); i++) {
			if (iccState.isValueAvailable(i)) {
				computedColumnHelper.getComputedColumnList().add(iccState.getComputedColumn(i));
				iccState.setLastAccessedComputedColumnId(i);

			} else {
				break;
			}
		}
		computedColumnHelper.setRePrepare(true);
	}

	/**
	 * Calculate the aggregation in computed columns
	 *
	 * @param iccState
	 * @throws DataException
	 */
	private void calculateAggregation() throws DataException {
		// Reset the ResultIterator which is used by IExpressionProcessor
		// instance.
		populator.getExpressionProcessor().setResultIterator(populator.getResultIterator());

		populator.getExpressionProcessor().evaluateMultiPassExprOnCmp(iccState, true);
	}
}
