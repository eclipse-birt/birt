/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.report.engine.layout.html.buffer;

import java.util.LinkedList;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.layout.html.HTMLLayoutContext;

public class PageBufferFactory {
	protected HTMLLayoutContext context;
	protected LinkedList<IPageBuffer> pages = new LinkedList<IPageBuffer>();

	public PageBufferFactory(HTMLLayoutContext context) {
		this.context = context;
	}

	public IPageBuffer createBuffer() {
		IPageBuffer buffer = null;
		if (pages.isEmpty()) {
			buffer = new HTMLPageBuffer(context);
		} else {
			buffer = new CachedHTMLPageBuffer(context, true);
		}
		pages.addLast(buffer);
		return buffer;
	}

	public void refresh() throws BirtException {
		while (!pages.isEmpty()) {
			IPageBuffer buffer = pages.getFirst();
			if (buffer.finished()) {
				buffer.flush();
				pages.removeFirst();
			} else {
				break;
			}
		}

	}

	public void close() throws BirtException {
		while (!pages.isEmpty()) {
			IPageBuffer buffer = pages.getFirst();
			buffer.flush();
			pages.removeFirst();
		}
	}

}
