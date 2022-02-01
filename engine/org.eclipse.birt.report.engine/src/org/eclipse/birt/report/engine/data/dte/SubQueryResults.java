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

	public void close() throws BirtException {
		// we needn't close the rs as the creator of this object
		// will close the rs.
	}

	public IPreparedQuery getPreparedQuery() {
		return null;
	}

	public IResultIterator getResultIterator() throws BirtException {
		return rs;
	}

	public IResultMetaData getResultMetaData() throws BirtException {
		if (rs != null) {
			return rs.getResultMetaData();
		}
		return null;
	}

	public String getID() {
		return null;
	}

	public void cancel() {
		// TODO Auto-generated method stub

	}

	public String getName() {
		throw new UnsupportedOperationException();
	}

	public void setName(String name) {
		throw new UnsupportedOperationException();
	}
}
