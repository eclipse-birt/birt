/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.internal.document;

import java.io.IOException;
import java.util.Collection;

import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.executor.PageVariable;
import org.eclipse.birt.report.engine.internal.document.v1.PageHintReaderV1;
import org.eclipse.birt.report.engine.internal.document.v2.PageHintReaderV2;
import org.eclipse.birt.report.engine.internal.document.v3.PageHintReaderV3;
import org.eclipse.birt.report.engine.internal.document.v4.FixedLayoutPageHintReader;
import org.eclipse.birt.report.engine.presentation.IPageHint;

/**
 * page hint reader
 * 
 * It can support multiple versions.
 * 
 */
public class PageHintReader implements IPageHintReader {

	IPageHintReader reader;
	IPageHint cachedHint;

	public PageHintReader(IReportDocument document) throws IOException {
		String version = document.getProperty(ReportDocumentConstants.PAGE_HINT_VERSION_KEY);

		if (ReportDocumentConstants.PAGE_HINT_VERSION_1.equals(version)) {
			this.reader = new PageHintReaderV1(document);
		} else if (ReportDocumentConstants.PAGE_HINT_VERSION_2.equals(version)) {
			this.reader = new PageHintReaderV2(document.getArchive());
		} else if (ReportDocumentConstants.PAGE_HINT_VERSION_FIXED_LAYOUT.equals(version)) {
			this.reader = new FixedLayoutPageHintReader(document.getArchive());
		} else {
			this.reader = new PageHintReaderV3(document.getArchive());
		}
	}

	public int getVersion() {
		return reader.getVersion();
	}

	public void close() {
		reader.close();
	}

	public long getTotalPage() throws IOException {
		return reader.getTotalPage();
	}

	public Collection<PageVariable> getPageVariables() throws IOException {
		return reader.getPageVariables();
	}

	public IPageHint getPageHint(long pageNumber) throws IOException {
		if (cachedHint != null && cachedHint.getPageNumber() == pageNumber) {
			return cachedHint;
		}
		cachedHint = reader.getPageHint(pageNumber);
		return cachedHint;
	}

	public long getPageOffset(long pageNumber, String masterPage) throws IOException {
		return reader.getPageOffset(pageNumber, masterPage);
	}

}
