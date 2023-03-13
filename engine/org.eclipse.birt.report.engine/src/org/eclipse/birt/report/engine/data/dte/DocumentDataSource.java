/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

import java.io.IOException;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.report.engine.api.InstanceID;

public class DocumentDataSource {

	IDocArchiveReader dataSource;
	InstanceID iid;
	String bookmark;

	public DocumentDataSource(IDocArchiveReader dataSource) {
		this(dataSource, null, null);
	}

	public DocumentDataSource(IDocArchiveReader dataSource, String bookmark, InstanceID iid) {
		this.dataSource = dataSource;
		this.bookmark = bookmark;
		if (iid != null) {
			this.iid = iid;
		}
	}

	public boolean isReportletDocument() {
		return bookmark != null && iid != null;
	}

	public void open() throws IOException {
		dataSource.open();
	}

	public void close() throws IOException {
		dataSource.close();
	}

	public IDocArchiveReader getDataSource() {
		return dataSource;
	}

	public InstanceID getInstanceID() {
		return iid;
	}

	public long getElementID() {
		if (iid != null) {
			return iid.getComponentID();
		}
		return -1;
	}

	public String getBookmark() {
		return bookmark;
	}
}
