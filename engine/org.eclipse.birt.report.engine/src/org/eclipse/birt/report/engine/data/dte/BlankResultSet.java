/*******************************************************************************
 * Copyright (c)2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public BigDecimal getBigDecimal(String name) throws BirtException {
		return null;
	}

	public Blob getBlob(String name) throws BirtException {
		return null;
	}

	public Boolean getBoolean(String name) throws BirtException {
		return null;
	}

	public byte[] getBytes(String name) throws BirtException {
		return null;
	}

	public Date getDate(String name) throws BirtException {
		return null;
	}

	public Double getDouble(String name) throws BirtException {
		return null;
	}

	public int getEndingGroupLevel() throws BirtException {
		return 0;
	}

	public String getGroupId(int groupLevel) {
		return null;
	}

	public Integer getInteger(String name) throws BirtException {
		return null;
	}

	public IResultIterator getResultIterator() {
		return null;
	}

	public IResultMetaData getResultMetaData() throws BirtException {
		return null;
	}

	public long getRowIndex() {
		return 0;
	}

	public int getStartingGroupLevel() throws BirtException {
		return 0;
	}

	public String getString(String name) throws BirtException {
		return null;
	}

	public Object getValue(String name) throws BirtException {
		return null;
	}

	public boolean isEmpty() throws BirtException {
		return hasNext;
	}

	public boolean next() throws BirtException {
		if (hasNext) {
			hasNext = false;
			return true;
		}
		return false;
	}

	public boolean skipTo(long rowIndex) throws BirtException {
		return false;
	}

	public void close() {
		if (rset != null) {
			rset.close();
		}
	}

	public Object evaluate(String expr) throws BirtException {
		return null;
	}

	public Object evaluate(String language, String expr) throws BirtException {
		return null;
	}

	public Object evaluate(IBaseExpression expr) throws BirtException {
		return null;
	}

	public DataSetID getID() {
		return null;
	}

	public IBaseResultSet getParent() {
		return null;
	}

	public IBaseQueryResults getQueryResults() {
		return null;
	}

	public String getRawID() throws BirtException {
		return null;
	}

	public int getType() {
		return IBaseResultSet.QUERY_RESULTSET;
	}

	public boolean isFirst() {
		return hasNext;
	}

	public boolean isBeforeFirst() {
		return false;
	}

	public String getQueryResultsID() {
		if (rset instanceof QueryResultSet)
			return ((QueryResultSet) rset).getQueryResultsID();
		return null;
	}
}
