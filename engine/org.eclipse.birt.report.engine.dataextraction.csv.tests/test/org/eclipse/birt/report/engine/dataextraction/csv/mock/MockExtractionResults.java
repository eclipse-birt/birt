/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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
package org.eclipse.birt.report.engine.dataextraction.csv.mock;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IDataIterator;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.IResultMetaData;

public class MockExtractionResults implements IExtractionResults {
	private boolean closed;
	private MockDataIterator iterator;
	private MockResultMetaData metaData;

	public MockExtractionResults(String[] columnNames, int[] columnTypes, Object[][] data) {
		closed = false;
		metaData = new MockResultMetaData(columnNames, columnTypes);
		iterator = new MockDataIterator(columnNames, data, this);
	}

	public void close() {
		closed = true;
		iterator.close();
	}

	private void assertOpened() throws BirtException {
		if (closed) {
			throw new BirtException(null, "Result set is already closed.", null);
		}
	}

	public IResultMetaData getResultMetaData() throws BirtException {
		return metaData;
	}

	public IDataIterator nextResultIterator() throws BirtException {
		assertOpened();
		return iterator;
	}

}
