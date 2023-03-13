/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.ICubeExtractionResults;
import org.eclipse.birt.report.engine.api.IDataIterator;
import org.eclipse.birt.report.engine.api.IResultMetaData;
import org.eclipse.birt.report.model.api.DesignElementHandle;

/**
 *
 *
 */
public class CubeExtractionResults implements ICubeExtractionResults {

	private IDocArchiveReader reportDocReader;

	/**
	 *
	 * @param reportDocReader
	 */
	public CubeExtractionResults(IDocArchiveReader reportDocReader) {
		this.reportDocReader = reportDocReader;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.ICubeExtractionResults#getReportDocReader
	 * ()
	 */
	@Override
	public IDocArchiveReader getReportDocReader() {
		return reportDocReader;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.IExtractionResults#close()
	 */
	@Override
	public void close() {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.IExtractionResults#getResultMetaData()
	 */
	@Override
	public IResultMetaData getResultMetaData() throws BirtException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.IExtractionResults#nextResultIterator
	 * ()
	 */
	@Override
	public IDataIterator nextResultIterator() throws BirtException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.IExtractionResults#getHandle ()
	 */
	public DesignElementHandle getHandle() {
		throw new UnsupportedOperationException();
	}
}
