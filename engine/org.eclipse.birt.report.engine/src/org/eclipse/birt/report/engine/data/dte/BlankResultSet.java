/*******************************************************************************
 * Copyright (c)2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.data.dte;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;

public class BlankResultSet extends QueryResultSet {

	boolean hasNext = true;
	IQueryResultSet rset;

	public BlankResultSet(IQueryResultSet rset) {
		this.rset = rset;
	}

	@Override
	public BigDecimal getBigDecimal(String name) throws BirtException {
		return null;
	}

	@Override
	public Blob getBlob(String name) throws BirtException {
		return null;
	}

	@Override
	public Boolean getBoolean(String name) throws BirtException {
		return null;
	}

	@Override
	public byte[] getBytes(String name) throws BirtException {
		return null;
	}

	@Override
	public Date getDate(String name) throws BirtException {
		return null;
	}

	@Override
	public Double getDouble(String name) throws BirtException {
		return null;
	}

	@Override
	public int getEndingGroupLevel() throws BirtException {
		return 0;
	}

	@Override
	public String getGroupId(int groupLevel) {
		return null;
	}

	@Override
	public Integer getInteger(String name) throws BirtException {
		return null;
	}

	@Override
	public IResultIterator getResultIterator() {
		return null;
	}

	@Override
	public IResultMetaData getResultMetaData() throws BirtException {
		return null;
	}

	@Override
	public long getRowIndex() {
		return 0;
	}

	@Override
	public int getStartingGroupLevel() throws BirtException {
		return 0;
	}

	@Override
	public String getString(String name) throws BirtException {
		return null;
	}

	@Override
	public Object getValue(String name) throws BirtException {
		return null;
	}

	@Override
	public boolean isEmpty() throws BirtException {
		return hasNext;
	}

	@Override
	public boolean next() throws BirtException {
		if (hasNext) {
			hasNext = false;
			return true;
		}
		return false;
	}

	@Override
	public boolean skipTo(long rowIndex) throws BirtException {
		return false;
	}

	@Override
	public void close() {
		if (rset != null) {
			rset.close();
		}
	}

	@Override
	public Object evaluate(String expr) throws BirtException {
		return null;
	}

	@Override
	public Object evaluate(String language, String expr) throws BirtException {
		return null;
	}

	@Override
	public Object evaluate(IBaseExpression expr) throws BirtException {
		return null;
	}

	@Override
	public DataSetID getID() {
		return null;
	}

	@Override
	public IBaseResultSet getParent() {
		return null;
	}

	@Override
	public IBaseQueryResults getQueryResults() {
		return null;
	}

	@Override
	public String getRawID() throws BirtException {
		return null;
	}

	@Override
	public int getType() {
		return IBaseResultSet.QUERY_RESULTSET;
	}

	@Override
	public boolean isFirst() {
		return hasNext;
	}

	@Override
	public boolean isBeforeFirst() {
		return false;
	}

	@Override
	public String getQueryResultsID() {
		if (rset instanceof QueryResultSet) {
			return ((QueryResultSet) rset).getQueryResultsID();
		}
		return null;
	}
}
