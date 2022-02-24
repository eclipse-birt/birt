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

package org.eclipse.birt.report.item.crosstab.core.re.executor;

import java.util.ArrayList;
import java.util.List;

import javax.olap.OLAPException;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;

/**
 * CachedColumnWalker
 */
class CachedColumnWalker implements IColumnWalker {

	private ColumnWalker worker;
	private boolean cacheMode;
	private List cache;
	private int pos;

	CachedColumnWalker(CrosstabReportItemHandle item, EdgeCursor columnEdgeCursor) {
		worker = new ColumnWalker(item, columnEdgeCursor);

		cache = new ArrayList();
		cacheMode = false;
	}

	public void reload() {
		cacheMode = true;
		pos = 0;
	}

	public boolean hasNext() throws OLAPException {
		if (cacheMode) {
			return pos < cache.size();
		} else {
			return worker.hasNext();
		}
	}

	public ColumnEvent next() throws OLAPException {
		ColumnEvent ev;

		if (cacheMode) {
			ev = (ColumnEvent) cache.get(pos++);
		} else {
			ev = worker.next();
			cache.add(ev);
		}

		return ev;
	}
}
