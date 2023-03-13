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

package org.eclipse.birt.report.engine.data.dte;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;

public class SubQueryResults implements IQueryResults {

	IResultIterator rs;

	public SubQueryResults(IResultIterator rs) {
		this.rs = rs;
	}

	@Override
	public void close() throws BirtException {
		// we needn't close the rs as the creator of this object
		// will close the rs.
	}

	@Override
	public IPreparedQuery getPreparedQuery() {
		return null;
	}

	@Override
	public IResultIterator getResultIterator() throws BirtException {
		return rs;
	}

	@Override
	public IResultMetaData getResultMetaData() throws BirtException {
		if (rs != null) {
			return rs.getResultMetaData();
		}
		return null;
	}

	@Override
	public String getID() {
		return null;
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setName(String name) {
		throw new UnsupportedOperationException();
	}
}
