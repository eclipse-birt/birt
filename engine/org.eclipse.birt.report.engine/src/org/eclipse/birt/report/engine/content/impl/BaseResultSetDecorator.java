/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.content.impl;

import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.data.dte.CubeResultSet;
import org.eclipse.birt.report.engine.data.dte.QueryResultSet;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;

public class BaseResultSetDecorator implements IBaseResultSet {
	private List<IBaseResultSet> resultSets;

	private IBaseResultSet lastResultSet;

	public BaseResultSetDecorator(List<IBaseResultSet> resultSets) {
		assert (resultSets.size() > 0);
		this.resultSets = resultSets;
		lastResultSet = resultSets.get(resultSets.size() - 1);
	}

	public void close() {
		for (IBaseResultSet resultSet : resultSets) {
			resultSet.close();
		}
	}

	public Object evaluate(String expr) throws BirtException {
		throw new UnsupportedOperationException();
	}

	public Object evaluate(String language, String expr) throws BirtException {
		throw new UnsupportedOperationException();
	}

	public Object evaluate(IBaseExpression expr) throws BirtException {
		throw new UnsupportedOperationException();
	}

	public DataSetID getID() {
		return lastResultSet.getID();
	}

	public IBaseResultSet getParent() {
		return lastResultSet.getParent();
	}

	public IBaseQueryResults getQueryResults() {
		return lastResultSet.getQueryResults();
	}

	public String getRawID() throws BirtException {
		return lastResultSet.getRawID();
	}

	public int getType() {
		return lastResultSet.getType();
	}

	public String getResultSetId() {
		if (lastResultSet instanceof QueryResultSet) {
			QueryResultSet queryResultSet = (QueryResultSet) lastResultSet;
			return queryResultSet.getQueryResultsID();
		} else {
			CubeResultSet cubeResultSet = (CubeResultSet) lastResultSet;
			return cubeResultSet.getQueryResultsID();
		}
	}
}
