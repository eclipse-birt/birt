/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IPreloadedResultIterator;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IDataIterator;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.IResultMetaData;
import org.eclipse.birt.report.engine.i18n.MessageConstants;

public class DataIterator implements IDataIterator {

	protected static Logger logger = Logger.getLogger(DataIterator.class.getName());

	protected IExtractionResults results;
	protected IResultIterator iterator;
	protected int startRow = -1;
	protected int maxRows;
	protected int rowCount;
	private boolean beforeFirstRow = true;

	private boolean invalidStartRow = false;

	DataIterator(IExtractionResults results, IResultIterator iterator, int startRow, int maxRows) throws BirtException {
		this.results = results;
		this.iterator = iterator;
		this.startRow = startRow;
		this.maxRows = maxRows;
		this.rowCount = 0;
		beforeFirstRow = true;
		if (iterator instanceof IPreloadedResultIterator) {
			// Predefine max row numbers and starting row index to save
			// execution time. Report engine may still limits the size but it's
			// fine to leave as it is.
			((IPreloadedResultIterator) iterator).setMaxRows(maxRows);
			((IPreloadedResultIterator) iterator).setStartingRow(startRow);
		}
		if (startRow > 0) {
			try {
				iterator.moveTo(startRow - 1);
			} catch (BirtException e) {
				logger.log(Level.WARNING, "The specified startRow value is out of range of the result set!", e);
				invalidStartRow = true;
			}
		}
	}

	@Override
	public IExtractionResults getQueryResults() {
		return results;
	}

	@Override
	public IResultMetaData getResultMetaData() throws BirtException {
		return results.getResultMetaData();
	}

	@Override
	public boolean next() throws BirtException {
		if (beforeFirstRow) {
			beforeFirstRow = false;
		}
		rowCount++;
		if (invalidStartRow || maxRows >= 0 && rowCount > maxRows) {
			return false;
		}
		return iterator.next();
	}

	@Override
	public Object getValue(String columnName) throws BirtException {
		if (beforeFirstRow) {
			throw new EngineException(MessageConstants.RESULTSET_ITERATOR_ERROR);
		}
		return iterator.getValue(columnName);
	}

	@Override
	public Object getValue(int index) throws BirtException {
		if (beforeFirstRow) {
			throw new EngineException(MessageConstants.RESULTSET_ITERATOR_ERROR);
		}
		IResultMetaData metaData = getResultMetaData();
		String columnName = metaData.getColumnName(index);
		return iterator.getValue(columnName);
	}

	@Override
	public void close() {
		try {
			iterator.close();
		} catch (BirtException ex) {
		}
	}

	@Override
	public boolean isEmpty() throws BirtException {
		return iterator.isEmpty();
	}

	@Override
	public IResultIterator getResultIterator() {
		return this.iterator;
	}
}
