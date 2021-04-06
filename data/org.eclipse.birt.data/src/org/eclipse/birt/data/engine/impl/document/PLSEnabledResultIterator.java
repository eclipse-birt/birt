
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
package org.eclipse.birt.data.engine.impl.document;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IGroupInstanceInfo;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.mozilla.javascript.Scriptable;

/**
 * This class take a document result iterator and a list of IGroupInstanceInfo
 * as input. It will then wrap the document result iterator so that only the
 * rows as defined in IGroupInstanceInfo list will be retrivable by user.
 */

public class PLSEnabledResultIterator extends PLSDataPopulator implements IResultIterator {
	/**
	 * Constructor.
	 * 
	 * @param targetGroups
	 * @param docIt
	 * @throws DataException
	 */
	PLSEnabledResultIterator(List<IGroupInstanceInfo> targetGroups, ResultIterator docIt) throws DataException {
		super(targetGroups, docIt);
	}

	public void close() throws BirtException {
		this.docIt.close();
	}

	public boolean findGroup(Object[] groupKeyValues) throws BirtException {
		return this.docIt.findGroup(groupKeyValues);
	}

	public BigDecimal getBigDecimal(String name) throws BirtException {
		return this.docIt.getBigDecimal(name);
	}

	public Blob getBlob(String name) throws BirtException {
		return this.docIt.getBlob(name);
	}

	public Boolean getBoolean(String name) throws BirtException {
		return this.docIt.getBoolean(name);
	}

	public byte[] getBytes(String name) throws BirtException {
		return this.docIt.getBytes(name);
	}

	public Date getDate(String name) throws BirtException {
		return this.docIt.getDate(name);
	}

	public Double getDouble(String name) throws BirtException {
		return this.docIt.getDouble(name);
	}

	public int getEndingGroupLevel() throws BirtException {
		if (this.currentBoundary != null) {
			if (this.docIt.getExprResultSet().getCurrentIndex() == this.currentBoundary.getEnd())
				return this.currentBoundary.endGroupLevel;
			else
				return this.docIt.getEndingGroupLevel();
		}
		throw new DataException(ResourceConstants.RESULT_SET_EMPTY);
	}

	public Integer getInteger(String name) throws BirtException {
		return this.docIt.getInteger(name);
	}

	public IQueryResults getQueryResults() {
		return this.docIt.getQueryResults();
	}

	public IResultMetaData getResultMetaData() throws BirtException {
		return this.docIt.getResultMetaData();
	}

	public int getRowId() throws BirtException {
		return this.docIt.getRowId();
	}

	public int getRowIndex() throws BirtException {
		return this.rowIndex;
	}

	public Scriptable getScope() {
		return this.docIt.getScope();
	}

	public IResultIterator getSecondaryIterator(String subQueryName, Scriptable scope) throws BirtException {
		return this.docIt.getSecondaryIterator(subQueryName, scope);
	}

	public IResultIterator getSecondaryIterator(ScriptContext context, String subQueryName) throws BirtException {
		return this.docIt.getSecondaryIterator(context, subQueryName);
	}

	public int getStartingGroupLevel() throws BirtException {
		if (this.currentBoundary != null) {
			if (this.docIt.getExprResultSet().getCurrentIndex() == this.currentBoundary.getStart())
				return this.currentBoundary.startGroupLevel;
			else
				return this.docIt.getStartingGroupLevel();
		}
		throw new DataException(ResourceConstants.RESULT_SET_EMPTY);
	}

	public String getString(String name) throws BirtException {
		return this.docIt.getString(name);
	}

	public Object getValue(String name) throws BirtException {
		return this.docIt.getValue(name);
	}

	public boolean isEmpty() throws BirtException {
		return this.isEmpty;
	}

	public void moveTo(int rowIndex) throws BirtException {
		while (this.rowIndex < rowIndex && this.next()) {
		}
	}

	public void skipToEnd(int groupLevel) throws BirtException {
		this.docIt.skipToEnd(groupLevel);

	}

	public boolean isBeforeFirst() throws BirtException {
		return !isEmpty() && rowIndex == -1;
	}

	public boolean isFirst() throws BirtException {
		return !isEmpty() && rowIndex == 0;
	}
}
