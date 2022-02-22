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

package org.eclipse.birt.report.engine.presentation;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.birt.report.engine.api.IPageHandler;
import org.eclipse.birt.report.engine.api.IReportDocumentInfo;

public class CompositePageHandler implements IPageHandler {

	ArrayList handlers = new ArrayList();

	public void addHandler(IPageHandler handler) {
		handlers.add(handler);
	}

	public void removeHandler(IPageHandler handler) {
		handlers.remove(handler);
	}

	@Override
	public void onPage(int pageNumber, boolean checkpoint, IReportDocumentInfo doc) {
		Iterator iter = handlers.iterator();
		while (iter.hasNext()) {
			IPageHandler handler = (IPageHandler) iter.next();
			handler.onPage(pageNumber, checkpoint, doc);
		}

	}

}
